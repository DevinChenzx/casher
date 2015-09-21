/*
 * @Id: OrderService_Impl.java 16:33:44 2006-2-15
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.logic;



import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ebank.core.OrderService;
import ebank.core.TradePrecardService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.domain.MerchantOrder;
import ebank.core.model.dao.GW30ExtDAO;
import ebank.core.model.dao.GwOrdersDAO;
import ebank.core.model.dao.GwTrxsDAO;
import ebank.core.model.dao.TradePrecardDAO;
import ebank.core.model.domain.GwGoods;
import ebank.core.model.domain.GwLgOptions;
import ebank.core.model.domain.GwLogistic;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwTrxs;
import ebank.core.model.domain.TradePrecard;


public class TradePrecard_Impl implements TradePrecardService {
	
	private Log log=LogFactory.getLog(this.getClass());	
	private TradePrecardDAO tradePrecardDAO;	
	public TradePrecardDAO getTradePrecardDAO() {
		return tradePrecardDAO;
	}
	public void setTradePrecardDAO(TradePrecardDAO tradePrecardDAO) {
		this.tradePrecardDAO = tradePrecardDAO;
	}
	public TradePrecard saveTradePreCard(TradePrecard tradePrecard) throws ServiceException {
		
		TradePrecard tradePrcard=tradePrecardDAO.insert(tradePrecard);
			
		return tradePrecard;
	}	
public int updateTradePreCard(TradePrecard tradePrecard) throws ServiceException {
		
	return tradePrecardDAO.update(tradePrecard);
			
		 
	}	
public TradePrecard selectTradePreCard(String merid,String id,String ordertime) throws ServiceException {
		
	return (TradePrecard)tradePrecardDAO.select(merid,id,ordertime);

	}

}
