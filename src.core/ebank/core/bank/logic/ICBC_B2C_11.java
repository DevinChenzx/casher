/*
 * @Id: ICBC_B2C_11.java 2010-7-27
 * 
 * @author yaobx@chinabank.com.cn
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.Security;
import java.util.HashMap;

import org.apache.log4j.Logger;

import cn.com.infosec.jce.provider.InfosecProvider;


import ebank.core.STEngineService;
import ebank.core.bank.BankService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.common.util.XmlUtils;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;



/**
 * Description: 工商银行新版V1.0.0.11接口
 */
public class ICBC_B2C_11  extends BankExt implements BankService{
	private Logger log = Logger.getLogger(this.getClass()) ;
	
	private String merchantaccount ;
	private String fileKey ;
	private String fileCert ;
	private String publicCert ;
//	private String keyPassword ;
	private STEngineService engineService ;
	private String merReference ;

	/**
	 * 工商银行系统常量
	 */
	private static String ICBC_NAME = "ICBC_PERBANK_B2C" ;
	private static String ICBC_VERSION = "1.0.0.11" ;
	private static String ICBC_CURTYPE = "001" ;
	private static String ICBC_VERIFYJOINFLAG = "0" ;
	private static String ICBC_LANGUAGE = "ZH_CN" ;
	private static String ICBC_NOTIFYTYPE = "HS" ; //通知类型，取值“HS”：在交易完成后实时将通知信息以HTTP协议POST方式，主动发送给商户
	private static String ICBC_RESULTTYPE = "0" ; //结果发送类型，取值“0”：无论支付成功或者失败，银行都向商户发送交易通知信息
	private static String ICBC_GOODSTYPE = "1" ; //取值“0”：虚拟商品；取值“1”，实物商品。
	
	static {
		Security.addProvider(new InfosecProvider()) ;
	}
	
	/**
	 * 得到接收验证URL
	 * @return String
	 */
	public String getRcvURL(HashMap reqs) {
		String strOutput = "notfrombank";		
		try{		
			PayResult result = this.getPayResult(reqs);
			if(result != null && 1==result.getTrxsts()){
				strOutput = "CBMD5";
				engineService.post(result);	
			}			
		} catch (Exception e){
			e.printStackTrace();
		}
		String x=this.getRecurl()+(this.getRecurl().indexOf("?")>0?"&":"?")+"v_md5info="+strOutput;
		log.info("ICBC:"+x);
		return x.replaceAll("https", "http");
	}
	
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		PayResult bean = null ;
		try {
			String notifyData = String.valueOf(reqs.get("notifyData")) ;
			String srcMsg = new String(cn.com.infosec.icbc.ReturnValue.base64dec(notifyData.getBytes())) ;
			if(log.isDebugEnabled()) log.debug("notifyData: " + srcMsg) ;
			if(this.verify(reqs) == 0) {
				String amount = XmlUtils.getNode(this.decode(srcMsg), "amount") ;
				String orderNum = XmlUtils.getNode(this.decode(srcMsg), "orderid") ;
				String tranStat = XmlUtils.getNode(this.decode(srcMsg), "tranStat") ; 
				String notifyDate = XmlUtils.getNode(this.decode(srcMsg), "notifyDate") ;
				String curType = XmlUtils.getNode(this.decode(srcMsg), "curType") ;
				String bankTransSeq = XmlUtils.getNode(this.decode(srcMsg), "TranSerialNo") ;
				
				BigDecimal bamount = new BigDecimal(amount) ;
				amount = bamount.movePointLeft(2).toString() ;
				//tranStat: 1-“交易成功，已清算”；2-“交易失败”；3-“交易可疑”
				bean = new PayResult(orderNum,this.bankcode, new BigDecimal(amount), "1".equals(tranStat)?1:-1) ;
				bean.setBankresult("0") ;				
				if(notifyDate!=null&&!"".equals(notifyDate)){
					if(notifyDate.length()>8){
						notifyDate = notifyDate.substring(0, 8);
					}
				}
				bean.setBankdate(notifyDate) ;
				bean.setCurrency(ICBC_CURTYPE.equals(curType)?"CNY":curType) ;
				bean.setBankresult(tranStat) ;
				bean.setBanktransseq(bankTransSeq) ;
			} else {
				log.info("ICBCSign wrong");
				throw new ServiceException(EventCode.ICBC_SIGNVERIFY);
			}
		} catch (Exception e) {
			HandleException.handle(e) ;
		}
		return bean;
	}

	/**
	 * 返回信息校验
	 * @param reqs
	 * @return
	 */
	private int verify(HashMap reqs) {
		try {
			String notifyData = String.valueOf(reqs.get("notifyData")) ;
			String signMsg = String.valueOf(reqs.get("signMsg")) ;
			byte[] srcMsg = cn.com.infosec.icbc.ReturnValue.base64dec(notifyData.getBytes()) ;
			return verifyMsg(srcMsg, signMsg.getBytes()) ;
		} catch (Exception e) {
			e.printStackTrace() ;
		}
		return -1 ;
	}
	
	private int verifyMsg(byte[] strMsg, byte[] signMsg) {
		int iJg = 1 ;
		try {
			//对签名数据进行BASE64解码
			byte[] sign = cn.com.infosec.icbc.ReturnValue.base64dec(signMsg) ;
			//公钥
			FileInputStream fis = new FileInputStream(this.publicCert) ;
			byte[] cert = new byte[fis.available()] ;
			fis.read(cert) ;
			fis.close() ;
			//验证BASE64解码后得到的签名信息;0表示成功，其他的表示失败
			iJg = cn.com.infosec.icbc.ReturnValue.verifySign(strMsg, strMsg.length, cert, sign) ;
		} catch (Exception e) {
			log.info(e) ;
		}
		return iJg ;
	}
	
	private String decode(Object reqs) {
		try {
			if(reqs == null) return "" ;
			else return URLDecoder.decode(String.valueOf(reqs), "GB2312") ;
		} catch (Exception e) {
			e.printStackTrace() ;
			return "" ;
		}
	}
	
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		StringBuffer sb = new StringBuffer() ;
		sb.append("<form name=\"sendOrder\" method=\"post\" action=\"").append(this.getDesturl()).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"interfaceName\" value=\"").append(ICBC_NAME).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"interfaceVersion\" value=\"").append(ICBC_VERSION).append("\">") ;
		String tranData = this.generateTranData(order) ;
		log.info("tranData:"+tranData);
		sb.append("<input type=\"hidden\" name=\"tranData\" value=\"").append(this.encodeTranData(tranData)).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"merSignMsg\" value=\"").append(this.generateSignMsg(tranData)).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"merCert\" value=\"").append(this.getCert()).append("\">") ;
		sb.append("</form>") ;
		if(log.isDebugEnabled()) log.debug(sb.toString()) ;
		return sb.toString();
	}
	
	/**
	 * 产生tranData的XML字符串原文
	 * @param order
	 * @return
	 */
	private String generateTranData(BankOrder order) {
		
		//根据客户传过来的支付类型，判断是否支付方式.0表示仅允许使用借记卡支付，1表示仅允许使用信用卡支付，2表示借记卡和信用卡都能对订单进行支付
		String tPaymentType="";
		if(order.getPayment_type()!=null&&!"".equals(order.getPayment_type())&&order.getPayment_type().equals("1")){
			 tPaymentType     = "0";//1：农行卡支付  
		}else if(order.getPayment_type()!=null&&!"".equals(order.getPayment_type())&&order.getPayment_type().equals("2")){
			tPaymentType     = "1";//3：贷记卡支付  
		}else{
			tPaymentType     = "2";//3：借记卡，贷记卡支付
		}
		StringBuffer sb = new StringBuffer() ;
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\" standalone=\"no\"?>") ;
		sb.append("<B2CReq>") ;
		sb.append("<interfaceName>").append(ICBC_NAME).append("</interfaceName>") ;
		sb.append("<interfaceVersion>").append(ICBC_VERSION).append("</interfaceVersion>") ;
		sb.append("<orderInfo>") ;
		sb.append("<orderDate>").append(order.getPostdate()).append("</orderDate>") ;
		sb.append("<curType>").append(ICBC_CURTYPE).append("</curType>") ;
		sb.append("<merID>").append(this.getCorpid()).append("</merID>") ;
		sb.append("<subOrderInfoList>") ;
		sb.append("<subOrderInfo>") ;
		sb.append("<orderid>").append(order.getRandOrderID()).append("</orderid>") ;
		sb.append("<amount>").append(getAmount(order.getAmount())).append("</amount>") ;
		sb.append("<installmentTimes>1</installmentTimes>") ;
		sb.append("<merAcct>").append(this.merchantaccount).append("</merAcct>") ;
		sb.append("<goodsID>001</goodsID>") ;
		sb.append("<goodsName>B2C</goodsName>") ;
		sb.append("<goodsNum></goodsNum>") ;
		sb.append("<carriageAmt></carriageAmt>") ;
		sb.append("</subOrderInfo>") ;
		sb.append("</subOrderInfoList>") ;
		sb.append("</orderInfo>") ;
		sb.append("<custom>") ;
		sb.append("<verifyJoinFlag>").append(ICBC_VERIFYJOINFLAG).append("</verifyJoinFlag>") ;
		sb.append("<Language>").append(ICBC_LANGUAGE).append("</Language>") ;
		sb.append("</custom>") ;
		sb.append("<message>") ;
		sb.append("<creditType>").append(tPaymentType).append("</creditType>") ;
		sb.append("<notifyType>").append(ICBC_NOTIFYTYPE).append("</notifyType>") ;
		sb.append("<resultType>").append(ICBC_RESULTTYPE).append("</resultType>") ;
		sb.append("<merReference>").append(this.merReference).append("</merReference>") ;
//		sb.append("<merReference></merReference>") ;
		if(order.getCustip().startsWith("10.68")||order.getCustip().startsWith("10.72.32.34")){
			sb.append("<merCustomIp>1.202.236.162</merCustomIp>") ;
		}else{
			sb.append("<merCustomIp>").append(order.getCustip()).append("</merCustomIp>") ;
		}
		sb.append("<goodsType>").append(ICBC_GOODSTYPE).append("</goodsType>") ;
		sb.append("<merCustomID></merCustomID>") ;
		sb.append("<merCustomPhone></merCustomPhone>") ;
		sb.append("<goodsAddress></goodsAddress>") ;
		sb.append("<merOrderRemark>").append("B2C").append("</merOrderRemark>") ;
		sb.append("<merHint>").append("["+order.getOrdernum()+"]").append("</merHint>") ;
		sb.append("<remark1></remark1>") ;
		sb.append("<remark2></remark2>") ;
		sb.append("<merURL>").append(this.recurl).append("</merURL>") ;
		sb.append("<merVAR>").append(this.idx).append("</merVAR>") ;
		sb.append("</message>") ;
		sb.append("</B2CReq>") ;
		if(log.isDebugEnabled()) log.debug("tranData XML : " + sb.toString()) ;
		return sb.toString() ;
	}
	
	/**
	 * 按照要求产生金额字符串,工商银行结算以分为主
	 * @param amount
	 * @return
	 */
	private String getAmount(String amount) {
		BigDecimal big = new BigDecimal(amount) ;
		BigDecimal big2=big.multiply(new BigDecimal("100"));
		int v_amount = big2.intValue();
		big=null;
		big2=null;
		return String.valueOf(v_amount);
	}
	
	/**
	 * 将tranData明文BASE64编码
	 * @param tranData
	 * @return
	 */
	private String encodeTranData(String tranData) {
		byte[] src = tranData.getBytes() ;
		byte[] encodeMsg = cn.com.infosec.icbc.ReturnValue.base64enc(src) ;
		return new String(encodeMsg) ;
	}
	
	/**
	 * 产生支付提交页面的验证信息
	 * @param tranData
	 * @return
	 */
	private String generateSignMsg(String tranData) {
		if(log.isDebugEnabled()) log.debug("icbc src = "+tranData);       
		return new String(setEncMsg(tranData));
	}
	
	private byte[] setEncMsg(String strMsg) {
		byte[] src = strMsg.getBytes() ;
		byte[] encodedMsg = null ;
		try {			
			FileInputStream fis = new FileInputStream(this.fileKey) ;
			byte[] privateKey = new byte[fis.available()] ;
			fis.read(privateKey) ;
			fis.close() ;
			//私钥密码（银行提供）
			char[] keyPass = this.keyPassword.toCharArray() ;
			//提交1：调用签名接口对数据签名(签名组、签名组长度、私钥、私钥密码)
			byte[] signature = cn.com.infosec.icbc.ReturnValue.sign(src, src.length, privateKey, keyPass) ;
			//提交2：调用BASE64编码API对产生的签名数据编码(签名组)
			encodedMsg = cn.com.infosec.icbc.ReturnValue.base64enc(signature) ;
		} catch (Exception ex) {
			log.info(ex) ;
		}
		return encodedMsg ;
	}
	
	/**
	 * 用二进制方式读取证书公钥文件后，进行BASE64编码后产生的字符串
	 * @return
	 */
	private String getCert() {
		byte[] EncCertID = null ;
		FileInputStream fis = null ;
		try {
			//读取证书
			fis = new FileInputStream(this.getFileCert()) ;
			byte[] cert = new byte[fis.available()] ;
			fis.read(cert) ;
			fis.close() ;
			//提交：调用BASE64编码API对产生的签名数据编码(签名组)
			EncCertID = cn.com.infosec.icbc.ReturnValue.base64enc(cert) ;
		} catch (Exception ex) {			
			ex.printStackTrace() ;
		}
		return new String(EncCertID) ;
	}
	
	private String getURL(String referer) {
		String host = "" ;
		try {
			if(!"".equals(referer)){
				URL url = new URL(referer) ;
				host = url.getHost() ;
			}			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return host ;
		}
		return host ;
	}
	
	public String getMerchantaccount() {
		return merchantaccount;
	}

	public void setMerchantaccount(String merchantaccount) {
		this.merchantaccount = merchantaccount;
	}

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

	public String getPublicCert() {
		return publicCert;
	}

	public void setPublicCert(String publicCert) {
		this.publicCert = publicCert;
	}

//	public String getKeyPassword() {
//		return keyPassword;
//	}

//	public void setKeyPassword(String keyPassword) {
//		this.keyPassword = keyPassword;
//	}

	public STEngineService getEngineService() {
		return engineService;
	}

	public void setEngineService(STEngineService engineService) {
		this.engineService = engineService;
	}
	
	public String getMerReference() {
		return merReference;
	}

	public void setMerReference(String merReference) {
		int index = merReference.indexOf(":") ;
		this.merReference = merReference.substring(index+3, merReference.length());
	}
}
