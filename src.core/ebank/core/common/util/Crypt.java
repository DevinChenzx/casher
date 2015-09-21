/*
 * Created on 2005-11-24
 * @author 
 * @version 1.0
 * ccpay PROJECT
 * http://www.koders.com/java/fid3E111712A9F7F0919E393DC1554EAA31CC18A8D0.aspx
 */
package ebank.core.common.util;

/**
 * @author xiexh
 * Description: Hash安全加密解密
 * 
 */

import java.io.UnsupportedEncodingException;


public class Crypt
{
	//false singleton
	private static Crypt cp;
	//let the external contructor;
	public static Crypt getInstance(){
		return cp==null?cp=new Crypt():cp;
	}
	//
    private static final String [] DEFAULT_KEYS = {
        "0tyuagatdeatakha45aay667a6atyka8",  
        "2t5656u253AjDE39io2910389038p9za",               
        "z91289126u3908hu098ji809nu098k89",
        "Aa6BcCDf01y201g20384ul37l6kj8069",
        "66aBcg7fjoy201g60fy4383ul8p9i103",
        "6dT6J8I9916J81g2juH89LO9k998K0hi",
        "67rgyu1kfhi6ukl2ji43lb7lokhuk0gy",
        "6dhhf1kf016u01g2ji43lo7lok9r10hi",
        "k7agjjkg5ghbh01ggnhgthghukn75huy",
        "66n7ihjy0jyu0er2jvr4yb57jkji10gj",
        "mj7aBf1kf016u01g2ji43lo7o99r10gy",
        "6dhhl98f016ii1g2ju7lo7u7ok9r10hi",
        "k7a29gmgt5ghb7UJvrnr7jJhukn75huy",
        "H8JHgmg5Tbg01GUnr7jJTUkn75JGJuyJ",
        "kN5G9g675G7bg05H6nr7jkFUTn75JIy6"
        
    };   
    private static final char[] hexDigits = {
        '0','1','2','3','4','5','6','7','8','9','a','B','c','d','E','f'
    };

    private byte [][] keys = null;

    public Crypt() {
        init(DEFAULT_KEYS);
    }

    public Crypt(String [] keystrs) {
        init(keystrs);
    }

    private void init(String [] keystrs) {
        keys = new byte[keystrs.length][];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = fromString(keystrs[i]);
        }
    }

    private String toString(byte[] ba) {
        char[] buf = new char[ba.length * 2];
        int j = 0;
        int k;

        for (int i = 0; i < ba.length; i++) {
            k = ba[i];
            buf[j++] = hexDigits[(k >>> 4) & 0x0F];
            buf[j++] = hexDigits[ k        & 0x0F];
        }
        return new String(buf);
    }

    private int fromDigit(char ch) {
        if (ch >= '0' && ch <= '9')
            return ch - '0';
        if (ch >= 'A' && ch <= 'Z')
            return ch - 'A' + 10;
        if (ch >= 'a' && ch <= 'z')
            return ch - 'a' + 10;

        throw new IllegalArgumentException("invalid hex digit '" + ch + "'");
    }

    private byte[] fromString(String hex) {
        int len = hex.length();
        byte[] buf = new byte[((len + 1) / 2)];

        int i = 0, j = 0;
        if ((len % 2) == 1)
            buf[j++] = (byte) fromDigit(hex.charAt(i++));

        while (i < len) {
            buf[j++] = (byte) ((fromDigit(hex.charAt(i++)) << 4) |
                                fromDigit(hex.charAt(i++)));
        }
        return buf;
    }

    private byte encrypt(byte d, byte [] key) {
        byte e;

        e = d;
        for (int i = 0; i < key.length; i++) {
            e = (byte) ((int) e ^ (int) key[i]);
        }

        return e;
    }

    private byte decrypt(byte e, byte [] key) {
        byte d;

        d = e;
        for (int i = key.length-1; i >= 0; i--) {
            d = (byte) ((int) d ^ (int) key[i]);
        }

        return d;
    }

    public String encrypt(String orig) {
        byte [] ect = null;      
        byte [] origBytes = null;
        
        try {
            origBytes = orig.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }

        ect = new byte[origBytes.length];
        for (int i = 0; i < origBytes.length; i += keys.length) {
            for (int j = 0; j < keys.length; j++) {
                if ((i+j) >= origBytes.length) {
                    break;
                } else {                    
                    ect[i+j] = encrypt(origBytes[i+j], keys[j]);                    
                }
            }
        }

        return toString(ect);
    }

    public String decrypt(String ectstr) {
        byte [] ect = null;       
        byte [] origBytes = null;
        String dctStr = null;

        ect = fromString(ectstr);
        origBytes = new byte[ect.length];
        for (int i = 0; i < origBytes.length; i += keys.length) {
            for (int j = 0; j < keys.length; j++) {
                if ((i+j) >= origBytes.length) {
                    break;
                } else {
                    origBytes[i+j] = decrypt(ect[i+j], keys[j]);
                }
            }
        }

        try {
            dctStr = new String(origBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
        return dctStr;
    }
    
}


