package ebank.core.model.dao;

import ebank.core.model.domain.MapiAsyncNotify;

public class GwNotifyDAO extends BaseDAO{	

	public Long saveAsyNotify(MapiAsyncNotify asyn){
		return (Long) this.getSqlMapClientTemplate().insert("gw40.insert_asynNotify", asyn);
	}
	public int updateAsyNotify(MapiAsyncNotify asyn){
		return this.getSqlMapClientTemplate().update("gw40.update_asynNotify",asyn);
	}

}
