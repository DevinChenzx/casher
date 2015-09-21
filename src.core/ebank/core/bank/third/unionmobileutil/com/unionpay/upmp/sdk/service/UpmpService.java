/*     */ package ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.service;
/*     */ 
/*     */ import java.util.HashMap;
import java.util.Map;

import ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.conf.UpmpConfig;
import ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.util.HttpUtil;
import ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.util.UpmpCore;
/*     */ 
/*     */ public class UpmpService
/*     */ {
/*     */   public static boolean trade(Map<String, String> req, Map<String, String> resp)
/*     */   {
/*  29 */     String nvp = buildReq(req);
/*  30 */     String respString = HttpUtil.post(UpmpConfig.TRADE_URL, nvp);
/*  31 */     return verifyResponse(respString, resp);
/*     */   }
/*     */ 
/*     */   public static boolean query(Map<String, String> req, Map<String, String> resp)
/*     */   {
/*  41 */     String nvp = buildReq(req);
/*  42 */     String respString = HttpUtil.post(UpmpConfig.QUERY_URL, nvp);
/*  43 */     return verifyResponse(respString, resp);
/*     */   }
/*     */ 
/*     */   public static String buildReserved(Map<String, String> req)
/*     */   {
/*  52 */     StringBuilder merReserved = new StringBuilder();
/*  53 */     merReserved.append("{");
/*  54 */     merReserved.append(UpmpCore.createLinkString(req, false, true));
/*  55 */     merReserved.append("}");
/*  56 */     return merReserved.toString();
/*     */   }
/*     */ 
/*     */   public static String buildReq(Map<String, String> req)
/*     */   {
/*  66 */     Map filteredReq = UpmpCore.paraFilter(req);
/*     */ 
/*  68 */     String signature = UpmpCore.buildSignature(filteredReq);
/*     */ 
/*  71 */     filteredReq.put("signature", signature);
/*  72 */     filteredReq.put("signMethod", UpmpConfig.SIGN_TYPE);
/*     */ 
/*  74 */     return UpmpCore.createLinkString(filteredReq, false, true);
/*     */   }
/*     */ 
/*     */   public static boolean verifySignature(Map<String, String> para)
/*     */   {
/*  83 */     String respSignature = (String)para.get("signature");
/*     */ 
/*  85 */     Map filteredReq = UpmpCore.paraFilter(para);
/*  86 */     String signature = UpmpCore.buildSignature(filteredReq);
/*  87 */     if ((respSignature != null) && (respSignature.equals(signature))) {
/*  88 */       return true;
/*     */     }
/*  90 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean verifyResponse(String respString, Map<String, String> resp)
/*     */   {
/* 101 */     if ((respString != null) && (!"".equals(respString)))
/*     */     {
				Map<String,String> para =new HashMap<String,String>();
/*     */       try
/*     */       {
/* 105 */          para = UpmpCore.parseQString(respString);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 107 */         return false;
/*     */       }
/* 109 */       boolean signIsValid = verifySignature(para);
/*     */ 
/* 111 */       resp.putAll(para);
/*     */ 
/* 113 */       if (signIsValid) {
/* 114 */         return true;
/*     */       }
/* 116 */       return false;
/*     */     }
/*     */ 
/* 120 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Users\lee\Desktop\web_update_02_06\web\WEB-INF\classes\
 * Qualified Name:     ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.service.UpmpService
 * JD-Core Version:    0.6.2
 */