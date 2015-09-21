package ebank.core.bank.third;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.HandleException;
import ebank.core.common.util.MD5sign;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class LitePay extends BankExt implements BankService {

	static Logger logger = Logger.getLogger(LitePay.class); 
	private static String version="1.0.0";//
	private static String charset="UTF-8";//
	private static String signMethod="MD5";//
	private static String orderCurrency="156";//人民币
	private static String transType="01";//人民币
	
	private String pubkey;		
	
	

	@Override
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		StringBuffer sf=new StringBuffer("");	
		String moneystr=String.valueOf(Amount.getIntAmount(order.getAmount(), 2));
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" name=\"version\" value=\""+version+"\" >");
		sf.append("<input type=\"hidden\" name=\"charset\" value=\""+charset+"\" >");
		sf.append("<input type=\"hidden\" name=\"signMethod\" value=\""+signMethod+"\" >");
		sf.append("<input type=\"hidden\" name=\"transType\" value=\""+transType+"\" >");
		sf.append("<input type=\"hidden\" name=\"merAbbr\" value=\"WonderPay\" >");
		sf.append("<input type=\"hidden\" name=\"merId\" value=\""+this.corpid+"\" >");
		sf.append("<input type=\"hidden\" name=\"merCode\" value=\"\" >");
		sf.append("<input type=\"hidden\" name=\"backEndUrl\" value=\""+this.getRecurl()+"&NR=SID_"+this.idx+"\" >");
		sf.append("<input type=\"hidden\" name=\"frontEndUrl\" value=\""+this.getRecurl()+"\" >");
		sf.append("<input type=\"hidden\" name=\"acqCode\" value=\"\" >");
		sf.append("<input type=\"hidden\" name=\"orderTime\" value=\""+order.getPostdate()+"\" >");
		sf.append("<input type=\"hidden\" name=\"orderNumber\" value=\""+order.getRandOrderID()+"\" >");
		sf.append("<input type=\"hidden\" name=\"commodityName\" value=\"\" >");
		sf.append("<input type=\"hidden\" name=\"commodityUrl\" value=\"\" >");
		sf.append("<input type=\"hidden\" name=\"commodityUnitPrice\" value=\"\" >");
		sf.append("<input type=\"hidden\" name=\"commodityQuantity\" value=\"\" >");
		sf.append("<input type=\"hidden\" name=\"transferFee\" value=\"\" >");
		sf.append("<input type=\"hidden\" name=\"commodityDiscount\" value=\"\" >");
		sf.append("<input type=\"hidden\" name=\"orderAmount\" value=\""+moneystr+"\" >");
		sf.append("<input type=\"hidden\" name=\"orderCurrency\" value=\""+orderCurrency+"\" >");
		sf.append("<input type=\"hidden\" name=\"customerName\" value=\"\" >");
		sf.append("<input type=\"hidden\" name=\"defaultPayType\" value=\"\" >");
		sf.append("<input type=\"hidden\" name=\"defaultBankNumber\" value=\"\" >");
		sf.append("<input type=\"hidden\" name=\"transTimeout\" value=\"\" >");
		sf.append("<input type=\"hidden\" name=\"customerIp\" value=\""+order.getCustip()+"\" >");
		sf.append("<input type=\"hidden\" name=\"origQid\" value=\"\" >");
		sf.append("<input type=\"hidden\" name=\"merReserved\" value=\"\" >");
		sf.append("<input type=\"hidden\" name=\"signature\" value=\""+generateTranData(order)+"\" >");
		
		sf.append("</form>");		
		if(logger.isDebugEnabled()) logger.debug(sf.toString());
		return sf.toString();
	}

	@Override
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		PayResult bean = null ;
		try {
		String version = String.valueOf(reqs.get("version")) ;
		String charset = String.valueOf(reqs.get("charset")) ;
		String signMethod = String.valueOf(reqs.get("signMethod")) ;
		String signature = String.valueOf(reqs.get("signature")) ;
		String transType = String.valueOf(reqs.get("transType")) ;
		String respCode = String.valueOf(reqs.get("respCode")) ;
		String respMsg = String.valueOf(reqs.get("respMsg")) ;
		String merAbbr = String.valueOf(reqs.get("merAbbr")) ;
		String merId = String.valueOf(reqs.get("merId")) ;
		String orderNumber = String.valueOf(reqs.get("orderNumber")) ;
		String traceNumber = String.valueOf(reqs.get("traceNumber")) ;
		String traceTime = String.valueOf(reqs.get("traceTime")) ;
		String qid = String.valueOf(reqs.get("qid")) ;
		String orderAmount = String.valueOf(Amount.getFormatAmount(String.valueOf(reqs.get("orderAmount")), -2)); ;
		String orderCurrency = String.valueOf(reqs.get("orderCurrency")) ;
		String respTime = String.valueOf(reqs.get("respTime")) ;
		String settleAmount = String.valueOf(reqs.get("settleAmount")) ;
		String settleCurrency = String.valueOf(reqs.get("settleCurrency")) ;
		String settleDate = String.valueOf(reqs.get("settleDate")) ;
		String exchangeRate = String.valueOf(reqs.get("exchangeRate")) ;
		String exchangeDate = String.valueOf(reqs.get("exchangeDate")) ;
		
		//验证页签
		if(signature.equals(verifySignMsg(reqs))){
			bean = new PayResult(orderNumber,this.bankcode, new BigDecimal(orderAmount), "00".equals(respCode)?1:-1) ;
			bean.setBankresult(respCode) ;			
			bean.setBanktransseq(qid); //银行流水
			if(respTime!=null&&!"".equals(respTime)){
				if(respTime.length()>8){
					respTime = respTime.substring(0, 8);
				}
			}
			bean.setBankdate(respTime); //清算日期
			
		   logger.info(String.valueOf(reqs.get("NR")));
			if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(String.valueOf(reqs.get("NR")))){
				 reqs.put("RES","200");
			}
		}else{
			logger.info("LitePaySign wrong");
			throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
		}
		} catch (Exception e) {
			HandleException.handle(e) ;
		}
		
		
		return bean;
	}
	
	private String generateTranData(BankOrder order) {
		
		String signature="";
		String moneystr=String.valueOf(Amount.getIntAmount(order.getAmount(), 2));
		Map sPara = new HashMap();
		sPara.put("version",version);
		sPara.put("charset",charset);
//		sPara.put("signMethod", signMethod);
		sPara.put("transType", transType);
		sPara.put("merAbbr", "WonderPay");
		
		sPara.put("merId",this.corpid);
		sPara.put("merCode","");
		sPara.put("backEndUrl", this.getRecurl()+"&NR=SID_"+this.idx);
		sPara.put("frontEndUrl", this.getRecurl());
		
		sPara.put("acqCode", "");
		sPara.put("orderTime", order.getPostdate());
		sPara.put("orderNumber", order.getRandOrderID());
		sPara.put("commodityName", "");
		sPara.put("commodityUrl", "");
		sPara.put("commodityUnitPrice", "");
		sPara.put("commodityQuantity", "");
		sPara.put("transferFee", "");
		sPara.put("commodityDiscount", "");
		sPara.put("orderAmount", moneystr);
		sPara.put("orderCurrency", orderCurrency);
		sPara.put("customerName", "");
		sPara.put("defaultPayType", "");
		sPara.put("defaultBankNumber", "");
		sPara.put("transTimeout", "");
		sPara.put("customerIp", order.getCustip());
		sPara.put("origQid", "");
		sPara.put("merReserved", "");
		
	    signature = MD5sign.BuildMysignForLitePay(sPara, this.pubkey,"utf-8");//生成签名结
		return signature;
	}
	
	
	private String verifySignMsg(HashMap reqs) 
	{
		String signature="";
		String version = String.valueOf(reqs.get("version")) ;
		String charset = String.valueOf(reqs.get("charset")) ;
		String signMethod = String.valueOf(reqs.get("signMethod")) ;
		String transType = String.valueOf(reqs.get("transType")) ;
		String respCode = String.valueOf(reqs.get("respCode")) ;
		String respMsg = String.valueOf(reqs.get("respMsg")) ;
		String merAbbr = String.valueOf(reqs.get("merAbbr")) ;
		String merId = String.valueOf(reqs.get("merId")) ;
		String orderNumber = String.valueOf(reqs.get("orderNumber")) ;
		String traceNumber = String.valueOf(reqs.get("traceNumber")) ;
		String traceTime = String.valueOf(reqs.get("traceTime")) ;
		String qid = String.valueOf(reqs.get("qid")) ;
		String orderAmount = String.valueOf(reqs.get("orderAmount")); ;
		String orderCurrency = String.valueOf(reqs.get("orderCurrency")) ;
		String respTime = String.valueOf(reqs.get("respTime")) ;
		String settleAmount = String.valueOf(reqs.get("settleAmount")) ;
		String settleCurrency = String.valueOf(reqs.get("settleCurrency")) ;
		String settleDate = String.valueOf(reqs.get("settleDate")) ;
		String exchangeRate = String.valueOf(reqs.get("exchangeRate")) ;
		String exchangeDate = String.valueOf(reqs.get("exchangeDate")) ;
		String cupReserved = String.valueOf(reqs.get("cupReserved")) ;
		
		Map sPara = new HashMap();
		sPara.put("version",version);
		sPara.put("charset",charset);
		sPara.put("transType", transType);
		sPara.put("respCode", respCode);
		sPara.put("respMsg", respMsg);
		sPara.put("merAbbr", merAbbr);
		sPara.put("merId",merId);
		sPara.put("orderNumber", orderNumber);
		sPara.put("traceNumber", traceNumber);
		
		sPara.put("traceTime", traceTime);
		sPara.put("qid", qid);
		sPara.put("orderAmount", orderAmount);
		sPara.put("orderCurrency", orderCurrency);
		sPara.put("respTime", respTime);
		sPara.put("settleAmount", settleAmount);
		sPara.put("settleCurrency", settleCurrency);
		
		sPara.put("settleDate",settleDate);
		sPara.put("exchangeRate", exchangeRate);
		sPara.put("exchangeDate", exchangeDate);
		sPara.put("cupReserved", cupReserved);
		
		
		signature = MD5sign.BuildMysignForLitePay(sPara, this.pubkey,"utf-8");//生成签名结
		
		return signature;
		
	}
	

	
	public void setPubkey(String pubkey) {
		this.pubkey = pubkey;
	}

}
