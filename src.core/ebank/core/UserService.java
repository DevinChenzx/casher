/*
 * @Id: MerchantService.java 13:19:25 2006-2-15
 * 
 * @author xiexh
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core;


import java.util.List;

import ebank.core.common.ServiceException;
import ebank.core.model.domain.CmCustomerChannel;
import ebank.core.model.domain.GwChannel;
import ebank.core.model.domain.GwDirectBind;
import ebank.core.model.domain.GwModel;
import ebank.core.model.domain.GwViewAccount;
import ebank.core.model.domain.GwViewUser;

/**
 * @author xiexh
 * Description: 商户服务
 * 
 */
public interface UserService {
	
	/**
	 * @param merchantid
	 * @return
	 * @throws ServiceException
	 */
	public GwViewUser getViewUser(String userid,String servicecode) throws ServiceException;
	
	public GwViewAccount findAccountBypwdAndAuth(String username,String paypwd) throws ServiceException;
	
	public GwViewAccount findAccountByAuth(String username) throws ServiceException;
	
	public GwViewUser getViewUserWithIdAndName(String username,String userid) throws ServiceException;
		
	public GwViewUser findByUsername(String username);
	
	public GwChannel getChannel(String channelCode,String payType,long amount) throws ServiceException;
	
	public GwViewUser getUserWithIdAndName(String username,String userid) throws ServiceException;
	
	public boolean checkBind(long partnerid,String bind_email);
	
	public List<GwViewUser> findUserBylogin_recepit(String login_recepit)throws ServiceException;
	
	public GwModel findModelByCustomID(String id) throws ServiceException;
	
	public GwDirectBind findDirectBindByCustomerNoAndPayAccountNo(String customerNo, String payAccountNo) throws ServiceException;
	
	public List<CmCustomerChannel> findUserChannelList(String customerId) throws ServiceException;
	
	public List<CmCustomerChannel> findUserChannelListByPaymentType(String customerId,String paymentType,String channel_code)throws ServiceException;
	
	public List<CmCustomerChannel> findUserChannelListByChannelCode(String customerId,String channel_code) throws ServiceException;
	
}
