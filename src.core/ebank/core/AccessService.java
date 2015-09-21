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
 * Description: �̻��ͻ��˽�������
 * 
 */
public interface AccessService {
	/**
	 * �����̻��ͻ����ύ��xml��ʽ����
	 * @param xml	 
	 * @return
	 * @throws ServiceException
	 */
	public  MerchantOrder resovleXml(String xml) throws ServiceException;
	
	/**
	 * ������������
	 * 
	 */
	public void addRules();

}
