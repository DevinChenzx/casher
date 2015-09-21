/*
 * @Id: GW20PayOrderDAO.java 14:36:20 2006-2-24
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

import ebank.core.model.domain.GwPayments;
import ebank.core.model.domain.GwTrxs;

/**
 * @author 
 * Description: 支付结果订单
 * 
 */
public class GwTrxsDAO extends BaseDAO {
	
	/**
	 * 插入对象
	 * @param gw20
	 * @return
	 * @throws DataAccessException
	 */
	public GwTrxs saveGwTrxs(GwTrxs gw20) throws DataAccessException{		
		this.getSqlMapClientTemplate().insert("gw20.trx_insert",gw20);
		return gw20;		
	}
	public GwPayments saveGwPayments(GwPayments gw50) throws DataAccessException{
		this.getSqlMapClientTemplate().insert("gw50.payment_insert",gw50);
		return gw50;
	}
	public GwPayments findPayment(String paymentid){
		return (GwPayments)this.getSqlMapClientTemplate().queryForObject("gw50.findPaymentById",paymentid);
	}
	public GwPayments findPayment(String paynum,int type){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("paynum", paynum);
		mp.put("paytype", type);
		return (GwPayments) this.getSqlMapClientTemplate().queryForObject("gw50.findByNumAndType",mp);
	}
	public int updatePaymentResult(GwPayments payment){
		return this.getSqlMapClientTemplate().update("gw50.update_payment",payment);
	}
	public int updatePaymentBillsts(GwPayments payment){
		return this.getSqlMapClientTemplate().update("gw51.updatebillsts",payment);
	}
	public int updateTradeBase(String recepit,String sts){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("trade_no", recepit);
		mp.put("status", sts);
		return this.getSqlMapClientTemplate().update("gw51.updatetradebase",mp);
	}
	
	public List findPaymentBySts(String sts){
		return this.getSqlMapClientTemplate().queryForList("gw51.querypaymentsbysts",sts);
	}
	
	public GwTrxs findByTrxAndBk(String trxnum,String bankcode){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("trxnum", trxnum);
		mp.put("bankcode", bankcode);
		return (GwTrxs)this.getSqlMapClientTemplate().queryForObject("gw20.trx_findbyTrxnumAndBK", mp);
	}
	
	public GwTrxs findFirstSuccessTrxByFk(String fkid){
		return (GwTrxs) this.getSqlMapClientTemplate().queryForObject("gw20.trx_firstTrx", fkid);
	}
	/**
	 * 更新交易结果
	 * @param trx
	 * @return
	 */
	public int updateTrxResult(GwTrxs trx){
		return this.getSqlMapClientTemplate().update("gw20.updateTrxResult", trx);
	}
	public GwTrxs findTrxsById(String pk){
		return (GwTrxs)this.getSqlMapClientTemplate().queryForObject("gw20.trx_findPk", pk);
	}
	
	public List<GwTrxs> findTrxWithoutProc(String procname){
		return (List<GwTrxs>)this.getSqlMapClientTemplate().queryForList("gw20.trx_withoutProc", procname, 0, 100);
	}
	
	public GwTrxs findTrxsByPId(String pk){
		return (GwTrxs)this.getSqlMapClientTemplate().queryForObject("gw20.trx_firstOrderPId", pk);
	}
	
	public GwTrxs findByGwOrderIDAndServiceCode(String gworders_id,String serviceCode){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("gworders_id", gworders_id);
		mp.put("serviceCode", serviceCode);
		return (GwTrxs)this.getSqlMapClientTemplate().queryForObject("gw20.trx_findbyGwOrderIDAndSerciceCode", mp);
	}

}
