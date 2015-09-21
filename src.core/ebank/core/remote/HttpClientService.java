package ebank.core.remote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ebank.ext.AuthSSLProtocolSocketFactory;

public class HttpClientService {
	
	protected Log log=LogFactory.getLog(this.getClass());
	private class GBKPostMethod extends PostMethod{
		public GBKPostMethod(String url) { 
			super(url); 
			} 

		@Override 
		public String getRequestCharSet() {		
			return "GBK"; 
		} 
	}
	private class UTF8PostMethod extends PostMethod{
		public UTF8PostMethod(String url) { 
			super(url); 
			} 

		@Override 
		public String getRequestCharSet() {		
			return "UTF-8"; 
		} 
	}
	
	public Map<String,Object> getHttpResp(final String charset,String httpUrl,HttpMethodCallback methodCallback,String jks,String jkspwd){
		HttpClient client=new HttpClient();		
		PostMethod method=null;
		if("GBK".equalsIgnoreCase(charset)) method=new HttpClientService.GBKPostMethod(httpUrl);
		else method=new HttpClientService.UTF8PostMethod(httpUrl);
		
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
	    		new DefaultHttpMethodRetryHandler()); 
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,new Integer(20000));
		String response="";		
		Map<String,Object> mp=new HashMap<String,Object>();
		try{
			URL url=new URL(httpUrl);
			method.setPath(url.getPath());			
			if(httpUrl.indexOf("https")==0){//https
				 
				Protocol myhttps = new Protocol("https", 
					                           new AuthSSLProtocolSocketFactory(
						                       new URL(jks), jkspwd,
						                       null, jkspwd), 8443); //no serverside truststore
						                      
					     
				client.getHostConfiguration().setHost(url.getHost(),
		                    url.getPort(),
		                    myhttps);
			}else{
				client.getHostConfiguration().setHost(url.getHost(),url.getPort(),url.getProtocol());
			}			
			methodCallback.initMethod(method);
			log.info("charset:"+charset+" try to connect:"+httpUrl +" query body:"+"queryString:"+method.getQueryString());
			int rescode=client.executeMethod(method);	
			
			mp.put("statusCode", rescode);
			if(rescode==200){
				BufferedReader reader=new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(),charset));
				String curline="";
				while((curline=reader.readLine())!=null){				
					response+=curline;
				}				
				log.info(httpUrl+" response:"+response);
				mp.put("response", response);
			}else{
				log.error(httpUrl+" error:"+rescode);
			}
		    
			}catch (Exception e) {
				e.printStackTrace();				
			}finally{
				method.releaseConnection();
			}
			log.info(mp);
			return mp;
	}	
		
	
	public Map<String,Object> getHttpResp(String httpUrl,HttpMethodCallback methodCallback,String jks,String jkspwd){
		HttpClient client=new HttpClient();
		PostMethod method=new PostMethod();				
		method.setRequestHeader("Content-type", "text/xml; charset=UTF-8");
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
	    		new DefaultHttpMethodRetryHandler()); 
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,new Integer(20000));
		String response="";
		/*
		client.getHostConfiguration().setProxy("192.168.1.224", 8080);
		
		client.getState().setProxyCredentials(null, "192.168.1.224",
				new NTCredentials("xiexh", "Windows0903", "127.0.0.1", ""));
		*/
		Map<String,Object> mp=new HashMap<String,Object>();
		try{
			URL url=new URL(httpUrl);
			method.setPath(url.getPath());			
			if(httpUrl.indexOf("https")==0){//https
				 
				Protocol myhttps = new Protocol("https", 
					                           new AuthSSLProtocolSocketFactory(
						                       new URL(jks), jkspwd,
						                       null, jkspwd), 8443); //no serverside truststore
						                       //new URL(this.jkspath), this.jkspwd), 8443);
					     
				client.getHostConfiguration().setHost(url.getHost(),
		                    url.getPort(),
		                    myhttps);
			}else{
				client.getHostConfiguration().setHost(url.getHost(),url.getPort(),url.getProtocol());
			}			
			methodCallback.initMethod(method);
			log.info("try to connect:"+httpUrl +" query body:"+"queryString:"+method.getQueryString());
			int rescode=client.executeMethod(method);	
			
			mp.put("statusCode", rescode);
			if(rescode==200){
				BufferedReader reader=new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(),"UTF-8"));
				String curline="";
				while((curline=reader.readLine())!=null){				
					response+=curline;
				}				
				log.info(httpUrl+" response:"+response);
				mp.put("response", response);
			}else{
				log.error(httpUrl+" error:"+rescode);
			}
		    
			}catch (Exception e) {
				e.printStackTrace();				
			}finally{
				method.releaseConnection();
			}
			return mp;
	}

}
