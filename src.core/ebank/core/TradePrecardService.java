/*
 * @Id: OrderService.java 16:50:49 2006-2-11
 * 
 * @author xiexh
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core;
import ebank.core.common.ServiceException;
import ebank.core.model.domain.TradePrecard;


/**
 * @author xiexh
 * Description: �̻������������
 * 
 */
public interface TradePrecardService {
	
	public TradePrecard saveTradePreCard(TradePrecard tradePrecard) throws ServiceException;
	public TradePrecard selectTradePreCard(String merid,String id,String ordertime) throws ServiceException;
	public int updateTradePreCard(TradePrecard tradePrecard) throws ServiceException;
}
