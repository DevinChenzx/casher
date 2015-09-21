/*
 * @(#)PKCS7Tool.java 1.0 2008-9-23
 * Copyright (c) 2008 Bank Of China Software Center
 * All rights reserved.
 */

package beartool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.security.auth.x500.X500Principal;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;

//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * PKCS7Tool.java
 * pkcs7格式签名工具
 * 
 * @version 1.0
 * @author SUNY
 * Written Date: 2008-9-23
 */
public class PKCS7Tool {

	/**签名*/
	private static final int SIGNER = 1;
	/**验证*/
	private static final int VERIFIER = 2;
	/**用途*/
	private int mode = 0;
	/**摘要算法*/
	private String digestAlgorithm = "SHA1";
	/**签名算法*/
	private String signingAlgorithm = "SHA1withRSA";
	/**签名证书链*/
	private X509Certificate[] certificates = null;
	/**签名私钥*/
	private PrivateKey privateKey = null;
	/**根证书*/
	private Certificate rootCertificate = null;

	/**
	 * 私有构造方法
	 */
	private PKCS7Tool(int mode) {
		this.mode = mode;
	}

	/**
	 * 取得签名工具
	 * 加载证书库, 取得签名证书链和私钥
	 * @param keyStorePath 证书库路径
	 * @param keyStorePassword 证书库口令
	 * @throws GeneralSecurityException
	 * @throws IOException 
	 */
	public static PKCS7Tool getSigner(String keyStorePath, String keyStorePassword, String keyPassword)
			throws GeneralSecurityException, IOException {
		//加载证书库
		KeyStore keyStore = null;
		if (keyStorePath.toLowerCase().endsWith(".pfx"))
			keyStore = KeyStore.getInstance("PKCS12");
		else 
			keyStore = KeyStore.getInstance("JKS");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(keyStorePath);
			keyStore.load(fis, keyStorePassword.toCharArray());
		} finally {
			if (fis != null)
				fis.close();
		}
		//在证书库中找到签名私钥
		Enumeration aliases = keyStore.aliases();
		String keyAlias = null;
		if (aliases != null) {
			while (aliases.hasMoreElements()) {
				keyAlias = (String) aliases.nextElement();
				Certificate[] certs = keyStore.getCertificateChain(keyAlias);
				if (certs == null || certs.length == 0)
					continue;
				X509Certificate cert = (X509Certificate)certs[0];
				if (matchUsage(cert.getKeyUsage(), 1)) {
					try {
						cert.checkValidity();
					} catch (CertificateException e) {
						continue;
					}
					break;
				}
			}
		}
		//没有找到可用签名私钥
		if (keyAlias == null)
			throw new GeneralSecurityException("None certificate for sign in this keystore");

		X509Certificate[] certificates = null;
		if (keyStore.isKeyEntry(keyAlias)) {
			//检查证书链
			Certificate[] certs = keyStore.getCertificateChain(keyAlias);
			for (int i = 0; i < certs.length; i ++) {
				if (!(certs[i] instanceof X509Certificate))
					throw new GeneralSecurityException("Certificate[" + i + "] in chain '" + keyAlias + "' is not a X509Certificate.");
			}
			//转换证书链
			certificates = new X509Certificate[certs.length];
			for (int i = 0; i < certs.length; i ++)
				certificates[i] = (X509Certificate) certs[i];
		} else if (keyStore.isCertificateEntry(keyAlias)) {
			//只有单张证书
			Certificate cert = keyStore.getCertificate(keyAlias);
			if (cert instanceof X509Certificate) {
				certificates = new X509Certificate[] {(X509Certificate) cert};
			}
		} else {
			throw new GeneralSecurityException(keyAlias + " is unknown to this keystore");
		}

		PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, keyPassword.toCharArray());
		//没有私钥抛异常
		if (privateKey == null) {
			throw new GeneralSecurityException(keyAlias + " could not be accessed");
		}
		
		PKCS7Tool tool = new PKCS7Tool(SIGNER);
		tool.certificates = certificates;
		tool.privateKey = privateKey;
		return tool;
	}

	/**
	 * 取得验签名工具
	 * 加载信任根证书
	 * @param rootCertificatePath 根证书路径
	 * @throws GeneralSecurityException
	 * @throws IOException 
	 */
	public static PKCS7Tool getVerifier(String rootCertificatePath)
			throws GeneralSecurityException, IOException {
		//加载根证书
		FileInputStream fis = null;
		Certificate rootCertificate = null;
		try {
			fis = new FileInputStream(rootCertificatePath);
			CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
			try {
				rootCertificate = certificatefactory.generateCertificate(fis);
			}
			catch(Exception exception) {
				BASE64Decoder base64decoder = new BASE64Decoder();
				InputStream is = new ByteArrayInputStream(base64decoder.decodeBuffer(fis));
				rootCertificate = certificatefactory.generateCertificate(is);
			}
		} finally {
			if (fis != null)
				fis.close();
		}		

		PKCS7Tool tool = new PKCS7Tool(VERIFIER);
		tool.rootCertificate = rootCertificate;
		return tool;
	}

	/**
	 * 签名
	 * @param data 数据
	 * @return signature 签名结果
	 * @throws GeneralSecurityException
	 * @throws IOException 
	 * @throws IllegalArgumentException
	 */
	public String sign(byte[] data) throws GeneralSecurityException, IOException {
		if (mode != SIGNER)
			throw new IllegalStateException("call a PKCS7Tool instance not for signature.");

		Signature signer = Signature.getInstance(signingAlgorithm);
		signer.initSign(privateKey);
		signer.update(data, 0, data.length);
		byte[] signedAttributes = signer.sign();

		ContentInfo contentInfo = null;

		contentInfo = new ContentInfo(ContentInfo.DATA_OID, null);
		//根证书
		X509Certificate x509 = certificates[certificates.length - 1];
		// 如果jdk1.4则用以下语句
		// sun.security.util.BigInt serial = new sun.security.util.BigInt(x509.getSerialNumber());
		// 如果jdk1.5则用以下语句
		java.math.BigInteger serial = x509.getSerialNumber();
		//签名信息
		SignerInfo si = new SignerInfo(new X500Name(x509.getIssuerDN().getName()), // X500Name, issuerName,
				serial, // x509.getSerialNumber(), BigInteger serial,
				AlgorithmId.get(digestAlgorithm), // AlgorithmId, digestAlgorithmId,
				null, // PKCS9Attributes, authenticatedAttributes,
				new AlgorithmId(AlgorithmId.RSAEncryption_oid), // AlgorithmId, digestEncryptionAlgorithmId,
				signedAttributes, // byte[] encryptedDigest,
				null); // PKCS9Attributes unauthenticatedAttributes) {

		SignerInfo[] signerInfos = { si };

		// 构造PKCS7数据
		AlgorithmId[] digestAlgorithmIds = {AlgorithmId.get(digestAlgorithm)};
		PKCS7 p7 = new PKCS7(digestAlgorithmIds, contentInfo, certificates, signerInfos);

		ByteArrayOutputStream baout = new ByteArrayOutputStream();
		p7.encodeSignedData(baout);
		//Base64编码
		return (new BASE64Encoder()).encode(baout.toByteArray());
//		return BASE64Encoder.encode();
	}

	/**
	 * 验证签名(无CRL)
	 * @param signature 签名签名结果
	 * @param data 被签名数据
	 * @param dn 签名证书dn, 如果为空则不做匹配验证
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 * @throws InvalidKeyException
	 * @throws CertificateException
	 * @throws NoSuchProviderException
	 */
	public void verify(String signature, byte[] data, String dn) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateException, NoSuchProviderException  {
		if (mode != VERIFIER)
			throw new IllegalStateException("call a PKCS7Tool instance not for verify.");
		PKCS7 p7 = new PKCS7((new BASE64Decoder()).decodeBuffer(signature));
//		PKCS7 p7 = new PKCS7(Base64.decode(signature));

		//验证签名本身、证书用法、证书扩展
		SignerInfo[] sis = p7.verify(data);

		// check the results of the verification
		if (sis == null)
			throw new SignatureException("Signature failed verification, data has been tampered");

		for (int i = 0; i < sis.length; i ++) {
			SignerInfo si = sis[i];

			X509Certificate cert = si.getCertificate(p7);
			//证书是否过期验证，如果不用系统日期可用cert.checkValidity(date);
			cert.checkValidity();
			if (! cert.equals(rootCertificate)) {
				//验证证书签名
				cert.verify(rootCertificate.getPublicKey());
			}
			//验证dn
			if (i == 0 && dn != null) {
				X500Principal name = cert.getSubjectX500Principal();
				if (!dn.equals(name.getName(X500Principal.RFC1779)) && !new X500Principal(dn).equals(name))
					throw new SignatureException("Signer dn '" + name.getName(X500Principal.RFC1779) + "' does not matchs '" + dn + "'");
			}
		}
	}

	/**
	 * 匹配私钥用法
	 * @param keyUsage
	 * @param usage
	 * @return
	 */
	private static boolean matchUsage(boolean[] keyUsage, int usage) {
		if (usage == 0 || keyUsage == null)
			return true;
		for (int i = 0; i < Math.min(keyUsage.length, 32); i ++) {
			if ((usage & (1 << i)) != 0 && !keyUsage[i])
			return false;
		}
		return true;
	}

	/**
	 * @return 返回 digestAlgorithm。
	 */
	public final String getDigestAlgorithm() {
		return digestAlgorithm;
	}

	/**
	 * @param digestAlgorithm 要设置的 digestAlgorithm。
	 */
	public final void setDigestAlgorithm(String digestAlgorithm) {
		this.digestAlgorithm = digestAlgorithm;
	}

	/**
	 * @return 返回 signingAlgorithm。
	 */
	public final String getSigningAlgorithm() {
		return signingAlgorithm;
	}

	/**
	 * @param signingAlgorithm 要设置的 signingAlgorithm。
	 */
	public final void setSigningAlgorithm(String signingAlgorithm) {
		this.signingAlgorithm = signingAlgorithm;
	}
	
	public static void main(String[] args) {
		try{
			PKCS7Tool tool = PKCS7Tool.getVerifier("c:/BOCSign.cer");
			String signature = "MIIEZgYJKoZIhvcNAQcCoIIEVzCCBFMCAQMxCTAHBgUrDgMCGjALBgkqhkiG9w0BBwGgggMdMIIDGTCCAgGgAwIBAgIQIbAY5mFnk0lHZcajtcSdEzANBgkqhkiG9w0BAQUFADBdMQswCQYDVQQGEwJDTjEWMBQGA1UEChMNQkFOSyBPRiBDSElOQTEQMA4GA1UECBMHQkVJSklORzEQMA4GA1UEBxMHQkVJSklORzESMBAGA1UEAxMJQk9DTkVUIENBMB4XDTA4MTExMDA3NDQwN1oXDTE3MDEyNzA3NDQwN1owNzELMAkGA1UEBhMCQ04xFjAUBgNVBAoTDUJBTksgT0YgQ0hJTkExEDAOBgNVBAMTB0JPQ1NpZ24wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAKaXwH6qjtEbS1P5xzxrO3J2r5ClZptoAy2AGSwNcKEeuCupnUrQyWvtLriK+6FSe/cCjuQV+YaHmengxrr3cVfDcl6O75xCCIR9KDYh+G9gQ6G7L3HkxDy7nITMC18Q06H2ASsOOWNltqGY3XEhcZSHsi1GNnmZnL+fsdgw8FT9AgMBAAGjfzB9MB8GA1UdIwQYMBaAFC5bWTgtYEZoVxobo/ime8UPZm7eMC4GA1UdHwQnMCUwI6AhoB+GHWh0dHA6Ly8xOTIuMTY4LjkuMTQ5L2NybDEuY3JsMAsGA1UdDwQEAwID+DAdBgNVHQ4EFgQUAIsaQbcg67J+ieK8G/ln5ZTq530wDQYJKoZIhvcNAQEFBQADggEBACxTTzp8crpgzm5gdWf1RGOm06KrWYihgj7hxnccCxofVGj4hPtwF90J0QEoV6UZ8m55hY3vi/5cdxSc+KhyH/15sxUup5W+uWE1GDD/6VFqwS9AiHvILNr/joAuGbsZI+S30cGRuZp5WdFOUskyG5kJ1xeGXpfHt/duMnMB/YYca+1hP1WllJMGaVEBcyM++40ykdCOesqcV7YobuunwsQ7akpdj5fAl3pV58B8IVIy2V3cLDRSRU3D6cbtJXF+WtAln+cY9ncEGi0zmZp9CQQYQ7RZlJ7j1gk33+3+ScvCxJ3oftY9om5OrbjjX4rQN3lOi8mx0xnLN7tKGFZvAFkxggETMIIBDwIBAzBxMF0xCzAJBgNVBAYTAkNOMRYwFAYDVQQKEw1CQU5LIE9GIENISU5BMRAwDgYDVQQIEwdCRUlKSU5HMRAwDgYDVQQHEwdCRUlKSU5HMRIwEAYDVQQDEwlCT0NORVQgQ0ECECGwGOZhZ5NJR2XGo7XEnRMwBwYFKw4DAhowCwYJKoZIhvcNAQEBBIGAIcxD9s7JTI51u/uoXQkPUNOhP6YDPKnXKgW9YdGGq4VwVjf/S02dfY+pyLEjCOZiZonI343cMAOu9sHWt3lveiz1ETdzCC6FYntg6HDxpy43C0wtWt2ihoEpcnH8KRiPKZB6rUuu65Btrs1UwKWeBSOXOdr6k1eU1z74QHO6mjo=";
			byte[] data = "104480148140006|200811120000107|7093|01|20081112154843|1|0.03".getBytes();
			String dn = "CN=BOCSign,O=BANK OF CHINA,C=CN";
			tool.verify(signature, data, dn);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
