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
 * Description: �̻�����ˮƽЭ�����
 * 
 */
public interface SLAService {
	
	
	/**
	 * Ϊ��鶩����ѡ��ͨ��	
	 * @param orderid
	 * @param indexc
	 * @return
	 * @throws ServiceException
	 */
	public  int getRouteChannel(String orderid,String indexc) throws ServiceException;
	
	public int getRouteChannelExt(String orderid,String indexc,long amount,String partnerid) throws ServiceException;
	
	/**
	 * �������������·��
	 * @param amount
	 * @param bankCode
	 * @return
	 * @throws ServiceException
	 */
	public String getRouteChannelByAmount(String amount,String bankCode,String oriChannelId,String customerId,String cardType) throws ServiceException;
	
	/**
	 * ȡͨ��	
	 */
	public GwChannel getChannel(int channelid) throws ServiceException;
	/**
	 * ȡ���̻���������з�����,��ʽ(ҵ������;ͨ������|)
	 * @param merchantID
	 * @return
	 * @throws ServiceException
	 */
	public String getChannelViews(String orderid) throws ServiceException;
	
	/**
	 * ȡ�ͻ���ͨ����ҵͨ��
	 * @param orderid
	 * @return
	 * @throws ServiceException
	 */
	public List<GwChannel> getPartnerEntChannels(String orderid) throws ServiceException; 
	
	public GwMerChannelRate findMerChannelRateByAcquireIndexc(String acquireIndexc);

}
