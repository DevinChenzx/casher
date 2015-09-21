/*
 * @Id: GW00MerchantDAO.java 14:20:49 2006-2-16
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.model.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import ebank.core.model.domain.CmCustomerChannel;
import ebank.core.model.domain.GwChannel;
import ebank.core.model.domain.GwDirectBind;
import ebank.core.model.domain.GwModel;
import ebank.core.model.domain.GwViewAccount;
import ebank.core.model.domain.GwViewUser;
import ebank.web.common.util.Validator;

/**
 * @author xiexh
 * Description: 商户基本信息DAO
 * 
 */
public class GwViewsDAO extends BaseDAO {
	public boolean checkBind(long partnerid,String bind_email){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("customer_no",String.valueOf(partnerid));
		mp.put("bind_email",bind_email);
		Integer x=(Integer)this.getSqlMapClientTemplate().queryForObject("gw00.findRoyaltyUserBind",mp);
		return (x!=null&&x>0)?true:false;
	}
	/**
	 * 查询网关上商户信息
	 * @param merchantid
	 * @return
	 * @throws DataAccessException
	 */
	public GwViewUser findUser(String merchantid,String servicecode) throws DataAccessException{
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("customer_no",merchantid);
		mp.put("service_code",servicecode);
		List<GwViewUser> li=this.getSqlMapClientTemplate().queryForList("gw00.findUser",mp);	
		if(li!=null){
			for (Iterator<GwViewUser> iterator = li.iterator(); iterator.hasNext();) {
				GwViewUser object = (GwViewUser) iterator.next();
				return object;				
			}
		}
		return null;
	}	
	
	
	/**
	 * 查询渠道信息
	 * @param merchantid
	 * @return
	 * @throws DataAccessException
	 */
	public GwChannel findChannel(String channelCode,String payType,long amount) throws DataAccessException{
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("channel_code",channelCode);
		mp.put("pay_type",payType);
		mp.put("amount",amount);
		List li=this.getSqlMapClientTemplate().queryForList("gw006.findChannel",mp);	
		if(li!=null&&li.size()>=1){
			return (GwChannel)li.get(0);
		}
		return null;
	}		
	/**
	 * 查询账户是否存在
	 * @param login_recepit
	 * @return
	 * @throws DataAccessException
	 */
	public List<GwViewUser> findUserBylogin_recepit(String login_recepit) throws DataAccessException{
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("login_recepit",login_recepit);
		List<GwViewUser> li=this.getSqlMapClientTemplate().queryForList("gw00.findUserBylogin_recepit",mp);	
		if(li!=null){
			return li;
		}
		return null;
	}	
	
	public GwViewUser findWithIdAndName(String userid,String username) throws DataAccessException{
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("login_recepit", Validator.isNull(username)?"":username);
		mp.put("customer_no", Validator.isNull(userid)?"":userid);
		mp.put("service_code", "online");		
		List li=this.getSqlMapClientTemplate().queryForList("gw00.findUserWithNameID", mp);
		if(li!=null&&li.size()>=1){
			return (GwViewUser)li.get(0);
		}
		return null;
	}
	public GwViewUser findWithIdAndNameNoProtocal(String userid,String username) 
	throws DataAccessException{	
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("login_recepit", Validator.isNull(username)?"":username);
		mp.put("customer_no", Validator.isNull(userid)?"":userid);		
		return (GwViewUser) this.getSqlMapClientTemplate().queryForObject("gw00.findUserWithNameIDNoService", mp);
	}
	
	public GwViewUser findByUsername(String username){
		return (GwViewUser)this.getSqlMapClientTemplate().queryForObject("gw00.findUserByName", username);
	}
	public GwViewAccount findAccountBypwdAndAuth(String username,String paypwd){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("username", username);
		mp.put("paypwd", paypwd);
		return (GwViewAccount)this.getSqlMapClientTemplate().queryForObject("gw00.findAcctByUserAndPwd", mp);
	}
	public GwViewAccount findAccountByAuth(String username){		
		return (GwViewAccount)this.getSqlMapClientTemplate().queryForObject("gw00.findAcctByUser", username);
 
	}
	/**
	 * 查询账户是否开通了合单支付功能
	 * @param id
	 * @return
	 */
	public GwModel findModelByCustomID(String id)throws DataAccessException{		
		return (GwModel)this.getSqlMapClientTemplate().queryForObject("gw00.findModelByCustomID", id);
 
	}
	
	/**
	 * 查询定向支付绑定的商旅卡的付款客户号与收款客户号是否已绑定
	 * @param customerNo 收款客户号
	 * @param accountNo 商旅卡的付款账号
	 * @return
	 * @throws DataAccessException
	 */
	public GwDirectBind findDirectBindByCustomerNoAndPayAccountNo(String customerNo, String payAccountNo)throws DataAccessException{
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("customerNo", customerNo);
		mp.put("payAccountNo", payAccountNo);
		return (GwDirectBind)this.getSqlMapClientTemplate().queryForObject("gw003.findDirectBindByCustomerNoAndPayAccountNo", mp);
 
	}
	/**
	 * 根据用户ID查找该用户的所有渠道
	 * @param customerId
	 * @return
	 * @throws DataAccessException
	 */
	public List<CmCustomerChannel> findUserChannelList(String customerId) throws DataAccessException{
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("customerId",customerId);
		List<CmCustomerChannel> li=this.getSqlMapClientTemplate().queryForList("gw00.findUserChannel",mp);	
		if(li!=null){
			return li;
		}
		return null;
	}	
	
	public List<CmCustomerChannel> findUserChannelListByPaymentType(String customerId,String paymentType,String channel_code) throws DataAccessException{
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("customerId",customerId);
		mp.put("paymentType",paymentType);
		mp.put("channelCode",channel_code);
		List<CmCustomerChannel> li=this.getSqlMapClientTemplate().queryForList("gw00.findUserChannelByPaymentType",mp);	
		if(li!=null){
			return li;
		}
		return null;
	}	
	public List<CmCustomerChannel> findUserChannelListByChannelCode(String customerId,String channel_code) throws DataAccessException{
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("customerId",customerId);
		mp.put("channelCode",channel_code);
		List<CmCustomerChannel> li=this.getSqlMapClientTemplate().queryForList("gw00.findUserChannelByChannelCode",mp);	
		if(li!=null){
			return li;
		}
		return null;
	}	
}
