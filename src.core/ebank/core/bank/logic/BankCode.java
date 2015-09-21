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
 * Description: NATIVED银行编号对照
 * bank.properties 配置当前使用的实例
 * 
 */
public class BankCode {
	/*
	 * 新银行编码至少三位(前缀)+分行编码
	 * 索引首项用于程序(新银行编码)，后几项用于程序版本兼容
	 */
	public static final String[] BKID_ICBC      ={getsid("ICBC"),"102","2","59","61","1021","1022"};          //新版工商银行
	public static final String[] BKID_ABC       ={getsid("ABC"),"103","54"};           //农业银行
	public static final String[] BKID_BOC2      ={getsid("BOC"),"104","55"};           //中国银行
	public static final String[] BKID_BOC       ={"1041"};                            //中国银行
	public static final String[] BKID_CCB       ={getsid("CCB"),"105","46",
                                                  "41","44","47","45","48","49"};    //建设银行
	public static final String[] BKID_CCB_BIG       ={getsid("CCB_BIG"),"1052"};    //建设银行	
	
	public static final String[] BKID_COMM      ={getsid("BOCM"),"301","57"};           //交通银行	
	
	
	public static final String[] BKID_ICBC_M={getsid("ICBC_M"),"61"};    //工行手机银行	
	
	public static final String[] BKID_ICBC_11 = {"102"} ; //新版工商银行 version 1.0.0.11
	
	public static final String[] BKID_GYL       ={getsid("GYL"),"0700","0701","1","7","8","9","10","11","12","13",
        										  "14","15","16","17","18","19",
        										  "20","21","50",
        										  "51","52"};        //广东银联
	
	public static final String[] BKID_SYL       ={getsid("SYL"),"0702","22","23","24","25",
							                      "26","27","28",
							                      "29","30","31","32","33",
							                      "34","35","36","37","39"};     //上海银联		
	public static final String[] BKID_PRECARD         ={getsid("PRECARD"),"0800"};        //预付卡支付
	public static final String[] BKID_YEEPAY         ={getsid("YEEPAY"),"0900"};        //易宝支付	
	
	public static final String[] BKID_CMBC        ={getsid("CMBC"),"305","3"};   //民生银行卡	
	public static final String[] BKID_CMBC_NETBANK={getsid("CMBC_NETBANK"),"3051","4"};    //民生网银银行
	
	public static final String[] BKID_CMBC_B2B={getsid("CMBC_B2B"),"9305","5"};    //民生网银银行
	
    public static final String[] BKID_GDB         ={getsid("GDB"),"306","60"};   //广发银行		
	
	public static final String[] BKID_SDB         ={getsid("SDB"),"307","62"};   //深发展银行
	
	public static final String[] BKID_CMB         ={getsid("CMB"),"308","42","43"}; //招商银行
	
	public static final String[] BKID_CIB         ={getsid("CIB"),"309"};        //兴业银行
	
	public static final String[] BKID_BOB         ={getsid("BOB"),"310"};        //北京银行
	
	public static final String[] BKID_HUAX        ={getsid("HXB"),"311"};        //华夏银行
	
	public static final String[] BKID_CEB         ={getsid("CEB"),"312"};        //光大银行
	
	public static final String[] BKID_CITIC         ={getsid("CITIC"),"313"};        //中信实业
	
	public static final String[] BKID_SPDB        ={getsid("SPDB"),"314"};       //上海浦发银行
	
	public static final String[] BKID_CQCB        ={getsid("CQCB"),"315"};       //重庆商业银行
	
	public static final String[] BKID_NJB         ={getsid("NJB"),"316"};       //南京银行
	
	public static final String[] BKID_CBHB        ={"317"};      //渤海银行
	
	public static final String[] BKID_BEA         ={"318"};        //东亚银行
	
	public static final String[] BKID_BJRCB       ={"319"};        //东亚银行
	
	public static final String[] BKID_PSBC        ={"323"};       // 邮政储蓄银行
	
	public static final String[] BKID_HSBK      ={"333"};       // 徽商银行
	
	public static final String[] BKID_SHBANK      ={"334"};       // 上海银行
	
	public static final String[] BKID_VISA        ={getsid("VISA"),"0801","VISA","visa"};        //visa 卡
	
	public static final String[] BKID_MASTERCARD  ={getsid("MASTERCAR"),"0802","mastercard"};   //MASTERCARD 卡	
	
	public static final String[] BKID_JCB         ={getsid("JCB"),"0803","jcb"};         //jcb	
	
	public static final String[] BKID_MOTO        ={getsid("MOTOPAY"),"081"};           //motopay
	
	public static final String[] BKID_Migs        ={getsid("MIGS"),"320"};           //MiGS
	
	public static final String[] BKID_AsiaPay_HKD     = {"330"} ; // AsiaPay  
	public static final String[] BKID_AsiaPay_MOP     = {"331"} ;
	
	
	
	public static final String[] BKID_PAY19_NULL         ={getsid("PAY19")};           //pay19 NULL 040
	public static final String[] BKID_PAY19_UN         ={getsid("PAY19_UNICOM")};           //pay19 unicom 041
	public static final String[] BKID_PAY19_MB         ={getsid("PAY19_MOBILE")};           //pay19 MOBILE 042
	
	public static final String[] BKID_RYF_I         ={getsid("PAYRYF_I")};           //pay19 MOBILE 046
	
	public static final String[] BKID_PAYRYF           ={getsid("PAYRYF")};                //如意付045
	
	public static final String[] BKID_CMBC_CALLER ={getsid("CMBC_CALLER"),"1105"};     //民生电话银行
	public static final String[] BKID_ICBC_CALLER ={getsid("ICBC_CALLER"),"1102"};      //工行电话银行
	public static final String[] BKID_CMB_CALLER ={getsid("CMB_CALLER"),"1108"};      //招行电话银行
	
	public static final String[] BKID_HZBANK      ={"335"};       // 杭州银行
	
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

	public static final String[] BKID_HeePay={"0908"};//汇付
	
	  public static final String[] BKID_LefuPay = { "0907" };

	  public static final String[] BKID_LefuPayMobile = { "0917" };

	  public static final String[] BKID_fengPay = { "2927" };

	  public static final String[] BKID_unionMPay = { "2928" };
	
	public static final String[] BKID_B2C_TFT={"2908"};//腾付通
	
	public static final String[] BKID_B2C_RONGXIN={"2911"};//腾付通
	
	public static final String[] BKID_B2C_BANKONLINE={"2907"};//网银在线
	
	public static final String[] BKID_PREPAYCARD      ={"8101"};       // 安心预付费卡
	public static final String[] BKID_FXPPAYCARD      ={"8102"};       // 方兴高盛预付费卡
	public static final String[] BKID_CREDITCARD      ={"8103"};       // 方兴高盛预付费卡
	
	 
	//银行集合
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
	 * 取得服务实例
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
