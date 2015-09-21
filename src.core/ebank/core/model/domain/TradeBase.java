package ebank.core.model.domain;

import java.util.Date;

public class TradeBase {
	String id;
	Date dateCreated;
    Date lastUpdated;
    Long rootId;
    Long originalId;
    String tradeNo;
    String tradeType;
    Long partnerId;
    Long payerId;
    String  payerName;
    String payerCode;
    String payerAccountNo;
    Long payeeId;
    String  payeeName;
    String payeeCode;
    String payeeCustNo;  //收款客户号
    String payeeAccountNo;
    String outTradeNo;
    Long amount;
    Long feeAmount=0L;
    String currency;
    String subject;
    String status;
    Integer tradeDate;
    String  note;
    
    
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
    
    public String getPayeeCustNo() {
		return payeeCustNo;
	}
	public void setPayeeCustNo(String payeeCustNo) {
		this.payeeCustNo = payeeCustNo;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public Long getRootId() {
		return rootId;
	}
	public void setRootId(Long rootId) {
		this.rootId = rootId;
	}
	public Long getOriginalId() {
		return originalId;
	}
	public void setOriginalId(Long originalId) {
		this.originalId = originalId;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public Long getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}
	public Long getPayerId() {
		return payerId;
	}
	public void setPayerId(Long payerId) {
		this.payerId = payerId;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getPayerCode() {
		return payerCode;
	}
	public void setPayerCode(String payerCode) {
		this.payerCode = payerCode;
	}
	public String getPayerAccountNo() {
		return payerAccountNo;
	}
	public void setPayerAccountNo(String payerAccountNo) {
		this.payerAccountNo = payerAccountNo;
	}
	public Long getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Long payeeId) {
		this.payeeId = payeeId;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayeeCode() {
		return payeeCode;
	}
	public void setPayeeCode(String payeeCode) {
		this.payeeCode = payeeCode;
	}
	public String getPayeeAccountNo() {
		return payeeAccountNo;
	}
	public void setPayeeAccountNo(String payeeAccountNo) {
		this.payeeAccountNo = payeeAccountNo;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Long getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(Long feeAmount) {
		this.feeAmount = feeAmount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Integer tradeDate) {
		this.tradeDate = tradeDate;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
    
    

}
