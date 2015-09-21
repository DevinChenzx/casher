package ebank.core.common.util;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import sun.misc.BASE64Encoder;

public class NetpaySignature{

	/**
	* ����ԭ���ݺ�ָ���ļ����㷨�������û���ǩ������
	* @param algorithm��ʹ�õļ����㷨��ΪSHA1withRSA
	* @param srcData����ǩ�����ݣ�byte[]����
	* @param path���̻�˽Կjks�ļ����·��
	* @return ��ǩ������
	* @throws Exception
	*/
	public String sign(String algorithm, byte[] srcData,String path)throws Exception{
	    try{
	           // ��ȡJKS ������˽��֤���˽Կ��ȡ�ñ�׼��JKS�� KeyStoreʵ��
	           KeyStore store = KeyStore.getInstance("JKS");
	           // ��ȡjks�ļ���pathΪ�̻�˽Կjks�ļ����·��
	           FileInputStream stream = new FileInputStream(path);
	           // jks�ļ����룬����ʵ������޸�
	           String passwd = "reapal2012";
	           store.load(stream, passwd.toCharArray());
	           // ��ȡjks֤�����
	           Enumeration en = store.aliases();
	           String pName = null;
	           while (en.hasMoreElements()){
	        	   String n = (String)en.nextElement();
	        	   if (store.isKeyEntry(n)){
	        		   pName = n;
	        	   }
	           }
	           // ��ȡ֤���˽Կ
	           PrivateKey key = (PrivateKey)store.getKey(pName, passwd.toCharArray());
	           // ����ǩ������
	           Signature signature=Signature.getInstance(algorithm);
	           signature.initSign(key);
	           signature.update(srcData);
	           byte[] signedData = signature.sign();
	           System.out.println("��ǩ��" + new BASE64Encoder().encode(signedData));
	           // ����ǩ�����
	           return new BASE64Encoder().encode(signedData);
	    }catch(Exception e){
	    	throw new Exception("signature.sign.error");
	    }
	}
	
	
	/**
	* ���ݶ�ǩ������ʹ��ǩ���ߵĹ�Կ�����ܺ���֤�Ƿ���ԭ������ͬ���Ӷ�ȷ���û�ǩ����ȷ
	* @param ʹ�õļ����㷨���������ʱʹ�õ��㷨һ��
	* @param srcData ��ǩ�����ݣ�byte[]����
	* @param signedData ʹ�ø��û���˽Կ���ɵ���ǩ������
	* @param path�̻���Կ֤��cer�ļ����·��
	* @return true��false����֤�ɹ�Ϊtrue��
	* @throws Exception
	*/
	public boolean verify(String algorithm, byte[] srcData, byte[]signedData,String path)throws Exception{
		
		// ��ȡָ��֤��Ĺ�Կ
		CertificateFactory certInfo=CertificateFactory.getInstance("x.509");
		X509Certificate cert = (X509Certificate) certInfo.generateCertificate(new FileInputStream(path));
		PublicKey publicKey = cert.getPublicKey();
		// ������֤ǩ������
		try{
			Signature sign3=Signature.getInstance(algorithm);
			sign3.initVerify(publicKey);
			sign3.update(srcData);
			return sign3.verify(signedData);
		}catch(Exception e){
			throw new Exception("signature.verify.error");
		}
		}
}
