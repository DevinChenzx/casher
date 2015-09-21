/*
 * Created on 2004-11-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

/**
 * 深圳发展银行
 * @author 
 * Description: 银行接口
 * 
 */
public class SDB  extends BankExt implements BankService{
	private Logger log = Logger.getLogger(this.getClass());
		
	@SuppressWarnings("unused")
	private String[] generateSignMsg(BankOrder order){
	    String[] macs=new String[2];
	    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String amount = order.getAmount();
	    StringBuffer origData = new StringBuffer();
	    origData.append("masterid=");
	    origData.append(this.getCorpid());
	    origData.append("|");
	    origData.append("orderid=");
	    origData.append(order.getRandOrderID());
	    origData.append("|");	    
	    origData.append("currency=RMB");
	    origData.append("|");
	    origData.append("amount=");
	    origData.append(amount);
	    origData.append("|");
		origData.append("date=");
		long ll=System.currentTimeMillis();
		origData.append(df.format(new java.util.Date(ll)));
		origData.append("|");	    
	    origData.append("timestamp=");	    
	    origData.append(ll);
	    origData.append("|");
		origData.append("remark=test");
		origData.append("|");
		origData.append("orderpaytype=");
		macs[0]=origData.toString();
		String sign = com.sdb.payment.core.MerchantSignVerify.merchantSignData_ABA( macs[0] );	
		macs[1]=sign;
		
		return macs;
	}
	/**
	 * 产生页面跳转的代码
	 */
	@SuppressWarnings("static-access")
	public String sendOrderToBank(BankOrder order)throws ServiceException{		
		String orig = "masterid="+this.getCorpid()+"|orderid="+order.getRandOrderID()+"|currency=RMB|amount="+order.getAmount()+"|timestamp="+System.currentTimeMillis()+"|remark=wonderpay|orderpaytype=A";
		com.sdb.payment.core.MerchantSignVerify util = new com.sdb.payment.core.MerchantSignVerify("merchantB2C");
		String sign = util.merchantSignData_ABA(orig);
		StringBuffer sb = new StringBuffer();
		sb.append("<form name=\"sendOrder\" method=\"post\" action=\""+this.getDesturl()+"\">");
		sb.append("<input type=hidden name=\"orig\" value=\""+orig+"\">");
		sb.append("<input type=hidden name=\"sign\" value=\""+sign+"\">");
		sb.append("<input type=hidden name=\"returnurl\" value=\""+this.getRecurl()+"\">");
		sb.append("<input type=hidden name=\"NOTIFYURL\" value=\""+this.getHttprecurl()+"&NR=SID_"+this.idx+"\">");
		sb.append("<input type=hidden name=\"transName\" value=\"paygate\" >");
		sb.append("</form>");
		log.info("sb.toString():"+sb.toString());
		return sb.toString();
	}
		
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PayResult getPayResult(HashMap reqs) throws ServiceException{
		
		String orig=String.valueOf(reqs.get("orig"));
		String sign=String.valueOf(reqs.get("sign"));
		PayResult bean=null;
		try{
			log.info("reqs:"+reqs);
			String[] result=orig.split("\\|");
			Map mp = new HashMap();
			for (int i = 0; i < result.length; i++) {
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
			if(com.sdb.payment.core.MerchantSignVerify.merchantVerifyPayGate_ABA(sign,orig))//签名验证通过
			{
				if("0".equals(String.valueOf(mp.get("status"))))
					bean = new PayResult(String.valueOf(mp.get("orderid")),this.bankcode,new BigDecimal(String.valueOf(mp.get("amount"))),1);
				else if("1".equals(String.valueOf(mp.get("status"))))
					bean = new PayResult(String.valueOf(mp.get("orderid")),this.bankcode,new BigDecimal(String.valueOf(mp.get("amount"))),-1);
				bean.setBankresult(String.valueOf(mp.get("status")));
				
				String bankDate = "";
				long date = Long.valueOf(String.valueOf(mp.get("timestamp")));
				Date dateTime = new Date(date);
				bankDate = new SimpleDateFormat("yyyyMMdd").format(dateTime);
				bean.setBankdate(bankDate); //清算日期
			}else{
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);				
			}
			log.info(String.valueOf(reqs.get("NR")));
			if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(String.valueOf(reqs.get("NR")))){
				 reqs.put("RES","200");
			}
		}catch (Exception e) {
			HandleException.handle(e);
		}
		return bean;
	}

	
}
