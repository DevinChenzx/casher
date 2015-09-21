package ebank.core.model.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class BaseDAO extends SqlMapClientDaoSupport{
	protected Log log = LogFactory.getLog(getClass());

}

