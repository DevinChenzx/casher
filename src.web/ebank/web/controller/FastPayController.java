package ebank.web.controller;

import beartool.Md5Encrypt;
import ebank.core.NoticeService;
import ebank.core.OrderService;
import ebank.core.UserService;
import ebank.core.bank.BankService;
import ebank.core.bank.BankServiceFactory;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.BankOrder;
import ebank.core.domain.MerchantOrder;
import ebank.core.model.domain.CmCustomerChannel;
import ebank.core.model.domain.GwChannel;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwViewUser;
import ebank.web.common.util.PartnerInterface;
import ebank.web.common.util.RequestUtil;
import ebank.web.common.util.Validator;
import ebank.web.common.util.XSerialize;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class FastPayController implements Controller {
	private Log log = LogFactory.getLog(getClass());
	private UserService userService;
	private BankServiceFactory bsf;
	private String localDomain;
	private NoticeService notifyService;
	private PartnerInterface parterInterface;
	private OrderService orderService;
	private static Key key = XSerialize.getKey(null);

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public void setNotifyService(NoticeService notifyService) {
		this.notifyService = notifyService;
	}

	public String getLocalDomain() {
		return this.localDomain;
	}

	public void setLocalDomain(String localDomain) {
		this.localDomain = localDomain;
	}

	public void setParterInterface(PartnerInterface parterInterface) {
		this.parterInterface = parterInterface;
	}

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse rep) throws Exception {
		req.setCharacterEncoding("GBK");
		this.log.debug("request from url pay:" + RequestUtil.getIpAddr(req));
		String partner = req.getParameter("merchantNo");
		GwViewUser user = this.userService.getViewUser(partner, "online");
		String charset = req.getParameter("charset");
		String out_trade_no = req.getParameter("transId");
		try {
			if ((user == null) || (user.getMstate() != 1)) {
				throw new ServiceException("00004", "商户不存在");
			}
			if (!"normal".equals(user.getStatus())) {
				throw new ServiceException("00003", "商户状态不正常");
			}

			String seller_email = req.getParameter("sellerEmail");

			String subject = "fengfuPay";

			String amount = req.getParameter("totalFee");

			String notifyUrl = req.getParameter("notifyUrl");

			String body = "quickpay";

			String transType = req.getParameter("transType");

			String userIdIdentify = req.getParameter("userIdIdentity");

			String partnerkey = user.getMd5Key();

			String bankCardType = req.getParameter("type");

			String sign = req.getParameter("sign");

			String signStr = out_trade_no;

			if (!sign.equals(Md5Encrypt.md5(signStr + partnerkey, charset))) {
				throw new ServiceException("00002", "签名失败");
			}

			bankCardType = "0".equals(bankCardType) ? "1" : "2";

			List<CmCustomerChannel> channelList = userService
					.findUserChannelList(user.getUserId() + "");
			String svcCode = null;
			for (CmCustomerChannel channel : channelList) {
				if (("FENGPAY".equals(channel.getBank_code()))
						&& (bankCardType.equals(channel.getChannel_type()))) {
					svcCode = "2927";
					break;
				}

			}

			if (svcCode == null) {
				throw new ServiceException("00003", "服务未开通");
			}

			if ("createOrder".equals(transType)) {
				Map<String,String> sPara = new HashMap<String,String>();
				sPara.put("service", "fastpay");
				sPara.put("payment_type", "1");
				sPara.put("merchant_ID", partner);
				sPara.put("seller_email", seller_email);
				sPara.put("notify_url", notifyUrl);

				sPara.put("charset", charset);
				sPara.put("order_no", out_trade_no);
				sPara.put("title", subject);
				sPara.put("body", body);
				sPara.put("total_fee", amount);
				sPara.put("sign", sign);
				sPara.put("sign_type", "MD5");

				MerchantOrder morder = parterInterface
						.getMerchantOrderByService(sPara);

				morder.getOrders().setIps(RequestUtil.getIpAddr(req));

				if (Validator.isNull(morder.getOrders().getSeller_id())) {
					morder.getOrders().setSeller_id(partner);
				}

				GwViewUser seller = this.userService.getViewUserWithIdAndName(
						morder.getOrders().getSeller_id(), morder.getOrders()
								.getSeller_name());
				if (seller != null) {
					if (Validator.isNull(morder.getOrders().getSeller_id())) {
						morder.getOrders().setSeller_id(
								String.valueOf(seller.getCustomer_no()));
					}
					if (Validator.isNull(morder.getOrders().getSeller_name())) {
						morder.getOrders().setSeller_name(
								seller.getLogin_recepit());
					}

					if (Validator
							.isNull(morder.getOrders().getSeller_remarks()))
						morder.getOrders().setSeller_remarks(
								seller.getUserAlias());
				} else {
					throw new ServiceException("00004", "商户不存在");
				}

				String orderid = this.orderService.tx_savePostOrder(morder);

				BankService bank = this.bsf.getBank(this.bsf
						.getBankCode(svcCode));

				HashMap params = new HashMap();
				params.put("systemOrderId", orderid);
				params.put("userIdIdentity", userIdIdentify);
				JSONObject object = new JSONObject();
				object.accumulate("transType", "createOrder");
				object.accumulate("inputcharset", charset);

				params.put("outJson", CryptUtil.encrypt(object.toString()));
				params.put("bankCardType", bankCardType);
				BankOrder bankorder = MapOrder2Bank(morder.getOrders());
				bankorder.setMp(params);
				bankorder.setMerchantid(partner);
				String result = bank.sendOrderToBank(bankorder);
				JSONObject ret = JSONObject.fromObject(result);
				String retSignStr = ret.getString("result") + out_trade_no;
				String retSign = Md5Encrypt.md5(retSignStr + user.getMd5Key(),
						charset);
				ret.accumulate("resultSignature", retSign);
				ret.accumulate("requestId", out_trade_no);
				rep.setContentType("text/xml;charset=UTF-8");
				rep.getWriter().write(ret.toString());
				rep.getWriter().close();
				return null;
			}
			if (("pay".equals(transType)) || ("repay".equals(transType))) {
				HashMap<String,String> mp = new HashMap<String,String>();
				String originalId = req.getParameter("originalTradeId");
				String bankCode = req.getParameter("bankCode");
				String cardType = req.getParameter("type");
				if ("repay".equals(transType)) {
					String bindId = req.getParameter("bindId");
					mp.put("bindId", bindId);
				} else {
					String bankAccount = req.getParameter("no");
					String exp = req.getParameter("exp");
					String cvv2 = req.getParameter("cvv2");
					String idType = req.getParameter("idType");
					String idNo = req.getParameter("idNo");
					String name = req.getParameter("name");
					String phone = req.getParameter("phone");
					mp.put("no", bankAccount);
					mp.put("type", cardType);
					mp.put("exp", exp);
					mp.put("cvv2", cvv2);
					mp.put("idType", idType);
					mp.put("idNo", idNo);
					mp.put("name", name);
					mp.put("phone", phone);
				}

				GwOrders order = orderService.queryOriOrderId(Long
						.parseLong(partner), out_trade_no, Amount.getIntAmount(
						amount, 2));
				HashMap<String,Object> map = new HashMap<String,Object>();
				map.put("_orderId", order.getOrdernum());
				map.put("_persistence", XSerialize.serialize(order, key));
				map.put("_id", CryptUtil.encrypt(order.getId()));
				map.put("fraudcheck", user.getFraud_check());
				map.put("userID", Long.valueOf(user.getUserId()));

				mp.put("fengFuTradeId", originalId);
				mp.put("userIdIdentity", userIdIdentify);
				mp.put("transType", transType);
				mp.put("inputcharset", charset);
				mp.put("bankCode", bankCode);

				mp.put("codeNo", req.getParameter("codeNo"));
				mp.put("code", req.getParameter("code"));

				map.put("merchantjson", CryptUtil.encrypt(JSONObject
						.fromObject(mp).toString()));
				map.put("bankCardType", bankCardType);
				GwChannel channel = this.userService.getChannel("FENGPAY", "2",
						order.getAmount());
				int channelid = (int) channel.getId();
				map.put("_channelToken", CryptUtil
						.randomcrypt(new int[] { channelid }));

				String result = this.notifyService.tx_responseNotice("utf-8",
						this.localDomain + "/Ebank", getNameValuePair(map));
				rep.setContentType("text/xml;charset=UTF-8");

				JSONObject ret = JSONObject.fromObject(result);
				String retSignStr = ret.getString("result") + out_trade_no;
				String retSign = Md5Encrypt.md5(retSignStr + user.getMd5Key(),
						charset);
				ret.accumulate("resultSignature", retSign);

				rep.getWriter().write(ret.toString());
				rep.getWriter().close();
				return null;
			}
			if (("sendSMS".equals(transType))
					|| ("reSendSMS".equals(transType))) {
				GwOrders order = this.orderService.queryOriOrderId(Long
						.parseLong(partner), out_trade_no, Amount.getIntAmount(
						amount, 2));
				if (order == null) {
					throw new ServiceException("00005", "未找到订单:" + out_trade_no
							+ "!");
				}
				BankOrder bankorder = MapOrder2Bank(order);
				BankService bank = this.bsf.getBank(this.bsf
						.getBankCode(svcCode));
				String rondamNo = bank.generateOrderID();

				HashMap<String,String> params = new HashMap<String,String>();
				params.put("systemOrderId", order.getId());
				params
						.put("userIdIdentity", req
								.getParameter("userIdIdentity"));
				params.put("rondamNo", rondamNo);
				params.put("bankCardType", bankCardType);

				JSONObject object = new JSONObject();
				object.accumulate("transType", transType);
				object.accumulate("inputcharset", charset);
				params.put("outJson", CryptUtil.encrypt(object.toString()));

				String bankCode = req.getParameter("bankCode");
				String cardType = req.getParameter("type");
				params.put("bankCode", bankCode);

				if ("reSendSMS".equals(transType)) {
					String bindId = req.getParameter("bindId");
					params.put("bindId", bindId);
				} else {
					String bankAccount = req.getParameter("no");
					String exp = req.getParameter("exp");
					String cvv2 = req.getParameter("cvv2");
					String idType = req.getParameter("idType");
					String idNo = req.getParameter("idNo");
					String name = req.getParameter("name");
					String phone = req.getParameter("phone");
					params.put("no", bankAccount);
					params.put("type", cardType);
					params.put("exp", exp);
					params.put("cvv2", cvv2);
					params.put("idType", idType);
					params.put("idNo", idNo);
					params.put("name", name);
					params.put("phone", phone);
				}
				bankorder.setMp(params);
				bankorder.setMerchantid(partner);

				String result = bank.sendOrderToBank(bankorder);
				JSONObject ret = JSONObject.fromObject(result);
				String retSignStr = ret.getString("result") + out_trade_no;
				String retSign = Md5Encrypt.md5(retSignStr + user.getMd5Key(),
						charset);
				ret.accumulate("resultSignature", retSign);
				ret.accumulate("requestId", out_trade_no);
				rep.setContentType("text/xml;charset=UTF-8");
				rep.getWriter().write(ret.toString());
				rep.getWriter().close();
				return null;
			}

			respException(new ServiceException("00005", "交易类型错误"), rep, user,
					out_trade_no, charset);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			if ((e instanceof ServiceException)) {
				e.printStackTrace();
				respException((ServiceException) e, rep, user, out_trade_no,
						charset);
			}
		}
		return null;
	}

	private void respException(ServiceException e, HttpServletResponse resp,
			GwViewUser user, String requestId, String charset) {
		resp.setContentType("text/xml;charset=UTF-8");
		try {
			if (user == null) {
				resp.getWriter().write(
						"{\"result\":\"" + e.getEventID() + "\",\"msg\":\""
								+ e.getMessage() + "\",\"requestId\":\""
								+ requestId + "\"}");
				resp.getWriter().close();
			} else {
				String signStr = e.getEventID() + requestId;
				String sign = Md5Encrypt.md5(signStr + user.getMd5Key(),
						charset);
				resp.getWriter().write(
						"{\"resultSignature\":\"" + sign + "\",\"result\":\""
								+ e.getEventID() + "\",\"msg\":\""
								+ e.getMessage() + "\",\"requestId\":\""
								+ requestId + "\"}");
				resp.getWriter().close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private BankOrder MapOrder2Bank(GwOrders order) {
		BankOrder bx = new BankOrder();
		bx.setAmount(Amount.getFormatAmount(String.valueOf(order.getAmount()),
				-2));
		bx.setOrdernum(order.getOrdernum());
		bx.setCurrency(order.getCurrency());
		bx.setPayment_type(order.getOrder_type());
		return bx;
	}

	public String BuildMysign(Map sArray, String key, String charset) {
		String prestr = CreateLinkString(sArray);
		prestr = prestr + key;
		String mysign = Md5Encrypt.md5(prestr, charset);
		return mysign;
	}

	public NameValuePair[] getNameValuePair(Map<String, Object> bean) {
		List x = new ArrayList();
		for (Iterator iterator = bean.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			x.add(new NameValuePair(type, String.valueOf(bean.get(type))));
		}
		Object[] y = x.toArray();
		NameValuePair[] n = new NameValuePair[y.length];
		System.arraycopy(y, 0, n, 0, y.length);
		return n;
	}

	public static Map<String, Object> ParaFilter(Map sArray) {
		List keys = new ArrayList(sArray.keySet());
		Map sArrayNew = new HashMap();

		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) sArray.get(key);

			if ((!"".equals(value)) && (value != null)
					&& (!key.equalsIgnoreCase("sign"))
					&& (!key.equalsIgnoreCase("sign_type"))) {
				sArrayNew.put(key, value);
			}
		}
		return sArrayNew;
	}

	public static String CreateLinkString(Map params) {
		List keys = new ArrayList(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) params.get(key);

			if (i == keys.size() - 1)
				prestr = prestr + key + "=" + value;
			else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setBsf(BankServiceFactory bsf) {
		this.bsf = bsf;
	}
}