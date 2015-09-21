/*
 * @Id: BOC2.java 上午11:24:13 2008-12-10
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import beartool.PKCS7Tool;
import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.web.common.util.Validator;

/**
 * @author 
 * Description: 中国银行
 * 
 */
public class BOC2 extends BankExt implements BankService{

	private String keyStorePath;
	private String keyStorePassword;
	private String rootCertificatePath;
	private String dn;
	
	private Log log=LogFactory.getLog(this.getClass());
	
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		log.info(reqs);
		String merchantNo = String.valueOf(reqs.get("merchantNo"));
		String orderNo = String.valueOf(reqs.get("orderNo"));
		String orderSeq = String.valueOf(reqs.get("orderSeq"));
		String cardTyp = String.valueOf(reqs.get("cardTyp"));
		String payTime = String.valueOf(reqs.get("payTime"));
		String orderStatus = String.valueOf(reqs.get("orderStatus"));
		String payAmount = String.valueOf(reqs.get("payAmount"));
		//merchantNo|orderNo|orderSeq|cardTyp|payTime|orderStatus|payAmount		
		String signData = String.valueOf(reqs.get("signData"));
		String plain=merchantNo+"|"+orderNo+"|"+orderSeq+"|"+cardTyp+"|"+payTime+"|"+orderStatus+"|"+payAmount;		
		PayResult bean=null;		
		if(!Validator.isNull(signData)){			
			if(verify(signData,plain)){ //支付成功
				if("1".equals(orderStatus)){
					bean=new PayResult(orderNo,this.bankcode,new BigDecimal(payAmount),
					           "1".equals(orderStatus)?1:-1);
					bean.setBanktransseq(orderSeq);
					bean.setBankresult(orderStatus);					
					bean.setEnableFnotice(false);	
			    }else{
			    	throw new ServiceException("501125"); //支付失败
			    }
		    }else{
		    	throw new ServiceException(EventCode.CRYPT_VALIADATESIGN); //验证签名失败
		    }
			if(this.idx.equals(reqs.get("NR"))){
				reqs.put("RES",orderSeq+orderNo);
			}				
		   			
		}			
		return bean;
	}

	public String sendOrderToBank(BankOrder order) throws ServiceException {
		//orderNo|orderTime|curCode|orderAmount|merchantNo
		String plain=order.getRandOrderID()+"|"+order.getPostdate()+"|001|"+order.getAmount()+"|"+this.corpid;
		System.out.println("plain:"+plain);
		
		String signData=getSignData(plain);		
		if(signData==null) throw new ServiceException(EventCode.BOC_SIGNERROR);		
		StringBuffer sf=new StringBuffer();
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");	
		sf.append("<input type=\"hidden\" name=\"payType\" value=\"1\" />");
		sf.append("<input type=\"hidden\" name=\"merchantNo\" value=\""+this.corpid+"\" />");
		sf.append("<input type=\"hidden\" name=\"orderNo\" value=\""+order.getRandOrderID()+"\">");
		sf.append("<input type=\"hidden\" name=\"curCode\" value=\"001\" />");
		sf.append("<input type=\"hidden\" name=\"orderAmount\" value=\""+order.getAmount()+"\" />");
		sf.append("<input type=\"hidden\" name=\"orderTime\" value=\""+order.getPostdate()+"\" />");
		String orderNote=order.getOrdernum();
		if(orderNote!=null&&orderNote.length()>30) orderNote=orderNote.substring(0, 29);
		else orderNote=order.getRandOrderID();
		sf.append("<input type=\"hidden\" name=\"orderNote\" value=\""+orderNote+"\" />");	
		sf.append("<input type=\"hidden\" name=\"orderUrl\" value=\""+this.getRecurl()+"\">");
		
		sf.append("<input type=\"hidden\" name=\"signData\" value=\""+signData+"\">");
		sf.append("</form>");	
		System.out.println("sf:"+sf.toString());
		return sf.toString();		
	}
	
	public String getSignData(String plain){
		try {
			String keypass = this.keyPassword;
			return PKCS7Tool.getSigner(this.keyStorePath, this.keyStorePassword, keypass).sign(plain.getBytes());																			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean verify(String signData,String plain){
		try {
			PKCS7Tool tools=PKCS7Tool.getVerifier(rootCertificatePath);
			if(tools!=null){
				tools.verify(signData, plain.getBytes(), this.dn);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}

	public void setDn(String dn) {
		this.dn = dn;
	}


	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}

	public void setRootCertificatePath(String rootCertificatePath) {
		this.rootCertificatePath = rootCertificatePath;
	}
	
	

}
