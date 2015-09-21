package ebank.web.controller;

import java.security.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ebank.core.OrderService;
import ebank.core.SLAService;
import ebank.core.UserService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.model.domain.CmCustomerChannel;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwSuborders;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.util.RequestUtil;
import ebank.web.common.util.Validator;
import ebank.web.common.util.XSerialize;

public class PayCashier implements Controller {
	private SLAService slaService;
	private OrderService orderService;
	private UserService userService;
	private static Key key = XSerialize.getKey(null);

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse arg1) throws Exception {
		try {
			String coid = RequestUtil.HtmlEscape(request
					.getParameter(WebConstants.MAP_KEY_ID));
			String paytype = RequestUtil.HtmlEscape(request
					.getParameter("_paytype"));
			String fraudcheck = RequestUtil.HtmlEscape(request
					.getParameter("fraudcheck"));
			String userID = RequestUtil.HtmlEscape(request
					.getParameter("userID"));
			String rePay = RequestUtil.HtmlEscape(request
					.getParameter("_rePay"));
			if (coid == null) {
				throw new ServiceException(EventCode.ORDER_CHECK_EXCEPTION);
			}

			String id = CryptUtil.decrypt(coid);

			Map<String, Object> b2cChannel = new HashMap<String, Object>();
			GwOrders order = orderService.findOrderByPk(id);
			// int channelid=slaService.getRouteChannelExt(order.getId(),
			// order.getPreference(),order.getAmount(),order.getPartnerid()+"");
			int channelid = 0;
			if (channelid == 0) {
				order.setPreference(WebConstants.DEFAULT_BANK_SELECTED);
			}
			// 获取当前商户的分配的渠道
			List<CmCustomerChannel> channelList = userService
					.findUserChannelList(userID);
			if (channelList.size() < 1) {
				throw new ServiceException(EventCode.SLA_NOUSERCHANNEL);
			}
			
			String[] bankList=new String[]{"CMB","ICBC","CCB","BOC","ABC","BOCM","SPDB","GDB","CITIC","CEB","CIB","SDB","CMBC","HXB"};
			
			for(int i=0;i<bankList.length*2;i++){
				
				CmCustomerChannel channel=new CmCustomerChannel();
				channel.setBank_code(bankList[i%14]);
				channel.setBank_type("0");
				channel.setChannel_code(bankList[i%14]);
				channel.setChannel_type(i<14?"1":"2");
				channel.setCustomerId(1111);
				channel.setId(1111);
				channel.setPayment_mode("0");
				channel.setPayment_type("2");
				channel.setVersion(0);
				
				channelList.add(channel);
			}
			if (order == null)
				throw new ServiceException(EventCode.ORDER_NOTFOUND);
			List<GwSuborders> subOrderslist = null;
			if ("12".equals(order.getRoyalty_type())) {
				subOrderslist = orderService.findSubOrderByGwOdersId(order
						.getId());
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("_b2cChannel", b2cChannel);
			map.put("_subOrderslist", subOrderslist);
			CmCustomerChannel channel = null;
			int b2cChannelNum = 0;
			int b2bChannelNum = 0;
			int cChannelNum = 0;
			int litepayNum = 0;
			
			int isDebitCard=0;
			int isCreditCard=0;
			
			int b2cflag = 0, b2bflag = 0, cflag = 0;
			String b2cStr = "", b2bStr = "", cStr = "";
			for (int i = 0; i < channelList.size(); i++) {
				channel = channelList.get(i);
				if (channel.getChannel_type().equals("1")
						&& !channel.getChannel_code().equals("TFT")&&(!channel.getChannel_code().equals("UNIONGATEWAY"))) {// b2c
					if (channel.getChannel_code().equals("ONLINE")
							|| channel.getChannel_code().equals("ONLINE_FIVE")
							|| channel.getChannel_code().equals("YINTONG")|| (channel.getChannel_code().equals("RONGXIN"))) {
						isDebitCard=1;
					}
						b2cChannelNum = b2cChannelNum + 1;
					b2cStr = channel.getChannel_code();
					// if(b2cflag==0){
					// if(!Validator.isNull(order.getPreference())&&channel.getChannel_code().equalsIgnoreCase(order.getPreference())){
					// map.put(WebConstants.MAP_KEY_B2CCHANNELCODE,order.getPreference());
					b2cflag = 1;
					// }
					// }

					map.put(WebConstants.MAP_KEY_B2CCHANNELCODE, order
							.getPreference());
				} else if (channel.getChannel_type().equals("3")) {// b2b
//					if (channel.getChannel_code().equals("ONLINE")
//							|| channel.getChannel_code().equals("ONLINE_FIVE")
//							|| channel.getChannel_code().equals("YINTONG")
//							|| channel.getChannel_code().equals("CHINABANK")) {
						b2bChannelNum = b2bChannelNum + 1;
//					}
					b2bStr = channel.getChannel_code();
					// if(b2bflag==0){
					// if(!Validator.isNull(order.getPreference())&&channel.getChannel_code().equalsIgnoreCase(order.getPreference())){
					// map.put(WebConstants.MAP_KEY_B2BCHANNELCODE,order.getPreference());
					b2bflag = 1;
					// }
					// }
					map.put(WebConstants.MAP_KEY_B2BCHANNELCODE, b2bStr);
				} else if (channel.getChannel_type().equals("2")
						&& !channel.getChannel_code().equals("TFT")&&(!channel.getChannel_code().equals("UNIONGATEWAY"))) {
//					if (channel.getChannel_code().equals("ONLINE")
//							|| channel.getChannel_code().equals("ONLINE_FIVE")
//							|| channel.getChannel_code().equals("YINTONG")
//							|| channel.getChannel_code().equals("CHINABANK")) {
					if (channel.getChannel_code().equals("ONLINE")
							|| channel.getChannel_code().equals("ONLINE_FIVE")
							|| channel.getChannel_code().equals("YINTONG")|| (channel.getChannel_code().equals("RONGXIN"))) {
						isCreditCard=1;
					}
						cChannelNum = cChannelNum + 1;
//					}
					cStr = channel.getChannel_code();
					if (cflag == 0) {
						if (!Validator.isNull(order.getPreference())
								&& channel.getChannel_code().equalsIgnoreCase(
										order.getPreference())) {
							map.put(WebConstants.MAP_KEY_CCHANNELCODE, order
									.getPreference());
							cflag = 1;
						}
					}
					map.put(WebConstants.MAP_KEY_CCHANNELCODE, order
							.getPreference());
				}
				if (channel.getChannel_type().equals("2")
						&& channel.getChannel_code().equals("UNIONGATEWAY")) {

					litepayNum = 2;

				}

			}
			if (b2cflag == 0)
				map.put(WebConstants.MAP_KEY_B2CCHANNELCODE, b2cStr);
			if (b2bflag == 0)
				map.put(WebConstants.MAP_KEY_B2BCHANNELCODE, b2bStr);
			if (cflag == 0)
				map.put(WebConstants.MAP_KEY_CCHANNELCODE, cStr);
			map.put(WebConstants.MAP_KEY_ORDER, order);
			map.put(WebConstants.MAP_KEY_PERSISTENCE, XSerialize.serialize(
					order, key));
			map.put(WebConstants.MAP_KEY_ID, coid);
			map.put("_entChannel", slaService.getPartnerEntChannels(id));
			map.put(WebConstants.MAP_KEY_PAYTYPE, paytype);
			map.put("fraudcheck", fraudcheck);
			map.put("rePay", rePay == null ? "" : rePay);
			map.put("b2cChannelNum", b2cChannelNum);
			map.put("b2bChannelNum", b2bChannelNum);
			map.put("cChannelNum", cChannelNum);
			map.put("litepayNum", litepayNum);
			
			map.put("isDebitCard", isDebitCard);
			map.put("isCreditCard", isCreditCard);
			
			map.put("_channelList", channelList);
			map.put("userID", userID);
			if ((paytype == null) || ("0".equals(paytype))) {
				map.put("tab_index", "2");
			} else if (paytype.equals("8")) {
				map.put("tab_index", "0");
			} else if (paytype.equals("9")) {
				map.put("tab_index", "3");
			} else if (paytype.equals("1")) {
				map.put("tab_index", "1");
			}
			return new ModelAndView(Constants.APP_VERSION + "/options", "m",
					RequestUtil.HtmlEscapeMap(map));
		} catch (Exception e) {
			if (!(e instanceof ServiceException))
				e.printStackTrace();
			return new ModelAndView(WebConstants.ERROR_PAGE,
					WebConstants.ERROR_MODEL, new WebError(e));
		}

	}

	public void setSlaService(SLAService slaService) {
		this.slaService = slaService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
