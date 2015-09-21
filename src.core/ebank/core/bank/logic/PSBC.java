package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import com.psbc.payment.client.SignatureService;
import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

/**
 * 邮储银行接口
 * @author zhwe2008
 *
 */
public class PSBC extends BankExt implements BankService {
	private Logger log = Logger.getLogger(this.getClass());

	public String sendOrderToBank(BankOrder order) throws ServiceException {

		String plain = "TermCode=001|TermSsn=" + order.getRandOrderID()
				+ "|OSttDate=|MercDtTm=" + order.getPostdate()
				+ "|Remark2=|Remark1=|TranAmt=" + order.getAmount()
				+ "|MercUrl=" + this.getRecurl() + "|TranAbbr=IPER|MercCode="
				+ this.getCorpid() + "|OAcqSsn=";
		String sign = SignatureService.sign(plain);// "7ecb8fa518c20c0d1375312e61cc52bc1397fdbeceaeb25a73d4d807ba7bc0053258445bae46e9fd98ac75ccec993e3625286fd6b1dfcdb466a68e6f00ef09ffab25ad639e9b1c1428373badc91aac18d2564beb2bb5ad792704077005082f804deade792dd0347a268a3c2de515a1946a8b8dc1fe3dfb08f957692ab06da07b";
		StringBuffer sf = new StringBuffer();
		sf.append("<form name=\"sendOrder\" method=\"post\" action=\""
				+ this.getDesturl() + "\">");
		sf.append("<INPUT type=\"hidden\" name=\"transName\" value='IPER' />");
		sf.append("<INPUT type=\"hidden\" name=\"Plain\" value='" + plain
				+ "' />");
		sf.append("<INPUT type=\"hidden\" name=\"Signature\" value='" + sign
				+ "' />");
		System.out.println(sf.toString());
		return sf.toString();

	}

	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		String plain = String.valueOf(reqs.get("Plain"));
		String signature = String.valueOf(reqs.get("Signature"));
		PayResult bean = null;
		try {
			String[] result = plain.split("\\|");
			log.debug("psbc plain:" + plain + " sign:" + signature);
			Map<String,Object> mp = new HashMap<String,Object>();
			for (int i = 0; i < result.length; i++) {
				String[] ss = result[i].split("\\=");
				if (ss != null) {
					if (ss.length > 1)
						mp.put(ss[0], ss[1]);
					else
						mp.put(ss[0], "");
				}
			}
			log.debug(mp);
			if (SignatureService.verify(plain, signature)) {// 签名验证通过
				if ("00000000".equals(String.valueOf(mp.get("RespCode")))) {
					bean = new PayResult(String.valueOf(mp.get("TermSsn")),
							this.bankcode, new BigDecimal(String.valueOf(mp
									.get("TranAmt"))), 1);
				} else {
					bean = new PayResult(String.valueOf(mp.get("TermSsn")),
							this.bankcode, new BigDecimal(String.valueOf(mp
									.get("TranAmt"))), -1);
				}
				bean.setBankresult(String.valueOf(mp.get("RespCode")));
				bean.setBanktransseq(String.valueOf(mp.get("AcqSsn"))); // 银行流水
				bean.setBankdate(String.valueOf(mp.get("SettDate"))); // 清算日期
			} else {
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
			}
		} catch (Exception e) {
			HandleException.handle(e);
		}
		return bean;
	}

}
