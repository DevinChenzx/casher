/*
 * @Id: PayResultService.java 14:03:10 2006-5-11
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core;

import java.util.Map;
import org.apache.commons.httpclient.NameValuePair;


import ebank.core.common.ServiceException;
import ebank.core.domain.PayResult;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwTrxs;
import ebank.core.model.domain.TradePrecard;

/**
 * @author xiexh
 * Description: 支付结果处理
 * 
 */
public interface PayResultService {
	
	public Map mapresult(PayResult bean,Map<String,Object> mp,boolean notify) throws ServiceException;
	/**
	 * 取支付结果命名配对
	 * @param bean
	 * @return
	 */
	public NameValuePair[] getNameValuePair(Map<String,String> bean);
	
	public Map mapresult(GwOrders order,boolean notify) throws ServiceException;
	public Map mapPrecardResult(TradePrecard tradePrecard,boolean notify) throws ServiceException;


}
