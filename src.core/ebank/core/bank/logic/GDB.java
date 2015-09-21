
package ebank.core.bank.logic;
import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import gfbank.payment.merchant.SignAndVerify;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.Logger;

/**
 * @author 
 * Description: 广东发展银行服务
 * 
 */
public class GDB  extends BankExt implements BankService{
	static Logger logger = Logger.getLogger(GDB.class); 
	private String fileCert;
	
	private String generateSignMsg(BankOrder order){
		String envolopData="";
		try{
			String signdata = this.getCorpid() + order.getRandOrderID() + order.getAmount();		//'合并源数据
			if(logger.isDebugEnabled()) logger.debug("signdata:"+signdata);			
			envolopData=SignAndVerify.sign_md(signdata,"");	//'执行签名packetSign(String as[]),并获得签名结果=String sign_md(String s, String s1)返回值
			signdata=null;					
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return envolopData;
	}
	//16位订单
	/* (non-Javadoc)
	 * @see ebank.core.bank.logic.BankExt#generateOrderID()
	 */
	public String generateOrderID() throws ServiceException{
		String RandOrderID=sequenceservice.getCode();	//yyDDD(6)+8位序列			
		Random rd=new Random();
		String str="";			
		for(int i=0;i<2-this.prefixnum.length();i++) str+=rd.nextInt(10);
		rd=null;		
		return prefixnum+RandOrderID+str;
	}
	
	/**
	 * 产生页面跳转的代码
	 */
	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#sendOrderToBank(ebank.core.domain.BankOrder)
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException{		
		StringBuffer sf=new StringBuffer("");		
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" name=\"merchid\" value=\""+getCorpid()+"\" >");
		sf.append("<input type=\"hidden\" name=\"orderid\" value=\""+order.getRandOrderID()+"\" >");
		sf.append("<input type=\"hidden\" name=\"amount\" value=\""+order.getAmount()+"\" >");		
		sf.append("<input type=\"hidden\" name=\"sign\" value=\""+generateSignMsg(order)+"\" >");
		sf.append("<input type=\"hidden\" name=\"returl\" value=\""+this.recurl+"\" >");
		sf.append("</form>");		
		if(logger.isDebugEnabled()) logger.debug(sf.toString());
		return sf.toString();
	}
	
	
	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#getPayResult(java.util.HashMap)
	 */
	public PayResult getPayResult(HashMap reqs) throws ServiceException{		
		PayResult bean=null;
		try{
			String amount    = String.valueOf(reqs.get("amount"));
			String orderid   = String.valueOf(reqs.get("orderid"));
			String sequence  = String.valueOf(reqs.get("sequence"));
			String orderdate = String.valueOf(reqs.get("orderdate"));
			String retcode   = String.valueOf(reqs.get("success"));
			String merchid   = String.valueOf(reqs.get("merchid"));
			String crypt = String.valueOf(reqs.get("crypt"));			
			String signdata= orderid + amount + orderdate + retcode + sequence + merchid;
			
			String status=SignAndVerify.verify_md(signdata, crypt,this.fileCert);	//'执行验证签名动作verify_md(String s, String s1, String s2)
			
			if(logger.isDebugEnabled()) logger.info("from GDB sign:"+signdata+" file:"+this.fileCert+" status:"+status);
			
			if(status.equals("0")){//'验证签名通过，并且银行返回支付成功信息（RC000）
				bean=new PayResult(orderid,this.bankcode,new BigDecimal(amount),"RC000".equals(retcode)?1:-1);					
			}else{
				throw new ServiceException(EventCode.GDB_VERIFY);
			}				
			bean.setBankdate(orderdate);
			bean.setBanktransseq(sequence);	
			
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
	
	
}
