/*
 * @Id: VelocityTemplateService_Impl.java 20:05:09 2006-4-29
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.logic;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import org.springframework.core.io.ClassPathResource;

import ebank.core.VelocityTemplate;
import ebank.core.VelocityTemplateCallback;


public class VelocityTemplateService_Impl implements VelocityTemplate{
	
	private Log log=LogFactory.getLog(this.getClass());
	private VelocityEngine velocityEngine;
	private String path;
	
	/* (non-Javadoc)
	 * @see ebank.core.VelocityTemplate#getMessage(ebank.core.VelocityTemplateCallback)
	 */
	public String getMessage(VelocityTemplateCallback callback){
		try {
			VelocityContext context1 =null; 
			Map mp=new HashMap();
			Object[] args=new Object[10];
			callback.execute(mp,args);	
			if(mp!=null){	
				context1=new VelocityContext((Map)args[0]);				
			}else{
				context1=new VelocityContext();
			}
			if(args!=null){
				if(args[2]==null) args[2]="gb2312";  //È±Ê¡µÄ×Ö·û±àÂë				
			}else{
				throw new RuntimeException("argument lack..");
			}
			Properties prop=new Properties();
			try{
				prop.load(new ClassPathResource("velocity.properties").getInputStream());//,VelocityManager.class));
				
			}catch(Exception ex){
				log.debug("velocity.properties not found");				
				prop.put("resource.loader", "class");
				prop.put("class.resource.loader.description",
						"Velocity Classpath Resource Loader");
				prop.put("class.resource.loader.class",
								"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
				
			}
			velocityEngine.init(prop);
			StringWriter writer = new StringWriter();
			Template template = velocityEngine.getTemplate(path+"/"+String.valueOf(args[1]),String.valueOf(args[2]));			
			template.merge( context1, writer);
			if(log.isDebugEnabled()) {
				log.debug("template:"+args[1]+" encode:"+args[2]);
				log.debug("render msg:"+writer);
			}
			context1=null;
			mp=null;
			args=null;
			return String.valueOf(writer);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
		
	}

	/**
	 * @param ve The ve to set.
	 */
	public void setVelocityEngine(VelocityEngine ve) {
		this.velocityEngine = ve;
	}

	/**
	 * @param path The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	
	

}
