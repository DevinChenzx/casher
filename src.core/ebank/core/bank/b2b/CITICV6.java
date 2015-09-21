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
		//��������δ����xmlͷ����
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
		// �����ཻ��ȷ�������Ľ��׺�: ECGTPODR
		// �����ཻ��ȷ������ӿ�
		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		sb.append("<stream>");
		//����ͷ_start
		sb.append("<VERSION>2.0</VERSION>");  //�ӿڰ汾��
		sb.append("<BIZCODE>ECGTPODR</BIZCODE>");//���׺�
		//����ͷ_e_n_d  
		
		//������Ϣ_start
		sb.append("<MCTNO>"+this.getCorpid()+"</MCTNO>");//�̻����
		sb.append("<NTFTYPE>2</NTFTYPE>");//֪ͨ���ͣ�1 - ������ض���2 - ������ض��򣬲��з�������Ե�ͨѶ��
		sb.append("<RSPTYPE>1</RSPTYPE>");//Ӧ������: 0 - ����ҪӦ���յ���HTTPЭ����Ӧ��200����Ϊ֪ͨ�ɹ�������Ϊ֪ͨʧ�ܣ�1 - ��ҪӦ���յ�֧���ɹ�֪ͨ��ʾ�ɹ������򴥷��ط���

		sb.append("<PCBURL>"+this.getRecurl()+"</PCBURL>"); //ҳ��֪ͨ��ַURL
		sb.append("<SCBURL>"+this.getHttprecurl()+"&amp;NR=SID_"+this.idx+"</SCBURL>");//��̨֪ͨ��ַURL
		sb.append("<TRANPERIOD>30</TRANPERIOD>");				//����ȷ����Ч��(����)
		//������Ϣ_e_n_d
		
		//������Ϣ_start
		sb.append("<list name=\"TRANLIST\">");
		sb.append("<row>");
		sb.append("<MCTJNLNO>"+generateRandom()+"</MCTJNLNO>"); //�̻�������ˮ��
		sb.append("<BSNNO>00</BSNNO>");								 //ҵ����
		sb.append("<PAYEEACCNO>"+this.payeeaccno+"</PAYEEACCNO>");//�տ��˺�
		sb.append("<CRYCODE>CNY</CRYCODE>");							 //���ִ���
		sb.append("<PURPOSE>wonderpay</PURPOSE>");					 //��ע
		//���ʽ�����Ϣ_e_n_d
		
		//����������Ϣ_start
		sb.append("<ORDERNO>"+order.getRandOrderID()+"</ORDERNO>"); //������
		sb.append("<ORDERTIME>"+order.getPostdate()+"</ORDERTIME>");//��������ʱ��(YYYYMMDDhhmmss)
		sb.append("<MCDNAME>B2B</MCDNAME>");							//��Ʒ����
		sb.append("<MCDTYPE>06</MCDTYPE>");								//��Ʒ���
		sb.append("<ORDERDESC>wonderpay</ORDERDESC>");					//��������
		sb.append("<ORDERAMT>"+order.getAmount()+"</ORDERAMT>");    //�������
		//����������Ϣ_e_n_d
		
		//����������Ϣ_start
		//����������Ϣ
		sb.append("<BERNAME>wonderpay</BERNAME>");				//������
		sb.append("<ORDERSRC>wonderpay</ORDERSRC>");//������Դ
		//����������Ϣ_e_n_d
		
		sb.append("</row>");
		sb.append("</list>");
		//������Ϣ_e_n_d
		sb.append("</stream>");
		log.info("order info��"+sb.toString());
		return sb.toString();
	}
	
	public String generateSign(String orderInfo) throws ServiceException
	{
		//����������Ϣ��ʵ�ʿ���ʱ����Ϊ��������
    	//�������ڴ���ǩ�������Ķ���
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
    	//��������Ϣ����ǩ�����õ�BASE64�����������Ϣ�����������ġ�ǩ����֤������
    	byte[] signedMessage	= (byte[])util.getMethodOfSign().invoke(util.getObjectOfECCryptoProcessor(), new Object[]{message.getBytes()});
		return signedMessage;
    }
    
   byte[] verify(String signedMessage)throws Exception{
    	//���������ݽ�����ǩ
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
