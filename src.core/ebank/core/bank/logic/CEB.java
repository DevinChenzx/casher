/*
 * @Id: CEB.java 下午01:36:19 2008-3-19
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.csii.payment.client.core.CebMerchantSignVerify;

import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

/**
 * @author 
 * Description: 光大银行
 * 
 */
public class CEB extends BankExt implements BankService{
	private Logger log = Logger.getLogger(this.getClass());

	//	12位订单号
	public String generateOrderID() throws ServiceException{
		String RandOrderID=sequenceservice.getCode().substring(6,14);	//yyDDDD(6)+8位序列
		Random rd=new Random();
		String str="";
		for(int i=0;i<4-this.prefixnum.length();i++) str+=rd.nextInt(10);
		rd=null;
		return prefixnum+RandOrderID+str;
	}

	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#sendOrderToBank(ebank.core.domain.BankOrder)
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		String plain="transId=IPER~|~merchantId="+this.getCorpid()+"~|~orderId="+
			order.getRandOrderID()+"~|~transAmt="+order.getAmount()+"~|~transDateTime="+order.getPostdate()+
			"~|~currencyType=01~|~customerName=~|~productInfo=~|~customerEMail=~|~merURL="+this.getHttprecurl()+"&NR=SID_"+this.idx+"~|~merURL1="+this.getRecurl()+"~|~msgExt=";
		String sign=CebMerchantSignVerify.merchantSignData_ABA(plain);
		
		StringBuffer sf=new StringBuffer("");
		sf.append("<form name=\"sendOrder\" action=\""+this.getDesturl()+"\" method=post>");
		sf.append("<input type=hidden name=\"TransName\" value='IPER'/>");
		sf.append("<input type=hidden name=\"Plain\" value=\""+plain+"\">");
		sf.append("<input type=hidden name=\"Signature\" value=\""+sign+"\">");
		sf.append("</form>");
		log.info(sf.toString());
		return sf.toString();
	}
	
	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#getPayResult(java.util.HashMap)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		String plain=String.valueOf(reqs.get("Plain"));
		String signature=String.valueOf(reqs.get("Signature"));
		PayResult bean=null;
		try {
			log.info("plain:"+plain+" sign:"+signature+"reqs:"+reqs);
			String[] result=plain.split("~\\|~");
			Map mp = new HashMap();
			for (int i = 0; i < result.length; i++) 
			{
				log.info("result"+i+"="+result[i]);
			}
			for(int i=0;i<result.length;i++)
			{
				String[] ss=result[i].split("\\=");
				if(ss!=null){
					if(ss.length>1)	mp.put(ss[0], ss[1]);
					else mp.put(ss[0], "");
				}
				log.info("mp value is "+mp.get(ss[0]));
			}
			log.info("time is "+mp.get("transDateTime"));
			if(CebMerchantSignVerify.merchantVerifyPayGate_ABA(signature,plain)){//签名验证通过
				//通知即成功
				log.info("Plain:"+plain);
				log.info("Signature:"+signature);
				
				bean=new PayResult(String.valueOf(mp.get("orderId")),this.bankcode,new BigDecimal(String.valueOf(mp.get("transAmt"))),1);
				bean.setBankresult("0");
				bean.setBanktransseq(String.valueOf(mp.get("transSeqNo"))); //银行流水
				bean.setBankdate(String.valueOf(mp.get("clearingDate"))); //清算日期
			}else{
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
			}
			//通知响应
			log.info(String.valueOf(reqs.get("NR")));
			if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(String.valueOf(reqs.get("NR")))){
				log.info("sec time is "+mp.get("transDateTime"));
				String plain_r="merchantId="+String.valueOf(mp.get("merchantId"));
				plain_r+="~|~orderId="+String.valueOf(mp.get("orderId"));
				plain_r+="~|~transDateTime="+mp.get("transDateTime");
				plain_r+="~|~procStatus=1";
				plain_r+="~|~merURL2="+this.getRecurl();
				String sign_r=CebMerchantSignVerify.merchantSignData_ABA(plain_r);
				reqs.put("RES", "Plain="+plain_r+"\r\n"+"Signature="+sign_r);
				log.info("returnMsg："+"Plain="+plain_r+"\r\n"+"Signature="+sign_r);
			}
		}catch (Exception e) {
			HandleException.handle(e);
		}
		return bean;
	}
}
