/*
 * @Id: Constants.java 18:07:43 2006-2-13
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.common;

/**
 * @author xiexh
 * Description: 程序应用常量定义
 * 
 */
public class Constants {
	public static final int    MERCHANTAUTOBILL_MAXTIMES=4;        //商户对帐最大次数
	public static final int    MERCHANTAUTOBILL_ALERTVALUE=3;       //提醒值
	
	public static final String PAYTYPE_DEBIT="0";  //借记卡
	public static final String PAYTYPE_CREDIT="1"; //信用卡
	public static final String PAYTYPE_MOTO  ="2"; //MOTOPAY
	//public static final String PAYTYPE_MIGSs  ="3"; //MiGS
	public static final String PAYTYPE_B2B    ="9"; //B2B
	public static final String PAYTYPE_CALLER="6"; //电话支付
	public static final String PAYTYPE_MOBILE="8"; //电话支付
	
	
	public static final String CARDTYPE_VISA="00"; //VISA卡
	public static final String CARDTYPE_MASTERCARD="01"; //MASTERCARD卡
	public static final String CARDTYPE_JCB="02"; //JCB卡
	
	public static final String BIN_VISA="^4+\\d+";
	public static final String BIN_MASTERCARD="^51+\\d+|^52+\\d+|^53+\\d+|^54+\\d+|^55+\\d+";
	public static final String BIN_JCB="^3528+\\d+|^3529+\\d+|^353+\\d+|^354+\\d+|^355+\\d+|^356+\\d+|^357+\\d+|^358+\\d+|^2131+\\d+|^1800+\\d+";
		
	public static final String CURRENCY_RMB="RMB";
		
	public static final String SYN_DESC_OVERNOTBILL="未对帐超时,未支付";
	public static final String SYN_DESC_OVERBILLFAILURE="招行结算失败";
	
	public static final String STATE_OK     ="Y";          //发送到结算后台
	public static final String STATE_READY  ="N";          //发送到结算后台就绪
	public static final String STATE_FAILURE="F";          //发送到结算后台失败
	
	public static final int STATE_INT_OK   =1;                  //状态正常
	public static final int STATE_INT_PAUSE=2;                  //状态暂停
	public static final int STATE_INT_DEAD =4;                  //状态禁用
	public static final int STATE_INT_UNCERTAIN=0;              //状态未定

	public static final String STATE_STR_WAIT="N";          //等待支付
	public static final String STATE_STR_HANDING="A";       //处理中
	public static final String STATE_STR_OK="Y";            //支付成功完成
	public static final String STATE_STR_FAIL="F";          //支付失败
	public static final String STATE_STR_DEAD="S";          //未支付

	
	public static final String SEQ_ORDER="SEQ_ORDER";               //订单序列名称	
	
	public static final String CRYPT_RSA="rsa";                       //安全RAS加密方式简写
	public static final String CRYPT_MD5="md5";                       //安全MD5加密方式简写
	
	public static final int    MESSAGE_EMAIL=1;              //消息类型email
	public static final int    MESSAGE_SMS  =2;              //消息类型短信
	public static final int    MESSAGE_ALL  =0;              //待定类型
	
		
	public static final String TRANS_TYPE_CONSUME     ="100";  //消费
	public static final String TRANS_TYPE_CONSUME_NAME="消费";
	
	public static final String TRANS_TYPE_CANCLE      ="200";  //撤销
	public static final String TRANS_TYPE_CANCLE_NAME ="撤销";
	
	public static final String TRANS_TYPE_REFUND      ="300";  //退款
	public static final String TRANS_TYPE_REFUND_NAME ="退款";  //退款
	
	public static final String TRANS_TYPE_SETTLE        ="400";  //充值
	public static final String TRANS_TYPE_SETTLE_NAME   ="结算";
	
	public static final String SERVICE_CARD       ="10";       //卡
	public static final String SERVICE_NATIVECARD ="11";       //内卡
	public static final String SERVICE_CREDIT     ="12";       //外卡
	
	public static final String SERVICE_TYPE_ONLINE    ="510";  //线上
	public static final String SERVICE_TYPE_ONLINE_IN ="511";  //线上内卡
	public static final String SERVICE_TYPE_ONLINE_OUT="512";  //线上外卡
	
	public static final String SERVICE_TYPE_OFFLINE       ="520"; //线下
	public static final String SERVICE_TYPE_OFFLINE_DIVIED="521"; //分期付款
	
	public static final int    BLACK_TYPE_PAN       =1;      //信用卡类型
	public static final int    BLACK_TYPE_MERCHANTID=2;      //商户类型	
	
	public static final int    MESSAGE_TYPE_MERCHANT   =0;   //商户消息
	public static final int    MESSAGE_TYPE_CONSUME    =1; //持卡消费者
	public static final int    MESSAGE_TYPE_CUSTOMECARE=2;  //客服
	
	public static final String MESSAGE_TEMPLATE_SYNMERCHANT="merchant_syn.vm"; //自动对帐通知
	public static final String ORDER_STS_REFUSE = "6"; //交易限额拒绝订单状态
	
	
//	TODO MODIFY
	public static final String REG_MONEY ="^(\\d+)(\\.\\d{1,2})?$";  //货币正则表达式
	public static final String REG_MONEY2="(\\.\\d{1,2})?$";         //货币表达式
	
	public static final String REG_EMAIL ="\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";//"^[^@]+@([_a-z0-9A-Z]+\\.)+\\w{2,3}$";   //EMAIL
	
	
	public static final String REG_URL   ="^http(s)?://([\\w-]+\\.)*+[\\w-]+(:+\\d{1,})?+(/[\\w- ./?%&=]*)?";  //http
	
	public static final String REG_PID   ="[1-9]\\d{14}(\\d{2}[0-9a-zA-Z]{1})?";  //身份证
	
	public static final String REG_ORDERID   ="[a-zA-Z0-9_-]+$";  //订单号
	
	public static final String REG_PID4  ="\\d{3}[0-9a-zA-Z]{1}";  //身份证后四位
	
	public static final String REG_MOBILEPHONE="[1][3|5|8][0-9]\\d{8}";
	
	public static final String REG_POST ="\\d{6}";
	public static final String REG_ORDERTIME="\\d{14}"; //yyyyMMddhhmmss
	public static final String REG_ORDERDATE="\\d{8}";  //yyyyMMdd
	
	
	public static final String APP_VERSION="v4";
	
	public static final String API_WONDERPAY="ONLINE";  //吉卡
	public static final String API_APLIPAY="alipay3"; 
	public static final String API_APLIPAY_OLD="alipay";
	public static final String API_YEEPAY="yeepay";
	public static final String API_CHINABANK="chinabank";
	public static final String API_FASTPAY="fastpay";//快捷支付
	public static final String API_PRECARD="precard";
	public static final String API_99BILL="99bill"; //快钱
	public static final String API_CHINAPNR="chinapnr"; //汇付天下
	public static final String API_IPS="ips"; //环讯
	public static final String API_REAPAL="model1"; 
	
	public static final String ACQ_CODE_BHE="BHE";
	public static final String ACQ_CODE_APLIPAY="ALP"; 
	public static final String ACQ_CODE_YEEPAY="YEP";
	public static final String ACQ_CODE_CHINABANK="CBP";	
	public static final String ACQ_CODE_CHINAPNR="PNR";	
	public static final String ACQ_CODE_99BILL="BIL"; //快钱
	public static final String ACQ_CODE_IPS="IPS"; //环讯.
	public static final String ACQ_CODE_ONLINE="ONLINE"; 
	public static final String ACQ_CODE_ONLINE_FIVE="ONLINE_FIVE"; 
	public static final String ACQ_CODE_YINTONG="YINTONG"; 
	public static final String ACQ_CODE_REA="REA";
	public static final String ACQ_CODE_HEEPAY="HEEPAY";//汇付
	public static final String ACQ_CODE_TFT="tft";//腾付通通道
	public static final String ACQ_CODE_="UNIONGATEWAY";//银联B2C
	  public static final String ACQ_CODE_LEFU_MOBILE = "LEFU_MOBILE";
	  public static final String ACQ_CODE_FENGFU = "FENGPAY";
	  public static final String ACQ_CODE_UNIONMOBILEPAY = "UNIONMOBILE";
	
	public static final String FORMAT_FILED="_format";
	public static final String SESSION_KEY_MORDER="_session_key_merchantorder";
	
	public static enum FROM_TRXSTS {WAIT_BUYER_PAY,WAIT_SELLER_SEND_GOODS,WAIT_BUYER_CONFIRM_GOODS,TRADE_FINISHED,TRADE_CLOSED,TRADE_FAILURE};
	
	public static enum TO_TRXSTS {WAIT_TRADE,TRADE_FINISHED,TRADE_FAILURE,TRADE_CLOSED};
	
	public static enum PAYMENT_TYPE{BI_TRX,TR_TRX}; //立即到账(双方) 担保(三方)
	
	public static enum NOTIFY_STS{WAIT_TRIGGER,WAIT_NOTIFY,RESPONSE_FAILURE,RESPONSE_SUCCESS,NOTIFY_CLOSED};
	
	public static enum NOTIFY_TYPE{trade_status_sync,trade_post_syn,trade_aquire_syn,trade_result_success};
	
	public static enum POST_STS{WAIT_CONF,WAIT_POST,POSTING,POST_SUCCESS,POST_FAILURE};
	
	public static enum PROC_IDS{POST,SETTLE};
	public static enum PROC_NAME{TRX_POSTING,TRX_BANKSETTLE}
	public static enum PROC_STS{N,A,Y,F}
	
	public static enum PAYMENT_STS{NOT_PAY,PAID};
	public static enum PAYMENT_PAYTYPE{GWORDER,GWTRX};
	public static enum PAYMENT_CHANNEL{EBANK,ACCOUNT};
	
	public static enum ORDER_TYPE{NET_SHOPPING,CHARGE};
	
	public static final String NOTIFY_RESPONSE_FROM_MERCHANT="SUCCESS";
	public static final String NOTIFY_RESPONSE_FROM_ICMS="0000";	
	public static String input_charset = "gbk";
	

}
