package ebank.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ebank.core.OrderService;
import ebank.core.PayResultService;
import ebank.core.UserService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.common.util.SHA1;
import ebank.core.domain.PayResult;
import ebank.core.logic.DirectPay_Impl;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwTrxs;
import ebank.core.model.domain.GwViewUser;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.util.LocaleUtil;
import ebank.web.common.util.RequestUtil;
import ebank.web.common.util.Validator;

public class PayAccount implements Controller{
	
	private OrderService orderService;
	private DirectPay_Impl directPayService;
	private PayResultService payResultService;
	private UserService userService;
	
	private String resultExport;
	
	
	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		try{			
			
			String orderid=CryptUtil.decrypt(req.getParameter(WebConstants.MAP_KEY_ID));		
			
			String encpaypwd=(String)req.getSession().getAttribute(WebConstants.SESSION_USER_PAYWD);
			if(Validator.isNull(encpaypwd)){
				throw new ServiceException(EventCode.BUYER_WAIT_OVERTIME);
			}
			String paypwd=CryptUtil.decrypt(encpaypwd);
			
			if(Validator.isNull(paypwd)){
				throw new ServiceException(EventCode.WEB_PARAMEMPTY,"buyer_paypwd");
			}	
			GwOrders torder=orderService.findOrderByPk(orderid);
			GwViewUser user=userService.findByUsername(torder.getBuyer_name());
			if(user==null) throw new ServiceException(EventCode.BUYER_NOTFOUND); //buyer not found
			if(!"normal".equals(user.getStatus())){
				throw new ServiceException(EventCode.BUYER_STATEILL);
			}
			
			//判断是否商旅卡,如果是商旅卡则不需要输入手机验证码,否则需要
			String istravel = req.getParameter("accttravel");
			if("".equals(istravel)){
				if(req.getParameter("mverify_code")==null ||"".equals(req.getParameter("mverify_code").toString())){
					throw new ServiceException(EventCode.PAGE_MOBILE_RVERFIYCODE);
				}
				
				if(!req.getParameter("mverify_code").toString().equals(torder.getQuery_key())){
					throw new ServiceException(EventCode.PAGE_MOBILE_VERFIYCODE);
				}
			}
			
			String encpwd=user.getOperid()+"p"+paypwd;
			
			String paymentid=directPayService.directPaybyOrder(torder, new SHA1().getDigestOfString(encpwd.getBytes()));			
				
			GwOrders order=orderService.findOrderByPaymentid(paymentid);
							
			if(order!=null&&("3".equals(order.getOrdersts())||"1".equals(order.getOrdersts()))){		
					PayResult result=new PayResult("");
					Map<String,Object> mp=new HashMap<String,Object>();
					result.setOrder(order);
					GwTrxs trx=new GwTrxs();
					trx.setAmount(order.getAmount());
					result.setTrx(trx);
					mp.put("result",result);
					mp.put("action","#");
					if(order!=null){
						mp.put("action",RequestUtil.getAction(order.getReturn_url()));
						mp.put("locale",LocaleUtil.getLocale(order.getLocale(),"CN"));
						mp.put("forms", payResultService.mapresult(order,false));
					}				
				return new ModelAndView(this.resultExport,"res",RequestUtil.HtmlEscapeMap(mp));	
			}else
				throw new ServiceException(EventCode.ORDER_STS_NOTPAY);
			
						
		}catch(Exception e){	
			e.printStackTrace();
			return new ModelAndView(WebConstants.ERROR_PAGE,  WebConstants.ERROR_MODEL,new WebError(e));			
		
		}
	}
	

	public void setResultExport(String resultExport) {
		this.resultExport = resultExport;
	}


	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}


	public void setDirectPayService(DirectPay_Impl directPayService) {
		this.directPayService = directPayService;
	}


	public void setPayResultService(PayResultService payResultService) {
		this.payResultService = payResultService;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	


}
