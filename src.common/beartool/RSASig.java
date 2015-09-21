package beartool;


import java.security.*;

import netpay.merchant.crypto.ABAProvider;
//import au.net.aba.crypto.provider.*;
import netpay.merchant.crypto.RSAPubKey;

public class RSASig{
	private String priKey;
	private String pubKey;
	
	public boolean generateKeys(){

		PublicKey		keyPub;
		PrivateKey		keyPri;
		SecureRandom		rand;

		Security.addProvider(new ABAProvider());

		rand = new SecureRandom();
		
		rand.setSeed(System.currentTimeMillis());

		try
		{
			KeyPairGenerator	fact;
			KeyPair			keyPair;

			//fact = KeyPairGenerator.getInstance("RSA", "ABA");
      fact = KeyPairGenerator.getInstance("RSA");

			fact.initialize(1024, rand);

			keyPair = fact.generateKeyPair();

			keyPub = keyPair.getPublic();

			keyPri = keyPair.getPrivate();

			pubKey = bytesToHexStr(keyPub.getEncoded());			

			priKey = bytesToHexStr(keyPri.getEncoded());			
			
		}
		catch (Exception e)
		{
			System.err.println(e);
			e.printStackTrace();
			e.printStackTrace(System.err);
			System.exit(1);
			return false;
		}
		return true;
	}

	
	public String getPublicKey(){
		return pubKey;
	}
	
	public String getPrivateKey(){
		return priKey;
	}
	
	public void setPublicKey(String pkey){
		pubKey = pkey;
	}
	
	public void setPrivateKey(String pkey){
		priKey = pkey;
	}	
	public boolean verifySigature(String sign,String src){
		try{
		    Security.addProvider(new ABAProvider());
		    //Signature sigEng = Signature.getInstance("MD5withRSA","ABA");
		    Signature sigEng = Signature.getInstance("MD5withRSA");
            //  Signature sigEng = Signature.getInstance("MD5withRSA","RSA");
            //  Signature sigEng = Signature.getInstance("MD5withRSA");

			byte[] pubbyte = hexStrToBytes(pubKey.trim());		
			
			sigEng.initVerify(new RSAPubKey(pubbyte));
			
			sigEng.update(src.getBytes("GBK")); //src chinese
	
			byte[] sign1 = hexStrToBytes(sign);
			if (sigEng.verify(sign1))
			{
				return true;
			}
			else
			{
				return false;
			}
	
		}catch(Exception e){
			System.err.println(e);
			e.printStackTrace(System.err);
			System.exit(1);
			return false;
		}
	}

	/**
	 * Transform the specified byte into a Hex String form.
	 */
	public static final String bytesToHexStr(
		byte[] bcd)
	{
		StringBuffer s = new StringBuffer(bcd.length * 2);

		for (int i = 0; i < bcd.length; i++)
		{
			s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
			s.append(bcdLookup[bcd[i] & 0x0f]);
		}

		return s.toString();
	}


	/**
	 * Transform the specified Hex String into a byte array.
	 */
	public static final byte[] hexStrToBytes(
		String	s)
	{
		byte[]	bytes;

		bytes = new byte[s.length() / 2];

		for (int i = 0; i < bytes.length; i++)
		{
			bytes[i] = (byte)Integer.parseInt(
					s.substring(2 * i, 2 * i + 2), 16);
		}

		return bytes;
	}

	private static final char[] bcdLookup =
	{
		'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'
	};
	
	
	public static void main(String[] args)
	{
	        RSASig test = new RSASig();
	        test.setPublicKey("30819c300d06092a864886f70d010101050003818a003081860281807b19ab02143341bbe10a387097701b7f7c7325f355bda51f537b01c474cf0e6fdc085f8dfa56e1dfe619b3b92432a07f98a25ca68948f1ff1e289897035a0442c33f4e753cc48f49e47b6ab6583b9912147ec3448eff3d72e581f1463d767081a391313be3ec3b63c6f3491fbf760a16e7376d271cdf90fcc8ab3713aeaeab3d020111");
	        boolean v = test.verifySigature("1fa9fed65958da3cf130ba88794ed48e723d81259d76f26ba058e5d54ab790b6f8ce3573af352924e2c5c7b21174f3cfb7b3b13732c72cc4491126630aba6e73bf41589f839a808b3478fbf701e610da115dd26318290c67f271d44ec045f70b05340f5b8884327101d37fa359508766f0d9a0ff3ae10580b654ef7bc267f034","POSID=000000000&BRANCHID=110000000&ORDERID=19991101234&PAYMENT=500.00&CURCODE=01&SUCCESS=Y");
	        System.out.println(v);
	}
}

