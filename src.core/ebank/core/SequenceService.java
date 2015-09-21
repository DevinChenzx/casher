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
 * Description: 序列服务
 * 
 */
public interface SequenceService {
	
	/**
	 * 根据缺省序列号取序列
	 * @return
	 * @throws ServiceException
	 */
	public String getCode() throws ServiceException;
	
	/**
	 * 指定序列名称取序列
	 * @param seqname
	 * @return
	 * @throws ServiceException
	 */
	public String getCode(String seqname) throws ServiceException;
	
	/**
	 * 更加服务编码取序列
	 * @param servciecode （内部号<4位)
	 * @return
	 * @throws ServiceException
	 */
	public String getServiceSeq(int servciecode) throws ServiceException;
	
	/**
	 * 扩展使用
	 * @param name
	 * @param seq
	 * @return
	 * @throws ServiceException
	 */
	public String getCode(String name,String seq) throws ServiceException;
	
	/**
	 * @param sqlmapid sqlmap id
	 * @param arg 参数
	 * @return
	 * @throws ServiceException
	 */
	public String getSeq(String sqlmapid,String arg) throws ServiceException;
	
	

}
