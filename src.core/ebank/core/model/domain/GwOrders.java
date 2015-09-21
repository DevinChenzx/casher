package ebank.core.model.domain;

import java.io.Serializable;
import java.util.Date;

import ebank.core.common.Constants;
import ebank.core.common.Validator;

public class GwOrders implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2306652277359409107L;
	private String id; // number(19,0) not null,
		
	@Validator(maxsize = 2, nullable = false,regx = "1|2|3")  //支付方式
	private String order_type;

	@Validator(maxsize = 128, nullable = false)
	private String ordernum; // varchar(128) not null,

	@Validator(maxsize = 64)
	private String seller_name; // login receipt varchar(64) not null,

	@Validator(regx = "\\d{1,19}")
	private String seller_id;   //customer num
	
	@Validator(maxsize = 64)
	private String buyer_name;

	@Validator(regx = "\\d{1,19}")
	private String buyer_id;

	private String trxtype; // [BI:TR]

	@Validator(regx = "\\d{10,19}",nullable=false)
	private long partnerid;

	@Validator(maxsize=13,regx = "^(\\d+)(\\.\\d{1,2})?$")
	private long price;

	@Validator(regx = "\\d{1,9}")
	private int quantity;

	@Validator(maxsize=13,regx = "^(\\d+)(\\.\\d{1,2})?$",nullable=false)
	private long amount; // int not null,

	@Validator(maxsize = 4)
	private String currency; // varchar(4) not null,

	@Validator(maxsize = 14, regx = "\\d{8}||\\d{14}")  //extend to yyyymmddHHmiss
	private String orderdate; // varchar(8) not null,

	private Date createdate; // date not null,

	@Validator(maxsize = 64)
	private String query_key; // varchar(64),

	@Validator(length = 8, regx = "\\d{1,2}h|d|m$")
	private String exp_dates; // date not null,

	@Validator(maxsize = 20)
	private String ips; // varchar(20) not null,

	private String ordersts; // varchar(3) not null,

	@Validator(maxsize = 512)
	private String seller_remarks; // varchar(200),

	@Validator(maxsize = 512)
	private String buyer_remarks; // varchar(1024),

	@Validator(maxsize = 512, regx = "^http(s)?://([\\w-]+\\.)*+[\\w-]+(:+\\d{1,})?+(/[\\w- ./?!%&=]*)?", message = "URL格式错")
	private String return_url; // varchar(512),

	@Validator(maxsize = 512, regx = "^http(s)?://([\\w-]+\\.)*+[\\w-]+(:+\\d{1,})?+(/[\\w- ./?!%&=]*)?", message = "URL格式错")
	private String notify_url; // varchar(512),
	
	private long refund_amount;
	private String refund_sts;

	@Validator(maxsize = 256,nullable=false)
	private String subject; // varchar(128),

	@Validator(nullable=false,maxsize = 512)
	private String bodys; // varchar(512),

	private String locale;	
	private String apiversion; // 接口版本号
	private String gwlgoptions_id; // 物流付费ID
	private int pricechanged; // 价格是否修改

	@Validator(regx = "DSA|RSA|MD5",nullable=false)
	private String sign_type; //

	@Validator(maxsize = 512, regx = "^http(s)?://([\\w-]+\\.)*+[\\w-]+(:+\\d{1,})?+(/[\\w- ./?%&=]*)?", message = "URL格式错")
	private String show_url;

	@Validator(maxsize=18)
	private int discount; //
	private Date gwl_update;	
	
	@Validator(regx = "GBK|UTF-8|GB2312")
	private String charsets; // 编码

	@Validator(maxsize = 64,nullable=false)
	private String service;

	@Validator(regx = "10|12")
	private String royalty_type;
	//收款方 Email_1^金额1^备注1|付款方 Email^收款方 Email_2^金额2^备注2
	@Validator(maxsize = 512)//,regx="^("+Constants.REG_EMAIL+"\\^"+Constants.REG_MONEY2+"\\^+\\w+)+(\\|"+Constants.REG_EMAIL+"\\^"+Constants.REG_EMAIL+"\\^"+Constants.REG_MONEY2+"\\^\\w+)*")
	private String royalty_parameters;

	private int version;
	
	private String discountdesc;
	
	private String discount_mode;
	
	@Validator(regx = "\\d{1,19}")  //航空代理
	private String agentid;

	@Validator(maxsize = 25,nullable=false)
	private String preference; // defaultbank
	
	 //bankPay,银行卡;cartoon,卡通;balancePay,余额;directPay,直连银行;creditPay,信用卡;CASH,网点;
	@Validator(regx="directPay|bankPay|cartoon|balancePay|creditPay|CASH")
	private String paymethod;  // 支付方式
	
	private long service_fee;  
	
	private Date closedate;
	private long partner_id;
	
	private long  directpayamt; //余额付款金额
	
	@Validator(maxsize = 32)
	private String buyer_realname; //买家实名
	
	@Validator(maxsize = 64)
	private String buyer_contact;  //买家联系方式
	
	@Validator(maxsize = 512)  
	private String ext_param1;  //扩展参数1
	
	@Validator(maxsize = 512) //扩展参数2
	private String ext_param2;
	
	private String pay_cus_no;//付款客户号
	
	
	public String getPay_cus_no() {
		return pay_cus_no;
	}

	public void setPay_cus_no(String pay_cus_no) {
		this.pay_cus_no = pay_cus_no;
	}

	public String getExt_param1() {
		return ext_param1;
	}

	public void setExt_param1(String ext_param1) {
		this.ext_param1 = ext_param1;
	}

	public String getExt_param2() {
		return ext_param2;
	}

	public void setExt_param2(String ext_param2) {
		this.ext_param2 = ext_param2;
	}

	public long getDirectpayamt() {
		return directpayamt;
	}

	public void setDirectpayamt(long directpayamt) {
		this.directpayamt = directpayamt;
	}

	public String getBuyer_realname() {
		return buyer_realname;
	}

	public void setBuyer_realname(String buyer_realname) {
		this.buyer_realname = buyer_realname;
	}

	public String getBuyer_contact() {
		return buyer_contact;
	}

	public void setBuyer_contact(String buyer_contact) {
		this.buyer_contact = buyer_contact;
	}

	public long getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(long partnerId) {
		partner_id = partnerId;
	}

	public Date getClosedate() {
		return closedate;
	}

	public void setClosedate(Date closedate) {
		this.closedate = closedate;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public long getService_fee() {
		return service_fee;
	}

	public void setService_fee(long serviceFee) {
		service_fee = serviceFee;
	}

	public String getDiscount_mode() {
		return discount_mode;
	}

	public void setDiscount_mode(String discountMode) {
		discount_mode = discountMode;
	}

	public String getDiscountdesc() {
		return discountdesc;
	}

	public void setDiscountdesc(String discountdesc) {
		this.discountdesc = discountdesc;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String orderType) {
		order_type = orderType;
	}

	public long getRefund_amount() {
		return refund_amount;
	}

	public void setRefund_amount(long refundAmount) {
		refund_amount = refundAmount;
	}

	public String getRefund_sts() {
		return refund_sts;
	}

	public void setRefund_sts(String refundSts) {
		refund_sts = refundSts;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getRoyalty_type() {
		return royalty_type;
	}

	public void setRoyalty_type(String royaltyType) {
		royalty_type = royaltyType;
	}

	public String getRoyalty_parameters() {
		return royalty_parameters;
	}

	public void setRoyalty_parameters(String royaltyParameters) {
		royalty_parameters = royaltyParameters;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPreference() {
		return preference;
	}

	public void setPreference(String preference) {
		this.preference = preference;
	}

	public String getTrxtype() {
		return trxtype;
	}

	public void setTrxtype(String trxtype) {
		this.trxtype = trxtype;
	}

	public String getCharsets() {
		return charsets;
	}

	public void setCharsets(String charsets) {
		this.charsets = charsets;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public Date getGwl_update() {
		return gwl_update;
	}

	public void setGwl_update(Date gwlUpdate) {
		gwl_update = gwlUpdate;
	}

	public String getShow_url() {
		return show_url;
	}

	public void setShow_url(String showUrl) {
		show_url = showUrl;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String signType) {
		sign_type = signType;
	}

	public long getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(long partnerid) {
		this.partnerid = partnerid;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String getGwlgoptions_id() {
		return gwlgoptions_id;
	}

	public void setGwlgoptions_id(String gwlgoptionsId) {
		gwlgoptions_id = gwlgoptionsId;
	}

	public int getPricechanged() {
		return pricechanged;
	}

	public void setPricechanged(int pricechanged) {
		this.pricechanged = pricechanged;
	}
	

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getQuery_key() {
		return query_key;
	}

	public void setQuery_key(String queryKey) {
		query_key = queryKey;
	}

	public String getIps() {
		return ips;
	}

	public void setIps(String ips) {
		this.ips = ips;
	}

	public String getOrdersts() {
		return ordersts;
	}

	public void setOrdersts(String ordersts) {
		this.ordersts = ordersts;
	}

	public String getSeller_remarks() {
		return seller_remarks;
	}

	public void setSeller_remarks(String sellerRemarks) {
		seller_remarks = sellerRemarks;
	}

	public String getBuyer_remarks() {
		return buyer_remarks;
	}

	public void setBuyer_remarks(String buyerRemarks) {
		buyer_remarks = buyerRemarks;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String returnUrl) {
		return_url = returnUrl;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notifyUrl) {
		notify_url = notifyUrl;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBodys() {
		return bodys;
	}

	public void setBodys(String bodys) {
		this.bodys = bodys;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getApiversion() {
		return apiversion;
	}

	public void setApiversion(String apiversion) {
		this.apiversion = apiversion;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String sellerName) {
		seller_name = sellerName;
	}

	public String getBuyer_name() {
		return buyer_name;
	}

	public void setBuyer_name(String buyerName) {
		buyer_name = buyerName;
	}

	public String getExp_dates() {
		return exp_dates;
	}

	public void setExp_dates(String expDates) {
		exp_dates = expDates;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String sellerId) {
		seller_id = sellerId;
	}

	public String getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(String buyerId) {
		buyer_id = buyerId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

}
