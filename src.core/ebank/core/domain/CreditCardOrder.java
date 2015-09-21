/*
 * Created on 2004-12-1
 */
package ebank.core.domain;

import java.io.Serializable;

/**
 * @author fengwh
 *
 */
public class CreditCardOrder implements Serializable{	
	
	private static final long serialVersionUID = 1L;
	private String pan;        //信用卡号
	private String expiry;     //有效期
	private String currency;   //币种	
	private String holder;     //持卡人姓名
	private String holderid;   //持卡人证件
	private String cardtype;   //卡类型
	private String csc;        //卡安全码
	private String custphone;  //持卡人电话
	private String custemail;  //持卡人邮箱
	private String custworkphone;//工作电话
	
	/* 备用 */
	private String installments;
	private String recur_frequency;
	private String recur_expiry;
	
	private String exp;//币种的指数因子?
	private String desc;
	private String language; //语言版本
	
	private String pagekvs;
	
	
	
	
	
	public String getCustemail() {
		return custemail;
	}
	public void setCustemail(String custemail) {
		this.custemail = custemail;
	}
	public String getCustworkphone() {
		return custworkphone;
	}
	public void setCustworkphone(String custworkphone) {
		this.custworkphone = custworkphone;
	}
	public String getCustphone() {
		return custphone;
	}
	public void setCustphone(String custphone) {
		this.custphone = custphone;
	}
	public String getPagekvs() {
		return pagekvs;
	}
	public void setPagekvs(String pagekvs) {
		this.pagekvs = pagekvs;
	}
	/**
	 * @return Returns the csc.
	 */
	public String getCsc() {
		return csc;
	}
	/**
	 * @param csc The csc to set.
	 */
	public void setCsc(String csc) {
		this.csc = csc;
	}
	/**
	 * @return Returns the holderid.
	 */
	public String getHolderid() {
		return holderid;
	}
	/**
	 * @param holderid The holderid to set.
	 */
	public void setHolderid(String holderid) {
		this.holderid = holderid;
	}
	/**
	 * @return Returns the cardtype.
	 */
	public String getCardtype() {
		return cardtype;
	}
	/**
	 * @param cardtype The cardtype to set.
	 */
	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}
	/**
	 * @return Returns the holder.
	 */
	public String getHolder() {
		return holder;
	}
	/**
	 * @param holder The holder to set.
	 */
	public void setHolder(String holder) {
		this.holder = holder;
	}	
	/**
	 * @return Returns the currency.
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency The currency to set.
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	 * @return Returns the desc.
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc The desc to set.
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @return Returns the expiry.
	 */
	public String getExpiry() {
		return expiry;
	}
	/**
	 * @param expiry The expiry to set.
	 */
	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}
	/**
	 * @return Returns the installments.
	 */
	public String getInstallments() {
		return installments;
	}
	/**
	 * @param installments The installments to set.
	 */
	public void setInstallments(String installments) {
		this.installments = installments;
	}
	/**
	 * @return Returns the language.
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language The language to set.
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	/**
	 * @return Returns the pan.
	 */
	public String getPan() {
		return pan;
	}
	/**
	 * @param pan The pan to set.
	 */
	public void setPan(String pan) {
		this.pan = pan;
	}
	/**
	 * @return Returns the recur_expiry.
	 */
	public String getRecur_expiry() {
		return recur_expiry;
	}
	/**
	 * @param recur_expiry The recur_expiry to set.
	 */
	public void setRecur_expiry(String recur_expiry) {
		this.recur_expiry = recur_expiry;
	}
	/**
	 * @return Returns the recur_frequency.
	 */
	public String getRecur_frequency() {
		return recur_frequency;
	}
	/**
	 * @param recur_frequency The recur_frequency to set.
	 */
	public void setRecur_frequency(String recur_frequency) {
		this.recur_frequency = recur_frequency;
	}
	


	
	/**
	 * @return Returns the exp.
	 */
	public String getExp() {
		return exp;
	}
	/**
	 * @param exp The exp to set.
	 */
	public void setExp(String exp) {
		this.exp = exp;
	}
}
