/*
 * @Id: SLAService_Impl.java 18:16:00 2006-2-25
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.logic;

import java.util.List;

import ebank.core.SLAService;
import ebank.core.common.ServiceException;
import ebank.core.model.dao.GwServiceDAO;
import ebank.core.model.domain.GwChannel;
import ebank.core.model.domain.GwMerChannelRate;

/**
 * @author xiexh
 * Description: 服务水平实现
 * 
 */
public class SLAService_Impl implements SLAService {
	
	private GwServiceDAO gw01DAO;
	
	public GwChannel getChannel(int channelid) throws ServiceException {		
		return gw01DAO.getChannelById(channelid);
	}
	
	public int getRouteChannelExt(String orderid, String indexc, long amount,
			String partnerid) throws ServiceException {		
		return gw01DAO.getRouteChannelExt(orderid, indexc, amount, partnerid);
	}
	
	
	@Override
	public String getRouteChannelByAmount(String amount, String bankCode,String oriChannelId,String customerId,String cardType)
			throws ServiceException {
		return gw01DAO.getRouteChannelByAmount(amount, bankCode,oriChannelId,customerId,cardType);
		
	}

	
	public List<GwChannel> getPartnerEntChannels(String orderid)
			throws ServiceException {		
		return gw01DAO.getEntChannels(orderid);
	}

	public String getChannelViews(String orderid) throws ServiceException {		
		return gw01DAO.getChannelViews(orderid);
	}
	public int getRouteChannel(String orderid, String indexc)
			throws ServiceException {		
		return gw01DAO.getRouteChannel(orderid, indexc);
	}

	public GwMerChannelRate findMerChannelRateByAcquireIndexc(String acquireIndexc) {
		return gw01DAO.findMerChannelRateByAcquireIndexc(acquireIndexc);
	}
	public GwServiceDAO getGw01DAO() {
		return gw01DAO;
	}
	public void setGw01DAO(GwServiceDAO gw01dao) {
		gw01DAO = gw01dao;
	}
	
	
		

}
