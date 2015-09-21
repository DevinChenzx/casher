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
 * Description: 网关定义的服务项明细
 * 
 */
public class GW02ServiceItem {
	private long   servicenum	;         //服务项编号
	private String servicename	;         //服务项名称
	private long   parentnum;             //上级服务编号
	private String extcode	    ;         //服务外部编号
	private String extname	    ;         //外部编号名称
	private String servicestatue;         //服务状态（Y)启用 N.停用 P.暂停
	private String servicedesc	;         //服务描述
	private String serviceparam	;         //程序XML参数
	
	
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
