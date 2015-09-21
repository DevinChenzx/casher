/*
 * @Id: JmsService_Impl.java 16:17:44 2006-2-25
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ebank.core.OrderService;
import ebank.core.PublishService;
import ebank.core.domain.PayResult;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwTrxs;


/**
 * @author xiexh
 * Description: 通知发布服务
 * 
 */
public class PublishService_Impl extends Observable implements PublishService  {

	private Log log=LogFactory.getLog(this.getClass());
	private List<Observer> subs= new ArrayList<Observer>();	
	private DirectPay_Impl directPayService;
	private OrderService orderService;	
	

	/* (non-Javadoc)
	 * @see ebank.core.service.JmsService#publish(ebank.core.model.domain.GW20PayOrder)
	 */
	public void publish(PayResult tx) {
		log.info("ORDER_ID:"+ tx.getTransnum()+" send to publish ...");
		//new directpay
		GwOrders order=tx.getOrder();
		GwTrxs trx=tx.getTrx();
		notifyObservers(tx);
		
		if(order!=null&&trx!=null&&"1".equals(trx.getTrxsts())){
			if("0".equals(order.getOrdersts())&&(trx.getAmount()<order.getAmount())&&"balancePay".equals(order.getPaymethod())){
				//direct pay
				log.info("composite payment logic .."+order.getId());
				directPayService.directPay(trx.getId());			
			}
		}
				
		//当日交易金额累计		
		if(order.getOrdersts().equals("3")){
			log.info("商户:"+order.getPartnerid()+">>>>>>>>交易金额累计："+order.getAmount());
			orderService.updateDayQutorCount(order);
		}
	}	
	
	/*** 
	 * @param sub
	 */
	public void addListener(Observer sub) {
		 this.subs.add(sub);
    }
	
	/*** 
	 * @param sub
	 */
	public void removeListener(Observer sub) {
		if (this.subs.contains(sub)){ 
			this.subs.remove(sub);
		}                             
	}
	
	private void notifyObservers(final PayResult tx){
		for (Iterator<Observer> iter = subs.iterator(); iter.hasNext();) {
			Observer element = (Observer) iter.next();
			element.update(this,tx);			
		}
	}	

	public void setSubs(List<Observer> subs) {
		this.subs = subs;
	}

	public void setDirectPayService(DirectPay_Impl directPayService) {
		this.directPayService = directPayService;
	}
	
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	
	

}
