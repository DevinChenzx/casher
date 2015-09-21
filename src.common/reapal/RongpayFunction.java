package reapal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import beartool.Md5Encrypt;


import ebank.core.common.Constants;

/**
 *���ܣ�����֧���ӿڹ��ú���
 *��ϸ����ҳ��������֪ͨ���������ļ������õĹ��ú������Ĵ����ļ�������Ҫ�޸�
 *�汾��1.0
 *�޸����ڣ�2012-05-01
 '˵����
 '���´���ֻ��Ϊ�˷����̻����Զ��ṩ���������룬�̻����Ը����Լ���վ����Ҫ�����ռ����ĵ���д,����һ��Ҫʹ�øô��롣
 '�ô������ѧϰ���о�����֧���ӿ�ʹ�ã�ֻ���ṩһ���ο���
*/
public class RongpayFunction {
	
	/** 
	 * ���ܣ�����ǩ�����
	 * @param sArray Ҫǩ��������
	 * @param key ��ȫУ����
	 * @return ǩ������ַ���
	 */
	public static String BuildMysign(Map sArray, String key) {
		if(sArray!=null && sArray.size()>0){
			StringBuilder prestr = CreateLinkString(sArray);  //����������Ԫ�أ����ա�����=����ֵ����ģʽ�á�&���ַ�ƴ�ӳ��ַ���
			System.out.println("prestr:"+prestr);
			System.out.println("key:"+key);
			return Md5Encrypt.md5(prestr.append(key).toString(),Constants.input_charset);//��ƴ�Ӻ���ַ������밲ȫУ����ֱ����������,�������ɼ��ܴ�
		}
		return null;
	}
	
	/** 
	 * ���ܣ�����������Ԫ�����򣬲����ա�����=����ֵ����ģʽ�á�&���ַ�ƴ�ӳ��ַ���
	 * @param params ��Ҫ���򲢲����ַ�ƴ�ӵĲ�����
	 * @return ƴ�Ӻ��ַ���
	 */
	public static StringBuilder CreateLinkString(Map params){
			List keys = new ArrayList(params.keySet());
			Collections.sort(keys);
	
			StringBuilder prestr = new StringBuilder();
			String key="";
			String value="";
			for (int i = 0; i < keys.size(); i++) {
				key=(String) keys.get(i);
				value = (String) params.get(key);
				if("".equals(value) || value == null || 
						key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")){
					continue;
				}
				prestr.append(key).append("=").append(value).append("&");
			}
			return prestr.deleteCharAt(prestr.length()-1);
	}
	
	/**
	 * ������֧��POST����������Ϣת��һ��
	 * @param requestParams ���ز�����Ϣ
	 * @return Map ����һ��ֻ���ַ���ֵ��MAP
	 * */
	public static Map transformRequestMap(Map requestParams){
		Map params = null;
		if(requestParams!=null && requestParams.size()>0){
			params = new HashMap();
			String name ="";
			String[] values =null;
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				name= (String) iter.next();
				values= (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				//����������δ����ڳ�������ʱʹ�á����mysign��sign�����Ҳ����ʹ����δ���ת��
				params.put(name, valueStr);
			}
		}
		return params;
	}
	
	/**
	* *���ܣ���ȡԶ�̷�����ATN���,��֤����URL
	* @param notify_id ֪ͨУ��ID
	* @return ������ATN���
	* ��֤�������
	* invalid����������� ��������������ⷵ�ش�����merchant_ID��key�Ƿ�Ϊ�� 
	* true ������ȷ��Ϣ
	* false �������ǽ�����Ƿ�������ֹ�˿������Լ���֤ʱ���Ƿ񳬹�һ����
	*/
	public static String Verify(String notify_id,String corpid){
		
		//��ȡԶ�̷�����ATN�������֤�Ƿ��Ǽ���֧������������������
		String transport = "http";
		
		String merchant_ID =corpid;
		
		StringBuilder veryfy_url = new StringBuilder();
		if(transport.equalsIgnoreCase("https")){
			veryfy_url.append("https://interface.reapal.com/verify/notify?");
		} else{
			veryfy_url.append("http://interface.reapal.com/verify/notify?");
		}
		veryfy_url.append("merchant_ID=").append(merchant_ID).append("&notify_id=").append(notify_id);
		System.out.println(veryfy_url);
		
		String responseTxt = CheckUrl(veryfy_url.toString());
		
		return responseTxt;

	}
	
	/**
	* *���ܣ���ȡԶ�̷�����ATN���
	* @param urlvalue ָ��URL·����ַ
	* @return ������ATN���
	* ��֤�������
	* invalid����������� ��������������ⷵ�ش�����merchant_ID��key�Ƿ�Ϊ�� 
	* true ������ȷ��Ϣ
	* false �������ǽ�����Ƿ�������ֹ�˿������Լ���֤ʱ���Ƿ񳬹�һ����
	*/
	private static String CheckUrl(String urlvalue){
		String inputLine = "";
		try {
			URL url = new URL(urlvalue);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			if(in!=null){
				inputLine = in.readLine().toString();
			}
			in.close();
			urlConnection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputLine;
	}
	
	/**
	 * ���ܣ�XML���Ľ�������
	 * */
	public static HashMap GetMessage(String url){
		SAXReader reader = new SAXReader();
		Document doc=null;
		HashMap hm=null;
		try {
			InputStream in=new URL(url).openStream();
			if (in != null) {
				doc = reader.read(in);
				hm = new HashMap();
				Element root = doc.getRootElement();
				for (Iterator it = root.elementIterator(); it.hasNext();) {
					Element e = (Element) it.next();
					if (e.nodeCount() > 1) {
						HashMap hm1 = new HashMap();
						for (Iterator it1 = e.elementIterator(); it1.hasNext();) {
							Element e1 = (Element) it1.next();
							hm1.put(e1.getName(), e1.getText());
						}
						hm.put(e.getName(), hm1);
					} else {
						hm.put(e.getName(), e.getText());
					}
				}
			}
			doc.clearContent();
			in.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hm;
	}
}
