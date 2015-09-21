package ebank.core.logic;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ebank.core.AccountService;
import ebank.core.EsbService;
import ebank.core.OrderService;
import ebank.core.ProcService;
import ebank.core.common.Constants;
import ebank.core.domain.PayResult;
import ebank.core.model.domain.GwPayments;
import ebank.core.model.domain.GwProc;
import ebank.core.model.domain.GwTrxs;


public class TrxPostObserver_Impl implements Observer {

	private Log log=LogFactory.getLog(this.getClass());
	private ProcService procService;
	private AccountService accountService;
	private OrderService orderService;	
	private EsbService esbService;
	
	public void update(Observable arg0, Object arg1) {
		try {			
		
			final PayResult ntx=(PayResult)arg1;
			
			if(ntx.getTrx()!=null&&ntx.getTrx().getTrxsts().equalsIgnoreCase("1")){ //trade_post_syn
				log.debug(ntx.getTransnum()+" posting trxs start ...");
				if(procService.getProcess(String.valueOf(Constants.PROC_NAME.values()[0]), ntx.getTrx().getId())!=null)
					return;
				final GwProc proc=new GwProc();
				proc.setProcid(String.valueOf(Constants.PROC_IDS.values()[0]));
				proc.setProcname(String.valueOf(Constants.PROC_NAME.values()[0]));
				proc.setProsts("1");
				proc.setGwt_id(ntx.getTrx().getId());
				procService.tx_saveProc(proc);	
				
				//remove thread logic
				log.debug("Proc Thread running on:"+proc.getGwt_id());
				GwTrxs trx=new GwTrxs();
				trx.setId(proc.getGwt_id());
				//Map<String,Object> mp=accountService.tx_posttrx(trx.getId());
				GwPayments payment=orderService.findPaymentByNum(trx.getId(), Constants.PAYMENT_PAYTYPE.GWTRX.ordinal());
				Map<String,Object> mp=accountService.tx_command_action(accountService.tx_postpayment(payment.getId()));//
				if(mp!=null&&"200".equals(mp.get("responseCode"))){
					//find payment trade
					GwPayments ps=orderService.findPaymentById(payment.getPrid());
					if(ps!=null) esbService.send(ps); //jms
					
					String result=String.valueOf(mp.get("responseResult"));
					log.debug(proc.getGwt_id()+" post result "+result);
					if(result!=null&&"true".equals(result))
						proc.setProsts("3");
					else
						proc.setProsts("2");
					String msgString=String.valueOf(mp.get("responseMsg"));
					if(!"null".equals(msgString)&&msgString.length()>250){
						msgString=msgString.substring(0,250);
					}else msgString="";
					proc.setOperresult(msgString);
					procService.tx_updateProcSts(proc);														
				}
									
					
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void setProcService(ProcService procService) {
		this.procService = procService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public void setEsbService(EsbService esbService) {
		this.esbService = esbService;
	}
	
	
	

}
