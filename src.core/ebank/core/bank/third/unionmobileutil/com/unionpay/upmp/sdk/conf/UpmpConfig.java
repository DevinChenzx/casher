/*    */ package ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.conf;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.util.PropertyResourceBundle;
/*    */ 
/*    */ public class UpmpConfig
/*    */ {
/*    */   public static String VERSION;
/*    */   public static String CHARSET;
/*    */   public static String TRADE_URL;
/*    */   public static String QUERY_URL;
/*    */   public static String MER_ID;
/*    */   public static String MER_BACK_END_URL;
/*    */   public static String MER_FRONT_END_URL;
/*    */   public static String MER_FRONT_RETURN_URL;
/*    */   public static String SIGN_TYPE;
/*    */   public static String SECURITY_KEY;
/*    */   private static final String KEY_VERSION = "version";
/*    */   private static final String KEY_CHARSET = "charset";
/*    */   private static final String KEY_TRADE_URL = "upmp.trade.url";
/*    */   private static final String KEY_QUERY_URL = "upmp.query.url";
/*    */   private static final String KEY_MER_ID = "mer.id";
/*    */   private static final String KEY_MER_BACK_END_URL = "mer.back.end.url";
/*    */   private static final String KEY_MER_FRONT_END_URL = "mer.front.end.url";
/*    */   private static final String KEY_SIGN_METHOD = "sign.method";
/*    */   private static final String KEY_SECURITY_KEY = "security.key";
/*    */   public static final String RESPONSE_CODE_SUCCESS = "00";
/*    */   public static final String SIGNATURE = "signature";
/*    */   public static final String SIGN_METHOD = "signMethod";
/*    */   public static final String RESPONSE_CODE = "respCode";
/*    */   public static final String RESPONSE_MSG = "respMsg";
/*    */   private static final String CONF_FILE_NAME = "upmp.properties";
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 78 */       InputStream fis = UpmpConfig.class.getClassLoader().getResourceAsStream("upmp.properties");
/* 79 */       PropertyResourceBundle props = new PropertyResourceBundle(fis);
/* 80 */       VERSION = props.getString("version");
/* 81 */       CHARSET = props.getString("charset");
/* 82 */       TRADE_URL = props.getString("upmp.trade.url");
/* 83 */       QUERY_URL = props.getString("upmp.query.url");
/* 84 */       MER_ID = props.getString("mer.id");
/* 85 */       MER_BACK_END_URL = props.getString("mer.back.end.url");
/* 86 */       MER_FRONT_END_URL = props.getString("mer.front.end.url");
/* 87 */       SIGN_TYPE = props.getString("sign.method");
/* 88 */       SECURITY_KEY = props.getString("security.key");
/*    */     } catch (Exception e) {
/* 90 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\lee\Desktop\web_update_02_06\web\WEB-INF\classes\
 * Qualified Name:     ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.conf.UpmpConfig
 * JD-Core Version:    0.6.2
 */