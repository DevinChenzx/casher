package ebank.core.model.domain;

import java.util.Date;

public class GwFaultTrx {
	    long id;
	    int version;
	    
	    String trxid;            //ϵͳ���׺�
	    String acquire_trxnum;    //���׺�
	    String trxdate;           //����ʱ��
	    int trxamount;          //���׽��
	    String acquire_code;      //�յ��б���
	    String acquire_merchant;  //�յ��̻�
	    String acquire_seq;        //�յ���ˮ
	    String acquire_refnum;      //�ο���
	    String acquire_authcode;   //��Ȩ��
	    String acquire_cardnum;    //�յ�����
	    String acquire_date;       //�յ�����
	    String payer_ip;           //������IP

	    int ini_sts;         //����ʱ�ĳ�ʼ״̬
	    String auth_sts ;          //���״̬
	    int change_sts;         //�����״̬

	    Date create_date;        //����ʱ��
	    Date update_date ;       //����ʱ��
	    Date auth_date  ;        //���ʱ��

	    String final_sts;          //����״̬
	    String final_result ;      //������

	    String change_applier;    //����������
	    String auth_oper  ;       //�����
	    String datasrc  ;        //������Դ
	    String fault_advice ;    //�������
	    
	    
		public String getPayer_ip() {
			return payer_ip;
		}
		public void setPayer_ip(String payer_ip) {
			this.payer_ip = payer_ip;
		}
		public int getVersion() {
			return version;
		}
		public void setVersion(int version) {
			this.version = version;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getTrxid() {
			return trxid;
		}
		public void setTrxid(String trxid) {
			this.trxid = trxid;
		}
		public String getAcquire_trxnum() {
			return acquire_trxnum;
		}
		public void setAcquire_trxnum(String acquire_trxnum) {
			this.acquire_trxnum = acquire_trxnum;
		}
		public String getTrxdate() {
			return trxdate;
		}
		public void setTrxdate(String trxdate) {
			this.trxdate = trxdate;
		}
		public int getTrxamount() {
			return trxamount;
		}
		public void setTrxamount(int trxamount) {
			this.trxamount = trxamount;
		}
		public String getAcquire_code() {
			return acquire_code;
		}
		public void setAcquire_code(String acquire_code) {
			this.acquire_code = acquire_code;
		}
		public String getAcquire_merchant() {
			return acquire_merchant;
		}
		public void setAcquire_merchant(String acquire_merchant) {
			this.acquire_merchant = acquire_merchant;
		}
		public String getAcquire_seq() {
			return acquire_seq;
		}
		public void setAcquire_seq(String acquire_seq) {
			this.acquire_seq = acquire_seq;
		}
		public String getAcquire_refnum() {
			return acquire_refnum;
		}
		public void setAcquire_refnum(String acquire_refnum) {
			this.acquire_refnum = acquire_refnum;
		}
		public String getAcquire_authcode() {
			return acquire_authcode;
		}
		public void setAcquire_authcode(String acquire_authcode) {
			this.acquire_authcode = acquire_authcode;
		}
		public String getAcquire_cardnum() {
			return acquire_cardnum;
		}
		public void setAcquire_cardnum(String acquire_cardnum) {
			this.acquire_cardnum = acquire_cardnum;
		}
		public String getAcquire_date() {
			return acquire_date;
		}
		public void setAcquire_date(String acquire_date) {
			this.acquire_date = acquire_date;
		}
		public int getIni_sts() {
			return ini_sts;
		}
		public void setIni_sts(int ini_sts) {
			this.ini_sts = ini_sts;
		}
		public String getAuth_sts() {
			return auth_sts;
		}
		public void setAuth_sts(String auth_sts) {
			this.auth_sts = auth_sts;
		}
		public int getChange_sts() {
			return change_sts;
		}
		public void setChange_sts(int change_sts) {
			this.change_sts = change_sts;
		}
		public Date getCreate_date() {
			return create_date;
		}
		public void setCreate_date(Date create_date) {
			this.create_date = create_date;
		}
		public Date getUpdate_date() {
			return update_date;
		}
		public void setUpdate_date(Date update_date) {
			this.update_date = update_date;
		}
		public Date getAuth_date() {
			return auth_date;
		}
		public void setAuth_date(Date auth_date) {
			this.auth_date = auth_date;
		}
		public String getFinal_sts() {
			return final_sts;
		}
		public void setFinal_sts(String final_sts) {
			this.final_sts = final_sts;
		}
		public String getFinal_result() {
			return final_result;
		}
		public void setFinal_result(String final_result) {
			this.final_result = final_result;
		}
		public String getChange_applier() {
			return change_applier;
		}
		public void setChange_applier(String change_applier) {
			this.change_applier = change_applier;
		}
		public String getAuth_oper() {
			return auth_oper;
		}
		public void setAuth_oper(String auth_oper) {
			this.auth_oper = auth_oper;
		}
		public String getDatasrc() {
			return datasrc;
		}
		public void setDatasrc(String datasrc) {
			this.datasrc = datasrc;
		}
		public String getFault_advice() {
			return fault_advice;
		}
		public void setFault_advice(String fault_advice) {
			this.fault_advice = fault_advice;
		}
	    

}
