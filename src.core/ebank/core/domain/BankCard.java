package ebank.core.domain;

// default package

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class BankCard implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3229372269070695343L;

	/** persistent field */
    private int bciid;

    /** nullable persistent field */
    private int bid;

    /** nullable persistent field */
    private String cardname;

    /** nullable persistent field */
    private String blogon;

    /** nullable persistent field */
    private int borderby;

    /** full constructor */
    public BankCard(int bciid, int bid, java.lang.String cardname, java.lang.String blogon, int borderby) {
        this.bciid = bciid;
        this.bid = bid;
        this.cardname = cardname;
        this.blogon = blogon;
        this.borderby = borderby;
    }

    /** default constructor */
    public BankCard() {
    }

    public int getBciid() {
        return this.bciid;
    }

	public void setBciid(int bciid) {
		this.bciid = bciid;
	}

    public int getBid() {
        return this.bid;
    }

	public void setBid(int bid) {
		this.bid = bid;
	}

    public java.lang.String getCardname() {
        return this.cardname;
    }

	public void setCardname(java.lang.String cardname) {
		this.cardname = cardname;
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
