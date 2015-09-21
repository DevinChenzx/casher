/*
 * @Id: AgentFacadeService.java 11:08:59 2006-12-13
 * 
 * @author 
 * @version 1.0
 * payment_agent PROJECT
 */
package ebank.core.remote.agent;

import ebank.core.model.domain.GwOrders;

/**
 * @author 
 * Description: 远程外观
 * 
 */
public interface AgentFacadeService {
	
	/**
	 * @param callback 是否做远程更新
	 * @param actioncode 0.订单查询 1.退款查询 2,退款 3，对帐
	 * @param gw10  网关订单对象
	 * @return
	 */
	public AgentTransaction execute(boolean callback,String actioncode,GwOrders gw10); 
	/**
	 * @param callback
	 * @param actioncode
	 * @param orderseq
	 * @return
	 */
	public AgentTransaction findAndExecute(boolean callback,String actioncode,String orderseq);

}
