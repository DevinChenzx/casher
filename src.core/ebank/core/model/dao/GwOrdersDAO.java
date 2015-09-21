/*
 * @Id: GW00MerchantOrderDAO.java 14:37:12 2006-2-15
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import ebank.core.model.domain.CmCorporationInfo;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwSuborders;
import ebank.core.model.domain.TradeBase;
import ebank.core.model.domain.TrxsNum;
import ebank.web.common.util.Validator;


/**
 * @author xiexh
 * Description: 商户订单dao

 * 
 */
public class GwOrdersDAO extends BaseDAO{
	
	/**
	 * 插入商户订单

	 * @param g00
	 * @return
	 */
	public GwOrders insert(GwOrders g00) throws DataAccessException{		
		String pk=(String)this.getSqlMapClientTemplate().insert("gw10.orders_insert",g00);
		g00.setId(pk);
		return g00;
	}
	
	public GwSuborders insertSubOrder(GwSuborders sub) throws DataAccessException{		
		String pk=(String)this.getSqlMapClientTemplate().insert("gw10.subOrders_insert",sub);
		sub.setId(pk);
		return sub;
	}
	
	/**
	 * 主键查询订单

	 * @param pk
	 * @return
	 * @throws DataAccessException
	 */
	public GwOrders findByPk(String pk)  throws DataAccessException{
		return (GwOrders)this.getSqlMapClientTemplate().queryForObject("gw10.order_selectByPk", pk);
	}
	/**
	 * 交易号关联订单
	 * 查询合单支付总单对应的明细订单
	 * @param gwordersId
	 * @return
	 * @throws DataAccessException
	 */
	public List<GwSuborders> findSubOrderByGwOdersId(String gwordersId)  throws DataAccessException{
		return this.getSqlMapClientTemplate().queryForList("gw10.subOrder_selectByOrderId", gwordersId);
	}
	
	/**
	 * 根据合单明细单的收款账号跟邮箱查询出状态为支付完成的主单
	 * @param orderno
	 * @param email
	 * @return
	 * @throws DataAccessException
	 */
	public List<GwOrders> findByCodeAndNo(String orderno,String email)  throws DataAccessException{
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("outtradeno", orderno);
		mp.put("seller_code", email);
		return this.getSqlMapClientTemplate().queryForList("gw10.order_selectOrdersByCodeAndNo", mp);
	}
	/**
	 * 交易号关联订单
	 * @param trxnum
	 * @param bankcode
	 * @return
	 */
	public GwOrders findByTrx_Bankcode(String trxnum,String bankcode){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("trxnum", trxnum);
		mp.put("bankcode", bankcode);
		return (GwOrders) this.getSqlMapClientTemplate().queryForObject("gw10.order_selectwithTrxAndBk", mp);
	}
	/**
	 * 伙伴号，订单号查询订单

	 * @param partnerid
	 * @param ordernum
	 * @return
	 */
	public GwOrders findPartnerOrder(long partnerid,String ordernum){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("partnerid", partnerid);
		mp.put("ordernum", ordernum);
		return (GwOrders) this.getSqlMapClientTemplate().queryForObject("gw10.findPartnerOrder", mp);
	}
	
	public int countPaidOrder(long partnerid,String ordernum,long amount){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("partnerid", partnerid);
		mp.put("ordernum", ordernum);
		mp.put("amount",amount);		
		return (Integer) this.getSqlMapClientTemplate().queryForObject("gw10.countPartnerOrder", mp);
	}
	
	public int updateOrderBuyerContact(String orderid,String contact){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("id", orderid);
		mp.put("buyer_contact", contact);
		return this.getSqlMapClientTemplate().update("gw10.updateBuyerContact",mp);
	}
	/**
	 * 通知号，伙伴号查询订单

	 * @param partnerid
	 * @param notifyid
	 * @return
	 */
	public GwOrders findByNotifyInfo(long partnerid,String notifyid){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("partnerid", partnerid);
		mp.put("notifyid", notifyid);
		return (GwOrders) this.getSqlMapClientTemplate().queryForObject("gw10.findOrderByNotifyInfo", mp);
	}
	/**
	 * 更新订单支付结果

	 * @param order
	 * @return
	 */
	public int updatePayResult(GwOrders order){
		return this.getSqlMapClientTemplate().update("gw10.updateOrderPayResult", order);
	}
	/**
	 * @param orderseq 订单序列号

	 * @return
	 * @throws DataAccessException
	 */
	public GwOrders getBySeq(String orderseq) throws DataAccessException{
		return (GwOrders)getSqlMapClientTemplate().queryForObject("gw10.select0",orderseq);
	}
	/**
	 * 判断是否能够产生交易

	 * @param id
	 * @param acquirecode
	 * @param acquiremerchant
	 * @return
	 * @throws DataAccessException
	 */
	public String getPayDecision(String id,String acquirecode,String acquiremerchant) throws DataAccessException{
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("id", id);
		mp.put("acquirecode", acquirecode);
		mp.put("acquiremerchant", acquiremerchant);
		mp.put("result", "");
		log.info(mp);
		this.getSqlMapClientTemplate().update("gw10.getOrderDecision",mp);
		return (String)mp.get("result");
	}
	
	public int updateOrderBuyerInfo(String id,String buyername,String buyerid){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("id", id);
		mp.put("buyer_name", buyername);
		if(Validator.isNull(buyerid)){
			mp.put("buyer_id", "");
		}
		return this.getSqlMapClientTemplate().update("gw10.updateBuyerInfo",mp);
	}
	public int updateOrderBuyerInfo(GwOrders order){
		return this.getSqlMapClientTemplate().update("gw10.updateBuyerInfos",order);
	}
	public List<GwOrders> findOrderWithoutNotify(){
		return this.getSqlMapClientTemplate().queryForList("gw10.withoutNotify", null, 0, 100);
	}
	public GwOrders findByPaymentid(String paymentid){
		return (GwOrders)this.getSqlMapClientTemplate().queryForObject("gw10.findOrderByPaymentID", paymentid);
	}
	//TODO LAST
    public List<TradeBase> findTradeBaseByOrderId(String orderid){
		return this.getSqlMapClientTemplate().queryForList("gw10.findTradeBaseByOrderId",orderid);
	}

	public int updateOrderQueryKey(GwOrders order){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("id", order.getId());
		mp.put("query_key", order.getQuery_key());
		return this.getSqlMapClientTemplate().update("gw10.updateBuyerQueryKey",mp);
	}
	
	  public TrxsNum findTrxnumByOrderId(String id, String bankcode)
	  {
	    Map mp = new HashMap();
	    mp.put("id", id);
	    mp.put("bankcode", bankcode);
	    return (TrxsNum)getSqlMapClientTemplate().queryForObject("gw10.order_findTrxnum", mp);
	  }

	public CmCorporationInfo findCorporationInfoByCustomerNo(String customerNo){
		return (CmCorporationInfo)this.getSqlMapClientTemplate().queryForObject("gw10.findCorporationInfoByCustomerNo", customerNo);
	}

	public int updateOrderStatus(GwOrders order){
		return this.getSqlMapClientTemplate().update("gw10.updateOrderStatus",order);
	}
	
	public int updateDayQutorCount(CmCorporationInfo cmCorporationInfo){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("customerNo",cmCorporationInfo.getCustomerNo());	
		mp.put("dayQutorCount",cmCorporationInfo.getDayQutorCount());
		return this.getSqlMapClientTemplate().update("gw10.updateDayQutorCount",mp);
	}
}
