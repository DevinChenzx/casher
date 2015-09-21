package ebank.core.bank.third;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import reapal.RongpayFunction;

import com.alibaba.fastjson.JSON;

import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.RetBean;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.common.util.YinTongUtil;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class YintongPay extends BankExt implements BankService {

	static Logger logger = Logger.getLogger(YintongPay.class);
	private String pubkey;
	private String merBusType;
	private String currentIp;

	public String getCurrentIp() {
		return currentIp;
	}

	public void setCurrentIp(String currentIp) {
		this.currentIp = currentIp;
	}

	public String getMerBusType() {
		return merBusType;
	}

	public void setMerBusType(String merBusType) {
		this.merBusType = merBusType;
	}

	public String sendOrderToBank(BankOrder order) throws ServiceException {

		StringBuffer sbHtml = new StringBuffer();
		HashMap mp = order.getMp();

		String outjson = CryptUtil.decrypt(String.valueOf(mp.get("outJson")));// outJson必填
		logger.info(outjson);
		JSONObject jo = JSONObject.fromObject(outjson);
		String service = "online_pay";
		String partner = this.corpid;
		String return_url = this.getRecurl();
		String notify_url = this.getRecurl() + "&NR=SID_" + this.idx;
		String sign_type = "MD5";

		// String busiPartner=jo.getString("busiPartner");//虚拟商品销售：101001
		// 实物商品销售：109001 外部账户充值：108001
		String busiPartner = this.getMerBusType();

		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("version", getVersion());
		sParaTemp.put("oid_partner", getCorpid());
		sParaTemp.put("user_id", mp.get("currentMerchantId").toString());

		sParaTemp.put("timestamp", order.getPostdate());
		sParaTemp.put("sign_type", sign_type);

		// 业务参数
		sParaTemp.put("busi_partner", busiPartner);
		sParaTemp.put("no_order", order.getRandOrderID());
		sParaTemp.put("dt_order", order.getPostdate());
		sParaTemp.put("name_goods", "test");// 商品名称
		sParaTemp.put("info_order", "");
		sParaTemp.put("money_order", order.getAmount());
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("url_return", return_url);
		sParaTemp.put("url_order", notify_url);
		sParaTemp.put("userreq_ip", this.getCurrentIp());

		//
		sParaTemp.put("bank_code", mp.get("directBankCode").toString());
		sParaTemp.put("pay_type", mp.get("pay_type").toString());

		String sign = YinTongUtil.addSign(JSON.parseObject(JSON
				.toJSONString(sParaTemp)), this.getPubkey(), this.getMd5());
		sParaTemp.put("sign", sign);
		sParaTemp.put("sign_type", "MD5");

		// 拼装form
		List<String> keys = new ArrayList<String>(sParaTemp.keySet());
		sbHtml.append("<form name=\"sendOrder\" action=\"" + this.desturl
				+ "\" method=\"post\">");
		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			String value = (String) sParaTemp.get(name);
			sbHtml.append("<input type=\"hidden\" name=\"" + name
					+ "\" value=\"" + value + "\"/>");
		}
		if (logger.isDebugEnabled())
			logger.debug("str to alipay:" + sbHtml.toString());
		return sbHtml.toString();
	}

	public PayResult getPayResult(HashMap request) throws ServiceException {

		PayResult bean = null;
		if (!YinTongUtil.checkSign(request.get("yintongReq").toString(), this
				.getPubkey(), this.getMd5())) {
			bean = new PayResult(request.get("no_order").toString(),
					this.bankcode, new BigDecimal(request.get("money_order")
							.toString()), -1);
			bean.setBanktransseq(request.get("oid_paybill").toString());
			request.put("RES", "{\"ret_code\":\"9999\",\"ret_msg\":\"验证失败\"}");// 交易失败
			throw new ServiceException(EventCode.SIGN_VERIFY);
		} else {
			String isSuccess = request.get("result_pay").toString();
			bean = new PayResult(request.get("no_order").toString(),
					this.bankcode, new BigDecimal(request.get("money_order")
							.toString()), 1);
			bean.setBanktransseq(request.get("oid_paybill").toString());

			if ("SUCCESS".equals(isSuccess)) {
				request.put("RES", "{\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"}");
			} else {
				request.put("RES", "{\"ret_code\":\"9999\",\"ret_msg\":\"交易失败\"}");
			}

		}
		return bean;

	}

	public String getPubkey() {
		return pubkey;
	}

	public void setPubkey(String pubkey) {
		this.pubkey = pubkey;
	}

	private String getJsonParams(JSONObject jo, String key, String defaults) {
		try {
			if (jo != null)
				return jo.getString(key) == null ? defaults : jo.getString(key);
		} catch (Exception e) {

		}
		return defaults;
	}

}
