package ebank.core.logic;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ebank.core.HttpInvokeService;
import ebank.core.OrderService;
import ebank.core.PayResultService;
import ebank.core.PublishService;
import ebank.core.common.Constants;
import ebank.core.common.util.Amount;
import ebank.core.domain.PayResult;

import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwTrxs;
import ebank.web.common.util.Validator;

public class PublishServiceAware_Impl implements HttpInvokeService{
	private PublishService publishService;
	private OrderService orderService;
	private PayResultService payResultService;
	private JMSBillService_Aware_Impl jmsAwareService;
	
	private Log log=LogFactory.getLog(this.getClass());
	
	public void run(){
		log.info("PublishAwareService is running ..");
		List<GwTrxs> trxs=orderService.findTrxWithoutProc(String.valueOf(Constants.PROC_NAME.values()[0]));
		for (Iterator<GwTrxs> iterator = trxs.iterator(); iterator.hasNext();) {
			try {		
				GwTrxs gwTrxs = (GwTrxs) iterator.next();
				GwOrders order=null;
				if(!Validator.isNull(gwTrxs.getGworders_id())){
					order=orderService.findOrderByPk(gwTrxs.getGworders_id());
				}
				if("1".equals(gwTrxs.getTrxsts())){
					PayResult result=new PayResult(gwTrxs.getTrxnum(),
							                       gwTrxs.getAcquirer_code(),
							                       new BigDecimal(Amount.getIntAmount(String.valueOf(gwTrxs.getAmount()),-2)),1);
					result.setTrx(gwTrxs);
					result.setOrder(order);
					publishService.publish(result);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<GwOrders> orders=orderService.findOrderWithoutNotify();
		for (Iterator<GwOrders> iterator = orders.iterator(); iterator.hasNext();) {
			try {			
				GwOrders gwOrders = (GwOrders) iterator.next();
				
				if(gwOrders!=null&&gwOrders.getNotify_url()!=null){
					log.debug("Aware "+gwOrders.getId()+" merchant bill start ..."+gwOrders.getOrdernum());
					payResultService.mapresult(gwOrders,true);					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	

	public boolean tx_next(long id) {
		// TODO Auto-generated method stub
		return false;
	}


	public int tx_batchNext(String batchnum) {
		// TODO Auto-generated method stub
		return 0;
	}


	public boolean tx_publish() {
		//do publish trx job
		this.run();
		//do jms bill job
		jmsAwareService.onMessage(null);
		return true;
	}


	public void setPublishService(PublishService publishService) {
		this.publishService = publishService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public void setPayResultService(PayResultService payResultService) {
		this.payResultService = payResultService;
	}


	public void setJmsAwareService(JMSBillService_Aware_Impl jmsAwareService) {
		this.jmsAwareService = jmsAwareService;
	}
	
	
	public Map abc_query(String tOrderNo, String tQueryType){
    	return null;
    }
	
	public String abc_refund(String tOrderNo,String  tNewOrderNo, long tTrxAmount){
		return "";
	}
	
	public String abc_getSettleList(long tSettleDate){
		return "";
	}
	
	

}
