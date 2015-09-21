/*
 * @Id: CryptUtil.java 22:16:40 2005-12-13
 * 
 * @author 
 * @version 1.0
 * ccpay02_www PROJECT
 */
package ebank.core.common.util;

import java.util.Random;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.chinabank.crypto.CryptoFactory;


/**
 * @author 
 * Description: º”√‹≤ﬂ¬‘
 * 
 */

public class CryptUtil {
	private static Logger log = Logger.getLogger(CryptUtil.class);
	private CryptUtil(){};
	private static CryptUtil cu;
	public static CryptUtil getInstance(){
		return cu==null?cu=new CryptUtil():cu;		
	}
	
	public static String randomcrypt(int[] digistal){
		Random random=new Random();
		int radom=random.nextInt(1000);
		String plain="";
		for(int i=0;i<digistal.length;i++){
			digistal[i]+=radom;	
			plain+=digistal[i]+"=";
		}
		
		plain+=radom;
		log.debug("plain="+plain);
		return Crypt.getInstance().encrypt(plain);
	}
	
	public static int[] decryptrandom(String encrypt){		
		String temp=Crypt.getInstance().decrypt(encrypt);
		log.debug(temp);
		if(!temp.matches("^((\\d+)=){1,}(\\d+)$")){
			log.debug("ill char in string");
			return null;
		}				
		String[] result=temp.split("=");
		int[] plain=new int[result.length-1];
		for(int i=0;i<plain.length;i++){
			plain[i]=Integer.parseInt(result[i])-Integer.parseInt(result[result.length-1]);
		}
		return plain;
	}
	
	public static String encrypt(String str){
		return Crypt.getInstance().encrypt(str);
	}
	public static String decrypt(String str){
		return Crypt.getInstance().decrypt(str);
	}
	
	public static String pci(String org,String sens){
		if(org==null){
			if(sens!=null&&sens.length()>10) return sens.substring(0,6)+"****"+sens.substring(sens.length()-4);
			else if(sens!=null) return sens.replace(sens.charAt(0),'*');
			return sens;
		}else{
			if(sens!=null&&sens.length()>=6) return org.replaceAll(sens, sens.substring(0,6)+"****"+sens.substring(sens.length()-4));
			else return "";
		}		    
	}
	private static String getKeyName(String key){
		return ResourceBundle.getBundle("CbkCrypto").getString(key);		
	}
	public static String crypt_pci(String sens){
		if(sens==null) return "";
		try {
			return CryptoFactory.getInstance("desede").getCrypto(getKeyName("deskey")).encryptData(sens);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	public static String decrypt_pci(String enc){
		if(enc==null) return "";
		try {
			return CryptoFactory.getInstance("desede").getCrypto(getKeyName("deskey")).decryptData(enc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String pci(String sens){
		return pci(null,sens);
	}
	
	public static void main(String[] args) {
		System.out.println(CryptUtil.crypt_pci("1234568956235658"));
		/*
		try{
		String info=CryptUtil.randomcrypt(new int[]{18,22,3,7});
		System.out.println("info="+info);
		int[] result=CryptUtil.decryptrandom(info);
		for (int i = 0; i < result.length; i++) {
			System.out.println("result="+result[i]);
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		*/
		
	}

}
