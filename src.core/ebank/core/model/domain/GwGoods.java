package ebank.core.model.domain;

import java.io.Serializable;
import java.util.Date;

public class GwGoods implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4412176336652100620L;
	private String gworders_id;// number(19,0),
	private long id;// number(19,0) not null,
	private String goodid;// varchar(64),
	private String goodname;// varchar(128) not null,
	private int counts;// int not null,
	private Double unitprice;// decimal(10,2),
	private Double amount;// decimal(10,2) not null,
	private Date createdate;// date not null,
	private String gooddesc;// varchar(256),

	public String getGworders_id() {
		return gworders_id;
	}

	public void setGworders_id(String gwordersId) {
		gworders_id = gwordersId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGoodid() {
		return goodid;
	}

	public void setGoodid(String goodid) {
		this.goodid = goodid;
	}

	public String getGoodname() {
		return goodname;
	}

	public void setGoodname(String goodname) {
		this.goodname = goodname;
	}

	public int getCounts() {
		return counts;
	}

	public void setCounts(int counts) {
		this.counts = counts;
	}

	public Double getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(Double unitprice) {
		this.unitprice = unitprice;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getGooddesc() {
		return gooddesc;
	}

	public void setGooddesc(String gooddesc) {
		this.gooddesc = gooddesc;
	}

}
