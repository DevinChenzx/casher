package ebank.core.logic;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import ebank.core.EsbService;
import ebank.core.OrderService;
import ebank.core.model.domain.GwPayments;

public class JMSBillService_Aware_Impl implements MessageListener{

	private Log log=LogFactory.getLog(this.getClass());

	private OrderService orderService;
	private EsbService esbService;
	public void onMessage(Message arg0) {
		log.info("JMSAware is running on:"+new Date());
		List<GwPayments> list=orderService.findPaymentByBillSts("N");
		for (Iterator<GwPayments> iterator = list.iterator(); iterator.hasNext();) {
			GwPayments payment = (GwPayments) iterator.next();
			log.info("JMSAware working on:"+payment.getId());
			esbService.send(payment);		
		}
		if(list!=null) log.info("List size:"+list.size());
		
	}
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	public void setEsbService(EsbService esbService) {
		this.esbService = esbService;
	}
	
	

}
