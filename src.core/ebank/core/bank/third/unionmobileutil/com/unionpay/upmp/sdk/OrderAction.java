/*    */ package ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk;
/*    */ 
/*    */ import ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.conf.UpmpConfig;
/*    */ import ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.service.UpmpService;
/*    */ import java.text.DateFormat;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class OrderAction
/*    */ {
/*    */   public static String createOrder()
/*    */   {
/* 24 */     Map req = new HashMap();
/* 25 */     req.put("version", UpmpConfig.VERSION);
/* 26 */     req.put("charset", UpmpConfig.CHARSET);
/* 27 */     req.put("transType", "01");
/* 28 */     req.put("merId", UpmpConfig.MER_ID);
/* 29 */     req.put("backEndUrl", UpmpConfig.MER_BACK_END_URL);
/* 30 */     req.put("frontEndUrl", UpmpConfig.MER_FRONT_END_URL);
/* 31 */     req.put("orderDescription", "订单描述");
/* 32 */     req.put("orderTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
/* 33 */     req.put("orderTimeout", "");
/* 34 */     req.put("orderNumber", generateOrdrNo());
/* 35 */     req.put("orderAmount", "1");
/* 36 */     req.put("orderCurrency", "156");
/*    */ 
/* 44 */     Map resp = new HashMap();
/* 45 */     boolean validResp = UpmpService.trade(req, resp);
/*    */ 
/* 50 */     if ((validResp) && ("00".equals(resp.get("respCode"))))
/*    */     {
/* 52 */       return (String)resp.get("tn");
/*    */     }
/*    */ 
/* 55 */     return null;
/*    */   }
/*    */ 
/*    */   public static String generateOrdrNo()
/*    */   {
/* 65 */     DateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
/* 66 */     StringBuilder sb = new StringBuilder(formater.format(new Date()));
/* 67 */     return sb.toString();
/*    */   }
/*    */ 
/*    */   public static void main(String[] args)
/*    */   {
/* 73 */     createOrder();
/*    */   }
/*    */ }

/* Location:           C:\Users\lee\Desktop\web_update_02_06\web\WEB-INF\classes\
 * Qualified Name:     ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.OrderAction
 * JD-Core Version:    0.6.2
 */