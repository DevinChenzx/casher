package ebank.web.common.util;


import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.Clazz;
import ebank.core.domain.MerchantOrder;
import ebank.core.model.domain.TradePrecard;

public class PreCardInterface implements IPartnerInterface {

	public MerchantOrder getMerchantOrderByService(HttpServletRequest request)
			throws ServiceException {
		MerchantOrder mo=new MerchantOrder();	   
		   
		if("create_direct_pay_by_user".equals(request.getParameter("service"))
		  ||"trade_create_by_buyer".equals(request.getParameter("service"))
		  ||"distribute_royalty".equals(request.getParameter("service"))){
			//GwOrders order=new GwOrders();
			TradePrecard tradePrecard=new TradePrecard();
			/*String service=request.getParameter("service");
			Clazz.Annotation(GwOrders.class, "service", service);
			order.setService(service);				
			
			String sign=request.getParameter("sign");
			Clazz.Annotation(MerchantOrder.class, "sign", sign);
			mo.setSign(sign);
			
			String sign_type=request.getParameter("sign_type");
			Clazz.Annotation(GwOrders.class, "sign_type", sign_type==null?"":sign_type.toUpperCase());
			order.setSign_type(sign_type);
			 
			String partner=request.getParameter("partner");
			Clazz.Annotation(GwOrders.class, "partnerid", partner);
			order.setPartnerid(Long.parseLong(partner));
			
			String _input_charset=request.getParameter("_input_charset");
			if(Validator.isNull(_input_charset)){
				_input_charset="GBK";
			}
			Clazz.Annotation(GwOrders.class, "charsets", _input_charset.toUpperCase());
			order.setCharsets(_input_charset);
			*/
			String out_trade_no=request.getParameter("out_trade_no");
			Clazz.Annotation(TradePrecard.class, "id", out_trade_no);
			tradePrecard.setId(out_trade_no);
			
			
			
			
			String return_url=request.getParameter("return_url");				
			Clazz.Annotation(TradePrecard.class, "return_url", return_url);
			tradePrecard.setMerrcvurl(return_url);
			
			String subject=request.getParameter("subject");
			Clazz.Annotation(TradePrecard.class, "ext1", subject);
			tradePrecard.setExt1(subject);
			
			String body=request.getParameter("body");
			Clazz.Annotation(TradePrecard.class, "productname", body);
			tradePrecard.setProductname(body);				
							
			
		 if(!Validator.isNull(request.getParameter("price"))){
				String price=request.getParameter("price");
				Clazz.Annotation(TradePrecard.class, "amount", price);
				tradePrecard.setAmount(Amount.getIntAmount(price,2));
				
				String quantity=request.getParameter("quantity");
				Clazz.Annotation(TradePrecard.class, "productnum", quantity);
				tradePrecard.setProductnum(Integer.parseInt(quantity));
				
				tradePrecard.setAmount(tradePrecard.getAmount()*tradePrecard.getProductnum());
			}

						
			tradePrecard.setCurcode("CNY");		
			tradePrecard.setCurcode(Validator.currencyStanderize(tradePrecard.getCurcode()));
			if(Validator.isNull(tradePrecard.getOrdertime())){
				
				tradePrecard.setOrdertime(new Date());
			}
			
			mo.setTradePrecard(tradePrecard);
			
		}
		else{
			
			throw new ServiceException(EventCode.SLA_SERVICENOTFOUND);
		}
		mo.setSortstr(PartnerInterface.getFormOrderStr(request));
		return mo;
		
	}
	

}
