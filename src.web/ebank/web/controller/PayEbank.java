package ebank.web.controller;

import java.security.Key;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import beartool.Md5Encrypt;

import ebank.core.OrderService;
import ebank.core.SLAService;
import ebank.core.UserService;
import ebank.core.VelocityTemplate;
import ebank.core.VelocityTemplateCallback;
import ebank.core.bank.BankService;
import ebank.core.bank.BankServiceFactory;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.CryptUtil;
import ebank.core.common.util.Udate;
import ebank.core.domain.BankOrder;
import ebank.core.model.domain.GwChannel;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwPayments;
import ebank.core.model.domain.GwTrxs;
import ebank.core.model.domain.GwViewUser;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.WebEvent;
import ebank.web.common.util.LocaleUtil;
import ebank.web.common.util.RequestUtil;
import ebank.web.common.util.Validator;
import ebank.web.common.util.XSerialize;

public class PayEbank implements Controller {

	private Log log = LogFactory.getLog(this.getClass());
	private OrderService orderService;
	private VelocityTemplate velocityService;
	private SLAService slaService;
	private UserService userService;
	private static Key key = XSerialize.getKey(null);
	private BankServiceFactory bsf;
	private final String bankvm = "bank.vm";
	private String feeSignKey = "fee201203190953amount";

	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {

			String pageSign = "/common/forbank";
			String keyid = request.getParameter(WebConstants.MAP_KEY_ID);
			String pers = request
					.getParameter(WebConstants.MAP_KEY_PERSISTENCE);
			String channelToken = request
					.getParameter(WebConstants.MAP_KEY_CHANNELTOKEN); // _channelToken
			String orderId = request.getParameter(WebConstants.MAP_KEY_ORDERID);
			String payment_type = RequestUtil.HtmlEscape(request
					.getParameter("payment_type"));
			String payerNo = RequestUtil.HtmlEscape(request
					.getParameter("payerNo"));

			// add json params to pass the merchant interface params.
			String merchantparam = request.getParameter("merchantjson");
			GwChannel gc = slaService.getChannel(CryptUtil
					.decryptrandom(channelToken)[0]);

			if (gc == null) {
				throw new ServiceException(EventCode.SLA_SERVICECHANNELCOLSED);
			}
			String serviceCode = gc.getService_code();
			String outchannelNum = null;
			if (serviceCode.indexOf("-") > 0) { // include subchannel
				String[] x = serviceCode.split("-");
				serviceCode = x[0];
				outchannelNum = x[1];
			}
			String channel = gc.getChannel_type();//B2C B2B ���մ���
			String paymenttype = gc.getPayment_type();//0:�������ˣ�1:����֧��
			String paymode = gc.getPayment_mode();// ���  ���  all

			if (Validator.isNull(serviceCode) || Validator.isNull(paymenttype)) {
				throw new ServiceException(EventCode.WEB_PARAMNULL);
			}
			GwOrders morder = null;
			if (!Validator.isNull(pers)) {// charge
				morder = (GwOrders) XSerialize.deserialize(pers, key);
			}
			if (morder == null || morder.getAmount() <= 0) {
				throw new ServiceException(EventCode.ORDER_VALIDATE_AMOUNT);
			}
			BankService bank = null;
			// ȡ��֧��ģʽ
			try {
				// ת���������
				log.info(serviceCode);
				// order.setServicecode(bankId); //��׼����
				bank = bsf.getBank(bsf.getBankCode(serviceCode));// ȡ����Ӧ�����з���
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ServiceException(WebEvent.SERVICE_NOTPROVIDED, "ID:"
						+ serviceCode);
			}
			/**
			 * ===========================================�ж϶����Ƿ����ύ============
			 * ===============================
			 **/
			String trxnum = null;
			boolean isnewtrx = true;
			if (!Validator.isNull(morder.getId())) {
				GwTrxs t = orderService.findByGwOrderIDAndServiceCode(morder
						.getId(), serviceCode);
				if (t != null) {
					trxnum = t.getTrxnum();
					isnewtrx = false;
				}
			}
			trxnum = (trxnum == null ? bank.generateOrderID() : trxnum);
			
			if (("2907").equals(serviceCode)||("2910").equals(serviceCode)) {//���֧��order
				trxnum=morder.getOrdernum();
			}
			/**
			 * ===========================================�ж϶����Ƿ����ύ============
			 * ===============================
			 **/
			if (trxnum == null)
				throw new ServiceException(EventCode.SLA_SERVICENOTACTIVE, "��ͣ"
						+ bank.getBankname() + "֧������");

			GwTrxs tx0 = MapOrder2Trx(morder);

			tx0.setTrxnum(trxnum);
			tx0.setAcquirer_id(String.valueOf(gc.getId())); // add channel id
			tx0.setAcquirer_code(bank.getBankcode());
			tx0.setAcquirer_merchant(bank.getCorpid());
			if ("CMB_B2B".equals(bank.getBankcode())
					|| "PREPAYCARD".equals(bank.getBankcode())
					// || "ICBC_B2B".equals(bank.getBankcode())
					// || "CMBC".equals(bank.getBankcode())
					|| "YEP".equals(bank.getBankcode())) {
				pageSign = "/common/forbankGBK";
			}
			if ("CMBC_B2B".equals(bank.getBankcode()))
				pageSign = "/common/resultCMBC_B2B";
			
			//���þ����֧������
			JSONObject jo=null;
			if(merchantparam!=null&&merchantparam.trim().length()!=0){
				String outjson=CryptUtil.decrypt(merchantparam);
				jo=JSONObject.fromObject(outjson);
				if(jo.containsKey("defaultbank")&&jo.getString("defaultbank")!=null&&jo.getString("defaultbank").trim().length()!=0){
					tx0.setAcquirer_name(bank.getBankname()+"("+jo.getString("defaultbank")+")");
				}else{
					tx0.setAcquirer_name(bank.getBankname());
				}
			}else{
				tx0.setAcquirer_name(bank.getBankname());
			}
			
			tx0.setTrxtype(Constants.TRANS_TYPE_CONSUME);
			tx0.setPayer_ip(RequestUtil.getIpAddr(request));
			tx0.setFromacctid(gc.getAcquire_acct_id());// �յ���ID
			tx0.setFromacctnum(String.valueOf(gc.getAccount_id()));// ϵͳ�˻�
			tx0.setServicecode(gc.getService_code()); // servicecode
			tx0.setSubmitdates(Udate.format(new Date(), "yyyyMMddHHmmss"));
			tx0.setTrxsts(String.valueOf(Constants.TO_TRXSTS.WAIT_TRADE
					.ordinal()));

			tx0.setBuyer_id(morder.getBuyer_id());
			tx0.setBuyer_name(morder.getBuyer_name());

			tx0.setChannel(channel); //
			tx0.setPaymode(paymode); // ��Ǵ���
			tx0.setPayment_type(paymenttype); // ��������

			BankOrder bankorder = MapOrder2Bank(morder);
			bankorder.setRandOrderID(trxnum);
			bankorder.setCustip(RequestUtil.getIpAddr(request));

			bankorder.setPostdate(tx0.getSubmitdates());
			bankorder.setMerchantid(bank.getCorpid());
			/*
			 * ================================================B2B�����˿�============
			 * ====================================================
			 */
			if ("2".equals(gc.getChannel_type())
					&& "999999".equals(gc.getTerminal())) {
				String gCorpName = request.getParameter("gCorpName") == null ? ""
						: request.getParameter("gCorpName").toString().trim();
				String gContactPhone = request.getParameter("gContactPhone") == null ? ""
						: request.getParameter("gContactPhone").toString()
								.trim();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("��ҵ����", gCorpName);
				jsonObject.put("��ϵ�绰", gContactPhone);
				String trxdesc = jsonObject.toString();

				tx0.setTrxdesc(trxdesc);
				tx0
						.setOpers(request.getParameter("gFinanceContact") == null ? ""
								: request.getParameter("gFinanceContact")
										.toString().trim());
			}
			/*
			 * ================================================B2B�����˿�============
			 * =====================================================
			 */
			bankorder.setBankID(bank.getBankcode());

			HashMap<String, Object> bankOrderMp = new HashMap<String, Object>();
		      bankOrderMp.put("systemOrderId", morder.getId());
			if (merchantparam != null) {
				bankOrderMp.put("outJson", merchantparam);
			}
			if (outchannelNum != null) {
				bankOrderMp.put("outChannel", outchannelNum);

			}
			
			//��֧ͨ������==========================
			if(request.getParameter("directBankCode")!=null){
				bankOrderMp.put("directBankCode", request.getParameter("directBankCode"));
			}
			
			if(request.getParameter("pay_type")!=null){
				bankOrderMp.put("pay_type", request.getParameter("pay_type"));
			}
			//��֧ͨ������==========================
			
			
		      if (request.getParameter("fengFuTradeId") != null) {
		          bankOrderMp.put("fengFuTradeId", request.getParameter("fengFuTradeId"));
		        }
			
			//��������֧������
			if(request.getParameter("orderInfoUrl")!=null){
				bankOrderMp.put("orderInfoUrl", request.getParameter("orderInfoUrl"));
			}
			//��������֧������
			
			//����֧������
			bankOrderMp.put("currentMerchantId", morder.getPartnerid());
			
		      bankOrderMp.put("bankCardType", request.getParameter("bankCardType"));

			bankorder.setMp(bankOrderMp);
			/**
			 * ================================================��������B2B,��ͨB2B,
			 * �B2B
			 * �������������ҵ�ͻ�֧������Ҫ�жϸ���ͻ����Ƿ�����====================================
			 * ===============================
			 **/
			if (("CMBC_B2B".equals(bank.getBankcode()) || ("SPDB_B2B"
					.equals(bank.getBankcode())))
					&& Validator.isNull(payerNo))
				throw new ServiceException(EventCode.ORDER_PAYERNOTFOUND,
						"�������ύ����!");
			if (("CMBC_B2B".equals(bank.getBankcode()) || ("SPDB_B2B"
					.equals(bank.getBankcode())))
					&& !Validator.isNull(payerNo)) {
				bankorder.setPayaccount(payerNo);
			}
			if (("BOCM_B2B".equals(bank.getBankcode()))
					&& Validator.isNull(payerNo))
				throw new ServiceException(EventCode.ORDER_PAYENONOTFOUND,
						"�������ύ����!");
			if (("BOCM_B2B".equals(bank.getBankcode()))
					&& !Validator.isNull(payerNo)) {
				bankorder.setPayaccount(payerNo);
			}
			/**
			 * =====================================���ѿ������ѷ��ʡ�ʵ��֧�������ʾ==========
			 * ==================
			 **/
			String feeAmount = request.getParameter("feeAmount");
			String feeSign = request.getParameter("feeSign"); // �����ѽ��ԭʼǩ��
			if (!Validator.isNull(feeAmount)) {
				String feeSign2 = Md5Encrypt.md5(feeAmount + this.feeSignKey,
						"UTF-8"); // �����ѽ����ǩ
				if (feeSign2.equals(feeSign)) {
					tx0.setFee_amount(Amount.getIntAmount(feeAmount, 2));
					if (tx0.getFee_amount() != 0) {
						tx0.setAmount(tx0.getAmount() + tx0.getFee_amount());
						bankorder.setAmount(Amount.getFormatAmount(String
								.valueOf(tx0.getAmount()), -2));
					}
				} else {
					throw new ServiceException(WebEvent.ARG_AMOUNT_NOTEQUALS);
				}
			}

			/**
			 * =====================================���ѿ������ѷ��ʡ�ʵ��֧�������ʾ==========
			 * ==================
			 **/

			if (!Validator.isNull(keyid)) { // direct charge trx without orderid
				String id = CryptUtil.decrypt(keyid);
				GwPayments payment = new GwPayments();
				GwOrders order = orderService.findOrderByPk(id);
				if (order == null || order.getAmount() != morder.getAmount()) {
					throw new ServiceException(EventCode.ORDER_CHECK_EXCEPTION);
				}

				String allowcode = orderService.tx_getPayAllow(order.getId(),
						bank.getBankcode(), bank.getCorpid());
				if (!"0".equals(allowcode)) {
					throw new ServiceException(allowcode);
				}

				if (!Validator.isNull(request.getSession().getAttribute(
						WebConstants.SESSION_USER_PAYWD))
						&& "1".equals(request.getParameter("_compositemode"))) {// directpay
					// validator paypass and balance
					long feeamount = 0;
					if (!Validator.isNull(tx0.getFee_amount())) {
						feeamount = tx0.getFee_amount();
					}
					tx0.setAmount(order.getAmount() - order.getDirectpayamt()
							+ feeamount);
					bankorder.setAmount(Amount.getFormatAmount(String
							.valueOf(tx0.getAmount()), -2));
					if (order.getDirectpayamt() <= 0 || tx0.getAmount() <= 0) {
						throw new ServiceException(
								EventCode.ORDER_VALIDATE_AMOUNT);
					}
				}
				payment.setAmount(order.getAmount());
				payment.setChannel(Constants.PAYMENT_CHANNEL.ACCOUNT.ordinal()
						+ "");
				payment.setInfromacct("");
				payment.setPaytype(Constants.PAYMENT_PAYTYPE.GWORDER.ordinal()
						+ "");
				payment.setPaynum(id);
				payment.setPayamount(order.getAmount());

				tx0.setGworders_id(id);

				tx0.setPaymentid(orderService.tx_savePayments(payment));
			}
			// ������ֵ��������
			/**
			 * ===========================================�ж϶����Ƿ����ύ============
			 * ===============================
			 **/

			if (isnewtrx) {
				if (orderService.tx_saveTrxs(tx0) == null)
					// ���涩��ʧ��
					throw new ServiceException(EventCode.ORDER_SAVE);
			}
			/**
			 * ==========================================�ж�֧����ʽ================
			 * ================================
			 **/
			if (payment_type != null && !"".equals(payment_type)) {
				bankorder.setPayment_type(payment_type);
			}
			final String bankname = bank.getBankname();
			final String paytype = bank.getPaytype();
			final String strOut = bank.sendOrderToBank(bankorder);

			if (("2907").equals(serviceCode)||("2910").equals(serviceCode)) {// ����ǿ��֧��

				if (strOut == null) {
					throw new ServiceException(EventCode.SLA_SERVICENOTACTIVE,
							"ǩ����֤ʧ��!");
				} else {
					HashMap<String, Object> mp = new HashMap<String, Object>();
					String[] arr=strOut.split("\\$");
					mp.put(WebConstants.Action,arr[1]);
					
					GwViewUser user = userService.getViewUser(morder.getPartnerid()+"", "online");
					
					String signStr=arr[0].substring(6, arr[0].trim().length()-7);
					String sign = Md5Encrypt.md5(signStr+user.getMd5Key(), morder.getCharsets().trim());
					signStr=("<data>"+signStr+"<sign>"+sign+"</sign></data>");
					mp.put("resp", signStr);
					
					response.getWriter().write(signStr);
					response.getWriter().close();
					
					log.debug(mp);
//					return new ModelAndView(
//							Constants.APP_VERSION + "/redirect",
//							WebConstants.PaRes, RequestUtil.HtmlEscapeMap(mp));
					return null;
				}

			}

		    if ("2927".equals(serviceCode))
		      {
		        if (strOut == null) {
		          throw new ServiceException("511603", 
		            "ǩ����֤ʧ��!");
		        }

		        response.getWriter().write(strOut);
		        response.getWriter().close();

		        return null;
		      }

		      if ("2928".equals(serviceCode))
		      {
		        if (strOut == null) {
		          throw new ServiceException("511603", 
		            "ǩ����֤ʧ��!");
		        }
		        
		        if ("".equals(strOut)) {
			          throw new ServiceException("511603", 
			            "�������ʹ���!");
			        }

		        response.getWriter().write(strOut);
		        response.getWriter().close();

		        return null;
		      }

		      if ("0917".equals(serviceCode)) {
		        pageSign = "/common/formobile";
		      }

		      if ("2911".equals(serviceCode)) {
		        response.sendRedirect(strOut);

		        return null;
		      }
		      
			if (strOut == null)
				throw new ServiceException(EventCode.SLA_SERVICENOTACTIVE,
						"��ͣ��֧������");
			if (log.isDebugEnabled())
				log.debug("strout=" + strOut);
			String html = velocityService
					.getMessage(new VelocityTemplateCallback() {
						public void execute(Map<String, Object> mps,
								Object[] arg) {
							if (Constants.PAYTYPE_DEBIT.equals(paytype)
									|| Constants.PAYTYPE_MOBILE.equals(paytype)
									|| Constants.PAYTYPE_B2B.equals(paytype)) {// �ڿ�or
								// B2B
								// OR
								// TELCARD
								String s = "<body onload=\"javascript:document.sendOrder.submit();\">";
								mps.put("form", s + strOut);
								s = null;
							} else {
								mps.put("form", strOut);
							}
							arg[0] = mps;
							arg[1] = bankvm;
						}
					});
			bank = null;
			Map<String, Object> mp = new HashMap<String, Object>();
			mp.put("bank", bankname);
			mp.put("form", html);
			mp.put("locale", LocaleUtil.getLocale(request
					.getParameter("locale"), "CN")); // i18n
			log.debug(mp);
			return new ModelAndView(pageSign, "page", mp);

			/**
			 * ===========================================�ж϶����Ƿ����ύ============
			 * ===============================
			 **/
		} catch (Exception ex) {
			if (!(ex instanceof ServiceException))
				ex.printStackTrace();
			return new ModelAndView(WebConstants.ERROR_PAGE,
					WebConstants.ERROR_MODEL, new WebError(ex));
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

	private GwTrxs MapOrder2Trx(GwOrders order) {
		GwTrxs tx0 = new GwTrxs();
		tx0.setAmount(order.getAmount());
		if (order.getId() != null)
			tx0.setGworders_id(order.getId());
		tx0.setCurrency(order.getCurrency());
		return tx0;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public void setVelocityService(VelocityTemplate velocityService) {
		this.velocityService = velocityService;
	}

	public void setBsf(BankServiceFactory bsf) {
		this.bsf = bsf;
	}

	public void setSlaService(SLAService slaService) {
		this.slaService = slaService;
	}

}
