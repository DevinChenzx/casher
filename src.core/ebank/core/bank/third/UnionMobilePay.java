package ebank.core.bank.third;

import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.conf.UpmpConfig;
import ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.service.UpmpService;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.Crypt;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

public class UnionMobilePay extends BankExt
  implements BankService
{
  static Logger logger = Logger.getLogger(ChinaPnr.class);

//  private static String nofityURL = "http://epay.gicard.net/unionMobilePayNotify";
  private static String nofityURL = "http://221.222.214.223:8082/unionMobilePayNotify";
  
  public PayResult getPayResult(HashMap reqs)
    throws ServiceException
  {
    Map<String,String> params = new HashMap<String,String>();
    params.put("qn", reqs.get("qn").toString());
    params.put("respCode", reqs.get("respCode").toString());
    params.put("exchangeRate", reqs.get("exchangeRate").toString());
    params.put("merId", reqs.get("merId").toString());
    params.put("charset", reqs.get("charset").toString());
    params.put("settleDate", reqs.get("settleDate").toString());
    params.put("orderTime", reqs.get("orderTime").toString());
    params.put("transStatus", reqs.get("transStatus").toString());
    params.put("sysReserved", reqs.get("sysReserved").toString());
    params.put("version", reqs.get("version").toString());
    params.put("settleCurrency", reqs.get("settleCurrency").toString());
    params.put("signMethod", reqs.get("signMethod").toString());
    params.put("transType", reqs.get("transType").toString());
    params.put("settleAmount", reqs.get("settleAmount").toString());
    params.put("orderNumber", reqs.get("orderNumber").toString());
    params.put("signature", reqs.get("signature").toString());
    boolean isVerified = UpmpService.verifySignature(params);
    if (isVerified) {
      String trxnum = reqs.get("orderNumber").toString();
      String totalPrice = Amount.getFormatAmount(reqs.get("settleAmount").toString(), -2);
      String payId = reqs.get("qn").toString();
      String result = reqs.get("transStatus").toString();

      PayResult bean = new PayResult(trxnum, this.bankcode, new BigDecimal(
        totalPrice), "00".equals(result) ? 1 : -1);
      bean.setBanktransseq(payId);
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
    String charset = "UTF-8";

    if ("createOrder".equals(tradeType)) {
      Map<String,String> req = new HashMap<String,String>();
      req.put("version", UpmpConfig.VERSION);
      req.put("charset", UpmpConfig.CHARSET);
      req.put("transType", "01");
      req.put("merId", UpmpConfig.MER_ID);
      req.put("backEndUrl", UpmpConfig.MER_BACK_END_URL);
      req.put("frontEndUrl", UpmpConfig.MER_FRONT_END_URL);
      req.put("orderDescription", "∂©µ•√Ë ˆ");
      req.put("orderTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

      req.put("orderNumber", order.getRandOrderID());
      req.put("orderAmount", Amount.getIntAmount(formData.getString("amount"), 2)+"");
      req.put("orderCurrency", "156");

      Map<String, String> resp = new HashMap<String, String>();
      boolean validResp = UpmpService.trade(req, resp);

      if ((validResp) && ("00".equals(resp.get("respCode"))))
      {
        JSONObject ret = new JSONObject();
        ret.put("orderNum", resp.get("tn"));
        ret.put("result", "00000");
        return ret.toString();
      }

      return null;
    }

    return "";
  }

  public void config()
  {
    super.config();
    UpmpConfig.VERSION = "1.0.0";
    UpmpConfig.CHARSET = "UTF-8";
    UpmpConfig.TRADE_URL = getDesturl();
    UpmpConfig.QUERY_URL = getDesturl();
    UpmpConfig.MER_ID = getCorpid();
    UpmpConfig.MER_BACK_END_URL = nofityURL;
    UpmpConfig.MER_FRONT_END_URL = nofityURL;
    UpmpConfig.SIGN_TYPE = "MD5";
    UpmpConfig.SECURITY_KEY = getMd5();
  }
}