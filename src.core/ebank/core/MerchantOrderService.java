/*
 * @Id: MerchantOrderService.java 16:25:51 2006-2-15
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core;

import java.util.List;


import ebank.core.common.ServiceException;
import ebank.core.model.domain.GwOrders;


/**
 * @author 
 * Description: �̻���������
 * 
 */
public interface MerchantOrderService {
	
	/**
	 * ���붩��
	 * @param gw10
	 * @return
	 * @throws ServiceException
	 */
	public GwOrders insertOrder(GwOrders gw10) throws ServiceException;
	
	/**
	 * ���¶���״̬
	 * @param gw10
	 * @throws ServiceException
	 */
	public void thread_updateOrderState(GwOrders gw10) throws ServiceException;
	
	/**
	 * �����¶���״̬
	 * @param gw10
	 * @throws ServiceException
	 */
	public void thread_updateSynBankState(GwOrders gw10)throws ServiceException;
		
	
	/**
	 * ��������δ֧������
	 * @param servicecode
	 * @param opername
	 * @param syndesc
	 * @throws ServiceException
	 */
	public void thread_batchUpdateDeadOrder(String servicecode,String opername,String syndesc) throws ServiceException;
	
	
	
	/**
	 * ��ѯ��Ҫ���̻��Զ����ʵĶ���
	 * @return
	 * @throws ServiceException
	 */
	public List getMerchantAutoBillOrder(int maxfetch) throws ServiceException;
	
	/**
	 * ��ѯ�������ж���
	 * @param inflg
	 * @param gw10
	 * @return
	 * @throws ServiceException
	 */
	public List getSynOrder(boolean inflg,GwOrders gw10) throws ServiceException;
	
	
	

}
