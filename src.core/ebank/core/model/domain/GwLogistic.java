package ebank.core.model.domain;

import java.io.Serializable;
import java.util.Date;

public class GwLogistic implements Serializable{
	
	   /**
	 * 
	 */
	private static final long serialVersionUID = 3161521746803254629L;
	private String gworders_id  ;//        number(19,0),
	   private String recname  ;//            varchar(10)                     not null,
	   private String recpid  ;//             varchar(30),
	   private String recphone ;//            varchar(30),
	   private String recaddr  ;//            varchar(128),
	   private String recmphone ;//           varchar(30),
	   private String recpost ; //
	   private String deliver  ;//            varchar(20),
	   private String delivertime ;//         varchar(128),
	   private String delivers;//             varchar(10),
	   private Date createate;//            date                            not null,
	   private long id;//                   number(19,0)                    not null,
	   
	
	public String getGworders_id() {
		return gworders_id;
	}
	public void setGworders_id(String gwordersId) {
		gworders_id = gwordersId;
	}
	public String getRecname() {
		return recname;
	}
	public void setRecname(String recname) {
		this.recname = recname;
	}
	public String getRecpid() {
		return recpid;
	}
	public void setRecpid(String recpid) {
		this.recpid = recpid;
	}
	public String getRecphone() {
		return recphone;
	}
	public void setRecphone(String recphone) {
		this.recphone = recphone;
	}
	public String getRecaddr() {
		return recaddr;
	}
	public void setRecaddr(String recaddr) {
		this.recaddr = recaddr;
	}
	public String getRecmphone() {
		return recmphone;
	}
	public void setRecmphone(String recmphone) {
		this.recmphone = recmphone;
	}
	public String getRecpost() {
		return recpost;
	}
	public void setRecpost(String recpost) {
		this.recpost = recpost;
	}
	public String getDeliver() {
		return deliver;
	}
	public void setDeliver(String deliver) {
		this.deliver = deliver;
	}
	public String getDelivertime() {
		return delivertime;
	}
	public void setDelivertime(String delivertime) {
		this.delivertime = delivertime;
	}
	public String getDelivers() {
		return delivers;
	}
	public void setDelivers(String delivers) {
		this.delivers = delivers;
	}
	public Date getCreateate() {
		return createate;
	}
	public void setCreateate(Date createate) {
		this.createate = createate;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	   
	   

}
