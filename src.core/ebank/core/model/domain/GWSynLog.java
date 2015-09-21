/*
 * @Id: GWSynLog.java 20:16:18 2006-5-11
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.model.domain;

import java.util.Date;

/**
 * @author 
 * Description: ������ʱ��־
 * 
 */
public class GWSynLog {
	private long logseq;	     //��־���к� 
	private String bankcode;	   //���б���  
	private String transnum;	  //���׺�;    
	private String transamount;	//���׽��
	private String transstate;	//����״̬
	private String transdate;	  //����ʱ��  
	private int versionnum;	      //�汾��  
	private String batchnum;	  //���κ� 
	private String bankseq;       //������ˮ
	private String xdesc;         //��ע 
	private Date cratedate;	     //����ʱ��  gwlogistic
	
	
	/**
	 * @return Returns the bankseq.
	 */
	public String getBankseq() {
		return bankseq;
	}
	/**
	 * @param bankseq The bankseq to set.
	 */
	public void setBankseq(String bankseq) {
		this.bankseq = bankseq;
	}
	/**
	 * @return Returns the xdesc.
	 */
	public String getXdesc() {
		return xdesc;
	}
	/**
	 * @param xdesc The xdesc to set.
	 */
	public void setXdesc(String xdesc) {
		this.xdesc = xdesc;
	}
	/**
	 * @return Returns the bankcode.
	 */
	public String getBankcode() {
		return bankcode;
	}
	/**
	 * @param bankcode The bankcode to set.
	 */
	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}
	/**
	 * @return Returns the batchnum.
	 */
	public String getBatchnum() {
		return batchnum;
	}
	/**
	 * @param batchnum The batchnum to set.
	 */
	public void setBatchnum(String batchnum) {
		this.batchnum = batchnum;
	}
	/**
	 * @return Returns the cratedate.
	 */
	public Date getCratedate() {
		return cratedate;
	}
	/**
	 * @param cratedate The cratedate to set.
	 */
	public void setCratedate(Date cratedate) {
		this.cratedate = cratedate;
	}
	/**
	 * @return Returns the logseq.
	 */
	public long getLogseq() {
		return logseq;
	}
	/**
	 * @param logseq The logseq to set.
	 */
	public void setLogseq(long logseq) {
		this.logseq = logseq;
	}
	/**
	 * @return Returns the transamount.
	 */
	public String getTransamount() {
		return transamount;
	}
	/**
	 * @param transamount The transamount to set.
	 */
	public void setTransamount(String transamount) {
		this.transamount = transamount;
	}
	/**
	 * @return Returns the transdate.
	 */
	public String getTransdate() {
		return transdate;
	}
	/**
	 * @param transdate The transdate to set.
	 */
	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}
	/**
	 * @return Returns the transnum.
	 */
	public String getTransnum() {
		return transnum;
	}
	/**
	 * @param transnum The transnum to set.
	 */
	public void setTransnum(String transnum) {
		this.transnum = transnum;
	}
	/**
	 * @return Returns the transstate.
	 */
	public String getTransstate() {
		return transstate;
	}
	/**
	 * @param transstate The transstate to set.
	 */
	public void setTransstate(String transstate) {
		this.transstate = transstate;
	}
	/**
	 * @return Returns the versionnum.
	 */
	public int getVersionnum() {
		return versionnum;
	}
	/**
	 * @param versionnum The versionnum to set.
	 */
	public void setVersionnum(int versionnum) {
		this.versionnum = versionnum;
	}
	
	
	

}
