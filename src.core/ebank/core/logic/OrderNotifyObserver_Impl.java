/*
 * @Id: MerchantAutoBill_Impl.java 14:43:02 2006-2-27
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.logic;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import beartool.Md5Encrypt;
import ebank.core.NoticeService;
import ebank.core.PayResultService;
import ebank.core.UserService;
import ebank.core.domain.PayResult;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwViewUser;
import ebank.core.model.domain.MapiAsyncNotify;
import ebank.web.common.util.Validator;


/**
 * @author xiexh
 * Description: 订单通知对帐观察者
 * 
 */
public class OrderNotifyObserver_Impl implements Observer {

	private Log log=LogFactory.getLog(this.getClass());	
	private PayResultService payResultService;
	private NoticeService noticeService;
	private UserService userService;
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable arg0, Object arg1) {	
		try {
			final PayResult ntx=(PayResult)arg1;
			GwOrders order=ntx.getOrder();
			if(order!=null&&(Integer.parseInt(order.getOrdersts())>0)&&!Validator.isNull(order.getNotify_url())){
				log.debug("Notify:"+order.getNotify_url()+" ORDERID:"+order.getId()+" merchant trx notify start ..."+order.getOrdernum());
				
				Map mp=payResultService.mapresult(order, false);				
				
				String notify_id=mp.get("notify_id").toString();
				
//				if("chinabank".equals(ntx.getBankcode())){
//						mp.clear();
//						GwViewUser user=userService.getViewUser(String.valueOf(order.getPartnerid()),"online");
//						String fastPaySign=Md5Encrypt.md5(ntx.getAuthcode().toString()+user.getMd5Key(), order.getCharsets());
//						mp.clear();
//						mp.put("resp","<data>"+ntx.getAuthcode()+"<sign>"+fastPaySign+"</sign></data>");
//				}
				//real time notify20111103
				String result=noticeService.tx_responseNotice(order.getCharsets(),order.getNotify_url(),payResultService.getNameValuePair(mp));
		    	MapiAsyncNotify asyn=new MapiAsyncNotify();
		    	asyn.setNotify_id(notify_id);
		    	if(result!=null&&result.indexOf("success")<0){
		    		asyn.setStatus("processing");		    		
		    		log.info("asyn notify:"+order.getId());				    		
		    	}else{
		    		asyn.setStatus("success");
		    		log.info("syn notify:"+order.getId()+" result:"+result);
		    	}		    	
		    	noticeService.tx_updateNoticeStatus(asyn);
			}else{
				log.info("No Notify:order is null or order.getNotify_url() is null or order.getOrdersts() <=0 ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	
	

	public void setUserService(UserService userService) {
		this.userService = userService;
	}



	public void setPayResultService(PayResultService payResultService) {
		this.payResultService = payResultService;
	}

	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}
	

	
	

}
