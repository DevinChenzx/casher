package ebank.core.bank.third;

import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

public class LeFuMobile extends BankExt
  implements BankService
{
  static Logger logger = Logger.getLogger(LeFuMobile.class);

  private static String apiCode = "directPay";

  private static String versionCode = "1.0";

  private static String inputCharset = "UTF-8";

  private static String signType = "MD5";

  private static String redirectURL = "http://epay.gicard.net/LefuPayRedirectMobile";

  private static String nofityURL = "http://epay.gicard.net/LefuPayNotifyMobile";

  private static String sign = "";

  private static String buyer = "";

  private static String buyerContactType = "email";

  private static String buyerContact = "test@lefu8.com";

  private static String paymentType = "ALL";

  private static String interfaceCode = "";

  private static String retryFalg = "TRUE";

  private static String timeout = "1D";
  private String pubkey;

  private String generateSign(BankOrder order)
  {
    HashMap mp = order.getMp();
    JSONObject jo = null;
    if ((mp != null) && (mp.get("outJson") != null)) {
      String outjson = CryptUtil.decrypt(String.valueOf(mp.get("outJson")));
      logger.info(outjson);
      jo = JSONObject.fromObject(outjson);
    }

    String gateID = "";
    if ((order.getMp() != null) && (order.getMp().get("outChannel") != null)) {
      gateID = String.valueOf(order.getMp().get("outChannel"));
    }
    String defaultbank = getJsonParams(jo, "defaultbank", gateID);

    if ((defaultbank.equals("BCM")) && (order.getPayment_type().equals("2"))) interfaceCode = "B2C_BCM-DEBIT_CARD";
    if ((defaultbank.equals("BOC")) && (order.getPayment_type().equals("2"))) interfaceCode = "B2C_BOC-DEBIT_CARD";
    if ((defaultbank.equals("CCB")) && (order.getPayment_type().equals("2"))) interfaceCode = "B2C_CCB-DEBIT_CARD";
    if ((defaultbank.equals("CEB")) && (order.getPayment_type().equals("2"))) interfaceCode = "B2C_CEB-DEBIT_CARD";
    if ((defaultbank.equals("CMB")) && (order.getPayment_type().equals("2"))) interfaceCode = "B2C_CMB-DEBIT_CARD";
    if ((defaultbank.equals("CMBC")) && (order.getPayment_type().equals("2"))) interfaceCode = "B2C_CMBC-DEBIT_CARD";
    if ((defaultbank.equals("ICBC")) && (order.getPayment_type().equals("2"))) interfaceCode = "B2C_ICBC-DEBIT_CARD";
    if ((defaultbank.equals("PAB")) && (order.getPayment_type().equals("2"))) interfaceCode = "B2C_PAB-DEBIT_CARD";
    if ((defaultbank.equals("PSBC")) && (order.getPayment_type().equals("2"))) interfaceCode = "B2C_PSBC-DEBIT_CARD";
    if ((defaultbank.equals("CIB")) && (order.getPayment_type().equals("2"))) interfaceCode = "B2C_CIB-DEBIT_CARD";
    if ((defaultbank.equals("HXB")) && (order.getPayment_type().equals("2"))) interfaceCode = "B2C_HXB-DEBIT_CARD";

    if ((defaultbank.equals("BCM")) && (order.getPayment_type().equals("1"))) interfaceCode = "B2C_BCM-CREDIT_CARD";
    if ((defaultbank.equals("BOC")) && (order.getPayment_type().equals("1"))) interfaceCode = "B2C_BOC-CREDIT_CARD";
    if ((defaultbank.equals("CCB")) && (order.getPayment_type().equals("1"))) interfaceCode = "B2C_CCB-CREDIT_CARD";
    if ((defaultbank.equals("CEB")) && (order.getPayment_type().equals("1"))) interfaceCode = "B2C_CEB-CREDIT_CARD";
    if ((defaultbank.equals("CMB")) && (order.getPayment_type().equals("1"))) interfaceCode = "B2C_CMB-CREDIT_CARD";
    if ((defaultbank.equals("CMBC")) && (order.getPayment_type().equals("1"))) interfaceCode = "B2C_CMBC-CREDIT_CARD";
    if ((defaultbank.equals("ICBC")) && (order.getPayment_type().equals("1"))) interfaceCode = "B2C_ICBC-CREDIT_CARD";
    if ((defaultbank.equals("PAB")) && (order.getPayment_type().equals("1"))) interfaceCode = "B2C_PAB-CREDIT_CARD";
    if ((defaultbank.equals("PSBC")) && (order.getPayment_type().equals("1"))) interfaceCode = "B2C_PSBC-CREDIT_CARD";
    if ((defaultbank.equals("CIB")) && (order.getPayment_type().equals("1"))) interfaceCode = "B2C_CIB-CREDIT_CARD";
    if ((defaultbank.equals("HXB")) && (order.getPayment_type().equals("1"))) interfaceCode = "B2C_HXB-CREDIT_CARD";

    String sign = "amount=" + order.getAmount() + "&apiCode=" + apiCode + "&buyer=" + order.getMerchantid() + "&buyerContact=" + buyerContact + "&buyerContactType=" + buyerContactType + 
      "&clientIP=" + order.getCustip() + "&currency=" + order.getCurrency() + "&inputCharset=" + inputCharset + "&notifyURL=" + nofityURL + 
      "&outOrderId=" + order.getRandOrderID() + "&partner=" + getCorpid() + "&paymentType=" + paymentType + "&redirectURL=" + redirectURL + "&retryFalg=" + retryFalg + 
      "&signType=" + signType + "&submitTime=" + order.getPostdate() + "&timeout=" + timeout + "&versionCode=" + versionCode + this.pubkey;
    sign = getMD5Str(sign);

    return sign;
  }

  public String sendOrderToBank(BankOrder order) throws ServiceException
  {
    logger.info("乐富在线支付接口 start.....");

    StringBuffer sf = new StringBuffer("");
    sf.append("<form name=sendOrder method=\"post\" action=\"" + getDesturl() + "\">");
    sf.append("<input type=\"hidden\" name=\"apiCode\" value=\"" + apiCode + "\" >");
    sf.append("<input type=\"hidden\" name=\"versionCode\" value=\"" + versionCode + "\" >");
    sf.append("<input type=\"hidden\" name=\"inputCharset\" value=\"" + inputCharset + "\" >");
    sf.append("<input type=\"hidden\" name=\"signType\" value=\"" + signType + "\" >");
    sf.append("<input type=\"hidden\" name=\"redirectURL\" value=\"" + redirectURL + "\" >");
    sf.append("<input type=\"hidden\" name=\"notifyURL\" value=\"" + nofityURL + "\" >");

    buyer = order.getMerchantid();

    sign = generateSign(order);

    sf.append("<input type=\"hidden\" name=\"sign\" value=\"" + sign + "\" >");
    sf.append("<input type=\"hidden\" name=\"partner\" value=\"" + getCorpid() + "\" >");
    sf.append("<input type=\"hidden\" name=\"buyer\" value=\"" + buyer + "\" >");
    sf.append("<input type=\"hidden\" name=\"buyerContactType\" value=\"" + buyerContactType + "\">");
    sf.append("<input type=\"hidden\" name=\"buyerContact\" value=\"" + buyerContact + "\">");
    sf.append("<input type=\"hidden\" name=\"outOrderId\" value=\"" + order.getRandOrderID() + "\">");
    sf.append("<input type=\"hidden\" name=\"amount\" value=\"" + order.getAmount() + "\">");
    sf.append("<input type=\"hidden\" name=\"paymentType\" value=\"" + paymentType + "\">");
    sf.append("<input type=\"hidden\" name=\"interfaceCode\" value=\"" + interfaceCode + "\">");
    sf.append("<input type=\"hidden\" name=\"retryFalg\" value=\"" + retryFalg + "\" >");
    sf.append("<input type=\"hidden\" name=\"submitTime\" value=\"" + order.getPostdate() + "\" >");
    sf.append("<input type=\"hidden\" name=\"timeout\" value=\"" + timeout + "\" >");
    sf.append("<input type=\"hidden\" name=\"clientIP\" value=\"" + order.getCustip() + "\" >");
    sf.append("<input type=\"hidden\" name=\"currency\" value=\"" + order.getCurrency() + "\" >");
    sf.append("<input type=\"hidden\" name=\"extParam\" value=\"\" >");
    sf.append("<input type=\"hidden\" name=\"returnParam\" value=\"\" >");
    sf.append("<input type=\"hidden\" name=\"productName\" value=\"\" >");
    sf.append("<input type=\"hidden\" name=\"productPrice\" value=\"\" >");
    sf.append("<input type=\"hidden\" name=\"productNum\" value=\"\" >");
    sf.append("<input type=\"hidden\" name=\"productURL\" value=\"\" >");
    sf.append("</form>");
    if (logger.isDebugEnabled()) logger.debug(sf.toString());
    return sf.toString();
  }

  public PayResult getPayResult(HashMap reqs) throws ServiceException
  {
    logger.info("乐富在线支付结果通知。。。。。。。。。");

    String NR = String.valueOf(reqs.get("NR"));

    String apiCode = String.valueOf(reqs.get("apiCode"));
    String versionCode = String.valueOf(reqs.get("versionCode"));
    String currency = String.valueOf(reqs.get("currency"));
    String amount = String.valueOf(reqs.get("amount"));
    String handlerResult = String.valueOf(reqs.get("handlerResult"));
    String tradeOrderCode = String.valueOf(reqs.get("tradeOrderCode"));
    String outOrderId = String.valueOf(reqs.get("outOrderId"));
    String inputCharset = String.valueOf(reqs.get("inputCharset"));
    String signType = String.valueOf(reqs.get("signType"));
    String partner = String.valueOf(reqs.get("partner"));
    String returnParam = String.valueOf(reqs.get("returnParam"));
    String sign = String.valueOf(reqs.get("sign"));

    String returnSign = "amount=" + amount + "&apiCode=" + apiCode + "&currency=" + currency + "&handlerResult=" + handlerResult + 
      "&inputCharset=" + inputCharset + "&outOrderId=" + outOrderId + "&partner=" + partner + "&signType=" + signType + "&tradeOrderCode=" + tradeOrderCode + 
      "&versionCode=" + versionCode + this.pubkey;
    returnSign = getMD5Str(returnSign);

    PayResult bean = null;
    boolean verifyresult = returnSign.equals(sign);
    try {
      if (logger.isDebugEnabled()) logger.debug("lefu:verifyresult=" + verifyresult + ",handlerResult=" + handlerResult + ",sign:" + sign);
      if (verifyresult) {
        bean = new PayResult(outOrderId, this.bankcode, new BigDecimal(amount), "0000".equals(handlerResult) ? 1 : -1);
        if (String.valueOf("SID_" + this.idx).equalsIgnoreCase(NR)) {
          logger.info("支付结果后台 支付结果后台....... ");
          reqs.put("RES", "SUCCESS");
        }
      } else {
        throw new ServiceException("201003");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bean;
  }

  public void setPubkey(String pubkey) {
    this.pubkey = pubkey;
  }

  private String getJsonParams(JSONObject jo, String key, String defaults) {
    try {
      if (jo != null) return jo.getString(key) == null ? defaults : jo.getString(key); 
    }
    catch (Exception localException)
    {
    }
    return defaults;
  }

  private String getMD5Str(String str) {
    MessageDigest messageDigest = null;
    try
    {
      messageDigest = MessageDigest.getInstance("MD5");

      messageDigest.reset();

      messageDigest.update(str.getBytes("UTF-8"));
    } catch (NoSuchAlgorithmException e) {
      System.out.println("NoSuchAlgorithmException caught!");
      System.exit(-1);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    byte[] byteArray = messageDigest.digest();

    StringBuffer md5StrBuff = new StringBuffer();

    for (int i = 0; i < byteArray.length; i++) {
      if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
        md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
      else {
        md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
      }
    }
    return md5StrBuff.toString().toUpperCase();
  }
}