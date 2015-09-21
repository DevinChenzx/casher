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
 * Description: 回调接口
 * 
 */
public interface VelocityTemplateCallback {
	/**
	 * 
	 * @param mp context 参数
	 * @param other 0:模板名称 1:字符编码
	 */
	public void execute(Map<String,Object> mp,Object[] other);

}
