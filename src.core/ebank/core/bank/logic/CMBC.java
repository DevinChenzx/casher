/*
 * @Id: SequenceService_Impl.java 13:06:17 2006-2-14
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */

package ebank.core.bank.logic;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import java.util.Locale;
import org.apache.log4j.Logger;

import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.common.util.IOUtils;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;


import Union.JnkyServer;


/**
 * @author 
 * Description: 民生银行服务实现
 * 
 */

public class CMBC extends BankExt implements BankService{
	static Logger log = Logger.getLogger(CMBC.class); 
			
	
	private String fileKey;
	private String fileCert;
	
	
	//15位订单
	/* (non-Javadoc)
	 * @see ebank.core.bank.logic.BankExt#generateOrderID()
	 */
	public String generateOrderID() throws ServiceException{
		String RandOrderID=sequenceservice.getCode();	//yyDDD(6)+8位序列			
		Random rd=new Random();
		String str="";			
		for(int i=0;i<3-this.prefixnum.length();i++) str+=rd.nextInt(10);
		rd=null;		
		return this.corpid+RandOrderID+str;
	}
	
	private String generateSignMsg(BankOrder order){
		//订单号|交易金额|币种|交易日期|交易时间|商户代码|商户名称|备注1|备注2|是否实时返回标志|处理结果返回的URL| MAC		
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd",Locale.US);
		Date curdate=new Date();
		String txDate=sf.format(curdate);
		sf=new SimpleDateFormat("hhmmss",Locale.US);
		String txTime=sf.format(curdate);
		String  plaintext=order.getRandOrderID()+"|"+order.getAmount()+"|01|"+txDate+"|"+txTime+"|"+getCorpid()+"|WonderPay|"+order.getOrdernum()+"|"+order.getOrdernum()+"|"+"0"+"|"+this.recurl+"|reapal";
		log.debug("CMBC plainText TO BANK:"+plaintext);
		System.out.println("CMBC plainText TO BANK:"+plaintext);
		String envolopData="";
		try{
			JnkyServer my = new JnkyServer(IOUtils.readFile(this.fileCert).toByteArray(),
					IOUtils.readFile(this.fileKey).toByteArray(),this.keyPassword);
			envolopData = my.EnvelopData(plaintext);//""中为待加密的数据
			my=null;			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}finally{
			sf=null;
			curdate=null;
			txDate=null;
			txTime=null;
			plaintext=null;
		}
		return envolopData;
	}
	
	/**
	 * 产生页面跳转的代码
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException{		
		StringBuffer sf=new StringBuffer("");		
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" name=\"orderinfo\" value=\""+generateSignMsg(order)+"\" >");
		sf.append("</form>");		
		if(log.isDebugEnabled()) log.debug("CMBC:"+sf.toString());
		return sf.toString();
	}	
	/**
	 * 
	 */
	public PayResult getPayResult(HashMap reqs) throws ServiceException{		
		PayResult bean=null;
		try{			
			String v_amount="";
			String billstatus="";
			String envolopData=String.valueOf(reqs.get("payresult"));
			try{
				JnkyServer my1 = new JnkyServer(IOUtils.readFile(this.fileKey).toByteArray(),this.keyPassword);
				String plainText = my1.DecryptData(envolopData);
				my1=null;
				String[] result = plainText.split("\\|");
				v_amount=result[2];		
				billstatus=result[5];
				bean=new PayResult(result[0].trim(),this.bankcode,new BigDecimal(v_amount),"0".equals(billstatus)?1:-1);
				bean.setBankdate(result[3]);			
				result=null;
				v_amount=null;
				billstatus=null;
				plainText=null;
			}catch(Exception e){
				log.error(e.getMessage());
				throw new ServiceException(EventCode.CMBC_VERIFY);			
			}			
				
		}catch(Exception e){
			HandleException.handle(e);
		}
		return bean;
	}
	
	
	/**
	 * @param fileCert The fileCert to set.
	 */
	public void setFileCert(String fileCert) {
		this.fileCert = fileCert;
	}

	/**
	 * @param fileKey The fileKey to set.
	 */
	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}
	
	
}
