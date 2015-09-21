package ebank.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.util.HtmlUtils;

import beartool.Md5Encrypt;
import beartool.RSAUtil;
import bill99.MD5Util;
import ebank.core.NoticeService;
import ebank.core.OrderService;
import ebank.core.SLAService;
import ebank.core.UserService;
import ebank.core.common.BankInfo;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.MerchantOrder;
import ebank.core.model.domain.CmCorporationInfo;
import ebank.core.model.domain.CmCustomerChannel;
import ebank.core.model.domain.GwChannel;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwViewUser;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.util.DigestUtil;
import ebank.web.common.util.IPartnerInterface;
import ebank.web.common.util.PartnerFactory;
import ebank.web.common.util.RequestUtil;
import ebank.web.common.util.Validator;
import ebank.web.common.util.XSerialize;

public class PayAccess implements Controller {
	private OrderService orderService;
	private UserService userService;
	private SLAService slaService;
	private static Key key = XSerialize.getKey(null);
	private Log log = LogFactory.getLog(this.getClass());
	private PartnerFactory factory;
	private NoticeService notifyService;
	private String localDomain;
	private RSAUtil rsaUtil;
	

	public void setRsaUtil(RSAUtil rsaUtil) {
		this.rsaUtil = rsaUtil;
	}
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log.info("remote host:" + request.getHeader("host") + " service url:"
				+ request.getHeader("REFERER") + " ips:"
				+ RequestUtil.getIpAddr(request));

		try {
			// 得到商户订单对象
			// MerchantOrder morder=
			// factory.getInstance(request);//MerchantOrder
			// morder=PartnerInterface.getMerchantOrderByService(request);
			IPartnerInterface serviceobj = factory
					.getThirdPartInstance(request);

			MerchantOrder morder = serviceobj
					.getMerchantOrderByService(request);
			GwOrders order = morder.getOrders();
			if (order == null) {
				throw new ServiceException(EventCode.WEB_PARAMNULL);
			}
			// 验证商户号
			String apiformat = request.getParameter(Constants.FORMAT_FILED);
			boolean checkorder = false;
			if (apiformat == null
					|| Constants.API_WONDERPAY.equals(order.getApiversion())
					|| Constants.API_APLIPAY.equals(order.getApiversion())) {
				checkorder = true;
			}
			// //验证数据域
			if (morder != null && checkorder) {
				PartnerFactory.validateOrder(morder);
			}

			GwViewUser user = null;

			// 判断服务
			if ("10".equals(morder.getOrders().getRoyalty_type())) {
				user = PartnerFactory.ValidateRoyaltyParam(userService, morder
						.getOrders().getRoyalty_parameters(), morder
						.getOrders().getAmount(), String.valueOf(morder
						.getOrders().getPartnerid()));
			} else if ("12".equals(morder.getOrders().getRoyalty_type())) {// 合单支付
				user = PartnerFactory.ValidateRoyaltyParamForSub(userService,
						morder.getOrders().getRoyalty_parameters(), morder
								.getOrders().getAmount(), String.valueOf(morder
								.getOrders().getPartnerid()), morder);
			} else
				user = userService.getViewUser(String.valueOf(morder
						.getOrders().getPartnerid()), "online");

			if (user == null || user.getMstate() != 1) {
				throw new ServiceException(EventCode.MERCHANT_STATEILL);
			}
			if (!"normal".equals(user.getStatus())) {
				log.debug("user state unnormal:" + user.getService_code() + " "
						+ user.getCustomer_no());
				throw new ServiceException(EventCode.MERCHANT_STATEILL);
			}
			// 校验商户接入地址
			if ("1".equals(user.getFraud_check())
					&& !Validator.isNull(user.getCompany_website())) {
				String refer=request.getHeader("REFERER");
				if(refer==null){
					throw new ServiceException(EventCode.ORDER_FRAUD_CHECK);
				}
				URL url = new URL(request.getHeader("REFERER"));
				String urlhost = url.getHost();
				String websitehost;
				try {
					URL websiteurl = new URL(user.getCompany_website());
					websitehost = websiteurl.getHost();
				} catch (Exception e) {
					websitehost = user.getCompany_website();
				}
				log.debug("urlhost:" + urlhost + "   websitehost:"
						+ websitehost);
				if (!urlhost.equalsIgnoreCase(websitehost)) {
					throw new ServiceException(EventCode.ORDER_FRAUD_CHECK);
				}
			}
			
			String signType=request.getParameter("sign_type");//MD5,RSA
			// 验证签名
			if (Constants.API_WONDERPAY.equals(morder.getOrders()
					.getApiversion())
					|| Constants.API_APLIPAY.equals(morder.getOrders()
							.getApiversion())) {
				if (Validator.isNull(morder.getSign())
						|| !morder.getSign().equals(
								Md5Encrypt
										.md5(morder.getSortstr()
												+ user.getMd5Key(), order
												.getCharsets()))) {
					throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
				}
			} else if (Constants.API_REAPAL.equals(morder.getOrders()
					.getApiversion())) {
				if (!Validator.isNull(morder.getSign())){
					 if("MD5".equals(signType)&&!morder.getSign().equals(Md5Encrypt.md5(morder.getSortstr()+user.getMd5Key(),order.getCharsets()))){
						 throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
					 }else if("RSA".equals(signType)&&!rsaUtil.checksign(morder.getOrders().getPartnerid()+"", morder.getSortstr(), morder.getSign())){
						 throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
					 }
				}else{
					throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
					
				}
			} else if (Constants.API_YEEPAY.equals(morder.getOrders()
					.getApiversion())) {
				String pubkey = "";
				if (user.getMd5Key() != null && !"".equals(user.getMd5Key())
						&& user.getMd5Key().length() > 63) {
					String key = user.getMd5Key();
					pubkey = key;
				}
				if (Validator.isNull(morder.getSign())
						|| !morder.getSign().equals(
								DigestUtil
										.hmacSign(morder.getSortstr(), pubkey))) {
					throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
				}
			} else if (Constants.API_99BILL.equals(morder.getOrders()
					.getApiversion())) {
				if (Validator.isNull(morder.getSign())
						|| !morder.getSign().toUpperCase().equals(
								MD5Util.md5Hex(
										(morder.getSortstr() + "&key=" + user
												.getMd5Key()).getBytes(morder
												.getOrders().getCharsets()))
										.toUpperCase())) {
					throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
				}
			} else if (Constants.API_CHINABANK.equals(morder.getOrders()
					.getApiversion())) {
				if (Validator.isNull(morder.getSign())
						|| !morder.getSign().toUpperCase().equals(
								MD5Util
										.md5Hex(
												(morder.getSortstr() + user
														.getMd5Key())
														.getBytes())
										.toUpperCase())) {
					throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
				}
			} else if (Constants.API_IPS.equals(morder.getOrders()
					.getApiversion())) {
				cryptix.jce.provider.MD5 b = new cryptix.jce.provider.MD5();
				if (Validator.isNull(morder.getSign())
						|| !morder.getSign().toUpperCase().equals(
								b.toMD5(morder.getSortstr() + user.getMd5Key())
										.toUpperCase())) {
					throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
				}
			} else {
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
			}

			order = (GwOrders) RequestUtil.HtmlEscape(order); // 转义对象
			order.setNotify_url(HtmlUtils.htmlUnescape(order.getNotify_url()));
			order.setReturn_url(HtmlUtils.htmlUnescape(order.getReturn_url()));
			// CHECK SELLER

			if (Validator.isNull(order.getSeller_id())) {
				order.setSeller_id(String.valueOf(order.getPartnerid()));
			}
			GwViewUser seller = userService.getViewUserWithIdAndName(order
					.getSeller_id(), order.getSeller_name());
			if (seller != null) {
				if (Validator.isNull(order.getSeller_id())) {
					morder.getOrders().setSeller_id(
							String.valueOf(seller.getCustomer_no()));
				}
				if (Validator.isNull(order.getSeller_name())) {
					morder.getOrders()
							.setSeller_name(seller.getLogin_recepit());
				}
				if (Validator.isNull(order.getSeller_remarks())) {
					morder.getOrders().setSeller_remarks(seller.getUserAlias());
				}
			} else {
				throw new ServiceException(EventCode.SELLER_NOTFOUND);
			}
			// CHECK BUYER
			if (Constants.API_WONDERPAY.equals(order.getApiversion())) {
				if (!(Validator.isNull(order.getBuyer_id()) && Validator
						.isNull(order.getBuyer_name()))) {
					GwViewUser buyer = userService.getUserWithIdAndName(order
							.getBuyer_id(), order.getBuyer_name());
					if (buyer == null) {
						throw new ServiceException(EventCode.BUYER_NOTFOUND);
					} else {
						// reset the buyer
						morder.getOrders().setBuyer_id(
								String.valueOf(buyer.getCustomer_no()));
						morder.getOrders().setBuyer_name(order.getBuyer_name());
					}
				}
			}
			if (order.getPreference() == null)
				order.setPreference(WebConstants.DEFAULT_BANK_SELECTED);
			String orderid = "";
			orderid = orderService.tx_savePostOrder(morder);

			morder.getOrders().setId(orderid);
			// 缓存对象
			request.getSession().removeAttribute(Constants.SESSION_KEY_MORDER);
			request.getSession().setAttribute(Constants.SESSION_KEY_MORDER,
					morder.getOrders());

			HashMap<String, Object> mp = new HashMap<String, Object>();
			mp.put(WebConstants.MAP_KEY_ORDERID, morder.getOrders()
					.getOrdernum());
			mp.put(WebConstants.MAP_KEY_PERSISTENCE, XSerialize.serialize(
					morder.getOrders(), key));
			mp.put(WebConstants.MAP_KEY_ID, CryptUtil.encrypt(orderid));
			mp.put("fraudcheck", user.getFraud_check());
			mp.put("userID", user.getUserId());
			if (order.getPreference().endsWith("CARD")) {
				mp.put("_paytype", "8");
			} else if (order.getPreference().endsWith("_B2B")) {
				mp.put("_paytype", "9");
			} else if (order.getPreference().equalsIgnoreCase("wonderpay")) {
				mp.put("_paytype", "1");
			} else
				mp.put("_paytype", "0");

			if ("fastpay".equals(request.getParameter("service"))
					&& Constants.API_WONDERPAY.equals(order.getApiversion())) {// 快捷支付流程

				mp.put(WebConstants.Action, "/Ebank");
				mp.put("orderInfoUrl", request.getParameter("orderInfoUrl"));// 付款成功跳转界面
				mp.put("merchantjson", CryptUtil.encrypt(JSONObject.fromObject(
						RequestUtil.getFormParams(request)).toString()));
				
				order.setPreference("CHINABANK");
				String payType=null;
				if("D".equals(request.getParameter("type"))){
					payType="0";
				}else{
					payType="1";
				}
				
				GwChannel channel=userService.getChannel(order.getPreference(),payType,order.getAmount());
				
//				int channelid = slaService.getRouteChannelExt(orderid, order
//						.getPreference(), order.getAmount(), order
//						.getPartnerid()
//						+ "");
				

				if(channel==null){
					throw new ServiceException(EventCode.SLA_NOSERVICECHANNEL);
				}
				int channelid=(int)channel.getId();
				
				log.debug("API form preference:" + order.getPreference()+"& channelid:"+channelid);
				if (channelid != 0) {
					mp.put(WebConstants.MAP_KEY_CHANNELTOKEN, CryptUtil
							.randomcrypt(new int[] { channelid }));
					log.debug(mp);

					String result = notifyService.tx_responseNotice("utf-8",
							localDomain + "/Ebank", getNameValuePair(mp));
					response.setContentType("text/xml;charset=UTF-8");
					response.getWriter().write(result);
					response.getWriter().close();
					return null;
				}
			}

			
			CmCorporationInfo corporationInfo = orderService.findCorporationInfoByCustomerNo(order.getPartnerid()+"");
			BigDecimal dayQutor = new BigDecimal(corporationInfo.getDayQutor()).multiply(new BigDecimal(100));//当日限额
			BigDecimal dayQutorCount = new BigDecimal(corporationInfo.getDayQutorCount()).multiply(new BigDecimal(100));//当日累计
			BigDecimal qutor = new BigDecimal(corporationInfo.getQutor()).multiply(new BigDecimal(100));//单笔限额
			BigDecimal amount = new BigDecimal(order.getAmount());
			if(amount.compareTo(qutor) == 1){
				log.info("商户["+order.getPartnerid()+"],支付金额："+amount+",超过单笔交易限额："+qutor);
				order.setSubject("超过单笔交易限额");
				order.setOrdersts(Constants.ORDER_STS_REFUSE);
				orderService.updateOrderStatus(order);
				throw new ServiceException(EventCode.AMOUNT_QUTOR);
			}
			
		    BigDecimal dayQutorTotal = amount.add(dayQutorCount);
	        if(dayQutorTotal.compareTo(dayQutor) == 1){
	        	log.info("商户["+order.getPartnerid()+"],支付金额："+amount+",当日交易限额累计："+dayQutorCount+",超过当日交易限额"+dayQutor);
	        	order.setSubject("超过单日交易限额");
	        	order.setOrdersts(Constants.ORDER_STS_REFUSE);
	        	orderService.updateOrderStatus(order);
	        	throw new ServiceException(EventCode.AMOUNT_DAY_QUTOR);
			}
			
	               
			// 三方直连银行
//			if (!Constants.API_WONDERPAY.equals(order.getApiversion())) {
			if(!Validator.isNull(order.getPreference())
					&& "directPay".equals(order.getPaymethod())){
				mp.put(WebConstants.Action, "/Ebank");
				mp.put("merchantjson", CryptUtil.encrypt(JSONObject.fromObject(
						RequestUtil.getFormParams(request)).toString()));
//				int channelid = slaService.getRouteChannelExt(orderid, order
//						.getPreference(), order.getAmount(), order
//						.getPartnerid()
//						+ "");
				
				
				String defaultbank = request.getParameter("defaultbank");

				//若开通融信通
			       List<CmCustomerChannel> channelList = this.userService
			          .findUserChannelList(user.getUserId()+"");

			        boolean isRongxinExist = false;

			        for (int i = 0; i < channelList.size(); i++)
			        {
			          if ("RONGXIN".equals(((CmCustomerChannel)channelList.get(i)).getBank_code()))
			          {
			            if ("BOCM".equals(defaultbank)) {
			              defaultbank = "COMM";
			            }
			            mp.put("directBankCode", defaultbank);

			            isRongxinExist = true;
			          }
			        }

			        int channelid = 0;

			        if ((isRongxinExist) && (!"UNIONGATEWAY".equals(order.getPreference())) && (!"LEFU_MOBILE".equals(order.getPreference())))
			        {
			          channelid = this.slaService.getRouteChannelExt(orderid, "RONGXIN", order.getAmount(), order
			            .getPartnerid()+"");

			          if (channelid != 0) {
			            mp.put("_channelToken", 
			              CryptUtil.randomcrypt(new int[] { channelid }));
			            this.log.debug(mp);
			            return new ModelAndView(
			              "v4/redirect", 
			              "PaRes", RequestUtil.HtmlEscapeMap(mp));
			          }

			          throw new ServiceException("501611");
			        }
			        
			        
			  //若配置文件中出现融信通特殊通道
			        Properties p = null;
			        try {
			          p = PropertiesLoaderUtils.loadProperties(new ClassPathResource(
			            "RongxintongChannel.properties"));
			        } catch (IOException e) {
			          e.printStackTrace();
			        }
			        String specialBank = p.getProperty(order.getPartnerid()+"", "-1");
			        if ((!"-1".equals(specialBank)) && (Arrays.asList(specialBank.split(",")).indexOf(defaultbank.trim()) > -1)) {
			          channelid = this.slaService.getRouteChannelExt(orderid, "RONGXIN", order.getAmount(), order
			            .getPartnerid()+"");

			          if (channelid != 0) {
			            mp.put("_channelToken", 
			              CryptUtil.randomcrypt(new int[] { channelid }));
			            if ("BOCM".equals(defaultbank)) {
			              defaultbank = "COMM";
			            }
			            mp.put("directBankCode", defaultbank);
			            this.log.debug(mp);
			            return new ModelAndView(
			              "v4/redirect", 
			              "PaRes", RequestUtil.HtmlEscapeMap(mp));
			          }

			          throw new ServiceException("501611");
			        }
    
			        
			        channelid = this.slaService.getRouteChannelExt(orderid, order
			                .getPreference(), order.getAmount(), order
			                .getPartnerid()+"");
			        
			    //正常流程
				String payTypeFlag = order.getOrder_type();// 1:贷记2：借记

				// 根據當前查詢出channelId匹配路由設置中
				channelid = Integer.parseInt(slaService
						.getRouteChannelByAmount(new BigDecimal(request
								.getParameter("total_fee")).doubleValue()
								+ "", defaultbank.trim(), channelid + "",
								seller.getUserId() + "", payTypeFlag));

				log.debug("API form preference:" + order.getPreference());

				// 获取当前商户的分配的渠道
				GwChannel gc = slaService.getChannel(channelid);


//				List<CmCustomerChannel> channelList = userService
//						.findUserChannelList(user.getUserId() + "");
				boolean isAvailable = false;// 通道是否可用
				String bankId = null;
				for (CmCustomerChannel channel : channelList) {

					if (channel.getChannel_code().toUpperCase().equals(
							gc.getAcquire_indexc().trim().toUpperCase())) {// 当前渠道

						isAvailable = true;

						String directBank = request.getParameter("defaultbank");

						bankId = BankInfo.bankInfo.get(directBank);

						mp.put("directBankCode", bankId);

						if ("1".equals(channel.getChannel_type())) {//
						// 若开通借记通道，并且商户选择借记通道
							// //b2c借记
							if (payTypeFlag != null && "2".equals(payTypeFlag)) {
								mp.put("pay_type", 1);// 借记卡
								break;
							} else if (payTypeFlag == null) {
								mp.put("pay_type", 1);
							}

						} else if ("2".equals(channel.getChannel_type()))// b2c贷记
						{
							if (payTypeFlag != null && "1".equals(payTypeFlag)) {
								mp.put("pay_type", 8);// 贷记卡
								break;
							} else if (payTypeFlag == null) {
								mp.put("pay_type", 8);
							}

						} else {
							isAvailable = false;
						}

					}

				}

				if (channelid != 0 && isAvailable && mp.get("pay_type") != null) {
					mp.put(WebConstants.MAP_KEY_CHANNELTOKEN, CryptUtil
							.randomcrypt(new int[] { channelid }));
					log.debug(mp);
					return new ModelAndView(
							Constants.APP_VERSION + "/redirect",
							WebConstants.PaRes, RequestUtil.HtmlEscapeMap(mp));
				} else {

					throw new ServiceException(EventCode.SLA_NOSERVICECHANNEL);

				}
			}

			// 吉卡直连银行判断
//			if (!Validator.isNull(order.getPreference())
//					&& "directPay".equals(order.getPaymethod())) {
//				// 判断该用户是否有开通了支付渠道.
//				// if(!Validator.isNull(order.getOrder_type())&&"1".equals(order.getOrder_type())){//b2c借记,b2b
//				// List <CmCustomerChannel>channelList =
//				// userService.findUserChannelListByPaymentType(String.valueOf(user.getUserId()),"1",order.getPreference());
//				// if(channelList.size()<1){
//				// throw new ServiceException(EventCode.SLA_NOUSERCHANNEL);
//				// }
//				// }else
//				// if(!Validator.isNull(order.getOrder_type())&&"2".equals(order.getOrder_type())){//b2c贷记
//				// List <CmCustomerChannel>channelList =
//				// userService.findUserChannelListByPaymentType(String.valueOf(user.getUserId()),"2",order.getPreference());
//				// if(channelList.size()<1){
//				// throw new ServiceException(EventCode.SLA_NOUSERCHANNEL);
//				// }
//				// }else{//b2c借记和b2b贷记
//				// List <CmCustomerChannel>channelList =
//				// userService.findUserChannelListByChannelCode(String.valueOf(user.getUserId()),order.getPreference());
//				// if(channelList.size()<2){
//				// throw new ServiceException(EventCode.SLA_NOUSERCHANNEL);
//				// }
//				// }
//				if (("CMBC_B2B".equals(order.getPreference())
//						|| ("SPDB_B2B".equals(order.getPreference())) || ("BOCM_B2B"
//						.equals(order.getPreference())))
//						&& !Validator.isNull(order.getPay_cus_no())) {
//					mp.put("payerNo", order.getPay_cus_no());
//				}
//				mp.put(WebConstants.Action, "/Ebank");
//				int channelid = slaService.getRouteChannelExt(orderid, order
//						.getPreference(), order.getAmount(), order
//						.getPartnerid()
//						+ "");
//				log.debug("form preference:" + order.getPreference());
//				if (channelid != 0 && !order.getPreference().endsWith("CARD")) {
//					mp.put(WebConstants.MAP_KEY_CHANNELTOKEN, CryptUtil
//							.randomcrypt(new int[] { channelid }));
//					log.debug(mp);
//					return new ModelAndView(
//							Constants.APP_VERSION + "/redirect",
//							WebConstants.PaRes, RequestUtil.HtmlEscapeMap(mp));
//				}
//			}
			// mp.put(WebConstants.MAP_KEY_ORDER, morder.getOrders());
			// return new
			// ModelAndView(Constants.APP_VERSION+"/portal","m",RequestUtil.HtmlEscapeMap(mp));
			// new version

			mp.put(WebConstants.Action, "/Pay");

//			String result = notifyService.tx_responseNotice("utf-8", "/Pay",
//					getNameValuePair(RequestUtil.HtmlEscapeMap(mp)));
			 return new ModelAndView(Constants.APP_VERSION + "/redirect",
			 WebConstants.PaRes, RequestUtil.HtmlEscapeMap(mp));
//			 response.setContentType("text/xml;charset=UTF-8");
//			response.getWriter().write(result);
//			response.getWriter().close();
//			return null;

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (!(e instanceof ServiceException))
				e.printStackTrace();
			return new ModelAndView(WebConstants.ERROR_PAGE,
					WebConstants.ERROR_MODEL, new WebError(e));
		}

	}

	public NameValuePair[] getNameValuePair(Map<String, Object> bean) {
		List<NameValuePair> x = new ArrayList<NameValuePair>();
		for (Iterator<String> iterator = bean.keySet().iterator(); iterator
				.hasNext();) {
			String type = (String) iterator.next();
			x.add(new NameValuePair(type, String.valueOf(bean.get(type))));
		}
		Object[] y = x.toArray();
		NameValuePair[] n = new NameValuePair[y.length];
		System.arraycopy(y, 0, n, 0, y.length);
		return n;
	}

	public void setLocalDomain(String localDomain) {
		this.localDomain = localDomain;
	}

	public void setNotifyService(NoticeService notifyService) {
		this.notifyService = notifyService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setSlaService(SLAService slaService) {
		this.slaService = slaService;
	}

	public void setFactory(PartnerFactory factory) {
		this.factory = factory;
	}

}
