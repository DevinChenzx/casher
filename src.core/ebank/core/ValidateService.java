/*
 * @Id: ValidateService.java 16:53:39 2006-2-11
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core;

import ebank.core.common.ServiceException;


/**
 * @author 
 * Description: 支付验证服务
 * 
 */
public interface ValidateService {
	
	public boolean checkMoneyType(String moneytype) throws ServiceException;

}
