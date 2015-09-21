/*
 * @Id: SLAService.java 15:59:57 2006-2-13
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core;

import java.util.List;

import ebank.core.common.ServiceException;
import ebank.core.model.domain.GwChannel;
import ebank.core.model.domain.GwMerChannelRate;


/**
 * @author xiexh
 * Description: 商户服务水平协议服务
 * 
 */
public interface SLAService {
	
	
	/**
	 * 为伙伴订单号选择通道	
	 * @param orderid
	 * @param indexc
	 * @return
	 * @throws ServiceException
	 */
	public  int getRouteChannel(String orderid,String indexc) throws ServiceException;
	
	public int getRouteChannelExt(String orderid,String indexc,long amount,String partnerid) throws ServiceException;
	
	/**
	 * 根据渠道金额做路由
	 * @param amount
	 * @param bankCode
	 * @return
	 * @throws ServiceException
	 */
	public String getRouteChannelByAmount(String amount,String bankCode,String oriChannelId,String customerId,String cardType) throws ServiceException;
	
	/**
	 * 取通道	
	 */
	public GwChannel getChannel(int channelid) throws ServiceException;
	/**
	 * 取得商户定义的所有服务项,格式(业务类型;通道编码|)
	 * @param merchantID
	 * @return
	 * @throws ServiceException
	 */
	public String getChannelViews(String orderid) throws ServiceException;
	
	/**
	 * 取客户开通的企业通道
	 * @param orderid
	 * @return
	 * @throws ServiceException
	 */
	public List<GwChannel> getPartnerEntChannels(String orderid) throws ServiceException; 
	
	public GwMerChannelRate findMerChannelRateByAcquireIndexc(String acquireIndexc);

}
