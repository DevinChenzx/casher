package ebank.core.model.domain;

/**
 * payer account balance
 * @author Kitian-XIE
 *
 */
public class GwViewAccount {
	private long   user_id;        //�û�ID
	private String user_type;      //�û�����
	private String login_recepit;  //��¼ƾ֤��
	private String login_pwd;      //��¼����
	private String pay_pwd;        //֧������
	private long    acct_amount;    //�˺Ž��
	private long    acct_balance;   //�˻��������
	private String acct_sts;       //�˺�״̬
	private long   acct_id;        //�˺�ID
	private String acct_name;      //�˺�����
	private String oper_sts;       //����Ա״̬
	
	
	public String getOper_sts() {
		return oper_sts;
	}
	public void setOper_sts(String oper_sts) {
		this.oper_sts = oper_sts;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long userId) {
		user_id = userId;
	}
	public String getLogin_recepit() {
		return login_recepit;
	}
	public void setLogin_recepit(String loginRecepit) {
		login_recepit = loginRecepit;
	}
	public String getLogin_pwd() {
		return login_pwd;
	}
	public void setLogin_pwd(String loginPwd) {
		login_pwd = loginPwd;
	}
	public String getPay_pwd() {
		return pay_pwd;
	}
	public void setPay_pwd(String payPwd) {
		pay_pwd = payPwd;
	}
	
	public long getAcct_amount() {
		return acct_amount;
	}
	public long getAcct_balance() {
		return acct_balance;
	}
	public void setAcct_balance(long acct_balance) {
		this.acct_balance = acct_balance;
	}
	public void setAcct_amount(long acct_amount) {
		this.acct_amount = acct_amount;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String userType) {
		user_type = userType;
	}
	public String getAcct_sts() {
		return acct_sts;
	}
	public void setAcct_sts(String acctSts) {
		acct_sts = acctSts;
	}
	public long getAcct_id() {
		return acct_id;
	}
	public void setAcct_id(long acctId) {
		acct_id = acctId;
	}
	public String getAcct_name() {
		return acct_name;
	}
	public void setAcct_name(String acctName) {
		acct_name = acctName;
	}
	
	
	
	

}
