package ebank.core.remote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QueryResultService {

	protected Log log=LogFactory.getLog(this.getClass());
	private String queryResultUrl;

	
	public void setQueryResultUrl(String queryResultUrl) {
		this.queryResultUrl = queryResultUrl;
	}


	public Map<String,Object> getHttpResp(String id){
		HttpClient client=new HttpClient();
		PostMethod method=new PostMethod();				
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
	    		new DefaultHttpMethodRetryHandler()); 
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,new Integer(60000));
		String response="";
		
		Map<String,Object> mp=new HashMap<String,Object>();
		try{
			URL url=new URL(this.queryResultUrl);
			method.setPath(url.getPath());			
			if(this.queryResultUrl.indexOf("https")==0){//https
				 /*
				Protocol myhttps = new Protocol("https", 
					                           new AuthSSLProtocolSocketFactory(
						                       new URL(jks), jkspwd,
						                       null, jkspwd), 8443); //no serverside truststore
						                       //new URL(this.jkspath), this.jkspwd), 8443);
					     
				client.getHostConfiguration().setHost(url.getHost(),
		                    url.getPort(),
		                    myhttps);
		                    */
			}else{
				client.getHostConfiguration().setHost(url.getHost(),url.getPort(),url.getProtocol());
			}	
			
			method.setQueryString(new NameValuePair[]{
					new NameValuePair("id", id)					
			});
			
			int rescode=client.executeMethod(method);	
			mp.put("statusCode", rescode);
			
			if(rescode==200){
				BufferedReader reader=new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(),"UTF-8"));
				String curline="";
				while((curline=reader.readLine())!=null){				
					response+=curline;
				}				
				log.info(this.queryResultUrl+" response:"+response);
				mp.put("response", response);
				
			}else{
				log.error(this.queryResultUrl+" error:"+rescode);
			}
		    
			}catch (Exception e) {
				e.printStackTrace();				
			}finally{
				method.releaseConnection();
			}
			return mp;
	}

}
