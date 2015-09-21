package ebank.core.logic;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

import ebank.core.EsbService;
import ebank.core.OrderService;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwPayments;
import ebank.core.model.domain.TradeBase;

/**
 * @author kitian Description: 发送到JMS到结算后台
 * 
 */
public class JMSBill_Impl implements EsbService {
	private Log log = LogFactory.getLog(this.getClass());

	private JmsTemplate jmsTemplate;
	private TransactionTemplate txTemplate;
	private OrderService orderService;
	private PooledExecutor threadPool;

	private boolean enable;

	public void send(final GwPayments payment) {
		final GwOrders order = orderService.findOrderByPaymentid(payment
				.getId());
		log.info("esb:" + payment.getId() + " enable:" + enable);
		if (enable&&order!=null){
			if("10".equals(order.getRoyalty_type())){
				log.info("royalty order do nothing:"+payment.getId());
				return;
			}else{
				log.info("order trx:"+payment.getId());
				List<TradeBase> list=orderService.findTradeBaseByOrderId(order.getId());
				for (Iterator<TradeBase> iterator = list.iterator(); iterator.hasNext();) {
					final TradeBase trade = (TradeBase) iterator.next();			
			try {
				if(threadPool==null){
					new Thread(){
						public void run(){
									thread_exec(trade,payment);
						}
					}.start();
				}else
					threadPool.execute(new Runnable() {
						public void run() {
							try {
										thread_exec(trade,payment);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
	
						}
					});
			} catch (Exception ex) {
				ex.printStackTrace();
			}
				}
			}	
		}

	}
	
	public void thread_exec(final TradeBase trade,final GwPayments payment){
		txTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(
					TransactionStatus ts) {
				try {
					log.info("JMS working on:"
							+ trade.getTradeNo()+"\n tradeDate :"+new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.SSS")
							.format(payment
									.getPaytime()
									)+"\n billDate :"+new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss.SSS")
									.format(trade.getLastUpdated()));
					jmsTemplate.send(new ActiveMQQueue(
							"settle"),
							new MessageCreator() {
								public Message createMessage(
										Session session)
										throws JMSException {
									javax.jms.MapMessage message = session
											.createMapMessage();
									message.setString(
											"srvCode",
											"online");
									message.setString(
											"tradeCode",
											trade.getTradeType());
									message.setString(
											"customerNo",
											String.valueOf(trade.getPayeeCustNo()));
									message.setLong(
											"amount",
											trade.getAmount());
									message.setString(
											"seqNo",
											trade.getTradeNo());
									message.setString(
											"tradeDate",
											new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss.SSS")
													.format(payment
															.getPaytime()
															));
									message.setString(
											"billDate",
											new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss.SSS")
													.format(trade.getLastUpdated()));
									message.setString(
											"channel",
											payment.getModes());

									return message;
								}
							});
					payment.setBillsts("Y"); // marked yes
					orderService
							.tx_UpdatePaymentSts(payment);

				} catch (Exception e) {
					e.printStackTrace();
					ts.setRollbackOnly();
				}
				return null;
			}

		});
	}
	
	public void thread_exec(final GwOrders order,final GwPayments payment){
		txTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(
					TransactionStatus ts) {
				try {
					log.info("JMS working on:"
							+ order.getId()+"\n tradeDate2 :"+new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.SSS")
							.format(order
									.getClosedate()
									)+"\n billDate2 :"+new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss.SSS")
									.format(payment
											.getPaytime()));
					jmsTemplate.send(new ActiveMQQueue(
							"settle"),
							new MessageCreator() {
								public Message createMessage(
										Session session)
										throws JMSException {
									javax.jms.MapMessage message = session
											.createMapMessage();
									message.setString(
											"srvCode",
											"online");
									message.setString(
											"tradeCode",
											"payment");
									message.setString(
											"customerNo",
											order.getSeller_id());
									message.setLong(
											"amount",
											order.getAmount());
									message.setString(
											"seqNo",
											order.getId());
									message.setString(
											"tradeDate",
											new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss.SSS")
													.format(order
															.getClosedate()));
									message.setString(
											"billDate",
											new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss.SSS")
													.format(payment
															.getPaytime()));

									return message;
								}
							});
					payment.setBillsts("Y"); // marked yes
					orderService
							.tx_UpdatePaymentSts(payment);

				} catch (Exception e) {
					e.printStackTrace();
					ts.setRollbackOnly();
				}
				return null;
			}

		});
	}

	/**
	 * @param jmsTemplate
	 *            The jmsTemplate to set.
	 */
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setTxTemplate(TransactionTemplate txTemplate) {
		this.txTemplate = txTemplate;
	}

	/**
	 * @param orderService
	 *            The orderService to set.
	 */
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setThreadPool(PooledExecutor threadPool) {
		this.threadPool = threadPool;
	}
	

}
