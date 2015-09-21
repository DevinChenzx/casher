/*
 * @Id: GW30ExtDAO.java 20:05:19 2006-2-22
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;


import ebank.core.model.domain.GwGoods;
import ebank.core.model.domain.GwLgOptions;
import ebank.core.model.domain.GwLogistic;


/**
 * @author xiexh
 * Description: 消息通知，商品信息，配送信息
 * 
 */
public class GW30ExtDAO extends BaseDAO{
	
	/**
	 * 写入gw30模型
	 * @param gw30
	 * @throws DataAccessException
	 */
	public void saveGoods(GwGoods gw30) throws DataAccessException{
		this.getSqlMapClientTemplate().insert("gw30.goods_insert",gw30);		
	}
	
	/**
	 * @param gw31
	 * @throws DataAccessException
	 */
	public void saveLogistic(GwLogistic gw31) throws DataAccessException{
		this.getSqlMapClientTemplate().insert("gw30.logistic_insert",gw31);
	}

	
	/**
	 * @param gw31
	 * @throws DataAccessException
	 */
	public void saveLgOptions(GwLgOptions gw31) throws DataAccessException{
		this.getSqlMapClientTemplate().insert("gw30.lgoptions_insert",gw31);
	}
	
	

	public List<GwGoods> getGoodsByFk(String fk) throws DataAccessException{
		return this.getSqlMapClientTemplate().queryForList("gw30.goods_getByFk",fk);
	}
	public GwLogistic getLogisticByFk(String fk) throws DataAccessException{
		return (GwLogistic)this.getSqlMapClientTemplate().queryForObject("gw30.logistic_getByFk",fk);
	}
	public List<GwLgOptions> getLgOptionsByFk(String fk) throws DataAccessException{
		return this.getSqlMapClientTemplate().queryForList("gw30.lgoptions_getByFk",fk);
	}
	public GwLgOptions getLgOptionsByPk(String pk) 
	throws DataAccessException{
		return (GwLgOptions)this.getSqlMapClientTemplate().queryForObject("gw30.lgoptions_getByPk",pk);
	}
	
	
}
