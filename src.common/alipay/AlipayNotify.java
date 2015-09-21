package alipay;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ebank.core.bank.third.Alipay;
import ebank.core.common.util.MD5sign;

/* *
 *������AlipayNotify
 *���ܣ�֧����֪ͨ������
 *��ϸ������֧�������ӿ�֪ͨ����
 *�汾��3.2
 *���ڣ�2011-03-25
 *˵����
 *���´���ֻ��Ϊ�˷����̻����Զ��ṩ���������룬�̻����Ը����Լ���վ����Ҫ�����ռ����ĵ���д,����һ��Ҫʹ�øô��롣
 *�ô������ѧϰ���о�֧�����ӿ�ʹ�ã�ֻ���ṩһ���ο�

 *************************ע��*************************
 *����֪ͨ����ʱ���ɲ鿴���дlog��־��д��TXT������ݣ������֪ͨ�����Ƿ�����
 */
public class AlipayNotify {
	static Logger logger = Logger.getLogger(AlipayNotify.class); 	
    /**
     * HTTPS��ʽ��Ϣ��֤��ַ
     */
    private static final String HTTPS_VERIFY_URL = "https://www.alipay.com/cooperate/gateway.do?service=notify_verify&";

    /**
     * HTTP��ʽ��Ϣ��֤��ַ
     */
    private static final String HTTP_VERIFY_URL  = "http://notify.alipay.com/trade/notify_query.do?";

    /**
     * ��֤��Ϣ�Ƿ���֧���������ĺϷ���Ϣ
     * @param params ֪ͨ�������Ĳ�������
     * @return ��֤���
     */
    public static boolean verify(Map<String, String> params,String pubkey,String partner) {
        String mysign = getMysign(params,pubkey);
        String responseTxt = "true";
        if(params.get("notify_id") != null) {responseTxt = verifyResponse(params.get("notify_id"),partner);}
        String sign = "";
        logger.debug("notify_id:"+params.get("notify_id"));
        logger.debug("responseTxt:"+responseTxt);
        if(params.get("sign") != null) {sign = params.get("sign");}
        if (mysign.equals(sign) && responseTxt.equals("true")) {
            return true;
        } else {
        	 logger.debug("params:"+params+" sign:"+mysign);
            return false;
        }
    }

    /**
     * ���ݷ�����������Ϣ������ǩ�����
     * @param Params ֪ͨ�������Ĳ�������
     * @return ���ɵ�ǩ�����
     */
    private static String getMysign(Map<String, String> Params,String pubkey) {
    	
    	MD5sign t = new MD5sign();
		Map<String, String> sPara=t.ParaFilter(Params);
        String mysign = t.BuildMysign(sPara,pubkey,"utf-8");//���ǩ�����
        return mysign;
    }

    /**
    * ��ȡԶ�̷�����ATN���,��֤����URL
    * @param notify_id ֪ͨУ��ID
    * @return ������ATN���
    * ��֤�������
    * invalid����������� ��������������ⷵ�ش�����partner��key�Ƿ�Ϊ�� 
    * true ������ȷ��Ϣ
    * false �������ǽ�����Ƿ�������ֹ�˿������Լ���֤ʱ���Ƿ񳬹�һ����
    */
    private static String verifyResponse(String notify_id,String partner) {
        //��ȡԶ�̷�����ATN�������֤�Ƿ���֧��������������������
        String transport = "http";
        String veryfy_url = "";
        if (transport.equalsIgnoreCase("https")) {
            veryfy_url = HTTPS_VERIFY_URL;
        } else {
            veryfy_url = HTTP_VERIFY_URL;
        }
        veryfy_url = veryfy_url + "partner=" + partner + "&notify_id=" + notify_id;

        return checkUrl(veryfy_url);
    }

    /**
    * ��ȡԶ�̷�����ATN���
    * @param urlvalue ָ��URL·����ַ
    * @return ������ATN���
    * ��֤�������
    * invalid����������� ��������������ⷵ�ش�����partner��key�Ƿ�Ϊ�� 
    * true ������ȷ��Ϣ
    * false �������ǽ�����Ƿ�������ֹ�˿������Լ���֤ʱ���Ƿ񳬹�һ����
    */
    private static String checkUrl(String urlvalue) {
        String inputLine = "";

        try {
            URL url = new URL(urlvalue);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection
                .getInputStream()));
            inputLine = in.readLine().toString();
        } catch (Exception e) {
            e.printStackTrace();
            inputLine = "";
        }

        return inputLine;
    }
    
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

}
