package ebank.core.model.domain;

import java.util.Date;

public class GwProc {
	   private String   id   ;//                 number(19,0)                    not null,	   
	   private String trxnum  ;//             varchar(128)                    not null,
	   private String bankcode ;//            varchar(10)                     not null,
	   private int    amount       ;//        int                             not null,
	   private String procname ;//            varchar(30)                     not null,
	   private String prosts    ;//           varchar(3)                      not null,
	   private String params  ;//             varchar(128),
	   private String trxdate    ;//          varchar(8),
	   private String acquirer_seq;//         varchar(64),
	   private String batch    ;//            varchar(30),
	   private Date   createdate  ;//         date                            not null,
	   private String opers     ;//           varchar(30),
	   private Date   operdate    ;//         date,
	   private String procid;
	   private String gwt_id;
	   private String operresult; 
	  
	
	public String getOperresult() {
		return operresult;
	}
	public void setOperresult(String operresult) {
		this.operresult = operresult;
	}
	public String getGwt_id() {
		return gwt_id;
	}
	public void setGwt_id(String gwtId) {
		gwt_id = gwtId;
	}
	public String getProcid() {
		return procid;
	}
	public void setProcid(String procid) {
		this.procid = procid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	public String getTrxnum() {
		return trxnum;
	}
	public void setTrxnum(String trxnum) {
		this.trxnum = trxnum;
	}
	public String getBankcode() {
		return bankcode;
	}
	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getProcname() {
		return procname;
	}
	public void setProcname(String procname) {
		this.procname = procname;
	}
	public String getProsts() {
		return prosts;
	}
	public void setProsts(String prosts) {
		this.prosts = prosts;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getTrxdate() {
		return trxdate;
	}
	public void setTrxdate(String trxdate) {
		this.trxdate = trxdate;
	}
	public String getAcquirer_seq() {
		return acquirer_seq;
	}
	public void setAcquirer_seq(String acquirerSeq) {
		acquirer_seq = acquirerSeq;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getOpers() {
		return opers;
	}
	public void setOpers(String opers) {
		this.opers = opers;
	}
	public Date getOperdate() {
		return operdate;
	}
	public void setOperdate(Date operdate) {
		this.operdate = operdate;
	}
	   
	   

}
