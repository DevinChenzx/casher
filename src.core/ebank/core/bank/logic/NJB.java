/*
 * @Id: NJB.java 上午09:56:46 2008-7-16
 * 南京银行
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.koalii.svs.SvsSign;
import com.koalii.svs.SvsVerify;

import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class NJB extends BankExt implements BankService{

	private Logger log = Logger.getLogger(this.getClass());	
	private String fileKey;
	private String fileCert;
	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#getPayResult(java.util.HashMap)
	 */
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		String signDataStr=String.valueOf(reqs.get("signDataStr"));
	    String signData=String.valueOf(reqs.get("signData"));	  
	    String str=String.valueOf(reqs.get("queryString"));
	    //商户代码+”|”+订单号+”|”+订单金额+”|”+支付币种+”|”+交易流水号+”|”+交易结果	    
	    log.info("NJB:"+reqs);
	    PayResult bean=null;
	    try {
	    	int nRet =verify(signDataStr, signData);			
			if (0 == nRet){
				String[] pl=signDataStr.split("\\|");
				bean=new PayResult(pl[1],this.bankcode,new BigDecimal(pl[2]),"4".equals(pl[5])?1:-1);
				bean.setAquiremerchant(pl[0]);
				bean.setEnableFnotice(false);
				bean.setBanktransseq(pl[4]);
				bean.setBankresult(pl[5]); //银行返回结果
				if((this.idx).equals(reqs.get("NR"))){
					reqs.put("RES",str);
				}
			}
			else{
				log.info("NJB nRet:"+nRet);
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
			}
		} catch (Exception e) {
			HandleException.handle(e);
		}		

		return bean;
	}

	public int verify(String src,String sign){
		try {
			return SvsVerify.verify (src.getBytes(), sign, fileCert);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		
	}
	public String[] getSignData(String plain) throws ServiceException{
		SvsSign signer = new SvsSign();
		try
		{
	//		 初始化签名证书和私钥，即.pfx(或.p12)文件名和口令（口令不能为空）
			signer.initSignCertAndKey(this.fileKey, this.keyPassword);
	//		 签名，获得签名结果（Base64 编码）
			return new String[]{signer.signData(plain.getBytes()),signer.getEncodedSignCert ()};
	//		 获得签名证书的Base64 编码			
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
			throw new ServiceException(EventCode.SLA_SERVICENOTACTIVE);
		}
	}
	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#sendOrderToBank(ebank.core.domain.BankOrder)
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		//商户客户号|订单号|交易金额|交易币种。
		String plain=this.getCorpid()+"|"+order.getRandOrderID()+"|"+order.getAmount()+"|"+"01";
		String[] x=this.getSignData(plain);
		String szRet=null;
		String szCert=null;
		if(x!=null&&x.length==2){
		   szRet=x[0];
		   szCert=x[1];
		}		
		StringBuffer sf=new StringBuffer("");		
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");		
		sf.append("<input type=\"hidden\" name=\"netType\" value=\"7\">");
		sf.append("<input type=\"hidden\" name=\"merchantID\" value=\""+this.getCorpid()+"\">");
		sf.append("<input type=\"hidden\" name=\"merOrderNum\" value=\""+order.getRandOrderID()+"\">");
		sf.append("<input type=\"hidden\" name=\"merOrderDate\" value=\""+order.getPostdate().substring(0, 8)+"\">");
		sf.append("<input type=\"hidden\" name=\"merOrderTime\" value=\""+order.getPostdate().substring(8, 14)+"\">");
		sf.append("<input type=\"hidden\" name=\"merTranType\" value=\"0\">");
		sf.append("<input type=\"hidden\" name=\"merOrderAmt\" value=\""+order.getAmount()+"\">");
		sf.append("<input type=\"hidden\" name=\"curType\" value=\"01\">");
		sf.append("<input type=\"hidden\" name=\"reserved1\" value=\"\">");
		sf.append("<input type=\"hidden\" name=\"reserved2\" value=\"\">");
		sf.append("<input type=\"hidden\" name=\"reserved3\" value=\"\">");
		sf.append("<input type=\"hidden\" name=\"reserved4\" value=\"\">");
		sf.append("<input type=\"hidden\" name=\"reserved5\" value=\"\">");
		sf.append("<input type=\"hidden\" name=\"merNotifySign\" value=\"0\">"); //0：只接收支付成功结果
		sf.append("<input type=\"hidden\" name=\"merNotifyUrl\" value=\""+this.getRecurl()+"&NR="+this.idx+"\">");
		sf.append("<input type=\"hidden\" name=\"merGetGoodsSign\" value=\"0\">");//0：成功时展示取货地址
		sf.append("<input type=\"hidden\" name=\"merGetGoodsUrl\" value=\""+this.getRecurl()+"\">");
		sf.append("<input type=\"hidden\" name=\"merAutoTurnTime\" value=\"3\">");
		sf.append("<input type=\"hidden\" name=\"merchantRemarks\" value=\""+order.getOrdernum()+"\">");
		sf.append("<input type=\"hidden\" name=\"signData\" value=\""+szRet+"\">");
		sf.append("<input type=\"hidden\" name=\"signDataStr\" value=\""+plain+"\">");
		sf.append("<input type=\"hidden\" name=\"KoalB64Cert\" value=\""+szCert+"\">");
		sf.append("<input type=\"hidden\" name=\"channelType\" value=\"0\">");
		return sf.toString();
	}

	/**
	 * @param fileKey The fileKey to set.
	 */
	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	/**
	 * @param fileCert The fileCert to set.
	 */
	public void setFileCert(String fileCert) {
		this.fileCert = fileCert;
	}
	
	

}
