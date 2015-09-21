/*
 * @Id: SequenceService.java 18:17:58 2006-2-13
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core;

import ebank.core.common.ServiceException;

/**
 * @author 
 * Description: ���з���
 * 
 */
public interface SequenceService {
	
	/**
	 * ����ȱʡ���к�ȡ����
	 * @return
	 * @throws ServiceException
	 */
	public String getCode() throws ServiceException;
	
	/**
	 * ָ����������ȡ����
	 * @param seqname
	 * @return
	 * @throws ServiceException
	 */
	public String getCode(String seqname) throws ServiceException;
	
	/**
	 * ���ӷ������ȡ����
	 * @param servciecode ���ڲ���<4λ)
	 * @return
	 * @throws ServiceException
	 */
	public String getServiceSeq(int servciecode) throws ServiceException;
	
	/**
	 * ��չʹ��
	 * @param name
	 * @param seq
	 * @return
	 * @throws ServiceException
	 */
	public String getCode(String name,String seq) throws ServiceException;
	
	/**
	 * @param sqlmapid sqlmap id
	 * @param arg ����
	 * @return
	 * @throws ServiceException
	 */
	public String getSeq(String sqlmapid,String arg) throws ServiceException;
	
	

}
