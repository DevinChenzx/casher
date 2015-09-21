import ebank.core.bank.logic.BankExt;
import ebank.core.model.domain.GwOrders;


public class TestBankExt {
	public static void main(String[] args) {
		BankExt tx=new BankExt();
		tx.setParamPath("D:\\workspace\\payment\\keys.testing\\citic\\CITIC313.json");
		//tx.setParamJson("{\"corpid\":\"123\",\"desturl\":\"http://xxx\",\"enabled\":false}");
		tx.config();
		System.out.println(tx.isEnabled());
		System.out.println(tx.getDesturl());
		GwOrders order=new GwOrders();
		order.setAmount(999999990);
		System.out.println(Integer.MAX_VALUE);
		System.out.println(order.getAmount());
	}

}
