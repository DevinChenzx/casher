package beartool;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import cfca.util.CertUtil;
import cfca.util.KeyUtil;
import cfca.util.SignatureUtil;
import cfca.util.cipher.lib.JCrypto;
import cfca.util.cipher.lib.Session;
import cfca.x509.certificate.X509Cert;
import cfca.x509.certificate.X509CertValidator;
import ebank.core.common.Base64;
import ebank.core.model.dao.MerchantCertsDAO;
import ebank.core.model.domain.MerchantCertificates;

/**
 * @author shmily
 */
public class RSAUtil {

	private MerchantCertsDAO certDAO;
	
    static Session cfcaSession=null;
    static SignatureUtil signatureUtil=new SignatureUtil();
    static{
        try {
			JCrypto.getInstance().initialize(JCrypto.JSOFT_LIB,null);
			cfcaSession=JCrypto.getInstance().openSession(JCrypto.JSOFT_LIB);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	
	public void setCertDAO(MerchantCertsDAO certDAO) {
		this.certDAO = certDAO;
	}

	/**merchantNo�̶�Ϊ0:��ʾϵͳ��
	 * @return
	 * @throws Exception
	 */
	public  String sign(String merchantNo, String sign_str) {
		try {
//			KeyStore ks = KeyStore.getInstance("PKCS12");
			
			MerchantCertificates certs=certDAO.obtainMerchantCertsByMerchantNo(merchantNo);
			
			
			String password = certs.getStorePwd();
			PrivateKey priKey=KeyUtil.getPrivateKeyFromPFX(certs.getMerchantPrivateKey(), password);
			 X509Cert x509=CertUtil.getCertFromPfx(certs.getMerchantPrivateKey(), password);
			
			return Base64.getBASE64(signatureUtil.p7SignMessageDetach("SHA1withRSAEncryption", sign_str.getBytes(), priKey, x509, cfcaSession));
//			java.io.FileInputStream fis = new java.io.FileInputStream(certs.getMerchantPrivateKey());
//			ks.load(fis, password);
//			fis.close();
//			
//			Enumeration<String> set=ks.aliases();
//			String serverAliasName=null;
//			while(set.hasMoreElements()){
//				serverAliasName=set.nextElement();
//			}
//			
//			Key myKey = ks.getKey(serverAliasName, certs.getMerchantPwd().toCharArray());
//			PrivateKey myPrivateKey = (PrivateKey) myKey;
//
//			Signature mySign = Signature.getInstance("MD5withRSA");
//			mySign.initSign(myPrivateKey);
//			mySign.update(sign_str.getBytes());
//			byte[] byteSignedData = mySign.sign();
//			return Base64.getBASE64(byteSignedData);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return
	 */
	public  boolean checksign(String merchantNo, String oid_str,
			String signed_str) {
		try {
			
			byte[] signData=Base64.getBytesBASE64(signed_str);
//			MerchantCertificate certs=certDAO.obtainMerchantCertsByMerchantId(merchantNo);
//			
//			CertificateFactory cf = CertificateFactory.getInstance("X.509");
//			FileInputStream in = new FileInputStream(certs.getCertPath());
//			java.security.cert.Certificate c = cf.generateCertificate(in);
//			in.close();
//			X509Certificate t = (X509Certificate) c;
//			PublicKey pubKeyReceiver = t.getPublicKey();
//
//			Signature myVerifySign = Signature.getInstance("MD5withRSA");
//			myVerifySign.initVerify(pubKeyReceiver);
//			myVerifySign.update(oid_str.getBytes());
//			boolean verifySign = myVerifySign.verify(Base64
//					.getBytesBASE64(signed_str));

			
			 boolean isTrue=signatureUtil.p7VerifyMessageDetach(oid_str
	                    .getBytes("UTF8"), signData, cfcaSession);
			
			X509Cert userCert = signatureUtil.getSignerCert();
	            if(!X509CertValidator.verifyCertDate(userCert)){
	            	System.out.println(userCert.getNotBefore() + "---" + userCert.getNotAfter());
	            	isTrue=false;
	            }else{
	            	System.out.println(userCert.getNotBefore() + "---" + userCert.getNotAfter());
	            }
	            
	            
	        String trustCertPath = "E:/����/��ӯ/CFCA/����֤��/root.cer";
            X509CertValidator.updateTrustCertsMap(trustCertPath);
            if(X509CertValidator.validateCertSign(userCert)){
                System.out.println("");
                System.out.println(" 3 ֤��䷢����ȷ!");
                System.out.println("");                
            }else{
                System.out.println("");
                System.out.println(" 3 ֤��䷢�ߴ���!");
                System.out.println(""); 
            }
            
            String clrPath = "E:/����/��ӯ/CFCA/����֤��/root.crl";
            if(X509CertValidator.verifyCertByCRLOutLine(userCert, clrPath)){          	
                System.out.println(" 4 ֤��Ϊ��Ч֤�飬δ����!");
            }else{
                System.out.println(" 4 ��֤���ѵ���!"); 
            }            
    
			return isTrue;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) throws Exception {

		// FileInputStream in = new
		// FileInputStream("d:/merchant00001.keystore");
		// KeyStore ks = KeyStore.getInstance("JKS");// JKS: Java Key
		// StoreJKS�������ж�������
		// ks.load(in, "123456".toCharArray());
		// in.close();
		//        
		// // ��keystore�ж�ȡ֤���˽Կ
		// String alias = "merchant00001"; // ��¼�ı���
		// String pswd = "merchant00001"; // ��¼�ķ�������
		// java.security.cert.Certificate cert = ks.getCertificate(alias);
		// PublicKey publicKey = cert.getPublicKey();
		// PrivateKey privateKey = (PrivateKey) ks.getKey(alias,
		// pswd.toCharArray());
		//        
		// String sign= sign(new String(privateKey.getEncoded()),"12345678");
		//        
		// System.out.println(sign);
		// if(checksign(new String(publicKey.getEncoded()),"12345678",sign)){
		// System.out.println("true");
		// }else{
		// System.out.println("false");
		// }

		String str = "test1234325435";

		KeyStore ks = KeyStore.getInstance("PKCS12");
		char[] password = "123456".toCharArray();
		java.io.FileInputStream fis = new java.io.FileInputStream(
				"d:/cashier.p12");
		ks.load(fis, password);
		fis.close();

		// // 2.2 Creating an X509 Certificate of the Receiver
		// X509Certificate recvcert ;
		// MessageDigest md = MessageDigest.getInstance("MD5");
		// recvcert = (X509Certificate)ks.getCertificate("merchant00001");
		// // 2.3 Getting the Receivers public Key from the Certificate
		// PublicKey pubKeyReceiver = recvcert.getPublicKey();

		Key myKey = ks.getKey("cashier", "123456".toCharArray());
		PrivateKey myPrivateKey = (PrivateKey) myKey;

		Signature mySign = Signature.getInstance("MD5withRSA");
		mySign.initSign(myPrivateKey);
		mySign.update(str.getBytes());
		byte[] byteSignedData = mySign.sign();

		// ��Կ��ǩ
		// Signature myVerifySign = Signature.getInstance("MD5withRSA");
		// myVerifySign.initVerify(pubKeyReceiver);
		// myVerifySign.update(str.getBytes());
		//        
		// boolean verifySign = myVerifySign.verify(byteSignedData);
		// if (verifySign == false)
		// {
		// System.out.println(" Error in validating Signature ");
		// }
		//        
		// else
		// System.out.println(" Successfully validated Signature ");

		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		FileInputStream in = new FileInputStream("d:/cashier.crt");
		java.security.cert.Certificate c = cf.generateCertificate(in);
		in.close();
		X509Certificate t = (X509Certificate) c;
		PublicKey pubKeyReceiver = t.getPublicKey();

		Signature myVerifySign = Signature.getInstance("MD5withRSA");
		myVerifySign.initVerify(pubKeyReceiver);
		myVerifySign.update(str.getBytes());

		boolean verifySign = myVerifySign.verify(byteSignedData);
		if (verifySign == false) {
			System.out.println(" Error in validating Signature ");
		}

		else
			System.out.println(" Successfully validated Signature ");
	}
}
