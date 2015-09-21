/*
 * @Id: URIService_Impl.java 16:50:30 2006-7-28
 * 
 * @author 
 * @version 1.0
 * payment_web_v2.0 PROJECT
 */
package ebank.web.remote;

public class URIService_Impl implements URIService {
	private String defaultUrl;

	/* (non-Javadoc)
	 * @see ebank.core.web.remote.URIService#getUrl(java.lang.String)
	 */
	public String getUrl(String merchantid,String version) {
		//可通过配置文件做个性化区分网关服务
		return defaultUrl;
	}

	/**
	 * @param defaultUrl The defaultUrl to set.
	 */
	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}
	

}
