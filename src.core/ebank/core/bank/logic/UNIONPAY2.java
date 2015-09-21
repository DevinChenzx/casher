package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

/**
 * @author Description: 银联支付新版本 依赖属性文件:
 * 
 */
public class UNIONPAY2 extends BankExt implements BankService {

	static Logger log = Logger.getLogger(UNIONPAY2.class);
	
	@Override
	public String generateOrderID() throws ServiceException {		
		String RandOrderID = sequenceservice.getCode(); // yyDDD(6)+8位序列
		Random rd = new Random();
		String str = "";
		int length = this.prefixnum == null ? 0 : this.prefixnum.length();
		for (int i = 0; i < 4 - length; i++)
			str += rd.nextInt(10);
		return prefixnum + str + this.getCorpid().substring(10)
				+ RandOrderID.substring(RandOrderID.length() - 7);

	}

	public String sign(String MerId, String OrdId, String TransAmt,
			String CuryId, String TransDate, String TransType) {
		chinapay.PrivateKey key = new chinapay.PrivateKey();
		chinapay.SecureLink t;
		boolean flag;
		flag = key.buildKey(MerId, 0, this.getFileKey()); // 注意要用哪一个商户号签名就要用对应的key文件。
		if (flag == false) {
			System.out.println("build key error!");
			return "";
		}
		t = new chinapay.SecureLink(key);
		String CheckValue = t.signOrder(MerId, OrdId, TransAmt, CuryId,
				TransDate, TransType); // 获得对应商户的签名数据。
		System.out.println(CheckValue.length());
		return CheckValue;
	}

	/**
	 * 产生页面跳转的代码
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		// 产生订单号
		String amt = String.valueOf(Amount.getIntAmount(order.getAmount(), 2)); // String.valueOf(Double.valueOf(order.getAmount())*100);
		String s = "";
		for (int i = 0; i < 12 - amt.length(); i++) {
			s = s + "0";
		}
		String gateID = "";
		String amount = s + amt;
		if (order.getMp() != null && order.getMp().get("outChannel") != null) {
			gateID = String.valueOf(order.getMp().get("outChannel"));
		}
		
		String CuryId = "156";
		String TransDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String TransType = "0001";
		String Version = "20040916";
		String BgRetUrl = this.getRootIp()+ "/PayRec?idx="+this.getIdx()+"&NR=" + this.getIdx();
		String PageRetUrl = this.getRecurl();
		StringBuffer sf = new StringBuffer("");

		String orderID = order.getRandOrderID();
		sf.append("<form name=sendOrder  METHOD=\"POST\" action=\""
				+ this.getDesturl() + "\">");
		sf.append("<input type=\"hidden\" name=\"MerId\" value=\""
				+ this.getCorpid() + "\"/>");
		sf.append("<input type=\"hidden\" name=\"OrdId\" value=\"" + orderID
				+ "\"/>");
		sf.append("<input type=\"hidden\" name=\"TransAmt\" value=\""
				+ amount + "\"/>");
		sf.append("<input type=\"hidden\" name=\"CuryId\" value=\"" + CuryId
				+ "\"/>");
		sf.append("<input type=\"hidden\" name=\"TransDate\" value=\""
				+ TransDate + "\"/>");
		sf.append("<input type=\"hidden\" name=\"TransType\" value=\""
				+ TransType + "\"/>");
		sf.append("<input type=\"hidden\" name=\"Version\" value=\"" + Version
				+ "\"/>");
		sf.append("<input type=\"hidden\" name=\"BgRetUrl\" value=\""
				+ BgRetUrl + "\"/>");
		sf.append("<input type=\"hidden\" name=\"PageRetUrl\" value=\""
				+ PageRetUrl + "\"/>");
		sf.append("<input type=\"hidden\" name=\"GateId\"  value=\"" + gateID
				+ "\"/>");
		sf.append("<input type=\"hidden\" name=\"Priv1\" value=\"" + gateID
				+ "\"/>");
		sf.append("<input type=\"hidden\" name=\"ChkValue\" value=\""
				+ sign(this.getCorpid(), orderID, amount, CuryId, TransDate,
						TransType) + "\"/>");
		sf.append("</form>");
		return sf.toString();
	}

	public PayResult getPayResult(HashMap reqs) throws ServiceException {

		String MerId = (String) reqs.get("merid");
		String TransDate = (String) reqs.get("transdate");

		String OrdId = (String) reqs.get("orderno");
		String TransType = (String) reqs.get("transtype");
		String TransAmt = (String) reqs.get("amount");
		String amt = Amount.getFormatAmount(TransAmt, -2);
		String CuryId = (String) reqs.get("currencycode");
		String OrderStatus = (String) reqs.get("status");
		String ChkValue = (String) reqs.get("checkvalue");
		PayResult result = null;

		result = new PayResult(OrdId, this.getBankcode(), new BigDecimal(amt),
				"1001".equals(OrderStatus) ? 1 : -1);

		chinapay.PrivateKey key = new chinapay.PrivateKey();

		boolean flag;

		flag = key.buildKey("999999999999999", 0, this.getFileCert());
		if (flag == false) {
			log.debug("buildKey failure");
			return null;
		}
		chinapay.SecureLink t = new chinapay.SecureLink(key);
		boolean flag1=false;
		if(t!=null)
		   flag1 = t.verifyTransResponse(MerId, OrdId, TransAmt, CuryId,
				TransDate, TransType, OrderStatus, ChkValue);
		if (!flag1) {
			throw new ServiceException(EventCode.CRYPT_VALIADATESIGN); // 验证签名失败
		}
		return result;
	}
	private String fileKey;
	private String fileCert;

	public String getFileKey() {
		return fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	public String getFileCert() {
		return fileCert;
	}

	public void setFileCert(String fileCert) {
		this.fileCert = fileCert;
	}
	private String rootIp;

	public String getRootIp() {
		return rootIp;
	}

	public void setRootIp(String rootIp) {
		this.rootIp = rootIp;
	}
	
	
	

}
