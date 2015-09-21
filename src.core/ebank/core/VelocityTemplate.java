/*
 * @Id: VelocityTemplate.java 20:24:54 2006-4-29
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core;

/**
 * @author 
 * Description: 模板服务
 * 
 */
public interface VelocityTemplate {
	/**
	 * 模板Merge后信息
	 * @param callback
	 * @return
	 */
	public String getMessage(VelocityTemplateCallback callback);

}
