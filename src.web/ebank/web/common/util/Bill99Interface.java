package ebank.web.common.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.Validate;

import ebank.core.common.Constants;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.Clazz;
import ebank.core.domain.MerchantOrder;

import ebank.core.model.domain.GwOrders;

public class Bill99Interface implements IPartnerInterface{

	public MerchantOrder getMerchantOrderByService(HttpServletRequest request)
			throws ServiceException {
		MerchantOrder mo=new MerchantOrder();
		GwOrders order=new GwOrders();		
		
		
		String service=request.getParameter(Constants.FORMAT_FILED);
		Clazz.Annotation(GwOrders.class, "service", service);
		order.setService(service);				
		
		String sign=request.getParameter("signMsg");
		Clazz.Annotation(MerchantOrder.class, "sign", sign);
		mo.setSign(sign);
		
				
		String _input_charset=request.getParameter("inputCharset");
		if(Validator.isNull(_input_charset)){
			_input_charset="UTF-8";
		}else{
			if("1".equals(_input_charset)){
				_input_charset="UTF-8";
			}
			if("2".equals(_input_charset)){
				_input_charset="GBK";
			}
			if("3".equals(_input_charset)){
				_input_charset="GB2312";
			}
		}
		Clazz.Annotation(GwOrders.class, "charsets", _input_charset.toUpperCase());
		order.setCharsets(_input_charset);
		
		String showUrl=request.getParameter("pageUrl");
		Clazz.Annotation(GwOrders.class, "show_url", showUrl);
		order.setShow_url(showUrl);	
		
		String notifyUrl=request.getParameter("bgUrl");
		Clazz.Annotation(GwOrders.class, "notify_url", notifyUrl);
		order.setNotify_url(notifyUrl);
		
		String version=request.getParameter("version");
		Clazz.Annotation(GwOrders.class, "apiversion", version);
		order.setApiversion(Constants.API_99BILL);	
		
		String language=request.getParameter("language");
		if("1".equals(language)){
			language="CN";
		}
		Clazz.Annotation(GwOrders.class, "locale", language);
		order.setLocale(language);
				
		String sign_type=request.getParameter("signType");
		if("1".equals(sign_type)){
			sign_type="MD5";
		}		
		Clazz.Annotation(GwOrders.class, "sign_type", sign_type==null?"":sign_type.toUpperCase());
		order.setSign_type(sign_type);
		

		//sf.append("<input type=\"hidden\" name=\"merchantAcctId\" value=\""+merchantAcctId+"\" >");
		
		String buyer_realname=request.getParameter("payerName");
			
		Clazz.Annotation(GwOrders.class, "buyer_realname", buyer_realname);
		order.setBuyer_realname(buyer_realname);
		
		String buyer_contact=request.getParameter("payerContact");
		
		Clazz.Annotation(GwOrders.class, "buyer_contact", buyer_contact);
		order.setBuyer_contact(buyer_contact);

      	String amount=request.getParameter("orderAmount");		
		Clazz.Annotation(GwOrders.class, "amount", amount);
		order.setAmount(Amount.getIntAmount(amount,0));
		order.setPrice(order.getAmount()); //相等
		order.setQuantity(1);

		String orderdate=request.getParameter("orderTime");
		Clazz.Annotation(GwOrders.class, "orderdate", orderdate);
		order.setOrderdate(orderdate);

		//sf.append("<input type=\"hidden\" name=\"productName\" value=\""+productName+"\" >");
		String subject=request.getParameter("productName");
		Clazz.Annotation(GwOrders.class, "subject", Validator.isNull(subject)?"Bill99":subject);
		order.setSubject(Validator.isNull(subject)?"Bill99":subject);
		
		//sf.append("<input type=\"hidden\" name=\"productNum\" value=\""+productNum+"\" >");	
		String quantity=request.getParameter("productNum");
		if(quantity==null){
			quantity="1";
		}else Clazz.Annotation(GwOrders.class, "quantity", quantity);
		order.setQuantity(Integer.parseInt(quantity));
			
		String body=request.getParameter("productDesc");
		if(Validator.isNull(body)){
			body=request.getParameter("productId")+"("+order.getOrdernum()+")";
		}
		Clazz.Annotation(GwOrders.class, "bodys", body);
		order.setBodys(body);	
		
		//sf.append("<input type=\"hidden\" name=\"ext1\" value=\""+ext1+"\" >");
		String ext_param1=request.getParameter("ext1");
		if(!Validator.isNull(ext_param1)){
			Clazz.Annotation(GwOrders.class, "ext_param1", ext_param1);
			order.setExt_param1(ext_param1);
		}
		String ext_param2=request.getParameter("ext2");
		if(!Validator.isNull(ext_param2)){
			Clazz.Annotation(GwOrders.class, "ext_param2", ext_param2);
			order.setExt_param2(ext_param2);
		}			
		
		String partner=request.getParameter("merchantAcctId");
		Clazz.Annotation(GwOrders.class, "partnerid", partner);
		order.setPartnerid(Long.parseLong(partner));		
		
		
		String out_trade_no=request.getParameter("orderId");
		Clazz.Annotation(GwOrders.class, "ordernum", out_trade_no);
		order.setOrdernum(out_trade_no);
		
		
		String paymethod="bankPay";//request.getParameter("payType");
		if(!Validator.isNull(paymethod)){
			Clazz.Annotation(GwOrders.class, "paymethod", paymethod);
			order.setPaymethod(paymethod);
			
			String defaultbank=request.getParameter("bankId");
			Clazz.Annotation(GwOrders.class, "preference", defaultbank);
			order.setPreference(!Validator.isNull(defaultbank)?Constants.ACQ_CODE_99BILL+"-"+defaultbank.toUpperCase():Constants.ACQ_CODE_99BILL+"L");
		}else{
			order.setPaymethod("bankPay");
		}
		
		String return_url=request.getParameter("bgUrl");				
		Clazz.Annotation(GwOrders.class, "return_url", return_url);
		order.setReturn_url(return_url);
		
		String payment_type="1";
		Clazz.Annotation(GwOrders.class, "order_type", payment_type);
		order.setOrder_type(payment_type);
		
		if(!Validator.isNull(request.getParameter("it_b_pay"))){ //转换成
			String exp_dates=request.getParameter("it_b_pay");
			Clazz.Annotation(GwOrders.class, "exp_dates", exp_dates);
			order.setExp_dates(exp_dates);
		}else{
			order.setExp_dates("15d");
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
		
		
		mo.setOrders(order);
		mo.setSortstr(getFormOrderStr(request));
		return mo;
		
	}
	
	private String getFormOrderStr(HttpServletRequest request){		
		String signMsgVal="";
		String _input_charset=request.getParameter("inputCharset");
		if(Validator.isNull(_input_charset)){
			_input_charset="1";
		}else{
			if("UTF-8".equals(_input_charset)){
				_input_charset="1";
			}
			if("GBK".equals(_input_charset)){
				_input_charset="2";
			}
			if("GB2312".equals(_input_charset)){
				_input_charset="3";
			}
		}
		signMsgVal=appendParam(signMsgVal,"inputCharset",_input_charset);
		signMsgVal=appendParam(signMsgVal,"pageUrl",request.getParameter("pageUrl"));
		signMsgVal=appendParam(signMsgVal,"bgUrl",request.getParameter("bgUrl"));
		signMsgVal=appendParam(signMsgVal,"version",request.getParameter("version"));
		signMsgVal=appendParam(signMsgVal,"language",request.getParameter("language"));
		signMsgVal=appendParam(signMsgVal,"signType",request.getParameter("signType"));
		signMsgVal=appendParam(signMsgVal,"merchantAcctId",request.getParameter("merchantAcctId"));
		signMsgVal=appendParam(signMsgVal,"payerName",request.getParameter("payerName"));
		signMsgVal=appendParam(signMsgVal,"payerContactType",request.getParameter("payerContactType"));
		signMsgVal=appendParam(signMsgVal,"payerContact",request.getParameter("payerContact"));
		signMsgVal=appendParam(signMsgVal, "orderId",request.getParameter("orderId"));
		signMsgVal=appendParam(signMsgVal,"orderAmount",request.getParameter("orderAmount"));
		signMsgVal=appendParam(signMsgVal,"orderTime",request.getParameter("orderTime"));
		signMsgVal=appendParam(signMsgVal,"productName",request.getParameter("productName"));
		signMsgVal=appendParam(signMsgVal,"productNum",request.getParameter("productNum"));
		signMsgVal=appendParam(signMsgVal,"productId",request.getParameter("productId"));
		signMsgVal=appendParam(signMsgVal,"productDesc",request.getParameter("productDesc"));
		signMsgVal=appendParam(signMsgVal,"ext1",request.getParameter("ext1"));
		signMsgVal=appendParam(signMsgVal,"ext2",request.getParameter("ext2"));
		signMsgVal=appendParam(signMsgVal,"payType",request.getParameter("payType"));
		signMsgVal=appendParam(signMsgVal,"bankId",request.getParameter("bankId"));
		
		String redoFlag = request.getParameter("redoFlag");
		if(Validator.isNull(redoFlag)){
			redoFlag="0";
         }
		signMsgVal=appendParam(signMsgVal,"redoFlag",redoFlag);
		signMsgVal=appendParam(signMsgVal,"pid",request.getParameter("pid"));
		System.out.println("signMsgVal:"+signMsgVal);
		return signMsgVal;
	}
	
	public String appendParam(String returnStr,String paramId,String paramValue)
	{
			if(!returnStr.equals(""))
			{
				if(paramValue!=null&&!"".equals(paramValue))
				{
					returnStr=returnStr+"&"+paramId+"="+paramValue.trim();
				}
			}
			else
			{
				if(!"".equals(paramValue))
				{
				returnStr=paramId+"="+paramValue;
				}
			}	
			return returnStr;
	}
	

}
