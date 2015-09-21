/*
 * @Id: AccessService.java 13:40:48 2006-2-17
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core;


import ebank.core.common.ServiceException;
import ebank.core.domain.MerchantOrder;


/**
 * @author 
 * Description: 商户客户端接入网关
 * 
 */
public interface AccessService {
	/**
	 * 解析商户客户端提交的xml格式数据
	 * @param xml	 
	 * @return
	 * @throws ServiceException
	 */
	public  MerchantOrder resovleXml(String xml) throws ServiceException;
	
	/**
	 * 创建解析规则
	 * 
	 */
	public void addRules();

}
