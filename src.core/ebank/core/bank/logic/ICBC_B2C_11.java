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
 * Description: ���������°�V1.0.0.11�ӿ�
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
	 * ��������ϵͳ����
	 */
	private static String ICBC_NAME = "ICBC_PERBANK_B2C" ;
	private static String ICBC_VERSION = "1.0.0.11" ;
	private static String ICBC_CURTYPE = "001" ;
	private static String ICBC_VERIFYJOINFLAG = "0" ;
	private static String ICBC_LANGUAGE = "ZH_CN" ;
	private static String ICBC_NOTIFYTYPE = "HS" ; //֪ͨ���ͣ�ȡֵ��HS�����ڽ�����ɺ�ʵʱ��֪ͨ��Ϣ��HTTPЭ��POST��ʽ���������͸��̻�
	private static String ICBC_RESULTTYPE = "0" ; //����������ͣ�ȡֵ��0��������֧���ɹ�����ʧ�ܣ����ж����̻����ͽ���֪ͨ��Ϣ
	private static String ICBC_GOODSTYPE = "1" ; //ȡֵ��0����������Ʒ��ȡֵ��1����ʵ����Ʒ��
	
	static {
		Security.addProvider(new InfosecProvider()) ;
	}
	
	/**
	 * �õ�������֤URL
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
				//tranStat: 1-�����׳ɹ��������㡱��2-������ʧ�ܡ���3-�����׿��ɡ�
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
	 * ������ϢУ��
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
			//��ǩ�����ݽ���BASE64����
			byte[] sign = cn.com.infosec.icbc.ReturnValue.base64dec(signMsg) ;
			//��Կ
			FileInputStream fis = new FileInputStream(this.publicCert) ;
			byte[] cert = new byte[fis.available()] ;
			fis.read(cert) ;
			fis.close() ;
			//��֤BASE64�����õ���ǩ����Ϣ;0��ʾ�ɹ��������ı�ʾʧ��
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
	 * ����tranData��XML�ַ���ԭ��
	 * @param order
	 * @return
	 */
	private String generateTranData(BankOrder order) {
		
		//���ݿͻ���������֧�����ͣ��ж��Ƿ�֧����ʽ.0��ʾ������ʹ�ý�ǿ�֧����1��ʾ������ʹ�����ÿ�֧����2��ʾ��ǿ������ÿ����ܶԶ�������֧��
		String tPaymentType="";
		if(order.getPayment_type()!=null&&!"".equals(order.getPayment_type())&&order.getPayment_type().equals("1")){
			 tPaymentType     = "0";//1��ũ�п�֧��  
		}else if(order.getPayment_type()!=null&&!"".equals(order.getPayment_type())&&order.getPayment_type().equals("2")){
			tPaymentType     = "1";//3�����ǿ�֧��  
		}else{
			tPaymentType     = "2";//3����ǿ������ǿ�֧��
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
	 * ����Ҫ���������ַ���,�������н����Է�Ϊ��
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
	 * ��tranData����BASE64����
	 * @param tranData
	 * @return
	 */
	private String encodeTranData(String tranData) {
		byte[] src = tranData.getBytes() ;
		byte[] encodeMsg = cn.com.infosec.icbc.ReturnValue.base64enc(src) ;
		return new String(encodeMsg) ;
	}
	
	/**
	 * ����֧���ύҳ�����֤��Ϣ
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
			//˽Կ���루�����ṩ��
			char[] keyPass = this.keyPassword.toCharArray() ;
			//�ύ1������ǩ���ӿڶ�����ǩ��(ǩ���顢ǩ���鳤�ȡ�˽Կ��˽Կ����)
			byte[] signature = cn.com.infosec.icbc.ReturnValue.sign(src, src.length, privateKey, keyPass) ;
			//�ύ2������BASE64����API�Բ�����ǩ�����ݱ���(ǩ����)
			encodedMsg = cn.com.infosec.icbc.ReturnValue.base64enc(signature) ;
		} catch (Exception ex) {
			log.info(ex) ;
		}
		return encodedMsg ;
	}
	
	/**
	 * �ö����Ʒ�ʽ��ȡ֤�鹫Կ�ļ��󣬽���BASE64�����������ַ���
	 * @return
	 */
	private String getCert() {
		byte[] EncCertID = null ;
		FileInputStream fis = null ;
		try {
			//��ȡ֤��
			fis = new FileInputStream(this.getFileCert()) ;
			byte[] cert = new byte[fis.available()] ;
			fis.read(cert) ;
			fis.close() ;
			//�ύ������BASE64����API�Բ�����ǩ�����ݱ���(ǩ����)
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
