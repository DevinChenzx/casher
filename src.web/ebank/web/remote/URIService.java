/*
 * @Id: URIService.java 16:48:28 2006-7-28
 * 
 * @author 
 * @version 1.0
 * payment_web_v2.0 PROJECT
 */
package ebank.web.remote;

/**
 * @author 
 * Description: ��ȡ������Դ
 * 
 */
public interface URIService {
	/**
	 * ��ȡ���ص�ַ
	 * @param merchantid �̻���
	 * @param version    �����汾��
	 * @return
	 */
	public String getUrl(String merchantid,String version);

}
