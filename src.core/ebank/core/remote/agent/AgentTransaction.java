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
 * Description: 抽象交易
 * 
 */
public class AgentTransaction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String transnum;   //交易号
	private String bankseq;    //银行流水
	
	private boolean success;  //查询交易是否成功
	private String result;    //查询交易消息
	private String transcode; //交易代码
	private int    exeresult; //回调处理结果 0－未回调 -1 回调失败 1回调处理成功
	private String exedesc;   //执行说明
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

