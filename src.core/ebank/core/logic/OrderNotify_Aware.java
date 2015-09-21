/*
 * @Id: MerchantAutoBill_Aware.java 15:23:59 2006-4-13
 * 
 * @author xiexh
 * payment_core PROJECT
 */
package ebank.core.logic;

import java.util.Iterator;
import java.util.List;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import ebank.core.MerchantOrderService;
import ebank.core.NoticeService;
import ebank.core.common.Constants;
import ebank.core.domain.PayResult;
import ebank.core.model.domain.GwOrders;


/**
 * @author xiexh
 * Description: 商户自动定时对帐
 * 
 */
public class OrderNotify_Aware{
	private Log log=LogFactory.getLog(this.getClass());
	private boolean run=false;  //
	private int maxfetch=10;
	private long pausetimes=1; //一个间隔
	private long maxtimes=3;  //最大三次间隔不工作
	
	private boolean decision=false;
	private long temp=this.pausetimes;
	
	
	private NoticeService merchantAutoBillService;
	private MerchantOrderService merchantOrderServcie;
	
	/* (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message arg){
		if(arg!=null){
			if(arg instanceof TextMessage){
				TextMessage msg=(TextMessage) arg;
				try{
					if(msg.getText().indexOf("run")>=0) run=true;
					if(msg.getText().indexOf("stop")>=0) run=false;
				}catch(Exception ex){
					ex.printStackTrace();
					run=false;
				}
			}
		}
		execute();		
	}
	
	/**
	 * 执行自动对帐商户过程
	 */
	public boolean execute(){
		/*
		try{
			if(temp>maxtimes){ //重新初始化 
				decision=true;
				temp=pausetimes;
			}
			if(run&&decision){				
				List li=merchantOrderServcie.getMerchantAutoBillOrder(maxfetch);
				if(log.isDebugEnabled()){
					log.debug("synch merchant start...");
					log.debug("merchant auto fetch order list size="+li.size());
				}
				if(li!=null&&li.size()>0){
					for (Iterator iter = li.iterator(); iter.hasNext();) {
						GwOrders element = (GwOrders)iter.next();
						PayResult result=new PayResult(element.getTransnum(),element.getCreatedate());
						//订单已经支付
						result.setResultsucc(Constants.STATE_STR_OK.equals(element.getOrderstate())?true:false);
						result.setBankamount(element.getOrderprice());
						result.setMerchant(element.getMerchant());
						result.setOrder(element);
						merchantAutoBillService.thread_PostTo(result,element.getGw20().getSynmerchantflg());						
					}
				}else{
					decision=false;
				}
			}else{
				temp+=pausetimes;
				log.info("merchant auto syn stopped...,pausetimes="+this.temp);
			}			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		*/
		return true;
	}
	/**
	 * @return Returns the run.
	 */
	public boolean isRun() {
		return run;
	}
	/**
	 * @param run The run to set.
	 */
	public void setRun(boolean run) {
		this.run = run;
	}

	/**
	 * @param merchantAutoBillService The merchantAutoBillService to set.
	 */
	public void setMerchantAutoBillService(
			NoticeService merchantAutoBillService) {
		this.merchantAutoBillService = merchantAutoBillService;
	}

	/**
	 * @param merchantOrderServcie The merchantOrderServcie to set.
	 */
	public void setMerchantOrderServcie(MerchantOrderService merchantOrderServcie) {
		this.merchantOrderServcie = merchantOrderServcie;
	}

	/**
	 * @param decision The decision to set.
	 */
	public void setDecision(boolean decision) {
		this.decision = decision;
	}

	/**
	 * @param maxfetch The maxfetch to set.
	 */
	public void setMaxfetch(int maxfetch) {
		this.maxfetch = maxfetch;
	}

	/**
	 * @param pausetimes The pausetimes to set.
	 */
	public void setPausetimes(long pausetimes) {
		this.pausetimes = pausetimes;
	}

	/**
	 * @param maxtimes The maxtimes to set.
	 */
	public void setMaxtimes(long maxtimes) {
		this.maxtimes = maxtimes;
	}
	
	

}
