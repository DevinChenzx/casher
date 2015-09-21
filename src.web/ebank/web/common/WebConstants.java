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
 * Description: web 应用常量
 * 
 */
public class WebConstants {
	
	public static final String ERROR_PAGE="/common/error";   //错误指向页
	public static final String ERROR_MODEL="we";             //错误页错误对象
	
	public static final String Direction="direction";         //方向
	public static final String Action="action";               //ACTION
	public static final String PaRes="PaRes";                 //支付转发响应	
	
	public static final String DEFAULT_BANK_SELECTED="CMB";   //默认选择招行
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
	
	public static final String Direction_Result="0";  //结果导向	
	public static final String Direction_BANKPAY="1"; //直连银行 
		
	public static final int STATE_OK   =1;                  //状态正常
	public static final int STATE_PAUSE=2;                  //状态暂停
	public static final int STATE_DEAD =4;                  //状态禁用
	public static final int STATE_UNCERTAIN=0;              //状态未定
	
	public static final String STATE_STR_WAIT="N";          //等待支付
	public static final String STATE_STR_HANDING="A";       //处理中
	public static final String STATE_STR_OK="Y";            //支付成功完成
	public static final String STATE_STR_FAIL="F";          //支付失败
	public static final String STATE_STR_DEAD="S";          //未支付
	
	public static final int SERVICE_EMAIL=5101;            //商户email服务
	public static final int SERVICE_SMS  =5102;            //商户短信服务
	
	public static final int MESSAGE_EMAIL=1;              //EMAIL 消息
	public static final int MESSAGE_SMS  =2;                //消息短信
	
	public static final int AUTOBILL_IMM  =1;             //支付完成立即自动对单
	public static final int AUTOBILL_NOIMM=0;             //不对单
	
	
	
	public static final String TRANS_TYPE_CONSUME     ="100";  //消费
	public static final String TRANS_TYPE_CONSUME_NAME="消费";
	public static final String TRANS_TYPE_CANCLE      ="200";  //撤销
	public static final String TRANS_TYPE_CANCLE_NAME ="撤销";
	public static final String TRANS_TYPE_REFUND        ="300";  //退款
	public static final String TRANS_TYPE_REFUND_NAME   ="退款";
	
	public static final int SERVICE_CARD       =1;       //卡
	public static final int SERVICE_NATIVECARD =10;       //内卡
	public static final int SERVICE_CREDIT     =11;       //外卡
	public static final int SERVICE_BOTH       =12;       //支持内卡和外卡
	
	public static final String SERVICE_TYPE_ONLINE    ="510";  //线上
	public static final String SERVICE_TYPE_ONLINE_IN ="511";  //线上内卡
	public static final String SERVICE_TYPE_ONLINE_OUT="512";  //线上外卡
	
	public static final String SERVICE_TYPE_OFFLINE       ="520";     //线下
	public static final String SERVICE_TYPE_OFFLINE_DIVIED="521"; //分期付款
	
	
	public static final String TEMPLATE_EMAIL_CONSUMER="consumer_email.vm";
	public static final String TEMPLATE_SMS_CONSUMER  ="consumer_sms.vm";
	public static final String TEMPLATE_MAIL_PAYRESULT_MERCHANT="merchant_mail_payresult.vm";
	public static final String TEMPLATE_SMS_PAYRESULT_MERCHANT ="merchant_sms_payresult.vm";
	
	public static final String TRADEPRECARD_PAYSTATUS_SUCCESS="Y";//交易成功
	public static final String TRADEPRECARD_PAYSTATUS_NOTENOUGH="51";//余额不足
	public static final String KEY="291096g47g4c15830816f742898085c4b6a0fa8332eg6fa7a0382f3794199e4b";
	
	
	

}
