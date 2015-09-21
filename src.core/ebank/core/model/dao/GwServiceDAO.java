/*
 * @Id: GW01ServiceDAO.java 9:53:04 2006-3-14
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

import ebank.core.model.domain.GwChannel;
import ebank.core.model.domain.GwMerChannelRate;

/**
 * @author xiexh
 * Description: 服务DAO
 * 
 */
public class GwServiceDAO extends BaseDAO {
		
	public int getRouteChannel(String orderid,String indexc){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("orderid",orderid);
		mp.put("indexc", indexc);
		mp.put("channelid","");
		this.getSqlMapClientTemplate().update("gw01.getRouteChannel",mp);
		
		return Integer.parseInt(String.valueOf(mp.get("channelid")));
	}
	public int getRouteChannelExt(String orderid,String indexc,long amount,String partnerid){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("orderid",orderid);
		mp.put("indexc", indexc);
		mp.put("amount", amount);
		mp.put("partner", partnerid);
		mp.put("channelid","");
		this.getSqlMapClientTemplate().update("gw01.getRouteChannelEXT",mp);
		
		return Integer.parseInt(String.valueOf(mp.get("channelid")));
	}
	
	
	public String getRouteChannelByAmount(String amount,String bankCode,String oriChannelId,String customerId,String cardType){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("amount", amount);
		mp.put("bankCode", bankCode);
		mp.put("oriChannelId", oriChannelId);
		mp.put("customerId",customerId);
		mp.put("cardType", cardType);
		mp.put("channelId","");
		this.getSqlMapClientTemplate().update("gw01.getRouteChannelByAmonut",mp);
	
		return String.valueOf(mp.get("channelId"));
	}
	public String getChannelViews(String orderid) throws DataAccessException{
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("orderid",orderid);
		mp.put("slaresult","");
		this.getSqlMapClientTemplate().update("gw01.getChannelViews",mp);
		return String.valueOf(mp.get("slaresult"));
	}
	
	public GwChannel getChannelById(int id){
		return (GwChannel)this.getSqlMapClientTemplate().queryForObject("gw01.getChannelById", id);
	}
	
	public List<GwChannel> getEntChannels(String orderid){
		return this.getSqlMapClientTemplate().queryForList("gw01.getEntChannel",orderid);
	}
	/**
	 * 查询消费卡商户手续费费费率设定
	 * @param 
	 * @return
	 * @throws DataAccessException
	 */
	public GwMerChannelRate findMerChannelRateByAcquireIndexc(String acquireIndexc) throws DataAccessException{
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("acquireIndexc", acquireIndexc);
		return (GwMerChannelRate)this.getSqlMapClientTemplate().queryForObject("gw004.findMerChannelRateByAcquireIndexc", mp);
 
	}
	
	
}
