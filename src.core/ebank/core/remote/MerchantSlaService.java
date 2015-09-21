/*
 * @Id: MerchantSlaService.java 9:25:39 2007-3-15
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.remote;

import java.util.List;

import ebank.core.model.domain.GW01Service;
import ebank.core.model.domain.GwViewUser;

/**
 * @author 
 * Description: 服务开通停止维护接口
 * 
 */
public interface MerchantSlaService {
	/**
	 * @param merchantid
	 * @return 查询商户服务
	 */
	public List getServices(String merchantid);
	/**
	 * 更新或者插入服务
	 * @param gw01
	 * @return
	 */
	public void doServices(GW01Service gw01);
	
	/**
	 * 查询商户
	 * @param merchantid
	 * @return
	 */
	public GwViewUser getSlaMerchant(String merchantid);
	/**
	 * 更新商户
	 * @param gw00
	 * @return
	 */
	public int doSlaMerchant(GwViewUser gw00);

}
