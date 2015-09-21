/*
 * @Id: NoAuthSSLTrustManager.java ÉÏÎç11:12:14 2010-11-9
 * 
 * @author Kitian@chinabank.com.cn
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.ext;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;



public class NoAuthSSLTrustManager implements X509TrustManager{

	public NoAuthSSLTrustManager(final X509TrustManager defaultTrustManager) {
        super();
        if (defaultTrustManager == null) {
            throw new IllegalArgumentException("Trust manager may not be null");
        }
        //this.defaultTrustManager = defaultTrustManager;
    }
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub
		
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub
		
	}

	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
