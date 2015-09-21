package ebank.web.controller;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.captcha.Captcha;
import ocx.AESWithJCE;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ebank.core.AccountService;
import ebank.core.OrderService;
import ebank.core.UserService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.common.util.SHA1;
import ebank.core.model.domain.GwDirectBind;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwViewAccount;
import ebank.core.model.domain.GwViewUser;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.util.XSerialize;

public class CompositeCashier implements Controller{

	private static Key key=XSerialize.getKey(null);
	private OrderService orderService;
	private UserService userService;
	private AccountService accountService;
	
	
	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		try {
			//===========================================密码控件部分====================================================================			
			 String local_network = null;
		     String local_disk =null;
		     String local_nic =null;
		     String network=null;
		     String disk=null;
		     String nic=null;
		     
		     String mcrypt_key_1=(String)req.getSession().getAttribute("mcrypt_key");
		     String password1 = req.getParameter("buyer_paypwd").trim();
		     
		     local_network = req.getParameter("local_network").trim();//加密后的客户端网卡和MAC信息;    
		     local_disk = req.getParameter("local_disk").trim();//获取加密后的客户端硬盘序列号;
		     local_nic = req.getParameter("local_nic").trim();//获取加密后的客户端cpuid号;  
		    //String username=AESWithJCE.getResult(mcrypt_key_1,username1);//调用解密接口。mcrypt_key_1为获取的32位随机数，username1为用户名密文；
		    String password=AESWithJCE.getResult(mcrypt_key_1,password1);//调用解密接口。mcrypt_key_1为获取的32位随机数，password1为密码的密文；
		   
		    if(local_network!=null) network=AESWithJCE.getResult(mcrypt_key_1,local_network);//调用解密接口.获取网卡信息;
		    if(local_disk!=null)  disk=AESWithJCE.getResult(mcrypt_key_1,local_disk);//调用解密接口.获取硬盘序列号信息;
		    if(local_nic!=null)  nic=AESWithJCE.getResult(mcrypt_key_1,local_nic);//调用解密接口.获取cpuid号信息;    
		        
		    //req.getSession().invalidate();//清除session;
			
			//=============================================密码控件部分==================================================================
			String orderid=req.getParameter(WebConstants.MAP_KEY_ORDERID);
			String pers=req.getParameter(WebConstants.MAP_KEY_PERSISTENCE);
			
			Map<String,Object> mp=new HashMap<String,Object>();
			String buyer_name=req.getParameter("buyer_name");
			/*String buyer_paypwd=req.getParameter("buyer_paypwd");*/
			String buyer_paypwd=password;
			
			mp.put("_buyer_name", buyer_name);
			
			Captcha verifycode=(Captcha)req.getSession().getAttribute("captcha");
			if(verifycode==null||!verifycode.isCorrect(req.getParameter("verify_code").toLowerCase())){
				throw new ServiceException(EventCode.PAGE_VERFIYCODE);
			}else{
				req.getSession().removeAttribute("captcha");
			}		
			
			GwViewUser user=userService.findByUsername(buyer_name);
			if(user==null) throw new ServiceException(EventCode.BUYER_NOTFOUND); //buyer not found
			if(!"normal".equals(user.getStatus())){
				throw new ServiceException(EventCode.BUYER_STATEILL);
			}
			
			String encpwd=user.getOperid()+"p"+buyer_paypwd;				
			GwViewAccount vu=userService.findAccountBypwdAndAuth(buyer_name, 
					new SHA1().getDigestOfString(encpwd.getBytes()));
			
			if(vu==null) throw new ServiceException(EventCode.BUYER_PAYPWD_WRONG); //user null or paypass wrong
			if(!"normal".equals(vu.getOper_sts())) throw new ServiceException(EventCode.BUYER_STATEILL);
			
			GwViewAccount va=accountService.getAccount(vu.getAcct_id()+"");
			if(va==null) throw new ServiceException(EventCode.BUYER_ACCOUNT_NOTFOUND);
			

			
			long balance=va.getAcct_balance();			
			mp.put("_buyer_balance",balance);
			GwOrders order=(GwOrders)XSerialize.deserialize(pers, key);
			
			String istravel="";
			
			/**=========================================商旅卡余额支付=========================================**/
			if("K".equals(vu.getUser_type())){
				GwDirectBind gd = userService.findDirectBindByCustomerNoAndPayAccountNo(order.getSeller_id(), String.valueOf(vu.getAcct_id()));
				istravel = "y";
				if(gd==null) throw new ServiceException(EventCode.PAYACCOUNTNO_UNBINDCODE);
			}
			
			/**=========================================商旅卡余额支付=========================================**/
			/*GwOrders torder=orderService.findOrderByPk(order.getId());
			if(!req.getParameter("mverify_code").toString().equals(torder.getQuery_key())){
				throw new ServiceException(EventCode.PAGE_MOBILE_VERFIYCODE);
			}else{*/
			req.getSession().setAttribute(WebConstants.SESSION_USER_PAYWD,CryptUtil.encrypt(buyer_paypwd));
			if(!order.getOrdernum().equals(orderid)){ //orde exception
				throw new ServiceException(EventCode.ORDER_CHECK_EXCEPTION);
			}
			if(String.valueOf(vu.getUser_id()).equals(order.getSeller_id())){
				throw new ServiceException(EventCode.SELLER_SAME_BUYER); //pay self
			}
			//check enable ebank
			if(balance<order.getAmount()&&"0".equalsIgnoreCase(req.getParameter("_ebankenable"))){
				throw new ServiceException("202004");
			}
			mp.put("_ebankenable",req.getParameter("_ebankenable"));
			mp.put(WebConstants.MAP_KEY_ORDER, order);
			mp.put(WebConstants.MAP_KEY_PERSISTENCE, pers);
			mp.put(WebConstants.MAP_KEY_CHANNELCODE, order.getPreference());
			mp.put(WebConstants.MAP_KEY_ID, req.getParameter(WebConstants.MAP_KEY_ID));
			mp.put("_bankname", order.getPreference());
			mp.put("istravel", istravel);
			
			order.setPaymethod("balancePay"); //
			order.setBuyer_id(user.getCustomer_no());
			order.setBuyer_name(buyer_name);
			order.setDirectpayamt(va.getAcct_balance());
			orderService.tx_updateOrderBuyerInfos(order);
			//}
			
			return new ModelAndView(Constants.APP_VERSION+"/composite","m",mp);
		}catch(Exception ex){
			if(!(ex instanceof ServiceException))
				ex.printStackTrace();
			return new ModelAndView(WebConstants.ERROR_PAGE,
		                            WebConstants.ERROR_MODEL,new WebError(ex));
		}
		
		
		
	}
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	
	
	

}
