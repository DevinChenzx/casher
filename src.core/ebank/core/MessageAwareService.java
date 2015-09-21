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
 * Description: 接收JMS发送过来的消息
 * 
 */
public interface MessageAwareService {
	/**
	 * 接收JMS发送的消息
	 * @return
	 */
	public List getMessages();

}
