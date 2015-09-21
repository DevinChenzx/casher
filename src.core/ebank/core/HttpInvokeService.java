package ebank.core;

import java.util.Map;

public interface HttpInvokeService {
	/**
	 * Ä£ÄâJMSÍ¨Öª
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public boolean tx_next(long id);
	
	public int tx_batchNext(String batchnum);
	
	public boolean tx_publish();	
	
	public Map abc_query(String tOrderNo, String tQueryType);
	
	public String abc_refund(String tOrderNo,String  tNewOrderNo, long tTrxAmount);
	
	public String abc_getSettleList(long tSettleDate);

}
