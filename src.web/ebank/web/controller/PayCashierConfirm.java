package ebank.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.Key;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import beartool.Md5Encrypt;
import ebank.core.OrderService;
import ebank.core.SLAService;
import ebank.core.UserService;
import ebank.core.common.BankInfo;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.Clazz;
import ebank.core.common.util.CryptUtil;
import ebank.core.model.domain.CmCustomerChannel;
import ebank.core.model.domain.GwChannel;
import ebank.core.model.domain.GwMerChannelRate;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwSuborders;
import ebank.core.model.domain.GwViewUser;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.util.RequestUtil;
import ebank.web.common.util.Validator;
import ebank.web.common.util.XSerialize;

public class PayCashierConfirm implements Controller {
	private static Key key = XSerialize.getKey(null);
	private SLAService slaService;
	private OrderService orderService;
	private Log log = LogFactory.getLog(this.getClass());
	private String feeSignKey = "fee201203190953amount";
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse arg1) throws Exception {
		try {
			String orderid = request.getParameter(WebConstants.MAP_KEY_ORDERID);
			String pers = request
					.getParameter(WebConstants.MAP_KEY_PERSISTENCE);
			String channelcode = request
					.getParameter(WebConstants.MAP_KEY_CHANNELCODE);
			String fraudcheck = RequestUtil.HtmlEscape(request
					.getParameter("fraudcheck"));
			String userID = RequestUtil.HtmlEscape(request
					.getParameter("userID"));
			// instanced the morder;
			String oid = CryptUtil.decrypt(request
					.getParameter(WebConstants.MAP_KEY_ID));
			GwOrders morder = orderService.findOrderByPk(oid);
			if (morder == null) {
				morder = (GwOrders) XSerialize.deserialize(pers, key);
			}
			List<GwSuborders> subOrderslist = null;
			if ("12".equals(morder.getRoyalty_type())) {
				subOrderslist = orderService.findSubOrderByGwOdersId(morder
						.getId());
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("_subOrderslist", subOrderslist);
			map.put(WebConstants.MAP_KEY_ORDER, morder);
			map.put(WebConstants.MAP_KEY_PERSISTENCE, XSerialize.serialize(
					morder, key));
			map.put(WebConstants.MAP_KEY_CHANNELCODE, channelcode);
			map.put(WebConstants.MAP_KEY_ID, request
					.getParameter(WebConstants.MAP_KEY_ID));
			map.put("_bankname", request.getParameter("_bankname")
					.toUpperCase());
			map.put("_paytype", request.getParameter("_paytype"));
			map.put("payment_type", request.getParameter("payment_type"));

			map.put("userID", userID);
			map.put("fraudcheck", fraudcheck);
			if (channelcode.indexOf("-") > 0) {
				channelcode = channelcode.split("-")[0];
			}

			String defaultbank = channelcode;

			if (!channelcode.equals(Constants.ACQ_CODE_)) {//����
//				channelcode = Constants.ACQ_CODE_;// �ڸ�ͨ�ӿ�
//				channelcode = "UNIONPAY";// �ڸ�ͨ�ӿ�
//			} else {
				Properties p = null;
				try {
					p = PropertiesLoaderUtils
							.loadProperties(new ClassPathResource(
									"ChannelFlag.properties"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (p == null || "1".equals(p.getProperty("heepay"))) {// ��ͨ�㸶ͨ��
					channelcode = Constants.ACQ_CODE_HEEPAY;// �㸶�ӿ�
				} else if ("1".equals(p.getProperty("online"))) {// ��ͨ��������ͨ��
					if ("ICBC".equals(channelcode) || "CCB".equals(channelcode)
							|| "ABC".equals(channelcode)
							|| "BOC".equals(channelcode)
							|| "CMBC".equals(channelcode)) {// �����ͨ��
						channelcode = Constants.ACQ_CODE_ONLINE_FIVE;// �������߽ӿ�
					} else {
						channelcode = Constants.ACQ_CODE_ONLINE;// �������߽ӿ�
					}
				} else if ("1".equals(p.getProperty("yintong"))) {//
					if (("ICBC".equals(channelcode)
							|| "CCB".equals(channelcode)
							|| "ABC".equals(channelcode)
							|| "BOC".equals(channelcode) || "CMBC"
							.equals(channelcode))
							&& "1"
									.equals(p
											.getProperty("yintongButFiveOnline"))) {// ����м�����ͨ������������
						channelcode = Constants.ACQ_CODE_ONLINE_FIVE;//

					} else if ("CMB".equals(channelcode)) {

						channelcode = Constants.ACQ_CODE_ONLINE;// �������߽ӿ�

					} else {
						channelcode = Constants.ACQ_CODE_YINTONG;// ��ҵ��������ͨ�ӿ�
					}
				}
			}

			/**************************************************************************************/
			GwViewUser user = this.userService.getViewUser(String
					.valueOf(morder.getPartnerid()), "online");

			List<CmCustomerChannel> channelList = this.userService
					.findUserChannelList(user.getUserId() + "");

			boolean isRongxinExist = false;

			JSONObject requestJson = JSONObject.fromObject(RequestUtil
					.getFormParams(request));
			requestJson.put("defaultbank", defaultbank);
			map.put("merchantjson", CryptUtil.encrypt(requestJson.toString()));

			for (int i = 0; i < channelList.size(); i++) {
				if ("RONGXIN".equals(((CmCustomerChannel) channelList.get(i))
						.getBank_code())) {
					if ("BOCM".equals(defaultbank)) {
						defaultbank = "COMM";
					}
					map.put("directBankCode", defaultbank);
					map.put("pay_type", Integer.valueOf(8));

					isRongxinExist = true;
				}
			}

			GwChannel gc = null;
			int channelid = 0;

			Properties p = null;
			try {
				p = PropertiesLoaderUtils.loadProperties(new ClassPathResource(
						"RongxintongChannel.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			String specialBank = p
					.getProperty(morder.getPartnerid() + "", "-1");

			if ((isRongxinExist) && (!"UNIONGATEWAY".equals(channelcode))) {
				channelid = this.slaService.getRouteChannelExt(oid, "RONGXIN",
						morder.getAmount(), String.valueOf(morder
								.getPartnerid()));
				gc = this.slaService.getChannel(channelid);
			} else if ((!"-1".equals(specialBank))
					&& (Arrays.asList(specialBank.split(",")).indexOf(
							defaultbank.trim()) > -1)) {
				channelid = this.slaService.getRouteChannelExt(oid, "RONGXIN",
						morder.getAmount(), String.valueOf(morder
								.getPartnerid()));
				gc = this.slaService.getChannel(channelid);
				if ("BOCM".equals(defaultbank)) {
					defaultbank = "COMM";
				}
				map.put("directBankCode", defaultbank.trim());
				map.put("pay_type", Integer.valueOf(8));
			} else {

				channelid = slaService.getRouteChannelExt(oid, channelcode,
						morder.getAmount(), String.valueOf(morder
								.getPartnerid()));
				log.info("return Channel:" + channelid + " [" + oid + ","
						+ channelcode + "," + morder.getAmount() + ","
						+ morder.getPartnerid() + "]");

				// String defaultbank = request.getParameter("defaultbank");

				String payTypeFlag = request.getParameter("payment_type");// 2:����1�����

				if ("1".equals(payTypeFlag)) {
					payTypeFlag = "2";
				} else {
					payTypeFlag = "1";
				}
				GwViewUser seller = userService.getViewUserWithIdAndName(morder
						.getSeller_id(), morder.getSeller_name());
				// GwViewUser user =
				// userService.getViewUser(String.valueOf(morder
				// .getPartnerid()), "online");
				// ������ǰ��ԃ��channelIdƥ��·���O����
				channelid = Integer.parseInt(slaService
						.getRouteChannelByAmount(Amount.getFormatAmount(
								new BigDecimal(morder.getAmount())
										.doubleValue()
										+ "", -2)
								+ "", defaultbank.trim(), channelid + "",
								seller.getUserId() + "", payTypeFlag));

				// ��ȡ��ǰ�̻��ķ��������
				gc = slaService.getChannel(channelid);

				// List<CmCustomerChannel> channelList = userService
				// .findUserChannelList(user.getUserId() + "");
				boolean isAvailable = false;// ͨ���Ƿ����
				String bankId = null;
				//			
				// requestJson=JSONObject.fromObject(
				// RequestUtil.getFormParams(request));
				// requestJson.put("defaultbank", defaultbank);
				// map.put("merchantjson",
				// CryptUtil.encrypt(requestJson.toString()));
				for (CmCustomerChannel channel : channelList) {

					if (channel.getChannel_code().toUpperCase().equals(
							gc.getAcquire_indexc().trim().toUpperCase())) {// ��ǰ����

						isAvailable = true;

						String directBank = defaultbank;

						bankId = BankInfo.bankInfo.get(directBank);

						map.put("directBankCode", bankId);

						if ("1".equals(channel.getChannel_type())) {//
							// ����ͨ���ͨ���������̻�ѡ����ͨ��
							// //b2c���
							if (payTypeFlag != null && "2".equals(payTypeFlag)) {
								map.put("pay_type", 1);// ��ǿ�
								break;
							} else if (payTypeFlag == null) {
								map.put("pay_type", 1);
							}

						} else if ("2".equals(channel.getChannel_type()))// b2c����
						{
							if (payTypeFlag != null && "1".equals(payTypeFlag)) {
								map.put("pay_type", 8);// ���ǿ�
								break;
							} else if (payTypeFlag == null) {
								map.put("pay_type", 8);
							}

						} else {
							isAvailable = false;
						}

					}

				}

				if (channelid == 0 || !isAvailable
						|| map.get("pay_type") == null) {
					throw new ServiceException(EventCode.SLA_NOSERVICECHANNEL);
				}
			}
			if ("1".equals(request.getParameter("_compositemode"))
					&& !Validator.isNull(morder.getBuyer_id())
					&& morder.getPaymethod().equals("balancePay")) {
				map.put("_directPayAmt", morder.getDirectpayamt());
				// �ж��Ƿ����ÿ�,��������ÿ�����Ҫ�����ֻ���֤��,������Ҫ
				String istravel = request.getParameter("cashiertravel");
				if ("".equals(istravel)) {
					GwOrders torder = orderService.findOrderByPk(oid);
					if (Validator.isNull(request
							.getParameter("_compositemobile"))) {
						throw new ServiceException(
								EventCode.PAGE_MOBILE_RVERFIYCODE);
					}
					if (!request.getParameter("_compositemobile").toString()
							.equals(torder.getQuery_key())) {
						throw new ServiceException(
								EventCode.PAGE_MOBILE_VERFIYCODE);
					}
				}
			}
			String buyer_contact = request.getParameter("buyer_contact");
			Clazz.Annotation(GwOrders.class, "buyer_contact", buyer_contact);

			if (!Validator.isNull(buyer_contact)) {
				orderService.tx_updateOrderBuyerContact(morder.getId(),
						buyer_contact);
			}
			/**
			 * =====================================���ѿ������ѷ��ʡ�ʵ��֧�������ʾ==========
			 * ===========================
			 **/
			if ("8".equals(map.get("_paytype"))) {
				GwMerChannelRate gmcr = slaService
						.findMerChannelRateByAcquireIndexc(channelcode);
				if (gmcr != null) {
					Double feeAmount = null; // �����ѽ��
					Double actualAmount = null; // ʵ��֧�����
					map.put("feeType", gmcr.getFeeType());
					map.put("feeRate", gmcr.getFeeRate());
					map.put("isRefundFee", gmcr.getIsRefundFee());
					// ������������
					if ("0".equals(gmcr.getFeeType())) {
						feeAmount = gmcr.getFeeRate();
						BigDecimal fee = new BigDecimal(feeAmount == null ? 0
								: feeAmount);
						feeAmount = fee.setScale(2, BigDecimal.ROUND_HALF_UP)
								.doubleValue();
						actualAmount = Double.valueOf(morder.getAmount()
								- morder.getDirectpayamt())
								/ 100 + feeAmount;
						BigDecimal actual = new BigDecimal(
								actualAmount == null ? 0 : actualAmount);
						actualAmount = actual.setScale(2,
								BigDecimal.ROUND_HALF_UP).doubleValue();
					}
					// ��������������
					else if ("1".equals(gmcr.getFeeType())) {
						feeAmount = (morder.getAmount() - morder
								.getDirectpayamt())
								* (gmcr.getFeeRate() / 100) / 100;
						BigDecimal fee = new BigDecimal(feeAmount == null ? 0
								: feeAmount);
						feeAmount = fee.setScale(2, BigDecimal.ROUND_HALF_UP)
								.doubleValue();
						feeAmount = feeAmount == 0 ? 0.01 : feeAmount;
						actualAmount = (morder.getAmount()
								- morder.getDirectpayamt() + feeAmount * 100) / 100;
						BigDecimal actual = new BigDecimal(
								actualAmount == null ? 0 : actualAmount);
						actualAmount = actual.setScale(2,
								BigDecimal.ROUND_HALF_UP).doubleValue();
					}
					feeAmount = feeAmount == null ? 0 : feeAmount;
					actualAmount = actualAmount == null ? 0 : actualAmount;
					map.put("feeAmount", feeAmount);
					map.put("actualAmount", actualAmount);
					map.put("feeSign", Md5Encrypt.md5(feeAmount
							+ this.feeSignKey, "UTF-8"));
				}
			}
			/**
			 * =====================================���ѿ������ѷ��ʡ�ʵ��֧�������ʾ==========
			 * ===========================
			 **/
			String bankName = "";
			String bankContact = "";
			String bankUrl = "";
			if ((("1".equals(gc.getChannel_type())) || ("2".equals(gc.getChannel_type()))) && 
			        (showBankInfo(defaultbank) != null) && (showBankInfo(defaultbank).split(",").length >= 3)) {
			        bankName = new String(showBankInfo(defaultbank).split(",")[0].getBytes("iso-8859-1"), "GBK");
			        bankContact = showBankInfo(defaultbank).split(",")[1];
			        bankUrl = showBankInfo(defaultbank).split(",")[2];
			      }
			map.put(WebConstants.MAP_KEY_CHANNEL, gc);
			map.put(WebConstants.MAP_KEY_CHANNELTOKEN, CryptUtil
					.randomcrypt(new int[] { channelid }));
			map.put("acquireCode", gc.getAcquire_indexc());
			map.put("terminal", gc.getTerminal());
			map.put("bankName", bankName);
			map.put("bankContact", bankContact);
			map.put("bankUrl", bankUrl);
			map.put("bankImg", defaultbank);

			if (!String.valueOf(map.get("_bankname")).equalsIgnoreCase(
					gc.getAcquire_indexc())) {
				if (!gc.getAcquire_indexc().toUpperCase().startsWith("UNP"))
					map.put("_bankname", gc.getAcquire_indexc().toUpperCase());
				else
					map.put("_bankname", gc.getAcquire_indexc().substring(4,
							gc.getAcquire_indexc().length()).toUpperCase());
			}
			return new ModelAndView(Constants.APP_VERSION
					+ "/options_confirm_new", "m", RequestUtil
					.HtmlEscapeMap(map));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (!(e instanceof ServiceException))
				e.printStackTrace();
			return new ModelAndView(WebConstants.ERROR_PAGE,
					WebConstants.ERROR_MODEL, new WebError(e));
		}
	}

	private String showBankInfo(String bankCode) {
		Properties defaultProps = new Properties();
		InputStream in = null;
		try {
			in = new ClassPathResource("conf/bankInfo.properties")
					.getInputStream();
			defaultProps.load(in);
			return defaultProps.getProperty(bankCode);
		} catch (Exception e) {
			System.err.println("Error: could not find the config of bank");
			e.printStackTrace();
			return "";
		}
	}

	public void setSlaService(SLAService slaService) {
		this.slaService = slaService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

}
