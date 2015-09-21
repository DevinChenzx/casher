
package beartool;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Encrypt {
	/**
	 * Used building output as Hex
	 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String md5(String text,String charset) {
		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					"System doesn't support MD5 algorithm.");
		}

		try {
			msgDigest.update(text.getBytes(charset));    
 
		} catch (UnsupportedEncodingException e) {

			throw new IllegalStateException(
					"System doesn't support your  EncodingException.");

		}

		byte[] bytes = msgDigest.digest();

		String md5Str = new String(encodeHex(bytes));

		return md5Str;
	}

	public static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

	  public static void main(String[] args){
		  
		  String str="body=1&discount=0.00&gmt_create=2015-02-25 14:39:54&gmt_logistics_modify=2015-02-25 15:04:53&gmt_payment=2015-02-25 14:42:00&is_success=T&is_total_fee_adjust=0&notify_id=77cc6533133a48dcb5a7a1c2f31e7ddd&notify_time=2015-02-25 14:42:00&notify_type=WAIT_TRIGGER&order_no=20150225143959&payment_type=2&price=0.01&quantity=1&seller_actions=SEND_GOODS&seller_email=59098759@qq.com&seller_id=100000000001984&title=1&total_fee=0.01&trade_no=101502250135919&trade_status=TRADE_FINISHED89baa2dg16gg74a3bg2af80469d1f594ge0a7c6a0bee455b629798ad954g690g";
	  
		  System.out.println(md5(str,"GBK"));
		  
	  }
}
