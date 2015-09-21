package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.csii.payment.client.core.HsMerchantSignVerify;


import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class HSBK extends BankExt implements BankService {

	public String sendOrderToBank(BankOrder order) throws ServiceException {
		
			String plain = "transId=IPER~|~merchantId=" + order.getMerchantid()
			+ "~|~orderId="+order.getRandOrderID()+"~|~transAmt=" + order.getAmount()
			+ "~|~transDateTime="+order.getPostdate()+"~|~currencyType=001"
			+ "~|~merURL="+ this.getRecurl();
			
	
			String sign = HsMerchantSignVerify.merchantSignData_ABA(plain);
			StringBuffer sf = new StringBuffer();
			sf.append("<form name=\"sendOrder\" method=\"post\" action=\""
					+ this.getDesturl() + "\">");
			sf.append("<INPUT type=\"hidden\" name=\"Plain\" value='" + plain
					+ "' />");
			sf.append("<INPUT type=\"hidden\" name=\"Signature\" value='" + sign
					+ "' />");
			return sf.toString();
	}

	public PayResult getPayResult(HashMap reqs) throws ServiceException {	 
		
		String plain = String.valueOf(reqs.get("Plain"));
		String signature = String.valueOf(reqs.get("Signature"));
		PayResult bean = null;
		try {
			String[] result = plain.split("\\~|~");
			log.debug("HSCB plain:" + plain + " sign:" + signature);
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
			if (HsMerchantSignVerify.merchantVerifyPayGate_ABA(signature,plain)) {// ǩ����֤ͨ��
			    bean = new PayResult(String.valueOf(mp.get("orderId")),   //����������
							this.bankcode, new BigDecimal(String.valueOf(mp
									.get("transAmt"))), 1);              //����״̬([-1:ʧ�� 0 δȷ�� 1:�ɹ�]
				bean.setBankresult(String.valueOf(mp.get("RespCode")));  //���׹ؼ�����	
			} else {
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
			}
		} catch (Exception e) {
			HandleException.handle(e);
		}
		return bean;
	}

}
