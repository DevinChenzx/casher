/*
 * @Id: JniProxyService_Impl.java 10:11:36 2006-9-13
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.remote;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.caucho.hessian.client.HessianProxyFactory;


/**
 * @author 
 * Description: jni 代理调用
 * 
 */
public class JniProxyService_Impl implements JniProxyService {

	public HessianProxyFactory factory=new HessianProxyFactory();
	private Log log=LogFactory.getLog(this.getClass());
	
	/* (non-Javadoc)
	 * @see ebank.core.remote.JniProxyService#getCrypt(java.lang.String, java.lang.String)
	 */
	private String proxyhost;
	private String proxyhost2;
	
	public String getCrypt(String plain, String serviceid,String cmd) {
		try{	    	
			JniProxyService qs = (JniProxyService) factory.create(JniProxyService.class,proxyhost);
	    	return qs==null?null:qs.getCrypt(plain,serviceid,cmd);
	    }catch(Exception ex){
	    	log.info(proxyhost+" jnicrypt:"+plain);
	    	ex.printStackTrace();
	    	log.info("enable proxyhost2:"+proxyhost2);
	    	try {
	    		JniProxyService qs = (JniProxyService) factory.create(JniProxyService.class,proxyhost2);
	    		return qs==null?null:qs.getCrypt(plain,serviceid,cmd);
			} catch (Exception e) {
				log.info(proxyhost2+" jnicrypt:"+plain);
				e.printStackTrace();
			}	    	
	    }
	    return null;
	}

	/* (non-Javadoc)
	 * @see ebank.core.remote.JniProxyService#verify(java.lang.String, java.lang.String)
	 */
	public boolean verify(String plain, String serviceid,String cmd) {
		try{	    	
			JniProxyService qs = (JniProxyService) factory.create(JniProxyService.class,proxyhost);
	    	return qs==null?false:qs.verify(plain,serviceid,cmd);
	    }catch(Exception ex){
	    	log.info(proxyhost+" jniverify:"+plain);
	    	ex.printStackTrace();
	    	log.info("enable proxyhost2:"+proxyhost2);
	    	try {
	    		JniProxyService qs = (JniProxyService) factory.create(JniProxyService.class,proxyhost2);
	    		return qs==null?false:qs.verify(plain,serviceid,cmd);
			} catch (Exception e) {
				log.info(proxyhost2+" jniverify:"+plain);
				e.printStackTrace();
			}	  
	    }
	    return false;
	}
	

	/* (non-Javadoc)
	 * @see ebank.core.remote.JniProxyService#decrypt(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String decrypt(String plain, String serviceid, String cmd) {
		try{	    	
			JniProxyService qs = (JniProxyService) factory.create(JniProxyService.class,proxyhost);
	    	return qs==null?null:qs.decrypt(plain,serviceid,cmd);
	    }catch(Exception ex){
	    	log.info(proxyhost+" exception...");
	    	ex.printStackTrace();
	    	log.info("enable proxyhost2:"+proxyhost2);
	    	try {	    		
	    		JniProxyService qs = (JniProxyService) factory.create(JniProxyService.class,proxyhost2);
	    		return qs==null?null:qs.decrypt(plain,serviceid,cmd);
			} catch (Exception e) {
				log.info(proxyhost2+" exception...");
				e.printStackTrace();
			}	  
	    }
	    return null;
	}

	/**
	 * @param proxyhost The proxyhost to set.
	 */
	public void setProxyhost(String proxyhost) {
		this.proxyhost = proxyhost;
	}

	public void setProxyhost2(String proxyhost2) {
		this.proxyhost2 = proxyhost2;
	}
	
	

}
