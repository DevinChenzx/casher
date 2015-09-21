/*
 * @Id: AsiaPay.java 下午02:17:23 2010-2-22
 * 
 * @author yaobx@chinabank.com.cn
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.asiapay.secure.PaydollarSecure;
import com.asiapay.secure.PaydollarSecureException;
import com.asiapay.secure.PaydollarSecureFactory;

import ebank.core.OrderService;
import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.core.model.domain.GwOrders;
import ebank.core.remote.DecisionService;

/**
 * @author yaobx@chinabank.com.cn
 * Description: AsiaPay
 * 
 */
public class AsiaPay extends BankExt implements BankService{
	private Logger logger = Logger.getLogger(this.getClass());	
	
	private String cardtype ;   // 08 AsiaPay
	private String payType ; 
	private String merchantid;
	
	private String lowIp ;
	private String upIp ;
	private String returl ;
	private String secureHashSecret ; //AsiaPay签名密钥
	
	private final String RISK_CALLBACK_QUEUE="QUEUE:TRANSCALLBACK";
	private DecisionService decisionService;
	private JmsTemplate jmsTemplate;
	private OrderService orderService;
	
	private String MPSMODE = "NIL" ;
	
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		PayResult payResult = null ;
		try {
			reqs.put("RES", "OK") ;
			
			// 判断IP地址
			String remoteIP = String.valueOf(reqs.get("RemoteIP")) ;
			logger.debug("RemoteIP:" + remoteIP) ;
			if(!this.isValid(remoteIP)) {
				throw new ServiceException("返回IP地址不安全![" + remoteIP + "]") ;
			}
			
			// -1:Error 0:Transaction succeeded 1:Transaction failure
			String successCode = String.valueOf(reqs.get("successcode")) ;  //Transaction Status
			String ref = String.valueOf(reqs.get("Ref")) ; //Merchant‘s Order Reference Number
			String payRef = String.valueOf(reqs.get("PayRef")) ; // PayDollar transaction reference
			String amount = String.valueOf(reqs.get("Amt")) ;
			String cur = this.switchCur(String.valueOf(reqs.get("Cur"))) ;
			String authId = String.valueOf(reqs.get("AuthId")) ;
			String eci = String.valueOf(reqs.get("eci")) ;
			String resmg = "" ;
			if(successCode.equals("0")) {
				resmg = "0:支付成功" ;
			} else if(successCode.equals("1")) {
				resmg = "1:支付失败" ;
			} else {
				resmg = "-1:支付异常" ;
			}
			String ord = String.valueOf(reqs.get("Ord")) ;
			
			if(verifySecureHash(reqs)) {			
				// 回调风险系统
				GwOrders gw10=orderService.findOrderByTrx(ref,null);
				if(gw10==null) throw new ServiceException(EventCode.ORDER_NOTFOUND);
				
				final String risk=getRisk(ref,
						new SimpleDateFormat("yyyyMMdd").format(gw10.getCreatedate()),
						successCode.equals("0")?"Y":"F",
								authId,
								new SimpleDateFormat("yyyyMMdd").format(gw10.getCreatedate()),
						"000000",
						"",
						eci,
						successCode,
						resmg,
						payRef,
						ord
						);
				sendMsg(risk);
				
				payResult = new PayResult(ref,this.bankcode, new BigDecimal(amount), "0".equals(successCode)?1:-1) ;
				
				payResult.setCurrency(cur) ;
				payResult.setBankresult(successCode) ;
				payResult.setBanktransseq(payRef) ;
				payResult.setAuthcode(authId) ;
			} else {
				logger.debug(EventCode.SIGN_VERIFY) ;
				throw new ServiceException(EventCode.SIGN_VERIFY);
			}
		} catch(Exception e) {
			HandleException.handle(e) ;
		}
		return payResult;
	}

	// Secure Hash验证
	private boolean verifySecureHash(HashMap reqs) {
		String secureHash = (String)reqs.get("secureHash") ;
		if(secureHash == null) return false ; 
		if(logger.isDebugEnabled()) logger.debug("secureHash:" + secureHash) ;
		String[] secureHashs = secureHash.split(",") ;
		
		String src = String.valueOf(reqs.get("src")) ;
		String prc = String.valueOf(reqs.get("prc")) ;
		String successcode = String.valueOf(reqs.get("successcode")) ;
		String ref = String.valueOf(reqs.get("Ref")) ;
		String payRef = String.valueOf(reqs.get("PayRef")) ;
		String cur = String.valueOf(reqs.get("Cur")) ;
		String amt = String.valueOf(reqs.get("Amt")) ;
		String payerAuth = String.valueOf(reqs.get("payerAuth")) ;
		return verifyPaymentDatafeed(src, prc, successcode, ref, payRef, cur, amt, payerAuth, secureHashs) ;
	}
	
	/**
	 * AsiaPay程序不支持配置多个Secure Hash，故修改AsiaPay签名程序
	 * @param src
	 * @param prc
	 * @param successCode
	 * @param ref
	 * @param payRef
	 * @param cur
	 * @param amt
	 * @param payerAuth
	 * @param secureHashs
	 * @return
	 */
	private boolean verifyPaymentDatafeed(String src,String prc, String successCode, String ref, String payRef,
			String cur,String amt, String payerAuth, String[] secureHashs) {
		PaydollarSecure paydollarSecure = PaydollarSecureFactory.getPaydollarSecure("SHA");
		String secureHashSecret = this.getSecureHashSecret() ;
		boolean verifyResult = false;

		for (int i = 0; secureHashs != null && i < secureHashs.length; i++) {

			verifyResult = paydollarSecure.verifyPaymentDatafeed(src, prc,
					successCode, ref, payRef, cur, amt, payerAuth,
					secureHashSecret, secureHashs[i]);

			if (verifyResult) {
				break;
			}
		}

		if(logger.isDebugEnabled()) logger.debug("verifyResult =" + verifyResult);
		return verifyResult;
	}
	
	
	
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		StringBuffer sb = new StringBuffer() ;
		
		String orderRef = order.getRandOrderID() ;
		String orderNo = order.getOrdernum() ;
		logger.info("order:" + orderRef) ;
		String amount = order.getAmount() ;
		String currCode = this.switchCur(order.getCurrency()) ;
		String lang = this.switchLang(order.getCct().getLanguage());
		String merchantId = this.getMerchantid() ;
		String cardNo = order.getCct().getPan() ;
		String pMethod = "" ;
		if(cardNo != null) {
			if(cardNo.charAt(0)=='4') pMethod = "VISA" ;
			else if(cardNo.charAt(0)=='5') pMethod = "Master" ;
		}
		String ep = order.getCct().getExpiry() ;
		String epYear = ep.substring(0, 4) ;
		String epMonth = ep.substring(5, 6) ;
		String securityCode = order.getCct().getCsc() ; 
		String cardHolder = order.getCct().getHolder() ;
		
		String info = CryptUtil.encrypt(orderNo+"="+orderRef);
		//String url = "https://localhost/PayResult.do?xids=" + info ; 
		String url = this.getReturl() + info ;
		
		String successUrl = url + "&successUrl=1" ;
		String failUrl = url + "&failUrl=1" ;
		String errorUrl = url + "&errorUrl=1" ;
		
		String payType = this.getPayType() ;
		
		String mpsMode = MPSMODE ;
		String secureHash = this.getSecureHash(merchantId, orderRef, currCode, amount, payType) ;
		if(secureHash == null) {
			return "<font color=red>商户端签名失败</font>" ;
		}
		
		sb.append("<body onload=\"javascript:document.payForm.submit();\"> ") ; 
		sb.append("<form name=\"payForm\" method=\"post\" action=\"").append(this.getDesturl()).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"merchantId\" value=\"").append(merchantId).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"amount\" value=\"").append(amount).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"orderRef\" value=\"").append(orderRef).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"currCode\" value=\"").append(currCode).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"pMethod\" value=\"").append(pMethod).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"cardNo\" value=\"").append(cardNo).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"securityCode\" value=\"").append(securityCode).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"cardHolder\" value=\"").append(cardHolder).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"epMonth\" value=\"").append(epMonth).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"epYear\" value=\"").append(epYear).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"payType\" value=\"").append(payType).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"successUrl\" value=\"").append(successUrl).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"failUrl\" value=\"").append(failUrl).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"errorUrl\" value=\"").append(errorUrl).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"lang\" value=\"").append(lang).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"mpsMode\" value=\"").append(mpsMode).append("\">") ;
		sb.append("<input type=\"hidden\" name=\"secureHash\" value=\"").append(secureHash).append("\">") ;
		sb.append("</form>") ;
		
		return sb.toString() ;
	}
	
	private String getSecureHash(String merchantId, String orderRef, String currCode, String amount, String payType) {
		String secureHash = null ;
		try {
			secureHash = generatePaymentSecureHash(merchantId, orderRef, currCode, amount, payType) ;
		} catch (PaydollarSecureException e) {
			e.printStackTrace();
			return null ;
		}
		return secureHash ;
	}

	/**
	 * AsiaPay程序不支持配置多个Secure Hash，故修改AsiaPay签名程序
	 * @param merchantId
	 * @param orderRef
	 * @param currCode
	 * @param amount
	 * @param paymentType
	 * @return
	 * @throws PaydollarSecureException
	 */
	private String generatePaymentSecureHash(String merchantId, String orderRef, String currCode, String amount,
			String paymentType) throws PaydollarSecureException {
		PaydollarSecure paydollarSecure = PaydollarSecureFactory.getPaydollarSecure("SHA");
		String secureHash = null;
		
		String secureHashSecret = this.getSecureHashSecret() ;
		if(secureHashSecret==null) 
			throw new NullPointerException("It can not load the Secure Hash Secret Data!") ;
			
		secureHash = paydollarSecure.generatePaymentSecureHash(merchantId,
				orderRef, currCode, amount, paymentType, secureHashSecret);
		
		return secureHash ;
	}
	
	private String switchCur(String cur) {
		Map curMap = new HashMap() ;
		curMap.put("HKD", "344") ;
		curMap.put("USD", "840") ;
		curMap.put("SGD", "702") ;
		curMap.put("CNY", "156") ;
		curMap.put("JPY", "392") ;
		curMap.put("TWD", "901") ;
		curMap.put("AUD", "036") ;
		curMap.put("EUR", "978") ;
		curMap.put("GBP", "826") ;
		curMap.put("CAD", "124") ;
		curMap.put("MOP", "446") ;
		
		curMap.put("344", "HKD") ;
		curMap.put("840", "USD") ;
		curMap.put("702", "SGD") ;
		curMap.put("156", "CNY") ;
		curMap.put("392", "JPY") ;
		curMap.put("901", "TWD") ;
		curMap.put("036", "AUD") ;
		curMap.put("978", "EUR") ;
		curMap.put("826", "GBP") ;
		curMap.put("124", "CAD") ;
		curMap.put("446", "MOP") ;
		return (String) curMap.get(cur) ;
	}
	
	private String switchLang(String language) {
		Map langMap = new HashMap() ;
		langMap.put("CN", "X") ;
		langMap.put("US", "E") ;
		//langMap.put("FR", "E") ;
		langMap.put("KR", "K") ;
		langMap.put("JP", "J") ;
		langMap.put("BIG5", "C") ;
		return (String)langMap.get(language) ;
	}
	
		
	private boolean isValid(String remoteIP) {
		String[] tmpIPs = remoteIP.split(",") ;
		String realIp = "" ;
		for(int i=0; i<tmpIPs.length; i++) {
			if(!tmpIPs[i].trim().equalsIgnoreCase("unknown")) {
				realIp = tmpIPs[i].trim() ;
				break ;
			}
		}
		
		if(realIp == null || "".equals(realIp)) {
			return false ;
		}
		
		String[] ips = realIp.split("\\.") ;
		
		String[] lIps = this.getLowIp().split("\\.") ;
		String[] uIps = this.getUpIp().split("\\.") ;
		
		logger.debug("IP ranges :" + this.getLowIp() + " - " + this.getUpIp()) ;
		
		if(ips[0].equals(lIps[0]) && ips[1].equals(lIps[1]) && 
				ips[2].equals(lIps[2]) && Integer.parseInt(ips[3])>=Integer.parseInt(lIps[3])
				&& Integer.parseInt(ips[3])<=Integer.parseInt(uIps[3])) {
			return true ;
		}
		return false ;
	}
	
	protected void sendMsg(final String risk){		
		try {
			logger.debug("同步回调风险...") ;
			if(decisionService.transcallback(risk)<0){//同步调用
				new Thread(){
					public void run(){
						try {
				    		jmsTemplate.send(RISK_CALLBACK_QUEUE,new MessageCreator(){
								public Message createMessage(Session session) throws JMSException{
				            		ObjectMessage Omessage=session.createObjectMessage();
				            		Omessage.setObject(risk);   	
							        return Omessage;
							        }
						     });	    		
						} catch (Exception e) {
							e.printStackTrace();	
										
						}
					}
				}.start();
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	protected String getRisk(String transnum,String seqdate,
			String state,String auth,String transdate,String time,
			String cvv,String eci,String rescode,
			String resmg,String billaccount,String desc2){
		String x="<?xml version='1.0' encoding='GBK'?>"+
		"<chinabank>"+
		"<transaction>"+
		"<result>"+
		"<transid></transid>"+
		"<transseq>"+transnum+"</transseq>"+
		"<seqdate>"+seqdate+"</seqdate>"+
		"<servicecode>98</servicecode>"+
		"<transstate>"+state+"</transstate>"+
		"<transauth>"+auth+"</transauth>"+
		"<transdate>"+transdate+"</transdate>"+
		"<transtime>"+time+"</transtime>"+
		"<cvv>"+cvv+"</cvv>"+
		"<eci>"+eci+"</eci>"+
		"<transrem></transrem>"+		
		"<transdesc2>"+desc2+"</transdesc2>"+
		"<rescode>"+rescode+"</rescode>"+
		"<rescodedesc>"+resmg+"</rescodedesc>"+
		"<billaccount>"+billaccount+"</billaccount>"+		
		"</result>"+
		"</transaction>"+
		"</chinabank>";
		logger.debug("risk:" + x) ;
		return x;
	}
	
	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	
	public String getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}
	
	public String getLowIp() {
		return lowIp;
	}

	public void setLowIp(String lowIp) {
		this.lowIp = lowIp;
	}

	public String getUpIp() {
		return upIp;
	}

	public void setUpIp(String upIp) {
		this.upIp = upIp;
	}
	
	public String getReturl() {
		return returl;
	}

	public void setReturl(String returl) {
		this.returl = returl;
	}
	
	public void setDecisionService(DecisionService decisionService) {
		this.decisionService = decisionService;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	
	public String getSecureHashSecret() {
		return secureHashSecret;
	}

	public void setSecureHashSecret(String secureHashSecret) {
		this.secureHashSecret = secureHashSecret;
	}
}

















