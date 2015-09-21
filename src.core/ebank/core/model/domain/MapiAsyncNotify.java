package ebank.core.model.domain;

import java.util.Date;

public class MapiAsyncNotify {
	private long id;//	NUMBER(19)	N			ID
	private Date date_created;	//DATE	N			创建时间 grails
	private Date last_updated;	//DATE	Y			更新时间 grails
	private long customer_id;	//NUMBER(19)	N			客户ID
	private String record_table	;//VARCHAR2(24 CHAR)	Y			业务表
	private long   record_id	;//NUMBER(19)	Y			业务表主键
	private String notify_method;//	VARCHAR2(16 CHAR)	N			通知方法 http email mobile
	private String notify_address;//	VARCHAR2(64 CHAR)	N			通知地址
	private String notify_contents;//	CLOB	Y			通知内容
	private Date notify_time;//	DATE	Y			通知时间
	private String notify_id;//	VARCHAR2(32 CHAR)	Y			通知ID
	private Date next_attempt_time;//	DATE	Y			下次尝试时间
	private String status;//	VARCHAR2(16 CHAR)	Y			通知状态 未成功 已成功
	private int attempts_count;//	INTEGER	Y			尝试次数
	private Date time_expired;//	DATE	N			过期时间
	private int is_verify;//	NUMBER(1)	Y			已经读取验证
	private String sign_type="md5";
	private String output_charset;
	
	
	
	
	
	public String getOutput_charset() {
		return output_charset;
	}
	public void setOutput_charset(String output_charset) {
		this.output_charset = output_charset;
	}
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String signType) {
		sign_type = signType;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getDate_created() {
		return date_created;
	}
	public void setDate_created(Date dateCreated) {
		date_created = dateCreated;
	}
	public Date getLast_updated() {
		return last_updated;
	}
	public void setLast_updated(Date lastUpdated) {
		last_updated = lastUpdated;
	}
	public long getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(long customerId) {
		customer_id = customerId;
	}
	public String getRecord_table() {
		return record_table;
	}
	public void setRecord_table(String recordTable) {
		record_table = recordTable;
	}
	public long getRecord_id() {
		return record_id;
	}
	public void setRecord_id(long recordId) {
		record_id = recordId;
	}
	public String getNotify_method() {
		return notify_method;
	}
	public void setNotify_method(String notifyMethod) {
		notify_method = notifyMethod;
	}
	public String getNotify_address() {
		return notify_address;
	}
	public void setNotify_address(String notifyAddress) {
		notify_address = notifyAddress;
	}
	public String getNotify_contents() {
		return notify_contents;
	}
	public void setNotify_contents(String notifyContents) {
		notify_contents = notifyContents;
	}
	public Date getNotify_time() {
		return notify_time;
	}
	public void setNotify_time(Date notifyTime) {
		notify_time = notifyTime;
	}
	public String getNotify_id() {
		return notify_id;
	}
	public void setNotify_id(String notifyId) {
		notify_id = notifyId;
	}
	public Date getNext_attempt_time() {
		return next_attempt_time;
	}
	public void setNext_attempt_time(Date nextAttemptTime) {
		next_attempt_time = nextAttemptTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getAttempts_count() {
		return attempts_count;
	}
	public void setAttempts_count(int attemptsCount) {
		attempts_count = attemptsCount;
	}
	public Date getTime_expired() {
		return time_expired;
	}
	public void setTime_expired(Date timeExpired) {
		time_expired = timeExpired;
	}
	public int getIs_verify() {
		return is_verify;
	}
	public void setIs_verify(int isVerify) {
		is_verify = isVerify;
	}

}
