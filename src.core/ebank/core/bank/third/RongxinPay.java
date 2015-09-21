package ebank.core.bank.third;

import beartool.Md5Encrypt;
import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

public class RongxinPay extends BankExt
  implements BankService
{
  static Logger logger = Logger.getLogger(RongxinPay.class);

  private static String nofityURL ="http://epay.gicard.net/RongxinNotify";
  
  public String sendOrderToBank(BankOrder order) throws ServiceException
  {
    StringBuffer sbHtml = new StringBuffer();
    HashMap mp = order.getMp();

    String outjson = CryptUtil.decrypt(String.valueOf(mp.get("outJson")));
    logger.info(outjson);
    JSONObject jo = JSONObject.fromObject(outjson);
    String service = "online_pay";
    String partner = this.corpid;
    String return_url = getRecurl();
    String notify_url = getRecurl() + "&NR=SID_" + this.idx;
    String sign_type = "MD5";

    Map<String,String> sParaTemp = new HashMap<String,String>();
    sParaTemp.put("tradeId", getCorpid());

    sParaTemp.put("orderId", order.getRandOrderID());
    sParaTemp.put("amount", order.getAmount());
    sParaTemp.put("returnUrl", return_url);
    sParaTemp.put("noticeUrl", nofityURL);
    sParaTemp.put("bankType", mp.get("directBankCode").toString());
    sParaTemp.put("billCreateIp", order.getCustip());

    String md5 = Md5Encrypt.md5(order.getRandOrderID() + order.getAmount() + getCorpid(), "UTF-8");

    sParaTemp.put("encryptionParams", md5);

    List<String> keys = new ArrayList(sParaTemp.keySet());

    String redirectUrl = this.desturl + "?";
    for (String key : keys) {
      String value = (String)sParaTemp.get(key);
      try {
        redirectUrl = redirectUrl + key + "=" + URLEncoder.encode(value, "UTF-8") + "&";
      }
      catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug("str to alipay:" + sbHtml.toString());
    }
    return redirectUrl;
  }

  public PayResult getPayResult(HashMap request) throws ServiceException
  {
    PayResult bean = null;

    String isSuccess = "0";

    Object idx = request.get("idx");
    Object result = request.get("result");
    
    if ((idx!=null&&idx.toString().indexOf("result=1") > -1)||(result!=null&&"1".equals(result.toString())))
    {
      isSuccess = "1";
    }

    bean = new PayResult(request.get("orderId").toString(), 
      this.bankcode, new BigDecimal(request.get("transAmount")
      .toString()), 1);
    bean.setBanktransseq(request.get("bankBillNo").toString());

    if ("1".equals(isSuccess))
      request.put("RES", "1");
    else {
      request.put("RES", "-1");
    }

    return bean;
  }
}