/*
 * @Id: PayResultService_Impl.java 14:06:14 2006-5-11
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.logic;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import beartool.Md5Encrypt;
import beartool.RSAUtil;
import ebank.core.NoticeService;
import ebank.core.OrderService;
import ebank.core.PayResultService;
import ebank.core.UserService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.Udate;
import ebank.core.domain.PayResult;
import ebank.core.model.domain.GwLgOptions;
import ebank.core.model.domain.GwLogistic;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwTrxs;
import ebank.core.model.domain.GwViewUser;
import ebank.core.model.domain.MapiAsyncNotify;
import ebank.core.model.domain.TradePrecard;
import ebank.web.common.util.Validator;

/**
 * @author xiexh Description: 支付结果实现
 * 
 */
public class PayResultService_Impl implements PayResultService {

	private OrderService orderService;
	private UserService userService;
	private NoticeService noticeService;
	private RSAUtil rsaUtil;
	

	public void setRsaUtil(RSAUtil rsaUtil) {
		this.rsaUtil = rsaUtil;
	}

	private Log log = LogFactory.getLog(this.getClass());

	public Map mapresult(GwOrders order, boolean notify)
			throws ServiceException {

		if (order == null)
			throw new ServiceException(EventCode.ORDER_PAYNOTFOUND);

		GwViewUser user = userService.getViewUser(String.valueOf(order
				.getPartnerid()), "online");
		if (user == null)
			throw new ServiceException(EventCode.MERCHANT_IDNOTFOUND);
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("body", order.getBodys());
		mp.put("buyer_email", order.getBuyer_name());
		mp.put("buyer_id", order.getBuyer_id());
		mp.put("discount", Amount.getFormatAmount(String.valueOf(order
				.getDiscount()), -2));
		mp.put("gmt_create", Udate.format(order.getCreatedate()));
		mp.put("gmt_logistics_modify", Udate
				.format(order.getGwl_update() == null ? new Date() : order
						.getGwl_update()));
		mp.put("gmt_payment", Udate.format(order.getClosedate()));
		mp.put("is_success", "T");
		mp.put("is_total_fee_adjust", order.getPricechanged());
		if (!"".equals(order.getGwlgoptions_id())) {
			GwLgOptions gl = orderService.findLogisticByPk(order
					.getGwlgoptions_id());
			mp.put("logistics_fee", gl.getLogistics_fee());
			mp.put("logistics_payment", gl.getLogistics_payment());
			mp.put("logistics_type", gl.getLogistics_type());
		}
		mp.put("order_no", order.getOrdernum());
		mp.put("payment_type", order.getOrder_type());
		mp.put("price", Amount.getFormatAmount(
				String.valueOf(order.getPrice()), -2));
		mp.put("quantity", order.getQuantity());
		GwLogistic gc = orderService.findLogisticByFk(order.getId());
		if (gc != null) {
			mp.put("receive_address", gc.getRecaddr());
			mp.put("receive_name", gc.getRecname());
			mp.put("receive_zip", gc.getRecpost());
		}
		mp.put("seller_actions", "SEND_GOODS");
		mp.put("seller_email", order.getSeller_name());
		mp.put("seller_id", order.getSeller_id());
		mp.put("title", order.getSubject());
		mp.put("total_fee", Amount.getFormatAmount(String.valueOf(order
				.getAmount()), -2));
		mp.put("trade_no", order.getId());
		mp.put("trade_status", Constants.FROM_TRXSTS.values()[Integer
				.parseInt(order.getOrdersts())]); // "WAIT_SELLER_SEND_GOODS"
		if ("C".equals(order.getDiscount_mode())) // TODO COUPON
			mp.put("use_coupon", "N");
		mp.put("notify_type", Constants.NOTIFY_STS.values()[0]);
		mp.put("notify_time", Udate.format(order.getClosedate()));

		// 增加扩展参数
		if (!Validator.isNull(order.getExt_param1())) {
			mp.put("ext_param1", order.getExt_param1());
		}
		if (!Validator.isNull(order.getExt_param2())) {
			mp.put("ext_param2", order.getExt_param2());
		}

		String notify_id = UUID.randomUUID().toString().replaceAll("-", "");
		if (!Validator.isNull(order.getNotify_url())) {
			MapiAsyncNotify asyn = new MapiAsyncNotify();
			asyn.setCustomer_id(order.getPartner_id());
			asyn.setNotify_address(order.getNotify_url());
			asyn.setNotify_method("http");
			asyn.setNotify_id(notify_id);
			asyn.setRecord_id(Long.parseLong(order.getId()));
			asyn.setRecord_table("GWORDERS");
			asyn.setCustomer_id(Long.parseLong(order.getSeller_id()));
			asyn.setOutput_charset(order.getCharsets());

			if ("qucikpay".equals(order.getSubject().trim())) {
				String resp = "<tradeType>S</tradeType><transId>"
						+ order.getOrdernum()
						+ "</transId><totalFee>"
						+ Amount.getFormatAmount(order.getAmount() + "", -2)
						+ "</totalFee><currency>"
						+ order.getCurrency()
						+ "</currency><date>"
						+ Udate
								.getDate(new Date(order.getClosedate()
										.getTime()))
						+ "</date><time>"
						+ Udate
								.getTime(new Date(order.getClosedate()
										.getTime())) + "</time><code>"
						+ (order.getOrdersts().equals("3") ? "0000" : "-1")
						+ "</code>";
				String fastPaySign = Md5Encrypt.md5(resp + user.getMd5Key(),
						order.getCharsets());
				mp.clear();
				mp.put("resp", "<data>" + resp + "<sign>" + fastPaySign
						+ "</sign></data>");
			}

			if ("fengfuPay".equals(order.getSubject().trim())) {
				mp.clear();
				mp.put("payId", order.getId());
				mp.put("result", order.getOrdersts().equals("3") ? "00000"
						: "00001");

				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				mp.put("fiscalDate", format.format(order.getCreatedate()));

				String resultMd5 = mp.get("result") + order.getOrdernum();
				String retSign = Md5Encrypt.md5(resultMd5 + user.getMd5Key(),
						order.getCharsets());
				mp.put("resultSignature", retSign);
				mp.put("requestId", order.getOrdernum());

				mp.put("totalPrice", Amount.getFormatAmount(order.getAmount()
						+ "", -2));
			}

			if ("unionPay".equals(order.getSubject().trim())) {
				mp.clear();
				mp.put("transId", order.getOrdernum());
				mp.put("transStatus", order.getOrdersts().equals("3") ? "00000"
						: "00001");

				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				mp.put("transDate", format.format(order.getCreatedate()));

				String resultMd5 = mp.get("transStatus") + order.getOrdernum();
				String retSign = Md5Encrypt.md5(resultMd5 + user.getMd5Key(),
						order.getCharsets());
				mp.put("signature", retSign);

				mp.put("amount", Amount.getFormatAmount(order.getAmount() + "",
						-2));
			}

			JSONObject jo = new JSONObject();
			jo.putAll(mp);
			asyn.setNotify_contents(jo.toString());
			if (notify) {
				asyn.setStatus("processing");
				noticeService.tx_saveAsynNotice(asyn);

			} else {
				asyn.setStatus("success");
				noticeService.tx_saveAsynNotice(asyn);
			}

		}

		mp.put("notify_id", notify_id);

		if ("qucikpay".equals(order.getSubject().trim())) {
			return mp;
		}

		if ("fengfuPay".equals(order.getSubject().trim())) {
			return mp;
		}

		ArrayList<String> list = new ArrayList<String>(mp.keySet());
		java.util.Collections.sort(list);
		Iterator<String> itr = list.iterator();
		StringBuffer sf = new StringBuffer();
		String enc = "";// OrderAdaptor.getAdviceFeature(bean.getOrder().getOrderdesc2(),
						// "encode");

		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = String.valueOf(mp.get(fieldName));
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				if (!"".equals(enc)) {// 需要编码
					mp.remove(fieldName);
					try {
						fieldValue = URLEncoder.encode(fieldValue, enc);
						mp.put(fieldName, fieldValue);
					} catch (Exception e) {
						log
								.error(order.getOrdernum() + "unsupport char "
										+ enc);
						mp.put(fieldName, fieldValue);
					}
				}
				sf.append(fieldName + "=" + fieldValue + "&");
			}
		}
		sf.setLength(sf.length() - 1);
		mp.put("sign_type", order.getSign_type());
		//mp.put("sign", Md5Encrypt.md5(sf.toString() + user.getMd5Key(), order
		//		.getCharsets()));
 		if("MD5".equals(order.getSign_type())){
        	mp.put("sign",Md5Encrypt.md5(sf.toString()+user.getMd5Key(), order.getCharsets()));	      
        }else{
        	mp.put("sign",rsaUtil.sign("cashier",sf.toString()));
        }
		return mp;
	}

	public Map mapresult(PayResult bean, Map<String, Object> map, boolean notify)
			throws ServiceException {
		GwTrxs trx = null;
		GwOrders order = null;
		if (map != null) {
			trx = (GwTrxs) map.get("GwTrxs");
			order = (GwOrders) map.get("GwOrders");
		}
		if (bean == null)
			throw new ServiceException(EventCode.ORDER_PAYNOTFOUND);
		if (trx == null)
			trx = orderService.findTrxByTrxnum(bean.getTransnum(), bean
					.getBankcode());
		if (order == null)
			order = orderService.findOrderByPk(trx.getGworders_id());
		return this.mapresult(order, notify);

	}

	public NameValuePair[] getNameValuePair(Map<String, String> bean) {
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

	/**
	 * @param orderService
	 *            The orderService to set.
	 */
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

	public Map mapPrecardResult(TradePrecard tradePrecard, boolean notify)
			throws ServiceException {
		if (tradePrecard == null)
			throw new ServiceException(EventCode.ORDER_PAYNOTFOUND);
		GwViewUser user = userService.getViewUser(String.valueOf(tradePrecard
				.getSellerid()), "online");
		// GwViewUser
		// user=userService.getViewUser(String.valueOf(order.getPartnerid()),"online");
		if (user == null)
			throw new ServiceException(EventCode.MERCHANT_IDNOTFOUND);
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("body", tradePrecard.getExt1());
		mp.put("is_success", "T");
		mp.put("out_trade_no", tradePrecard.getOuttradeno());
		mp.put("payment_type", tradePrecard.getPaymentType());
		mp.put("price", Amount.getFormatAmount(String.valueOf(tradePrecard
				.getAmount()), -2));
		mp.put("quantity", tradePrecard.getProductnum());
		mp.put("subject", tradePrecard.getProductname());
		mp.put("total_fee", Amount.getFormatAmount(String.valueOf(tradePrecard
				.getAmount()), -2));
		mp.put("trade_no", tradePrecard.getId());
		if ("Y".equals(tradePrecard.getPaystatus())) {
			mp.put("trade_status", Constants.FROM_TRXSTS.values()[3]); // "成功"
		} else {
			mp.put("trade_status", Constants.FROM_TRXSTS.values()[5]); // "失败"
		}

		mp.put("notify_type", Constants.NOTIFY_STS.values()[0]);
		mp.put("notify_time", Udate.format(tradePrecard.getOrdertime()));

		String notify_id = UUID.randomUUID().toString().replaceAll("-", "");
		if (!Validator.isNull(tradePrecard.getNotifyurl())) {
			MapiAsyncNotify asyn = new MapiAsyncNotify();

			asyn.setNotify_address(tradePrecard.getNotifyurl());
			asyn.setNotify_method("http");
			asyn.setNotify_id(notify_id);
			asyn.setRecord_id(Long.parseLong(tradePrecard.getId()));
			asyn.setRecord_table("TRADE_PRECARD");
			asyn.setCustomer_id(Long.parseLong(tradePrecard.getSellerid()));
			asyn.setOutput_charset(tradePrecard.getCharsets());
			JSONObject jo = new JSONObject();
			jo.putAll(mp);
			asyn.setNotify_contents(jo.toString());
			if (notify)
				asyn.setStatus("processing");

			else {
				asyn.setNotify_time(new Date());
				asyn.setStatus("success");
			}

			noticeService.tx_saveAsynNotice(asyn);
		}
		mp.put("notify_id", notify_id);

		ArrayList<String> list = new ArrayList<String>(mp.keySet());
		java.util.Collections.sort(list);
		Iterator<String> itr = list.iterator();
		StringBuffer sf = new StringBuffer();
		String enc = "";// OrderAdaptor.getAdviceFeature(bean.getOrder().getOrderdesc2(),
						// "encode");

		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = String.valueOf(mp.get(fieldName));
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				if (!"".equals(enc)) {// 需要编码
					mp.remove(fieldName);
					try {
						fieldValue = URLEncoder.encode(fieldValue, enc);
						mp.put(fieldName, fieldValue);
					} catch (Exception e) {
						log.error(tradePrecard.getId() + "unsupport char "
								+ enc);
						mp.put(fieldName, fieldValue);
					}
				}
				sf.append(fieldName + "=" + fieldValue + "&");
			}
		}
		sf.setLength(sf.length() - 1);
		mp.put("sign_type", tradePrecard.getSigntype());
		// mp.put("sign", tradePrecard.getSignMsg());
		mp.put("sign", Md5Encrypt.md5(sf.toString() + user.getMd5Key(),
				tradePrecard.getCharsets()));

		log.debug(mp);
		return mp;
	}

}
