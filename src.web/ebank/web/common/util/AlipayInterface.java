package ebank.web.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.Clazz;
import ebank.core.common.util.MD5sign;
import ebank.core.common.util.Udate;
import ebank.core.domain.MerchantOrder;
import ebank.core.model.domain.GwLgOptions;
import ebank.core.model.domain.GwLogistic;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.TradePrecard;

public class AlipayInterface implements IPartnerInterface {
	
		private static Log log=LogFactory.getLog(AlipayInterface.class);
		
		public MerchantOrder getMerchantOrderByService(HttpServletRequest request) throws ServiceException{
			    
			MerchantOrder mo=new MerchantOrder();
			GwOrders order=new GwOrders();		
			
			
			String service=request.getParameter(Constants.FORMAT_FILED);
			Clazz.Annotation(GwOrders.class, "service", service);
			order.setService(service);		
			
			String partner=request.getParameter("merchant_ID");
			Clazz.Annotation(GwOrders.class, "partnerid", partner);
			order.setPartnerid(Long.valueOf(partner));
			
			String return_url=request.getParameter("return_url");				
			Clazz.Annotation(GwOrders.class, "return_url", return_url);
			order.setReturn_url(return_url);
			
			String notifyUrl=request.getParameter("notify_url");
			Clazz.Annotation(GwOrders.class, "notify_url", notifyUrl);
			order.setNotify_url(notifyUrl);
			
			String sign_type=request.getParameter("signType");
			if(sign_type==null||"".equals(sign_type)){
				sign_type="MD5";
			}		
			Clazz.Annotation(GwOrders.class, "sign_type",sign_type.toUpperCase());
			order.setSign_type(sign_type);
			
			String payment_type="1";
			Clazz.Annotation(GwOrders.class, "order_type", payment_type);
			order.setOrder_type(payment_type);
			
			String _input_charset=request.getParameter("charset");
			Clazz.Annotation(GwOrders.class, "charsets", _input_charset.toUpperCase());
			order.setCharsets(_input_charset);
			
			order.setApiversion(Constants.API_APLIPAY);
			
			String seller_email=request.getParameter("seller_email");
			if(!Validator.isNull(seller_email)){
				Clazz.Annotation(GwOrders.class, "seller_name", seller_email);
				order.setSeller_name(seller_email);
			}
			
			String out_trade_no=request.getParameter("order_no");
			Clazz.Annotation(GwOrders.class, "ordernum", out_trade_no);
			order.setOrdernum(out_trade_no);
			
			String subject=request.getParameter("title");
			Clazz.Annotation(GwOrders.class, "subject", Validator.isNull(subject)?"Alipay":subject);
			order.setSubject(Validator.isNull(subject)?"Alipay":subject);
			
			String total_fee=request.getParameter("total_fee");
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
			
			String paymethod=request.getParameter("paymethod");
			if(!Validator.isNull(paymethod)){
				Clazz.Annotation(GwOrders.class, "paymethod", paymethod);
				order.setPaymethod(paymethod);
			}else{
				order.setPaymethod("bankPay");
			}
			String defaultbank=request.getParameter("defaultbank");
			Clazz.Annotation(GwOrders.class, "preference", defaultbank);
			order.setPreference(defaultbank!=null?Constants.ACQ_CODE_APLIPAY+"-"+defaultbank.toUpperCase():Constants.ACQ_CODE_APLIPAY);
			
			
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
			
			String extra_common_param    =request.getParameter("extra_common_param");
			if(!Validator.isNull(extra_common_param)){
				Clazz.Annotation(GwOrders.class, "ext_param1", extra_common_param);
				order.setExt_param1(extra_common_param);
			}	
			
			String extend_param    =request.getParameter("extend_param");
			if(!Validator.isNull(extend_param)){
				Clazz.Annotation(GwOrders.class, "ext_param2", extend_param);
				order.setExt_param2(extend_param);
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
			if(!Validator.isNull(order.getCurrency())){
				String code=Validator.currencyStanderize(Validator.currencyStanderize(order.getCurrency()));
				if(code==null){
					throw new ServiceException("unsupport currency.");
				}
				order.setCurrency(code);
			}else{
				order.setCurrency("CNY");			
			}
			
			String orderdate=request.getParameter("gmt_out_order_create");				
			
			if(Validator.isNull(orderdate)){
				order.setOrderdate(Udate.format(new Date(),"yyyyMMdd"));
			}else{
				Clazz.Annotation(GwOrders.class, "orderdate", orderdate);
				order.setOrderdate(orderdate);
			}
			
			if(Validator.isNull(order.getLocale())){
				order.setLocale(LocaleUtil.getLocale(null, "CN"));
			}
			
			String sign=request.getParameter("sign");
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
			mo.setSortstr(PartnerInterface.getFormOrderStr(request));
			return mo;
		
	}
}
