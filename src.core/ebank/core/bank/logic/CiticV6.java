/*
 * @Id: CiticV6.java 上午09:49:47 2009-9-24
 * 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;


import ebank.core.OrderService;
import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.IOUtils;
import ebank.core.common.util.XmlUtils;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.core.model.domain.GwTrxs;

import java.security.cert.X509Certificate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
/**
 * @author 
 * Description: 中信银行新版接口
 * 
 */
public class CiticV6 extends BankExt implements BankService{
	private Log log=LogFactory.getLog(this.getClass());
	
	private String trustedCrt;
	private String signercrt;
	private String signerkey;
	private String signerpwd;
	private CiticV6_util util;
	private OrderService orderService;
	

	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		String signedMessage=String.valueOf(reqs.get("SIGNRESMSG"));		
		log.info("SIGNRESMSG:"+signedMessage);
		String plain="";
		String trxnum=String.valueOf(reqs.get("trxnum"));
		if(trxnum!=null){
			GwTrxs gw20=orderService.findTrxByTrxnum(String.valueOf(reqs.get("trxnum")), this.bankcode);
			if(gw20!=null&&"1".equals(gw20.getTrxsts())){
				return new PayResult(trxnum,
						this.bankcode,
					 new BigDecimal(Amount.getFormatAmount(String.valueOf(gw20.getAmount()),-2)),
					1
					);
			}else{
				log.info(trxnum+" " +gw20.getTrxsts());
			}
		}
		try {
			util.getMethodOfAddTrustedCertificate().invoke(util.getObjectOfECCryptoProcessor(), new Object[]{getFileContent(trustedCrt).getBytes()});
			this.verify(signedMessage);
			plain=new String((byte[])util.getMethodOfOfGetOrderMessage().invoke(util.getObjectOfECCryptoProcessor(), new Object[]{signedMessage.getBytes()}),Charset.forName("GBK"));
			log.debug("CITICV6 RETURN:"+plain);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
		}	
		//适配银行未返回xml头错误
		if(plain!=null&&plain.indexOf("<?xml")<0) plain="<?xml version=\"1.0\" encoding=\"GBK\"?>"+plain;
		if(!plain.contains("</PB")&&plain.contains("/PB")){
			plain = plain.replaceAll("/PB", "</PB");
			log.debug("CITICV6 replaceAll:"+plain);
		}
		Document doc=this.getDocument(plain);
		PayResult bean=null;
		if(doc!=null){
			bean=new PayResult(XmlUtils.getNodeValue(doc,"ORDERNO"),
					this.bankcode,
				new BigDecimal(XmlUtils.getNodeValue(doc,"PAYAMT")),
				"AAAAAAA".equals(XmlUtils.getNodeValue(doc,"MSGCODE"))?1:-1
				);
			bean.setPayinfo(XmlUtils.getNodeValue(doc,"ACCNO")+"Name:"+XmlUtils.getNodeValue(doc,"PBCSTNAME"));			
			bean.setBanktransseq(XmlUtils.getNodeValue(doc,"PAYNO"));
			bean.setBankresult(XmlUtils.getNodeValue(doc,"MSGCODE"));
			//bean.setResultflg(XmlUtils.getNodeValue(doc,"MSGCN"));
		}		
		return bean;
	}
	public Document getDocument(String xml){
		if(xml==null||xml.indexOf("<?xml")<0) return null;
		DocumentBuilder db=null;
		Document doc=null;
	    try {
	    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			doc = db.parse(new ByteArrayInputStream(xml.getBytes("GBK")));			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("GBK exception");
			try {
				doc = db.parse(new ByteArrayInputStream(xml.getBytes("utf-8")));
			} catch (Exception e2) {
				e2.printStackTrace();
				log.error("utf-8 exception");
				try {
					doc = db.parse(new ByteArrayInputStream(xml.getBytes()));
				} catch (Exception e3) {
					e3.printStackTrace();
				}				
			}			
		}
		return doc;
	}

	public String sendOrderToBank(BankOrder order) throws ServiceException {
		Date  ps=null;
		try {
			if(order.getPostdate()!=null)
			ps=new SimpleDateFormat("yyyyMMddHHmmss").parse(order.getPostdate());
		} catch (Exception e) {
			e.printStackTrace();
			ps=new Date();
		}
		
		String plain=
		"<?xml version=\"1.0\" encoding=\"GBK\"?>"+
        "<stream>"+
        "<E3RDPAYNO>"+this.getCorpid()+"</E3RDPAYNO>"+                                 //<!--第三方支付平台编号-->
        "<ORDERMODE>01</ORDERMODE>"+                                                //	<!--订单支付模式-->
	    //"<B2CCERTNO>"+this.getCorpid()+"</B2CCERTNO>"+                              //<!-- B2C编号-->	   
	    "<ORDERDATE>"+new SimpleDateFormat("yyyy-MM-dd").format(ps)+"</ORDERDATE>"+ //<!--订单日期-->"	    		
	    "<ORDERTIME>"+new SimpleDateFormat("HH:mm:ss").format(ps)+"</ORDERTIME>"+ //<!--订单时间-->
	    "<ORDERNO>"+order.getRandOrderID()+"</ORDERNO>"+                            //<!--订单号-->
	    "<CURRID>01</CURRID>"+                                                      //<!--订单支付币种-->
	    "<ORDERAMT>"+order.getAmount()+"</ORDERAMT>"+                               //<!--订单金额-->
	    "<MEMO>B2C支付</MEMO>"+                                                     //<!--摘要-->
	    "<NOTIFYMODE>01</NOTIFYMODE>"+                                              //<!--通知模式-->
	    "<NOTIFYURL>"+this.recurl+"?trxnum="+order.getRandOrderID()+"</NOTIFYURL>"+                              //<!--商户通知->
	    "<RISKLEVEL>00</RISKLEVEL>"+                                                //<!--风险级别-->
	    "<SUPPTCARDTYPE>00</SUPPTCARDTYPE>"+                                        //<!--支持卡种-->
	    "<TTL></TTL>"+                                                              //<!--订单有效期--> 
        "<MEMBERID></MEMBERID>"+                                                    //<!—订单人,可以是用户在商户网站的USERID-->
  	    "<NOTIFYSCOPE>02</NOTIFYSCOPE></stream>"; //+                                              //<!--支付结果通知范围01:只通知成功订单02：成功和失败订单均通知-->
	    /*"<list name=\"OrderListQue\">"+ 
		"<row>"+ 
			"<PRODUCTNAME></PRODUCTNAME>"+ //<!--商品名称-->
			"<PRODUCTCODE>1</PRODUCTCODE>"+ //<!--序号-->
			"<QUANTITY>1</QUANTITY>"+ //<!--数量-->
			"<SUBTOTAL></SUBTOTAL>"+ //<!--金额小记-->
			"<TERM>0</TERM>"+ //<!--分期方式-->
			"<NOTE></NOTE>"+ //<!--备注-->
	    "</row></list>"*/	    
		log.debug("citic:"+plain);		
		byte[] signedMessage=null;
		try {
			util.getMethodOfSetSignerCertificate().invoke(util.getObjectOfECCryptoProcessor(), new Object[]{getFileContent(signercrt).getBytes()});
	    	util.getMethodOfSetSignerPrivatekey().invoke(util.getObjectOfECCryptoProcessor(), new Object[]{getFileContent(signerkey).getBytes(), getFileContent(signerpwd)});
	    	signedMessage	= this.sign(plain);  
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(EventCode.CRYPT_EXCEPTION);
		}	 	    	
		StringBuffer sf=new StringBuffer("");		
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" name=\"SIGNREQMSG\" value=\""+new String(signedMessage)+"\" >");		
		sf.append("</form>");		
		return sf.toString();
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

	public void setTrustedCrt(String trustedCrt) {
		this.trustedCrt = trustedCrt;
	}

	public void setSignercrt(String signercrt) {
		this.signercrt = signercrt;
	}

	public void setSignerkey(String signerkey) {
		this.signerkey = signerkey;
	}

	public void setUtil(CiticV6_util util) {
		this.util = util;
	}

	public String getSignerpwd() {
		return signerpwd;
	}

	public void setSignerpwd(String signerpwd) {
		this.signerpwd = signerpwd;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	
	
	
	

}
