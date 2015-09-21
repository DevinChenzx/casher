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
 * Description: 商户订单服务
 * 
 */
public interface MerchantOrderService {
	
	/**
	 * 插入订单
	 * @param gw10
	 * @return
	 * @throws ServiceException
	 */
	public GwOrders insertOrder(GwOrders gw10) throws ServiceException;
	
	/**
	 * 更新订单状态
	 * @param gw10
	 * @throws ServiceException
	 */
	public void thread_updateOrderState(GwOrders gw10) throws ServiceException;
	
	/**
	 * 仅更新对帐状态
	 * @param gw10
	 * @throws ServiceException
	 */
	public void thread_updateSynBankState(GwOrders gw10)throws ServiceException;
		
	
	/**
	 * 批量更新未支付订单
	 * @param servicecode
	 * @param opername
	 * @param syndesc
	 * @throws ServiceException
	 */
	public void thread_batchUpdateDeadOrder(String servicecode,String opername,String syndesc) throws ServiceException;
	
	
	
	/**
	 * 查询需要和商户自动对帐的订单
	 * @return
	 * @throws ServiceException
	 */
	public List getMerchantAutoBillOrder(int maxfetch) throws ServiceException;
	
	/**
	 * 查询对帐银行订单
	 * @param inflg
	 * @param gw10
	 * @return
	 * @throws ServiceException
	 */
	public List getSynOrder(boolean inflg,GwOrders gw10) throws ServiceException;
	
	
	

}
