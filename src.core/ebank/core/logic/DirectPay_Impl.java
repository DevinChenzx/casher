package ebank.core.logic;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import beartool.Md5Encrypt;

import ebank.core.AccountService;
import ebank.core.DirectPayService;
import ebank.core.EsbService;
import ebank.core.NoticeService;
import ebank.core.OrderService;
import ebank.core.PayResultService;
import ebank.core.UserService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwPayments;
import ebank.core.model.domain.GwViewAccount;
import ebank.core.model.domain.GwViewUser;
import ebank.core.model.domain.MapiAsyncNotify;
import ebank.web.common.util.Validator;

public class DirectPay_Impl implements DirectPayService{
	public AccountService accountService;
	public OrderService orderService;
	public PayResultService payResultService;
	private UserService userService;
	private EsbService esbService;
	private NoticeService noticeService;
	
	private Log log=LogFactory.getLog(this.getClass());
	
	public GwViewAccount getAccount(String acctnum) throws ServiceException{		
		return accountService.getAccount(acctnum);		
	}
	
	public void directPay(GwPayments payment) {
		Map<String,Object> mp_order=accountService.tx_command_action(accountService.tx_postpayment(payment.getId()));
		if(mp_order!=null&&"true".equals(mp_order.get("acctcode"))){//direct payment success
			//order notify
			payment.setPaysts(Constants.PAYMENT_STS.PAID.ordinal());
			orderService.tx_UpdatePayment(payment);
			
			if(payment.getPaytype().equals(Constants.PAYMENT_PAYTYPE.GWTRX))
				esbService.send(orderService.findPaymentById(payment.getPrid()));  //send to bill
			else
				esbService.send(orderService.findPaymentById(payment.getId()));  //send to bill
			
			final GwOrders order=orderService.findOrderByPk(payment.getPaynum());			
			
						//当日交易金额累计		
			if(order.getOrdersts().equals("3")){
				log.info("商户:"+order.getPartnerid()+">>>>>>>>交易金额累计："+order.getAmount());
				orderService.updateDayQutorCount(order);
			}
			
			if(!"0".equals(order.getOrdersts())){
				if(order!=null&&(Integer.parseInt(order.getOrdersts())>0)&&!Validator.isNull(order.getNotify_url())&&!"5".equals(order.getOrdersts())){
					log.debug("DirectPay "+order.getId()+" merchant bill start ..."+order.getOrdernum());
					try {
												
						final Map mp=payResultService.mapresult(order, false);	
						
						//real time notify20111103
						new Thread(){
							public void run(){
								try {
									
									String notify_id=mp.get("notify_id").toString();

									String result=noticeService.tx_responseNotice(order.getCharsets(),order.getNotify_url(),payResultService.getNameValuePair(mp));
							    	MapiAsyncNotify asyn=new MapiAsyncNotify();
							    	asyn.setNotify_id(String.valueOf(notify_id));
							    	if(result!=null&&result.indexOf("success")<0){
							    		asyn.setStatus("processing");		    		
							    		log.info("asyn notify:"+order.getId()+" result:"+result);				    		
							    	}else{
							    		asyn.setStatus("success");
							    		log.info("syn notify:"+order.getId()+" result:"+result);
							    	}	
							    	noticeService.tx_updateNoticeStatus(asyn);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}.start();
						
					} catch (Exception e) {
						e.printStackTrace();						
					}									
				}
			}
			
		}else{
			log.error("direct pay failure: paymentid="+payment.getId());
		}
	}
	public void directPay(String trxid){
		GwPayments payment=orderService.findPaymentByNum(trxid,Constants.PAYMENT_PAYTYPE.GWTRX.ordinal());
		GwPayments payment_order=orderService.findPaymentById(payment.getPrid());
		directPay(payment_order); //composite payment
	}
	
	public String directPaybyOrder(GwOrders order,String encpwd) throws ServiceException{	
		if(order==null) throw new ServiceException(EventCode.ORDER_NOTFOUND);
		if(order!=null&&("0".equals(order.getOrdersts())||"5".equals(order.getOrdersts()))){ //待支付或失败
			GwViewUser user=userService.findByUsername(order.getBuyer_name());
			if(user==null) throw new ServiceException(EventCode.BUYER_NOTFOUND); //buyer not found
			if(!"normal".equals(user.getStatus())){
				throw new ServiceException(EventCode.BUYER_STATEILL);
			}
			
			GwViewAccount vu=userService.findAccountBypwdAndAuth(order.getBuyer_name(), 
					encpwd);
			
			if(vu==null) throw new ServiceException(EventCode.BUYER_PAYPWD_WRONG); //user null or paypass wrong
			
			GwViewAccount viewAccount=getAccount(vu.getAcct_id()+"");;
			if(viewAccount==null) throw new ServiceException("400000");
			
			//验证账户信息安全		
			if(viewAccount.getAcct_balance()<order.getAmount()){
				log.debug(viewAccount.getAcct_balance()+"<"+order.getAmount());
				throw new ServiceException("400002"); //余额不足
			}
				
			GwPayments payment=new GwPayments();
			payment.setAmount(order.getAmount());
			payment.setPayamount(order.getAmount());
			payment.setChannel(Constants.PAYMENT_CHANNEL.ACCOUNT.ordinal()+"");
			payment.setInfromacct(viewAccount.getAcct_id()+"");
			payment.setIntoacct("");		
			payment.setPaynum(order.getId());
			payment.setPaytype(Constants.PAYMENT_PAYTYPE.GWORDER.ordinal()+"");
			payment.setPaysts(Constants.PAYMENT_STS.NOT_PAY.ordinal());
			
			String allowcode=orderService.tx_getPayAllow(order.getId(),"","");
			if(!"0".equals(allowcode)){
				throw new ServiceException(allowcode);
			}	
			orderService.tx_savePayments(payment);				
			directPay(payment);		
			return payment.getId();
		}else{
			log.error("No pay for ordersts:"+order.getId());
			throw new ServiceException(EventCode.ORDER_STS_NOTPAY);
		}
	}
	/**
	 * 通过订单ID和支付密码付款
	 * @param orderid
	 * @param paypwd
	 * @throws ServiceException
	 */
	public String directPaybyOrderid(String orderid,String encpwd) throws ServiceException{			
			GwOrders order=orderService.findOrderByPk(orderid);
			return directPaybyOrder(order, encpwd);
			
	}
	
	public String WrapDirectPayByOrderid(String orderid,String encpwd){
		Map<String,Object> mp=new HashMap<String,Object>();
		JSONObject jo=new JSONObject();
		try{
			String paymentid=directPaybyOrderid(orderid,encpwd);
			mp.put("result", "00");
			mp.put("paymentid", paymentid);
			jo.putAll(mp);			
			
			return jo.toString();
		}catch(Exception ex){
			mp.put("result", "01");
			if(ex instanceof ServiceException)
				mp.put("errorcode",((ServiceException)ex).getEventID());
			else{				
				ex.printStackTrace();
				mp.put("errorcode", "100100");
			}
			jo.putAll(mp);	
			return jo.toString();
		}
	}
	
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public void setPayResultService(PayResultService payResultService) {
		this.payResultService = payResultService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setEsbService(EsbService esbService) {
		this.esbService = esbService;
	}

	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}
	
	
	
	
	
}
