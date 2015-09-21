package ebank.core.bank.third;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.Crypt;
import ebank.core.common.util.DES;
import ebank.core.common.util.MD5;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.web.common.util.XmlMsg;

public class FastPay extends BankExt implements BankService {

	static Logger logger = Logger.getLogger(ChinaPnr.class);

	private String jkspath;

	public String getJkspath() {
		return jkspath;
	}

	public void setJkspath(String jkspath) {
		this.jkspath = jkspath;
	}

	@Override
	public PayResult getPayResult(HashMap reqs) throws ServiceException {

		String temResp = reqs.get("resp").toString();
		String dataDES = null;
		PayResult bean = null;
		// 快捷支付数据先base64解码
		try {
			temResp = new String(Base64.decode(temResp));
			Map<String, String> respParams = ebank.web.common.util.XmlMsg
					.parse(temResp);
			// System.out.println("版本号|商户号|终端号|交易数据|数据签名"
			// +respParams.get("chinabank.version")+"|"+respParams.get("chinabank.merchant")+"|"+respParams.get("chinabank.terminal")+"|"
			// +respParams.get("chinabank.data")+"|"+respParams.get("chinabank.sign"));
			// //验证数据签名的合法性。版本号+商户号+终端号+交易数据使用md5加密和数据签名比较，md5密钥在网银在线商户后台设置
			if (MD5.verify(respParams.get("chinabank.version")
					+ respParams.get("chinabank.merchant")
					+ respParams.get("chinabank.terminal")
					+ respParams.get("chinabank.data"), this.getMd5(), respParams
					.get("chinabank.sign"))) {

				// 使用DES解密data交易数据,des密钥网银在线商户后台设置
				dataDES = DES.decrypt(respParams.get("chinabank.data"), getDes(),
						respParams.get("xml.charset"));
				Map<String, String> dataParams = XmlMsg.parse(dataDES);

				if ("0".equals(dataParams.get("data.trade.status"))
						&& "0000".equals(dataParams.get("data.return.code"))) {
					String transId = dataParams.get("data.trade.id");
					bean = new PayResult(transId, this.bankcode, new BigDecimal(
							Amount.getFormatAmount(dataParams.get("data.trade.amount"), -2)), 1);
					bean.setBanktransseq(transId);
				}else{
					String transId = dataParams.get("data.trade.id");
					bean = new PayResult(transId, this.bankcode, new BigDecimal(
							Amount.getFormatAmount(dataParams.get("data.trade.amount"), -2)), -1);
					bean.setBanktransseq(transId);
				}
				StringBuffer respBuffer=new StringBuffer("");
				
				
				respBuffer.append("<tradeType>").append(
						dataParams.get("data.trade.type")).append(
						"</tradeType>");
				respBuffer.append("<transId>").append(
						dataParams.get("data.trade.id")).append("</transId>");
				respBuffer.append("<totalFee>").append(
						Amount.getFormatAmount(dataParams.get("data.trade.amount"), -2)).append(
						"</totalFee>");
				respBuffer.append("<currency>").append(
						dataParams.get("data.trade.currency")).append(
						"</currency>");
				if ("S".equals(dataParams.get("data.trade.type"))) {// 消费专用参数
					respBuffer.append("<date>").append(
							dataParams.get("data.trade.date"))
							.append("</date>");
					respBuffer.append("<time>").append(
							dataParams.get("data.trade.time"))
							.append("</time>");
					respBuffer.append("<note>").append(
							dataParams.get("data.trade.note") == null ? ""
									: dataParams.get("data.trade.note"))
							.append("</note>");
				}

				respBuffer.append("<code>").append(
						dataParams.get("data.return.code")).append("</code>");
				// 支付结果描述暂时屏蔽
				// respBuffer.append("<desc>").append(
				// dataParams.get("data.return.desc")).append("</desc>");
				
				bean.setAuthcode(respBuffer.toString());//fastpay 
			}else{
				throw new ServiceException(EventCode.SIGN_VERIFY);
			}
		} catch (Base64DecodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;

	}

	public String sendOrderToBank(BankOrder order) throws ServiceException {

		JSONObject formData = JSONObject.fromObject(Crypt.getInstance()
				.decrypt(order.getMp().get("outJson").toString()));
		String tradeType = formData.getString("transType");
		String charset = formData.getString("inputcharset");
		// String reqUrl=formData.getString("reqUrl");
		StringBuffer data = new StringBuffer("");
		Map extraParams = order.getMp();

		if ("V".equals(tradeType)) {// 签约

			data.append("<?xml version=\"1.0\" encoding=\"").append(charset)
					.append("\"?>");
			data.append("<DATA>");
			data.append("<CARD>");
			data.append("<BANK>").append(formData.getString("bankCode"))
					.append("</BANK>");
			data.append("<TYPE>").append(formData.getString("type")).append(
					"</TYPE>");
			data.append("<NO>").append(formData.getString("no"))
					.append("</NO>");
			data.append("<EXP>").append(formData.getString("exp")).append(
					"</EXP>");
			data.append("<CVV2>").append(formData.getString("cvv2")).append(
					"</CVV2>");
			data.append("<NAME>").append(formData.getString("name")).append(
					"</NAME>");
			data.append("<IDTYPE>").append(formData.getString("idType"))
					.append("</IDTYPE>");
			data.append("<IDNO>").append(formData.getString("idNo")).append(
					"</IDNO>");
			data.append("<PHONE>").append(formData.getString("phone")).append(
					"</PHONE>");
			data.append("</CARD>");
			data.append("<TRADE>");
			data.append("<TYPE>").append(formData.getString("transType"))
					.append("</TYPE>");
			data.append("<ID>").append(formData.getString("transId")).append(
					"</ID>");
			data.append("<AMOUNT>").append(Amount.getIntAmount(formData.getString("total_fee"),2))
					.append("</AMOUNT>");
			data.append("<CURRENCY>").append(formData.getString("currency"))
					.append("</CURRENCY>");
			data.append("</TRADE>");
			data.append("</DATA>");

			String dataDES = null;
			String signMD5 = null;
			try {
				dataDES = DES.encrypt(data.toString(), this.getDes(), charset);
				// 计算数据签名version+merchant+terminal+data元素，MD5密钥是在商户在网银在线后台设置，签名是为了验证请求的合法性
				signMD5 = MD5.md5(this.getVersion() + getCorpid()
						+ getTerminal() + dataDES, getMd5());
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("签名失败!");
			}
			data = new StringBuffer("");

			data.append("<?xml version=\"1.0\" encoding=\"").append(charset)
					.append("\"?>");
			data.append("<CHINABANK>");
			data.append("<VERSION>").append(this.getVersion()).append(
					"</VERSION>");
			data.append("<MERCHANT>").append(this.getCorpid()).append(
					"</MERCHANT>");
			data.append("<TERMINAL>").append(this.getTerminal()).append(
					"</TERMINAL>");
			data.append("<DATA>").append(dataDES);
			data.append("</DATA>");
			data.append("<SIGN>").append(signMD5);
			data.append("</SIGN>");
			data.append("</CHINABANK>");

			// String
			// egStr="<?xml version=\"1.0\" encoding=\"UTF-8\"?><CHINABANK><VERSION>1.0.0</VERSION><MERCHANT>22907856</MERCHANT><TERMINAL>00000001</TERMINAL><DATA>U6uuI3PKfhPvh0nlVl1waLXIXf1L+bWTcxb9g6QCVMuENXNHZzepm9jwdvuz8yNjcUVdR5qTpGrZVy9jv02KG3nSgtBcKqQGpVzMQhGhOBUP0fkR8p5zvJ7Hmbq8QhqXRGnmtJcl1n1xJ4Ho1gklRiLEPxqjn/YouwHGIJyBmK1Gywkzw4KRGYeGPFGJp39VlDimD0aOomAIbq1pG4b1tDk/JX6gv7EHqOCs/JNJFWNTa2fd11oZwtoRJw/WCjKIZSjxF+4/ARS7USQeobKH4FSAXOFm41FIqk/xL1GIFkSUOKYPRo6iYLX02sNLRfBHN5DlPpmpaWOR10iJY6NgOK24XbUtU2NKH/j7bG38KiCS9XOGfti7MolvBvCw6q/xpVcfWeDA9IG9+ygQib0cto0kUDLJOiDwSffd4GGJdU7kNLKkh0lIMZVc3cEvQaC/2K2D9w9VCG9F6MnY/heO31azW4PIcS/3XuUByT03tKUxeorPUlywt1azW4PIcS/3z8O+Yl/1UhCZPt67EUjK+20mYK2r3YTz2VcvY79NihsjE6HOpmIWWHAAqtOMIVeuNw4t8QlGeOBX2mtyapCkmQcqEWf5dC3P2vCS2JcErtyWxTb8o6SwdZVc3cEvQaC/F7qSycSeyvuMUclU+gCkPg==</DATA><SIGN>d5bdd7768aa32e9f56b8bbbaa54bc228</SIGN></CHINABANK>";
			String reqParams = Base64.encode(data.toString().getBytes());
			// String reqParams = Base64.encode(egStr.getBytes());
			return process(charset, reqParams, getDesturl());

		} else if ("S".equals(tradeType)) {// 支付

			String orderInfoUrl = extraParams.get("orderInfoUrl").toString();// redirect
			// after
			// success

			data.append("<?xml version=\"1.0\" encoding=\"").append(charset)
					.append("\"?>");
			data.append("<DATA>");
			data.append("<CARD>");
			data.append("<BANK>").append(formData.getString("bankCode"))
					.append("</BANK>");
			data.append("<TYPE>").append(formData.getString("type")).append(
					"</TYPE>");
			data.append("<NO>").append(formData.getString("no"))
					.append("</NO>");
			data.append("<EXP>").append(formData.containsKey("exp")?formData.getString("exp"):"").append(
					"</EXP>");
			data.append("<CVV2>").append(formData.containsKey("cvv2")?formData.getString("cvv2"):"").append(
					"</CVV2>");
			data.append("<NAME>").append(
					URLDecoder.decode(formData.getString("name"))).append(
					"</NAME>");
			data.append("<IDTYPE>").append(formData.getString("idType"))
					.append("</IDTYPE>");
			data.append("<IDNO>").append(formData.getString("idNo")).append(
					"</IDNO>");
			data.append("<PHONE>").append(formData.getString("phone")).append(
					"</PHONE>");
			data.append("</CARD>");
			data.append("<TRADE>");
			data.append("<TYPE>").append(formData.getString("transType"))
					.append("</TYPE>");
			data.append("<ID>").append(formData.getString("transId")).append(
					"</ID>");
			data.append("<AMOUNT>").append(Amount.getIntAmount(formData.getString("total_fee"),2))
					.append("</AMOUNT>");
			data.append("<CURRENCY>").append(formData.getString("currency"))
					.append("</CURRENCY>");
			data.append("<DATE>").append(formData.getString("date")).append(
					"</DATE>");
			data.append("<TIME>").append(formData.getString("time")).append(
					"</TIME>");
			
			data.append("<NOTICE>").append(
					this.getRecurl())// 异步接口地址
					.append("</NOTICE>");
			
			
			data.append("<NOTE>").append(formData.getString("note")).append(
					"</NOTE>");
			data.append("<CODE>").append(formData.getString("code")).append(
					"</CODE>");
			data.append("</TRADE>");
			data.append("</DATA>");
//			System.out.println("originalData==="+data);
			String dataDES = null;
			String signMD5 = null;
			try {
				dataDES = DES.encrypt(data.toString(), this.getDes(), charset);
				// 计算数据签名version+merchant+terminal+data元素，MD5密钥是在商户在网银在线后台设置，签名是为了验证请求的合法性
				signMD5 = MD5.md5(this.getVersion() + getCorpid()
						+ getTerminal() + dataDES, getMd5());
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("签名失败!");
			}
			data = new StringBuffer("");

			data.append("<?xml version=\"1.0\" encoding=\"").append(charset)
					.append("\"?>");
			data.append("<CHINABANK>");
			data.append("<VERSION>").append(this.getVersion()).append(
					"</VERSION>");
			data.append("<MERCHANT>").append(this.getCorpid()).append(
					"</MERCHANT>");
			data.append("<TERMINAL>").append(this.getTerminal()).append(
					"</TERMINAL>");
			data.append("<DATA>").append(dataDES);
			data.append("</DATA>");
			data.append("<SIGN>").append(signMD5);
			data.append("</SIGN>");
			data.append("</CHINABANK>");
			
			String reqParams = Base64.encode(data.toString().getBytes());
			return process(charset, reqParams, getDesturl()) + "$"
					+ orderInfoUrl;

			// 消费
			// params.put("card_bank", Context.getValue("card_bank"));
			// params.put("card_type", Context.getValue("card_type"));
			// params.put("card_no", Context.getValue("card_no"));
			// params.put("card_exp", Context.getValue("card_exp"));
			// params.put("card_cvv2", Context.getValue("card_cvv2"));
			// params.put("card_name", Context.getValue("card_name"));
			// params.put("card_idtype", Context.getValue("card_idtype"));
			// params.put("card_idno", Context.getValue("card_idno"));
			// params.put("card_phone", Context.getValue("card_phone"));
			// params.put("trade_type", tradeType);
			// params.put("trade_id", System.currentTimeMillis());
			// params.put("trade_amount", Context.getValue("trade_amount"));
			// params.put("trade_currency", Context.getValue("trade_currency"));
			// params.put("trade_date", Context.getValue("trade_date"));
			// params.put("trade_time", Context.getValue("trade_time"));
			// params.put("trade_notice", Context.getValue("trade_notice"));
			// params.put("trade_code", Context.getValue("trade_code"));
		} else if ("R".equals(tradeType)) {
			// 退款
			// params.put("trade_type", tradeType);
			// params.put("trade_id", System.currentTimeMillis());
			// params.put("trade_oid", Context.getValue("oid"));
			// params.put("trade_amount", Context.getValue("trade_amount"));
			// params.put("trade_currency", Context.getValue("trade_currency"));
			// params.put("trade_date", Context.getValue("trade_date"));
			// params.put("trade_time", Context.getValue("trade_time"));
			// params.put("trade_notice", Context.getValue("trade_notice"));
			return "";
		} else if ("Q".equals(tradeType)) {
			// 查询
			// params.put("trade_type", tradeType);
			// params.put("trade_id", Context.getValue("trade_id"));
			return "";
		} else {
			return "";
		}

	}

	// private static final String url =
	// "https://quick.chinabank.com.cn/express.htm";
	//	
	public String process(String charset, String req, String reqUrl)
			throws ServiceException {
		String resp = null;
		HttpClient httpClient = null;
		// HttpPost httpPost = null;
		PostMethod post = null;
		InputStream in = null;
		String dataDES = null;
		StringBuffer respBuffer = new StringBuffer("");
		try {
			httpClient = new HttpClient();
			post = new PostMethod(getDesturl());

			if (reqUrl.indexOf("https") != -1) {
				SecureProtocolSocketFactory protoSocketFactory = new ClientAndServerAuthSSLSocketFactory(
						new URL("file:" + jkspath), "chinabank", KeyStore
								.getDefaultType(), new URL("file:" + jkspath),
						"chinabank", KeyStore.getDefaultType());
				Protocol authhttps = new Protocol("https", protoSocketFactory,
						443);
				// httpClient.getHostConfiguration().setHost("quick.chinabank.com.cn",
				// 443, authhttps);
				Protocol.registerProtocol("https", authhttps);
			}

			HttpParams httpParams = httpClient.getParams();
			httpParams.setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT,
					1000 * 60);
			httpParams.setIntParameter(HttpConnectionParams.SO_TIMEOUT,
					1000 * 60);
			post.addParameter(new NameValuePair("charset", charset));
			post.addParameter(new NameValuePair("req", req));

			int response = httpClient.executeMethod(post);

			byte[] responseData = post.getResponseBody();
			resp = new String(responseData, charset);

			if (post.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new Exception(post.getStatusLine().toString() + "|"
						+ resp);
			}

			String temResp = resp.substring(resp.indexOf("=") + 1);

			// 快捷支付数据先base64解码
			temResp = new String(Base64.decode(temResp));
			Map<String, String> respParams = ebank.web.common.util.XmlMsg
					.parse(temResp);
			// System.out.println("版本号|商户号|终端号|交易数据|数据签名"
			// +respParams.get("chinabank.version")+"|"+respParams.get("chinabank.merchant")+"|"+respParams.get("chinabank.terminal")+"|"
			// +respParams.get("chinabank.data")+"|"+respParams.get("chinabank.sign"));
			// //验证数据签名的合法性。版本号+商户号+终端号+交易数据使用md5加密和数据签名比较，md5密钥在网银在线商户后台设置
			if (MD5.verify(respParams.get("chinabank.version")
					+ respParams.get("chinabank.merchant")
					+ respParams.get("chinabank.terminal")
					+ respParams.get("chinabank.data"), getMd5(), respParams
					.get("chinabank.sign"))) {

				// 使用DES解密data交易数据,des密钥网银在线商户后台设置
				dataDES = DES.decrypt(respParams.get("chinabank.data"),
						getDes(), respParams.get("xml.charset"));
				Map<String, String> dataParams = XmlMsg.parse(dataDES);
				respBuffer.append("<data>");
				respBuffer.append("<tradeType>").append(
						dataParams.get("data.trade.type")).append(
						"</tradeType>");
				respBuffer.append("<transId>").append(
						dataParams.get("data.trade.id")).append("</transId>");
				respBuffer.append("<totalFee>").append(
						Amount.getFormatAmount(dataParams.get("data.trade.amount"), -2)).append(
						"</totalFee>");
				respBuffer.append("<currency>").append(
						dataParams.get("data.trade.currency")).append(
						"</currency>");
				if ("S".equals(dataParams.get("data.trade.type"))) {// 消费专用参数
					respBuffer.append("<date>").append(
							dataParams.get("data.trade.date"))
							.append("</date>");
					respBuffer.append("<time>").append(
							dataParams.get("data.trade.time"))
							.append("</time>");
//					respBuffer.append("<note>").append(
//							dataParams.get("data.trade.note") == null ? ""
//									: dataParams.get("data.trade.note"))
//							.append("</note>");
//					respBuffer.append("<status>").append(
//							dataParams.get("data.trade.status")).append(
//							"</status>");
				}

				respBuffer.append("<code>").append(
						dataParams.get("data.return.code")).append("</code>");
				// 支付结果描述暂时屏蔽
				// respBuffer.append("<desc>").append(
				// dataParams.get("data.return.desc")).append("</desc>");
				 respBuffer.append("</data>");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return resp;
			// throw new ServiceException("交易失败!");
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				post.releaseConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return respBuffer.toString();
	}

}

class ClientAndServerAuthSSLSocketFactory implements
		SecureProtocolSocketFactory {

	private URL keystoreUrl = null;

	private String keystorePassword = null;

	private String keystoreType;

	private URL truststoreUrl = null;

	private String truststorePassword = null;

	private String truststoreType;

	private SSLContext sslcontext = null;

	public KeyManager[] keymanagers;

	public TrustManager[] trustmanagers;

	/**
	 * Either a keystore or truststore file must be given. Otherwise SSL context
	 * initialization error will result.
	 * 
	 * @param keystoreUrl
	 *            URL of the keystore file. May be <tt>null</tt> if HTTPS client
	 *            authentication is not to be used.
	 * @param keystorePassword
	 *            Password to unlock the keystore. IMPORTANT: this
	 *            implementation assumes that the same password is used to
	 *            protect the key and the keystore itself.
	 * @param keystoreType
	 *            Keystore type (format) (e.g. : "JKS", "PKCS12")
	 * @param truststoreUrl
	 *            URL of the truststore file. May be <tt>null</tt> if HTTPS
	 *            server authentication is not to be used.
	 * @param truststoreType
	 *            Password to unlock the truststore.
	 * @param keyStoreType
	 *            Truststore type (format) (e.g. : "JKS", "PKCS12")
	 */
	public ClientAndServerAuthSSLSocketFactory(final URL keystoreUrl,
			final String keystorePassword, final String keystoreType,
			final URL truststoreUrl, final String truststorePassword,
			final String truststoreType) {
		super();
		this.keystoreUrl = keystoreUrl;
		this.keystorePassword = keystorePassword;
		this.keystoreType = keystoreType;
		this.truststoreUrl = truststoreUrl;
		this.truststorePassword = truststorePassword;
		this.truststoreType = truststoreType;
	}

	private SSLContext getSSLContext() throws IOException,
			UnsupportedOperationException {
		if (this.sslcontext == null) {
			this.sslcontext = createSSLContext();
		}
		return this.sslcontext;
	}

	private SSLContext createSSLContext() throws IOException,
			UnsupportedOperationException {
		try {
			KeyManager[] keymanagers = null;
			TrustManager[] trustmanagers = null;
			if (this.keystoreUrl != null) {
				KeyStore keystore = createKeyStore(this.keystoreUrl,
						this.keystorePassword, this.keystoreType);
				keymanagers = createKeyManagers(keystore, this.keystorePassword);
				this.keymanagers = keymanagers;
			}
			if (this.truststoreUrl != null) {
				KeyStore keystore = createKeyStore(this.truststoreUrl,
						this.truststorePassword, this.truststoreType);
				trustmanagers = createTrustManagers(keystore);
				this.trustmanagers = trustmanagers;
			}
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(keymanagers, trustmanagers, null);
			return sslcontext;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}

	/**
	 * @see SecureProtocolSocketFactory#createSocket(java.lang.String,int,java.net.InetAddress,int)
	 */
	public Socket createSocket(String host, int port, InetAddress clientHost,
			int clientPort) throws IOException, UnknownHostException,
			UnsupportedOperationException {
		return getSSLContext().getSocketFactory().createSocket(host, port,
				clientHost, clientPort);
	}

	/**
	 * @see SecureProtocolSocketFactory#createSocket(java.lang.String,int)
	 */
	public Socket createSocket(String host, int port) throws IOException,
			UnknownHostException, UnsupportedOperationException {
		return getSSLContext().getSocketFactory().createSocket(host, port);
	}

	/**
	 * @see SecureProtocolSocketFactory#createSocket(java.net.Socket,java.lang.String,int,boolean)
	 */
	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException,
			UnsupportedOperationException {
		return getSSLContext().getSocketFactory().createSocket(socket, host,
				port, autoClose);
	}

	public Socket createSocket(final String host, final int port,
			final InetAddress localAddress, final int localPort,
			final HttpConnectionParams params) throws IOException,
			UnknownHostException, ConnectTimeoutException,
			UnsupportedOperationException {
		// TODO ? use HttpConnectionParams ?
		return getSSLContext().getSocketFactory().createSocket(host, port,
				localAddress, localPort);
	}

	/**
	 * Builds a keystore from a file.
	 * 
	 * @param url
	 *            URL to the keystore file
	 * @param password
	 *            Keystore password
	 * @param keystoreType
	 *            Keystore type (cf <a href=
	 *            "http://docs.oracle.com/javase/1.4.2/docs/guide/security/CryptoSpec.html#AppA"
	 *            >Appendix A in the Java Cryptography Architecture API
	 *            Specification & Reference</a>)
	 */
	public static KeyStore createKeyStore(final URL url, final String password,
			final String keystoreType) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException {
		if (url == null) {
			throw new IllegalArgumentException("Keystore url may not be null");
		}
		KeyStore keystore = KeyStore.getInstance(keystoreType);
		keystore.load(url.openStream(), password != null ? password
				.toCharArray() : null);
		return keystore;
	}

	/**
	 * Builds a list of {@link KeyManager} from the default
	 * {@link KeyManagerFactory}.
	 * 
	 * @param keystore
	 *            The keystore that holds certificats
	 * @param password
	 *            Keystore password
	 * @see KeyManagerFactory#getKeyManagers()
	 * @see KeyManagerFactory#getInstance(String)
	 * @see KeyManagerFactory#getDefaultAlgorithm()
	 */
	public static KeyManager[] createKeyManagers(final KeyStore keystore,
			final String password) throws KeyStoreException,
			NoSuchAlgorithmException, UnrecoverableKeyException {
		if (keystore == null) {
			throw new IllegalArgumentException("Keystore may not be null");
		}
		KeyManagerFactory kmfactory = KeyManagerFactory
				.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmfactory.init(keystore, password != null ? password.toCharArray()
				: null);
		return kmfactory.getKeyManagers();
	}

	/**
	 * Builds a list of {@link TrustManager} from the default
	 * {@link TrustManagerFactory}.
	 * 
	 * @param keystore
	 *            Le keystore contenant les certificats
	 * @see TrustManagerFactory#getInstance(String)
	 * @see TrustManagerFactory#getTrustManagers()
	 */
	public static TrustManager[] createTrustManagers(final KeyStore keystore)
			throws KeyStoreException, NoSuchAlgorithmException {
		if (keystore == null) {
			throw new IllegalArgumentException("Keystore may not be null");
		}
		TrustManagerFactory tmfactory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmfactory.init(keystore);
		TrustManager[] trustmanagers = tmfactory.getTrustManagers();
		for (int i = 0; i < trustmanagers.length; i++) {
			if (trustmanagers[i] instanceof X509TrustManager) {
				trustmanagers[i] = (X509TrustManager) trustmanagers[i];
			}
		}
		return trustmanagers;
	}
	
	
}
