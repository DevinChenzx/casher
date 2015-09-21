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
import java.util.Set;
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

public class UnionPay extends BankExt
  implements BankService
{
  static Logger logger = Logger.getLogger(UnionPay.class);

//  private static String nofityURL = "http://epay.gicard.net/unionMobilePayNotify";
//  private static String nofityURL = "http://111.193.193.157:8082/unionMobilePayNotify";
  
  private String filePath;
  
  
  public void setFilePath(String filePath) {
	this.filePath = filePath;
  }

  
public String getFilePath() {
	return filePath;
}


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
        totalPrice), ("00".equals(result)&&respMsg.toUpperCase().contains("SUCCESS")) ? 1 : -1);
      bean.setBanktransseq(payId);
      
      if(1==bean.getTrxsts()){
    	  	reqs.put("RES", "{\"ret_code\":\"0000\",\"ret_msg\":\"���׳ɹ�\"}");
		} else {
			reqs.put("RES", "{\"ret_code\":\"9999\",\"ret_msg\":\"����ʧ��\"}");
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
//    String tradeType = formData.getString("transType");
    String charset = "UTF-8";
    
	String return_url = this.getRecurl();
	String notify_url = this.getRecurl() + "&NR=SID_" + this.idx;

	/**
	 * ��װ������
	 */
	Map<String, String> data = new HashMap<String, String>();
	// �汾��
	data.put("version", "5.0.0");
	// �ַ������� Ĭ��"UTF-8"
	data.put("encoding", charset);
	// ǩ������ 01 RSA
	data.put("signMethod", "01");
	// �������� 01-����
	data.put("txnType", "01");
	// ���������� 01:�������� 02:���� 03:���ڸ���
	data.put("txnSubType", "01");
	// ҵ������
	data.put("bizType", "000201");
	// �������ͣ�07-PC��08-�ֻ�
	data.put("channelType", "07");
	// ǰ̨֪ͨ��ַ ���ؼ����뷽ʽ������
	data.put("frontUrl", return_url);
	// ��̨֪ͨ��ַ
	data.put("backUrl", notify_url);
	// �������ͣ��̻�������0 0- �̻� �� 1�� �յ��� 2��ƽ̨�̻�
	data.put("accessType", "0");
	// �̻����룬��ĳ��Լ����̻���
	data.put("merId", getCorpid());
	// �̻������ţ�8-40λ������ĸ
	data.put("orderId", order.getRandOrderID());
	// ��������ʱ�䣬ȡϵͳʱ��
	data.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
	// ���׽���λ��
	data.put("txnAmt",  Amount.getIntAmount(order.getAmount(), 2)+"");
	// ���ױ���
	data.put("currencyCode", "156");
	// ���󷽱�����͸���ֶΣ���ѯ��֪ͨ�������ļ��о���ԭ������
	// data.put("reqReserved", "͸����Ϣ");
	// �����������ɲ����ͣ�����ʱ�ؼ��л���ʾ����Ϣ
	// data.put("orderDesc", "��������");

	Map<String, String> submitFromData = signData(data);

	// ��������url �������ļ���ȡ
	String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();
	/**
	 * ��������
	 */
	String html = createHtml(requestFrontUrl, submitFromData);

    return html;
  }

  
	/**
	 * ����HTTP POST���ױ����ķ���ʾ��
	 * 
	 * @param action
	 *            �����ύ��ַ
	 * @param hiddens
	 *            ��MAP��ʽ�洢�ı�����ֵ
	 * @return ����õ�HTTP POST���ױ���
	 */
	public static String createHtml(String action, Map<String, String> hiddens) {
		StringBuffer sf = new StringBuffer();
		sf.append("<form name= \"sendOrder\" action=\"" + action
				+ "\" method=\"post\">");
		if (null != hiddens && 0 != hiddens.size()) {
			Set<Entry<String, String>> set = hiddens.entrySet();
			Iterator<Entry<String, String>> it = set.iterator();
			while (it.hasNext()) {
				Entry<String, String> ey = it.next();
				String key = ey.getKey();
				String value = ey.getValue();
				sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\""
						+ key + "\" value=\"" + value + "\"/>");
			}
		}
		sf.append("</form>");
		return sf.toString();
	}

	
  public void config()
  {
    super.config();
    SDKConfig.getConfig().loadPropertiesFromPath(getFilePath());
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
				if("RemoteIP".equals(en) || "queryString".equals(en)||"idx".equals(en)||"NR".equals(en)){
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