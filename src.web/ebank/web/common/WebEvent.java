/*
 * @Id: WebEvent.java 19:13:52 2006-2-20
 * 
 * @author xiexh@chinabank.com.cn
 * @version 1.0
 * PAYGW_WEB_V6 PROJECT
 */
package ebank.web.common;

import ebank.core.common.EventCode;

/**
 * @author xiexh@chinabank.com.cn
 * Description: web 扩展事件代码
 * 
 * 
 */
public class WebEvent extends EventCode {
	
	public static final String FILE_NOTFOUND    ="100101" ;      //定义的文件未找到
	
	public static final String CURRENCY_NOTMATCH="501110" ;  //币种不正确
	
	public static final String SERVICE_NOTSUPPORT="501603";  //未开通,拒绝此项服务
	public static final String SERVICE_NOTPROVIDED="501604"; //未提供此项服务	
	
	public static final String MERCHANT_IDNOTFOUND="500708"; //商户号不存在
	public static final String MERCHANT_STATEILL="500704" ;  //该商户状态未激活
	public static final String MERCHANT_DIRECTSERVICE="500710"; //该商户未启用支付直通服务,错误的服务编码
	
	public static final String ARG_AMOUNT_EMPTY= "501104";   //金额空
	public static final String ARG_AMOUNT_ILLFORMAT="501112";      //金额格式不正确
	public static final String SERVICE_ARGS="501109";        //缺少或参数错误
	public static final String ARG_AMOUNT_OVER="501113";     //金额溢出
	public static final String ARG_AMOUNT_ZERO="501114";           //金额不能为0
	public static final String ARG_BACKURL_OVERFLOW="501115";   //返回地址过长
	public static final String ARG_BACKURL_ILLURL  ="501116";   //错误的url格式
	public static final String ARG_ORDERNAME_OVERFLOW ="501117"; //订单名称过长
	public static final String ARG_FIELD_OVER ="501118";         //订单域信息超出定义长度
	public static final String ARG_BANKEXPORT_OVERFLOW="501119";  //自定义服务项过长
	public static final String ARG_EMAIL_EMPTY="501120";          //email 地址空
	public static final String ARG_EMAIL_ERROR="501121";          //无效email地址
	public static final String ARG_MOBILEPHONE_ERROR="501122";    //无效的手机号码
	public static final String ARG_TYPE_ILL   ="501122";          //无效的消息类型
	public static final String ARG_CONTENT_OVER="501123";         //内容过长
	public static final String ARG_POST_ERROR  ="501129";         //邮编错误
	public static final String ARG_PRODUCT_LIST_OVER="501130";    //商品列表过多
	public static final String ARG_ORDER            ="501131";    //无效订单,订单或因版本不能识别
	public static final String ARG_AMOUNT_NOTEQUALS="501134";           //手续费金额不等
	public static final String MERCHANT_PAY_SERVICE="601500";     //商户支付服务停止
	
	
	public static final String REQUEST_PARAM_VALIDATE="501108" ;//参数校验错误
	
	public static final String ORDER_NOTFOUNDBYSEQ="501107";    //不存在该订单
	
	public static final String ORDER_PRICENOTEQUAL="501106";    //银行返回订单金额与商户金额不一致
	public static final String ORDER_SAVE="501105";             //支付结果保存出现错误
	public static final String ORDER_STATEILL="501101";         //该订单已经处理,请勿重复提交
	public static final String ORDER_UNPAY   ="501124";               //该订单未支付
	public static final String ORDER_PAYFAILURE="501125";             //该订单支付失败
	public static final String ORDER_ILLSTATE  ="501126";             //无效订单状态
	
	public static final String SERVICE_URLPARAM="501111";         //缺少必要返回参数[idx],服务不能识别
	
	public static final String SESSION_INVALIDATE="700000";      //SESSION失效
	public static final String ORDER_SUBMIT_OVERFLOW="700001";   //该订单多次提交,已禁止继续支付
	public static final String PAYRESULT_HANDLE_FAILURE="700002"; //处理支付结果失败

}
