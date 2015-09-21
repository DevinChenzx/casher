/*
 * @Id: EventCode.java 16:39:03 2006-2-11
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.common;

/**
 * @author 
 * Description: �쳣�¼��ʹ������
 * 
 */
public class EventCode {
	//�������
	//��ģ��       + ���󼶱�  2λ  ���(2λ����)  
    //��� 1-ϵͳ�쳣 2-��ȫ 3-�����쳣 4-δ֪�쳣 5-ҵ�� 6-ͨ�� 7-web
    //���󼶱� 0-���� 1-�е� 2-��΢
	//07-�߼�
	//08-����	
	//09-ϵͳ��
	//10-b2c֧��
	//11-�⿨
	//12-bank
	//13-MPI
	//15-EPOS
	//16-���ݿ�
	
	public static final String UNEXCPECTED_EXCEPTION="100900";      //����δԤ�ڵ��쳣
	
	public static final String ICBC_SIGNVERIFY      ="201001";      //������֤����ǩ��δͨ�����������н���
	public static final String BOC_MD5VERIFY        ="201002";      //����MD5��֤���ִ���
	public static final String SIGN_VERIFY          ="201003";      //ǩ����֤ʧ��
	
	public static final String SLA_SERVICENOTFOUND  ="511601";     //���ݿⷵ�� ��������Ż��߸÷���δ��ͨ
	public static final String SLA_SERVICEDISABLED  ="511602";     //����ķ����Ѿ���ֹ
	public static final String SLA_SERVICENOTACTIVE ="511603";     //����δ����
	public static final String SLA_SERVICEEXCEPIOTN ="501604";     //����δ��ͨ(�����쳣)
	public static final String SLA_SERVICETYPEEXCEPIOTN ="501605"; //�����������(�����쳣)
	public static final String SLA_SERVICECHANNELCOLSED="501610";   //����ͨ���ر�
	public static final String SLA_NOSERVICECHANNEL    ="501611";   //��ʱû�п����յ�����ͨ��
	public static final String SLA_NOUSERCHANNEL    ="501612";   //�̻�û�����ÿ��õ�֧������
	
	public static final String SP_TIMEOUT="501606";                //������ϵͳ��ʱ,���Ժ��ѯ���������
	public static final String SP_AMOUNT_VALUEOVER="501607";       //���С�ڶ������,���ܽ��н���
		public static final String AMOUNT_QUTOR = "501613";          //���������޶�	
	public static final String AMOUNT_DAY_QUTOR = "501614";      //���������޶�
	
	//new code
	public static final String SELLER_NOTFOUND="520001";  // ��Ϣ����,����δ�ҵ�
	public static final String SELLER_INF_LOST="520002";  //������Ϣ���ܿ�
	public static final String SELLER_SAME_BUYER="520003"; //��Һ�������ϢΪͬһ�û�
	public static final String INVALIDATE_USER="520004";  //��Ч�û�
	
	
	public static final String BUYER_NOTFOUND       ="530001";  // ��Ϣ����,���δ�ҵ�
	public static final String TRX_PROCESSFAILURE   ="530002"; //���״���ʧ��
	public static final String ORDER_STS_NOTPAY     = "530003";  //����״̬�Ѹı�,����֧��
	public static final String ORDER_CHECK_EXCEPTION="530004"; //���׼���쳣
	public static final String ROYATY_PARAME_MISSING="530005"; //ȱ�ٷ������
	public static final String TRX_EXPIRECLOSED     ="530006"; //���׳�ʱ�ѹر�
	public static final String MERCHANT_SERVICESTS  ="530007";              //�ͻ�����״̬�쳣
	public static final String BUYER_PAYPWD_WRONG   ="530008"; //֧���������
	public static final String PAGE_VERFIYCODE       ="530009"; //��֤�����
	public static final String BUYER_ACCOUNT_NOTFOUND="530010"; //�˻���ѯʧ��
	public static final String BUYER_WAIT_OVERTIME   ="530011"; //�Ựʱ�����
	public static final String BUYER_STATEILL        ="530012"; //���״̬�쳣
	public static final String PAGE_MOBILE_VERFIYCODE  ="530024";//�ֻ���֤�����
	public static final String PAGE_MOBILE_RVERFIYCODE  ="530025";//�������ֻ���֤�����
	public static final String PAYACCOUNTNO_UNBINDCODE  ="530026";//�����˺�δ�趨����֧����
	
	public static final String CCB_MD5VERIFY        ="201005";      //CCB
	public static final String CBP_SIGNVERIFY       ="201006";      //CBP
	public static final String CMB_MD5VERIFY        ="201007";      //Cmb
	public static final String CMBC_VERIFY          ="201008";      //CmbC	
	public static final String COMM_VERIFY          ="201009";      //COMM
	public static final String GDB_VERIFY           ="201010";      //gdb
	public static final String GYL_VERIFY           ="201011";      //gYL
	public static final String SDB_VERIFY           ="201012";      //SDB
	public static final String MATERCARD_VE_FAIL    ="201101";      //VE��֤ʧ��
	public static final String VISA_TRANS_ORDER     ="201102";      //visa ����������֤����
	public static final String JCB_VERIFY           ="201103";      //JCB�⿨��֤����
	public static final String JCB_VE_FAILURE	    ="201104";      //VE ��֤ʧ��
	public static final String JCB_VE_OTHERFAILURE  ="201105";      //��֤ʧ��
	public static final String CRYPT_MODE_NOTSUPPORT="200901";      //��֧�ֵİ�ȫ��ʽ,��������
	public static final String CRYPT_EXCEPTION      ="200902";      //���ܳ����쳣
	public static final String CRYPT_VALIADATESIGN  ="200903";      //����ǩ����֤δͨ��
	public static final String MPI_VEFAILURE        ="201300";      //VE��֤ʧ��
	
	public static final String RISK_BLACK           ="201106";                   //�ú�������տ���	
	public static final String RISK_3D_NEED         ="201107";                   //�̻�ֻ��ͨ��3D��֤
	public static final String RISK_UNKNOWN         ="201108";                   //δ֪����
	public static final String RISK_EXCEPTION       ="201109";                   //��֤�쳣�����ڷ���,���Ժ�����
	public static final String RISK_BLACK_MERCHANT  ="201114";                   //��֤�쳣�����ڷ���,���Ժ�����

	
	public static final String WEB_PARAMNULL        ="300800";               //WEB ����ȱ��
	public static final String WEB_PARAMFORMAT      ="300801";               //�������ͻ��ʽ����
	public static final String WEB_PARAMEMPTY       ="300802";               //������ֵ����
	public static final String WEB_PARAM_LENGTH_OVERFLOW     ="300803";               //�����������
	public static final String WEB_PARAM_LENGTH     ="300804";               //���Ȳ����趨
	public static final String WEB_PARAM_LOST       ="300805";               //ȱ�ٱ�Ҫ����
	public static final String WEB_PARAM_CONFLICT   ="300806";               //������ͻ
	public static final String WEB_PARAM_FILTER     ="300807";               //�������˴���,���Ƿ��ַ�
	
	
	public static final String JCB_SERVICE_REQUESTSAME        ="501101";    //�ظ��ķ������� "Please do not use your BACK or RELOAD/REFRESH browser functions or CLOSE your browser while using this service. ");
	public static final String MASTERCARD_SERVICE_REQUESTSAME ="501102" ;
	public static final String MASTERCARD_ORDERNULL           ="501103";       //�����Ų��ܿ�	
	public static final String JCB_ORDERNULL                  ="501104";       //�����Ų��ܿ�
	
	public static final String MASTERCARD_SERVERCONNECT       ="601301";    //MASTERCARD MPI ���Ӵ���
	public static final String MASTERCARD_ORDERVALIDATION     ="511101";    //������֤	
	
	public static final String ORDER_IDNULL                   ="500902" ;           //ȱ�ٶ�����
	public static final String ORDER_OVERFLOW                 ="500903" ;           //�����Ź���
	public static final String ORDER_XMLRESOVERL              ="500800" ;          //��������ݸ�ʽ,���ܽ���
	public static final String ORDER_SAVE                     ="500700";           //�������ܱ���
	public static final String ORDER_NOTFOUND                 ="500701";           //����������	
	public static final String SERVICE_NOTPROVIED             ="500702";           //����δ�ṩ
	public static final String MERCHANT_IDNOTFOUND            ="500703";        //�̻��Ҳ���
	public static final String MERCHANT_STATEILL              ="500704";          //�̻�״̬
	public static final String ORDER_PAYNOTFOUND              ="500705";          //����֧����Ϣδ�ҵ�
	public static final String STATE_UPDATEFAILURE            ="500706";        //״̬����ʧ��
	public static final String MERCHANT_IDNULL                ="500708";        //ȱ���̻�ID��
	public static final String SERVICE_CREDITNOTSUPPORT       ="500709";        //���̻�δ��ͨ�⿨֧��
	public static final String POS_MERCHANTNOTFOUND           ="500711";        //��ҵ���Ӧ���̻�������
	public static final String ORDER_ISEXIST                  ="500712";        //�����Ѵ���
	public static final String ORDER_FRAUD_CHECK              ="500713";           //��Ч��Դ���������󱻾ܾ�
	public static final String ORDER_URL_OUTTIME              ="500714";           //url��ʱ����������ܾ�
	public static final String ORDER_PAYERNOTFOUND            ="500715";           //����ͻ���Ϊ��
	public static final String ORDER_PAYENONOTFOUND            ="500716";           //�����˺�Ϊ��
	
	
	public static final String ORDER_VALIDATE_AMOUNT          ="501132";        //��Ч֧�����
	public static final String ORDER_VERSION_DISABLE          ="501133";        //�汾�Ų���ʶ��
	
	public static final String BOC_COMMUNICATIONERROR ="601000" ;     //�����������,�������ṩ
	public static final String BOC_SIGNERROR          ="601001";      //�����й���������ǩ�����ֹ��ϣ�������ֹ
	public static final String COMM_COMMUNICATIONERROR="601002";      //��ͨ�����������,�������ṩ
	public static final String COMM_SIGNERROR         ="601003";      //����ͨ��������ǩ�����ֹ��ϣ�������ֹ
	public static final String EXE_ACCESSTIMEOUT      ="601300";      //�ײ�ͨ�ų�ʱ�����ܾ�
	public static final String MPI_HANDLEVEFAILURE    ="601301";      //MPI�ײ㴦��VEʧ��
	public static final String MPI_DISABLED3D         ="601302";      //�����ÿ�δ�ڷ����п�ͨ3D��֤����
	public static final String MPI_UNIDENTIFY         ="601303";      //�������޷��Ըÿ���֤
	public static final String MPI_PAFAILURE          ="601304";      //������֤����ʧ�ܣ�֧��ȡ��
	public static final String MPI_PAEXCEPTION        ="601305";      //������֤�г��ֹ��ϣ����Ժ�����
	public static final String MPI_AUTHFAIL           ="601306";      //�ֿ�����֤ʧ��
	public static final String MPI_AUTHNOTPERFORMED   ="601307";      //�ֿ���δ�ܳɹ���֤
	public static final String EPOS_EXCEPTION         ="601308";      //epos�쳣
	public static final String CARD_TRANS_DISABLED    ="601309";      //�������Ƹ��࿨����
	
	
	public static final String SEQ_GET_FAILURE        ="601600";      //�����ݿ���������,�Ժ�����
	
	public static final String ORDER_CREATE_ERROR     ="701000"; //���������쳣
	public static final String ORDER_RESULT_VERFIY_FAILURE     ="701001"; //����У��ʧ��
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
