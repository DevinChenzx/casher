package ebank.test;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import ebank.core.AccountService;
import ebank.core.remote.HttpClientService;
import ebank.core.remote.HttpMethodCallback;
import ebank.core.remote.JniProxyService;


public class HttpClientService_Test extends BaseTest{
	
	public void test(){
		try {
			HttpClientService pm=(HttpClientService)factory.getBean("httpClientService");
			final NameValuePair[] gnt=new NameValuePair[]{new NameValuePair("body","ÖÐÎÄ")};
			
			pm.getHttpResp("GBK","http://10.68.76.155:8080/wgdemo2/notify_url.jsp", new HttpMethodCallback() {
				
				public PostMethod initMethod(PostMethod method) {
					
					//method.setQueryString(gnt);	
					//method.setRequestEntity(new StringRequestEntity(gnt));	
					method.setRequestBody(gnt);
				    //method.setRequestEntity(new StringRequestEntity(jo.toString()));
					return method;
					
					
					
				}
			}, null, null);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void tews2t(){
		try {
			AccountService pm=(AccountService)factory.getBean("accountService");
			pm.getAccount("5290000000000099");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
	 

}
