/*
 * @Id: Transaction.java 11:02:34 2006-12-8
 * 
 * @author 
 * @version 1.0
 * payment_agent PROJECT
 */
package ebank.core.remote.agent;

import java.io.Serializable;

/**
 * @author 
 * Description: ������
 * 
 */
public class AgentTransaction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String transnum;   //���׺�
	private String bankseq;    //������ˮ
	
	private boolean success;  //��ѯ�����Ƿ�ɹ�
	private String result;    //��ѯ������Ϣ
	private String transcode; //���״���
	private int    exeresult; //�ص������� 0��δ�ص� -1 �ص�ʧ�� 1�ص�����ɹ�
	private String exedesc;   //ִ��˵��
	public String toString(){
		return "AgentTransaction{transnum:"+transnum+" transeq:"+bankseq+" result:"+result+" success:"+success+" transcode:"+transcode+" exeresult:"+exeresult+" exedesc:"+exedesc;
	}
	
	public String getTransnum() {
		return transnum;
	}

	public void setTransnum(String transnum) {
		this.transnum = transnum;
	}

	public String getBankseq() {
		return bankseq;
	}

	public void setBankseq(String bankseq) {
		this.bankseq = bankseq;
	}

	/**
	 * @return Returns the success.
	 */
	public boolean isSuccess() {
		return success;
	}
	/**
	 * @param success The success to set.
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	/**
	 * @return Returns the transcode.
	 */
	public String getTranscode() {
		return transcode;
	}
	/**
	 * @param transcode The transcode to set.
	 */
	public void setTranscode(String transcode) {
		this.transcode = transcode;
	}
	/**
	 * @return Returns the exeresult.
	 */
	public int getExeresult() {
		return exeresult;
	}
	/**
	 * @param exeresult The exeresult to set.
	 */
	public void setExeresult(int exeresult) {
		this.exeresult = exeresult;
	}
	/**
	 * @return Returns the result.
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result The result to set.
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return Returns the exedesc.
	 */
	public String getExedesc() {
		return exedesc;
	}
	/**
	 * @param exedesc The exedesc to set.
	 */
	public void setExedesc(String exedesc) {
		this.exedesc = exedesc;
	}
	
	
	
	
	
	
	

}

