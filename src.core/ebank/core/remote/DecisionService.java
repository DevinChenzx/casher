/*
 * @Id: DecisionService.java ����10:43:57 2007-7-25
 * 
 * @author 
 * @version 1.0
 * web_risk PROJECT
 */
package ebank.core.remote;

/**
 * @author 
 * Description: ���շ���ӿ�
 * 
 */
public interface DecisionService {
	/**
	 * ������֤����
	 * @param trans
	 * @return xml
	 */
	public String decision(String trans,String loginname,String loginpwd)throws Exception;
	/**
	 * �ص�֪ͨ����ϵͳ���׽��
	 * @param result
	 * @return
	 */
	public int transcallback(String result);

}
