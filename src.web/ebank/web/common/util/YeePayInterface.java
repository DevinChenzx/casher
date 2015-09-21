package ebank.web.common.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ebank.core.common.Constants;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.Clazz;
import ebank.core.common.util.Udate;
import ebank.core.domain.MerchantOrder;
import ebank.core.model.domain.GwOrders;


public class YeePayInterface implements IPartnerInterface {
	
		private static Log log=LogFactory.getLog(PartnerInterface.class);
		
		public MerchantOrder getMerchantOrderByService(HttpServletRequest request) throws ServiceException{
			    
				 MerchantOrder mo=new MerchantOrder();	   
			 
				GwOrders order=new GwOrders();
				String   p0_Cmd		=formatString(request.getParameter("p0_Cmd"));
				String   p1_MerId   =formatString(request.getParameter("p1_MerId"));
				String   p4_Cur   =formatString(request.getParameter("p4_Cur"));
				String   pr_NeedResponse   =formatString(request.getParameter("pr_NeedResponse"));
				String    p2_Order     = formatString(request.getParameter("p2_Order"));           		// 商户订单号
				String	  p3_Amt       = formatString(request.getParameter("p3_Amt"));      	   		// 支付金额
				String	  p5_Pid 	   = formatString(request.getParameter("p5_Pid"));	       	   		// 商品名称
				String	  p6_Pcat  	   = formatString(request.getParameter("p6_Pcat"));	       	   		// 商品种类
				String 	  p7_Pdesc   = formatString(request.getParameter("p7_Pdesc"));		   			// 商品描述
				String 	  p8_Url 	  = formatString(request.getParameter("p8_Url")); 		       		// 商户接收支付成功数据的地址
				String 	  p9_SAF 	= formatString(request.getParameter("p9_SAF")); 			   		// 需要填写送货信息 0：不需要  1:需要
				String 	  pa_MP 	= formatString(request.getParameter("pa_MP"));         	   			// 商户扩展信息
				String    pd_FrpId   = formatString(request.getParameter("pd_FrpId"));       	   		// 支付通道编码
				String    _format   = formatString(request.getParameter(Constants.FORMAT_FILED));       	   		// 支付通道编码
				String sortStr=sort(p0_Cmd,p1_MerId,p2_Order,p3_Amt,p4_Cur,p5_Pid,p6_Pcat,p7_Pdesc,p8_Url, p9_SAF, pa_MP, pd_FrpId,pr_NeedResponse,_format);
				
					String service="yeepay";
					Clazz.Annotation(GwOrders.class, "service", service);
					order.setService(service);				
					 
					String sign=request.getParameter("hmac");
					Clazz.Annotation(MerchantOrder.class, "sign", sign);
					mo.setSign(sign);
					
					String sign_type=request.getParameter("signType");
					if(sign_type==null||"".equals(sign_type)){
						sign_type="MD5";
					}		
					Clazz.Annotation(GwOrders.class, "sign_type",sign_type.toUpperCase());
					order.setSign_type(sign_type);
					
					order.setExp_dates("15d");
					
					String partner=p1_MerId;
					Clazz.Annotation(GwOrders.class, "partnerid", partner);
					order.setPartnerid(Long.parseLong(partner));
					
					String _input_charset=request.getParameter("_input_charset");
					if(Validator.isNull(_input_charset)){
						_input_charset="GBK";
					}
					Clazz.Annotation(GwOrders.class, "charsets", _input_charset.toUpperCase());
					order.setCharsets(_input_charset);
					
					String out_trade_no=request.getParameter("p2_Order");
					Clazz.Annotation(GwOrders.class, "ordernum", out_trade_no);
					order.setOrdernum(out_trade_no);
					
					
					String paymethod="bankPay";
					if(!Validator.isNull(paymethod)){
						Clazz.Annotation(GwOrders.class, "paymethod", paymethod);
						order.setPaymethod(paymethod);
						
						String defaultbank=pd_FrpId;
						Clazz.Annotation(GwOrders.class, "preference", !Validator.isNull(defaultbank)?Constants.ACQ_CODE_YEEPAY+"-"+defaultbank.toUpperCase():Constants.ACQ_CODE_YEEPAY);
						order.setPreference(!Validator.isNull(defaultbank)?Constants.ACQ_CODE_YEEPAY+"-"+defaultbank.toUpperCase():Constants.ACQ_CODE_YEEPAY);
					}else{
						order.setPaymethod("bankPay");
					}
					
					String return_url=p8_Url;//request.getParameter("return_url");				
					Clazz.Annotation(GwOrders.class, "return_url", return_url);
					order.setReturn_url(return_url);
					
					String payment_type="1";
					Clazz.Annotation(GwOrders.class, "order_type", payment_type);
					order.setOrder_type(payment_type);
					
					String subject=p5_Pid;
					Clazz.Annotation(GwOrders.class, "subject",Validator.isNull(subject)?"YeePay":subject);
					order.setSubject(Validator.isNull(subject)?"YeePay":subject);
					
					String body=p7_Pdesc;
					Clazz.Annotation(GwOrders.class, "bodys", Validator.isNull(body)?p2_Order:body);
					order.setBodys(Validator.isNull(body)?p2_Order:body);				
					
				
					
					String seller_id   =request.getParameter("seller_id");
					String buyer_email =request.getParameter("buyer_email");
					String buyer_id    =request.getParameter("buyer_id");
					if(!Validator.isNull(seller_id)){
						Clazz.Annotation(GwOrders.class, "seller_id", seller_id);
						order.setSeller_id(seller_id);
					}
					if(!Validator.isNull(buyer_email)){
						Clazz.Annotation(GwOrders.class, "buyer_name", buyer_email);
						order.setBuyer_name(buyer_email);
					}
					if(!Validator.isNull(buyer_id)){
						Clazz.Annotation(GwOrders.class, "buyer_id", buyer_id);
						order.setBuyer_id(buyer_id);
					}				
					
					 if(!Validator.isNull(request.getParameter("p3_Amt"))){
						String price=request.getParameter("p3_Amt");
						Clazz.Annotation(GwOrders.class, "price", price);
						order.setPrice(Amount.getIntAmount(price,2));
						
						String quantity="1";request.getParameter("quantity");
						Clazz.Annotation(GwOrders.class, "quantity", quantity);
						order.setQuantity(Integer.parseInt(quantity));
						
						order.setAmount(order.getPrice()*order.getQuantity());
					}
					 String notifyUrl=request.getParameter("notify_url");
					Clazz.Annotation(GwOrders.class, "notify_url", notifyUrl);
					order.setNotify_url(notifyUrl);						
					
				
					
					
					order.setApiversion(Constants.API_YEEPAY);
					String remoteip=RequestUtil.getIpAddr(request);
					if(remoteip.indexOf(",")>0) remoteip=remoteip.split(",")[0];
					order.setIps(remoteip);				
					if(Validator.isNull(order.getDiscount())){
						order.setDiscount(0);
					}
					
					String ext_param1    =request.getParameter("pa_MP");
					if(!Validator.isNull(ext_param1)){
						Clazz.Annotation(GwOrders.class, "ext_param1", ext_param1);
						order.setExt_param1(ext_param1);
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
					order.setCurrency(Validator.currencyStanderize(order.getCurrency()));
					if(Validator.isNull(order.getOrderdate())){
						order.setOrderdate(Udate.format(new Date(),"yyyyMMdd"));
						
					}
					if(Validator.isNull(order.getLocale())){
						order.setLocale(LocaleUtil.getLocale(null, "CN"));
					}
					mo.setOrders(order);
					
				
				mo.setSortstr(sortStr);
				return mo;
		
	}
		public String formatString(String text){ 
			if(text == null) {
				return ""; 
			}
			return text;
		}
    private String sort(String p0_Cmd,String p1_MerId,
			String p2_Order, String p3_Amt, String p4_Cur,String p5_Pid, String p6_Pcat,
			String p7_Pdesc,String p8_Url, String p9_SAF,String pa_MP,String pd_FrpId,
			String pr_NeedResponse,String _format){
    	StringBuffer sValue = new StringBuffer();
		// 业务类型
		sValue.append(p0_Cmd);
		// 商户编号
		sValue.append(p1_MerId);
		// 商户订单号
		sValue.append(p2_Order);
		// 支付金额
		sValue.append(p3_Amt);
		// 交易币种
		sValue.append(p4_Cur);
		// 商品名称
		sValue.append(p5_Pid);
		// 商品种类
		sValue.append(p6_Pcat);
		// 商品描述
		sValue.append(p7_Pdesc);
		// 商户接收支付成功数据的地址
		sValue.append(p8_Url);
		// 送货地址
		sValue.append(p9_SAF);
		// 商户扩展信息
		sValue.append(pa_MP);
		// 银行编码
		sValue.append(pd_FrpId);
		// 应答机制
		sValue.append(pr_NeedResponse);
		//类型
//		sValue.append(_format);
		return sValue.toString();
    }
	
    
}
