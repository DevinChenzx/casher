package ebank.core.bank.b2b;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import ebank.core.OrderService;
import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.bank.logic.CiticV6_util;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.IOUtils;
import ebank.core.common.util.XmlUtils;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.core.model.domain.GwTrxs;

public class CITICV6 extends BankExt implements BankService {

	private Logger log = Logger.getLogger(this.getClass());
	private String payeeaccno; 
	private CiticV6_util util;
	private String trustedCrt;
	private String signercrt;
	private String signerkey;
	private String signerpwd;
	private OrderService orderService;
	
	public String generateRandom() throws ServiceException{
		String str="";
		Random rd=new Random();
		for(int i=0;i<2-this.prefixnum.length();i++) str+=rd.nextInt(10);			
		rd=null;
		String aa= prefixnum+sequenceservice.getCode().substring(6,14);
		return prefixnum+sequenceservice.getCode().substring(6,14)+aa.substring(0, 5);
	}

	public String sendOrderToBank(BankOrder order) throws ServiceException {
		StringBuffer sf=new StringBuffer("");		
		String orderInfo = packageOrderInfo(order);
		String signData = null;
		signData = generateSign(orderInfo);
		sf.append("<form name=\"sendOrder\" method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" name=\"SIGNREQMSG\" value=\""+signData+"\" >");		
		sf.append("</form>");		
		log.info("orderInfo:"+sf.toString());
		return sf.toString();
	}

	@SuppressWarnings("rawtypes")
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		PayResult bean = null; 
		String signedMessage = String.valueOf(reqs.get("SIGNRSPMSG"));
		log.info("SIGNRESMSG:"+signedMessage);
		String plain="";
		String trxnum=String.valueOf(reqs.get("trxnum"));
		if(trxnum!=null && !"null".equals(trxnum) && !"".equals(trxnum)){
			GwTrxs gw20=orderService.findTrxByTrxnum(String.valueOf(reqs.get("trxnum")), this.bankcode);
			if(gw20!=null&&"1".equals(gw20.getTrxsts()))
			{
				return new PayResult(trxnum,
									this.bankcode,
									new BigDecimal(Amount.getFormatAmount(String.valueOf(gw20.getAmount()),-2)),1);
			}else{
				log.info(trxnum+" " +gw20.getTrxsts());
			}
		}
		try {
			System.out.println("trustedCrt is : "+trustedCrt);
			util.getMethodOfAddTrustedCertificate().invoke(util.getObjectOfECCryptoProcessor(), new Object[]{trustedCrt.getBytes()});
			this.verify(signedMessage);
			plain=new String((byte[])util.getMethodOfOfGetOrderMessage().invoke(util.getObjectOfECCryptoProcessor(), new Object[]{signedMessage.getBytes()}),Charset.forName("GBK"));
			log.debug("CITICV6_B2B RETURN:"+plain);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
		}	
		//适配银行未返回xml头错误
		if(plain!=null&&plain.indexOf("<?xml")<0) plain="<?xml version=\"1.0\" encoding=\"GBK\"?>"+plain;
		if(!plain.contains("</PB")&&plain.contains("/PB")){
			plain = plain.replaceAll("/PB", "</PB");
			log.debug("CITICV6_B2B replaceAll:"+plain);
		}
		Document doc=this.getDocument(plain);
		if(doc!=null){
			bean=new PayResult(XmlUtils.getNodeValue(doc,"ORDERNO"),
								this.bankcode,
								new BigDecimal(XmlUtils.getNodeValue(doc,"TRANAMT")),
								"01".equals(XmlUtils.getNodeValue(doc,"STT"))?1:-1);			
			bean.setBanktransseq(XmlUtils.getNodeValue(doc,"MCTJNLNO"));
			bean.setBankresult(XmlUtils.getNodeValue(doc,"STT"));
		}		
		return bean;

	}
	
	public Document getDocument(String xml){
		if(xml==null||xml.indexOf("<?xml")<0) return null;
	    try {
	    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(xml.getBytes("GBK")));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public String packageOrderInfo(BankOrder order) throws ServiceException
	{
		// 订单类交易确认请求报文交易号: ECGTPODR
		// 订单类交易确认请求接口
		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		sb.append("<stream>");
		//报文头_start
		sb.append("<VERSION>2.0</VERSION>");  //接口版本号
		sb.append("<BIZCODE>ECGTPODR</BIZCODE>");//交易号
		//报文头_e_n_d  
		
		//基本信息_start
		sb.append("<MCTNO>"+this.getCorpid()+"</MCTNO>");//商户编号
		sb.append("<NTFTYPE>2</NTFTYPE>");//通知类型：1 - 浏览器重定向；2 - 浏览器重定向，并有服务器点对点通讯。
		sb.append("<RSPTYPE>1</RSPTYPE>");//应答类型: 0 - 不需要应答。收到以HTTP协议响应码200则认为通知成功，其他为通知失败；1 - 需要应答。收到支付成功通知表示成功，否则触发重发。

		sb.append("<PCBURL>"+this.getRecurl()+"</PCBURL>"); //页面通知地址URL
		sb.append("<SCBURL>"+this.getHttprecurl()+"&amp;NR=SID_"+this.idx+"</SCBURL>");//后台通知地址URL
		sb.append("<TRANPERIOD>30</TRANPERIOD>");				//交易确认有效期(分钟)
		//基本信息_e_n_d
		
		//交易信息_start
		sb.append("<list name=\"TRANLIST\">");
		sb.append("<row>");
		sb.append("<MCTJNLNO>"+generateRandom()+"</MCTJNLNO>"); //商户交易流水号
		sb.append("<BSNNO>00</BSNNO>");								 //业务编号
		sb.append("<PAYEEACCNO>"+this.payeeaccno+"</PAYEEACCNO>");//收款账号
		sb.append("<CRYCODE>CNY</CRYCODE>");							 //币种代码
		sb.append("<PURPOSE>wonderpay</PURPOSE>");					 //备注
		//单笔交易信息_e_n_d
		
		//订单基本信息_start
		sb.append("<ORDERNO>"+order.getRandOrderID()+"</ORDERNO>"); //订单号
		sb.append("<ORDERTIME>"+order.getPostdate()+"</ORDERTIME>");//订单日期时间(YYYYMMDDhhmmss)
		sb.append("<MCDNAME>B2B</MCDNAME>");							//商品名称
		sb.append("<MCDTYPE>06</MCDTYPE>");								//商品类别
		sb.append("<ORDERDESC>wonderpay</ORDERDESC>");					//订单描述
		sb.append("<ORDERAMT>"+order.getAmount()+"</ORDERAMT>");    //订单金额
		//订单基本信息_e_n_d
		
		//订单辅助信息_start
		//订单其他信息
		sb.append("<BERNAME>wonderpay</BERNAME>");				//订单人
		sb.append("<ORDERSRC>wonderpay</ORDERSRC>");//订单来源
		//订单辅助信息_e_n_d
		
		sb.append("</row>");
		sb.append("</list>");
		//交易信息_e_n_d
		sb.append("</stream>");
		log.info("order info："+sb.toString());
		return sb.toString();
	}
	
	public String generateSign(String orderInfo) throws ServiceException
	{
		//声明明文消息，实际开发时内容为订单数据
    	//声明用于处理签名操作的对象
		log.info("citic_B2B:"+orderInfo);		
		byte[] signedMessage=null;
		try {
			util.getMethodOfSetSignerCertificate().invoke(util.getObjectOfECCryptoProcessor(), new Object[]{getFileContent(signercrt).getBytes()});
	    	util.getMethodOfSetSignerPrivatekey().invoke(util.getObjectOfECCryptoProcessor(), new Object[]{getFileContent(signerkey).getBytes(), getFileContent(signerpwd)});
	    	signedMessage	= this.sign(orderInfo);  
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(EventCode.CRYPT_EXCEPTION);
		}	
		return new String(signedMessage);
	}
	
	public void setPayeeaccno(String payeeaccno) {
		this.payeeaccno = payeeaccno;
	}
	
	public void setUtil(CiticV6_util util) {
		this.util = util;
	}

	public void setTrustedCrt(String trustedCrt) {
		this.trustedCrt = trustedCrt;
	}

	public String getSignercrt() {
		return signercrt;
	}

	public void setSignercrt(String signercrt) {
		this.signercrt = signercrt;
	}

	public String getSignerkey() {
		return signerkey;
	}

	public void setSignerkey(String signerkey) {
		this.signerkey = signerkey;
	}

	public String getSignerpwd() {
		return signerpwd;
	}

	public void setSignerpwd(String signerpwd) {
		this.signerpwd = signerpwd;
	}
	byte[] sign(String message)throws Exception{	
    	//对明文消息进行签名，得到BASE64编码的密文消息，包含了明文、签名和证书数据
    	byte[] signedMessage	= (byte[])util.getMethodOfSign().invoke(util.getObjectOfECCryptoProcessor(), new Object[]{message.getBytes()});
		return signedMessage;
    }
    
   byte[] verify(String signedMessage)throws Exception{
    	//对密文数据进行验签
    	util.getMethodOfVerify().invoke(util.getObjectOfECCryptoProcessor(), new Object[]{signedMessage.getBytes()});   	
    	byte[] message	= (byte[])util.getMethodOfOfGetOrderMessage().invoke(util.getObjectOfECCryptoProcessor(), new Object[]{signedMessage.getBytes()});   
    	return message;
    }
   
   X509Certificate getSignerCertificate(String signedMessage)throws Exception{
	   X509Certificate crt = (X509Certificate)util.getMethodOfGetSignerCertificate().invoke(util.getObjectOfECCryptoProcessor(), new Object[]{signedMessage.getBytes()});   
	   return crt;
   }
	private String getFileContent(String file){
		ByteArrayOutputStream bo=IOUtils.readFile(file);		
		if(bo!=null) return bo.toString();		
		return "";
	}
	
	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
}
