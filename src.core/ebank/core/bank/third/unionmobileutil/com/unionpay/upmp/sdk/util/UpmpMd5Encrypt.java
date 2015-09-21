/*    */ package ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.util;
/*    */ 
/*    */ import ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.conf.UpmpConfig;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.security.MessageDigest;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ 
/*    */ public class UpmpMd5Encrypt
/*    */ {
/*    */   public static String md5(String str)
/*    */   {
/* 28 */     if (str == null) {
/* 29 */       return null;
/*    */     }
/*    */ 
/* 32 */     MessageDigest messageDigest = null;
/*    */     try
/*    */     {
/* 35 */       messageDigest = MessageDigest.getInstance(UpmpConfig.SIGN_TYPE);
/* 36 */       messageDigest.reset();
/* 37 */       messageDigest.update(str.getBytes(UpmpConfig.CHARSET));
/*    */     }
/*    */     catch (NoSuchAlgorithmException e) {
/* 40 */       return str;
/*    */     } catch (UnsupportedEncodingException e) {
/* 42 */       return str;
/*    */     }
/*    */ 
/* 45 */     byte[] byteArray = messageDigest.digest();
/*    */ 
/* 47 */     StringBuffer md5StrBuff = new StringBuffer();
/*    */ 
/* 49 */     for (int i = 0; i < byteArray.length; i++) {
/* 50 */       if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
/* 51 */         md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
/*    */       else {
/* 53 */         md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
/*    */       }
/*    */     }
/* 56 */     return md5StrBuff.toString();
/*    */   }
/*    */ }

/* Location:           C:\Users\lee\Desktop\web_update_02_06\web\WEB-INF\classes\
 * Qualified Name:     ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.util.UpmpMd5Encrypt
 * JD-Core Version:    0.6.2
 */