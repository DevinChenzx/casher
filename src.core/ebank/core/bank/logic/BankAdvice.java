/*
 * @Id: BankAdvice.java 16:45:11 2006-8-25
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;



import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;


import ebank.core.bank.BankService;

/**
 * @author 
 * Description: 银行接口功能增强
 * 
 */
public class BankAdvice implements MethodInterceptor {

	private Logger log = Logger.getLogger(this.getClass());
	/* (non-Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation method) throws Throwable {		
		log.debug("invoke the method invaction of bankAdvice..");
		BankService bank=(BankService)method.getThis();
		if(bank.isEnabled()){
			return method.proceed();
		}
		return null;
	}

}
