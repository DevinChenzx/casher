package ebank.core.model.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Kitian
 *
 */
public class GwChannel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id; //bo_merchant.id
	private String acquire_code;
	private String acquire_indexc;
	private String acquire_merchant;
	private String terminal;
	private String acquire_name;
	private String service_code;
	private String channel_type;
	private String payment_type;
	private String payment_mode;
	private Date createdate;
	private String channel_sts;
	private String channel_desc;
	private long day_qutor;
	private long qutor;
	private long account_id;  //bo_merchant.inner_acount_no
	private String acquire_acctid; //bo_merchant.bank_account_no
	private String acquire_acct_id;//bo_merchant.acquire_account_id
	
	
	public String getAcquire_acct_id() {
		return acquire_acct_id;
	}
	public void setAcquire_acct_id(String acquire_acct_id) {
		this.acquire_acct_id = acquire_acct_id;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAcquire_code() {
		return acquire_code;
	}
	public void setAcquire_code(String acquireCode) {
		acquire_code = acquireCode;
	}
	public String getAcquire_indexc() {
		return acquire_indexc;
	}
	public void setAcquire_indexc(String acquireIndexc) {
		acquire_indexc = acquireIndexc;
	}
	public String getAcquire_merchant() {
		return acquire_merchant;
	}
	public void setAcquire_merchant(String acquireMerchant) {
		acquire_merchant = acquireMerchant;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getAcquire_name() {
		return acquire_name;
	}
	public void setAcquire_name(String acquireName) {
		acquire_name = acquireName;
	}
	public String getService_code() {
		return service_code;
	}
	public void setService_code(String serviceCode) {
		service_code = serviceCode;
	}
	public String getChannel_type() {
		return channel_type;
	}
	public void setChannel_type(String channelType) {
		channel_type = channelType;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String paymentType) {
		payment_type = paymentType;
	}
	public String getPayment_mode() {
		return payment_mode;
	}
	public void setPayment_mode(String paymentMode) {
		payment_mode = paymentMode;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getChannel_sts() {
		return channel_sts;
	}
	public void setChannel_sts(String channelSts) {
		channel_sts = channelSts;
	}
	public String getChannel_desc() {
		return channel_desc;
	}
	public void setChannel_desc(String channelDesc) {
		channel_desc = channelDesc;
	}	
	public long getDay_qutor() {
		return day_qutor;
	}
	public void setDay_qutor(long day_qutor) {
		this.day_qutor = day_qutor;
	}
	public long getQutor() {
		return qutor;
	}
	public void setQutor(long qutor) {
		this.qutor = qutor;
	}	
	public long getAccount_id() {
		return account_id;
	}
	public void setAccount_id(long accountId) {
		account_id = accountId;
	}
	public String getAcquire_acctid() {
		return acquire_acctid;
	}
	public void setAcquire_acctid(String acquireAcctid) {
		acquire_acctid = acquireAcctid;
	}
	
	
	
	

}
