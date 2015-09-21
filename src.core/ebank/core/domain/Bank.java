package ebank.core.domain;

// default package

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Bank implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4112982969526758772L;

	/** persistent field */
    private int bid;             //银行标识

    /** nullable persistent field */
    private String bankname;     //银行名称

    /** nullable persistent field */
    private String banklogo;

    /** nullable persistent field */
    private double bpound;

    /** nullable persistent field */
    private String blogon;

    /** nullable persistent field */
    private int borderby;

    /** full constructor */
    public Bank(int bid, java.lang.String bankname, java.lang.String banklogo, double bpound, java.lang.String blogon, int borderby) {
        this.bid = bid;
        this.bankname = bankname;
        this.banklogo = banklogo;
        this.bpound = bpound;
        this.blogon = blogon;
        this.borderby = borderby;
    }

    /** default constructor */
    public Bank() {
    }

    public int getBid() {
        return this.bid;
    }

	public void setBid(int bid) {
		this.bid = bid;
	}

    public java.lang.String getBankname() {
        return this.bankname;
    }

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

    public java.lang.String getBanklogo() {
        return this.banklogo;
    }

	public void setBanklogo(java.lang.String banklogo) {
		this.banklogo = banklogo;
	}

    public double getBpound() {
        return this.bpound;
    }

	public void setBpound(double bpound) {
		this.bpound = bpound;
	}

    public java.lang.String getBlogon() {
        return this.blogon;
    }

	public void setBlogon(java.lang.String blogon) {
		this.blogon = blogon;
	}

    public int getBorderby() {
        return this.borderby;
    }

	public void setBorderby(int borderby) {
		this.borderby = borderby;
	}

    public String toString() {
        return new ToStringBuilder(this)
            .toString();
    }

}
