/*
 * @Id: GW00Merchant.java 16:20:23 2006-2-13
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.model.domain;

import java.io.Serializable;

/**
 * @author xiexh
 * Description: 
 * 
 */
public class GwViewUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7832289071586865739L;
	private long    userId	;          
	private String  userAlias	;     	
	private String  md5Key		; 
	private int     mstate		;  
	private String  customer_no ;  
	private String  service_code;
	private String  login_recepit;
	private String  status;       
	
	private int  operid;       
	private int  iscurrent;   
	
	private String fee_params; 
	private String service_params; 
	private String userMobile;
	
	private String fraud_check;
	private String company_website;
	
	
	
	public String getFraud_check() {
		return fraud_check;
	}


	public void setFraud_check(String fraud_check) {
		this.fraud_check = fraud_check;
	}


	public String getCompany_website() {
		return company_website;
	}


	public void setCompany_website(String company_website) {
		this.company_website = company_website;
	}


	public String getUserMobile() {
		return userMobile;
	}


	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}


	public String getFee_params() {
		return fee_params;
	}


	public void setFee_params(String fee_params) {
		this.fee_params = fee_params;
	}


	public String getService_params() {
		return service_params;
	}


	public void setService_params(String service_params) {
		this.service_params = service_params;
	}


	public int getIscurrent() {
		return iscurrent;
	}


	public void setIscurrent(int iscurrent) {
		this.iscurrent = iscurrent;
	}


	public int getOperid() {
		return operid;
	}


	public void setOperid(int operid) {
		this.operid = operid;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getLogin_recepit() {
		return login_recepit;
	}


	public void setLogin_recepit(String loginRecepit) {
		login_recepit = loginRecepit;
	}


	public String getService_code() {
		return service_code;
	}


	public void setService_code(String serviceCode) {
		service_code = serviceCode;
	}


	public long getUserId() {
		return userId;
	}


	public void setUserId(long userId) {
		this.userId = userId;
	}


	public String getUserAlias() {
		return userAlias;
	}


	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}


	public String getMd5Key() {
		return md5Key;
	}


	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
	}


	
	/**
	 * @return Returns the mstate.
	 */
	public int getMstate() {
		return mstate;
	}
	/**
	 * @param mstate The mstate to set.
	 */
	public void setMstate(int mstate) {
		this.mstate = mstate;
	}


	public String getCustomer_no() {
		return customer_no;
	}


	public void setCustomer_no(String customerNo) {
		customer_no = customerNo;
	}
	
	
	
	
	
	
	

	
	


}
