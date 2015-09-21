package ebank.core.bank.b2b;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.csii.payment.client.core.HsMerchantSignVerify;


import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class HSBK extends BankExt implements BankService {

	Logger log = Logger.getLogger(this.getClass());
	
	//10位（1+8(序列)+1位随机
	public String generateOrderID() throws ServiceException{
		String str="";
		Random rd=new Random();
		for(int i=0;i<2-this.prefixnum.length();i++) str+=rd.nextInt(12);	
		rd=null;
		return prefixnum+sequenceservice.getCode().substring(6,14);
	}
	
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		
			String plain = "transId=EPER~|~merchantId=" + this.corpid
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
			sf.append("</form>");
			log.info("HSBK-B2B info :"+sf.toString());
			return sf.toString();
	}

	@SuppressWarnings("rawtypes")
	public PayResult getPayResult(HashMap reqs) throws ServiceException {	 
		
		String plain = String.valueOf(reqs.get("Plain"));
		String signature = String.valueOf(reqs.get("Signature"));
		PayResult bean = null;
		try {
			String[] result = plain.split("\\~|~");
			log.info("HSCB_B2B plain:" + plain + " sign:" + signature);
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
			log.info(mp);
			if (HsMerchantSignVerify.merchantVerifyPayGate_ABA(signature,plain)) {// 签名验证通过
			    bean = new PayResult(String.valueOf(mp.get("orderId")),   //网银订单号
							this.bankcode, new BigDecimal(String.valueOf(mp
									.get("transAmt"))), 1);              //交易状态([-1:失败 0 未确认 1:成功]
				bean.setBankresult(String.valueOf(mp.get("RespCode")));  //交易关键项结果	
			} else {
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
			}
		} catch (Exception e) {
			HandleException.handle(e);
		}
		return bean;
	}

}
