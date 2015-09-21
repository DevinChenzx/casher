package ebank.web.common.util;

import javax.servlet.http.HttpServletRequest;

import ebank.core.common.ServiceException;
import ebank.core.domain.MerchantOrder;

public interface IPartnerInterface {

	public MerchantOrder getMerchantOrderByService(HttpServletRequest request) throws ServiceException;	
	
}
