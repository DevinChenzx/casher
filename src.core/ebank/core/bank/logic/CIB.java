/*
 * @Id: CIB.java 11:39:10 2006-3-30
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.Logger;

import com.crypt.Des.DesUtil;

import ebank.core.bank.BankService;
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
	/* (non-Javadoc)
	 * @see ebank.core.bank.service.BankService#sendOrderToBank(ebank.core.domain.BankOrder)
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		//商户编号+订单号+订单生成日期+订单金额+商户通知类型+订单币种+订单返回的URL+支付方式
		//NetPaymentMerchantID+NetPaymentMerchantTraceNo+orderDate+AmountNumber+notifyType+orderCurrencyCode+merchantURL+payMethod
		String orderid=order.getRandOrderID();
		String datetext=new SimpleDateFormat("yyyyMMdd").format(new Date());
		if(order.getCurrency()==null||"".equals(order.getCurrency()))
			order.setCurrency("CNY");
		StringBuffer sf=new StringBuffer();
		sf.append(this.getCorpid());
		sf.append(orderid);
		sf.append(datetext);
		sf.append(order.getAmount());
		sf.append("1");                 //交易成功立即通知
		sf.append(order.getCurrency()); //CNY
		sf.append(this.getRecurl());		
		sf.append("merchant");          //立即支付
		//sf.append(order.getMOrderID());  //商户订单ID做跟踪号
		String plain=sf.toString();		
		String mac=DesUtil.genMac(this.keyPassword,plain);
		if(mac.length()<8){
			for(int i=0;i<8-mac.length();i++){
				mac+="0";
			}
		}		
		if(log.isDebugEnabled()){
			log.debug("mac="+mac);
			log.debug("plain="+plain);	
			log.debug("keypass="+this.keyPassword);
		}
		mac=mac.substring(0,8); //8字符
		sf=null;		
		//NetPaymentMerchantID=130007&NetPaymentMerchantTraceNo=150549702&orderDate=20030718&AmountNumber=100.56
		//&notifyType=1&orderCurrencyCode=CNY&merchantURL=https://www.fz.fj.cn/receive.asp&payMethod=merchant&netBankTraceNo=00001234&payStatus=1&mac=7674CED3
		StringBuffer sf2=new StringBuffer();		
		sf2.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf2.append("<input type=\"hidden\" name=\"NetPaymentMerchantID\" value=\""+this.getCorpid()+"\" />");
		sf2.append("<input type=\"hidden\" name=\"NetPaymentMerchantTraceNo\" value=\""+orderid+"\" />");
		sf2.append("<input type=\"hidden\" name=\"orderDate\" value=\""+datetext+"\" />");
		sf2.append("<input type=\"hidden\" name=\"AmountNumber\" value=\""+order.getAmount()+"\" />");
		sf2.append("<input type=\"hidden\" name=\"notifyType\" value=\""+1+"\" />");
		sf2.append("<input type=\"hidden\" name=\"orderCurrencyCode\" value=\""+order.getCurrency()+"\" />");
		sf2.append("<input type=\"hidden\" name=\"merchantURL\" value=\""+this.getRecurl()+"\" />");
		sf2.append("<input type=\"hidden\" name=\"payMethod\" value=\"merchant\" />");		
		sf2.append("<input type=\"hidden\" name=\"merchantMac\" value=\""+mac+"\" />");
		sf2.append("</form>");		
		String result=sf2.toString();
		if(log.isDebugEnabled()) log.debug("to CIB:"+sf2.toString());
		sf2=null;
		return result;
	}

	/* (non-Javadoc)
	 * @see ebank.core.bank.service.BankService#getPA(java.util.HashMap)
	 */
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		// 商户代号+订单号+订单金额+订单币种+网银流水号+支付状态编号
		String merchantid=String.valueOf(reqs.get("merchantID"));
		String orderid=String.valueOf(reqs.get("orderID"));
		String orderamount=String.valueOf(reqs.get("orderAmount"));
		String cur=String.valueOf(reqs.get("orderCurrencyCode"));
		String tracenum=String.valueOf(reqs.get("netBankTraceNo"));
		String pstatus=String.valueOf(reqs.get("payStatus"));
		String mac=String.valueOf(reqs.get("mac"));
		String str=merchantid+orderid+orderamount+cur+tracenum+pstatus;
		if(log.isDebugEnabled()) log.debug("rev from CIB:"+str);
		boolean flag = DesUtil.checkMac(this.keyPassword,str,mac);		
		PayResult bean=null;		
		if (flag == true){
			bean=new PayResult(orderid,null,new BigDecimal(orderamount),"1".equals(pstatus)?1:-1);				
			
			bean.setCurrency(cur);
			bean.setBankresult(pstatus);
			bean.setBanktransseq(tracenum);			
	   }	
	   else			
		   throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);	
	   return bean;
	}
	/**
	 * @param keyPassword The keyPassword to set.
	 */
	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}
	

}
