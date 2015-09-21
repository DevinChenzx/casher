package ebank.core;

import java.util.Map;

import ebank.core.common.ServiceException;
import ebank.core.model.domain.GwViewAccount;

public interface AccountService {
	
	/**
	 * payment
	 * @param paymentid
	 * @return
	 * @throws ServiceException 
	 */
	public String tx_postpayment(String paymentid);
	
	public Map<String,Object> tx_command_action(String acctnum);
	
	public GwViewAccount getAccount(final String acctnum);
	

}
