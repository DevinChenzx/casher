/*
 * @Id: MessageAwareService.java 13:18:15 2006-6-9
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core;

import java.util.List;

/**
 * @author 
 * Description: ����JMS���͹�������Ϣ
 * 
 */
public interface MessageAwareService {
	/**
	 * ����JMS���͵���Ϣ
	 * @return
	 */
	public List getMessages();

}
