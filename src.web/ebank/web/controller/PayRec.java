package ebank.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.alibaba.fastjson.JSONObject;

import ebank.core.OrderService;
import ebank.core.PayResultService;
import ebank.core.STEngineService;
import ebank.core.bank.BankService;
import ebank.core.bank.BankServiceFactory;
import ebank.core.bank.logic.BankCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.YinTongUtil;
import ebank.core.domain.PayResult;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwTrxs;
import ebank.core.model.domain.TrxsNum;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.WebEvent;
import ebank.web.common.util.LocaleUtil;
import ebank.web.common.util.RequestUtil;
import ebank.web.common.util.Validator;

/**
 * @author xiexh Description: 接收银行返回结果
 * 
 */
public class PayRec implements Controller {
	private Log log = LogFactory.getLog(this.getClass());
	private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

	private OrderService orderService;
	private PayResultService payResultService;
	private BankServiceFactory services;
	private String resultExport;
	private STEngineService engineService;
	private boolean productionMode = false;

	private String getAdaptorBankID(HttpServletRequest request)
			throws ServiceException {
		String bankID = "";
		String uri = RequestUtil.HtmlEscape(request.getRequestURI());
		System.out.println("***********************From Bank:" + uri);
		if (uri != null && uri.indexOf("/PayRec/") >= 0
				&& uri.indexOf(".idx") > 0) {// special url return url
			return uri.substring(uri.indexOf("/PayRec/") + 8, uri
					.indexOf(".idx"));
		} 
		if (request.getParameter("ERR") != null) {
			throw new ServiceException(request.getParameter("ERR"));
		}

		if (request.getParameter("idx") != null) {
			bankID = String.valueOf(request.getParameter("idx"));
			bankID = bankID.split("\\?")[0];
			// 以下处理url不能带'?idx='号的银行
		} else if (request.getParameter("interfaceVersion") != null) { // 工行
			bankID = BankCode.BKID_ICBC[0];
		} else if (request.getParameter("merVAR") != null
				&& request.getParameter("notifyData") != null) { // 工行 version
			// 1.0.0.11
			bankID = BankCode.BKID_ICBC_11[0];
		} else if (request.getParameter("EncMsg") != null) { // 中行
			bankID = BankCode.BKID_BOC[0];
		} else if (request.getParameter("MerchantPara") != null) {
			bankID = String.valueOf(RequestUtil.HtmlEscape(request
					.getParameter("MerchantPara"))); // 招行处理
		} else if (request.getParameter("BillNo") != null
				&& request.getParameter("Signature") != null) {
			bankID = BankCode.BKID_CMB[0];
		} else if (request.getParameter("payresult") != null) {// 民生银行
			if (request.getParameter("payresult").indexOf("<CMBCB2B>") >= 0) {
				bankID = BankCode.BKID_CMBC_B2B[0];
			} else {
				bankID = BankCode.BKID_CMBC[0];
			}
		} else if (request.getParameter("netBankTraceNo") != null) {// 兴业银行
			bankID = BankCode.BKID_CIB[0];
		} else if (request.getParameter("sequence") != null) { // 广发银行
			bankID = BankCode.BKID_GDB[0];
		} else if (request.getParameter("BRANCHID") != null) {// 建行
			// 不用区分分行
			bankID = BankCode.BKID_CCB[0];
		} else if (request.getParameter("MPOSID") != null) {// 建行
			// 不用区分分行
			bankID = BankCode.BKID_B2B_CCB[0];
		} else if (request.getParameter("checkvalue") != null) {// 处理上海银联
			bankID = BankCode.BKID_SYL[0];
		} else if (request.getParameter("return_msg") != null) {// GYL
			bankID = BankCode.BKID_GYL[0];
		} else if (request.getParameter("NetpayNotifySignData") != null) {// GYL
			bankID = BankCode.BKID_B2B_COMM[0];
		} else if (request.getParameter("cq") != null) {// 重庆商行 (带'?'bug)
			String arg = request.getParameter("cq");
			if (arg.indexOf("errorcode=p80017") > 0)
				bankID = BankCode.BKID_CQCB[0];
			else if (arg.indexOf("errorcode=p80012") > 0)
				throw new ServiceException("400000");
			else if (arg.indexOf("errorcode=p80015") > 0)
				throw new ServiceException("400001");
			else if (arg.indexOf("errorcode=1091") > 0
					|| arg.indexOf("errorcode=1034") > 0)
				throw new ServiceException("400002");
			else if (arg.indexOf("errorcode=ICORE|EBK0021") > 0) {
				throw new ServiceException("400000");
			} else
				throw new ServiceException("400004");
		} else if (uri.indexOf("tftPay") > -1) {
			bankID = BankCode.BKID_B2C_TFT[0];

		} else if (uri.indexOf("bankOnline") > -1) {
			bankID = request.getParameter("idx");
//			BankCode.BKID_B2C_BANKONLINE[0];
		} else if ((uri.indexOf("LefuPayNotifyMobile") > -1) || (uri.indexOf("LefuPayRedirectMobile") > -1)) {
		      bankID = BankCode.BKID_LefuPayMobile[0];
	    } else if ((uri.indexOf("LefuPayNotify") > -1) || (uri.indexOf("LefuPayRedirect") > -1)) {
	      bankID = BankCode.BKID_LefuPay[0];
	    } else if (uri.indexOf("fengPayNotify") > -1) {
	      bankID = BankCode.BKID_fengPay[0];
	    }  else if (uri.indexOf("RongxinNotify") > -1) {
	      bankID = BankCode.BKID_B2C_RONGXIN[0];
	    } else if (uri.indexOf("unionMobilePayNotify") > -1) {
	      bankID = BankCode.BKID_unionMPay[0];
	      }else if (request.getParameter("remark") != null
				&& request.getParameter("remark").equals("0908")) {// 汇付
			bankID = BankCode.BKID_HeePay[0];
		} else {
			throw new ServiceException(WebEvent.SERVICE_URLPARAM);
		}

		return bankID;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setContentType(CONTENT_TYPE);
		PrintWriter out = null;
		HashMap<String, Object> reqs = RequestUtil.getFormInput(request);
		String uri = RequestUtil.HtmlEscape(request.getRequestURI());
		if(uri.indexOf("RongxinNotify") > -1){
			reqs.put("rongxin", "rongxin");
		}
		System.out.println("***********************From Bank:" + reqs);
		log.debug("From Bank:" + reqs);
		BankService bank = null;
		// 取得相应的银行
		try {
			out = response.getWriter();
			// error page
			String bankID = this.getAdaptorBankID(request); // 适配银行

			if ("2909".equals(bankID)) {// 若是连连支付

				String dataJson = YinTongUtil.readReqStr(request);

				Map jsonMap = (Map) reqs.clone();
				jsonMap.remove("RemoteIP");
				jsonMap.remove("queryString");
				jsonMap.remove("NR");
				jsonMap.remove("idx");

				if (jsonMap.size() == 0) {// 判断是够异步通知还是页面跳转;0:异步通知 else:页面跳转
					reqs.put("yintongReq", dataJson);
					
					//解析json设置value到map
					JSONObject jsonData = (JSONObject) JSONObject
							.parse(dataJson);
					Iterator<String> keySet = jsonData.keySet().iterator();
					while (keySet.hasNext()) {
						String key = keySet.next();
						reqs.put(key, jsonData.getString(key));
					}

				} else {
					reqs.put("yintongReq", JSONObject.toJSONString(jsonMap));
				}
				jsonMap = null;
			}
			if (log.isDebugEnabled())
				log.debug("bank idx=" + bankID);
			try {
				bank = services.getBank(bankID);// 取得相应的银行服务
				if (log.isDebugEnabled())
					log.debug("RESULT FROM:" + bank.getBankname());
			} catch (Exception ex) {
				throw new ServiceException(WebEvent.SERVICE_NOTPROVIDED);
			}
			if (bankID != null && bankID.length() >= 3)
				if ((BankCode.BKID_ICBC[0].equals(bankID.substring(0, 3)) && !reqs
						.containsKey("v_md5info"))
						|| (BankCode.BKID_B2B_ICBC[0].equals(bankID) && !reqs
								.containsKey("v_md5info"))
//						|| (BankCode.BKID_B2B_BOC[0].equals(bankID) && !reqs
//								.containsKey("v_md5info"))
						|| (BankCode.BKID_BOB[0].equals(bankID))
						&& !(reqs.containsKey("v_info"))// 北京银行，工商银行,广银联
				) {
					String url = bank.getRcvURL(reqs);
					response.setContentLength(url.length());
					if (log.isDebugEnabled())
						log.debug("** special url **=" + url);
					out.print(url);
					return null;
				}
			if (BankCode.BKID_B2B_CCB[0].equals(bankID)
					&& !reqs.containsKey("AMOUNT")) {
				if (log.isDebugEnabled())
					log.debug("** CCB_B2B Post args is null**");
				return null;
			}

			PayResult result = null;

			log.debug("mode value:" + request.getParameter("mode"));

			log.debug("mode judge :"
					+ (!productionMode && "test".equals(request
							.getParameter("mode"))));
			// TODO :IMPORTANT //MOCK RETURN
			if (!productionMode && "test".equals(request.getParameter("mode"))) {

				log
						.warn("********* System is running on test receive result Mode,remember to close the test mode ********");
				result = new PayResult(request.getParameter("trxnum"), request
						.getParameter("bankcode"), new BigDecimal(request
						.getParameter("amount")), Integer.parseInt(request
						.getParameter("sts")));
			} else {
				// 构造支付结果
				log.debug("come in boc");
				result = bank.getPayResult(reqs);
			}
			
		    if (ebank.core.bank.logic.BankCode.BKID_fengPay[0].equals(bankID)) {
		          TrxsNum trxs = this.orderService.findTrxnumByOrderId(result.getTransnum(), result.getBankcode());
		          result.setTransnum(trxs.getTrxnum());
		    }
		      
			log.debug("result is value:" + result);
			if (result == null) {
				throw new ServiceException(WebEvent.PAYRESULT_HANDLE_FAILURE);
			}
			result.setPayer_ip(RequestUtil.getIpAddr(request)); // 记录结果clientIP
			GwTrxs gw20 = orderService.findTrxByTrxnum(result.getTransnum(),
					result.getBankcode());

			GwOrders gw10 = orderService.findOrderByTrx(result.getTransnum(),
					result.getBankcode()); // 网银的订单号

			if (gw20 == null) {
				throw new ServiceException(WebEvent.ORDER_NOTFOUNDBYSEQ, result
						.getTransnum());
			} else {
				// 校验金额
				if (result.getBankamount() != null
						&& gw20.getAmount() != Amount.getIntAmount(String
								.valueOf(result.getBankamount()), 2)) {
					throw new ServiceException(WebEvent.ORDER_PRICENOTEQUAL); // 同步校验金额
				}

				// 校验币种
				if (result.getCurrency() != null
						&& !gw20.getCurrency().equalsIgnoreCase(
								result.getCurrency())) {
					throw new ServiceException(WebEvent.CURRENCY_NOTMATCH); // 同步校验币种
				}
				// 不审核该返回订单
				result.setOrder(gw10);
				result.setTrx(gw20);

				// 处理民生银行嵌套页面
				if (!Validator.isNull(bank)
						&& bank.getBankcode().equals("CMBC")) {
					this.setResultExport("v4/resultExportForCMBC");
				} else {
					this.setResultExport("v4/resultExport");
				}
				
				 if ("0917".equals(bankID)) {
				        setResultExport("v4/resultExportForMobile");
				 }
				 
				// i18n
				if ("1".equals(gw20.getTrxsts())) { // charge without payment or
					// repeated refresh
					if (isBggroud(bankID, reqs, out))
						return null;
					if ("2".equals(reqs.get("r9_BType")))
						return null;// 易宝支付时，交易类型是2时为服务器点对点通讯
					request.getSession().setAttribute(
							"e_lang",
							LocaleUtil.getLocale(gw10 == null ? "CN" : gw10
									.getLocale(), "CN"));
					Map<String, Object> mp = new HashMap<String, Object>();
					mp.put("result", result);
					mp.put("trx", gw20);
					if (gw10 != null) {
						mp.put("order", gw10);
						if (result.getOrder() != null) {
							mp.put("locale", LocaleUtil.getLocale(result
									.getOrder().getLocale(), "CN"));
							mp.put("forms", payResultService.mapresult(result
									.getOrder(),false));
						}
						Map<String, Object> map = RequestUtil.HtmlEscapeMap(mp);
						if (result.getOrder() != null)
							map.put("action", RequestUtil.getAction(result
									.getOrder().getReturn_url()));
						return new ModelAndView(this.getResultExport(), "res",
								map);
					} else
						return new ModelAndView("common/payresult", "res",
								RequestUtil.HtmlEscapeMap(mp));
				}

			}

			GwTrxs trx = null;

			trx = engineService.post(result);

			if (trx != null
					&& (!"1".equals(trx.getTrxsts()) && !"-1".equals(trx
							.getTrxsts()))) {
				throw new ServiceException(WebEvent.TRX_PROCESSFAILURE);
			}
			if (trx == null)
				throw new ServiceException(WebEvent.STATE_UPDATEFAILURE);
			// 后台通知
			if (isBggroud(bankID, reqs, out))
				return null;
			// 控制导向
			String exporter = this.getResultExport();
			// 指定模型导向action

			// if ("2909".equals(bankID)) {// 若是连连支付
			//
			// if ("0000".equals(result.getBankresult())) {// 通知连连通知发送通知
			// out.write("{\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"}");
			// } else {
			// out.write("{\"ret_code\":\"9999\",\"ret_msg\":\"交易失败\"}");
			// }
			// out.flush();
			//
			// }

			Map<String, Object> mp = new HashMap<String, Object>();
			mp.put("result", result);
			mp.put("action", "#");
			if (result.getOrder() != null) {
				// get update order
				result.setOrder(orderService.findOrderByPk(result.getOrder()
						.getId()));
				mp.put("locale", LocaleUtil.getLocale(result.getOrder()
						.getLocale(), "CN"));
				mp.put("forms", payResultService.mapresult(result.getOrder(),
						false));
			}
			Map<String, Object> map = RequestUtil.HtmlEscapeMap(mp);
			if (result.getOrder() != null)
				map.put("action", RequestUtil.getAction(result.getOrder()
						.getReturn_url()));
			

			return new ModelAndView(this.getResultExport(), "res", map);
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView(WebConstants.ERROR_PAGE,
					WebConstants.ERROR_MODEL, new WebError(e));

		} finally {
			out.close();
			reqs = null;
		}
	}

	/**
	 * 后台通知快捷支付
	 * @param params
	 */
	private void sendFastpay(Map<String,Object> params){
		
		HttpClient client=new HttpClient();
		
		PostMethod post=new PostMethod(params.get("action").toString());
		
		post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());//IO异常默认重发3次
		post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 7000);
		
		Iterator<String> keySet=params.keySet().iterator();
		
		while(keySet.hasNext()){
			String key=keySet.next().toString();
			post.addParameter(key, params.get(key).toString());
		}
		
		try {
			client.executeMethod(post);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		post.releaseConnection();
		
	}
	
	
	
	private boolean isBggroud(String bankid, HashMap reqs, PrintWriter out)
			throws ServiceException {
		if (bankid != null
				&& (String.valueOf("SID_" + bankid).equalsIgnoreCase(
						String.valueOf(reqs.get("NR"))) || bankid.equals(String
						.valueOf(reqs.get("NR")))||reqs.containsKey("rongxin"))) {
			log.debug("response background:" + bankid + "[" + reqs.get("RES")
					+ "]");
			String resp = String.valueOf(reqs.get("RES") == null ? bankid
					: reqs.get("RES"));
			out.print(resp);
			out.flush();
			reqs.remove("RES");
			return true;
		} else {
			log
					.debug(String.valueOf("SID_" + bankid) + " NR="
							+ reqs.get("NR"));
		}
		return false;
	}

	/**
	 * @param orderService
	 *            The orderService to set.
	 */
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	/**
	 * @param engineService
	 *            The engineService to set.
	 */
	public void setEngineService(STEngineService engineService) {
		this.engineService = engineService;
	}

	/**
	 * @param payResultService
	 *            The payResultService to set.
	 */
	public void setPayResultService(PayResultService payResultService) {
		this.payResultService = payResultService;
	}

	/**
	 * @param services
	 *            The services to set.
	 */
	public void setServices(BankServiceFactory services) {
		this.services = services;
	}

	/**
	 * @param resultExport
	 *            The resultExport to set.
	 */
	public void setResultExport(String resultExport) {
		this.resultExport = resultExport;
	}

	/**
	 * @return Returns the resultExport.
	 */
	public String getResultExport() {
		return resultExport;
	}

	public boolean isProductionMode() {
		return productionMode;
	}

	public void setProductionMode(boolean productionMode) {
		this.productionMode = productionMode;
	}

}
