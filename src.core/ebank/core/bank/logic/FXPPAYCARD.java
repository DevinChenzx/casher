/*
 * @Id: SPDB.java 14:13:12 2006-11-21
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap; 


import org.apache.log4j.Logger;

import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

/**
 * @author 
 * Description: ���˸�ʢԤ���ѿ��ӿ�
 * 
 */
public class FXPPAYCARD extends BankExt implements BankService{
	private Logger log = Logger.getLogger(this.getClass());
	private String merprivatekey;
	private String publickey;


	/* 
	 * ������Ϣ��֤
	 */
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		
		PayResult bean = null;
		//���հ����ӿ��ĵ��еĹ涨����װ���ڴӰ������յ�����ǩ�����ַ���		
		String signString = String.valueOf(reqs.get("tradeType"))
				+"~|~"+ String.valueOf(reqs.get("merchantId"))
				+"~|~"+ String.valueOf(reqs.get("merchantName"))
				+"~|~"+ String.valueOf(reqs.get("tradeTime"))
				+"~|~"+ String.valueOf(reqs.get("orderId"))
				+"~|~"+ String.valueOf(reqs.get("amount"))
				+"~|~"+ String.valueOf(reqs.get("ysTradeId"))
				+"~|~"+ String.valueOf(reqs.get("ysTime"))
				+"~|~"+ String.valueOf(reqs.get("respCode"));
	   
		 log.info("signString"+signString);
		String  signature= String.valueOf(reqs.get("signature"));
		//�õ�ǩ����֤���
		String str=String.valueOf(Amount.getFormatAmount(String.valueOf(reqs.get("amount")), -2));
		try{
			if(com.hnapay.payment.client.key.DSAService.verify(this.publickey, signString, signature)){//ǩ����֤ͨ��				
				if("00".equals(String.valueOf(reqs.get("respCode")))){
				    bean=new PayResult(String.valueOf(reqs.get("orderId")),this.bankcode,new BigDecimal(str),1);
				    if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(String.valueOf(reqs.get("NR")))){
						 reqs.put("RES","hnair.netpay.rspcode=00");					
					}
				}else{
					bean=new PayResult(String.valueOf(reqs.get("orderId")),this.bankcode,new BigDecimal(str),-1);				
				}				
				bean.setBankresult(String.valueOf(reqs.get("respCode")));
				bean.setBanktransseq(String.valueOf(reqs.get("ysTradeId")));
				String date = String.valueOf(reqs.get("tradeTime"));
				if(date!=null&&date.length()>8){
					date =date.substring(0, 7);
				}
				bean.setBankdate(date); //��������			
			}else{
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);				
			}			
		}catch (Exception e) {
			HandleException.handle(e);
		}
		return bean;
	}

	/* 
	 * ��������form
	 * 
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		StringBuffer sf=new StringBuffer("");   
		
		String moneystr=String.valueOf(Amount.getIntAmount(order.getAmount(), 2));
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\"/>");
		sf.append("<input type=\"hidden\" name=\"merchantId\" value=\""+this.getCorpid()+"\" />");
		sf.append("<input type=\"hidden\" name=\"merchantName\" value=\"����\" />");
		sf.append("<input type=\"hidden\" name=\"orderId\" value=\""+order.getRandOrderID()+"\" />");
		sf.append("<input type=\"hidden\" name=\"tradeTime\" value=\""+order.getPostdate()+"\" />");
		sf.append("<input type=\"hidden\" name=\"amount\" value=\""+moneystr+"\" />");
		
		
		sf.append("<input type=\"hidden\" name=\"merUrl1\" value=\""+this.getRecurl()+"&NR=SID_"+this.idx+"\" />");
		sf.append("<input type=\"hidden\" name=\"merUrl2\" value=\""+this.getRecurl()+"\" />");
		sf.append("<input type=\"hidden\" name=\"tradeType\" value=\"501\" />");
		sf.append("<input type=\"hidden\" name=\"subMerchantId\" value=\""+order.getMp().get("subMerchantId")+"\" />");
		sf.append("<input type=\"hidden\" name=\"signature\" value=\""+generateSignMsg(order)+"\" />");		
		return sf.toString();
	}
	/**
	 * ��ȡҳǩ
	 * @param order
	 * @return
	 */
	private String generateSignMsg(BankOrder order) 
	{
		String moneystr=String.valueOf(Amount.getIntAmount(order.getAmount(), 2));
		//��������Դ·��
		String ifno="501~|~"+this.getCorpid()+"~|~����~|~"+order.getPostdate()+"~|~"+order.getRandOrderID()+"~|~"+moneystr+"~|~"+this.getRecurl()+"&NR=SID_"+this.idx+"~|~"+this.getRecurl()+"~|~"+order.getMp().get("subMerchantId");
		String signature="";
		log.info("FXP:"+ifno);
		System.out.println("FXP:"+ifno);
		try {
			signature = com.hnapay.payment.client.key.DSAService.sign(this.merprivatekey, ifno);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return signature;
	}
	
	public String dateStr()
	{
		String date = "";
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		date = sdf.format(d);
		return date;
	}
	
	
	public void setMerprivatekey(String merprivatekey) {
		this.merprivatekey = merprivatekey;
	}

	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}
}
