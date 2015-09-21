/*
 * @Id: JniProxy.java 10:04:45 2006-9-13
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.remote;

/**
 * @author 
 * Description: �ӿ�ʹ��jni�Ĵ���
 * 
 */
public interface JniProxyService {
	/**
	 * ���ݷ���IDȡ�ü�������
	 * @param plain
	 * @param serviceid
	 * @param cmd 
	 * @return
	 */
	public String getCrypt(String plain,String serviceid,String cmd);
	
	/**
	 * ��֤ǩ������
	 * @param plain
	 * @param serviceid
	 * @return
	 */
	public boolean verify(String plain,String serviceid,String cmd);
	
	/**
	 * ����
	 * @param plain
	 * @param serviceid
	 * @param cmd
	 * @return
	 */
	public String decrypt(String plain,String serviceid,String cmd);

}
