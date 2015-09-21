package ebank.web.controller;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import ebank.core.OrderService;
import ebank.core.UserService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwViewUser;
import ebank.core.model.domain.GwViewAccount;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.util.RequestUtil;
import ebank.web.common.util.Validator;
import ebank.web.common.util.XSerialize;

public class PayInCashier implements Controller{
	private UserService userService;
	private OrderService orderService;	
	
	private Log log=LogFactory.getLog(this.getClass());
	private static Key key=XSerialize.getKey(null);
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try{
			RequestUtil.verifyInputBoxReq(request); //INPUT BOX CHECK
			GwViewAccount viewAccount=null;
			String pers=request.getParameter(WebConstants.MAP_KEY_PERSISTENCE);
			String orderid=request.getParameter(WebConstants.MAP_KEY_ORDERID);
			
			String username=null;
			if(Validator.isNull(pers)){
				throw new ServiceException(EventCode.WEB_PARAMNULL);
			}
			//instanced the morder;
			GwOrders morder=null;
			if(request.getSession().getAttribute(Constants.SESSION_KEY_MORDER)!=null){
				morder=(GwOrders)request.getSession().getAttribute(Constants.SESSION_KEY_MORDER);
				if(!morder.getOrdernum().equals(orderid)){
					morder=(GwOrders)XSerialize.deserialize(pers, key);
				}
			}
			if(morder==null){
				morder=(GwOrders)XSerialize.deserialize(pers, key);
			}
			Map<String,Object> mp=new HashMap<String,Object>();
			final String LEFT_LOGIN_ERR_KEY ="left_login_err_key";
			final String RIGHT_LOGIN_ERR_KEY="right_login_err_key";
			
			//login
			if("login".equals(request.getParameter("action"))){
				String paypwd=request.getParameter("password");
				username=request.getParameter("buyerUserName");				
				//validate the user login and get the accountinfo
				if(Validator.isNull(paypwd)){
					mp.put(LEFT_LOGIN_ERR_KEY, "支付密码空");
				}
				if(Validator.isNull(username)&&!mp.containsKey(LEFT_LOGIN_ERR_KEY)){
					mp.put(LEFT_LOGIN_ERR_KEY, "请输入用户密码");
				}
				if(!mp.containsKey(LEFT_LOGIN_ERR_KEY)){
					viewAccount=userService.findAccountBypwdAndAuth(username,paypwd);
					if(Validator.isNull(viewAccount)){
						mp.put(LEFT_LOGIN_ERR_KEY, "用户名和支付密码不匹配,请右侧登录");
					}else{
						morder.setBuyer_id(String.valueOf(viewAccount.getUser_id()));
						morder.setBuyer_name(viewAccount.getLogin_recepit());
						if(viewAccount.getUser_id()==Long.parseLong(morder.getSeller_id())){
							mp.put(RIGHT_LOGIN_ERR_KEY, "不能给自己付款");
						}
					}
				}
				
			}
			GwViewUser user=null;
			//
			if("quickLogin".equals(request.getParameter("action"))){
				username=request.getParameter("buyerUserEmail");
				morder.setBuyer_name(username);
				if(Validator.isNull(username)){
					mp.put(RIGHT_LOGIN_ERR_KEY, "请输入登录名称");
				}else{
					if(!username.matches(Constants.REG_MOBILEPHONE)&&!username.matches(Constants.REG_EMAIL)){
						mp.put(RIGHT_LOGIN_ERR_KEY, "请输入正确格式");
					}
				}
				if(!mp.containsKey(RIGHT_LOGIN_ERR_KEY)){
					user=userService.findByUsername(username);
					if(user!=null){
						mp.put(RIGHT_LOGIN_ERR_KEY, "已存在该账号,请左侧登录");
					}
				}	
				
			}
			user=userService.findByUsername(username);
			
			//morder.getOrders()
			HashMap<String,Object> map=new HashMap<String,Object>();
			
			map.put(WebConstants.MAP_KEY_PERSISTENCE, pers);
			
			//force session
			request.getSession().setAttribute(WebConstants.SESSION_USER_LOGIN_KEY, username);
			
			if(mp.containsKey(LEFT_LOGIN_ERR_KEY)||mp.containsKey(RIGHT_LOGIN_ERR_KEY)){
				map.put("LEFT_RESP_MSG", mp.get(LEFT_LOGIN_ERR_KEY)==null?"":mp.get(LEFT_LOGIN_ERR_KEY));
				map.put("RIGHT_RESP_MSG",mp.get(RIGHT_LOGIN_ERR_KEY)==null?"":mp.get(RIGHT_LOGIN_ERR_KEY));
				map.put(WebConstants.MAP_KEY_ORDER, morder);
				return new ModelAndView(Constants.APP_VERSION+"/portal","m",RequestUtil.HtmlEscapeMap(map));
			}
			//update buyer name;
			//return new ModelAndView(Constants.APP_VERSION+"/options","m",map);
			//orderid redirect to the options;
			String userid="";
			if(user!=null){
				userid=String.valueOf(user.getUserId());
			}
			orderService.tx_updateOrderBuyerInfo(morder.getId(),username,userid);
			
			request.getSession().removeAttribute(Constants.SESSION_KEY_MORDER);
			request.getSession().setAttribute(Constants.SESSION_KEY_MORDER, morder);
			
			map.put(WebConstants.MAP_KEY_ID, CryptUtil.encrypt(morder.getId()));
			map.put(WebConstants.Action,"/Pay");
			return new ModelAndView(Constants.APP_VERSION+"/redirect",WebConstants.PaRes,RequestUtil.HtmlEscapeMap(map));
			
		}catch(Exception ex){
			if(!(ex instanceof ServiceException))
				ex.printStackTrace();
			return new ModelAndView(WebConstants.ERROR_PAGE,
		                            WebConstants.ERROR_MODEL,new WebError(ex));
			
		}
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}	
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	
		

}
