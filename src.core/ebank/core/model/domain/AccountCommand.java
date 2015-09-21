package ebank.core.model.domain;

import java.util.Date;

public class AccountCommand {
	private String command_no; 
	private String resp_code; 
	private String sync_flag;
	private Date sync_time; 
	private String trans_code; 
	private String trans_id;
	private long amount; 
	private String currency; 
	private String from_account_no; 
	private String note; 
	private String out_trade_no; 
	private int sub_seqno; 
	private String to_account_no; 
	private String trade_id; 
	private String trade_no; 
	private String transfer_type;
	private String redo_flag;
	
		
	public String getRedo_flag() {
		return redo_flag;
	}
	public void setRedo_flag(String redo_flag) {
		this.redo_flag = redo_flag;
	}
	public String getCommand_no() {
		return command_no;
	}
	public void setCommand_no(String commandNo) {
		command_no = commandNo;
	}
	public String getResp_code() {
		return resp_code;
	}
	public void setResp_code(String respCode) {
		resp_code = respCode;
	}
	public String getSync_flag() {
		return sync_flag;
	}
	public void setSync_flag(String syncFlag) {
		sync_flag = syncFlag;
	}
	
	public Date getSync_time() {
		return sync_time;
	}
	public void setSync_time(Date syncTime) {
		sync_time = syncTime;
	}
	public String getTrans_code() {
		return trans_code;
	}
	public void setTrans_code(String transCode) {
		trans_code = transCode;
	}
	public String getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(String transId) {
		trans_id = transId;
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
	public String getFrom_account_no() {
		return from_account_no;
	}
	public void setFrom_account_no(String fromAccountNo) {
		from_account_no = fromAccountNo;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String outTradeNo) {
		out_trade_no = outTradeNo;
	}
	public int getSub_seqno() {
		return sub_seqno;
	}
	public void setSub_seqno(int subSeqno) {
		sub_seqno = subSeqno;
	}
	public String getTo_account_no() {
		return to_account_no;
	}
	public void setTo_account_no(String toAccountNo) {
		to_account_no = toAccountNo;
	}
	public String getTrade_id() {
		return trade_id;
	}
	public void setTrade_id(String tradeId) {
		trade_id = tradeId;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String tradeNo) {
		trade_no = tradeNo;
	}
	public String getTransfer_type() {
		return transfer_type;
	}
	public void setTransfer_type(String transferType) {
		transfer_type = transferType;
	}
	
	

}
