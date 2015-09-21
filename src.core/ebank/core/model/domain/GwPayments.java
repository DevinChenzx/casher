package ebank.core.model.domain;

import java.util.Date;

public class GwPayments {
	private String id;
	private String prid;
	private String paytype;
	private String paynum;
	private long payamount;
	private long amount;	
	private String infromacct;
	private String intoacct;
	private String refnum;
	private int paysts;
	private Date paytime;
	private String channel;
	private String modes;
	private String recepit;
	private Date createdate;
	private String payinfo;
	
	private String billsts="N"; //[Y:N]
	
	
	public String getBillsts() {
		return billsts;
	}
	public void setBillsts(String billsts) {
		this.billsts = billsts;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrid() {
		return prid;
	}
	public void setPrid(String prid) {
		this.prid = prid;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getPaynum() {
		return paynum;
	}
	public void setPaynum(String paynum) {
		this.paynum = paynum;
	}
	
	
	public long getPayamount() {
		return payamount;
	}
	public void setPayamount(long payamount) {
		this.payamount = payamount;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getInfromacct() {
		return infromacct;
	}
	public void setInfromacct(String infromacct) {
		this.infromacct = infromacct;
	}
	public String getIntoacct() {
		return intoacct;
	}
	public void setIntoacct(String intoacct) {
		this.intoacct = intoacct;
	}
	public String getRefnum() {
		return refnum;
	}
	public void setRefnum(String refnum) {
		this.refnum = refnum;
	}
	
	public int getPaysts() {
		return paysts;
	}
	public void setPaysts(int paysts) {
		this.paysts = paysts;
	}
	public Date getPaytime() {
		return paytime;
	}
	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getModes() {
		return modes;
	}
	public void setModes(String modes) {
		this.modes = modes;
	}
	public String getRecepit() {
		return recepit;
	}
	public void setRecepit(String recepit) {
		this.recepit = recepit;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getPayinfo() {
		return payinfo;
	}
	public void setPayinfo(String payinfo) {
		this.payinfo = payinfo;
	}
	
	
	

}
