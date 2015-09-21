package ebank.core.model.domain;

import java.io.Serializable;
import java.util.Date;

public class GwTrxs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 570960317073232195L;
	private String gworders_id; // number(19,0),
	private String paymentid;   //varchar(22)
	private String trxnum; // varchar(128) not null,
	private String trxtype; // varchar(4) not null,
	private String paymode; // varchar(10) not null,
	private long    amount; // int not null,
	private String currency; // varchar(4) not null,
	private String servicecode; // varchar(20),
	
	private String acquirer_id  ; //varchar(10),
	private String acquirer_code; // varchar(20) not null,
	private String acquirer_name; // varchar(30),
	private String acquirer_merchant; // varchar(64) not null,
	
	private String acquirer_seq; // varchar(128),
	private String acquirer_date; // varchar(8),
	private String acquirer_msg; // varchar(256),
	private String payer_ip; // varchar(20) not null,
	private String refnum; // varchar(128),
	private String authcode; // varchar(6),

	private String fromacctid; // number(19,0) not null,
	private String fromacctnum;
	private String payinfo; // varchar(64),

	private Date   createdate; // date not null,
	private Date   closedate; // date not null,
	private String submitdates; // 表单提交银行时间
	private String trxsts; // varchar(3) not null,	

	private String opers; // varchar(10) not null,
	private String operdate; // date not null,	
	private int    version; // int not null,
	private String trxdesc; // varchar(100),
	private String id; // number(19,0) not null
	
	private String channel;  //付款通道
	private String payment_type;//付款类型
	
	private String buyer_id;   //收款账号ID
	private String buyer_name;  //收款账号号
	private long fee_amount;
	
	
	
	public String getAcquirer_id() {
		return acquirer_id;
	}



	public void setAcquirer_id(String acquirer_id) {
		this.acquirer_id = acquirer_id;
	}

	public long getFee_amount() {
		return fee_amount;
	}

	public void setFee_amount(long fee_amount) {
		this.fee_amount = fee_amount;
	}

	public String getPaymentid() {
		return paymentid;
	}

	public void setPaymentid(String paymentid) {
		this.paymentid = paymentid;
	}

	public String getFromacctid() {
		return fromacctid;
	}

	public void setFromacctid(String fromacctid) {
		this.fromacctid = fromacctid;
	}

	public String getFromacctnum() {
		return fromacctnum;
	}

	public void setFromacctnum(String fromacctnum) {
		this.fromacctnum = fromacctnum;
	}

	
	public String getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(String buyerId) {
		buyer_id = buyerId;
	}

	public String getBuyer_name() {
		return buyer_name;
	}

	public void setBuyer_name(String buyerName) {
		buyer_name = buyerName;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String paymentType) {
		payment_type = paymentType;
	}

	

	
	

	public String getGworders_id() {
		return gworders_id;
	}

	public void setGworders_id(String gwordersId) {
		gworders_id = gwordersId;
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

	public String getTrxtype() {
		return trxtype;
	}

	public void setTrxtype(String trxtype) {
		this.trxtype = trxtype;
	}

	public String getPaymode() {
		return paymode;
	}

	public void setPaymode(String paymode) {
		this.paymode = paymode;
	}	

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getServicecode() {
		return servicecode;
	}

	public void setServicecode(String servicecode) {
		this.servicecode = servicecode;
	}

	public String getAcquirer_code() {
		return acquirer_code;
	}

	public void setAcquirer_code(String acquirerCode) {
		acquirer_code = acquirerCode;
	}

	public String getAcquirer_name() {
		return acquirer_name;
	}

	public void setAcquirer_name(String acquirerName) {
		acquirer_name = acquirerName;
	}

	public String getAcquirer_merchant() {
		return acquirer_merchant;
	}

	public void setAcquirer_merchant(String acquirerMerchant) {
		acquirer_merchant = acquirerMerchant;
	}

	public String getAcquirer_seq() {
		return acquirer_seq;
	}

	public void setAcquirer_seq(String acquirerSeq) {
		acquirer_seq = acquirerSeq;
	}

	public String getAcquirer_date() {
		return acquirer_date;
	}

	public void setAcquirer_date(String acquirerDate) {
		acquirer_date = acquirerDate;
	}

	public String getAcquirer_msg() {
		return acquirer_msg;
	}

	public void setAcquirer_msg(String acquirerMsg) {
		acquirer_msg = acquirerMsg;
	}

	public String getPayer_ip() {
		return payer_ip;
	}

	public void setPayer_ip(String payerIp) {
		payer_ip = payerIp;
	}

	public String getRefnum() {
		return refnum;
	}

	public void setRefnum(String refnum) {
		this.refnum = refnum;
	}

	public String getAuthcode() {
		return authcode;
	}

	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}

	

	public String getPayinfo() {
		return payinfo;
	}

	public void setPayinfo(String payinfo) {
		this.payinfo = payinfo;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getClosedate() {
		return closedate;
	}

	public void setClosedate(Date closedate) {
		this.closedate = closedate;
	}

	public String getTrxsts() {
		return trxsts;
	}

	public void setTrxsts(String trxsts) {
		this.trxsts = trxsts;
	}

	public String getOpers() {
		return opers;
	}

	public void setOpers(String opers) {
		this.opers = opers;
	}

	public String getOperdate() {
		return operdate;
	}

	public void setOperdate(String operdate) {
		this.operdate = operdate;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getTrxdesc() {
		return trxdesc;
	}

	public void setTrxdesc(String trxdesc) {
		this.trxdesc = trxdesc;
	}


	public String getId() {
		return id;
	}

	public String getSubmitdates() {
		return submitdates;
	}

	public void setSubmitdates(String submitdates) {
		this.submitdates = submitdates;
	}

	

}
