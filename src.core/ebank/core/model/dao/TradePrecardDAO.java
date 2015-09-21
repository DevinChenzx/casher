/*
 * @Id: GW00MerchantOrderDAO.java 14:37:12 2006-2-15
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.model.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import ebank.core.model.domain.TradePrecard;

public class TradePrecardDAO extends BaseDAO{
	

	public TradePrecard insert(TradePrecard precard) throws DataAccessException{
		String pk=(String)this.getSqlMapClientTemplate().insert("tradeprecard_insert",precard);
		precard.setId(pk);
		return precard;
	}
	public TradePrecard select(String merchantid,String id,String ordertime) throws DataAccessException{
		Map<String, Object> mp=new HashMap<String, Object>();
		mp.put("merid", merchantid);
		mp.put("outtradeno", id);
		mp.put("ordertime", ordertime);
		
		return (TradePrecard) this.getSqlMapClientTemplate().queryForObject("tradeprecard_select",mp);
	}
	public int update(TradePrecard precard) throws DataAccessException{
		return this.getSqlMapClientTemplate().update("tradeprecard_update",precard);
		
	}
	
}
