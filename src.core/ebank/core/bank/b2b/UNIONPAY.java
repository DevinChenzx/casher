/*
 * @Id: SPDB.java 14:13:12 2006-11-21
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.b2b;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import chinapay.PrivateKey;
import chinapay.SecureLink;

import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;


/**
 * @author 
 * Description: 中国银联
 * 
 */
public class UNIONPAY extends BankExt implements BankService{
	private Logger log = Logger.getLogger(this.getClass());
	private String fileCert;        
	private String fileKey;       		

	public String getFileCert() {
		return fileCert;
	}

	public void setFileCert(String fileCert) {
		this.fileCert = fileCert;
	}

	public String getFileKey() {
		return fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	//10位（1+8(序列)+1位随机
	public String generateOrderID() throws ServiceException{
		String str="";
		Random rd=new Random();
		for(int i=0;i<2-this.prefixnum.length();i++) str+=rd.nextInt(10);	
		rd=null;
		return prefixnum+sequenceservice.getCode();
	}
	
	public String generateRandom() throws ServiceException{
		String str="";
		Random rd=new Random();
		for(int i=0;i<2-this.prefixnum.length();i++) str+=rd.nextInt(10);			
		rd=null;
		String aa= prefixnum+sequenceservice.getCode().substring(6,14);
		return prefixnum+sequenceservice.getCode().substring(6,14)+aa.substring(0, 5);
	}
	
	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#getPayResult(java.util.HashMap)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PayResult getPayResult(HashMap reqs) throws ServiceException 
	{
		
		String checkvalue=String.valueOf(reqs.get("checkvalue"));
		PayResult bean=null;
		try{
			log.debug(reqs);			
			if(verifySignData(checkvalue,reqs))//签名验证通过
			{
				if("1001".equals(String.valueOf(reqs.get("status")))){
				    bean=new PayResult(String.valueOf(reqs.get("orderno")),this.bankcode,new BigDecimal(Amount.getFormatAmount(String.valueOf(reqs.get("amount")),-2)),1);
				}else{
					bean=new PayResult(String.valueOf(reqs.get("orderno")),this.bankcode,new BigDecimal(Amount.getFormatAmount(String.valueOf(reqs.get("amount")),-2)),-1);				
				}
				bean.setBankresult(String.valueOf(reqs.get("status")));
				//bean.setBanktransseq(String.valueOf(mp.get("AcqSsn"))); //银行流水
				bean.setBankdate(String.valueOf(reqs.get("transdate"))); //清算日期			
			}else{
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);				
			}
			log.debug(String.valueOf(reqs.get("NR")));			
			if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(String.valueOf(reqs.get("NR")))){
				 reqs.put("RES","200");
			}
		}catch (Exception e) {
			HandleException.handle(e);
		}
		return bean;
	}

	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#sendOrderToBank(ebank.core.domain.BankOrder)
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException {
				
		String gateID = "";
		if (order.getMp() != null && order.getMp().get("outChannel") != null) {
			gateID = String.valueOf(order.getMp().get("outChannel"));
		}
		String signData = "";
		String orderId = order.getRandOrderID();
		String postDate = order.getPostdate().substring(0, 8);
		StringBuffer sf=new StringBuffer("");
		order.setAmount(this.exchangeAmount(order.getAmount()));
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\"/>");
		sf.append("<input type=\"hidden\" name=\"MerId\" value=\""+this.getCorpid()+"\" />");
		sf.append("<input type=\"hidden\" name=\"OrdId\" value=\""+orderId+"\" />");
		sf.append("<input type=\"hidden\" name=\"TransAmt\" value=\""+order.getAmount()+"\" />");
		sf.append("<input type=\"hidden\" name=\"CuryId\" value=\"156\" />");
		sf.append("<input type=\"hidden\" name=\"TransDate\" value=\""+postDate+"\" />");
		sf.append("<input type=\"hidden\" name=\"TransType\" value=\"0001\" />");
		sf.append("<input type=\"hidden\" name=\"Version\" value=\"20070129\" />");
		sf.append("<input type=\"hidden\" name=\"BgRetUrl\" value=\""+this.getHttprecurl()+"&NR=SID_"+this.idx+"\" />");
		sf.append("<input type=\"hidden\" name=\"PageRetUrl\" value=\""+this.getRecurl()+"\" />");
		sf.append("<input type=\"hidden\" name=\"GateId\" value=\""+gateID+"\" />");
		sf.append("<input type=\"hidden\" name=\"Priv1\" value=\"wonderpay\" />");
		signData = generateSign(order,orderId,postDate);
		sf.append("<input type=\"hidden\" name=\"ChkValue\" value=\""+signData+"\" />");
		log.debug("sf.toString():========================="+sf.toString());		
		return sf.toString();
	}
	
	public String generateSign(BankOrder order, String orderId, String postDate)
	{
		PrivateKey key=new PrivateKey();
		SecureLink t; 
		boolean flag; 
		String MerId=this.getCorpid();
		String OrdId=orderId;
		String TransAmt=order.getAmount();
		String CuryId="156";
		String TransDate=postDate;
		String TransType="0001";
		String Priv1="wonderpay";
		String ChkValue;
		flag=key.buildKey(MerId, 0, this.getFileKey());
		if (flag==false) 
		{ 
			log.debug("build key error!");			
			return ""; 
		} 
		t=new SecureLink (key); 
		// 对订单的签名
		ChkValue= t.Sign(MerId+OrdId+TransAmt+CuryId+TransDate+TransType+Priv1);
		return ChkValue;
	}
	
	@SuppressWarnings("rawtypes")
	public boolean verifySignData(String signData, Map map)
	{
		boolean flag = false;
		boolean flag1 = false;
		chinapay.PrivateKey key=new chinapay.PrivateKey(); 
		chinapay.SecureLink t; 
		String MerId=String.valueOf(map.get("merid")); 
		String OrdId=String.valueOf(map.get("orderno"));  
		String TransDate=String.valueOf(map.get("transdate"));  
		String TransAmt=String.valueOf(map.get("amount"));  
		String CuryId=String.valueOf(map.get("currencycode"));  
		String TransType=String.valueOf(map.get("transtype")); 
		String OrderStatus=String.valueOf(map.get("status")); 
		String ChkValue=String.valueOf(map.get("checkvalue")); 
		String GateId=String.valueOf(map.get("GateId")); 
		String Priv1=String.valueOf(map.get("Priv1"));
		flag1=key.buildKey("999999999999999", 0, this.getFileCert());
		if(flag1==false) 
		{ 
			log.debug("build key error!");			
			return flag;
		} 
		t=new chinapay.SecureLink (key); 
		flag=t.verifyTransResponse(MerId,OrdId,TransAmt,CuryId,TransDate,TransType,OrderStatus, signData);
		if(!flag) {
			//签名验证错误处理
			log.debug("签名验证错误处理!");			
		}
		return flag;
	}
	
	public String exchangeAmount(String orderAmount)
	{
		String amount = "0";
		if(orderAmount!=null && !orderAmount.equals(""))
		{
			orderAmount = orderAmount.replaceAll("\\.", "");
			for(int i=1;i<12-orderAmount.length();i++)
			{
				amount=amount.concat("0");
			}
		}
		return amount.concat(orderAmount);
	}
}
