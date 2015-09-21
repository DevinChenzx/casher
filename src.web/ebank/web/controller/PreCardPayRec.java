package ebank.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import ebank.core.PayResultService;
import ebank.core.TradePrecardService;

import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;

import ebank.core.common.util.Amount;
import ebank.core.common.util.MD5sign;
import ebank.core.domain.PayResult;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwTrxs;
import ebank.core.model.domain.TradePrecard;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.util.LocaleUtil;
import ebank.web.common.util.PartnerFactory;
import ebank.web.common.util.RequestUtil;

public class PreCardPayRec implements Controller {
	private Log log = LogFactory.getLog(this.getClass());

	private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
	private TradePrecardService tradePrecardService;
	private PayResultService payResultService;
	private PartnerFactory factory;

	public void setPayResultService(PayResultService payResultService) {
		this.payResultService = payResultService;
	}

	public PartnerFactory getFactory() {
		return factory;
	}

	public void setFactory(PartnerFactory factory) {
		this.factory = factory;
	}

	public TradePrecardService getTradePrecardService() {
		return tradePrecardService;
	}

	public void setTradePrecardService(TradePrecardService tradePrecardService) {
		this.tradePrecardService = tradePrecardService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */

	private boolean isStop = false;

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public boolean isStop() {
		return isStop;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setContentType(CONTENT_TYPE);
		HashMap<String, Object> reqs = RequestUtil.getFormInput(request);
		log.debug("From Bank:" + reqs);

		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (checkSign(request)) {

				TradePrecard tradePrecardTemp = tradePrecardService
						.selectTradePreCard(request.getParameter("MerID"),request.getParameter("MorderID"),request.getParameter("OrderTime"));
				if (tradePrecardTemp == null) {
					throw new ServiceException(EventCode.TRX_PROCESSFAILURE);
				}
				tradePrecardTemp.setSigntype(request.getParameter("SignType"));
				tradePrecardTemp.setSystemid(request.getParameter("SystemID"));
				tradePrecardTemp
						.setPaystatus(request.getParameter("PayStatus"));

				int pk = tradePrecardService
						.updateTradePreCard(tradePrecardTemp);
				if (pk != 1) {
					throw new ServiceException(EventCode.TRX_PROCESSFAILURE);
				}
				if (tradePrecardTemp.getAmount() != Amount.getIntAmount(
						String.valueOf(request.getParameter("Amount")), 2)) {
					log.info(tradePrecardTemp.getAmount()+"<>"+Amount.getIntAmount(
							String.valueOf(request.getParameter("Amount")), 2));
					throw new ServiceException(EventCode.ORDER_VALIDATE_AMOUNT);
				}

				Map<String, Object> mp = new HashMap<String, Object>();

				GwTrxs gw20 = new GwTrxs();
				GwOrders order = new GwOrders();
				gw20.setTrxnum(tradePrecardTemp.getId());
				if ("Y".equals(tradePrecardTemp.getPaystatus())) {
					gw20.setTrxsts("1");
					order.setOrdersts("3");
				} else {
					order.setOrdersts("4");
					gw20.setTrxsts("-1");
				}
				gw20.setAmount((long) tradePrecardTemp.getAmount());

				PayResult result = new PayResult(tradePrecardTemp.getId());
				result.setTrx(gw20);

				order.setOrdernum(tradePrecardTemp.getOuttradeno());
				result.setOrder(order);

				tradePrecardTemp.setFormatOrdertime(sdf.format(tradePrecardTemp
						.getOrdertime()));
				//服务器通知
				payResultService.mapPrecardResult(tradePrecardTemp, true);
				mp.put("action",
						RequestUtil.getAction(tradePrecardTemp.getMerrcvurl()));
				mp.put("locale", LocaleUtil.getLocale("local", "CN"));
				mp.put("forms", payResultService.mapPrecardResult(
						tradePrecardTemp, false));
				// tradePrecardTemp.setAmount(Double.parseDouble(Amount.getFormatAmount(String.valueOf(tradePrecardTemp.getAmount()),-2)));//将金额换算成元单位
				mp.put("result", result);

				// return new
				// ModelAndView(Constants.APP_VERSION+"/returnPreCard_url","res",RequestUtil.HtmlEscapeMap(mp));
				return new ModelAndView(
						Constants.APP_VERSION + "/resultExport", "res",
						RequestUtil.HtmlEscapeMap(mp));
			} else {
				throw new ServiceException(EventCode.SIGN_VERIFY);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView(WebConstants.ERROR_PAGE,
					WebConstants.ERROR_MODEL, new WebError(e));
		}

	}

	@SuppressWarnings({ "unchecked", "static-access", "rawtypes" })
	public boolean checkSign(HttpServletRequest reqs) {

		String signtype = (String) reqs.getParameter("SignType");
		String Version = (String) reqs.getParameter("Version");
		String MerId = (String) reqs.getParameter("MerID");
		String MorderID = (String) reqs.getParameter("MorderID");
		String SystemID = (String) reqs.getParameter("SystemID");
		String ProductName = (String) reqs.getParameter("ProductName");
		String ProductNum = (String) reqs.getParameter("ProductNum");
		String Amount = (String) reqs.getParameter("Amount");
		String CurCode = (String) reqs.getParameter("CurCode");
		String MerRcvURL = (String) reqs.getParameter("MerRcvURL");
		String OrderTime = (String) reqs.getParameter("OrderTime");
		String PayStatus = (String) reqs.getParameter("PayStatus");
		String SignMsg = (String) reqs.getParameter("SignMsg");
		String Ext1 = (String) reqs.getParameter("Ext1");
		String Ext2 = (String) reqs.getParameter("Ext2");

		List list = new ArrayList();
		list.add("MerID=" + MerId);
		list.add("MorderID=" + MorderID);
		list.add("SystemID=" + SystemID);
		list.add("ProductName=" + ProductName);
		list.add("ProductNum=" + ProductNum);
		list.add("Amount=" + Amount);
		list.add("CurCode=" + CurCode);
		list.add("MerRcvURL=" + MerRcvURL);
		list.add("OrderTime=" + OrderTime);
		list.add("Ext1=" + Ext1);
		list.add("Ext2=" + Ext2);
		list.add("PayStatus=" + PayStatus);
		MD5sign t = new MD5sign();
		list = t.ParaFilterList(list);
		String CheckValue = t.BuildMysign(list, WebConstants.KEY);

		try {
			if (SignMsg != null && SignMsg.equals(CheckValue)) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;

	}

}
