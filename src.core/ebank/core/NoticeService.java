/*
 * @Id: MerchantBillService.java 14:53:36 2006-2-27
 * 
 * @author xiexh
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core;




import org.apache.commons.httpclient.NameValuePair;

import ebank.core.common.ServiceException;
import ebank.core.model.domain.MapiAsyncNotify;

/**
 * @author xiexh
 * Description: 商户自动对帐服务
 * 
 */
public interface NoticeService {
	
	public String tx_responseNotice(String charset,String addresss,NameValuePair[] vp) throws ServiceException;	
	
	
	public Long tx_saveAsynNotice(MapiAsyncNotify man) throws ServiceException;
	
	public boolean tx_updateNoticeStatus(MapiAsyncNotify notice) throws ServiceException;
	
	
	
}
