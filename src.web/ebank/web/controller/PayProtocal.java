package ebank.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import beartool.Md5Encrypt;
import ebank.core.OrderService;
import ebank.core.UserService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.domain.MerchantOrder;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwViewUser;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.util.PartnerFactory;
import ebank.web.common.util.PartnerInterface;
import ebank.web.common.util.RequestUtil;
import ebank.web.common.util.Validator;

/**
 * 协议付款
 * @author Kitian
 *
 */
public class PayProtocal implements Controller{

	private Log log=LogFactory.getLog(this.getClass());
	private PartnerFactory factory;
	private OrderService orderService;
	private UserService userService;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
         log.info("protcal payment remote host:"+request.getHeader("host")+" service url:"+request.getHeader("REFERER"));
		response.setContentType("text/html; charset=UTF-8");
		try {			
			//得到商户订单对象
			MerchantOrder morder= factory.getInstance(request);//MerchantOrder morder=PartnerInterface.getMerchantOrderByService(request);
			//验证商户号
			
			////验证数据域
			if(morder!=null){
				PartnerFactory.validateOrder(morder);
			}
			GwOrders order=morder.getOrders();			
			if(order==null){
				throw new ServiceException(EventCode.WEB_PARAMNULL);
			}	
			GwViewUser user=null;
			
			//判断代扣服务			
			if("10".equals(morder.getOrders().getRoyalty_type())){
				user=PartnerFactory.ValidateRoyaltyParam(userService, morder.getOrders().getRoyalty_parameters(),
						   morder.getOrders().getAmount(),String.valueOf(morder.getOrders().getPartnerid()));
			}else{
				user=userService.getViewUser(String.valueOf(morder.getOrders().getPartnerid()),"online");
			}
			if(user==null||user.getMstate()!=1){
				throw new ServiceException(EventCode.MERCHANT_STATEILL);
			}
			if(!"normal".equals(user.getStatus())){
				log.debug("user state unnormal:"+user.getService_code()+" "+user.getCustomer_no());
				throw new ServiceException(EventCode.MERCHANT_STATEILL);
			}
			//验证签名			
			if(Constants.API_WONDERPAY.equals(morder.getOrders().getApiversion())){
				if(Validator.isNull(morder.getSign())||!morder.getSign().equals(Md5Encrypt.md5(morder.getSortstr()+user.getMd5Key(),order.getCharsets()))){
					throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);				
				}			
			}else{
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN); 	
			}
			
			order=(GwOrders)RequestUtil.HtmlEscape(order); //转义对象		
			//CHECK SELLER
			
//			if(Validator.isNull(order.getSeller_id())){
//				order.setSeller_id(String.valueOf(order.getPartnerid()));
//			}			
			GwViewUser seller=userService.getUserWithIdAndName(order.getSeller_id(),order.getSeller_name());
			if(seller!=null){
				if(Validator.isNull(order.getSeller_id())){
					morder.getOrders().setSeller_id(String.valueOf(seller.getCustomer_no()));
				}
				if(Validator.isNull(order.getSeller_name())){
					morder.getOrders().setSeller_name(seller.getLogin_recepit());
				}				
				if(Validator.isNull(order.getSeller_remarks()))
					order.setSeller_remarks(seller.getUserAlias());
			}else{
				throw new ServiceException(EventCode.SELLER_NOTFOUND);
			}
			//CHECK BUYER
			if(!(Validator.isNull(order.getBuyer_id())&&Validator.isNull(order.getBuyer_name()))){
				GwViewUser buyer=userService.getUserWithIdAndName(order.getBuyer_id(),order.getBuyer_name());
				if(buyer==null){
					throw new ServiceException(EventCode.BUYER_NOTFOUND);
				}else{
					//reset the buyer
					morder.getOrders().setBuyer_id(String.valueOf(buyer.getCustomer_no()));
					morder.getOrders().setBuyer_name(buyer.getLogin_recepit());
					morder.getOrders().setBuyer_realname(buyer.getUserAlias());
				}
			}
			if(order.getPreference()==null)	order.setPreference(WebConstants.DEFAULT_BANK_SELECTED);
						
			String orderid="";			
			orderid=orderService.tx_savePostOrder(morder);
			
			if("synchronize".equalsIgnoreCase(request.getParameter("pageflow"))){
				HashMap<String,Object> mp=new HashMap<String,Object>();
				mp.put("service", "tradeid_payment");
				mp.put("trade_id",orderid);
				mp.put("partner",String.valueOf(morder.getOrders().getPartnerid()));
				mp.put("ebankenable", "1".equals(request.getParameter("ebankenable")) ? "1" : "0");								
				mp.put(WebConstants.Action,"/TradeIdAccess.do");
				String sign=PartnerInterface.getMapOrderStr(mp);
				mp.put("sign", Md5Encrypt.md5(sign+user.getMd5Key(), morder.getOrders().getCharsets()));
				mp.put("sign_type", "md5");
				return new ModelAndView(Constants.APP_VERSION+"/redirect",WebConstants.PaRes,RequestUtil.HtmlEscapeMap(mp));
				
			}else
				response.getOutputStream().write(builderResult(true,orderid).getBytes());
		}catch(Exception ex){
			if("synchronize".equalsIgnoreCase(request.getParameter("pageflow"))){
				return new ModelAndView(WebConstants.ERROR_PAGE,
                        WebConstants.ERROR_MODEL,new WebError(ex));
			}
			else
				response.getOutputStream().write(builderResult(false,new WebError(ex).getEventid()).getBytes());
				
		}
		return null;
	}
	
	private String builderResult(boolean success,String result){
		if(success){
			return "{respcode:00,resmsg:success,tradeid:"+result+"}";
		}else{
			return "{respcode:01,resmsg:failure,respmsg:"+result+"}";
		}
	}

	public void setFactory(PartnerFactory factory) {
		this.factory = factory;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	
	

}
