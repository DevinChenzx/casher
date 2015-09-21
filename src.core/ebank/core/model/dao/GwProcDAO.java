package ebank.core.model.dao;

import java.util.HashMap;
import java.util.Map;

import ebank.core.model.domain.GwProc;


public class GwProcDAO extends BaseDAO{
	public void save(GwProc proc){
		this.getSqlMapClientTemplate().insert("gw30.proc_insertWithTrxFk", proc);
	}
	public int updateProcSts(GwProc proc){
		return this.getSqlMapClientTemplate().update("gw30.proc_updatests", proc);
	}
	public GwProc getProcess(String procname,String txid){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("procname", procname);
		mp.put("gwt_id", txid);
		return (GwProc)this.getSqlMapClientTemplate().queryForObject("gw30.proc_getProc", mp);
	}

}
