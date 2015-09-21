package ebank.core.model.domain;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class GwFaq implements Serializable {

	private long id;
	private String trxno;
	private String ispay;
	private String contact;
	private String quedesc;
	private String issend;
	private String status;
	private String proceResult;
	private Date   createdate;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTrxno() {
		return trxno;
	}
	public void setTrxno(String trxno) {
		this.trxno = trxno;
	}
	public String getIspay() {
		return ispay;
	}
	public void setIspay(String ispay) {
		this.ispay = ispay;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getQuedesc() {
		return quedesc;
	}
	public void setQuedesc(String quedesc) {
		this.quedesc = quedesc;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getIssend() {
		return issend;
	}
	public void setIssend(String issend) {
		this.issend = issend;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProceResult() {
		return proceResult;
	}
	public void setProceResult(String proceResult) {
		this.proceResult = proceResult;
	}
}
