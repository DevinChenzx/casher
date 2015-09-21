/*
 * @Id: CIB.java 11:39:10 2006-3-30
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.bank.logic;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;

import cn.tempus.pt.supplier.ca.CAUtil;
import cn.tempus.pt.supplier.exception.TrxException;
import cn.tempus.pt.supplier.send.Sender;
import cn.tempus.pt.supplier.util.XMLConvertor;
import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

/**
 * @author Description: 腾付通快捷
 * 
 */
public class TFTPay extends BankExt implements BankService {
	private Logger log = Logger.getLogger(this.getClass());
	private String keyPassword;
	private String payResultRec;
	private String code;
	private String chkMethod;
	private String merBusType;

	public void setCode(String code) {
		this.code = code;
	}

	public void setChkMethod(String chkMethod) {
		this.chkMethod = chkMethod;
	}

	public void setMerBusType(String merBusType) {
		this.merBusType = merBusType;
	}

	public void setPayResultRec(String payResultRec) {
		this.payResultRec = payResultRec;
	}

	// 12位订单号
	public String generateOrderID() throws ServiceException {
		String RandOrderID = sequenceservice.getCode(); // yymmdd(5)+8位序列
		Random rd = new Random();
		String str = "";
		for (int i = 0; i < 3 - this.prefixnum.length(); i++) {
			str += rd.nextInt(10);
		}
		rd = null;
		return prefixnum + RandOrderID.substring(5, 14) + str;
	}

	public static String md5(String str) {

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes("UTF-8"));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer();
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ebank.core.bank.service.BankService#sendOrderToBank(ebank.core.domain
	 * .BankOrder)
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException {

		Map m = new HashMap();
		m.put("code", "ORD001");
		m.put("merOrderId", order.getRandOrderID());
		m.put("returnUrl", this.payResultRec);
		m.put("notifyUrl", this.payResultRec);
		m.put("chkMethod", this.chkMethod);
		m.put("merBusType", this.merBusType);
		m.put("payType", this.paytype);
		m.put("merOrderAmt", order.getAmount());
		m.put("merOrderDesc", "");
		m.put("merOrderName", "");
		m.put("remark", "");
		m.put("custPhone", "");
		m.put("merOrderUrl", this.payResultRec);
		m.put("price", "");
		m.put("merShortName", "");
		m.put("merOrderCount", "");
		m.put("saleAcct", "");
		m.put("payMethod", "");
		m.put("merRemak", "");

		Map outMap = new HashMap();
		try {
			outMap = Sender.send(m).getMap();
		} catch (TrxException e) {
			e.printStackTrace();
			throw new ServiceException(EventCode.TRX_PROCESSFAILURE);
		}

		if (log.isDebugEnabled()) {
			log.debug("result=" + JSONObject.fromObject(outMap).toString());
		}

		if (outMap == null) {// 若创建创建订单失败

			throw new ServiceException(EventCode.TRX_PROCESSFAILURE);

		}
		
		if(!"00".equals(outMap.get("merOrderStatus"))){//不是成功状态
			
			throw new ServiceException(EventCode.TRX_PROCESSFAILURE);
		}

		StringBuffer sf2 = new StringBuffer();
		sf2.append("<form name=sendOrder method=\"get\" action=\""
				+ outMap.get("payUrl").toString() + "\">");

		sf2.append("<input type='hidden' value='"
				+ outMap.get("merNo").toString() + "' name='merNo' />");
		sf2.append("<input type='hidden' value='"
				+ outMap.get("merOrderId").toString() + "' name='orderNo' />");
		sf2.append("<input type='hidden' value='UnionPay' name='bank' />");
		sf2.append("<input type='hidden' value='1' name='jumpFlag' />");

		sf2.append("</form>");
		String result = sf2.toString();
		if (log.isDebugEnabled())
			log.debug("to TFT:" + sf2.toString());
		sf2 = null;

		return result;
	}

	public PayResult getPayResult(HashMap reqs) throws ServiceException {

		String data = reqs.get("xml").toString();
		String sign = reqs.get("sign").toString();
		String xml1 = new String(Base64.decode(data));
		Map outMap = XMLConvertor.xml2Map(xml1);
		Map m = null;
		boolean flag = false;
		try {

			flag = CAUtil.verify(Base64.decode(data.getBytes()), sign
					.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(EventCode.MPI_PAFAILURE);
		}

		if (!flag) {
			throw new ServiceException(EventCode.MPI_PAFAILURE);
		}
		String ord_no = String.valueOf(outMap.get("merOrderId"));
		String ord_amt = String.valueOf(outMap.get("merOrderAmt"));
		String pay_amt = String.valueOf(outMap.get("merOrderAmt"));
		String sno = String.valueOf(outMap.get("payOrdNo"));
		
		PayResult bean = null;
		if("00".equals(outMap.get("merOrderStatus").toString())){
			bean = new PayResult(ord_no, this.bankcode, new BigDecimal(pay_amt), 1);
		}else{
			bean = new PayResult(ord_no, this.bankcode, new BigDecimal(pay_amt), -1);
		}
		bean.setBankresult("1");
		bean.setBanktransseq(sno);
		reqs.put("RES", "success");
		return bean;
	}

	public static void main(String[] args) {

		TFTPay pay = new TFTPay();

		HashMap params = new HashMap();
		params
				.put(
						"sign",
						"PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48YnVzaW5lc3MgY29kZT0iMjcyMDAwIj48Z3JvdXA+PGRhdGEgbmFtZT0iTm90aWZ5VHlwIiB2YWx1ZT0iMCIvPjxkYXRhIG5hbWU9Im1lck9yZGVyQW10IiB2YWx1ZT0iMi4wMCIvPjxkYXRhIG5hbWU9IkFjdERhdCIgdmFsdWU9IjIwMTQwNTEzIi8+PGRhdGEgbmFtZT0ibWVyT3JkZXJTdGF0dXMiIHZhbHVlPSIwMCIvPjxkYXRhIG5hbWU9InBheU9yZE5vIiB2YWx1ZT0iMjAxNDA1MDYwMDA5MzYxOTcxIi8+PGRhdGEgbmFtZT0ibWVyTm8iIHZhbHVlPSIwMDAwNDMxMyIvPjxkYXRhIG5hbWU9Im1lck9yZGVySWQiIHZhbHVlPSI2MDMwMDEzMTg3ODQiLz48L2dyb3VwPjwvYnVzaW5lc3M+");
		// try {
		// PayResult bean=pay.getPayResult(params);

		System.out
				.println(new String(
						org.bouncycastle.util.encoders.Base64
								.decode("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48YnVzaW5lc3MgY29kZT0iMjcyMDAwIj48Z3JvdXA+PGRhdGEgbmFtZT0iTm90aWZ5VHlwIiB2YWx1ZT0iMCIvPjxkYXRhIG5hbWU9Im1lck9yZGVyQW10IiB2YWx1ZT0iMi4wMCIvPjxkYXRhIG5hbWU9IkFjdERhdCIgdmFsdWU9IjIwMTQwNTEzIi8+PGRhdGEgbmFtZT0ibWVyT3JkZXJTdGF0dXMiIHZhbHVlPSIwMCIvPjxkYXRhIG5hbWU9InBheU9yZE5vIiB2YWx1ZT0iMjAxNDA1MDYwMDA5MzYxOTcxIi8+PGRhdGEgbmFtZT0ibWVyTm8iIHZhbHVlPSIwMDAwNDMxMyIvPjxkYXRhIG5hbWU9Im1lck9yZGVySWQiIHZhbHVlPSI2MDMwMDEzMTg3ODQiLz48L2dyb3VwPjwvYnVzaW5lc3M+")));
		// } catch (ServiceException e) {
		// e.printStackTrace();
		// }
		
		Map ooutMap = XMLConvertor.xml2Map("<?xml version=\"1.0\" encoding=\"UTF-8\"?><business code=\"272000\"><group><data name=\"NotifyTyp\" value=\"0\"/><data name=\"merOrderAmt\" value=\"2.00\"/><data name=\"ActDat\" value=\"20140513\"/><data name=\"merOrderStatus\" value=\"00\"/><data name=\"payOrdNo\" value=\"201405060009361971\"/><data name=\"merNo\" value=\"00004313\"/><data name=\"merOrderId\" value=\"603001318784\"/></group></business>");
		System.out.println(111);

	}

	/**
	 * @param keyPassword
	 *            The keyPassword to set.
	 */
	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}

}
