/*
 * @Id: GWSynLogDAO.java 9:34:52 2006-5-12
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;


import ebank.core.model.domain.GWSynLog;

/**
 * @author 
 * Description: 记录招行对帐日志
 * 
 */
public class GWSynLogDAO extends BaseDAO {
	
	public boolean isExist(String transnum,String amount,String bankseq) throws DataAccessException{
		GWSynLog log=new GWSynLog();
		log.setTransnum(transnum);
		log.setTransamount(amount);
		log.setBankseq(bankseq);
		Integer i=(Integer)this.getSqlMapClientTemplate().queryForObject("gwsynlog.counts",log);
		log=null;
		if(i!=null&&i.intValue()>=1) return true;
		return false;
	}
	public void insert(GWSynLog log) throws DataAccessException{
		this.getSqlMapClientTemplate().insert("gwsynlog.insert",log);
	}
	public int update(GWSynLog log) throws DataAccessException{
		return this.getSqlMapClientTemplate().update("gwsynlog.update",log);
	}
	public List getSynlog() throws DataAccessException{
		return this.getSqlMapClientTemplate().queryForList("gwsynlog.select",null);
	}
	public int removelog(String transnum) throws DataAccessException{
		return this.getSqlMapClientTemplate().delete("gwsynlog.delete",transnum);
	}
	public List getPagedLog(String state, String fromdate, String todate, int pageindex, int pagesize, int[] pagetotal) throws DataAccessException{
		Map mp=new HashMap();
		mp.put("transstate",state==null?"":state);
		mp.put("fromdate",fromdate==null?"":fromdate);
		mp.put("todate",todate==null?"":todate);		
		if(pagetotal[0]==0){
			Integer size=(Integer)this.getSqlMapClientTemplate().queryForObject("gwsynlog.pagedcount",mp);
			pagetotal[0]=size==null?0:size.intValue();
		}	
		mp.put("first",new Integer((pageindex-1)*pagesize));
		mp.put("last",new Integer((pageindex-1)*pagesize+pagesize));		
		return this.getSqlMapClientTemplate().queryForList("gwsynlog.pagedselect",mp);
	}
}
