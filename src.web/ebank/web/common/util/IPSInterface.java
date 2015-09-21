package ebank.web.common.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import ebank.core.common.Constants;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.Clazz;
import ebank.core.common.util.Udate;
import ebank.core.domain.MerchantOrder;
import ebank.core.model.domain.GwOrders;

public class IPSInterface implements IPartnerInterface {

	public MerchantOrder getMerchantOrderByService(HttpServletRequest request)
			throws ServiceException {
		
		MerchantOrder mo=new MerchantOrder();
		GwOrders order=new GwOrders();		
		
		
		String service=request.getParameter(Constants.FORMAT_FILED);
		Clazz.Annotation(GwOrders.class, "service", service);
		order.setService(service);		
		
		String partner=request.getParameter("Mer_code");
		Clazz.Annotation(GwOrders.class, "partnerid", partner);
		order.setPartnerid(Long.valueOf(partner));
		
		String return_url=request.getParameter("Merchanturl");				
		Clazz.Annotation(GwOrders.class, "return_url", return_url);
		order.setReturn_url(return_url);
		
		String notifyUrl=request.getParameter("ServerUrl");
		Clazz.Annotation(GwOrders.class, "notify_url", notifyUrl);
		order.setNotify_url(notifyUrl);
		
		String sign_type=request.getParameter("OrderEncodeType");
		if(sign_type==null||!"2".equals(sign_type)){
			sign_type="2";
		}
		if(sign_type!=null&&"2".equals(sign_type)){
			sign_type="MD5";
		}
		Clazz.Annotation(GwOrders.class, "sign_type",sign_type.toUpperCase());
		order.setSign_type(sign_type);
		
		String payment_type="1";
		Clazz.Annotation(GwOrders.class, "order_type", payment_type);
		order.setOrder_type(payment_type);
		
		String _input_charset=request.getParameter("_input_charset");
		if(Validator.isNull(_input_charset)){
			_input_charset="UTF-8";
		}
		Clazz.Annotation(GwOrders.class, "charsets", _input_charset.toUpperCase());
		order.setCharsets(_input_charset);
		
		order.setApiversion(Constants.API_IPS);
		
		String seller_email=request.getParameter("seller_email");
		if(!Validator.isNull(seller_email)){
			Clazz.Annotation(GwOrders.class, "seller_name", seller_email);
			order.setSeller_name(seller_email);
		}
		
		String out_trade_no=request.getParameter("Billno");
		Clazz.Annotation(GwOrders.class, "ordernum", out_trade_no);
		order.setOrdernum(out_trade_no);
		
		String subject=request.getParameter("subject");
		Clazz.Annotation(GwOrders.class, "subject", Validator.isNull(subject)?"IPS":subject);
		order.setSubject(Validator.isNull(subject)?"IPS":subject);
		
		String total_fee=request.getParameter("Amount");
		if(!Validator.isNull(total_fee)){
			Clazz.Annotation(GwOrders.class, "amount", total_fee);					
			order.setAmount(Amount.getIntAmount(total_fee,2));
			order.setPrice(order.getAmount()); //相等
			order.setQuantity(1);
		}
		String body=request.getParameter("body");
		if(Validator.isNull(body)){
			body=out_trade_no;
		}
		Clazz.Annotation(GwOrders.class, "bodys", body);
		order.setBodys(body);			
			
		String defaultbank=Constants.ACQ_CODE_IPS;
		Clazz.Annotation(GwOrders.class, "preference", defaultbank);
		order.setPreference(defaultbank);
		
		String buyer_email =request.getParameter("buyer_email");
		if(!Validator.isNull(buyer_email)){
			Clazz.Annotation(GwOrders.class, "buyer_name", buyer_email);
			order.setBuyer_name(buyer_email);
		}
		
		String seller_id   =request.getParameter("seller_id");
		if(!Validator.isNull(seller_id)){
			Clazz.Annotation(GwOrders.class, "seller_id", seller_id);
			order.setSeller_id(seller_id);
		}
		
		String buyer_id    =request.getParameter("buyer_id");
		if(!Validator.isNull(buyer_id)){
			Clazz.Annotation(GwOrders.class, "buyer_id", buyer_id);
			order.setBuyer_id(buyer_id);
		}				
		
		String show_url=request.getParameter("show_url");
		if(!Validator.isNull(show_url)){
			Clazz.Annotation(GwOrders.class, "show_url", show_url);
			order.setShow_url(show_url);	
		}
		
		order.setIps(RequestUtil.getIpAddr(request));				
		
		if(Validator.isNull(order.getDiscount())){
			order.setDiscount(0);
		}
		String Currency_Type    =request.getParameter("Currency_Type");
		if(!Validator.isNull(Currency_Type)){
			String code=Validator.currencyStanderize(Validator.currencyStanderize(Currency_Type));
			if(code==null){
				throw new ServiceException("unsupport currency.");
			}
			order.setCurrency(code);
		}else{
			order.setCurrency("CNY");			
		}
		
		String orderdate=request.getParameter("Date");				
		
		if(Validator.isNull(orderdate)){
			order.setOrderdate(Udate.format(new Date(),"yyyyMMdd"));
		}else{
			Clazz.Annotation(GwOrders.class, "orderdate", orderdate);
			order.setOrderdate(orderdate);
		}
		
		if(Validator.isNull(order.getLocale())){
			order.setLocale(LocaleUtil.getLocale(null, "CN"));
		}
		
		String sign=request.getParameter("SignMD5");
		Clazz.Annotation(MerchantOrder.class, "sign", sign);
		mo.setSign(sign);
				
		if(!Validator.isNull(request.getParameter("it_b_pay"))){ //转换成
			String it_b_pay=request.getParameter("it_b_pay");
			Clazz.Annotation(GwOrders.class, "exp_dates", it_b_pay);
			order.setExp_dates(it_b_pay);
		}else{
			order.setExp_dates("15d");
		}
								
		
		mo.setOrders(order);
		mo.setSortstr(getFormOrderStr(out_trade_no,total_fee,orderdate,Currency_Type));
		return mo;
		
		
	
	}
	
	
      private String getFormOrderStr(String Billno,String Amount,String Date,String Currency_Type){	
		
		StringBuffer signMsgBuffer = new StringBuffer();	
		 signMsgBuffer.append(Billno);
		 signMsgBuffer.append(Amount);
		 signMsgBuffer.append(Date);
		 signMsgBuffer.append(Currency_Type);
		return signMsgBuffer.toString();
	}
	

}
