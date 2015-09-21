/*
 * @Id: MerchantService_Impl.java 9:28:43 2006-2-16
 * 
 * @author xiexh
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.logic;

import java.util.List;

import ebank.core.UserService;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.model.dao.GwViewsDAO;
import ebank.core.model.domain.CmCustomerChannel;
import ebank.core.model.domain.GwChannel;
import ebank.core.model.domain.GwDirectBind;
import ebank.core.model.domain.GwModel;
import ebank.core.model.domain.GwViewAccount;
import ebank.core.model.domain.GwViewUser;



/**
 * @author xiexh
 * Description: 视图服务实现
 * 
 */
public class UserService_Impl implements UserService {	
	
	private GwViewsDAO viewsDAO;	
		

	public GwViewUser getUserWithIdAndName(String userid, String username)
			throws ServiceException {
		try {
			return viewsDAO.findWithIdAndNameNoProtocal(userid,username);
		} catch (Exception e) {
			HandleException.handle(e);
		}
		return null;
	}
	

	public boolean checkBind(long partnerid, String bind_email) {
		// TODO Auto-generated method stub
		return viewsDAO.checkBind(partnerid,bind_email);
	}


	/* (non-Javadoc)
	 * @see ebank.core.service.b2c.MerchantService#getMerchant(java.lang.String)
	 */
	public GwViewUser getViewUser(String merchantid,String servicecode) throws ServiceException {
		try {
			
			return viewsDAO.findUser(merchantid,servicecode);
		} catch (Exception e) {
			HandleException.handle(e);
		}
		return null;
	}
	
	public GwChannel getChannel(String channelCode,String payType,long amount) throws ServiceException {
		try {
			
			return viewsDAO.findChannel(channelCode,payType,amount);
		} catch (Exception e) {
			HandleException.handle(e);
		}
		return null;
	}
	
	
	public GwViewUser getViewUserWithIdAndName(String userid,String username)
			throws ServiceException {
		try {
			return viewsDAO.findWithIdAndName(userid,username);
		} catch (Exception e) {
			HandleException.handle(e);
		}
		return null;
	}

	public GwViewAccount findAccountByAuth(String username)
			throws ServiceException {
		// TODO Auto-generated method stub
		return viewsDAO.findAccountByAuth(username);
	}

	public GwViewUser findByUsername(String username) {		
		return viewsDAO.findByUsername(username);
	}

	public GwViewAccount findAccountBypwdAndAuth(String username,
			String paypwd) throws ServiceException {
		
		return viewsDAO.findAccountBypwdAndAuth(username,paypwd.toLowerCase());
	}
	
	public List<GwViewUser> findUserBylogin_recepit(String login_recepit) throws ServiceException{
		// TODO Auto-generated method stub
		return viewsDAO.findUserBylogin_recepit(login_recepit);
	}
	
	public GwModel findModelByCustomID(String id)throws ServiceException {
		return viewsDAO.findModelByCustomID(id);
	}
	
	public GwDirectBind findDirectBindByCustomerNoAndPayAccountNo(
			String customerNo, String payAccountNo) throws ServiceException {
		
		return viewsDAO.findDirectBindByCustomerNoAndPayAccountNo(customerNo, payAccountNo);
	}
	
	public List<CmCustomerChannel> findUserChannelList(String customerID) throws ServiceException{
		// TODO Auto-generated method stub
		return viewsDAO.findUserChannelList(customerID);
	}
	/**
	 * 
	 */
	public List<CmCustomerChannel> findUserChannelListByPaymentType(String customerID,String paymentType,String channel_code) throws ServiceException{
		// TODO Auto-generated method stub
		return viewsDAO.findUserChannelListByPaymentType(customerID, paymentType,channel_code);
	}
	/**
	 * 
	 */
	public List<CmCustomerChannel> findUserChannelListByChannelCode(String customerID,String channel_code) throws ServiceException{
		// TODO Auto-generated method stub
		return viewsDAO.findUserChannelListByChannelCode(customerID,channel_code);
	}
	
	public void setViewsDAO(GwViewsDAO viewUserDAO) {
		this.viewsDAO = viewUserDAO;
	}



	
}
