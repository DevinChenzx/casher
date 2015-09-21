/*
 * @Id: DecisionService.java 上午10:43:57 2007-7-25
 * 
 * @author 
 * @version 1.0
 * web_risk PROJECT
 */
package ebank.core.remote;

/**
 * @author 
 * Description: 风险服务接口
 * 
 */
public interface DecisionService {
	/**
	 * 风险验证服务
	 * @param trans
	 * @return xml
	 */
	public String decision(String trans,String loginname,String loginpwd)throws Exception;
	/**
	 * 回调通知风险系统交易结果
	 * @param result
	 * @return
	 */
	public int transcallback(String result);

}
