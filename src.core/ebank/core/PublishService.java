/*
 * @Id: JmsService.java 14:19:18 2006-2-25
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core;

import ebank.core.domain.PayResult;
/**
 * @author xiexh
 * Description: 发布消息
 * 
 */
public interface PublishService {
	/**
	 * 发布支付结果订单
	 * @param gw20
	 */
	public void publish(PayResult gnt);

}
