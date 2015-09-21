/*
 * @Id: CryptService.java 13:30:52 2006-2-18
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core;

import ebank.core.common.ServiceException;

/**
 * @author 
 * Description: �ӽ��ܷ���
 * 
 */
public interface CryptService {
	/**
	 * ��������
	 * @param strOrig
	 * @param merchantid
	 * @param mode
	 * @return
	 */
	public String getCryptMsg(String strOrig,String merchantid,String mode) throws ServiceException;
	
	/**
	 * ǩ������
	 * @param strOrig
	 * @param merchantid
	 * @param mode
	 * @return
	 */
	public String getSignMsg(String strOrig,String merchantid,String mode) throws ServiceException;
	
	/**
	 * ��������
	 * @param strEncode
	 * @param sign
	 * @param version
	 * @return
	 */
	public String getCertDecodeMsg(String strEncode,String mode) throws ServiceException;
	
	/**
	 * ��֤ǩ��
	 * @param merchantid
	 * @param mode
	 * @param strEncode 
	 * @param sign 
	 * @return
	 */
	public boolean verfiysign(String merchantid,String mode,String strEncode,String sign);

}
