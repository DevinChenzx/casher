package ebank.core.model.domain;

import java.io.Serializable;

public class CmCorporationInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String customerNo;
	
	private double  dayQutor; 
	
	private double  dayQutorCount;
	
	private double  qutor;
	

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public double getDayQutor() {
		return dayQutor;
	}

	public void setDayQutor(double dayQutor) {
		this.dayQutor = dayQutor;
	}

	public double getDayQutorCount() {
		return dayQutorCount;
	}

	public void setDayQutorCount(double dayQutorCount) {
		this.dayQutorCount = dayQutorCount;
	}

	public double getQutor() {
		return qutor;
	}

	public void setQutor(double qutor) {
		this.qutor = qutor;
	}	
}
