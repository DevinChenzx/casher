package ebank.core.model.domain;

import java.io.Serializable;
import java.util.Date;

import ebank.core.common.Validator;

public final class GwSuborders implements Serializable {

	
	private static final long serialVersionUID = -2306652277359409107L;
	
	private String id; // not null,
	
	private String gwordersid; // not null,
	
	@Validator(maxsize = 64)
    private String outtradeno;//not null
    
    @Validator(maxsize = 128)
	private String seller_name; //
    
    @Validator(maxsize = 18)
	private String seller_custno; //  varchar(18) not null,
    
    @Validator(maxsize = 64)
    private String seller_code;//not null
    
    @Validator(maxsize = 256)
    private String seller_ext;
    

	@Validator(maxsize=13,regx = "^(\\d+)(\\.\\d{1,2})?$",nullable=false)
	private long amount; // amount not null,
	
	private Date createdate; // date not null,
	
	
	
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGwordersid() {
		return gwordersid;
	}

	public void setGwordersid(String gwordersid) {
		this.gwordersid = gwordersid;
	}

	public String getOuttradeno() {
		return outtradeno;
	}

	public void setOuttradeno(String outtradeno) {
		this.outtradeno = outtradeno;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}


	public String getSeller_code() {
		return seller_code;
	}

	public String getSeller_custno() {
		return seller_custno;
	}

	public void setSeller_custno(String seller_custno) {
		this.seller_custno = seller_custno;
	}

	public void setSeller_code(String seller_code) {
		this.seller_code = seller_code;
	}

	public String getSeller_ext() {
		return seller_ext;
	}

	public void setSeller_ext(String seller_ext) {
		this.seller_ext = seller_ext;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	
    
}
