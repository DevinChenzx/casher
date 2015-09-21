package ebank.core.model.domain;

import java.io.Serializable;
import java.util.Date;

import ebank.core.common.Validator;

public class GwLgOptions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 113608558985925539L;
	@Validator(regx="POST|EXPRESS|EMS")
	private String logistics_type; // varchar(10),
	
	private String logistics_fee; // varchar(2),
	
	@Validator(regx="BUYER_PAY|SELLER_PAY|BUYER_PAY_AFTER_RECEIVE")
	private String logistics_payment; // varchar(512),
	
	private Date createdate;
	private String id;
	private String gworders_id;

	public String getLogistics_type() {
		return logistics_type;
	}

	public void setLogistics_type(String logisticsType) {
		logistics_type = logisticsType;
	}

	public String getLogistics_fee() {
		return logistics_fee;
	}

	public void setLogistics_fee(String logisticsFee) {
		logistics_fee = logisticsFee;
	}

	public String getLogistics_payment() {
		return logistics_payment;
	}

	public void setLogistics_payment(String logisticsPayment) {
		logistics_payment = logisticsPayment;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	

	public String getGworders_id() {
		return gworders_id;
	}

	public void setGworders_id(String gwordersId) {
		gworders_id = gwordersId;
	}

	

}
