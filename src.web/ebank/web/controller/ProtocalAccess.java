package ebank.web.controller;

import java.security.Key;
import java.util.HashMap;

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
import ebank.core.common.util.CryptUtil;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwViewUser;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.util.PartnerInterface;
import ebank.web.common.util.RequestUtil;
import ebank.web.common.util.Validator;
import ebank.web.common.util.XSerialize;

/**
 * protcal payment
 * 
 * @author Kitian
 * 
 */
public class ProtocalAccess implements Controller {

	private static Key key = XSerialize.getKey(null);
	private OrderService orderService;
	private UserService userService;
	private Log log = LogFactory.getLog(this.getClass());

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse response) throws Exception {

		String servicename = req.getParameter("service");
		String tradeid = req.getParameter("trade_id");
		String partnerid = req.getParameter("partner");
		String sign = req.getParameter("sign");
		String sign_type = req.getParameter("sign_type");
		String ebankenable = req.getParameter("ebankenable");

		log.info("ProtocalAccess:" + servicename + " " + tradeid + " "
				+ partnerid + " " + sign + " " + sign_type + "　" + ebankenable);
		try {
			if (Validator.isNull(servicename)
					|| !"tradeid_payment".equalsIgnoreCase(servicename)) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY, new String[]{"service"});
			}
			if (Validator.isNull(tradeid)) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY, new String[]{"trade_id"});
			}
			if (Validator.isNull(partnerid)) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY, new String[]{"partner"});
			}
			if (Validator.isNull(sign)) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY, new String[]{"sign"});
			}
			if (Validator.isNull(sign_type)
					|| !"md5".equalsIgnoreCase(sign_type)) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY,
						new String[]{"sign_type"});
			}
			if (Validator.isNull(ebankenable)) {
				ebankenable = "";
			}
			GwViewUser user = null;

			GwOrders order = orderService.findOrderByPk(tradeid);
			if (order == null) {
				throw new ServiceException(EventCode.ORDER_PAYNOTFOUND);
			}
			if (!"0".equals(order.getOrdersts())) {
				throw new ServiceException(EventCode.ORDER_STS_NOTPAY);
			}
			if (!String.valueOf(order.getPartnerid()).equalsIgnoreCase(
					partnerid)) {
				throw new ServiceException(EventCode.ORDER_NOTFOUND);
			}
			// 判断服务
			if ("10".equals(order.getRoyalty_type())) {
				user = userService.getViewUser(String.valueOf(partnerid),
						"royalty");
				String royaltyparam=order.getRoyalty_parameters();
				if(!Validator.isNull(royaltyparam)){
					String[] reg=royaltyparam.split("\\|");
					if(reg!=null&&reg.length>=1){
						for (int i = 0; i < reg.length; i++) {
							String[] items=reg[i].split("\\^");
							if(items!=null&&items.length>=2){
								for (int j = 0; j < items.length; j++) {
									if(j<=1&&items[i].matches(Constants.REG_EMAIL)){
										GwViewUser itemusers=userService.getUserWithIdAndName("",items[i]);
										if(itemusers==null) throw new ServiceException(EventCode.INVALIDATE_USER,new String[]{items[1]});
									}
								}								
							}else{
								throw new ServiceException(EventCode.WEB_PARAMFORMAT,new String[]{"royalty_parameter"});
							}
						}
					}else{
						throw new ServiceException(EventCode.WEB_PARAMFORMAT,new String[]{"royalty_parameter"});
					}
				}
			} else
				user = userService.getViewUser(String.valueOf(partnerid),
						"online");

			if (user == null || user.getMstate() != 1) {
				throw new ServiceException(EventCode.MERCHANT_STATEILL);
			}
			if (!"normal".equals(user.getStatus())) {
				log.debug("user state unnormal:" + user.getService_code() + " "
						+ user.getCustomer_no());
				throw new ServiceException(EventCode.MERCHANT_STATEILL);
			}
			// 验证签名
			String str = PartnerInterface.getFormOrderStr(req);
			String sign_str=Md5Encrypt.md5(str+user.getMd5Key(), order.getCharsets());
			log.info("sign string:" + str+" sign_str:"+sign_str+"　key5:"+user.getMd5Key().substring(user.getMd5Key().length()-5));
			if (!sign.equalsIgnoreCase(sign_str)) {
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
			}

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(WebConstants.MAP_KEY_ORDER, order);
			map.put(WebConstants.MAP_KEY_PERSISTENCE,
					XSerialize.serialize(order, key));
			map.put(WebConstants.MAP_KEY_ID, CryptUtil.encrypt(order.getId()));
			map.put("_ebankenable", "1".equals(ebankenable) ? 1 : 0);
			return new ModelAndView(Constants.APP_VERSION + "/pureisms", "m",
					RequestUtil.HtmlEscapeMap(map));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (!(e instanceof ServiceException))
				e.printStackTrace();
			return new ModelAndView(WebConstants.ERROR_PAGE,
					WebConstants.ERROR_MODEL, new WebError(e));

		}
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
