package ebank.core.common;

import java.util.HashMap;
import java.util.Map;
/**
 * 银通支付
 * @author qiuqiu
 *
 */
public class BankInfo {

	public static final Map<String, String> bankInfo = new HashMap<String, String>();
	static {
		bankInfo.put("ABC", "01030000");
		bankInfo.put("ICBC", "01020000");
		bankInfo.put("BOC", "01040000");
		bankInfo.put("CCB", "01050000");
		bankInfo.put("BOCM", "03010000");
		bankInfo.put("PSBC", "01000000");
		bankInfo.put("CITIC", "03020000");
		bankInfo.put("CEB", "03030000");
		bankInfo.put("HXB", "03040000");
		bankInfo.put("CMBC", "03050000");
		bankInfo.put("GDB", "03060000");
		bankInfo.put("CMB", "03080000");
		bankInfo.put("SDB", "03070000");
		bankInfo.put("CIB", "03090000");
		bankInfo.put("SPDB", "03100000");
		bankInfo.put("BOB", "04031000");
		bankInfo.put("HSCB", "04403600");
		bankInfo.put("JSBC", "05083000");
		bankInfo.put("JHCCB", "04263380");
		bankInfo.put("NJB", "04243010");
		bankInfo.put("NBCB", "04083320");
		bankInfo.put("SHB", "04012900");
		bankInfo.put("WZB", "04123330");
	}
}