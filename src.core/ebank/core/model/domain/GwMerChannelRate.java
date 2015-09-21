package ebank.core.model.domain;

public class GwMerChannelRate 
{
	private Long merchantId;
	private String feeType;
	private Double feeRate;
	private String isRefundFee;
	private String acquireIndexc;
	public String getAcquireIndexc() {
		return acquireIndexc;
	}
	public void setAcquireIndexc(String acquireIndexc) {
		this.acquireIndexc = acquireIndexc;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public Double getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(Double feeRate) {
		this.feeRate = feeRate;
	}
	public String getIsRefundFee() {
		return isRefundFee;
	}
	public void setIsRefundFee(String isRefundFee) {
		this.isRefundFee = isRefundFee;
	}
	
}
