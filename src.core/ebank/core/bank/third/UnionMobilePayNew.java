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
		// ��ȡ������������е���Ϣ
		Map<String, String> reqParam = getAllRequestParam(reqs);
		// ��ӡ������
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

		// ��֤ǩ��
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
		// �汾��
		data.put("version", "5.0.0");
		// �ַ������� Ĭ��"UTF-8"
		data.put("encoding", "UTF-8");
		// ǩ������ 01 RSA
		data.put("signMethod", "01");
		// �������� 01-����
		data.put("txnType", "01");
		// ���������� 01:�������� 02:���� 03:���ڸ���
		data.put("txnSubType", "01");
		// ҵ������
		data.put("bizType", "000201");
		// �������ͣ�07-PC��08-�ֻ�
		data.put("channelType", "08");
		// ǰ̨֪ͨ��ַ ���ؼ����뷽ʽ������
		data.put("frontUrl", "http://localhost:8080/ACPTest/acp_front_url.do");
		// ��̨֪ͨ��ַ	
		data.put("backUrl", nofityURL);
		// �������ͣ��̻�������0 0- �̻� �� 1�� �յ��� 2��ƽ̨�̻�
		data.put("accessType", "0");
		// �̻����룬��ĳ��Լ����̻���
		data.put("merId", getCorpid());
		// �̻������ţ�8-40λ������ĸ
		data.put("orderId", order.getRandOrderID());
		// ��������ʱ�䣬ȡϵͳʱ��
		data.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		// ���׽���λ��
		data.put("txnAmt",  Amount.getIntAmount(formData.getString("amount"), 2)+"");
		// ���ױ���
		data.put("currencyCode", "156");
		
		data = signData(data);

		String requestAppUrl = SDKConfig.getConfig().getAppRequestUrl();

		Map<String, String> resmap = submitUrl(data, requestAppUrl);
		
		
		// ���󷽱�����͸���ֶΣ���ѯ��֪ͨ�������ļ��о���ԭ������
		// data.put("reqReserved", "͸����Ϣ");
		// �����������ɲ����ͣ�����ʱ�ؼ��л���ʾ����Ϣ
		// data.put("orderDesc", "��������");
		
//      Map<String,String> req = new HashMap<String,String>();
//      req.put("version", UpmpConfig.VERSION);
//      req.put("charset", UpmpConfig.CHARSET);
//      req.put("transType", "01");
//      req.put("merId", UpmpConfig.MER_ID);
//      req.put("backEndUrl", UpmpConfig.MER_BACK_END_URL);
//      req.put("frontEndUrl", UpmpConfig.MER_FRONT_END_URL);
//      req.put("orderDescription", "��������");
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
				// ��valueֵ����ȥ��ǰ��մ���
				submitFromData.put(obj.getKey(), value.trim());
				System.out
						.println(obj.getKey() + "-->" + String.valueOf(value));
			}
		}
		/**
		 * ǩ��
		 */
		SDKUtil.sign(submitFromData, "UTF-8");

		return submitFromData;
	}
  
  
	public  Map<String, String> submitUrl(
			Map<String, String> submitFromData,String requestUrl) {
		String resultString = "";
		/**
		 * ����
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
		 * ��֤ǩ��
		 */
		if (null != resultString && !"".equals(resultString)) {
			// �����ؽ��ת��Ϊmap
			resData = SDKUtil.convertResultStringToMap(resultString);
			if (!SDKUtil.validate(resData, "UTF-8")) {
				resData=null;
				logger.debug("��ǩʧ��!");
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
				//�ڱ�������ʱ������ֶε�ֵΪ�գ�������<����Ĵ���Ϊ�ڻ�ȡ���в�������ʱ���ж���ֵΪ�գ���ɾ������ֶ�>
				//System.out.println("ServletUtil��247��  temp���ݵļ�=="+en+"     ֵ==="+value);
				if (null == res.get(en) || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}
}