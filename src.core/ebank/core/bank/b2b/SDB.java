package ebank.core.bank.b2b;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.Logger;

import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class SDB extends BankExt implements BankService {

	private Logger log = Logger.getLogger(this.getClass());
	
	//15位订单
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
	
	public String sendOrderToBank(BankOrder order) throws ServiceException 
	{
		String orig = "masterid="+this.getCorpid()+"|orderid="+order.getRandOrderID()+"|currency=RMB|amount="+order.getAmount()+"|timestamp="+System.currentTimeMillis()+"|validtime=0|remark=wonderpay";
		String sign = com.sdb.payment.core.MerchantSignVerify.merchantSignData_ABA(orig);
		StringBuffer sb = new StringBuffer();
		sb.append("<form name=\"sendOrder\" method=\"post\" action=\""+this.getDesturl()+"\">");
		sb.append("<input type=hidden name=\"orig\" value=\""+orig+"\">");
		sb.append("<input type=hidden name=\"sign\" value=\""+sign+"\">");
		sb.append("<input type=hidden name=\"returnurl\" value=\""+this.getRecurl()+"\">");
		sb.append("<input type=hidden name=\"NOTIFYURL\" value=\""+this.getHttprecurl()+"&NR=SID_"+this.idx+"\">");
		sb.append("<input type=hidden name=\"transName\" value=\"EntPaygate\" >");
		sb.append("</form>");
		log.info("sb.toString():"+sb.toString());
		return sb.toString();
	}

	@SuppressWarnings("rawtypes")
	public PayResult getPayResult(HashMap reqs) throws ServiceException 
	{
		String orig=String.valueOf(reqs.get("orig"));
		String sign=String.valueOf(reqs.get("sign"));
		PayResult bean=null;
		try{
			log.info("reqs:"+reqs);
			String[] result=orig.split("\\|");
			if(log.isDebugEnabled()){
				for (int i = 0; i < result.length; i++) {
					log.info("result"+i+"="+result[i]);
				}
			}
			if(com.sdb.payment.core.MerchantSignVerify.merchantVerifyPayGate_ABA(sign,orig))//签名验证通过
			{
				if("0".equals(String.valueOf(result[0].split("\\=")[1])))
					bean = new PayResult(String.valueOf(result[6].split("\\=")[1]),this.bankcode,new BigDecimal(String.valueOf(result[8].split("\\=")[1])),1);
				else if("1".equals(String.valueOf(result[0].split("\\=")[1])))
					bean = new PayResult(String.valueOf(result[6].split("\\=")[1]),this.bankcode,new BigDecimal(String.valueOf(result[8].split("\\=")[1])),-1);
				bean.setBankresult(String.valueOf(result[0].split("\\=")[1]));
				bean.setBanktransseq(String.valueOf(result[4].split("\\=")[1])); //银行流水
				
				String bankDate = "";
				long date = Long.valueOf(String.valueOf(result[9].split("\\=")[1]));
				Date dateTime = new Date(date);
				bankDate = new SimpleDateFormat("yyyyMMdd").format(dateTime);
				bean.setBankdate(bankDate); //清算日期
			}else{
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);				
			}
			/*log.info(String.valueOf(reqs.get("NR")));
			System.out.println(String.valueOf(reqs.get("NR")));
			if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(String.valueOf(reqs.get("NR")))){
				 reqs.put("RES","200");
			}*/
		}catch (Exception e) {
			HandleException.handle(e);
		}
		return bean;
	}

}
