/*
 * @Id: URIService.java 16:48:28 2006-7-28
 * 
 * @author 
 * @version 1.0
 * payment_web_v2.0 PROJECT
 */
package ebank.web.remote;

/**
 * @author 
 * Description: 获取网关资源
 * 
 */
public interface URIService {
	/**
	 * 获取网关地址
	 * @param merchantid 商户号
	 * @param version    订单版本号
	 * @return
	 */
	public String getUrl(String merchantid,String version);

}
