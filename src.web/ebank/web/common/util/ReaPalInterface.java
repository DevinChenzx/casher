package ebank.web.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.Clazz;
import ebank.core.common.util.Udate;
import ebank.core.domain.MerchantOrder;
import ebank.core.model.domain.GwLgOptions;
import ebank.core.model.domain.GwLogistic;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwSuborders;
import ebank.core.model.domain.GwViewUser;
import ebank.web.common.WebConstants;

public class ReaPalInterface implements IPartnerInterface {
	private static Log log = LogFactory.getLog(ReaPalInterface.class);

	public MerchantOrder getMerchantOrderByService(HttpServletRequest request)
			throws ServiceException {

		MerchantOrder mo = new MerchantOrder();
		GwOrders order = new GwOrders();

		String service = request.getParameter(Constants.FORMAT_FILED);
		Clazz.Annotation(GwOrders.class, "service", service);
		order.setService(service);

		String sign = request.getParameter("sign");
		Clazz.Annotation(MerchantOrder.class, "sign", sign);
		mo.setSign(sign);

		String sign_type = request.getParameter("sign_type");
		Clazz.Annotation(GwOrders.class, "sign_type", sign_type == null ? ""
				: sign_type.toUpperCase());
		order.setSign_type(sign_type);

		String partner = request.getParameter("merchant_ID");
		Clazz.Annotation(GwOrders.class, "partnerid", partner);
		order.setPartnerid(Long.parseLong(partner));

		String _input_charset = request.getParameter("charset");
		if (Validator.isNull(_input_charset)) {
			_input_charset = "UTF-8";
		}
		Clazz.Annotation(GwOrders.class, "charsets", _input_charset
				.toUpperCase());
		order.setCharsets(_input_charset);

		String out_trade_no = request.getParameter("order_no");
		Clazz.Annotation(GwOrders.class, "ordernum", out_trade_no);
		order.setOrdernum(out_trade_no);

		String paymethod = request.getParameter("paymethod");
		if (!Validator.isNull(paymethod)) {
			Clazz.Annotation(GwOrders.class, "paymethod", paymethod);
			order.setPaymethod(paymethod);

			if(!"bankPay".equals(paymethod)){
				String defaultbank = request.getParameter("defaultbank");
				Clazz.Annotation(GwOrders.class, "preference", defaultbank);
				
				if (defaultbank.endsWith("T0")) {
					order.setPreference(Constants.ACQ_CODE_HEEPAY);// T0走乐富接口
				} else if (defaultbank.equals("UNIONPAY")) {
					order.setPreference(Constants.ACQ_CODE_);// 腾付通接口
				}  else if (defaultbank.equals("MOBILE")) {
			          order.setPreference("LEFU_MOBILE");
		        } else {
					Properties p = null;
					try {
						p=PropertiesLoaderUtils.loadProperties(new ClassPathResource(
								"ChannelFlag.properties"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (p == null || "1".equals(p.getProperty("heepay"))) {//开通汇付通道
						order.setPreference(Constants.ACQ_CODE_HEEPAY);// 汇付接口
					} else if ("1".equals(p.getProperty("online"))) {//开通网银在线通道
						if ("ICBC".equals(defaultbank) || "CCB".equals(defaultbank)
								|| "ABC".equals(defaultbank)
								|| "BOC".equals(defaultbank)
								|| "CMBC".equals(defaultbank)) {//五大行通道
							order.setPreference(Constants.ACQ_CODE_ONLINE_FIVE);// 网银在线接口
						} else {
							order.setPreference(Constants.ACQ_CODE_ONLINE);// 网银在线接口
						}
					}else if("1".equals(p.getProperty("yintong"))){//
						if (("ICBC".equals(defaultbank) || "CCB".equals(defaultbank)
								|| "ABC".equals(defaultbank)
								|| "BOC".equals(defaultbank)
								|| "CMBC".equals(defaultbank))&&"1".equals(p.getProperty("yintongButFiveOnline"))) {//五大行及招行通道走网银在线
							order.setPreference(Constants.ACQ_CODE_ONLINE_FIVE);//
							
						}else if("CMB".equals(defaultbank)){
							
							order.setPreference(Constants.ACQ_CODE_ONLINE);// 网银在线接口
							
						}else{
							order.setPreference(Constants.ACQ_CODE_YINTONG);// 企业走连连银通接口
						}
					}
				}
				// order.setPreference(defaultbank!=null?defaultbank.toUpperCase():WebConstants.DEFAULT_BANK_SELECTED);
////				String defaultbank = request.getParameter("defaultbank");
//				Clazz.Annotation(GwOrders.class, "preference", defaultbank);
			}
			// 判断走网银还是吉卡
			// if(defaultbank.equals("CEB")||defaultbank.equals("ABC")||defaultbank.equals("CCB")||defaultbank.equals("ICBC")||defaultbank.equals("BOC")||defaultbank.equals("BOCM")||defaultbank.equals("CMBC")){
			// order.setPreference(Constants.ACQ_CODE_REA);
			// }

		} else {
			order.setPaymethod("bankPay");
		}

		String return_url = request.getParameter("return_url");
		Clazz.Annotation(GwOrders.class, "return_url", return_url);
		order.setReturn_url(return_url);

		String payment_type = request.getParameter("payment_type");
		Clazz.Annotation(GwOrders.class, "order_type", payment_type);
		order.setOrder_type(payment_type);

		String subject = request.getParameter("title");
		Clazz.Annotation(GwOrders.class, "subject", subject);
		order.setSubject(subject);

		String body = request.getParameter("body");
		Clazz.Annotation(GwOrders.class, "bodys", body);
		order.setBodys(body);

		if (!Validator.isNull(request.getParameter("logistics_type"))) {
			GwLgOptions gw = new GwLgOptions();
			String logistics_type = request.getParameter("logistics_type");
			Clazz.Annotation(GwLgOptions.class, "logistics_type",
					logistics_type);
			gw.setLogistics_type(logistics_type);

			String logistics_fee = request.getParameter("logistics_fee");
			Clazz.Annotation(GwLgOptions.class, "logistics_fee", logistics_fee);
			gw.setLogistics_fee(logistics_fee);

			String logistics_payment = request
					.getParameter("logistics_payment");
			Clazz.Annotation(GwLgOptions.class, "logistics_payment",
					logistics_payment);
			gw.setLogistics_payment(logistics_payment);

			mo.getLgoptionList().add(gw);
		}
		if (!Validator.isNull(request.getParameter("receive_name"))) {
			GwLogistic gl = new GwLogistic();
			String receive_address = request.getParameter("receive_address");
			Clazz.Annotation(GwLogistic.class, "recaddr", receive_address);
			gl.setRecaddr(receive_address);

			String receive_name = request.getParameter("receive_name");
			Clazz.Annotation(GwLogistic.class, "recname", receive_name);
			gl.setRecname(receive_name);

			String receive_zip = request.getParameter("receive_zip");
			Clazz.Annotation(GwLogistic.class, "recpost", receive_zip);
			gl.setRecpost(receive_zip);

			String receive_phone = request.getParameter("receive_phone");
			Clazz.Annotation(GwLogistic.class, "recphone", receive_phone);
			gl.setRecphone(receive_phone);

			String receive_mobile = request.getParameter("receive_mobile");
			Clazz.Annotation(GwLogistic.class, "recmphone", receive_mobile);
			gl.setRecmphone(receive_mobile);

			mo.setLogistic(gl);
		}
		String seller_email = request.getParameter("seller_email");
		String seller_id = request.getParameter("seller_id");
		String buyer_email = request.getParameter("buyer_email");
		String buyer_id = request.getParameter("buyer_id");
		if (!Validator.isNull(seller_email)) {
			Clazz.Annotation(GwOrders.class, "seller_name", seller_email);
			order.setSeller_name(seller_email);
		}
		if (!Validator.isNull(seller_id)) {
			Clazz.Annotation(GwOrders.class, "seller_id", seller_id);
			order.setSeller_id(seller_id);
		}
		if (!Validator.isNull(buyer_email)) {
			Clazz.Annotation(GwOrders.class, "buyer_name", buyer_email);
			order.setBuyer_name(buyer_email);
		}
		if (!Validator.isNull(buyer_id)) {
			Clazz.Annotation(GwOrders.class, "buyer_id", buyer_id);
			order.setBuyer_id(buyer_id);
		}
		String total_fee = request.getParameter("total_fee");

		if (!Validator.isNull(total_fee)
				&& !Validator.isNull(request.getParameter("price"))) {
			throw new ServiceException(EventCode.WEB_PARAM_CONFLICT);
		}
		if (!Validator.isNull(total_fee)) {
			Clazz.Annotation(GwOrders.class, "amount", total_fee);
			order.setAmount(Amount.getIntAmount(total_fee, 2));
			order.setPrice(order.getAmount()); // 相等
			order.setQuantity(1);
		} else if (!Validator.isNull(request.getParameter("price"))) {
			String price = request.getParameter("price");
			Clazz.Annotation(GwOrders.class, "price", price);
			order.setPrice(Amount.getIntAmount(price, 2));

			String quantity = request.getParameter("quantity");
			Clazz.Annotation(GwOrders.class, "quantity", quantity);
			order.setQuantity(Integer.parseInt(quantity));

			order.setAmount(order.getPrice() * order.getQuantity());
		} else {// ext the amount param
			String amount = request.getParameter("amount");
			Clazz.Annotation(GwOrders.class, "amount", amount);
			order.setAmount(Amount.getIntAmount(amount, 2));
			order.setPrice(order.getAmount()); // 相等
			order.setQuantity(1);

		}
		// 付款客户号
		String pay_cus_no = request.getParameter("pay_cus_no");
		order.setPay_cus_no(pay_cus_no);

		String royalty_type = request.getParameter("royalty_type");
		String royalty_parameters = request.getParameter("royalty_parameters");

		if (!Validator.isNull(royalty_type)) {

			Clazz.Annotation(GwOrders.class, "royalty_type", royalty_type);
			order.setRoyalty_type(royalty_type);
			if (!Validator.isNull(royalty_parameters)) {
				Clazz.Annotation(GwOrders.class, "royalty_parameters",
						royalty_parameters);
				String[] seg = royalty_parameters.split("\\|");
				long checkamount = 0;

				HashSet<String> payees = new HashSet<String>();
				GwSuborders suborder = null;
				GwViewUser viewUser = null;
				for (int i = 0; i < seg.length; i++) {
					String[] items = seg[i].split("\\^");
					if (items.length > 0) {
						suborder = new GwSuborders();
						// 判断当前的业务类型为分润(Royalty_type=10)，还是合单支付(Royalty_type=12)
						// ----------------------------------------------------------------------分润start----------------------------------------
						if ("10".equals(royalty_type)) {
							if (items.length == 4) {
								if (!items[0].matches(Constants.REG_EMAIL)) {
									throw new ServiceException(
											EventCode.WEB_PARAMFORMAT,
											new String[] { items[0] });
								}
								if (!items[1].matches(Constants.REG_EMAIL)) {
									throw new ServiceException(
											EventCode.WEB_PARAMFORMAT,
											new String[] { items[1] });
								}
								if (!payees.contains(items[1]))
									payees.add(items[1]);
								else
									throw new ServiceException("530019",
											new String[] { items[1] });
								if (!items[2].matches(Constants.REG_MONEY)) {
									throw new ServiceException(
											EventCode.WEB_PARAMFORMAT,
											new String[] { items[2] });
								}
								if (Amount.getIntAmount(items[2], 2) < 0) {
									throw new ServiceException("501114");
								}
								checkamount += Amount.getIntAmount(items[2], 2);
							}
							if (items.length == 3 || items.length == 2) {
								if (!items[0].matches(Constants.REG_EMAIL)) {
									throw new ServiceException(
											EventCode.WEB_PARAMFORMAT,
											new String[] { items[0] });
								}
								if (items.length == 3) {
									if (items[1].matches(Constants.REG_EMAIL)) {
										if (!payees.contains(items[1]))
											payees.add(items[1]);
										else
											throw new ServiceException(
													"530019",
													new String[] { items[1] });
										if (!seg[i].endsWith("^")) {// mapi
																	// bug处理
											throw new ServiceException(
													EventCode.WEB_PARAMFORMAT,
													new String[] { "缺少^" });
										}
										if (!items[2]
												.matches(Constants.REG_MONEY))
											throw new ServiceException(
													EventCode.WEB_PARAMFORMAT,
													new String[] { items[2] });
										else if (Amount.getIntAmount(items[2],
												2) < 0) {
											throw new ServiceException("501114");
										}
										checkamount += Amount.getIntAmount(
												items[2], 2);
									} else if (!items[1]
											.matches(Constants.REG_MONEY)) {
										throw new ServiceException(
												EventCode.WEB_PARAMFORMAT,
												new String[] { items[1] });
									} else {
										checkamount += Amount.getIntAmount(
												items[1], 2);
										if (items[1]
												.matches(Constants.REG_MONEY)
												&& Amount.getIntAmount(
														items[1], 2) < 0) {
											throw new ServiceException("501114");
										}
									}

								}
								if (items.length == 2) {
									if (!items[1].matches(Constants.REG_MONEY)) {
										throw new ServiceException(
												EventCode.WEB_PARAMFORMAT,
												new String[] { items[1] });
									}
									checkamount += Amount.getIntAmount(
											items[1], 2);
									if (Amount.getIntAmount(items[1], 2) < 0) {
										throw new ServiceException("501114");
									}
									if (!payees.contains(items[0]))
										payees.add(items[0]);
									else
										throw new ServiceException("530019",
												new String[] { items[0] });

								}

							}
							// ----------------------------------------------------------------------分润end----------------------------------------
							// ----------------------------------------------------------------------合单支付start----------------------------------------
						} else if ("12".equals(royalty_type)) {
							// 先判断收款账户是否超过十个，如果超过，提醒客户
							if (seg.length > 10) {
								throw new ServiceException("530022",
										new String[] { "10" });
							}
							if (items.length >= 3) {
								if (items[0] == null || "".equals(items[0])) {
									throw new ServiceException(
											EventCode.MASTERCARD_ORDERNULL);
								}
								if (items[0].length() > 32) {
									throw new ServiceException(
											EventCode.WEB_PARAM_LENGTH,
											new String[] { items[0],
													",订单号长度不能大于32位" });
								}
								if (!items[0].matches(Constants.REG_ORDERID)) {
									throw new ServiceException("530021");
								}
								if (!items[1].matches(Constants.REG_EMAIL)) {
									throw new ServiceException(
											EventCode.WEB_PARAMFORMAT,
											new String[] { items[1] });
								}
								if (!payees.contains(items[1]))// 判断收款账号是否重复
									payees.add(items[1]);
								else
									throw new ServiceException("530020",
											new String[] { items[1] });
								if (!items[2].matches(Constants.REG_MONEY)) {
									throw new ServiceException(
											EventCode.WEB_PARAMFORMAT,
											new String[] { items[2] });
								}
								if (Amount.getIntAmount(items[2], 2) <= 0) {
									throw new ServiceException("501114");
								}
								checkamount += Amount.getIntAmount(items[2], 2);
							}
							suborder.setOuttradeno(items[0]);
							suborder.setSeller_code(items[1]);
							suborder.setSeller_ext(items[3]);
							suborder
									.setAmount(Amount.getIntAmount(items[2], 2));
							suborder.setCreatedate(new Date());

							// 赋值
							mo.getSubOrdersList().add(suborder);
						}
						// ----------------------------------------------------------------------合单支付end----------------------------------------

					}
				}
				if (checkamount != order.getAmount()) {
					throw new ServiceException("530013");
				}

			}
			order.setRoyalty_parameters(royalty_parameters);

		} else {
			if ("distribute_royalty".equals(request.getParameter("service"))) { // 分润接口扩展
				throw new ServiceException(EventCode.ROYATY_PARAME_MISSING);
			}
		}

		if (!Validator.isNull(request.getParameter("it_b_pay"))) { // 转换成
			String exp_dates = request.getParameter("it_b_pay");
			Clazz.Annotation(GwOrders.class, "exp_dates", exp_dates);
			order.setExp_dates(exp_dates);
		} else {
			order.setExp_dates("15d");
		}

		String notifyUrl = request.getParameter("notify_url");
		Clazz.Annotation(GwOrders.class, "notify_url", notifyUrl);
		order.setNotify_url(notifyUrl);

		String buyer_msg = request.getParameter("buyer_msg");
		Clazz.Annotation(GwOrders.class, "buyer_remarks", buyer_msg);
		if (!Validator.isNull(buyer_msg))
			order.setBuyer_remarks(buyer_msg);

		String buyer_realname = request.getParameter("buyer_realname");
		Clazz.Annotation(GwOrders.class, "buyer_realname", buyer_realname);
		if (!Validator.isNull(buyer_realname))
			order.setBuyer_realname(buyer_realname);

		String buyer_contact = request.getParameter("buyer_contact");
		Clazz.Annotation(GwOrders.class, "buyer_contact", buyer_contact);
		if (!Validator.isNull(buyer_contact))
			order.setBuyer_contact(buyer_contact);

		String agent = request.getParameter("agent");
		Clazz.Annotation(GwOrders.class, "agentid", agent);
		if (!Validator.isNull(agent))
			order.setAgentid(agent);

		String service_fee = request.getParameter("service_fee");
		Clazz.Annotation(GwOrders.class, "service_fee", service_fee);
		if (!Validator.isNull(service_fee))
			order.setService_fee(Amount.getIntAmount(service_fee, 2));

		String showUrl = request.getParameter("show_url");
		Clazz.Annotation(GwOrders.class, "show_url", showUrl);
		order.setShow_url(showUrl);

		String ext_param1 = request.getParameter("ext_param1");
		if (!Validator.isNull(ext_param1)) {
			Clazz.Annotation(GwOrders.class, "ext_param1", ext_param1);
			order.setExt_param1(ext_param1);
		}

		String ext_param2 = request.getParameter("ext_param2");
		if (!Validator.isNull(ext_param2)) {
			Clazz.Annotation(GwOrders.class, "ext_param2", ext_param2);
			order.setExt_param2(ext_param2);
		}

		String ext_param3 = request.getParameter("ext_param3");
		if (!Validator.isNull(ext_param3)) {
			Clazz.Annotation(GwOrders.class, "discountdesc", ext_param3);
			order.setDiscountdesc(ext_param3);
		}

		order.setApiversion(Constants.API_REAPAL);
		order.setIps(RequestUtil.getIpAddr(request));

		if (Validator.isNull(order.getDiscount())) {
			order.setDiscount(0);
		}
		if (!Validator.isNull(order.getCurrency())) {
			String code = Validator.currencyStanderize(Validator
					.currencyStanderize(order.getCurrency()));
			if (code == null) {
				throw new ServiceException("unsupport currency.");
			}
			order.setCurrency(code);
		} else {
			order.setCurrency("CNY");

		}
		order.setCurrency(Validator.currencyStanderize(order.getCurrency()));

		String orderdate = request.getParameter("gmt_out_order_create");

		if (Validator.isNull(orderdate)) {
			order.setOrderdate(Udate.format(new Date(), "yyyyMMdd"));
		} else {
			Clazz.Annotation(GwOrders.class, "orderdate", orderdate);
			order.setOrderdate(orderdate);
		}
		if (Validator.isNull(order.getLocale())) {
			order.setLocale(LocaleUtil.getLocale(null, "CN"));
		}
		mo.setOrders(order);
		mo.setSortstr(ReaPalInterface.getFormOrderStr(request));
		return mo;
	}

	/**
	 * 得到form表单非签名项组合串
	 * 
	 * @param request
	 * @return
	 */
	public static String getFormOrderStr(HttpServletRequest request) {
		Map<String, Object> fields = new HashMap<String, Object>();
		Enumeration<String> e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String fieldName = (String) e.nextElement();
			String fieldValue = request.getParameter(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)
					&& !fieldName.startsWith("sign")
					&& !fieldName.matches("^_([a-zA-Z]+)+_$")) {
				fields.put(fieldName, fieldValue);
			}
		}
		List<String> fieldNames = new ArrayList<String>(fields.keySet());
		Collections.sort(fieldNames);
		StringBuffer buf = new StringBuffer();
		Iterator<String> itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = (String) fields.get(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				buf.append(fieldName + "=" + fieldValue + "&");
			}
		}
		if (buf.length() > 1)
			buf.setLength(buf.length() - 1);
		return buf.toString(); // 去掉最后&
	}

	public static String getMapOrderStr(HashMap<String, Object> request) {
		List<String> fieldNames = new ArrayList<String>(request.keySet());
		Collections.sort(fieldNames);
		StringBuffer buf = new StringBuffer();
		Iterator<String> itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = String.valueOf(request.get(fieldName));
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				buf.append(fieldName + "=" + fieldValue + "&");
			}
		}
		if (buf.length() > 1)
			buf.setLength(buf.length() - 1);
		return buf.toString(); // 去掉最后&

	}

}
