/*     */ package ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
/*     */ import org.apache.commons.httpclient.HttpClient;
/*     */ import org.apache.commons.httpclient.HttpConnectionManager;
/*     */ import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
/*     */ import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
/*     */ import org.apache.commons.httpclient.methods.PostMethod;
/*     */ import org.apache.commons.httpclient.methods.RequestEntity;
/*     */ import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
/*     */ import org.apache.commons.httpclient.params.HttpMethodParams;
/*     */ 
/*     */ public class HttpUtil
/*     */ {
/*     */   public static String encoding;
/*     */   private static final HttpConnectionManager connectionManager;
/*  45 */   private static final HttpClient client ;
/*     */ 
/*     */   static
/*     */   {
/*  39 */     HttpConnectionManagerParams params = loadHttpConfFromFile();
/*     */ 
/*  41 */     connectionManager = new MultiThreadedHttpConnectionManager();
/*     */ 
/*  43 */     connectionManager.setParams(params);

			client = new HttpClient(connectionManager);
/*     */   }
/*     */ 
/*     */   private static HttpConnectionManagerParams loadHttpConfFromFile()
/*     */   {
/*  49 */     Properties p = new Properties();
/*     */     try {
/*  51 */       p.load(HttpUtil.class.getResourceAsStream(HttpUtil.class.getSimpleName().toLowerCase() + ".properties"));
/*     */     }
/*     */     catch (IOException localIOException) {
/*     */     }
/*  55 */     encoding = p.getProperty("http.content.encoding");
/*     */ 
/*  57 */     HttpConnectionManagerParams params = new HttpConnectionManagerParams();
/*  58 */     params.setConnectionTimeout(Integer.parseInt(p.getProperty("http.connection.timeout")));
/*  59 */     params.setSoTimeout(Integer.parseInt(p.getProperty("http.so.timeout")));
/*  60 */     params.setStaleCheckingEnabled(Boolean.parseBoolean(p.getProperty("http.stale.check.enabled")));
/*  61 */     params.setTcpNoDelay(Boolean.parseBoolean(p.getProperty("http.tcp.no.delay")));
/*  62 */     params.setDefaultMaxConnectionsPerHost(Integer.parseInt(p.getProperty("http.default.max.connections.per.host")));
/*  63 */     params.setMaxTotalConnections(Integer.parseInt(p.getProperty("http.max.total.connections")));
/*  64 */     params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler(0, false));
/*  65 */     return params;
/*     */   }
/*     */ 
/*     */   public static String post(String url, String encoding, String content) {
/*     */     try {
/*  70 */       byte[] resp = post(url, content.getBytes(encoding));
/*  71 */       if (resp == null)
/*  72 */         return null;
/*  73 */       return new String(resp, encoding); } catch (UnsupportedEncodingException e) {
/*     */     }
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   public static String post(String url, String content)
/*     */   {
/*  81 */     return post(url, encoding, content);
/*     */   }
/*     */ 
/*     */   public static byte[] post(String url, byte[] content)
/*     */   {
/*     */     try {
/*  87 */       return post(url, new ByteArrayRequestEntity(content));
/*     */     } catch (Exception e) {
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   public static byte[] post(String url, RequestEntity requestEntity)
/*     */     throws Exception
/*     */   {
/*  96 */     PostMethod method = new PostMethod(url);
/*  97 */     method.addRequestHeader("Connection", "Keep-Alive");
/*  98 */     method.getParams().setCookiePolicy("ignoreCookies");
/*  99 */     method.getParams().setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler(0, false));
/* 100 */     method.setRequestEntity(requestEntity);
/* 101 */     method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
/*     */     try
/*     */     {
/* 104 */       int statusCode = client.executeMethod(method);
/* 105 */       if (statusCode != 200) {
/* 106 */         return null;
/*     */       }
/* 108 */       return method.getResponseBody();
/*     */     }
/*     */     catch (SocketTimeoutException e) {
/* 111 */       return null;
/*     */     } catch (Exception e) {
/* 113 */       return null;
/*     */     } finally {
/* 115 */       method.releaseConnection();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\lee\Desktop\web_update_02_06\web\WEB-INF\classes\
 * Qualified Name:     ebank.core.bank.third.unionmobileutil.com.unionpay.upmp.sdk.util.HttpUtil
 * JD-Core Version:    0.6.2
 */