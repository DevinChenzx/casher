/*
 * @Id: Constants.java 18:58:12 2006-2-20
 * 
 * @author xiexh@chinabank.com.cn
 * @version 1.0
 * PAYGW_WEB_V6 PROJECT
 */
package ebank.web.common;
/**
 * @author xiexh
 * Description: web Ӧ�ó���
 * 
 */
public class WebConstants {
	
	public static final String ERROR_PAGE="/common/error";   //����ָ��ҳ
	public static final String ERROR_MODEL="we";             //����ҳ�������
	
	public static final String Direction="direction";         //����
	public static final String Action="action";               //ACTION
	public static final String PaRes="PaRes";                 //֧��ת����Ӧ	
	
	public static final String DEFAULT_BANK_SELECTED="CMB";   //Ĭ��ѡ������
	public static final String MAP_KEY_ORDER="_sorder";
	public static final String MAP_KEY_ORDERID="_orderId";
	public static final String MAP_KEY_PERSISTENCE="_persistence";
	public static final String MAP_KEY_CHANNELTOKEN="_channelToken";
	public static final String MAP_KEY_CHANNELCODE="_channelcode";
	public static final String MAP_KEY_B2BCHANNELCODE="_b2bchannelcode";
	public static final String MAP_KEY_B2CCHANNELCODE="_b2cchannelcode";
	public static final String MAP_KEY_CCHANNELCODE="_cchannelcode";
	public static final String MAP_KEY_CHANNEL    ="_channel";
	public static final String MAP_KEY_ID="_id";  //crypt trxs id
	public static final String MAP_KEY_PAYTYPE="_paytype";  //crypt trxs id
	public static final String MAP_KEY_PAYMENTID="_paymentid";
	
	public static final String SESSION_USER_LOGIN_KEY="_cashieruser";
	public static final String SESSION_USER_PAYWD="_cashieruserpwd";
	public static final String ISMS_MD5_KEY="itcomesfromIsms2011&";
	
	public static final String Direction_Result="0";  //�������	
	public static final String Direction_BANKPAY="1"; //ֱ������ 
		
	public static final int STATE_OK   =1;                  //״̬����
	public static final int STATE_PAUSE=2;                  //״̬��ͣ
	public static final int STATE_DEAD =4;                  //״̬����
	public static final int STATE_UNCERTAIN=0;              //״̬δ��
	
	public static final String STATE_STR_WAIT="N";          //�ȴ�֧��
	public static final String STATE_STR_HANDING="A";       //������
	public static final String STATE_STR_OK="Y";            //֧���ɹ����
	public static final String STATE_STR_FAIL="F";          //֧��ʧ��
	public static final String STATE_STR_DEAD="S";          //δ֧��
	
	public static final int SERVICE_EMAIL=5101;            //�̻�email����
	public static final int SERVICE_SMS  =5102;            //�̻����ŷ���
	
	public static final int MESSAGE_EMAIL=1;              //EMAIL ��Ϣ
	public static final int MESSAGE_SMS  =2;                //��Ϣ����
	
	public static final int AUTOBILL_IMM  =1;             //֧����������Զ��Ե�
	public static final int AUTOBILL_NOIMM=0;             //���Ե�
	
	
	
	public static final String TRANS_TYPE_CONSUME     ="100";  //����
	public static final String TRANS_TYPE_CONSUME_NAME="����";
	public static final String TRANS_TYPE_CANCLE      ="200";  //����
	public static final String TRANS_TYPE_CANCLE_NAME ="����";
	public static final String TRANS_TYPE_REFUND        ="300";  //�˿�
	public static final String TRANS_TYPE_REFUND_NAME   ="�˿�";
	
	public static final int SERVICE_CARD       =1;       //��
	public static final int SERVICE_NATIVECARD =10;       //�ڿ�
	public static final int SERVICE_CREDIT     =11;       //�⿨
	public static final int SERVICE_BOTH       =12;       //֧���ڿ����⿨
	
	public static final String SERVICE_TYPE_ONLINE    ="510";  //����
	public static final String SERVICE_TYPE_ONLINE_IN ="511";  //�����ڿ�
	public static final String SERVICE_TYPE_ONLINE_OUT="512";  //�����⿨
	
	public static final String SERVICE_TYPE_OFFLINE       ="520";     //����
	public static final String SERVICE_TYPE_OFFLINE_DIVIED="521"; //���ڸ���
	
	
	public static final String TEMPLATE_EMAIL_CONSUMER="consumer_email.vm";
	public static final String TEMPLATE_SMS_CONSUMER  ="consumer_sms.vm";
	public static final String TEMPLATE_MAIL_PAYRESULT_MERCHANT="merchant_mail_payresult.vm";
	public static final String TEMPLATE_SMS_PAYRESULT_MERCHANT ="merchant_sms_payresult.vm";
	
	public static final String TRADEPRECARD_PAYSTATUS_SUCCESS="Y";//���׳ɹ�
	public static final String TRADEPRECARD_PAYSTATUS_NOTENOUGH="51";//����
	public static final String KEY="291096g47g4c15830816f742898085c4b6a0fa8332eg6fa7a0382f3794199e4b";
	
	
	

}
