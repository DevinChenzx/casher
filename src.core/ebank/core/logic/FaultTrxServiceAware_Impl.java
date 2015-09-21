package ebank.core.logic;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ebank.core.HttpInvokeService;
import ebank.core.STEngineService;
import ebank.core.common.util.Amount;
import ebank.core.domain.PayResult;
import ebank.core.model.dao.BaseDAO;
import ebank.core.model.domain.GwFaultTrx;
import ebank.core.model.domain.GwTrxs;

public class FaultTrxServiceAware_Impl extends BaseDAO implements MessageListener,HttpInvokeService{
	protected Log log = LogFactory.getLog(getClass());
	private STEngineService engineService;

	public void onMessage(Message arg0) {
		try {	
			if(arg0 instanceof TextMessage){
				TextMessage msg=(TextMessage)arg0;
				log.info("get JMS message:"+msg.getText());
				execute();
				
			}else{
				System.out.println("wrong message type");
			}
			
		} catch (Exception e){			
			e.printStackTrace();
		}		
	}
	
	public void execute(){
		try {
			List<GwFaultTrx> li=this.getSqlMapClientTemplate().queryForList("gw30.getFaultTrxs", null, 0, 30);
			for (Iterator<GwFaultTrx> iterator = li.iterator(); iterator.hasNext();) {				
				GwFaultTrx faultTrx = (GwFaultTrx) iterator.next();
				tx_next(faultTrx.getId());
				
			}
			log.debug("FaultTrx service aware...");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public boolean tx_next(long id) {
		try {	
			log.info("httpClientInvoke by:"+id);
			GwFaultTrx faultTrx=(GwFaultTrx)this.getSqlMapClientTemplate().queryForObject("gw30.getFaultTrxsById",id);
			if(faultTrx==null){
				log.error("faulttrx id:"+id+" not found");
				return false;
			}
			log.info("starting process faultTrx:"+faultTrx.getAcquire_trxnum());
			procfault(faultTrx);
			log.info("ending process faultTrx:"+faultTrx.getAcquire_trxnum());		
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public int procfault(GwFaultTrx faultTrx) throws Exception{
		PayResult result=new PayResult(faultTrx.getAcquire_trxnum(),faultTrx.getAcquire_code(),
				new BigDecimal(Amount.getFormatAmount(String.valueOf(faultTrx.getTrxamount()),-2)),faultTrx.getChange_sts()==1?1:-1);	
		result.setAuthcode(faultTrx.getAcquire_authcode());
		result.setBanktransseq(faultTrx.getAcquire_seq());
		result.setRefnum(faultTrx.getAcquire_refnum());	
		result.setPayer_ip(faultTrx.getPayer_ip());
		result.setPayinfo(faultTrx.getAcquire_cardnum());
		GwTrxs trx=engineService.post(result);	
		if(trx!=null)
			faultTrx.setFinal_sts("SUCCESS");
		else
			faultTrx.setFinal_sts("FAILURE");
		return this.getSqlMapClientTemplate().update("gw30.updateFaultTrxs",faultTrx);
	}
	

	public int tx_batchNext(String batchnum) {
		int i=0;
		try {
			log.debug("get faultTrx by batchnum:"+batchnum);
			List<GwFaultTrx> li=this.getSqlMapClientTemplate().queryForList("gw30.getFaultTrxsByBatchnum",batchnum);
			for (Iterator<GwFaultTrx> iterator = li.iterator(); iterator.hasNext();) {
				GwFaultTrx object = (GwFaultTrx) iterator.next();
				i+=procfault(object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return i;
	}
	

	public boolean tx_publish() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setEngineService(STEngineService engineService) {
		this.engineService = engineService;
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
