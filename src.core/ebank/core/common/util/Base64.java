package ebank.core.common.util;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * <p>Title: test</p>
 * <p>Description: </P>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: www.chinabank.com.cn</p>
 * 
 * Created on 2005-4-11
 * @author zhe.c
 * @version 0.5
 */
public class Base64 {
	/**	
	 * @param src
	 * @return
	 */
	public static String encode(byte[] src) {
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(src);
	}
	
	/**	
	 * @param base64
	 * @return
	 */
	public static byte[] decode(String base64) {
		BASE64Decoder decoder = new BASE64Decoder();
        try {
            return decoder.decodeBuffer(base64);
        } catch (IOException io) {
        	throw new RuntimeException(io.getMessage(), io.getCause());
        }
	}
	
	/**	
	 * @param base64
	 * @return
	 */
	public static String decodeToString(String base64) 
	{
	    return new String(decode(base64));
	}
	
	public static String encodeString(String base64)
	{
	    return encode(base64.getBytes());
	}
}
