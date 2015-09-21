package ebank.web.controller;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import beartool.Md5Encrypt;
import ebank.core.NoticeService;
import ebank.core.UserService;
import ebank.core.bank.BankService;
import ebank.core.bank.BankServiceFactory;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Clazz;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.BankOrder;
import ebank.core.model.domain.CmCustomerChannel;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwViewUser;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.util.LocaleUtil;
import ebank.web.common.util.RequestUtil;

public class QuickPayController implements Controller {
	private Log log = LogFactory.getLog(this.getClass());
	private UserService userService;
	private static Map<String, Object> mp = new HashMap<String, Object>();
	private BankServiceFactory bsf;
	private String localDomain;
	private NoticeService notifyService;
	
	public void setNotifyService(NoticeService notifyService) {
		this.notifyService = notifyService;
	}

	public String getLocalDomain() {
		return localDomain;
	}

	public void setLocalDomain(String localDomain) {
		this.localDomain = localDomain;
	}

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse rep) throws Exception {
		try {
//			req.setCharacterEncoding("GBK");
			String reqMsg=req.getParameter("req");
			
			Document docData=DocumentHelper.parseText(reqMsg);
			
			Element rootElement=docData.getRootElement();
			
			log.debug("request from url pay:" + RequestUtil.getIpAddr(req));
			String partner = rootElement.elementText("merchantID");
			mp.put("merchant_ID", partner);

			Clazz.Annotation(GwOrders.class, "partnerid", partner);

			GwViewUser user = userService.getViewUser(partner, "online");
			if (user == null || user.getMstate() != 1) {
				throw new ServiceException(EventCode.MERCHANT_STATEILL);
			}
			if (!"normal".equals(user.getStatus())) {
				throw new ServiceException(EventCode.MERCHANT_STATEILL);
			}

			String partnerkey = user.getMd5Key();
			
			String seller_email = rootElement.elementText("sellerEmail");
			Clazz.Annotation(GwOrders.class, "seller_name", seller_email);
			mp.put("seller_email", seller_email);

			String out_trade_no = rootElement.elementText("transId");
			if (out_trade_no == null) {
				out_trade_no = partner
						+ "-"
						+ new SimpleDateFormat("MMddHHmmssSSS")
								.format(new Date()) + "-"
						+ new Random().nextInt(10);
			}
			mp.put("order_no", out_trade_no);

			String subject = "qucikpay";// 默认数据

			mp.put("title", subject);

			String amount = rootElement.elementText("totalFee");
			Clazz.Annotation(GwOrders.class, "amount", amount);

			mp.put("total_fee", amount);

			if (partner == null) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY,
						new String[] { "partner" });
			}
			// if(seller_email==null){
			// throw new ServiceException(EventCode.WEB_PARAMEMPTY,new
			// String[]{"seller_email"});
			// }

//			String return_url = RequestUtil.HtmlEscape(req
//					.getParameter("return_url"));
			String reqUrl = rootElement.elementText("notifyUrl");//异步通知接口
			mp.put("return_url", reqUrl);
			
			

			String orderInfoUrl = reqUrl;//支出成功通知界面
			mp.put("orderInfoUrl", orderInfoUrl);

//			String notify_url = RequestUtil.HtmlEscape(req
//					.getParameter("reqUrl"));
			mp.put("notify_url", reqUrl);//通知接口

			String show_url = RequestUtil.HtmlEscape(req
					.getParameter("show_url"));
			mp.put("show_url", show_url);

			String body = "quickpay";// default value

			mp.put("body", body);

			String bankCode = rootElement.elementText("bankCode");

			if (bankCode == null) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY,
						new String[] { "bankCode" });
			}

			mp.put("bankCode", bankCode);

			String type = rootElement.elementText("type");

			if (type == null) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY,
						new String[] { "type" });
			}
			mp.put("type", type);

			String no = rootElement.elementText("no");

			if (no == null) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY,
						new String[] { "no" });
			}
			mp.put("no", no);

			String exp = rootElement.elementText("exp");
			mp.put("exp", exp);
			String cvv2 = rootElement.elementText("cvv2");
			mp.put("cvv2", cvv2);

			String name = rootElement.elementText("name");

			if (name == null) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY,
						new String[] { "name" });
			}
			mp.put("name", name);

			String idType = rootElement.elementText("idType");

			if (idType == null) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY,
						new String[] { "idType" });
			}
			mp.put("idType", idType);

			String idNo = rootElement.elementText("idNo");

			if (idNo == null) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY,
						new String[] { "idNo" });
			}
			mp.put("idNo", idNo);

			String phone = rootElement.elementText("phone");

			if (phone == null) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY,
						new String[] { "phone" });
			}
			mp.put("phone", phone);

			String transType = rootElement.elementText("transType");

			if (transType == null) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY,
						new String[] { "transType" });
			}
			mp.put("transType", transType);

			String transId = rootElement.elementText("transId");

			if (transId == null) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY,
						new String[] { "transId" });
			}
			mp.put("transId", transId);

			String currency = rootElement.elementText("currency");

			if (currency == null) {
				throw new ServiceException(EventCode.WEB_PARAMEMPTY,
						new String[] { "currency" });
			}
			mp.put("currency", currency);

			String logistics_fee = req.getParameter("logistics_fee");
			mp.put("logistics_fee", logistics_fee);

			String logistics_type = req.getParameter("logistics_type");
			mp.put("logistics_type", logistics_type);

			String logistics_payment = req.getParameter("logistics_payment");
			mp.put("logistics_payment", logistics_payment);

			String quantity = req.getParameter("quantity");
			mp.put("show_url", quantity);

			String receive_name = RequestUtil.HtmlEscape(req
					.getParameter("receive_name"));
			mp.put("receive_name", receive_name);

			String receive_address = RequestUtil.HtmlEscape(req
					.getParameter("receive_address"));
			mp.put("receive_address", receive_address);

			String receive_zip = req.getParameter("receive_zip");
			mp.put("receive_zip", receive_zip);

			String receive_phone = req.getParameter("receive_phone");
			mp.put("receive_phone", receive_phone);

			String receive_mobile = req.getParameter("receive_mobile");
			mp.put("receive_mobile", receive_mobile);

			String logistics_fee_1 = req.getParameter("logistics_fee_1");
			mp.put("logistics_fee_1", logistics_fee_1);

			String logistics_type_1 = req.getParameter("logistics_type_1");
			mp.put("logistics_type_1", logistics_type_1);

			String logistics_payment_1 = req
					.getParameter("logistics_payment_1");
			mp.put("logistics_payment_1", logistics_payment_1);

			String logistics_fee_2 = req.getParameter("logistics_fee_2");
			mp.put("logistics_fee_2", logistics_fee_2);

			String logistics_type_2 = req.getParameter("logistics_type_2");
			mp.put("logistics_type_2", logistics_type_2);

			String logistics_payment_2 = req
					.getParameter("logistics_payment_2");
			mp.put("logistics_payment_2", logistics_payment_2);

			String buyer_email = RequestUtil.HtmlEscape(req
					.getParameter("buyer_email"));
			mp.put("buyer_email", buyer_email);

			String discount = RequestUtil.HtmlEscape(req
					.getParameter("discount"));
			mp.put("discount", discount);

			String input_charset = rootElement.elementText("charset");
			if (input_charset == null)
				input_charset = "UTF-8";
			mp.put("charset", input_charset);
			mp.put("inputcharset", input_charset);

//			String reqUrl = rootElement.elementText("reqUrl");
			mp.put("reqUrl", reqUrl);
			
			
			String sign=rootElement.elementText("sign");//验证签名
			
			rootElement.remove(rootElement.element("sign"));
			
			String paramsText=rootElement.asXML().substring(6, rootElement.asXML().length()-7);
			
			if(!sign.equals(Md5Encrypt.md5(paramsText+user.getMd5Key(), input_charset))){//sign校验
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
			}
			
			
			String sign_type = RequestUtil.HtmlEscape(req
					.getParameter("sign_type"));
			if (sign_type == null)
				sign_type = "MD5";
			mp.put("sign_type", sign_type);

			mp.put("date", rootElement.elementText("date"));
			mp.put("time", rootElement.elementText("time"));
			mp.put("note", rootElement.elementText("note"));
			mp.put("code", rootElement.elementText("code"));

			List<CmCustomerChannel> channelList = userService
			.findUserChannelList(user.getUserId() + "");
			boolean isAvailable = false;// 通道是否可用
			String bankId = null;
			String svcCode=null;
			for (CmCustomerChannel channel : channelList) {
		
					isAvailable = false;
		
					if ("1".equals(channel.getChannel_type())&&"D".equals(type)&&"CHINABANK".equals(channel.getBank_code())) {//
					// 若开通借记通道，但商户选择贷记通道
						isAvailable=true;
						svcCode="2907";
						break;
		
					} 
					
					if ("2".equals(channel.getChannel_type())&&"C".equals(type)&&"CHINABANK".equals(channel.getBank_code()))// b2c贷记
					{
						isAvailable=true;
						svcCode="2910";
						break;
		
					} 
		
			}
	
			if(!isAvailable&&svcCode==null){
				String respText="<tradeType>"+transType+"</tradeType><transId>"+transId+"</transId><totalFee>"+amount+"</totalFee><currency>"+currency+"</currency><code>0001</code>";
				String fastPaySign=Md5Encrypt.md5(respText+user.getMd5Key(), input_charset);
				mp.clear();
				respText="<data>"+respText+"<sign>"+fastPaySign+"</sign></data>";
				rep.setContentType("text/xml;charset=UTF-8");
				rep.getWriter().write(respText);//没开通道
				rep.getWriter().close();
				return null;
			}
	
	
			if ("V".equals(transType)) {//签约不用直接走接口
				
				BankService bank =  bsf.getBank(bsf.getBankCode(svcCode));

				HashMap<String,String> params=new HashMap<String,String>();
				params.put("outJson", CryptUtil.encrypt(JSONObject.fromObject(mp).toString()));
				
				BankOrder order=new BankOrder();
				order.setMp(params);
				
				String result=bank.sendOrderToBank(order);
				
				String signStr=result.substring(6, result.trim().length()-7);
				String SMSSign = Md5Encrypt.md5(signStr+user.getMd5Key(), input_charset);
				signStr=("<data>"+signStr+"<sign>"+SMSSign+"</sign></data>");
				
				rep.setContentType("text/xml;charset=UTF-8");
				rep.getWriter().write(signStr);
				rep.getWriter().close();
				return null;

			} else {
				String form = BuildForm(localDomain,partner, seller_email, mp.get("return_url").toString(),
						mp.get("notify_url").toString(), show_url, out_trade_no, subject, body,
						amount, logistics_fee, logistics_type,
						logistics_payment, quantity, receive_name,
						receive_address, receive_zip, receive_phone,
						receive_mobile, logistics_fee_1, logistics_type_1,
						logistics_payment_1, logistics_fee_2, logistics_type_2,
						logistics_payment_2, buyer_email, discount,
						input_charset, partnerkey, sign_type);
//				Map<String, Object> mps = new HashMap<String, Object>();
//				mps.put("form", form);
//				mps.put("bank", "收银台");
//				mps.put("locale", LocaleUtil.getLocale(req
//						.getParameter("locale"), "CN"));
//
//				return new ModelAndView("/common/forbank", "page", mps);
				rep.setContentType("text/xml;charset=UTF-8");
				rep.getWriter().write(form);
				rep.getWriter().close();
				return null;

			}
		} catch (Exception e) {
			e.printStackTrace();
			if (!(e instanceof ServiceException))
				e.printStackTrace();
			return new ModelAndView(WebConstants.ERROR_PAGE,
					WebConstants.ERROR_MODEL, new WebError(e));
		}
	}

	public  String BuildForm(String content,String partner, String seller_email,
			String return_url, String notify_url, String show_url,
			String out_trade_no, String subject, String body, String price,
			String logistics_fee, String logistics_type,
			String logistics_payment, String quantity, String receive_name,
			String receive_address, String receive_zip, String receive_phone,
			String receive_mobile, String logistics_fee_1,
			String logistics_type_1, String logistics_payment_1,
			String logistics_fee_2, String logistics_type_2,
			String logistics_payment_2, String buyer_email, String discount,
			String input_charset, String key, String sign_type) {
		Map<String, String> sPara = new HashMap<String, String>();
		sPara.put("service", "fastpay");
		sPara.put("payment_type", "1");
		sPara.put("merchant_ID", partner);
		sPara.put("seller_email", seller_email);
		sPara.put("return_url", return_url);
		sPara.put("notify_url", notify_url);
		sPara.put("charset", input_charset);
		sPara.put("show_url", show_url);
		sPara.put("order_no", out_trade_no);
		sPara.put("title", subject);
		sPara.put("body", body);
		sPara.put("total_fee", price);
		sPara.put("logistics_fee", logistics_fee);
		sPara.put("logistics_type", logistics_type);
		sPara.put("logistics_payment", logistics_payment);
		// sPara.put("quantity", quantity);
		sPara.put("receive_name", receive_name);
		sPara.put("receive_address", receive_address);
		sPara.put("receive_zip", receive_zip);
		sPara.put("receive_phone", receive_phone);
		sPara.put("receive_mobile", receive_mobile);
		sPara.put("logistics_fee_1", logistics_fee_1);
		sPara.put("logistics_type_1", logistics_type_1);
		sPara.put("logistics_payment_1", logistics_payment_1);
		sPara.put("logistics_fee_2", logistics_fee_2);
		sPara.put("logistics_type_2", logistics_type_2);
		sPara.put("logistics_payment_2", logistics_payment_2);
		sPara.put("buyer_email", buyer_email);
		sPara.put("discount", discount);

		sPara.put("totalFee", mp.get("total_fee").toString());
		sPara.put("bankCode", mp.get("bankCode").toString());
		sPara.put("type", mp.get("type").toString());
		sPara.put("no", mp.get("no").toString());
		sPara.put("exp", mp.get("exp").toString());
		sPara.put("orderInfoUrl", mp.get("orderInfoUrl").toString());
		sPara.put("cvv2", mp.get("cvv2").toString());
		sPara.put("name", URLEncoder.encode(mp.get("name").toString()));
		sPara.put("idType", mp.get("idType").toString());
		sPara.put("idNo", mp.get("idNo").toString());
		sPara.put("phone", mp.get("phone").toString());
		sPara.put("transType", mp.get("transType").toString());
		sPara.put("transId", mp.get("transId").toString());
		sPara.put("currency", mp.get("currency").toString());
		sPara.put("inputcharset", input_charset.toString());
		sPara.put("reqUrl", mp.get("reqUrl").toString());

		sPara.put("date", mp.get("date").toString());
		sPara.put("time", mp.get("time").toString());
		sPara.put("note", mp.get("note").toString());
		sPara.put("code", mp.get("code").toString());

		Map sParaNew = UrlPayController.ParaFilter(sPara); // 除去数组中的空值和签名参数
		String mysign = UrlPayController.BuildMysign(sParaNew, key,
				input_charset);// 生成签名结果

		StringBuffer sbHtml = new StringBuffer();
		List<String> keys = new ArrayList(sParaNew.keySet());

		String gateway = content+"/portal?";
		
		sPara.put("sign", mysign);
		sPara.put("sign_type", sign_type);
		
		
		String result;
		try {
			result = notifyService.tx_responseNotice(input_charset,gateway,getNameValuePair(sPara));
		} catch (ServiceException e) {
			throw new RuntimeException("invoker failure:"+e.getMessage());
		}
		
//		sbHtml.append("<form id=\"easypsubmit\" name=\"easypsubmit\" action=\""
//				+ gateway + "charset=" + input_charset + "\" method=\"post\">");
//
//		for (int i = 0; i < keys.size(); i++) {
//			String name = (String) keys.get(i);
//			String value = (String) sParaNew.get(name);
//			if("name".equals(name)){
//				sbHtml.append("<input type=\"hidden\" name=\"" + name
//						+ "\" value=\"" + URLEncoder.encode(value) + "\"/>");
//			}else{
//			sbHtml.append("<input type=\"hidden\" name=\"" + name
//					+ "\" value=\"" + value + "\"/>");
//			}
//			}
//		sbHtml.append("<input type=\"hidden\" name=\"sign\" value=\"" + mysign
//				+ "\"/>");
//		sbHtml.append("<input type=\"hidden\" name=\"sign_type\" value=\""
//				+ sign_type + "\"/>");
//		sbHtml
//				.append("<script>document.forms['easypsubmit'].submit();</script>");

		return result;
	}

	public  String BuildMysign(Map sArray, String key, String charset) {
		String prestr = CreateLinkString(sArray); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		prestr = prestr + key; // 把拼接后的字符串再与安全校验码直接连接起来
		String mysign = Md5Encrypt.md5(prestr, charset);
		return mysign;
	}
	
	
	public  NameValuePair[] getNameValuePair(Map<String,String> bean){		
		List<NameValuePair> x=new ArrayList<NameValuePair>();
		for (Iterator<String> iterator = bean.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			x.add(new NameValuePair(type,String.valueOf(bean.get(type))));			
		}	
		Object[] y=x.toArray();
		NameValuePair[] n=new NameValuePair[y.length];
		System.arraycopy(y, 0, n, 0, y.length);
		return n;
	}	

	/**
	 * 功能：除去数组中的空值和签名参数
	 * 
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, Object> ParaFilter(Map sArray) {
		List<String> keys = new ArrayList<String>(sArray.keySet());
		Map<String, Object> sArrayNew = new HashMap<String, Object>();

		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) sArray.get(key);

			if ("".equals(value) || value == null
					|| key.equalsIgnoreCase("sign")
					|| key.equalsIgnoreCase("sign_type")) {
				continue;
			}

			sArrayNew.put(key, value);
		}

		return sArrayNew;
	}

	/**
	 * 功能：把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String CreateLinkString(Map params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) params.get(key);

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
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
