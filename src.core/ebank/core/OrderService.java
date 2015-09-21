/*
 * @Id: OrderService.java 16:50:49 2006-2-11
 * 
 * @author xiexh
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core;

import java.util.List;

import ebank.core.common.ServiceException;
import ebank.core.domain.MerchantOrder;
import ebank.core.model.domain.CmCorporationInfo;
import ebank.core.model.domain.GwLgOptions;
import ebank.core.model.domain.GwLogistic;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwPayments;
import ebank.core.model.domain.GwSuborders;
import ebank.core.model.domain.GwTrxs;
import ebank.core.model.domain.TradeBase;
import ebank.core.model.domain.TrxsNum;


/**
 * @author xiexh
 * Description: 
 * 
 */
public interface OrderService {
	/**
	 * @param id
	 * @return
	 */
	public GwLgOptions findLogisticByPk(String id);
	public GwLogistic  findLogisticByFk(String fkid);
	public GwTrxs findTrxByTrxnum(String trxnum,String bankcode);
	
	public GwOrders findOrderByPk(String pk);
	
	public List<GwSuborders> findSubOrderByGwOdersId(String gwordersid); 
	
	public GwOrders findOrderByTrx(String trxnum,String bankcode);
	public boolean tx_updateTrx(GwTrxs trx,GwOrders order)  throws ServiceException;
	
	public GwTrxs findFirstTrxById(String orderpk);
	
	public GwTrxs findTrxsById(String txid);
	
	public String tx_getPayAllow(String order_id,String bankcode,String acquirer_merchant);
	
	public GwOrders findOrderByNotifyInfo(long partner,String notifyid);
	
	
	public int tx_updateOrderBuyerInfo(String id,String buyername,String buyerid);
	public int tx_updateOrderBuyerInfos(GwOrders order);
	public int tx_updateOrderBuyerContact(String id,String contact);
	public int tx_updateOrderQueryKey(GwOrders order);
	
	
	//ref by Aware service
	public List<GwTrxs> findTrxWithoutProc(String procname);
	public List<GwOrders> findOrderWithoutNotify();
	
	/**
	 * save the trx
	 * @param tx
	 * @return
	 */
	public GwTrxs tx_saveTrxs(GwTrxs tx);
	public String tx_savePostOrder(MerchantOrder order) throws ServiceException;
	
	public String tx_savePayments(GwPayments tx);
	public int tx_UpdatePayment(GwPayments payment);
	public int tx_UpdatePaymentSts(GwPayments payment);
	public GwOrders findOrderByPaymentid(String paymentid);
	public GwPayments findPaymentById(String paymentid);
	public GwPayments findPaymentByNum(String paynum,int paytype);
	public List<GwPayments> findPaymentByBillSts(String sts);
	 
	public List<TradeBase> findTradeBaseByOrderId(String orderid);
	public GwTrxs findFirstTrxByPId(String orderpk);
	public GwTrxs findByGwOrderIDAndServiceCode(String gworders_id,String serviceCode);
	

	public GwOrders queryOriOrderId(long paramLong1, String paramString, long paramLong2);
	
	  public  TrxsNum findTrxnumByOrderId(String paramString1, String paramString2);

	public CmCorporationInfo findCorporationInfoByCustomerNo(String customerNo);
	
	public int updateOrderStatus(GwOrders order);

	 /**
     * 商户当日交易金额累计
     * @param corporationInfo
     */   
	public int updateDayQutorCount(GwOrders order);
}
