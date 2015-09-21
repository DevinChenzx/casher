package ebank.core.model.domain;

import java.io.Serializable;
import java.util.Date;

import ebank.core.common.Validator;

public class TradePrecard implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2306652277359409107L;
	private String id; // number(19,0) not null,
		
	private String merid;
	//private String morderid;
	private String systemid;
	
	@Validator(maxsize = 100,nullable=false)
	private String productname;
	
	@Validator(maxsize = 22)
	private int productnum;
	
	@Validator(maxsize=13,regx = "^(\\d+)(\\.\\d{1,2})?$")
	private long amount;
	private String curcode;
	
	@Validator(maxsize = 512, regx = "^http(s)?://([\\w-]+\\.)*+[\\w-]+(:+\\d{1,})?+(/[\\w- ./?%&!=]*)?", message = "URL格式错")
	private String merrcvurl;
	
	@Validator(maxsize = 512, regx = "^http(s)?://([\\w-]+\\.)*+[\\w-]+(:+\\d{1,})?+(/[\\w- ./?%&!=]*)?", message = "URL格式错")
	private String notifyurl;
	
	@Validator(regx = "\\d{1,22}")
	private String sellerid;
	
	private Date ordertime;
	private String ext1;
	private String ext2;
	private String paystatus;
	private String signMsg;
	private String apiversion; // 接口版本号
	private String signtype;
	private String formatOrdertime;
	@Validator(maxsize = 128, nullable = false)
	private String outtradeno;
	public String getOuttradeno() {
		return outtradeno;
	}
	public void setOuttradeno(String outtradeno) {
		this.outtradeno = outtradeno;
	}
	private String paymentType;
	
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	private GwTrxs   trx;
	public GwTrxs getTrx() {
		return trx;
	}
	public void setTrx(GwTrxs trx) {
		this.trx = trx;
	}
	@Validator(regx = "GBK|UTF-8|GB2312")
	private String charsets; // 编码 

	
	public String getCharsets() {
		return charsets;
	}
	public void setCharsets(String charsets) {
		this.charsets = charsets;
	}
	public String getFormatOrdertime() {
		return formatOrdertime;
	}
	public void setFormatOrdertime(String formatOrdertime) {
		this.formatOrdertime = formatOrdertime;
	}
	public String getSellerid() {
		return sellerid;
	}
	public void setSellerid(String sellerid) {
		this.sellerid = sellerid;
	}
	public String getNotifyurl() {
		return notifyurl;
	}
	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}
	public String getSigntype() {
		return signtype;
	}
	public void setSigntype(String signtype) {
		this.signtype = signtype;
	}
	public String getApiversion() {
		return apiversion;
	}
	public void setApiversion(String apiversion) {
		this.apiversion = apiversion;
	}
	public String getSignMsg() {
		return signMsg;
	}
	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMerid() {
		return merid;
	}
	public void setMerid(String merid) {
		this.merid = merid;
	}
	/*public String getMorderid() {
		return morderid;
	}
	public void setMorderid(String morderid) {
		this.morderid = morderid;
	}*/
	public String getSystemid() {
		return systemid;
	}
	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public int getProductnum() {
		return productnum;
	}
	public void setProductnum(int productnum) {
		this.productnum = productnum;
	}	
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getCurcode() {
		return curcode;
	}
	public void setCurcode(String curcode) {
		this.curcode = curcode;
	}
	public String getMerrcvurl() {
		return merrcvurl;
	}
	public void setMerrcvurl(String merrcvurl) {
		this.merrcvurl = merrcvurl;
	}
	public Date getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	public String getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	


}
