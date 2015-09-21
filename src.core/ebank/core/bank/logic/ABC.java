/*
 * Created on 2004-11-30
 *
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import org.apache.log4j.Logger;


import com.hitrust.trustpay.client.TrxResponse;
import com.hitrust.trustpay.client.b2c.Order;
import com.hitrust.trustpay.client.b2c.PaymentRequest;
import com.hitrust.trustpay.client.b2c.PaymentResult;

import ebank.core.bank.BankService;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;



/**
 * @author 
 * Description: 农业银行
 * 
 */
public class ABC extends BankExt implements BankService{
	static Logger logger = Logger.getLogger(ABC.class); 	

	private String getPAURL(BankOrder order){
		String PAURL="";
		//1、取得支付请求所需要的信息
		String tOrderURL        = this.getRecurl();//"http://manager.chinaebank.cn/abcchina/MerchantQueryOrder.jsp?ON="+order.getRandOrderID()+"&QueryType=1";
		String tProductType     = "1";//商品种类
		//根据客户传过来的支付类型，判断是否支付方式
		String tPaymentType="";
		if(order.getPayment_type()!=null&&!"".equals(order.getPayment_type())&&order.getPayment_type().equals("1")){
			 tPaymentType     = "1";//1：农行卡支付  
		}else if(order.getPayment_type()!=null&&!"".equals(order.getPayment_type())&&order.getPayment_type().equals("2")){
			tPaymentType     = "3";//3：贷记卡支付  
		}else{
			tPaymentType     = "A";//3：借记卡，贷记卡支付
		}
		//String tResultNotifyURL = "http://receive.chinaebank.cn/abcchina/payment.jsp";//支付结果回传网址
		//String tMerchantRemarks = v_url;//商户备注信息
//		2、生成订单对象
		Order tOrder = new Order();
		tOrder.setOrderNo    (order.getRandOrderID()); //设定订单编号 （必要信息）
		tOrder.setOrderDesc  (order.getOrdernum()==null?order.getRandOrderID():order.getOrdernum());    //设定订单说明
		Date curdate=null;
		try {
			curdate=new SimpleDateFormat("yyyyMMddHHmmss").parse(order.getPostdate());
		} catch (Exception e) {
			logger.warn("paser date exception,use sysdate");
			curdate=new Date();
		}	
		//
		String hour=new SimpleDateFormat("HH").format(curdate);
		int min=curdate.getMinutes();
		if((hour.equals("01")&&min<28)
		   ||(hour.equals("00")&&min>55)){
			return "F:<font color=red>农业银行 1:00-1:30 时段系统清算,系统不稳定,<br>此时间段无法交易,若交易可能导致支付失败。现在系统时间:"+hour+"时"+min+"分,请稍后支付</font>";			
		}
		tOrder.setOrderDate  (new SimpleDateFormat("yyyy/MM/dd",Locale.US).format(curdate)); //设定订单日期 （必要信息 - YYYY/MM/DD）
		tOrder.setOrderTime  (new SimpleDateFormat("HH:mm:ss",Locale.US).format(curdate)); //设定订单时间 （必要信息 - HH:MM:SS）
		tOrder.setOrderAmount(Double.parseDouble(order.getAmount())); //设定订单金额 （必要信息）
		tOrder.setOrderURL   (tOrderURL   ); //设定订单网址
		
		
//		3、生成定单订单对象，并将订单明细加入定单中（可选信息）
//		tOrder.addOrderItem(new OrderItem("IP000001", "中国移动IP卡", 100.00f, 1));
//		tOrder.addOrderItem(new OrderItem("IP000002", "网通IP卡"    ,  90.00f, 2));

//		4、生成支付请求对象
		PaymentRequest tPaymentRequest = new PaymentRequest();
		tPaymentRequest.setOrder      (tOrder      ); //设定支付请求的订单 （必要信息）
		tPaymentRequest.setProductType(tProductType); //设定商品种类 （必要信息）
		                                              //PaymentRequest.PRD_TYPE_ONE：非实体商品，如服务、IP卡、下载MP3、...
		                                              //PaymentRequest.PRD_TYPE_TWO：实体商品
		tPaymentRequest.setPaymentType(tPaymentType); //设定支付类型
		                                              //PaymentRequest.PAY_TYPE_ABC：农行卡支付
		                                              //PaymentRequest.PAY_TYPE_INT：国际卡支付
		tPaymentRequest.setNotifyType("1");
		tPaymentRequest.setResultNotifyURL(this.httprecurl+"?idx=103"); //设定支付结果回传网址 （必要信息）
		tPaymentRequest.setMerchantRemarks(order.getMerchantid()); //设定商户备注信息
		TrxResponse tTrxResponse = tPaymentRequest.postRequest();
		if (tTrxResponse.isSuccess()) {
			PAURL=tTrxResponse.getValue("PaymentURL");
		}else{
			PAURL="E:"+tTrxResponse.getReturnCode()+"@#@"+tTrxResponse.getErrorMessage();
		}
		tPaymentRequest=null;
		tTrxResponse=null;
		tOrder=null;
		curdate=null;
		tOrderURL=null;
		tProductType=null;
		tPaymentType=null;
		return PAURL;
	}
	
	/**
	 * 产生页面跳转的代码
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException{
		//产生订单号
		//order.setRandOrderID(generateOrderID());
		StringBuffer sf=new StringBuffer("");
		String strAction=getPAURL(order);
		if(strAction.startsWith("E:")){
			String[] strs=strAction.split("@#@");
			sf.append("农业银行支付接口故障,很抱歉请稍后尝试支付<br>");
			sf.append("ReturnCode   = [" + strs[0] + "]<br>");
			sf.append("ErrorMessage = [" + strs[1] + "]<br>");
		}else if(strAction.startsWith("F:")){
			sf.append(strAction.substring(2));			
		}else{
			sf.append("<form name=sendOrder method=\"post\" action=\""+strAction+"\"></form>");
			//sf.append("<body onload=\"javascript:window.location.href='"+strAction+"';\">");
			if(logger.isDebugEnabled()) logger.debug("abc action url="+strAction);			
		}
		strAction=null;
		logger.info(sf.toString());
		return sf.toString();
	}
	
	/***
	 * 支付结果
	 * @param 根据request得到的参数Map
	 * @return 
	 */
	public PayResult getPayResult(HashMap reqs) throws ServiceException
	{		
		PayResult bean=null;
		boolean bln=false;
		String msg=String.valueOf(reqs.get("MSG"));
		try{
			PaymentResult tResult = new PaymentResult(msg);
			String OrderNo = tResult.getValue("OrderNo");
			String Amount = tResult.getValue("Amount");				
			//2、判断支付结果状态，进行后续操作						
			bln=tResult.isSuccess();			
			bean=new PayResult(OrderNo,this.bankcode,new BigDecimal(Amount),bln?1:-1);			
			bean.setBankresult(String.valueOf(bln));						
			tResult=null;		
			OrderNo=null;
			Amount=null;
		}catch(Exception e){
			HandleException.handle(e);
		}
		return bean;
	}	
	
}
