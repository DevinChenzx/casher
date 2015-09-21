package ebank.test;

import ebank.core.bank.BankService;
import ebank.core.domain.BankOrder;
import ebank.core.remote.JniProxyService;

public class COMM_Test extends BaseTest{
	public void test(){
		try {/*
			JniProxyService pm=(JniProxyService)factory.getBean("jniProxyservice");
			String x=pm.getCrypt("xiexk", "BOCM", null);
			System.out.println(x);
			System.out.println(pm.decrypt(x, "BOCM", null));
			*/
			BankService bf=(BankService)factory.getBean("SID_308");
			System.out.println(bf.getBankcode());
			System.out.println(bf.getCorpid());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
