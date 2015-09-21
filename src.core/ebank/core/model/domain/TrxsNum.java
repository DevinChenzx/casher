package ebank.core.model.domain;

public class TrxsNum
{
  String acquirer_code;
  String trxnum;

  public String getAcquirer_code()
  {
    return this.acquirer_code;
  }
  public void setAcquirer_code(String acquirerCode) {
    this.acquirer_code = acquirerCode;
  }
  public String getTrxnum() {
    return this.trxnum;
  }
  public void setTrxnum(String trxnum) {
    this.trxnum = trxnum;
  }
}