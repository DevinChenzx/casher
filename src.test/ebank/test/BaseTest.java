/*
 * @Id: BaseTest.java 11:24:34 2006-3-21
 * 
 * @author xiexh@chinabank.com.cn
 * @version 1.0
 * PAYGW_WEB_V6 PROJECT
 */
package ebank.test;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

public class BaseTest extends TestCase{
	protected BeanFactory factory;
	protected Logger log = Logger.getLogger(TestCase.class);
	private ClassPathXmlApplicationContext createSpringConfig() {
	      return new ClassPathXmlApplicationContext(new String[]{"applicationContext-Service.xml","bank.xml"	    		                                               
	    		  });
    }
	protected void setUp() throws Exception {
		 factory =(BeanFactory)createSpringConfig();         
         assertTrue("Should have created the server side", factory != null);
	}

	

}
