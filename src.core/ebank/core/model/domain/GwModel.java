package ebank.core.model.domain;

import java.io.Serializable;

public class GwModel implements Serializable {

	private static final long serialVersionUID = -2306652277359409107L;
	
	private String id;
	
	private String customer_no;
	
	private String pay_model;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomer_no() {
		return customer_no;
	}

	public void setCustomer_no(String customer_no) {
		this.customer_no = customer_no;
	}

	public String getPay_model() {
		return pay_model;
	}

	public void setPay_model(String pay_model) {
		this.pay_model = pay_model;
	}

	
}
