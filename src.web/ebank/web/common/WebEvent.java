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
 * Description: web ��չ�¼�����
 * 
 * 
 */
public class WebEvent extends EventCode {
	
	public static final String FILE_NOTFOUND    ="100101" ;      //������ļ�δ�ҵ�
	
	public static final String CURRENCY_NOTMATCH="501110" ;  //���ֲ���ȷ
	
	public static final String SERVICE_NOTSUPPORT="501603";  //δ��ͨ,�ܾ��������
	public static final String SERVICE_NOTPROVIDED="501604"; //δ�ṩ�������	
	
	public static final String MERCHANT_IDNOTFOUND="500708"; //�̻��Ų�����
	public static final String MERCHANT_STATEILL="500704" ;  //���̻�״̬δ����
	public static final String MERCHANT_DIRECTSERVICE="500710"; //���̻�δ����֧��ֱͨ����,����ķ������
	
	public static final String ARG_AMOUNT_EMPTY= "501104";   //����
	public static final String ARG_AMOUNT_ILLFORMAT="501112";      //����ʽ����ȷ
	public static final String SERVICE_ARGS="501109";        //ȱ�ٻ��������
	public static final String ARG_AMOUNT_OVER="501113";     //������
	public static final String ARG_AMOUNT_ZERO="501114";           //����Ϊ0
	public static final String ARG_BACKURL_OVERFLOW="501115";   //���ص�ַ����
	public static final String ARG_BACKURL_ILLURL  ="501116";   //�����url��ʽ
	public static final String ARG_ORDERNAME_OVERFLOW ="501117"; //�������ƹ���
	public static final String ARG_FIELD_OVER ="501118";         //��������Ϣ�������峤��
	public static final String ARG_BANKEXPORT_OVERFLOW="501119";  //�Զ�����������
	public static final String ARG_EMAIL_EMPTY="501120";          //email ��ַ��
	public static final String ARG_EMAIL_ERROR="501121";          //��Чemail��ַ
	public static final String ARG_MOBILEPHONE_ERROR="501122";    //��Ч���ֻ�����
	public static final String ARG_TYPE_ILL   ="501122";          //��Ч����Ϣ����
	public static final String ARG_CONTENT_OVER="501123";         //���ݹ���
	public static final String ARG_POST_ERROR  ="501129";         //�ʱ����
	public static final String ARG_PRODUCT_LIST_OVER="501130";    //��Ʒ�б����
	public static final String ARG_ORDER            ="501131";    //��Ч����,��������汾����ʶ��
	public static final String ARG_AMOUNT_NOTEQUALS="501134";           //�����ѽ���
	public static final String MERCHANT_PAY_SERVICE="601500";     //�̻�֧������ֹͣ
	
	
	public static final String REQUEST_PARAM_VALIDATE="501108" ;//����У�����
	
	public static final String ORDER_NOTFOUNDBYSEQ="501107";    //�����ڸö���
	
	public static final String ORDER_PRICENOTEQUAL="501106";    //���з��ض���������̻���һ��
	public static final String ORDER_SAVE="501105";             //֧�����������ִ���
	public static final String ORDER_STATEILL="501101";         //�ö����Ѿ�����,�����ظ��ύ
	public static final String ORDER_UNPAY   ="501124";               //�ö���δ֧��
	public static final String ORDER_PAYFAILURE="501125";             //�ö���֧��ʧ��
	public static final String ORDER_ILLSTATE  ="501126";             //��Ч����״̬
	
	public static final String SERVICE_URLPARAM="501111";         //ȱ�ٱ�Ҫ���ز���[idx],������ʶ��
	
	public static final String SESSION_INVALIDATE="700000";      //SESSIONʧЧ
	public static final String ORDER_SUBMIT_OVERFLOW="700001";   //�ö�������ύ,�ѽ�ֹ����֧��
	public static final String PAYRESULT_HANDLE_FAILURE="700002"; //����֧�����ʧ��

}
