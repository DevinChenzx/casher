package ebank.core.remote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import beartool.Md5Encrypt;

public class SmsClientService {

	protected Log log=LogFactory.getLog(this.getClass());
	private String smsServiceUrl;
	private String smsUserid;
	private String smsUserpwd;

	
	public Map<String,Object> getHttpResp(String randomNum,String mobilePhone){
		HttpClient client=new HttpClient();
		PostMethod method=new PostMethod();				
		method.setRequestHeader("Content-type", "text/xml; charset=GB2312");
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
	    		new DefaultHttpMethodRetryHandler()); 
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,new Integer(60000));
		String response="";
		
		Map<String,Object> mp=new HashMap<String,Object>();
		try{
			URL url=new URL(this.smsServiceUrl);
			method.setPath(url.getPath());			
			if(this.smsServiceUrl.indexOf("https")==0){//https
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
			String charset="UTF-8";
			
			Date myDate=new Date(System.currentTimeMillis());
			SimpleDateFormat sDateFormat=new SimpleDateFormat("MM/dd");
			String mm = sDateFormat.format(myDate).substring(0, 2);
			String dd = sDateFormat.format(myDate).substring(3, 5);
			String signStr2=mm+"月"+dd+"日，您申请进行吉高余额支付操作。手机校验码:"+randomNum+"。-吉卡。";
			String signStr=mobilePhone+signStr2+this.smsUserid+this.smsUserpwd;
			
			
			method.setQueryString(new NameValuePair[]{
					new NameValuePair("target", mobilePhone),
					new NameValuePair("content", URLEncoder.encode(signStr2, charset)),
					new NameValuePair("userid", this.smsUserid),
					new NameValuePair("sign", Md5Encrypt.md5(signStr,charset)),
					new NameValuePair("mode", "1"),
					new NameValuePair("charset", charset)
					
			});
			
			int rescode=client.executeMethod(method);	
			mp.put("statusCode", rescode);
			
			if(rescode==200){
				BufferedReader reader=new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(),charset));
				String curline="";
				while((curline=reader.readLine())!=null){				
					response+=curline;
				}				
				log.info(this.smsServiceUrl+" response:"+response);
				mp.put("response", response);
				
			}else{
				log.error(this.smsServiceUrl+" error:"+rescode);
			}
		    
			}catch (Exception e) {
				e.printStackTrace();				
			}finally{
				method.releaseConnection();
			}
			return mp;
			//杈撳嚭绫讳技{"rescode":"00","resmsg":"success","messageid":1994}
			//00鏍囩ず鎴愬姛
	}

	public void setSmsServiceUrl(String smsServiceUrl) {
		this.smsServiceUrl = smsServiceUrl;
	}

	public void setSmsUserid(String smsUserid) {
		this.smsUserid = smsUserid;
	}

	public void setSmsUserpwd(String smsUserpwd) {
		this.smsUserpwd = smsUserpwd;
	}
}
