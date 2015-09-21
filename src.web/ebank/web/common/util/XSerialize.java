/*
 * @Id: XSerialize.java 10:54:59 2006-8-31
 * 
 * @author xiexh@chinabank.com.cn
 * @version 1.0
 * payment_web_v2.0 PROJECT
 */
package ebank.web.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author xiexh
 * Description: 对象序列化
 * 
 */
public class XSerialize {
	/**
	 * 对象序列化串
	 * @param obj
	 * @param keyin
	 * @return
	 */
	public static String serialize(Object obj,Key keyin){
		try{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream os=new ObjectOutputStream(bos);
			os.writeObject(obj);
			os.flush();
			os.close();
			byte[] original = bos.toByteArray();
			
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE,keyin);
			byte[] raw=cipher.doFinal(original);
			
			String base64 = new BASE64Encoder().encode(raw);			
			base64=base64.replaceAll(System.getProperty("line.separator"), "  ");			
			return base64;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	/**
	 * @param base
	 * @param in
	 * @return
	 */
	public static Object deserialize(String base,Key in){
		String v=base.replaceAll("  ", System.getProperty("line.separator"));		
		v=v.replaceAll(" ", "+");	
		
		byte[] original=null;
		Object vo=null;
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE,in);
			original=cipher.doFinal(new BASE64Decoder().decodeBuffer(v));
			ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(original));		
			vo= oin.readObject();			
			return vo;
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			e1.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return vo;
	}
	/**
	 * 得到用来加密的Key
	 * @param in
	 * @return
	 */
	public static Key getKey(InputStream in){
		/*
		try{
			ObjectInputStream oin=new ObjectInputStream(in);
			return (Key) oin.readObject();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		*/
		try {
			Security.insertProviderAt(new com.sun.crypto.provider.SunJCE(), 1);
	        KeyGenerator generator = KeyGenerator.getInstance("DES");
	        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );  
	        secureRandom.setSeed("default-persistence2011".getBytes());
	        generator.init(secureRandom);//new SecureRandom("".getBytes()));
	        Key key = generator.generateKey();
	        return key;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		

	}	
}
