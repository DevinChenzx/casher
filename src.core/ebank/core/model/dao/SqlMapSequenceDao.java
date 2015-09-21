package ebank.core.model.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;


import ebank.core.model.domain.Sequence;

public class SqlMapSequenceDao extends BaseDAO {
  
   /**
     *  »°oracle–Ú¡–
	 * @param name
	 * @return
	 * @throws DataAccessException
	 */
  public String getOracleSeq(String name) throws DataAccessException{
	    Sequence sequence = new Sequence(name, "-1");
	    sequence = (Sequence) getSqlMapClientTemplate().queryForObject("oracleSequence", sequence);
	    if (sequence == null) {
	      throw new DataRetrievalFailureException("Error: A null sequence was returned from the database (could not get next " +
	      			name + " sequence).");
	    }
	    return sequence.getNextId();
	  
  }
  
  public String getServiceSeq(int servicecode) throws DataAccessException{
	  Sequence sequence = new Sequence(String.valueOf(servicecode), "-1");
	    sequence = (Sequence) getSqlMapClientTemplate().queryForObject("serviceSequence", sequence);
	    if (sequence == null) {
	      throw new DataRetrievalFailureException("Error: A null sequence was returned from the database (could not get next " +
	    		  String.valueOf(servicecode) + " sequence).");
	    }
	    return sequence.getNextId();
	  
  }
  
  public String getSeqFactory(String sqlmapid,String arg) throws DataAccessException{
	  Sequence sequence = new Sequence(arg, "-1");
	  sequence = (Sequence) getSqlMapClientTemplate().queryForObject(sqlmapid, sequence);
	  if (sequence == null) {
	      throw new DataRetrievalFailureException("Error: A null sequence was returned from the database (could not get next ");
	   
	  }
	  return sequence.getNextId();
	  
  }
}
