/*
 * @Id: OrderService_Impl.java 16:33:44 2006-2-15
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.logic;



import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ebank.core.OrderService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.domain.MerchantOrder;
import ebank.core.model.dao.GW30ExtDAO;
import ebank.core.model.dao.GwOrdersDAO;
import ebank.core.model.dao.GwTrxsDAO;
import ebank.core.model.dao.GwViewsDAO;
import ebank.core.model.domain.CmCorporationInfo;
import ebank.core.model.domain.GwGoods;
import ebank.core.model.domain.GwLgOptions;
import ebank.core.model.domain.GwLogistic;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwPayments;
import ebank.core.model.domain.GwSuborders;
import ebank.core.model.domain.GwTrxs;
import ebank.core.model.domain.GwViewUser;
import ebank.core.model.domain.TradeBase;
import ebank.core.model.domain.TrxsNum;

/**
 * @author xiexh
 * Description: ¶©µ¥·þÎñ
 * 
 */
public class OrderService_Impl implements OrderService {
	
	private Log log=LogFactory.getLog(this.getClass());

	private GwOrdersDAO orderDAO;
	private GW30ExtDAO extDAO;
	private GwTrxsDAO trxDAO;	
	private GwViewsDAO viewsDAO;	
	private OrderService orderService;			
	

	public String tx_savePostOrder(MerchantOrder order)
			throws ServiceException {
		GwOrders g0=orderDAO.findPartnerOrder(order.getOrders().getPartnerid(),order.getOrders().getOrdernum());
		if(g0!=null){
			//throw new ServiceException(EventCode.ORDER_ISEXIST);
			int count=orderDAO.countPaidOrder(order.getOrders().getPartnerid(), 
					                 		  order.getOrders().getOrdernum(),
                                              order.getOrders().getAmount());
			if(count>0){
				throw new ServiceException("501127");
			}
			if(g0.getAmount()==order.getOrders().getAmount()) return g0.getId();		
		}
		GwOrders gw0=orderDAO.insert(order.getOrders());
		log.debug("generator orderid:"+gw0.getId());		
		if(gw0!=null&&"12".equals(gw0.getRoyalty_type())){
			List<GwSuborders>  li = order.getSubOrdersList();
			if(li!=null&&li.size()>0)
			for (Iterator<GwSuborders> iterator = li.iterator(); iterator.hasNext();) {
				GwSuborders object = (GwSuborders) iterator.next();	
				object.setGwordersid(gw0.getId());				
				 List orderlist = orderDAO.findByCodeAndNo(object.getOuttradeno(), object.getSeller_code());
				 if(orderlist!=null&&orderlist.size()>0){
					 throw new ServiceException("501127");
				 }				
				List<GwViewUser> viewList = viewsDAO.findUserBylogin_recepit(object.getSeller_code());
				for (Iterator<GwViewUser> iterator2 = viewList.iterator(); iterator2.hasNext();) {
					GwViewUser viewObj = (GwViewUser) iterator2.next();	
					if("online".equals(viewObj.getService_code())){
						object.setSeller_name(viewObj.getUserAlias());
						object.setSeller_custno(viewObj.getCustomer_no());
						break;
					}
				}
				GwSuborders suborder=orderDAO.insertSubOrder(object);
			}
		}
		if(order.getGoodsList()!=null)
		for (Iterator<GwGoods> iterator = order.getGoodsList().iterator(); iterator.hasNext();) {
			GwGoods	goods = (GwGoods) iterator.next();
			goods.setGworders_id(gw0.getId());
			extDAO.saveGoods(goods);			
		}
		if(order.getLgoptionList()!=null)
			for (Iterator<GwLgOptions> iterator = order.getLgoptionList().iterator(); iterator.hasNext();) {
				GwLgOptions type = (GwLgOptions) iterator.next();
				type.setGworders_id(gw0.getId());
				extDAO.saveLgOptions(type);
			}
		if(order.getLogistic()!=null){
			order.getLogistic().setGworders_id(gw0.getId());
			extDAO.saveLogistic(order.getLogistic());
		}		
		return gw0.getId();
	}
	public boolean tx_updateTrx(GwTrxs trx, GwOrders order) throws ServiceException{
		if(order!=null)
		   orderDAO.updatePayResult(order);
		int j=trxDAO.updateTrxResult(trx);		
		if(j==1){			
			return true;
		}else{
			throw new ServiceException(EventCode.TRX_PROCESSFAILURE);
		}		
	}	
	
	public int tx_updateOrderBuyerInfo(String id,String buyername,String buyerid){
		return orderDAO.updateOrderBuyerInfo(id,buyername,buyerid);
	}
	
	
	public int tx_updateOrderBuyerInfos(GwOrders order) {		
		return orderDAO.updateOrderBuyerInfo(order);
	}
	
	public int tx_updateOrderBuyerContact(String id, String contact) {
		
		return orderDAO.updateOrderBuyerContact(id,contact);
	}
	public GwTrxs tx_saveTrxs(GwTrxs tx) {
		GwTrxs txs=trxDAO.saveGwTrxs(tx);
		GwPayments payment=new GwPayments();
		payment.setPaynum(txs.getId());
		payment.setAmount(tx.getAmount());
		payment.setInfromacct(tx.getFromacctnum());
		payment.setPayamount(tx.getAmount());
		payment.setPaytype(Constants.PAYMENT_PAYTYPE.GWTRX.ordinal()+"");
		payment.setPrid(tx.getPaymentid());
		payment.setPayinfo(tx.getTrxnum());
		payment.setChannel(Constants.PAYMENT_CHANNEL.EBANK.ordinal()+"");
		payment.setPaysts(Constants.PAYMENT_STS.NOT_PAY.ordinal());
		trxDAO.saveGwPayments(payment);	
		return txs;
	}	

	public int tx_UpdatePayment(GwPayments payment) {		
		int j=trxDAO.updatePaymentResult(payment);
		if(1==payment.getPaysts()){
			GwOrders order=orderDAO.findByPaymentid(payment.getId());
		    order.setOrdersts(Constants.FROM_TRXSTS.TRADE_FINISHED.ordinal()+"");
			orderDAO.updatePayResult(order);
			orderDAO.updatePayResult(order);			
			trxDAO.updateTradeBase(payment.getPaynum(),"completed");			
		}
		return j;
	}
	
	public int tx_UpdatePaymentSts(GwPayments payment) {
		int j=trxDAO.updatePaymentBillsts(payment);
		return j;
	}
	public String tx_savePayments(GwPayments tx) {		
		GwPayments payment=trxDAO.saveGwPayments(tx);
		if(payment!=null) return payment.getId();
		else return null;
	}
	
	public GwOrders findOrderByPaymentid(String paymentid) {		
		return orderDAO.findByPaymentid(paymentid);
	}
	public GwPayments findPaymentById(String paymentid) {		
		return trxDAO.findPayment(paymentid);
	}
	
	public GwPayments findPaymentByNum(String paynum, int paytype) {		
		return trxDAO.findPayment(paynum,paytype);
	}
	
	public List<GwPayments> findPaymentByBillSts(String sts) {
		// TODO Auto-generated method stub
		return trxDAO.findPaymentBySts(sts);
	}
	public GwTrxs findFirstTrxById(String orderpk) {
		
		return trxDAO.findFirstSuccessTrxByFk(orderpk);
	}
	
	

	public List<GwOrders> findOrderWithoutNotify() {		
		return orderDAO.findOrderWithoutNotify();
	}
	public List<GwTrxs> findTrxWithoutProc(String procname) {
		
		return trxDAO.findTrxWithoutProc(procname);
	}
	public GwLogistic findLogisticByFk(String fkid) {		
		return extDAO.getLogisticByFk(fkid);
	}

	public GwLgOptions findLogisticByPk(String id) {		
		return extDAO.getLgOptionsByPk(id);
	}

	public GwOrders findOrderByPk(String pk) {
		return orderDAO.findByPk(pk);
	}
	
	public List<GwSuborders> findSubOrderByGwOdersId(String gwordersid) {
		return orderDAO.findSubOrderByGwOdersId(gwordersid);
	}


	public GwOrders findOrderByTrx(String trxnum, String bankcode) {
		// TODO Auto-generated method stub
		return orderDAO.findByTrx_Bankcode(trxnum,bankcode);
	}

	public GwTrxs findTrxByTrxnum(String trxnum, String bankcode) {		
		return trxDAO.findByTrxAndBk(trxnum,bankcode);
	}	
	
	

	public List<TradeBase> findTradeBaseByOrderId(String orderid) {		
		return orderDAO.findTradeBaseByOrderId(orderid);
	}
	public GwTrxs findTrxsById(String txid) {
		
		return trxDAO.findTrxsById(txid);
	}
	public GwOrders findOrderByNotifyInfo(long partner, String notifyid) {		
		return orderDAO.findByNotifyInfo(partner,notifyid);
	}

	public String tx_getPayAllow(String id, String bankcode,
			String acquirerMerchant) {		
		return orderDAO.getPayDecision(id,bankcode,acquirerMerchant);
	}
	
	public GwTrxs findFirstTrxByPId(String orderpk) {
		
		return trxDAO.findTrxsByPId(orderpk);
	}
	
	public void setOrderDAO(GwOrdersDAO orderDAO) {
		this.orderDAO = orderDAO;
	}

	public void setTrxDAO(GwTrxsDAO trxDAO) {
		this.trxDAO = trxDAO;
	}

	public void setExtDAO(GW30ExtDAO extDAO) {
		this.extDAO = extDAO;
	}
	public int tx_updateOrderQueryKey(GwOrders order) {
		return orderDAO.updateOrderQueryKey(order);
	}
	public void setViewsDAO(GwViewsDAO viewsDAO) {
		this.viewsDAO = viewsDAO;
	}
	public GwTrxs findByGwOrderIDAndServiceCode(String gworders_id,
			String serviceCode) {
		return trxDAO.findByGwOrderIDAndServiceCode(gworders_id, serviceCode);
	}

	public GwOrders queryOriOrderId(long merchantId, String orderNum, long amount)
	  {
	    GwOrders g0 = this.orderDAO.findPartnerOrder(merchantId, orderNum);
	    if (g0 != null)
	    {
	      int count = this.orderDAO.countPaidOrder(merchantId, orderNum, amount);
	      if (count > 0) {
	        return null;
	      }
	      if (g0.getAmount() == amount) return g0; return null;
	    }
	    return null;
	  }

	
	  public TrxsNum findTrxnumByOrderId(String id, String bankcode) {
		    return this.orderDAO.findTrxnumByOrderId(id, bankcode);
		  }

	public CmCorporationInfo findCorporationInfoByCustomerNo(String customerNo){
		return orderDAO.findCorporationInfoByCustomerNo(customerNo);
	}
	
	public int updateOrderStatus(GwOrders order){
		return orderDAO.updateOrderStatus(order);
	}

	
	public int updateDayQutorCount(GwOrders order) {
		CmCorporationInfo corporationInfo = new CmCorporationInfo();
		corporationInfo.setCustomerNo(order.getPartnerid()+"");
		corporationInfo.setDayQutorCount(BigDecimal.valueOf(order.getAmount()).divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_HALF_UP).doubleValue());		
	    return orderDAO.updateDayQutorCount(corporationInfo);
	}		
	
	

}
