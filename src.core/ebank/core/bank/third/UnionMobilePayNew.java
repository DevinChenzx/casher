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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.unionpay.acp.sdk.HttpClient;
import com.unionpay.acp.sdk.LogUtil;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKConstants;
import com.unionpay.acp.sdk.SDKUtil;

public class UnionMobilePayNew extends BankExt
  implements BankService
{
  static Logger logger = Logger.getLogger(UnionMobilePayNew.class);

//  private static String nofityURL = "http://epay.gicard.net/unionMobilePayNotify";
  private static String nofityURL = "http://111.193.193.157:8082/unionMobilePayNotify";
  
  public PayResult getPayResult(HashMap reqs)
    throws ServiceException
  {
		String encoding = reqs.get(SDKConstants.param_encoding).toString();
		// 获取请求参数中所有的信息
		Map<String, String> reqParam = getAllRequestParam(reqs);
		// 打印请求报文
		LogUtil.printRequestLog(reqParam);

		Map<String, String> valideData = null;
		if (null != reqParam && !reqParam.isEmpty()) {
			Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
			valideData = new HashMap<String, String>(reqParam.size());
			while (it.hasNext()) {
				Entry<String, String> e = it.next();
				String key = (String) e.getKey();
				String value = (String) e.getValue();
				try {
					value = new String(value.getBytes("ISO-8859-1"), encoding);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				valideData.put(key, value);
			}
		}

		// 验证签名
		boolean isVerified = SDKUtil.validate(valideData, encoding);
		
//    Map<String,String> params = new HashMap<String,String>();
//    params.put("qn", reqs.get("qn").toString());
//    params.put("respCode", reqs.get("respCode").toString());
//    params.put("exchangeRate", reqs.get("exchangeRate").toString());
//    params.put("merId", reqs.get("merId").toString());
//    params.put("charset", reqs.get("charset").toString());
//    params.put("settleDate", reqs.get("settleDate").toString());
//    params.put("orderTime", reqs.get("orderTime").toString());
//    params.put("transStatus", reqs.get("transStatus").toString());
//    params.put("sysReserved", reqs.get("sysReserved").toString());
//    params.put("version", reqs.get("version").toString());
//    params.put("settleCurrency", reqs.get("settleCurrency").toString());
//    params.put("signMethod", reqs.get("signMethod").toString());
//    params.put("transType", reqs.get("transType").toString());
//    params.put("settleAmount", reqs.get("settleAmount").toString());
//    params.put("orderNumber", reqs.get("orderNumber").toString());
//    params.put("signature", reqs.get("signature").toString());
    
    if (isVerified) {
      String trxnum = reqs.get("orderId").toString();
      String totalPrice = Amount.getFormatAmount(reqs.get("settleAmt").toString(), -2);
      String payId = reqs.get("queryId").toString();
      String result = reqs.get("respCode").toString();
      String respMsg = reqs.get("respMsg").toString();
      
      PayResult bean = new PayResult(trxnum, this.bankcode, new BigDecimal(
        totalPrice), ("00".equals(result)&&respMsg.contains("Success")) ? 1 : -1);
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
    	
    	Map<String, String> data = new HashMap<String, String>();
		// 版本号
		data.put("version", "5.0.0");
		// 字符集编码 默认"UTF-8"
		data.put("encoding", "UTF-8");
		// 签名方法 01 RSA
		data.put("signMethod", "01");
		// 交易类型 01-消费
		data.put("txnType", "01");
		// 交易子类型 01:自助消费 02:订购 03:分期付款
		data.put("txnSubType", "01");
		// 业务类型
		data.put("bizType", "000201");
		// 渠道类型，07-PC，08-手机
		data.put("channelType", "08");
		// 前台通知地址 ，控件接入方式无作用
		data.put("frontUrl", "http://localhost:8080/ACPTest/acp_front_url.do");
		// 后台通知地址	
		data.put("backUrl", nofityURL);
		// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
		data.put("accessType", "0");
		// 商户号码，请改成自己的商户号
		data.put("merId", getCorpid());
		// 商户订单号，8-40位数字字母
		data.put("orderId", order.getRandOrderID());
		// 订单发送时间，取系统时间
		data.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		// 交易金额，单位分
		data.put("txnAmt",  Amount.getIntAmount(formData.getString("amount"), 2)+"");
		// 交易币种
		data.put("currencyCode", "156");
		
		data = signData(data);

		String requestAppUrl = SDKConfig.getConfig().getAppRequestUrl();

		Map<String, String> resmap = submitUrl(data, requestAppUrl);
		
		
		// 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
		// data.put("reqReserved", "透传信息");
		// 订单描述，可不上送，上送时控件中会显示该信息
		// data.put("orderDesc", "订单描述");
		
//      Map<String,String> req = new HashMap<String,String>();
//      req.put("version", UpmpConfig.VERSION);
//      req.put("charset", UpmpConfig.CHARSET);
//      req.put("transType", "01");
//      req.put("merId", UpmpConfig.MER_ID);
//      req.put("backEndUrl", UpmpConfig.MER_BACK_END_URL);
//      req.put("frontEndUrl", UpmpConfig.MER_FRONT_END_URL);
//      req.put("orderDescription", "订单描述");
//      req.put("orderTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
//
//      req.put("orderNumber", order.getRandOrderID());
//      req.put("orderAmount", Amount.getIntAmount(formData.getString("amount"), 2)+"");
//      req.put("orderCurrency", "156");
//      Map<String, String> resp = new HashMap<String, String>();
//      boolean validResp = UpmpService.trade(req, resp);

      if ((resmap!=null) && ("00".equals(resmap.get("respCode"))))
      {
        JSONObject ret = new JSONObject();
        ret.put("orderNum", resmap.get("tn"));
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
    SDKConfig.getConfig().loadPropertiesFromSrc();
  }
  
  
  public  Map<String, String> signData(Map<String, ?> contentData) {
		Entry<String, String> obj = null;
		Map<String, String> submitFromData = new HashMap<String, String>();
		for (Iterator<?> it = contentData.entrySet().iterator(); it.hasNext();) {
			obj = (Entry<String, String>) it.next();
			String value = obj.getValue();
			if (StringUtils.isNotBlank(value)) {
				// 对value值进行去除前后空处理
				submitFromData.put(obj.getKey(), value.trim());
				System.out
						.println(obj.getKey() + "-->" + String.valueOf(value));
			}
		}
		/**
		 * 签名
		 */
		SDKUtil.sign(submitFromData, "UTF-8");

		return submitFromData;
	}
  
  
	public  Map<String, String> submitUrl(
			Map<String, String> submitFromData,String requestUrl) {
		String resultString = "";
		/**
		 * 发送
		 */
		HttpClient hc = new HttpClient(requestUrl, 30000, 30000);
		try {
			int status = hc.send(submitFromData, "UTF-8");
			if (200 == status) {
				resultString = hc.getResult();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> resData = new HashMap<String, String>();
		/**
		 * 验证签名
		 */
		if (null != resultString && !"".equals(resultString)) {
			// 将返回结果转换为map
			resData = SDKUtil.convertResultStringToMap(resultString);
			if (!SDKUtil.validate(resData, "UTF-8")) {
				resData=null;
				logger.debug("验签失败!");
			} 
		}
		return resData;
	}

	
	public  Map<String, String> getAllRequestParam(final Map request) {
		Map<String, String> res = new HashMap<String, String>();
		Iterator<?> temp = request.keySet().iterator();
		if (null != temp) {
			while (temp.hasNext()) {
				String en = (String) temp.next();
				if("RemoteIP".equals(en) || "queryString".equals(en)){
					continue;
				}
				String value = request.get(en).toString();
				res.put(en, value);
				//在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				//System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
				if (null == res.get(en) || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}
}