/*
 * @Id: ICBC_V1000.java 10:28:57 2006-6-15
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;

import java.security.Security;
import java.util.HashMap;

import org.apache.log4j.Logger;


import cn.com.infosec.jce.provider.InfosecProvider;
import ebank.core.STEngineService;
import ebank.core.bank.BankService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;


/**
 * @author 
 * Description: 工商银行新版V1.0.0.0接口
 * 
 */
public class ICBC_B2C extends BankExt implements BankService{
	private Logger log= Logger.getLogger(ICBC_B2C.class);
	private String icbc_name="ICBC_PERBANK_B2C";
	private String icbc_version="1.0.0.0";
	private String verifyJoinFlag="0";
	
	private String fileKey;
	private String fileCert;
	private String keyPassword;
	private String publicCert;	
	private String merchantaccount;
	private STEngineService engineService;
	
	static{
		Security.addProvider(new InfosecProvider());		
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
	/* (non-Javadoc)
	 * @see com.chinabank.core.bank.BankService#getPayResult(java.util.HashMap)
	 */
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		
		//初始话resultbean
		PayResult bean=null;
	
		try{
			String amount   =String.valueOf(reqs.get("amount"));    // 订单金额
			String orderNum =String.valueOf(reqs.get("orderid"));
			String tranStat =String.valueOf(reqs.get("tranStat"));												
			if(this.verfiy(reqs)==0){
				BigDecimal bamount=new BigDecimal(amount);
				amount=bamount.movePointLeft(2).toString();	
				//transStat 0
				//1-“交易成功，已清算”；2-“交易失败”；3-“交易可疑”
				bean=new PayResult(orderNum,this.bankcode,new BigDecimal(amount),"1".equals(tranStat)?1:-1);				
				bean.setBankresult("0");
				bean.setBankdate(String.valueOf(reqs.get("notifyDate")));				
				bean.setCurrency("001".equals(String.valueOf(reqs.get("curType")))?"CNY":String.valueOf(reqs.get("curType")));				
				bean.setBankresult(tranStat);
				bean.setBanktransseq(String.valueOf(reqs.get("TranSerialNo")));				
			}else{
				throw new ServiceException(EventCode.ICBC_SIGNVERIFY);
			}			
		}catch(Exception e){
			HandleException.handle(e);
		}
		return bean;			
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
	
	private int verfiy(HashMap reqs){		   
			try{		    
			    String interfaceName=this.decode(reqs.get("interfaceName"));			    
		        String interfaceVersion=this.decode(reqs.get("interfaceVersion"));        
		        String orderNum       =this.decode(reqs.get("orderid"));
		        String tranSerialNum  = this.decode(reqs.get("TranSerialNo"));
				String amount         = this.decode(reqs.get("amount"));    // 订单金额    
				String curType        = this.decode(reqs.get("curType")); //币种代码		
				String merID          =this.decode(reqs.get("merID"));       //商户编号		
				String merAcct        =this.decode(reqs.get("merAcct"));   
				String verifyJoinFlag =this.decode(reqs.get("verifyJoinFlag"));   
				String JoinFlag       =this.decode(reqs.get("JoinFlag"));   
				String UserNum        =this.decode(reqs.get("UserNum"));   
				String resultType     =this.decode(reqs.get("resultType"));  
				String orderDate      =this.decode(reqs.get("orderDate"));   
				String notifyDate     =this.decode(reqs.get("notifyDate"));  
				String tranStat       =this.decode(reqs.get("tranStat"));   
				String comment        =this.decode(reqs.get("comment"));   
				String remark1        =this.decode(reqs.get("remark1"));  
				String remark2        =this.decode(reqs.get("remark2")); 				
				String signMsg        =this.decode(reqs.get("signMsg"));				
				
				String validate_str="interfaceName="+interfaceName+"&interfaceVersion="+interfaceVersion+
				                    "&orderid="+orderNum+"&TranSerialNo="+tranSerialNum+
				                    "&amount="+amount+"&curType="+curType+
				                    "&merID="+merID+"&merAcct="+merAcct+
				                    "&verifyJoinFlag="+verifyJoinFlag+"&JoinFlag="+JoinFlag+
				                    "&UserNum="+UserNum+"&resultType="+resultType+"&orderDate="+orderDate+
				                    "&notifyDate="+notifyDate+"&tranStat="+tranStat+
				                    "&comment="+comment+"&remark1="+remark1+"&remark2="+remark2;
										
				signMsg=signMsg.replaceAll(" ","+"); //add							
				return  verifyMsg(validate_str,signMsg.getBytes());		
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return -1;
			
		
	}

	/***
	 * 得到接收验证URL
	 * @return String
	 */
	public String getRcvURL(HashMap reqs){
		String strOutput="notfrombank";		
		try{		
			PayResult result=this.getPayResult(reqs);
			if(result!=null&&result.getTrxsts()==1){
				strOutput="CBMD5";								
				engineService.post(result);	
				}			
		}catch(Exception e){
			e.printStackTrace();
		}
		return this.getRecurl()+(this.getRecurl().indexOf("?")>0?"&":"?")+"v_md5info="+strOutput;
	}
	/* (non-Javadoc)
	 * @see com.chinabank.core.bank.BankService#sendOrderToBank(com.chinabank.core.domain.BankOrder)
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		StringBuffer sf=new StringBuffer("");		
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" name=\"interfaceName\" value=\""+this.icbc_name+"\">");
		sf.append("<input type=\"hidden\" name=\"interfaceVersion\" value=\""+this.icbc_version+"\">");
		sf.append("<input type=\"hidden\" name=\"orderid\" value=\""+order.getRandOrderID()+"\">");
		sf.append("<input type=\"hidden\" name=\"amount\" value=\""+getAmount(order)+"\">");
		sf.append("<input type=\"hidden\" name=\"curType\" value=\""+getCurType(order.getCurrency())+"\">");		
		sf.append("<input type=\"hidden\" name=\"merID\" value=\""+this.getCorpid()+"\">");
		sf.append("<input type=\"hidden\" name=\"merAcct\" value=\""+this.merchantaccount+"\">");
		sf.append("<input type=\"hidden\" name=\"verifyJoinFlag\" value=\""+this.verifyJoinFlag+"\">"); //联名标识
		sf.append("<input type=\"hidden\" name=\"notifyType\" value=\"HS\">"); //通知方式,立即通知
		sf.append("<input type=\"hidden\" name=\"merURL\" value=\""+this.recurl+"\">");
		sf.append("<input type=\"hidden\" name=\"resultType\" value=\"0\">");
		if("ICBC_MOBILE_B2C".equals(this.icbc_name))
		sf.append("<input type=\"hidden\" name=\"mobile\" value=\"\">");
		sf.append("<input type=\"hidden\" name=\"goodsID\" value=\"**\">");
		sf.append("<input type=\"hidden\" name=\"goodsName\" value=\"B2C支付\">");
		sf.append("<input type=\"hidden\" name=\"goodsNum\" value=\"\">");
		sf.append("<input type=\"hidden\" name=\"carriageAmt\" value=\"\">");		
		sf.append("<input type=\"hidden\" name=\"merHint\" value=\"我的订单 ["+order.getOrdernum()+"]\">");		
		sf.append("<input type=\"hidden\" name=\"orderDate\" value=\""+order.getPostdate()+"\">");			
		sf.append("<input type=\"hidden\" name=\"merSignMsg\" value=\""+generateSignMsg(order,order.getPostdate())+"\">"); //签名数据
		sf.append("<input type=\"hidden\" name=\"merCert\" value=\""+this.getCert()+"\">");	
		sf.append("<input type=\"hidden\" name=\"remark1\" value=\"\">");
		sf.append("<input type=\"hidden\" name=\"remark2\" value=\"\">");
		sf.append("</form>");					
		if(log.isDebugEnabled()) log.debug(sf.toString());
		return sf.toString();
	}
	/***
	 * 按照要求产生金额字符串
	 * @param order
	 * @return
	 */
	private String getAmount(BankOrder order){
		//工商银行结算以分为主
		BigDecimal big = new BigDecimal(order.getAmount());
		BigDecimal big2=big.multiply(new BigDecimal("100"));
		int v_amount = big2.intValue();
		big=null;
		big2=null;
		return String.valueOf(v_amount);
	}
	/***
	 * 得到当前支付货币类型
	 * @param moneyType
	 * @return
	 */
	private String getCurType(String moneyType){
		String curType_icbc="";
		if (moneyType == "0") {
			curType_icbc = "001";
		}else{
			curType_icbc = "001";//014是usd
		}
		return curType_icbc;
	}
	/***
	 * 产生支付提交页面的验证信息
	 * @param order
	 * @return
	 */
	private String generateSignMsg(BankOrder order,String ordertimes){				
		String src =this.icbc_name+this.icbc_version+this.corpid+this.merchantaccount+this.recurl+"HS"+order.getRandOrderID()
		            +getAmount(order)+getCurType(order.getCurrency())+"0"+ordertimes+this.verifyJoinFlag;
		if(log.isDebugEnabled()) log.debug("icbc src="+src);       
		return new String(setEncMsg(src));
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
		    byte[] src = strMsg.getBytes();
		    try {
		    	//对签名数据进行BASE64解码
		    	byte[] sign = cn.com.infosec.icbc.ReturnValue.base64dec(signMsg);		    	
		    	//公钥
		    	FileInputStream fis = new FileInputStream(this.publicCert);
		    	byte[] cert = new byte[fis.available()];
//		    	fis.skip(3);
		    	fis.read(cert);
		    	fis.close();		    	
		    	//验证BASE64解码后得到的签名信息
		    	iJg = cn.com.infosec.icbc.ReturnValue.verifySign(src, src.length, cert, sign);
		    	//0表示成功，其他的表示失败
		    }catch (Exception ex){	    	
		    	log.info(ex);
		    }
		    return iJg;
		   
	}

	
	/**
	 * @param merchantaccount The merchantaccount to set.
	 */
	public void setMerchantaccount(String merchantaccount) {
		this.merchantaccount = merchantaccount;
	}
	/**
	 * @return Returns the icbc_name.
	 */
	public String getIcbc_name() {
		return icbc_name;
	}

	/**
	 * @param icbc_name The icbc_name to set.
	 */
	public void setIcbc_name(String icbc_name) {
		this.icbc_name = icbc_name;
	}

	/**
	 * @return Returns the icbc_version.
	 */
	public String getIcbc_version() {
		return icbc_version;
	}

	/**
	 * @param icbc_version The icbc_version to set.
	 */
	public void setIcbc_version(String icbc_version) {
		this.icbc_version = icbc_version;
	}

	/**
	 * @param fileCert The fileCert to set.
	 */
	public void  setFileCert(String fileCerts) {
		fileCert = fileCerts;
	}

	/**
	 * @param fileKey The fileKey to set.
	 */
	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	/**
	 * @param keyPassword The keyPassword to set.
	 */
	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}

	/**
	 * @param publicCert The publicCert to set.
	 */
	public void setPublicCert(String publicCert) {
		this.publicCert = publicCert;
	}
	/**
	 * @return Returns the fileCert.
	 */
	public String getFileCert() {
		return fileCert;
	}
	/**
	 * @param engineService The engineService to set.
	 */
	public void setEngineService(STEngineService engineService) {
		this.engineService = engineService;
	}
	/**
	 * @param verifyJoinFlag The verifyJoinFlag to set.
	 */
	public void setVerifyJoinFlag(String verifyJoinFlag) {
		this.verifyJoinFlag = verifyJoinFlag;
	}
	
	
	

}
