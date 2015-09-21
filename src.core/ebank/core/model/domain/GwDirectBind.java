package ebank.core.model.domain;

public class GwDirectBind 
{
	private long id;
	private String account_no;
	private String customer_no;
	private String limite_amount;
	private String pay_customer_no;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAccount_no() {
		return account_no;
	}
	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}
	public String getCustomer_no() {
		return customer_no;
	}
	public void setCustomer_no(String customer_no) {
		this.customer_no = customer_no;
	}
	public String getLimite_amount() {
		return limite_amount;
	}
	public void setLimite_amount(String limite_amount) {
		this.limite_amount = limite_amount;
	}
	public String getPay_customer_no() {
		return pay_customer_no;
	}
	public void setPay_customer_no(String pay_customer_no) {
		this.pay_customer_no = pay_customer_no;
	}
}
