package ebank.core.bank.b2b;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.Logger;

import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

/**
 * @author 
 * Description: 兴业银行
 * 
 */
public class CIB extends BankExt implements BankService {
	private Logger log = Logger.getLogger(this.getClass());
	private String keyPassword;
	private static String SERVICE = "cib.netpay.b2b";
	private static String VER = "01";
	
	//12位订单号
	public String generateOrderID() throws ServiceException{
		String RandOrderID=sequenceservice.getCode();	//yymmdd(5)+8位序列			
		Random rd=new Random();
		String str="";			
		for(int i=0;i<3-this.prefixnum.length();i++){
			str+=rd.nextInt(10);
		}
		rd=null;		
		return prefixnum+RandOrderID.substring(5,14)+str;
	}

	public String sendOrderToBank(BankOrder order) throws ServiceException {

		String orderid=order.getRandOrderID();
		String datetext=new SimpleDateFormat("yyyyMMdd").format(new Date());
		if(order.getCurrency()==null||"".equals(order.getCurrency()))
			order.setCurrency("CNY");
		
		StringBuffer sf=new StringBuffer();
		sf.append("service");
		sf.append(SERVICE);
		sf.append("ver");
		sf.append(VER);
		sf.append("mrch_no");
		sf.append(this.getCorpid());
		sf.append("ord_no");
		sf.append(orderid);
		sf.append("ord_date");
		sf.append(datetext);
		sf.append("cur");
		sf.append(order.getCurrency());
		sf.append("ord_amt");
		sf.append(order.getAmount());
		
		log.debug("兴业银行B2B签名前字符串="+sf.toString());	
		
		String plain=sf.toString()+this.getKeyPassword();		
					
		String mac= md5(plain);
		
		log.debug("兴业银行B2B加密签名后字符串="+mac);
					
		sf=null;		
		StringBuffer sf2=new StringBuffer();		
		sf2.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf2.append("<input type=\"hidden\" name=\"service\" value=\""+SERVICE+"\" />");
		sf2.append("<input type=\"hidden\" name=\"ver\" value=\""+VER+"\" />");
		sf2.append("<input type=\"hidden\" name=\"mrch_no\" value=\""+this.getCorpid()+"\" />");
//		sf2.append("<input type=\"hidden\" name=\"sub_mrch\" value=\"\" />");
		sf2.append("<input type=\"hidden\" name=\"ord_no\" value=\""+orderid+"\" />");
		sf2.append("<input type=\"hidden\" name=\"ord_date\" value=\""+datetext+"\" />");
		sf2.append("<input type=\"hidden\" name=\"cur\" value=\""+order.getCurrency()+"\" />");
		sf2.append("<input type=\"hidden\" name=\"ord_amt\" value=\""+order.getAmount()+"\" />");	
//		sf2.append("<input type=\"hidden\" name=\"ord_ip\" value=\""+order.getCustip()+"\" />");
		sf2.append("<input type=\"hidden\" name=\"mac\" value=\""+mac+"\" />");
		sf2.append("</form>");		
	
		String result=sf2.toString();		
		if(log.isDebugEnabled()) log.debug("兴业银行B2B提交参数字符串:"+result);
		sf2=null;
		return result;
	}

	/* (non-Javadoc)
	 * @see ebank.core.bank.service.BankService#getPA(java.util.HashMap)
	 */
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		
		String bank=String.valueOf(reqs.get("bank"))==null?"cib":"cib";
		String mrch_no=String.valueOf(reqs.get("mrch_no"));
		String ord_no=String.valueOf(reqs.get("ord_no"));
		String cur=String.valueOf(reqs.get("cur"));
		String ord_amt=String.valueOf(reqs.get("ord_amt"));
		String pay_amt=String.valueOf(reqs.get("pay_amt"));
		String sno=String.valueOf(reqs.get("sno"));
		String mac=String.valueOf(reqs.get("mac"));
		
		StringBuffer sf=new StringBuffer();
		sf.append("bank");
		sf.append(bank);
		sf.append("mrch_no");
		sf.append(mrch_no);
		sf.append("ord_no");
		sf.append(ord_no);
		sf.append("cur");
		sf.append(cur);
		sf.append("ord_amt");
		sf.append(ord_amt);
		sf.append("pay_amt");
		sf.append(pay_amt);
		sf.append("sno");
		sf.append(sno);
		
		String plain=sf.toString()+this.getKeyPassword();	
		sf.append("mac");
		sf.append(mac);
		log.info("兴业银行B2B支付返回:"+sf.toString());
		
		String md5 = md5(plain);	
		log.info("兴业银行B2B支付返回参数MD5加密字符串:"+md5);
		PayResult bean=null;		
		if (mac.equals(md5)){	     
				bean=new PayResult(ord_no,this.bankcode,new BigDecimal(pay_amt),1);							
				bean.setCurrency(cur);
				bean.setBankresult("1");
				bean.setBanktransseq(sno);				
			    if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(String.valueOf(reqs.get("NR")))){
			    	log.debug("兴业银行B2B支付服务器通知成功！");
			    	reqs.put("RES","success");					
				}
		  }else{
			   if(("SID_"+this.idx).equals(reqs.get("NR"))){
				   log.debug("兴业银行B2B支付服务器通知验证失败！");
				   reqs.put("RES","false");
			   }
			   throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);	
		}			
	   return bean;
	}
	/**
	 * @param keyPassword The keyPassword to set.
	 */
	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}
	
   public static String md5(String str) {
	   
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes("UTF-8"));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer();
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
  }
   
   
}
