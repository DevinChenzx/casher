package ebank.core.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.anxin.ipg.util.MD5;

public class AnXinMd5 extends MD5 
{
	public AnXinMd5()
	{
		
	}
	
	public static String toStr(String str)
	{
		String sign="";
		try 
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			sign = buf.toString();
			System.out.println("result==: " + buf.toString());// 32Î»µÄ¼ÓÃÜ
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sign;
	}
}
