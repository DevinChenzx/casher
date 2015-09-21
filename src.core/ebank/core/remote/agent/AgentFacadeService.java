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
 * Description: Զ�����
 * 
 */
public interface AgentFacadeService {
	
	/**
	 * @param callback �Ƿ���Զ�̸���
	 * @param actioncode 0.������ѯ 1.�˿��ѯ 2,�˿� 3������
	 * @param gw10  ���ض�������
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
