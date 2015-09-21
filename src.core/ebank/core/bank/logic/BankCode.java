/*
 * @Id: BankCode.java 11:09:09 2006-2-20
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.bank.logic;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

/**
 * @author 
 * Description: NATIVED���б�Ŷ���
 * bank.properties ���õ�ǰʹ�õ�ʵ��
 * 
 */
public class BankCode {
	/*
	 * �����б���������λ(ǰ׺)+���б���
	 * �����������ڳ���(�����б���)���������ڳ���汾����
	 */
	public static final String[] BKID_ICBC      ={getsid("ICBC"),"102","2","59","61","1021","1022"};          //�°湤������
	public static final String[] BKID_ABC       ={getsid("ABC"),"103","54"};           //ũҵ����
	public static final String[] BKID_BOC2      ={getsid("BOC"),"104","55"};           //�й�����
	public static final String[] BKID_BOC       ={"1041"};                            //�й�����
	public static final String[] BKID_CCB       ={getsid("CCB"),"105","46",
                                                  "41","44","47","45","48","49"};    //��������
	public static final String[] BKID_CCB_BIG       ={getsid("CCB_BIG"),"1052"};    //��������	
	
	public static final String[] BKID_COMM      ={getsid("BOCM"),"301","57"};           //��ͨ����	
	
	
	public static final String[] BKID_ICBC_M={getsid("ICBC_M"),"61"};    //�����ֻ�����	
	
	public static final String[] BKID_ICBC_11 = {"102"} ; //�°湤������ version 1.0.0.11
	
	public static final String[] BKID_GYL       ={getsid("GYL"),"0700","0701","1","7","8","9","10","11","12","13",
        										  "14","15","16","17","18","19",
        										  "20","21","50",
        										  "51","52"};        //�㶫����
	
	public static final String[] BKID_SYL       ={getsid("SYL"),"0702","22","23","24","25",
							                      "26","27","28",
							                      "29","30","31","32","33",
							                      "34","35","36","37","39"};     //�Ϻ�����		
	public static final String[] BKID_PRECARD         ={getsid("PRECARD"),"0800"};        //Ԥ����֧��
	public static final String[] BKID_YEEPAY         ={getsid("YEEPAY"),"0900"};        //�ױ�֧��	
	
	public static final String[] BKID_CMBC        ={getsid("CMBC"),"305","3"};   //�������п�	
	public static final String[] BKID_CMBC_NETBANK={getsid("CMBC_NETBANK"),"3051","4"};    //������������
	
	public static final String[] BKID_CMBC_B2B={getsid("CMBC_B2B"),"9305","5"};    //������������
	
    public static final String[] BKID_GDB         ={getsid("GDB"),"306","60"};   //�㷢����		
	
	public static final String[] BKID_SDB         ={getsid("SDB"),"307","62"};   //�չ����
	
	public static final String[] BKID_CMB         ={getsid("CMB"),"308","42","43"}; //��������
	
	public static final String[] BKID_CIB         ={getsid("CIB"),"309"};        //��ҵ����
	
	public static final String[] BKID_BOB         ={getsid("BOB"),"310"};        //��������
	
	public static final String[] BKID_HUAX        ={getsid("HXB"),"311"};        //��������
	
	public static final String[] BKID_CEB         ={getsid("CEB"),"312"};        //�������
	
	public static final String[] BKID_CITIC         ={getsid("CITIC"),"313"};        //����ʵҵ
	
	public static final String[] BKID_SPDB        ={getsid("SPDB"),"314"};       //�Ϻ��ַ�����
	
	public static final String[] BKID_CQCB        ={getsid("CQCB"),"315"};       //������ҵ����
	
	public static final String[] BKID_NJB         ={getsid("NJB"),"316"};       //�Ͼ�����
	
	public static final String[] BKID_CBHB        ={"317"};      //��������
	
	public static final String[] BKID_BEA         ={"318"};        //��������
	
	public static final String[] BKID_BJRCB       ={"319"};        //��������
	
	public static final String[] BKID_PSBC        ={"323"};       // ������������
	
	public static final String[] BKID_HSBK      ={"333"};       // ��������
	
	public static final String[] BKID_SHBANK      ={"334"};       // �Ϻ�����
	
	public static final String[] BKID_VISA        ={getsid("VISA"),"0801","VISA","visa"};        //visa ��
	
	public static final String[] BKID_MASTERCARD  ={getsid("MASTERCAR"),"0802","mastercard"};   //MASTERCARD ��	
	
	public static final String[] BKID_JCB         ={getsid("JCB"),"0803","jcb"};         //jcb	
	
	public static final String[] BKID_MOTO        ={getsid("MOTOPAY"),"081"};           //motopay
	
	public static final String[] BKID_Migs        ={getsid("MIGS"),"320"};           //MiGS
	
	public static final String[] BKID_AsiaPay_HKD     = {"330"} ; // AsiaPay  
	public static final String[] BKID_AsiaPay_MOP     = {"331"} ;
	
	
	
	public static final String[] BKID_PAY19_NULL         ={getsid("PAY19")};           //pay19 NULL 040
	public static final String[] BKID_PAY19_UN         ={getsid("PAY19_UNICOM")};           //pay19 unicom 041
	public static final String[] BKID_PAY19_MB         ={getsid("PAY19_MOBILE")};           //pay19 MOBILE 042
	
	public static final String[] BKID_RYF_I         ={getsid("PAYRYF_I")};           //pay19 MOBILE 046
	
	public static final String[] BKID_PAYRYF           ={getsid("PAYRYF")};                //���⸶045
	
	public static final String[] BKID_CMBC_CALLER ={getsid("CMBC_CALLER"),"1105"};     //�����绰����
	public static final String[] BKID_ICBC_CALLER ={getsid("ICBC_CALLER"),"1102"};      //���е绰����
	public static final String[] BKID_CMB_CALLER ={getsid("CMB_CALLER"),"1108"};      //���е绰����
	
	public static final String[] BKID_HZBANK      ={"335"};       // ��������
	
	public static final String[] BKID_B2B_ICBC={"9102"};
	public static final String[] BKID_B2B_CMB={"9308"};
	public static final String[] BKID_B2B_CCB={"9105"};
	public static final String[] BKID_B2B_CEB={"9312"};
	public static final String[] BKID_B2B_SPDB={"9314"};
	public static final String[] BKID_B2B_ABC={"9103"};
	public static final String[] BKID_B2B_UPAY={"9702"};
	public static final String[] BKID_B2B_SDB={"9307"};
	public static final String[] BKID_B2B_BOC={"9104"};
	public static final String[] BKID_B2B_COMM={getsid("B2B_BOCM"),"9301"};
	public static final String[] BKID_B2B_CITICV6={"9313"};
	public static final String[] BKID_B2B_HSBK={"9333"};

	public static final String[] BKID_HeePay={"0908"};//�㸶
	
	  public static final String[] BKID_LefuPay = { "0907" };

	  public static final String[] BKID_LefuPayMobile = { "0917" };

	  public static final String[] BKID_fengPay = { "2927" };

	  public static final String[] BKID_unionMPay = { "2928" };
	
	public static final String[] BKID_B2C_TFT={"2908"};//�ڸ�ͨ
	
	public static final String[] BKID_B2C_RONGXIN={"2911"};//�ڸ�ͨ
	
	public static final String[] BKID_B2C_BANKONLINE={"2907"};//��������
	
	public static final String[] BKID_PREPAYCARD      ={"8101"};       // ����Ԥ���ѿ�
	public static final String[] BKID_FXPPAYCARD      ={"8102"};       // ���˸�ʢԤ���ѿ�
	public static final String[] BKID_CREDITCARD      ={"8103"};       // ���˸�ʢԤ���ѿ�
	
	 
	//���м���
	public static final String[][] BKID={BKID_ICBC,
		                                 BKID_ABC,
		                                 BKID_CCB,
		                                 BKID_CCB_BIG,
		                                 BKID_BOC,
		                                 BKID_BOC2,
		                                 BKID_CMB,		                                 
		                                 BKID_BOB,		                                
		                                 BKID_GYL,
		                                 BKID_SYL,
		                                 BKID_COMM,
		                                 BKID_CMBC,
		                                 BKID_CMBC_NETBANK,
		                                 BKID_CMBC_B2B,
		                                 BKID_GDB,
		                                 BKID_SDB,		                                 
		                                 BKID_CIB,
		                                 BKID_VISA,
		                                 BKID_MASTERCARD,
		                                 BKID_JCB,	                                
		                                 BKID_HUAX,
		                                 BKID_CEB,
		                                 BKID_CITIC,
		                                 BKID_SPDB,
		                                 BKID_CMBC_CALLER,
		                                 BKID_ICBC_CALLER,
		                                 BKID_CMB_CALLER,
		                                 BKID_MOTO,
		                                 BKID_CQCB,
		                                 BKID_Migs,
		                                 BKID_PAY19_NULL,
		                                 BKID_PAY19_UN,
		                                 BKID_PAY19_MB,
		                                 BKID_PAYRYF,
		                                 BKID_RYF_I,
		                                 BKID_NJB,
		                                 BKID_CBHB,
		                                 BKID_BEA,
		                                 BKID_BJRCB,
		                                 BKID_PSBC,
		                                 BKID_AsiaPay_HKD,
		                                 BKID_AsiaPay_MOP,
		                                 BKID_ICBC_11,
		                                 BKID_PRECARD,
		                                 BKID_YEEPAY,
		                                 BKID_HSBK,
		                                 BKID_HZBANK,
		                                 BKID_B2B_CMB,
		                                 BKID_SHBANK,
		                                 BKID_B2B_CCB,
		                                 BKID_B2B_CEB,
		                                 BKID_B2B_SPDB,
		                                 BKID_B2B_ABC,
		                                 BKID_B2B_UPAY,
		                                 BKID_B2B_SDB,
		                                 BKID_PREPAYCARD,
										 BKID_FXPPAYCARD,
		                                 BKID_CREDITCARD,
		                                 BKID_B2B_COMM,
		                                 BKID_B2B_CITICV6,
		                                 BKID_B2B_HSBK
		                                 };
	/**
	 * ȡ�÷���ʵ��
	 * @param name
	 * @return
	 */
	public static String getsid(String name){
	    Properties defaultProps = new Properties();
        InputStream in = null;
        try {
        	in = new ClassPathResource("conf/bank.properties").getInputStream();
            defaultProps.load(in);
            return defaultProps.getProperty(name);
        } catch (Exception e) {
            System.err.println("Error: could not find the config of bank");
            e.printStackTrace();
            return null;
        }
		
	}
		
}
