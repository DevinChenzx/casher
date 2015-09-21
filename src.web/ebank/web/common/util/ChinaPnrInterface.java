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

public class ChinaPnrInterface implements IPartnerInterface {

	public MerchantOrder getMerchantOrderByService(HttpServletRequest request)
			throws ServiceException {
		
		MerchantOrder mo=new MerchantOrder();
		GwOrders order=new GwOrders();		
		
		
		String service=request.getParameter(Constants.FORMAT_FILED);
		Clazz.Annotation(GwOrders.class, "service", service);
		order.setService(service);	
		
		String Version=request.getParameter("Version");
		Clazz.Annotation(GwOrders.class, "apiversion", Version);
		order.setApiversion(Constants.API_CHINABANK);	
		
		
		String MerId=request.getParameter("UsrId");
		Clazz.Annotation(GwOrders.class, "partnerid", MerId);
		order.setPartnerid(Long.valueOf(MerId));
		
		String OrdId=request.getParameter("OrdId");
		Clazz.Annotation(GwOrders.class, "ordernum", OrdId);
		order.setOrdernum(OrdId);
		
		String charsets=request.getParameter("charsets");
		Clazz.Annotation(GwOrders.class, "charsets", Validator.isNull(charsets)?"UTF-8":charsets);
		order.setCharsets(Validator.isNull(charsets)?"UTF-8":charsets);

		String total_fee=request.getParameter("OrdAmt");
		if(!Validator.isNull(total_fee)){
			Clazz.Annotation(GwOrders.class, "amount", total_fee);					
			order.setAmount(Amount.getIntAmount(total_fee,2));
			order.setPrice(order.getAmount()); //相等
			order.setQuantity(1);
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
		
		
		String RetUrl=request.getParameter("MerPriv");				
		Clazz.Annotation(GwOrders.class, "return_url", RetUrl);
		order.setReturn_url(RetUrl);
		
		String BgRetUrl=request.getParameter("BgRetUrl");
		Clazz.Annotation(GwOrders.class, "notify_url", BgRetUrl);
		order.setNotify_url(BgRetUrl);
		
		String sign_type=request.getParameter("signType");
		if(sign_type==null||"".equals(sign_type)){
			sign_type="MD5";
		}		
		Clazz.Annotation(GwOrders.class, "sign_type",sign_type.toUpperCase());
		order.setSign_type(sign_type);
		
		
		String payment_type="1";
		Clazz.Annotation(GwOrders.class, "order_type", payment_type);
		order.setOrder_type(payment_type);
		
		String seller_email=request.getParameter("seller_email");
		if(!Validator.isNull(seller_email)){
			Clazz.Annotation(GwOrders.class, "seller_name", seller_email);
			order.setSeller_name(seller_email);
		}
		
		String subject=request.getParameter("subject");
		Clazz.Annotation(GwOrders.class, "subject", Validator.isNull(subject)?"ChinaPnr":subject);
		order.setSubject(Validator.isNull(subject)?"ChinaPnr":subject);
		
		String body=request.getParameter("body");
		if(Validator.isNull(body)){
			body=OrdId;
		}
		Clazz.Annotation(GwOrders.class, "bodys", body);
		order.setBodys(body);	
		
		String defaultbank=request.getParameter("GateId");
		Clazz.Annotation(GwOrders.class, "preference", !Validator.isNull(defaultbank)?Constants.ACQ_CODE_CHINAPNR+"-"+defaultbank.toUpperCase():Constants.ACQ_CODE_CHINAPNR);
		order.setPreference(!Validator.isNull(defaultbank)?Constants.ACQ_CODE_CHINAPNR+"-"+defaultbank.toUpperCase():Constants.ACQ_CODE_CHINAPNR);
		
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
		
		String sign=request.getParameter("ChkValue");
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
		String UsrMp=request.getParameter("UsrMp");
		String CmdId=request.getParameter("CmdId");
		mo.setSortstr(getFormOrderStr(Version,CmdId,MerId,OrdId,total_fee,defaultbank,UsrMp,RetUrl));
		return mo;
	
 }
	
	
	private String getFormOrderStr(String Version,String CmdId,String UsrId,String OrdId,String OrdAmt,String GateId,String UsrMp,String MerPriv){	
		
		StringBuffer signMsgBuffer = new StringBuffer();		
		 signMsgBuffer.append(Version);
		 signMsgBuffer.append(CmdId);
		 signMsgBuffer.append(UsrId);
		 signMsgBuffer.append(OrdId);
		 signMsgBuffer.append(OrdAmt);
		 signMsgBuffer.append(GateId);
		 signMsgBuffer.append(UsrMp);
		 signMsgBuffer.append(MerPriv);
		return signMsgBuffer.toString();
	}
}
		
		

