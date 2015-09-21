package ebank.core.bank.third;

import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Crypt;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.log4j.Logger;

public class FengFuPay extends BankExt
  implements BankService
{
  static Logger logger = Logger.getLogger(ChinaPnr.class);

  private static String nofityURL = "http://epay.gicard.net/fengPayNotify";

  public PayResult getPayResult(HashMap reqs)
    throws ServiceException
  {
    String trxnum = reqs.get("requestId").toString();
    String totalPrice = reqs.get("totalPrice").toString();
    String payId = reqs.get("payId").toString();

    String result = reqs.get("status").toString();

    String md5ResStr = trxnum + payId + reqs.get("fiscalDate").toString() + reqs.get("description").toString();

    if (reqs.get("resultSignature").toString().equals(hmacSign(md5ResStr, getMd5())))
    {
      PayResult bean = new PayResult(trxnum, this.bankcode, new BigDecimal(
        totalPrice), "2".equals(reqs.get("status").toString()) ? 1 : -1);
      bean.setBanktransseq(payId);
      if ("2".equals(reqs.get("status").toString())) {
        reqs.put("RES", "200");
      }
      return bean;
    }

    return null;
  }

  public String sendOrderToBank(BankOrder order)
    throws ServiceException
  {
    Map extraParams = order.getMp();
    JSONObject formData = JSONObject.fromObject(Crypt.getInstance()
      .decrypt(extraParams.get("outJson").toString()));
    String tradeType = formData.getString("transType");
    String charset = "GBK";

    String bankCardType = extraParams.get("bankCardType").toString();
    if ("1".equals(bankCardType)) {
      setCorpid("1210000006");
      setMd5("81WJdC4QARjHeNDOya4brkO70PlsbQnP");
    } else {
      setCorpid("1210000005");
      setMd5("Rk5E5SUxXA4ZOvxZmgmYYlHZv1WqoVf1");
    }

    Map<String,String> params = new HashMap<String,String>();

    if ("createOrder".equals(tradeType))
    {
      params.put("tradeCode", "IFZ1000");
      params.put("requestId", extraParams.get("systemOrderId").toString());
      params.put("tradeProcess", getCorpid());

      params.put("totalBizType", "BIZ01105");
      params.put("totalPrice", order.getAmount());
      params.put("userIdIdentity", extraParams.get("userIdIdentity").toString() + order.getMerchantid());
      params.put("rePayTimeOut", "1");
      params.put("noticeurl", nofityURL);
      params.put("encode", "GBK");
      params.put("productId", "1");
      params.put("productName", "µç×Ó¿ÍÆ±");
      params.put("fund", order.getAmount());
      params.put("merAcct", getCorpid());
      params.put("bizType", "BIZ01105");
      params.put("productNumber", "1");
      params.put("passThrough", "createOrder");
      params.put("goodsDesc", "ecard");

      String md5Str = (String)params.get("requestId") + (String)params.get("tradeProcess") + (String)params.get("totalBizType") + (String)params.get("totalPrice") + (String)params.get("noticeurl") + (String)params.get("userIdIdentity") + (String)params.get("passThrough");
      params.put("mersignature", hmacSign(md5Str, getMd5()));

      JSONObject orderResp = process(charset, params, getDesturl());

      if (orderResp.getString("resultSignature") != null)
      {
        String md5ResStr = orderResp.getString("requestId") + orderResp.getString("result") + orderResp.getString("passThrough");
        if (orderResp.getString("resultSignature").equals(hmacSign(md5ResStr, getMd5())))
        {
          orderResp.remove("requestId");
          orderResp.remove("passThrough");
          orderResp.remove("resultSignature");
          return orderResp.toString();
        }
        return null;
      }

      return null;
    }

    if ("pay".equals(tradeType))
    {
      params.put("tradeCode", "IFZ0001");
      params.put("requestId", order.getRandOrderID());
      params.put("requestOrderId", extraParams.get("systemOrderId").toString());
      params.put("tradeProcess", getCorpid());

      params.put("bankCode", formData.getString("bankCode"));
      params.put("bankAccount", formData.getString("no"));
      params.put("bankCardType", formData.getString("type"));
      if (("1".equals(formData.getString("type"))) && (formData.containsKey("exp"))) {
        params.put("validDate", formData
          .getString("exp"));
      }

      if (("1".equals(formData.getString("type"))) && (formData.containsKey("cvv2"))) {
        params.put("cvnCode", formData
          .getString("cvv2"));
      }

      params.put("idType", formData.getString("idType"));
      params.put("idNumber", formData.getString("idNo"));
      try {
        params.put("name", URLDecoder.decode(formData.getString("name"), "utf-8"));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      params.put("mobilePhone", formData.getString("phone"));
      params.put("isNeedBind", "1");
      params.put("userIdIdentity", formData.getString("userIdIdentity") + extraParams.get("currentMerchantId").toString());
      params.put("passThrough", "pay");
      params.put("tradeId", formData.getString("fengFuTradeId"));
      params.put("randomValidateId", formData.getString("codeNo"));
      params.put("randomCode", formData.getString("code"));
      params.put("encode", "GBK");

      String md5Str = (String)params.get("requestId") + (String)params.get("requestOrderId") + (String)params.get("tradeProcess") + (String)params.get("bankCode") + (String)params.get("bankAccount") + 
        (String)params.get("bankCardType") + (params.get("validDate") == null ? "" : (String)params.get("validDate")) + (params.get("cvnCode") == null ? "" : (String)params.get("cvnCode")) + 
        (String)params.get("idType") + (String)params.get("idNumber") + (String)params.get("name") + (String)params.get("mobilePhone") + 
        (String)params.get("userIdIdentity") + (String)params.get("passThrough") + (String)params.get("tradeId");
      params.put("mersignature", hmacSign(md5Str, getMd5()));

      JSONObject orderResp = process(charset, params, getDesturl());
      System.out.println("resp:====" + orderResp.toString());
      if (orderResp.getString("resultSignature") != null)
      {
        String md5ResStr = orderResp.getString("requestId") + orderResp.getString("result") + orderResp.getString("payId") + orderResp.getString("fiscalDate") + orderResp.getString("status") + orderResp.getString("passThrough");
        if (orderResp.getString("resultSignature").equals(hmacSign(md5ResStr, getMd5()))) {
          orderResp.remove("tradeFee");
          orderResp.remove("requestId");
          orderResp.remove("userIdIdentity");
          orderResp.remove("passThrough");
          orderResp.remove("status");

          orderResp.remove("payId");
          orderResp.put("payId", extraParams.get("systemOrderId"));
          orderResp.remove("tradeAmount");
          orderResp.remove("resultSignature");
          orderResp.accumulate("requestId", order.getOrdernum());
          return orderResp.toString();
        }
        return null;
      }

      return null;
    }

    if ("repay".equals(tradeType))
    {
      params.put("tradeCode", "IFZ0002");
      params.put("requestId", order.getRandOrderID());
      params.put("requestOrderId", extraParams.get("systemOrderId").toString());
      params.put("tradeProcess", getCorpid());

      params.put("bankCode", formData.getString("bankCode"));
      params.put("bindId", formData.getString("bindId"));
      params.put("userIdIdentity", formData.getString("userIdIdentity") + order.getMerchantid());
      params.put("passThrough", "repay");
      params.put("tradeId", formData.getString("fengFuTradeId"));
      params.put("randomValidateId", formData.getString("codeNo"));
      params.put("randomCode", formData.getString("code"));
      params.put("encode", "GBK");

      String md5Str = (String)params.get("requestId") + (String)params.get("requestOrderId") + (String)params.get("tradeProcess") + (String)params.get("bankCode") + 
        formData.getString("bindId") + 
        (String)params.get("userIdIdentity") + (String)params.get("passThrough") + (String)params.get("tradeId");
      params.put("mersignature", hmacSign(md5Str, getMd5()));

      JSONObject orderResp = process(charset, params, getDesturl());

      if (orderResp.getString("resultSignature") != null)
      {
        String md5ResStr = orderResp.getString("requestId") + orderResp.getString("result") + orderResp.getString("payId") + orderResp.getString("fiscalDate") + orderResp.getString("status") + orderResp.getString("passThrough");
        if (orderResp.getString("resultSignature").equals(hmacSign(md5ResStr, getMd5()))) {
          orderResp.remove("tradeFee");
          orderResp.remove("requestId");
          orderResp.remove("userIdIdentity");
          orderResp.remove("passThrough");
          orderResp.remove("status");

          orderResp.remove("payId");
          orderResp.put("payId", extraParams.get("systemOrderId"));
          orderResp.remove("tradeAmount");
          orderResp.remove("resultSignature");
          orderResp.accumulate("requestId", order.getOrdernum());
          return orderResp.toString();
        }
        return null;
      }

      return null;
    }

    if ("sendSMS".equals(tradeType))
    {
      params.put("tradeCode", "IFY0001");
      params.put("requestId", extraParams.get("rondamNo").toString());
      params.put("requestOrderId", extraParams.get("systemOrderId").toString());
      params.put("tradeProcess", getCorpid());
      params.put("bankCode", extraParams.get("bankCode").toString());
      params.put("bankAccount", extraParams.get("no").toString());
      params.put("bankCardType", extraParams.get("type").toString());
      if (("1".equals(extraParams.get("type").toString())) && (extraParams.containsKey("exp"))) {
        params.put("validDate", extraParams.get("exp").toString());
      }

      if (("1".equals(extraParams.get("type").toString())) && (extraParams.containsKey("cvv2"))) {
        params.put("cvnCode", extraParams.get("cvv2").toString());
      }

      params.put("idType", extraParams.get("idType").toString());
      params.put("idNumber", extraParams.get("idNo").toString());
      try {
        params.put("name", URLDecoder.decode(extraParams.get("name").toString(), "utf-8"));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      params.put("mobilePhone", extraParams.get("phone").toString());
      params.put("userIdIdentity", extraParams.get("userIdIdentity").toString() + order.getMerchantid());
      params.put("encode", "GBK");

      String md5Str = (String)params.get("requestId") + (String)params.get("requestOrderId") + (String)params.get("tradeProcess") + 
        (String)params.get("idType") + (String)params.get("idNumber") + (String)params.get("name") + 
        (String)params.get("mobilePhone") + (String)params.get("bankCode") + (String)params.get("bankAccount") + 
        (String)params.get("bankCardType") + (
        params.get("validDate") == null ? "" : (String)params.get("validDate")) + (params.get("cvnCode") == null ? "" : (String)params.get("cvnCode")) + 
        (String)params.get("userIdIdentity");
      params.put("mersignature", hmacSign(md5Str, getMd5()));

      JSONObject orderResp = process(charset, params, getDesturl());

      if (orderResp.getString("resultSignature") != null)
      {
        String md5ResStr = orderResp.getString("requestId") + orderResp.getString("result") + orderResp.getString("randomValidateId") + orderResp.getString("tradeId");
        if (orderResp.getString("resultSignature").equals(hmacSign(md5ResStr, getMd5()))) {
          orderResp.remove("requestId");
          orderResp.remove("resultSignature");
          return orderResp.toString();
        }
        return null;
      }

      return null;
    }

    if ("reSendSMS".equals(tradeType))
    {
      params.put("tradeCode", "IFY0002");
      params.put("requestId", extraParams.get("rondamNo").toString());
      params.put("requestOrderId", extraParams.get("systemOrderId").toString());
      params.put("tradeProcess", getCorpid());
      params.put("bankCode", extraParams.get("bankCode").toString());

      params.put("bindId", extraParams.get("bindId").toString());
      params.put("userIdIdentity", extraParams.get("userIdIdentity").toString() + order.getMerchantid());
      params.put("encode", "GBK");

      String md5Str = (String)params.get("requestId") + (String)params.get("requestOrderId") + (String)params.get("tradeProcess") + 
        (String)params.get("bankCode") + (String)params.get("bindId") + (String)params.get("userIdIdentity");
      params.put("mersignature", hmacSign(md5Str, getMd5()));

      JSONObject orderResp = process(charset, params, getDesturl());

      if (orderResp.getString("resultSignature") != null)
      {
        String md5ResStr = orderResp.getString("requestId") + orderResp.getString("result") + orderResp.getString("randomValidateId") + orderResp.getString("tradeId");
        if (orderResp.getString("resultSignature").equals(hmacSign(md5ResStr, getMd5()))) {
          orderResp.remove("requestId");
          orderResp.remove("resultSignature");
          return orderResp.toString();
        }
        return null;
      }

      return null;
    }

    return "";
  }

  public JSONObject process(String charset, Map<String, String> params, String reqUrl)
    throws ServiceException
  {
    String resp = null;
    HttpClient httpClient = null;

    PostMethod post = null;
    InputStream in = null;
    String dataDES = null;
    StringBuffer respBuffer = new StringBuffer("");
    httpClient = new HttpClient();
    post = new PostMethod(getDesturl());
    post.getParams().setContentCharset("GBK");
    if (reqUrl.indexOf("https") != -1) {
      SecureProtocolSocketFactory protoSocketFactory = new NoAuthSSLSocketFactory();
      Protocol authhttps = new Protocol("https", protoSocketFactory, 
        443);

      Protocol.registerProtocol("https", authhttps);
    }

    HttpParams httpParams = httpClient.getParams();
    httpParams.setIntParameter("http.connection.timeout", 
      60000);
    httpParams.setIntParameter("http.socket.timeout", 
      60000);

    Iterator keySet = params.keySet().iterator();
    while (keySet.hasNext()) {
      String key = ((String)keySet.next()).toString();
      post.addParameter(new NameValuePair(key, (String)params.get(key)));
    }

    try
    {
      int response = httpClient.executeMethod(post);
      byte[] responseData = post.getResponseBody();
      resp = new String(responseData, charset);

      if (post.getStatusLine().getStatusCode() != 200)
        throw new Exception(post.getStatusLine().toString() + "|" + 
          resp);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    return JSONObject.fromObject(resp.trim());
  }
  public static String hmacSign(String aValue, String aKey) {
    byte[] k_ipad = new byte[64];
    byte[] k_opad = new byte[64];
    byte[] keyb;
    byte[] value;
    try { 
      keyb = aKey.getBytes("utf-8");
      value = aValue.getBytes("utf-8");
    }
    catch (UnsupportedEncodingException e)
    {
      keyb = aKey.getBytes();
      value = aValue.getBytes();
    }
    Arrays.fill(k_ipad, keyb.length, 64, (byte)54);
    Arrays.fill(k_opad, keyb.length, 64, (byte)92);
    for (int i = 0; i < keyb.length; i++) {
      k_ipad[i] = ((byte)(keyb[i] ^ 0x36));
      k_opad[i] = ((byte)(keyb[i] ^ 0x5C));
    }
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      return null;
    }
    md.update(k_ipad);
    md.update(value);
    byte[] dg = md.digest();
    md.reset();
    md.update(k_opad);
    md.update(dg, 0, 16);
    dg = md.digest();
    return toHex(dg);
  }

  public static String toHex(byte[] src)
  {
    StringBuilder stringBuilder = new StringBuilder("");
    if ((src == null) || (src.length <= 0)) {
      return null;
    }
    for (int i = 0; i < src.length; i++) {
      int v = src[i] & 0xFF;
      String hv = Integer.toHexString(v);
      if (hv.length() < 2) {
        stringBuilder.append(0);
      }
      stringBuilder.append(hv);
    }
    return stringBuilder.toString();
  }
}