/*
 * @Id: GW02ServiceItem.java 16:41:52 2006-2-13
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.model.domain;

/**
 * @author 
 * Description: ���ض���ķ�������ϸ
 * 
 */
public class GW02ServiceItem {
	private long   servicenum	;         //��������
	private String servicename	;         //����������
	private long   parentnum;             //�ϼ�������
	private String extcode	    ;         //�����ⲿ���
	private String extname	    ;         //�ⲿ�������
	private String servicestatue;         //����״̬��Y)���� N.ͣ�� P.��ͣ
	private String servicedesc	;         //��������
	private String serviceparam	;         //����XML����
	
	
	/**
	 * @return Returns the parentnum.
	 */
	public long getParentnum() {
		return parentnum;
	}
	/**
	 * @param parentnum The parentnum to set.
	 */
	public void setParentnum(long parentnum) {
		this.parentnum = parentnum;
	}
	/**
	 * @return Returns the extcode.
	 */
	public String getExtcode() {
		return extcode;
	}
	/**
	 * @param extcode The extcode to set.
	 */
	public void setExtcode(String extcode) {
		this.extcode = extcode;
	}
	/**
	 * @return Returns the extname.
	 */
	public String getExtname() {
		return extname;
	}
	/**
	 * @param extname The extname to set.
	 */
	public void setExtname(String extname) {
		this.extname = extname;
	}
	/**
	 * @return Returns the servicedesc.
	 */
	public String getServicedesc() {
		return servicedesc;
	}
	/**
	 * @param servicedesc The servicedesc to set.
	 */
	public void setServicedesc(String servicedesc) {
		this.servicedesc = servicedesc;
	}
	/**
	 * @return Returns the servicename.
	 */
	public String getServicename() {
		return servicename;
	}
	/**
	 * @param servicename The servicename to set.
	 */
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	/**
	 * @return Returns the servicenum.
	 */
	public long getServicenum() {
		return servicenum;
	}
	/**
	 * @param servicenum The servicenum to set.
	 */
	public void setServicenum(long servicenum) {
		this.servicenum = servicenum;
	}
	/**
	 * @return Returns the serviceparam.
	 */
	public String getServiceparam() {
		return serviceparam;
	}
	/**
	 * @param serviceparam The serviceparam to set.
	 */
	public void setServiceparam(String serviceparam) {
		this.serviceparam = serviceparam;
	}
	/**
	 * @return Returns the servicestatue.
	 */
	public String getServicestatue() {
		return servicestatue;
	}
	/**
	 * @param servicestatue The servicestatue to set.
	 */
	public void setServicestatue(String servicestatue) {
		this.servicestatue = servicestatue;
	}
	
	
	
	
	


}
