package ebank.test;

import CCBSign.RSASig;
import ebank.core.bank.BankService;
import ebank.core.domain.BankOrder;

public class CCB_Test extends BaseTest{
	public void test(){
		//²âÊÔCCB.java µÄsendOrderToBank·½·¨
		try {
			BankService pm=(BankService)factory.getBean("CCB_TJ");
			BankOrder order=new BankOrder();
			System.out.println(pm.sendOrderToBank(order));
			
			String plain="POSID=926651713&BRANCHID=120000000&ORDERID=711050400001336336&PAYMENT=0.01&CURCODE=01&REMARK1=V20&REMARK2=&SUCCESS=Y";
			String SIGN="73d152703214d09970e359ea9da892177abdfb6f30459cce5999ab430dff2bc9ebcf1397237dc3c81cb02f12e8bfd9be60ac9db479703711e18fa03d48866258fa1bf0d71b5dfd7344c33a9f77b2eba5f2c2d06d90ac0e2be3043743d6838236e18b3de8b835926968507112c00bdb48b30e48be8926f35a029c9dae47107c3d";
			String pubkey="30819d300d06092a864886f70d010101050003818b0030818702818100b4374b8e7539b0334eb4604339a0497a5d2d4996962db5afce82e2fd1fbd30ccfc05b21d862767cad52c9dc21f3dd50e0871d1ca6cc1c2847aa6bf0d320f06ed55c2382a8e8a56578099ff32d8f9233e121ff3f6931f1953b36e6da7af7343fa1b62ca67e8a98dbce3a5a276a24c90a0c9f356483f3b607946a111fc4391c69d020111";
			RSASig test1 = new RSASig();
			test1.setPublicKey(pubkey);				
			boolean verifyresult = test1.verifySigature(SIGN,plain);
			System.out.println(verifyresult);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
