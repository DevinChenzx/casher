/*
 * Created on 2004-12-31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ebank.core.common.util;

import java.io.InputStream;
import java.util.Properties;
import org.springframework.core.io.ClassPathResource;


/**
 * @author xiexh
 * Description: 读取资源文件
 * 
 */
public class ResCode {
	private static Properties defaultProps = getProps();
	public static String getCode(String strCode)
	{
		String strOut=null;
		try{
			strOut=new String(defaultProps.getProperty(strCode).getBytes("8859_1"));
		}catch(Exception e){
			
		}
		return strOut;
	}
	
    private static Properties getProps() {
        defaultProps = new Properties();
        InputStream in = null;

        try {
            in = new ClassPathResource("ResponseCode.properties").getInputStream();
            defaultProps.load(in);
        } catch (Exception e) {
            System.err.println("Error: could not find the config of Properties");
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception exception) {
            }
        }
        return defaultProps;
    }
    public static String getCode(String propname,String key){
    	String strOut="未定义码";    
    	Properties pr=new Properties();
		try{
			InputStream in = null;
	        try {
	            in = new ClassPathResource(propname).getInputStream();
	            pr.load(in);
	        } catch (Exception e) {
	            System.err.println("Error: could not find the config of Properties");
	            e.printStackTrace();
	        } finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception exception) {
	            }
	        }
			strOut=new String(pr.getProperty(key));
		}catch(Exception e){
			
		}
		return strOut;
    	
    }
}
