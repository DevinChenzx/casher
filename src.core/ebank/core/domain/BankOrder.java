/*
 * Created on 2004-11-30
 */
package ebank.core.domain;


import java.util.HashMap;
/**
 * @author xiexh
 * Description: ���͵����еĶ���
 * 
 */
public class BankOrder{
		
	private static final long serialVersionUID = 1L;
	private String RandOrderID;//�����������
	private String BankID;//���б��
	private String payaccount;//�����˺�
	private String merchantid;//�̻����
	private String ordernum;//�̻������� xxx-�̻���-xxx��ʽ ���20λ������Ϊ���ֺ���ĸ�����	
	
	private String amount;//���	
	private String orderName;//������
	private String currency;//����	

	private String postdate;   //���͵����е�ʱ��
	private String custip;     //�ύ��IP
	private String referer;   //source
	
	private String version;//�ӿڰ汾��
	private HashMap mp;	
	
	private CreditCardOrder cct;
	private String slas;
	
	private String payment_type; //1:  b2c��ǿ���2��b2c���ǿ���3���ÿ��ͽ�ǿ�
	
	
	
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	public CreditCardOrder getCct() {
		return cct;
	}
	public void setCct(CreditCardOrder cct) {
		this.cct = cct;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public String getCustip() {
		return custip;
	}
	public void setCustip(String custip) {
		this.custip = custip;
	}
	/**
	 * @return Returns the slas.
	 */
	public String getSlas() {
		return slas;
	}
	/**
	 * @param slas The slas to set.
	 */
	public void setSlas(String slas) {
		this.slas = slas;
	}
	
	/**
	 * @return Returns the postdate.
	 */
	public String getPostdate() {
		return postdate;
	}
	/**
	 * @param postdate The postdate to set.
	 */
	public void setPostdate(String postdate) {
		this.postdate = postdate;
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
	 * @return Returns the merchantid.
	 */
	public String getMerchantid() {
		return merchantid;
	}
	/**
	 * @param merchantid The merchantid to set.
	 */
	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}
	/**
	 * @return Returns the ordernum.
	 */
	public String getOrdernum() {
		return ordernum;
	}
	/**
	 * @param ordernum The ordernum to set.
	 */
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	/**
	 * @return Returns the amount.
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * @param amount The amount to set.
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}
	/**
	 * @return Returns the bankID.
	 */
	public String getBankID() {
		return BankID;
	}
	/**
	 * @param bankID The bankID to set.
	 */
	public void setBankID(String bankID) {
		BankID = bankID;
	}
	
		
	public String getPayaccount() {
		return payaccount;
	}
	public void setPayaccount(String payaccount) {
		this.payaccount = payaccount;
	}
	/**
	 * @return Returns the orderName.
	 */
	public String getOrderName() {
		return orderName;
	}
	/**
	 * @param orderName The orderName to set.
	 */
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}	
	/**
	 * @return Returns the randOrderID.
	 */
	public String getRandOrderID() {
		return RandOrderID;
	}
	/**
	 * @param randOrderID The randOrderID to set.
	 */
	public void setRandOrderID(String randOrderID) {
		RandOrderID = randOrderID;
	}		
	
    /**
     * @return Returns the version.
     */
    public String getVersion() {
        if(version==null)version="4.0";
        return version;
    }
    /**
     * @param version The version to set.
     */
    public void setVersion(String version) {
        this.version = version;
    }
	/**
	 * @return Returns the mp.
	 */
	public HashMap getMp() {
		return mp;
	}
	/**
	 * @param mp The mp to set.
	 */
	public void setMp(HashMap mp) {
		this.mp = mp;
	}
    
}
