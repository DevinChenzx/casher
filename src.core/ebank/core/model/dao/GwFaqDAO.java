package ebank.core.model.dao;

import org.springframework.dao.DataAccessException;

import ebank.core.model.domain.GwFaq;

public class GwFaqDAO extends BaseDAO 
{
	/**
	 * ≤Â»Î∂‘œÛ
	 * @param gw200
	 * @return
	 * @throws DataAccessException
	 */
	public GwFaq saveGwFaq(GwFaq gw200) throws DataAccessException{		
		this.getSqlMapClientTemplate().insert("gw200.faq_insert",gw200);
		return gw200;		
	}
	
	public int updateGwFaq(GwFaq gw200) throws DataAccessException{		
		return this.getSqlMapClientTemplate().update("gw200.faq_update",gw200);
	}
	
	public GwFaq findGwFaqByTrxno(String trxno) throws DataAccessException{		
		return (GwFaq) this.getSqlMapClientTemplate().queryForObject("gw200.faq_findByTrxno",trxno);
	}
}
