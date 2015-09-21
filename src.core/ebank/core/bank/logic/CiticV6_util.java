/*
 * @Id: CiticV6_util.java 上午09:18:13 2009-9-29
 * 
 * @author Kitian@chinabank.com.cn
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class CiticV6_util {
	//声明存放CNCBCryptoPkg.jar/bcmail-jdk13-136.jar/jce-jdk13-136.jar的路径	
	private static final String 	EC_CIPHER_TOOL_CLASS = "com.lsy.baselib.crypto.processor.ECCryptoProcessor";   
		
   	private Object objectOfECCryptoProcessor	= null;
   	private Method methodOfSetSignerCertificate	= null;
   	private Method methodOfSetSignerPrivatekey	= null;
   	private Method methodOfSign					= null;
   	private Method methodOfAddTrustedCertificate= null;
   	private Method methodOfVerify				= null;
   	private Method methodOfGetOrderMessage		= null;
   	private Method methodOfGetSignerCertificate	= null;
   	
   	public CiticV6_util(String lib_folder)throws Exception{ 
 		File cncb_ec_lib = new File(lib_folder);   
 		if(!cncb_ec_lib.exists()) {   
 			throw new Exception("lib folder '"+lib_folder+"' not exists!");   
		}   
 
 		File[] libs = cncb_ec_lib.listFiles(new FileFilter(){   
   			public boolean accept(File dir){   
     			String name = dir.getName().toLowerCase();   
     			return name.endsWith("jar");   
   			}   
 		});   

 		URL[] urls = new URL[libs.length];     
 		for(int i = 0; i < libs.length; i++) {   
  			urls[i] = new URL("file",null,libs[i].getAbsolutePath());   
 		}
  
 		//创建自定义类装载器
 		ClassLoader classLoader = new URLClassLoader(urls, null);
 		Class clazz 			= classLoader.loadClass(EC_CIPHER_TOOL_CLASS);
 		Constructor constructor	= clazz.getConstructor(new Class[]{});
 		objectOfECCryptoProcessor					= constructor.newInstance(new Object[]{});
		methodOfSetSignerCertificate	= clazz.getDeclaredMethod("setSignerCertificate", new Class[] {byte[].class});
		methodOfSetSignerPrivatekey		= clazz.getDeclaredMethod("setSignerPrivatekey", new Class[] {byte[].class, String.class});
		methodOfSign					= clazz.getDeclaredMethod("sign", new Class[] {byte[].class});
		methodOfAddTrustedCertificate	= clazz.getDeclaredMethod("addTrustedCertificate", new Class[] {byte[].class});
		methodOfVerify					= clazz.getDeclaredMethod("verify", new Class[] {byte[].class});
		methodOfGetOrderMessage			= clazz.getDeclaredMethod("getOrderMessage", new Class[] {byte[].class});
		methodOfGetSignerCertificate	= clazz.getDeclaredMethod("getSignerCertificate", new Class[] {byte[].class});
	}
   	
   	public Object getObjectOfECCryptoProcessor(){
   		return objectOfECCryptoProcessor;
   	}
   	
   	public Method getMethodOfSetSignerCertificate(){
   		return methodOfSetSignerCertificate;
   	}

   	public Method getMethodOfSetSignerPrivatekey(){
   		return methodOfSetSignerPrivatekey;
   	}
   	
   	public Method getMethodOfSign(){
   		return methodOfSign;
   	}
   	
   	public Method getMethodOfAddTrustedCertificate(){
   		return methodOfAddTrustedCertificate;
   	}
   	
   	public Method getMethodOfVerify(){
   		return methodOfVerify;
   	}
   	
   	public Method getMethodOfOfGetOrderMessage(){
   		return methodOfGetOrderMessage;
   	}
   	
   	public Method getMethodOfGetSignerCertificate(){
   		return methodOfGetSignerCertificate;
   	}   	
}
