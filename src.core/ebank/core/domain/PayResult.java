/*
 * @Id: PayResult.java 10:59:20 2006-2-14
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.domain;

import java.math.BigDecimal;

import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwTrxs;
/**
 * @author xiexh
 * Description: 从银行返回的支付结果传输对象
 * 
 */
public class PayResult{
	
	private PayResult(){};	
		
	public PayResult(String transnum,String bankcode,BigDecimal amount,int sts){
		this.setTransnum(transnum);
		this.bankcode=bankcode;
		this.trxsts=sts;
		if(amount!=null) this.setBankamount(amount);		
	}	
	
	public PayResult(String transnum){
		this.setTransnum(transnum);		
	}
	
	private String aquiremerchant;        //网银第三方编号
	private String transnum;        //网银订单号
	private int trxsts;      //交易状态([-1:失败 0 未确认 1:成功]
	private String bankcode;      //支付银行编码
	private String bankdate;     //交易日期
	private BigDecimal bankamount;    //银行返回结果金额(元)
	
	private boolean enableFnotice=true;   //失败是否通知商户(默认通知);
		
	private String bankresult="";        //交易关键项结果	
	
	private String currency;      //货币类型
	
	private GwOrders order;
	private GwTrxs   trx;
	
	//退款可能需要
	private String authcode="";      //授权号码(银行返回)
	private String banktransseq="";  //银行流水号
	private String refnum="";        //支付参考号
	private String payinfo="";       //付款信息	
	
	private String payer_ip;
	
	public int getTrxsts() {
		return trxsts;
	}	

	
	public GwOrders getOrder() {
		return order;
	}

	public void setOrder(GwOrders order) {
		this.order = order;
	}

	public GwTrxs getTrx() {
		return trx;
	}

	public void setTrx(GwTrxs trx) {
		this.trx = trx;
	}

	public String getPayer_ip() {
		return payer_ip;
	}

	public void setPayer_ip(String payerIp) {
		payer_ip = payerIp;
	}

	public void setTrxsts(int trxsts) {
		this.trxsts = trxsts;
	}

	public String getAquiremerchant() {
		return aquiremerchant;
	}

	public void setAquiremerchant(String aquiremerchant) {
		this.aquiremerchant = aquiremerchant;
	}

	public String getRefnum() {
		return refnum;
	}

	public void setRefnum(String refnum) {
		this.refnum = refnum;
	}

	public String getPayinfo() {
		return payinfo;
	}

	public void setPayinfo(String payinfo) {
		this.payinfo = payinfo;
	}

	public String getBankdate() {
		return bankdate;
	}

	public void setBankdate(String bankdate) {
		this.bankdate = bankdate;
	}

	/**
	 * @return Returns the enableFnotice.
	 */
	public boolean isEnableFnotice() {
		return enableFnotice;
	}
	/**
	 * @param enableFnotice The enableFnotice to set.
	 */
	public void setEnableFnotice(boolean enableFnotice) {
		this.enableFnotice = enableFnotice;
	}
			
	/**
	 * @return Returns the bankcode.
	 */
	public String getBankcode() {
		return bankcode;
	}
	/**
	 * @param bankcode The bankcode to set.
	 */
	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}
		
	/**
	 * @return Returns the authcode.
	 */
	public String getAuthcode() {
		return authcode;
	}
	/**
	 * @param authcode The authcode to set.
	 */
	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}
	
	/**
	 * @return Returns the bankamount.
	 */
	public BigDecimal getBankamount() {
		return bankamount;
	}
	/**
	 * @param bankamount The bankamount to set.
	 */
	public void setBankamount(BigDecimal bankamount) {
		this.bankamount = bankamount;
	}
	/**
	 * @return Returns the bankresult.
	 */
	public String getBankresult() {
		return bankresult;
	}
	/**
	 * @param bankresult The bankresult to set.
	 */
	public void setBankresult(String bankresult) {
		this.bankresult = bankresult;
	}
	
	/**
	 * @return Returns the currency.
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency The currency to set.
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
	public String getBanktransseq() {
		return banktransseq;
	}
	/**
	 * @param banktransseq The banktransseq to set.
	 */
	public void setBanktransseq(String banktransseq) {
		this.banktransseq = banktransseq;
	}		
	
	/**
	 * @return Returns the transnum.
	 */
	public String getTransnum() {
		return transnum;
	}
	/**
	 * @param transnum The transnum to set.
	 */
	public void setTransnum(String transnum) {
		this.transnum = transnum;
	}
	
	
	
	
	

}
