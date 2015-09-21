/*
 * @Id: ICBC.java 下午02:09:56 2009-7-6
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.b2b;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.Security;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import cn.com.infosec.jce.provider.InfosecProvider;


import ebank.core.STEngineService;
import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;


/**
 * @author xiexh
 * Description: ICBC(B2B)
 * 
 */
public class ICBC extends BankExt implements BankService{
	private String fileKey;
	private String fileCert;
	private String publicCert;
	private String merchantaccount;
	private String payeeaccount;
	private String payeename;
	
	private STEngineService engineService;
	
	
	private Logger log= Logger.getLogger(this.getClass());

	@SuppressWarnings("rawtypes")
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		//初始化resultbean
		PayResult bean=null;
		try{
			String amount   =String.valueOf(reqs.get("ContractAmt"));    // 订单金额
			String orderNum =String.valueOf(reqs.get("ContractNo"));
			String tranStat =String.valueOf(reqs.get("PayStatusZHCN"));
			if(this.verfiy(reqs)==0){
				amount=exchange(amount,-2);
				//transStat 0
				//0----成功	1----失败 2----可疑交易 3----等待授权
				bean=new PayResult(orderNum,this.bankcode,new BigDecimal(amount),"0".equals(tranStat)?1:-1);	
				String TranTime= String.valueOf(reqs.get("TranTime"));
				if(!"".equals(TranTime)&&TranTime.length()>8){
					TranTime = TranTime.substring(0, 8);
				}
				bean.setBankdate(TranTime);
				bean.setBankresult(tranStat);
				bean.setBanktransseq(String.valueOf(reqs.get("Serial_no")));
			}else{
				throw new ServiceException(EventCode.ICBC_SIGNVERIFY);
			}
		}catch(Exception e){
			HandleException.handle(e);
		}
		return bean;
	}
	/***
	 * 得到接收验证URL
	 * @return String
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	public String getRcvURL(HashMap reqs){
		String strOutput="notfrombank";
		try{
			String state="";
			PayResult result=this.getPayResult(reqs);
			if(result!=null&&result.getTrxsts()==1){
				strOutput="CBMD5";
				state=result.getTrxsts()==1?Constants.STATE_STR_OK:Constants.STATE_STR_FAIL;
				engineService.post(result);	
			}
			if("3".equals(result.getBankresult())){
				state=Constants.STATE_STR_HANDING;//等待授权状态
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this.recurl+(this.recurl.indexOf("?")>0?"&":"?")+"v_md5info="+strOutput;
	}
	/***
	 * 按照要求产生金额字符串
	 * @param order
	 * @return
	 */
	private String exchange(String amount,int rate){
		BigDecimal big = new BigDecimal(amount);
		BigDecimal big2;
		if(rate>0)
		   big2=big.movePointRight(rate);
		else
		   big2=big.movePointLeft(Math.abs(rate));
		return String.valueOf(big2);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		String plain="APIName=B2B&APIVersion=001.001.001.001&Shop_code="+this.corpid+"&MerchantURL="+this.getRecurl()+
		"&ContractNo="+order.getRandOrderID()+"&ContractAmt="+exchange(order.getAmount(),2)+"&Account_cur=001&JoinFlag=2&SendType=0"+
		"&TranTime="+order.getPostdate()+"&Shop_acc_num="+merchantaccount+"&PayeeAcct="+payeeaccount;
		
		HashMap mp=new HashMap();
		mp.put("APIName", "B2B");
		mp.put("APIVersion", "001.001.001.001");
		mp.put("Shop_code", this.getCorpid());
		mp.put("MerchantURL", this.getRecurl());
		mp.put("ContractNo", order.getRandOrderID());
		mp.put("ContractAmt", exchange(order.getAmount(),2));
		mp.put("Account_cur", "001");
		mp.put("JoinFlag", "2");
		mp.put("Mer_Icbc20_signstr", new String(setEncMsg(plain)));
		mp.put("Cert", this.getCert());
		mp.put("SendType", "0"); //0 All result 1.only success
		mp.put("TranTime", order.getPostdate());
		mp.put("Shop_acc_num", merchantaccount); //商城账号
		mp.put("PayeeAcct", payeeaccount);    //收款账号
		mp.put("GoodsCode", "B2B");		
		mp.put("GoodsName", "B2B"); //
		mp.put("Amount", "1"); //order amount
		mp.put("TransFee", "1");
		mp.put("ShopRemark", "1");
		mp.put("ShopRem", "1");
		mp.put("PayeeName", "");
		StringBuffer sf=new StringBuffer("");		
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		for (Iterator iterator = mp.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			sf.append("<input type=\"hidden\" name=\""+type+"\" value=\""+mp.get(type)+"\"/>");			
		}
		sf.append("</form>");		
		return sf.toString();
	}
	static{
		Security.addProvider(new InfosecProvider());		
	}
	private String decode(Object reqs){
		try{
			if(reqs==null) return "";
			else return URLDecoder.decode(String.valueOf(reqs),"GB2312");
			}catch(Exception ex){
				ex.printStackTrace();
				return "";
			}
		
	}	
	
	@SuppressWarnings("rawtypes")
	private int verfiy(HashMap reqs){		   
			try{		    
			    String APIName=this.decode(reqs.get("APIName"));			    
		        String APIVersion=this.decode(reqs.get("APIVersion"));
		        String Shop_code          =this.decode(reqs.get("Shop_code"));       //商户编号
		        String MerchantURL    =this.decode(reqs.get("MerchantURL"));		        
		        String Serial_no       =this.decode(reqs.get("Serial_no"));
		        
		        String PayStatusZHCN  = this.decode(reqs.get("PayStatusZHCN"));
				String TranErrorCode         = this.decode(reqs.get("TranErrorCode"));    
				String TranErrorMsg        = this.decode(reqs.get("TranErrorMsg")); 						
				String ContractNo        =this.decode(reqs.get("ContractNo"));   
				
				String ContractAmt =this.decode(reqs.get("ContractAmt"));   
				String Account_cur       =this.decode(reqs.get("Account_cur"));   
				String JoinFlag        =this.decode(reqs.get("JoinFlag"));   
				String ShopJoinFlag     =this.decode(reqs.get("ShopJoinFlag"));  
				String CustJoinFlag      =this.decode(reqs.get("CustJoinFlag"));   
				String CustJoinNumber     =this.decode(reqs.get("CustJoinNumber"));  
				
				String SendType       =this.decode(reqs.get("SendType"));   
				String TranTime        =this.decode(reqs.get("TranTime"));   
				String NotifyTime        =this.decode(reqs.get("NotifyTime")); 
				
				String Shop_acc_num        =this.decode(reqs.get("Shop_acc_num")); 
				String PayeeAcct        =this.decode(reqs.get("PayeeAcct")); 
				String PayeeName        =this.decode(reqs.get("PayeeName")); 		
				
				final String payeename="天津荣程网络科技有限公司";
				
				log.error("PayeeName equals:"+payeename.equals(PayeeName));
				
				String signMsg        =this.decode(reqs.get("NotifySign"));				
				
				String validate_str="APIName="+APIName+"&APIVersion="+APIVersion+
				                    "&Shop_code="+Shop_code+"&MerchantURL="+MerchantURL+
				                    "&Serial_no="+Serial_no+"&PayStatusZHCN="+PayStatusZHCN+
				                    "&TranErrorCode="+TranErrorCode+"&TranErrorMsg="+TranErrorMsg+
				                    "&ContractNo="+ContractNo+"&ContractAmt="+ContractAmt+
				                    "&Account_cur="+Account_cur+"&JoinFlag="+JoinFlag+"&ShopJoinFlag="+ShopJoinFlag+
				                    "&CustJoinFlag="+CustJoinFlag+"&CustJoinNumber="+CustJoinNumber+
				                    "&SendType="+SendType+"&TranTime="+TranTime+"&NotifyTime="+NotifyTime+
				                    "&Shop_acc_num="+Shop_acc_num+"&PayeeAcct="+PayeeAcct+"&PayeeName="+payeename;
				log.info(" ["+validate_str+"]"+signMsg);						
				signMsg=signMsg.replaceAll(" ","+"); //add							
				return  verifyMsg(validate_str,signMsg.getBytes());		
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return -1;
			
		
	}
	private String getCert(){		
	  	byte[] EncCertID = null;
	    FileInputStream fis = null;
	    try{
	      //读取证书
	    	fis = new FileInputStream(getFileCert());
	    	byte[] cert = new byte[fis.available()];
	    	fis.read(cert);
	    	fis.close();
	    	//提交：调用BASE64编码API对产生的签名数据编码(签名组)
	    	EncCertID = cn.com.infosec.icbc.ReturnValue.base64enc(cert);
		}catch (Exception e){
			log.debug("fileCert:"+getFileCert());
			e.printStackTrace();
	    }
	    return new String(EncCertID);
		
	}	
	
	private byte[] setEncMsg(String strMsg) {
		    byte[] src = strMsg.getBytes();
		    byte[] encodedMsg = null;
		    try{
		    	if(log.isDebugEnabled()) log.debug("filekey="+this.fileKey+" keypss="+keyPassword);
		    	//私钥
		    	FileInputStream fis = new FileInputStream(this.fileKey);
		    	byte[] privateKey = new byte[fis.available()];
		    	fis.read(privateKey);
		    	fis.close();
		    	//私钥密码（银行提供）
		    	char[] keyPass = this.keyPassword.toCharArray();
		    	//提交1：调用签名接口对数据签名(签名组、签名组长度、私钥、私钥密码)
		    	byte[] signature = cn.com.infosec.icbc.ReturnValue.sign(src, src.length, privateKey, keyPass);
		    	//提交2：调用BASE64编码API对产生的签名数据编码(签名组)
		    	encodedMsg = cn.com.infosec.icbc.ReturnValue.base64enc(signature);
		    }catch (Exception ex){	    	
		    	log.info(ex);
		    }
		    return encodedMsg;
	}
	
	private int verifyMsg(String strMsg, byte[] signMsg) {
		    int iJg = 1;		    
		    byte[] src =null;
		    try {
		    	src=strMsg.getBytes("GBK");;
		    	//对签名数据进行BASE64解码
		    	byte[] sign = cn.com.infosec.icbc.ReturnValue.base64dec(signMsg);		    	
		    	//公钥
		    	FileInputStream fis = new FileInputStream(this.publicCert);
		    	byte[] cert = new byte[fis.available()];
//		    	fis.skip(3);
		    	fis.read(cert);
		    	fis.close();		    	
		    	//验证BASE64解码后得到的签名信息
		    	log.info("src_length"+src.length+" sign_length:"+sign.length);
		    	iJg = cn.com.infosec.icbc.ReturnValue.verifySign(src, src.length, cert, sign);
		    	//0表示成功，其他的表示失败
		    }catch (Exception ex){		    	
		    	ex.printStackTrace();
		    }
		    return iJg;
		   
	}
	
	/**
	 * Test
	 * @param strMsg
	 * @param signMsg
	 * @param keypath
	 * @return
	 */
	private int verifyMsg_test(byte[] src, byte[] signMsg,String keypath) {
	    int iJg = 1;	    
	   
	    try {
	    	//对签名数据进行BASE64解码
	    	byte[] sign = cn.com.infosec.icbc.ReturnValue.base64dec(signMsg);		    	
	    	//公钥
	    	FileInputStream fis = new FileInputStream(keypath);
	    	byte[] cert = new byte[fis.available()];
//	    	fis.skip(3);
	    	fis.read(cert);
	    	fis.close();		    	
	    	//验证BASE64解码后得到的签名信息
	    	log.info("src_length"+src.length+" sign_length:"+sign.length);
	    	iJg = cn.com.infosec.icbc.ReturnValue.verifySign(src, src.length, cert, sign);
	    	//0表示成功，其他的表示失败
	    }catch (Exception ex){		    	
	    	ex.printStackTrace();
	    }
	    return iJg;
	   
}

	public String getFileKey() {
		return fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	public String getFileCert() {
		return fileCert;
	}

	public void setFileCert(String fileCert) {
		this.fileCert = fileCert;
	}
	public String getPublicCert() {
		return publicCert;
	}

	public void setPublicCert(String publicCert) {
		this.publicCert = publicCert;
	}

	public void setMerchantaccount(String merchantaccount) {
		this.merchantaccount = merchantaccount;
	}

	public void setPayeeaccount(String payeeaccount) {
		this.payeeaccount = payeeaccount;
	}
	public void setEngineService(STEngineService engineService) {
		this.engineService = engineService;
	}
	public void setPayeename(String payeename) {
		this.payeename = payeename;
	}
	public String getPayeename() {
		return payeename;
	}
	
	public static void main(String[] args) {
		ICBC icbc=new ICBC();
		for (int i = 0; i < args.length; i++) {
			System.out.println("arg["+i+"]"+args[i]);
		}
		
		try {
			String plain="APIName=B2B&APIVersion=001.001.001.001&Shop_code=0302EC13434186&MerchantURL=http://epay.gicard.net.cn/PayRecGBK?idx=9102&Serial_no=HFK302304777&PayStatusZHCN=0&TranErrorCode=0&TranErrorMsg=&ContractNo=621202080031074356&ContractAmt=1&Account_cur=001&JoinFlag=2&ShopJoinFlag=&CustJoinFlag=&CustJoinNumber=&SendType=0&TranTime=&NotifyTime=20120209165200&Shop_acc_num=0302009719300608539&PayeeAcct=0302009719300608539&PayeeName=荣程网络技术有限公司";
			String sign="dxJHgO2+J021E21Wf/4+q1iqDAiC4z0BrLw+fcy4OwQv07tmqauBw0htqhPlLYbven4TCXW3DtEIDmXBCnpk0LFQoCRy7yrlDhFZsuPYnYbFfodLoRJOrg2EaKWfnTcXACn3Is+qdRVHERJLECzgG26+JCwnC6J/yJ3z4UWvQ30=";
			if(args.length==1)
				System.out.println(args[0]+" static validate:"+icbc.verifyMsg_test(plain.getBytes(args[0]), sign.getBytes(), "/home/production/resource/keys.production/icbc_b2b/icbcpublic.crt"));
			else{
				System.out.println("static validate:"+icbc.verifyMsg_test(plain.getBytes(), sign.getBytes(), "/home/production/resource/keys.production/icbc_b2b/icbcpublic.crt"));
			}	
			if(args.length==4)
				System.out.println(icbc.verifyMsg_test(args[0].getBytes(args[3]), args[1].getBytes(args[3]),args[2]));
			else{
				System.out.println(icbc.verifyMsg_test(args[0].getBytes(), args[1].getBytes(),args[2]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
