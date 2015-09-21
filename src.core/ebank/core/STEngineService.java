/*
 * @Id: SynDBService.java 16:03:48 2006-3-3
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ebank.core.PublishService;
import ebank.core.common.Constants.TO_TRXSTS;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.domain.PayResult;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwTrxs;

/**
 * @author xiexh
 * Description: 更新对帐的数据
 * 
 */
public class STEngineService {	
		
	private PublishService publishService;	
	private OrderService orderService;	
	private Log log=LogFactory.getLog(this.getClass());	
	
	/**
	 * 处理交易状态，派发事件
	 * WAIT_BUYER_PAY->WAIT_SELLER_SEND_GOODS->WAIT_BUYER_CONFIRM_GOODS->TRADE_FINISHED
	 * @param result
	 */	 
	public GwTrxs post(PayResult result) throws ServiceException{
		log.debug("Post payresult tx:"+result.getTransnum()+" resultsts:"+result.getTrxsts());
		
		GwTrxs tx0;
		if(result.getTrx()==null)
			tx0=orderService.findTrxByTrxnum(result.getTransnum(), result.getBankcode());
		else tx0=result.getTrx();		
		if(tx0==null) throw new ServiceException(EventCode.ORDER_PAYNOTFOUND);
		
		GwOrders order;
		if(result.getOrder()==null)
		    order=orderService.findOrderByPk(tx0.getGworders_id());
		else order=result.getOrder();		
		
		if(result.getBankamount()!=null&&tx0.getAmount()!=Amount.getIntAmount(String.valueOf(result.getBankamount()),2)){
			throw new ServiceException(EventCode.ORDER_VALIDATE_AMOUNT);  //同步校验金额
		}
		if(result.getTrxsts()>=TO_TRXSTS.TRADE_CLOSED.ordinal()){
			result.setTrxsts(TO_TRXSTS.TRADE_CLOSED.ordinal());			
		}
		if(result.getTrxsts()<=-1){
			result.setTrxsts(TO_TRXSTS.TRADE_FAILURE.ordinal());
		}
		boolean repeatTrx=false;
		if(order!=null&&("1".equals(order.getOrdersts())||"2".equals(order.getOrdersts())||"3".equals(order.getOrdersts()))){
			repeatTrx=true;
		}
		if(result.getTrxsts()==TO_TRXSTS.TRADE_FINISHED.ordinal()){//支付成功	订单未处理		
			tx0.setTrxsts(String.valueOf(TO_TRXSTS.TRADE_FINISHED.ordinal()));
			tx0.setAcquirer_seq(result.getBanktransseq());
			tx0.setAcquirer_date(result.getBankdate());
			if("chinabank".equals(result.getBankcode())){
				tx0.setAuthcode("");	
			}else{
			tx0.setAuthcode(result.getAuthcode());
			}
			tx0.setRefnum(result.getRefnum());
			tx0.setPayer_ip(result.getPayer_ip());
			tx0.setAcquirer_msg(result.getBankresult());
			if(order!=null&&("0".equals(order.getOrdersts())||"5".equals(order.getOrdersts()))){ //待支付或失败
				if(tx0.getPayment_type().equals("0")&&tx0.getAmount()>=order.getAmount())
					order.setOrdersts("3");
				else if(tx0.getPayment_type().equals("1")&&tx0.getAmount()>=order.getAmount())
					order.setOrdersts("1");
				else if(tx0.getAmount()<order.getAmount()){					
					order.setOrdersts("0");					
				}
			}
		}
		else if(order!=null&&(result.getTrxsts()==-1||result.getTrxsts()==TO_TRXSTS.TRADE_FAILURE.ordinal())&&"0".equals(order.getOrdersts())){
			tx0.setTrxsts(String.valueOf(TO_TRXSTS.TRADE_FAILURE.ordinal()));
			if(order!=null) order.setOrdersts("5");
		}else{
			tx0.setTrxsts(String.valueOf(result.getTrxsts()));
		}
		if(!repeatTrx){
			if(orderService.tx_updateTrx(tx0,order)){
				result.setOrder(order);
				result.setTrx(tx0);				
				publishService.publish(result);	
			}
		}else{
			log.debug("repeat transaction:"+tx0.getId());
			tx0.setTrxsts(String.valueOf(result.getTrxsts()));
			orderService.tx_updateTrx(tx0,order);
		}
		return tx0;
		
	}	
	public void setPublishService(PublishService publishService) {
		this.publishService = publishService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	
}
