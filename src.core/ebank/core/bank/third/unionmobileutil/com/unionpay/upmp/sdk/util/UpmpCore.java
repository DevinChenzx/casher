/*     */ package ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.util;
/*     */ 
/*     */ import ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.conf.UpmpConfig;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class UpmpCore
/*     */ {
/*     */   public static final String QSTRING_EQUAL = "=";
/*     */   public static final String QSTRING_SPLIT = "&";
/*     */ 
/*     */   public static Map<String, String> paraFilter(Map<String, String> para)
/*     */   {
/*  38 */     Map result = new HashMap();
/*     */ 
/*  40 */     if ((para == null) || (para.size() <= 0)) {
/*  41 */       return result;
/*     */     }
/*     */ 
/*  44 */     for (String key : para.keySet()) {
/*  45 */       String value = (String)para.get(key);
/*  46 */       if ((value != null) && (!value.equals("")) && (!key.equalsIgnoreCase("signature")) && 
/*  47 */         (!key.equalsIgnoreCase("signMethod")))
/*     */       {
/*  50 */         result.put(key, value);
/*     */       }
/*     */     }
/*  53 */     return result;
/*     */   }
/*     */ 
/*     */   public static String buildSignature(Map<String, String> req)
/*     */   {
/*  62 */     String prestr = createLinkString(req, true, false);
/*  63 */     prestr = prestr + "&" + UpmpMd5Encrypt.md5(UpmpConfig.SECURITY_KEY);
/*  64 */     return UpmpMd5Encrypt.md5(prestr);
/*     */   }
/*     */ 
/*     */   public static String createLinkString(Map<String, String> para, boolean sort, boolean encode)
/*     */   {
/*  76 */     List keys = new ArrayList(para.keySet());
/*     */ 
/*  78 */     if (sort) {
/*  79 */       Collections.sort(keys);
/*     */     }
/*  81 */     StringBuilder sb = new StringBuilder();
/*  82 */     for (int i = 0; i < keys.size(); i++) {
/*  83 */       String key = (String)keys.get(i);
/*  84 */       String value = (String)para.get(key);
/*     */ 
/*  86 */       if (encode)
/*     */         try {
/*  88 */           value = URLEncoder.encode(value, UpmpConfig.CHARSET);
/*     */         }
/*     */         catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*     */         {
/*     */         }
/*  93 */       if (i == keys.size() - 1)
/*  94 */         sb.append(key).append("=").append(value);
/*     */       else {
/*  96 */         sb.append(key).append("=").append(value).append("&");
/*     */       }
/*     */     }
/*  99 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static Map<String, String> parseQString(String str)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 111 */     Map map = new HashMap();
/* 112 */     int len = str.length();
/* 113 */     StringBuilder temp = new StringBuilder();
/*     */ 
/* 115 */     String key = null;
/* 116 */     boolean isKey = true;
/*     */ 
/* 118 */     for (int i = 0; i < len; i++) {
/* 119 */       char curChar = str.charAt(i);
/*     */ 
/* 121 */       if (curChar == '&') {
/* 122 */         putKeyValueToMap(temp, isKey, key, map);
/* 123 */         temp.setLength(0);
/* 124 */         isKey = true;
/*     */       }
/* 126 */       else if (isKey) {
/* 127 */         if (curChar == '=') {
/* 128 */           key = temp.toString();
/* 129 */           temp.setLength(0);
/* 130 */           isKey = false;
/*     */         } else {
/* 132 */           temp.append(curChar);
/*     */         }
/*     */       } else {
/* 135 */         temp.append(curChar);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 140 */     putKeyValueToMap(temp, isKey, key, map);
/*     */ 
/* 142 */     return map;
/*     */   }
/*     */ 
/*     */   private static void putKeyValueToMap(StringBuilder temp, boolean isKey, String key, Map<String, String> map) throws UnsupportedEncodingException
/*     */   {
/* 147 */     if (isKey) {
/* 148 */       key = temp.toString();
/* 149 */       if (key.length() == 0) {
/* 150 */         throw new RuntimeException("QString format illegal");
/*     */       }
/* 152 */       map.put(key, "");
/*     */     } else {
/* 154 */       if (key.length() == 0) {
/* 155 */         throw new RuntimeException("QString format illegal");
/*     */       }
/* 157 */       map.put(key, URLDecoder.decode(temp.toString(), UpmpConfig.CHARSET));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\lee\Desktop\web_update_02_06\web\WEB-INF\classes\
 * Qualified Name:     ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.util.UpmpCore
 * JD-Core Version:    0.6.2
 */