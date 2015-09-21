/*
 * @Id: OrderService.java 16:50:49 2006-2-11
 * 
 * @author xiexh
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core;

import org.springframework.dao.DataAccessException;

import ebank.core.model.domain.GwFaq;

/**
 * @author xiexh
 * Description: 
 * 
 */
public interface FaqService {
	public GwFaq saveGwFaq(GwFaq gw200) throws DataAccessException;
	public int updateGwFaq(GwFaq gw200) throws DataAccessException;
	public GwFaq findGwFaqByTrxno(String trxno) throws DataAccessException;
}
