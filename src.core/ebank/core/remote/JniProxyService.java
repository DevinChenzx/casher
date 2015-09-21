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
 * Description: 接口使用jni的代理
 * 
 */
public interface JniProxyService {
	/**
	 * 根据服务ID取得加密数据
	 * @param plain
	 * @param serviceid
	 * @param cmd 
	 * @return
	 */
	public String getCrypt(String plain,String serviceid,String cmd);
	
	/**
	 * 验证签名数据
	 * @param plain
	 * @param serviceid
	 * @return
	 */
	public boolean verify(String plain,String serviceid,String cmd);
	
	/**
	 * 解密
	 * @param plain
	 * @param serviceid
	 * @param cmd
	 * @return
	 */
	public String decrypt(String plain,String serviceid,String cmd);

}
