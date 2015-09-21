/*
 * @Id: RouteService.java ионГ09:56:56 2009-2-12
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank;

import ebank.core.common.ServiceException;

public interface RouteService {
	public String route(String bankid,String merchantid,String amount,String cardnum,String exp) 
	throws ServiceException;
	public boolean isEnableAdvice();
}
