package ebank.web.controller;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ebank.core.OrderService;
import ebank.core.UserService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwViewUser;
import ebank.core.remote.SmsClientService;
import ebank.web.common.WebConstants;
import ebank.web.common.util.XSerialize;

public class SendSMS implements Controller {
	
	Logger log = Logger.getLogger(this.getClass());
	private static Key key=XSerialize.getKey(null);
	private SmsClientService smsClientService;
	private OrderService orderService;
	private UserService userService;
	
	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		String mobileCode;
		String buyer_name = req.getParameter("buyer_name");
		String orderid=req.getParameter(WebConstants.MAP_KEY_ORDERID);
		String pers=req.getParameter(WebConstants.MAP_KEY_PERSISTENCE);
		
		Map<String,Object> mp=new HashMap<String,Object>();
		
		GwOrders order=(GwOrders)XSerialize.deserialize(pers, key);
		GwOrders torder=orderService.findOrderByPk(order.getId());
		
		if(!order.getOrdernum().equals(orderid)){ //orde exception
			throw new ServiceException(EventCode.ORDER_CHECK_EXCEPTION);
		}
		GwViewUser user=userService.findByUsername(buyer_name);
		if(user!=null)
		{
			if(user.getUserMobile()!=null && !"".equals(user.getUserMobile()))
			{
				if(!"".equals(torder.getQuery_key()) && torder.getQuery_key()!=null){
					mobileCode = torder.getQuery_key();
				}
				else{
					mobileCode = randomNum();
				}
				mp = smsClientService.getHttpResp(mobileCode, user.getUserMobile());
				
				JSONObject json = JSONObject.fromObject(mp.get("response").toString());
				
				if(mp!=null&&"00".equals(json.get("rescode"))){
					order.setQuery_key(mobileCode);
					log.info(String.valueOf(mp.get("response")));
					orderService.tx_updateOrderQueryKey(order);
					resp.getWriter().print("success");
				}
				else{
					resp.getWriter().print("failure");
				}
			}else{
				resp.getWriter().print("failureMobile");
			}
		}else{
			resp.getWriter().print("failureUser");
		}
		return null;
	}
	
	public String randomNum()
	{
		Random rd=new Random();
		String prefixnum="";
		String str="";	
		int length=prefixnum==null?0:prefixnum.length();
		for(int i=0;i<6-length;i++) str+=rd.nextInt(10);
		return str;
	}
	
	public void setSmsClientService(SmsClientService smsClientService) {
		this.smsClientService = smsClientService;
	}
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
