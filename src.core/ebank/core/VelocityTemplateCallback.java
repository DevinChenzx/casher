/*
 * @Id: VelocityTemplateCallback.java 20:26:20 2006-4-29
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core;

import java.util.Map;

/**
 * @author 
 * Description: �ص��ӿ�
 * 
 */
public interface VelocityTemplateCallback {
	/**
	 * 
	 * @param mp context ����
	 * @param other 0:ģ������ 1:�ַ�����
	 */
	public void execute(Map<String,Object> mp,Object[] other);

}
