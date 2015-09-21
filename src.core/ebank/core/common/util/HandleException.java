/*
 * @Id: HandleException.java 10:22:55 2006-2-14
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.common.util;


import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;

/**
 * @author 
 * Description: 异常处理的公共类
 * 
 */
public class HandleException {
	public static void handle(Exception ex) throws ServiceException{		
		if(ex instanceof ServiceException)
			throw (ServiceException)ex;
		else{
			ex.printStackTrace();
			throw new ServiceException(EventCode.UNEXCPECTED_EXCEPTION,ex.getMessage());
		}
		
	}

}
