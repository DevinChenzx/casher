/*
 * @Id: SlaAdvice.java 17:01:53 2007-2-28
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.apache.log4j.Logger;

import ebank.core.SLAService;



/**
 * @author xiexh
 * Description: 服务水平增强
 * 
 */
public class SlaAdvice implements MethodInterceptor {

	private SLAService slaservice;
	private Logger log = Logger.getLogger(this.getClass());
	
	public Object invoke(MethodInvocation method) throws Throwable {	
		/*
		log.debug("invoke the method invaction of SlaAdvice..");
		BankService bank=(BankService)method.getThis();
		BankOrder order=(BankOrder)method.getArguments()[0];
		String paytype=bank.getPaytype(); //业务类型
		if(!bank.isMultiCurrency()&&!"CNY".equalsIgnoreCase(order.getCurrency())){
			throw new ServiceException("该支付不支持除CNY外币种:"+order.getCurrency());
		}
		String slas=order.getSlas();
		if(slas==null){
		   slas=slaservice.getAllServices(order.getMerchantid());
		}
		log.info("merchantid:"+order.getMerchantid()+" sla string:"+slas);
		boolean isenabled=false;
		Sla sla=new Sla(order.getMerchantid(),slas);
		if(sla.getEnables()!=null){
			isenabled="1".equals(sla.getEnables().get(paytype))?true:false;
		}
		String ss=order.getBankID();
		if(ss.startsWith("0")){
			ss=order.getBankID().substring(1);
		}
		if(isenabled&&"0".equals(sla.getDisables().get(ss))){
			isenabled=false;
		}		
		//开通了该服务
		if(isenabled){
			return method.proceed();
		}
		return null;
		*/
		return method.proceed();
	}
	/**
	 * @param slaservice The slaservice to set.
	 */
	public void setSlaservice(SLAService slaservice) {
		this.slaservice = slaservice;
	}
	
}
