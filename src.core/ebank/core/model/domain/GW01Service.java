/*
 * @Id: GW01Service.java 16:11:31 2006-2-13
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.model.domain;

import java.util.Date;

/**
 * @author 
 * Description: �����̻�����
 * 
 */
public class GW01Service {
	
	private long serviceid ;    //������
	private int servicenum ;    //��������
	private String merchantid ;    //�̻��ڹ����̨�ı��
	private int servicecode ;    //ҵ������	
	private String merchantaccount ;    //�̻��ʻ�
	private String authcode ;    //��Ȩ��
	private int servicestate ;    //��ͨ����ֹ
	private int servicelevel ;    //����ȼ�
	private String contractnum ;    //��ͬ���
	private Date createdate ;    //����ʱ��
	private String startdate ;    //������Ч��(yyyyMMdd)
	private String expiredate ;    //������Ч��(yyyyMMdd)
	
	
	/**
	 * @return Returns the authcode.
	 */
	public String getAuthcode() {
		return authcode;
	}
	/**
	 * @param authcode The authcode to set.
	 */
	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}
	/**
	 * @return Returns the contractnum.
	 */
	public String getContractnum() {
		return contractnum;
	}
	/**
	 * @param contractnum The contractnum to set.
	 */
	public void setContractnum(String contractnum) {
		this.contractnum = contractnum;
	}
	/**
	 * @return Returns the createdate.
	 */
	public Date getCreatedate() {
		return createdate;
	}
	/**
	 * @param createdate The createdate to set.
	 */
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	
	/**
	 * @return Returns the merchantaccount.
	 */
	public String getMerchantaccount() {
		return merchantaccount;
	}
	/**
	 * @param merchantaccount The merchantaccount to set.
	 */
	public void setMerchantaccount(String merchantaccount) {
		this.merchantaccount = merchantaccount;
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
	 * @return Returns the servicecode.
	 */
	public int getServicecode() {
		return servicecode;
	}
	/**
	 * @param servicecode The servicecode to set.
	 */
	public void setServicecode(int servicecode) {
		this.servicecode = servicecode;
	}
	/**
	 * @return Returns the serviceid.
	 */
	public long getServiceid() {
		return serviceid;
	}
	/**
	 * @param serviceid The serviceid to set.
	 */
	public void setServiceid(long serviceid) {
		this.serviceid = serviceid;
	}
	/**
	 * @return Returns the servicelevel.
	 */
	public int getServicelevel() {
		return servicelevel;
	}
	/**
	 * @param servicelevel The servicelevel to set.
	 */
	public void setServicelevel(int servicelevel) {
		this.servicelevel = servicelevel;
	}
	/**
	 * @return Returns the servicenum.
	 */
	public int getServicenum() {
		return servicenum;
	}
	/**
	 * @param servicenum The servicenum to set.
	 */
	public void setServicenum(int servicenum) {
		this.servicenum = servicenum;
	}
	/**
	 * @return Returns the servicestate.
	 */
	public int getServicestate() {
		return servicestate;
	}
	/**
	 * @param servicestate The servicestate to set.
	 */
	public void setServicestate(int servicestate) {
		this.servicestate = servicestate;
	}
	/**
	 * @return Returns the expiredate.
	 */
	public String getExpiredate() {
		return expiredate;
	}
	/**
	 * @param expiredate The expiredate to set.
	 */
	public void setExpiredate(String expiredate) {
		this.expiredate = expiredate;
	}
	/**
	 * @return Returns the startdate.
	 */
	public String getStartdate() {
		return startdate;
	}
	/**
	 * @param startdate The startdate to set.
	 */
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	
	
	
	
	

}
