package ebank.core.model.domain;

import java.util.Date;

public class MerchantCertificates {

	private String merchantNo;
	private String merchantPublicKey;
	private String merchantPwd;
	private String merchantPrivateKey;
	private String storePwd;
	private Date createDate;
	
	
	
	public String getStorePwd() {
		return storePwd;
	}
	public void setStorePwd(String storePwd) {
		this.storePwd = storePwd;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getMerchantPublicKey() {
		return merchantPublicKey;
	}
	public void setMerchantPublicKey(String merchantPublicKey) {
		this.merchantPublicKey = merchantPublicKey;
	}
	public String getMerchantPwd() {
		return merchantPwd;
	}
	public void setMerchantPwd(String merchantPwd) {
		this.merchantPwd = merchantPwd;
	}
	public String getMerchantPrivateKey() {
		return merchantPrivateKey;
	}
	public void setMerchantPrivateKey(String merchantPrivateKey) {
		this.merchantPrivateKey = merchantPrivateKey;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	

}
