package ebank.core.logic;

import org.springframework.dao.DataAccessException;

import ebank.core.FaqService;
import ebank.core.model.dao.GwFaqDAO;
import ebank.core.model.domain.GwFaq;

public class FaqService_Impl implements FaqService {

	private GwFaqDAO faqDAO;
	
	public void setFaqDAO(GwFaqDAO faqDAO) {
		this.faqDAO = faqDAO;
	}
	public GwFaq saveGwFaq(GwFaq gw200) throws DataAccessException {
		faqDAO.saveGwFaq(gw200);
		return gw200;
	}
	public int updateGwFaq(GwFaq gw200) throws DataAccessException {
		return faqDAO.updateGwFaq(gw200);
	}
	public GwFaq findGwFaqByTrxno(String trxno) throws DataAccessException {
		return faqDAO.findGwFaqByTrxno(trxno);
	}

}
