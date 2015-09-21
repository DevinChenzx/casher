/*
 * @Id: SequenceService_Impl.java 13:06:17 2006-2-14
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.logic;


import java.util.Random;


import ebank.core.SequenceService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.model.dao.SqlMapSequenceDao;

/**
 * @author 
 * Description: 序列服务实现
 * 
 */
public class SequenceService_Impl implements SequenceService {
	private SqlMapSequenceDao sqldao;
	/**
	 * 根据缺省序列号取序列
	 * @return
	 * @throws ServiceException
	 */
	public String getCode() throws ServiceException{
		try{			
			return String.valueOf(sqldao.getOracleSeq(Constants.SEQ_ORDER));
		}catch(Exception ex){
			throw new ServiceException(EventCode.SEQ_GET_FAILURE);
		}
		
	}
	
	
	/**
	 * 指定序列名称取序列
	 * @param seqname
	 * @return
	 * @throws ServiceException
	 */
	public String getCode(String seqname) throws ServiceException{
		try{			
			return String.valueOf(sqldao.getOracleSeq(seqname));
		}catch(Exception ex){
			throw new ServiceException(EventCode.SEQ_GET_FAILURE);
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see ebank.core.service.SequenceService#getServiceSeq(int)
	 */
	public String getServiceSeq(int servicecode) throws ServiceException {
		try{
			String str="";
			Random rd=new Random();
			for(int i=0;i<2;i++){				
				str+=rd.nextInt(10);				
			}
			rd=null;
			return sqldao.getServiceSeq(servicecode)+str;
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ServiceException(EventCode.SEQ_GET_FAILURE);
		}		
	}
	
	public String getSeq(String sqlmapid,String args) throws ServiceException{
		try{
			return sqldao.getSeqFactory(sqlmapid,args);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ServiceException(EventCode.SEQ_GET_FAILURE);
		}
		
		
	}


	/**
	 * 扩展使用
	 * @param name
	 * @param seq
	 * @return
	 * @throws ServiceException
	 */
	public String getCode(String name,String seq) throws ServiceException{
		//TODO 
		return null;
	}


	/**
	 * @param sqldao The sqldao to set.
	 */
	public void setSqldao(SqlMapSequenceDao sqldao) {
		this.sqldao = sqldao;
	}
	
	

}
