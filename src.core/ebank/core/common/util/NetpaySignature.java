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
	* 根据原数据和指定的加密算法来生成用户的签名数据
	* @param algorithm：使用的加密算法，为SHA1withRSA
	* @param srcData：被签名数据，byte[]类型
	* @param path：商户私钥jks文件存放路径
	* @return 已签名数据
	* @throws Exception
	*/
	public String sign(String algorithm, byte[] srcData,String path)throws Exception{
	    try{
	           // 获取JKS 服务器私有证书的私钥，取得标准的JKS的 KeyStore实例
	           KeyStore store = KeyStore.getInstance("JKS");
	           // 读取jks文件，path为商户私钥jks文件存放路径
	           FileInputStream stream = new FileInputStream(path);
	           // jks文件密码，根据实际情况修改
	           String passwd = "reapal2012";
	           store.load(stream, passwd.toCharArray());
	           // 获取jks证书别名
	           Enumeration en = store.aliases();
	           String pName = null;
	           while (en.hasMoreElements()){
	        	   String n = (String)en.nextElement();
	        	   if (store.isKeyEntry(n)){
	        		   pName = n;
	        	   }
	           }
	           // 获取证书的私钥
	           PrivateKey key = (PrivateKey)store.getKey(pName, passwd.toCharArray());
	           // 进行签名服务
	           Signature signature=Signature.getInstance(algorithm);
	           signature.initSign(key);
	           signature.update(srcData);
	           byte[] signedData = signature.sign();
	           System.out.println("已签名" + new BASE64Encoder().encode(signedData));
	           // 返回签名结果
	           return new BASE64Encoder().encode(signedData);
	    }catch(Exception e){
	    	throw new Exception("signature.sign.error");
	    }
	}
	
	
	/**
	* 根据对签名数据使用签名者的公钥来解密后验证是否与原数据相同。从而确认用户签名正确
	* @param 使用的加密算法，需与加密时使用的算法一致
	* @param srcData 被签名数据，byte[]类型
	* @param signedData 使用该用户的私钥生成的已签名数据
	* @param path商户公钥证书cer文件存放路径
	* @return true或false，验证成功为true。
	* @throws Exception
	*/
	public boolean verify(String algorithm, byte[] srcData, byte[]signedData,String path)throws Exception{
		
		// 获取指定证书的公钥
		CertificateFactory certInfo=CertificateFactory.getInstance("x.509");
		X509Certificate cert = (X509Certificate) certInfo.generateCertificate(new FileInputStream(path));
		PublicKey publicKey = cert.getPublicKey();
		// 进行验证签名服务
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
