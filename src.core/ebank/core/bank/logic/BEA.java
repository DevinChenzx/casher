/*
 * @Id: BEA.java 上午10:07:06 2008-12-9
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.hkbea.netpay.b2cAPI.BEAB2CClient;
import com.hkbea.netpay.b2cAPI.BEAB2COPReply;
import com.hkbea.netpay.b2cAPI.BEASetting;
import com.hkbea.netpay.b2cAPI.NetSignServer;
import com.hkbea.netpay.b2cAPI.OpResult;

import ebank.core.bank.BankService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

/**
 * @author 
 * Description: 东亚银行
 * 
 */
public class BEA extends BankExt implements BankService{

	Logger log = Logger.getLogger(this.getClass());
	
	private static String path;	
	
	private static BEAB2CClient client = new BEAB2CClient();
	
	private BEAB2CClient getClient() throws ServiceException{		
		 try {			
			 int ret=client.initialize(path);
			 
			 if (client==null||ret != 0) { //初始化失败
			        log.error("BEA ini failure,path:"+path+" error:"+client.getLastErr());
			        throw new ServiceException(EventCode.EXE_ACCESSTIMEOUT);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	 
		return client;
	}
	
	
	public PayResult getPayResult(HashMap request) throws ServiceException {
		String notifyMsg = String.valueOf(request.get("notifyMsg"));//获取银行通知结果
		if(notifyMsg==null) throw new ServiceException(EventCode.WEB_PARAMNULL);
		int lastIndex = notifyMsg.lastIndexOf("|");
		String signMsg = notifyMsg.substring(lastIndex+1, notifyMsg.length());//获取签名信息
		String srcMsg = notifyMsg.substring(0, lastIndex+1);	
		log.info("BEA Result:"+notifyMsg);
		//301310053110018|99990001|100.00|CNY|14124|412444|20060729|120902|0000052|1|10.21|2|||QWR+QWRQWEQD+G\RQWRFVBNJKFSFAF
		String[] s=srcMsg.split("\\|");		
		if(s==null||s.length!=13) throw new ServiceException(EventCode.WEB_PARAMNULL);		
				
		PayResult bean=null;
		if(verify(srcMsg, this.bankcode, signMsg)){
			bean=new PayResult(s[4],this.bankcode,new BigDecimal(s[6]).movePointLeft(2),"0".equals(s[9])?1:-1);
			bean.setEnableFnotice(false);
			bean.setBanktransseq(s[12]);			
			if(this.idx.equals(request.get("NR"))){
				request.put("RES",s[1]);
			}
		}else{
			throw new ServiceException(EventCode.SIGN_VERIFY);
		}
		return bean;		
	}

	public String sendOrderToBank(BankOrder order) throws ServiceException {
		      
		String merID=this.corpid ;                
		String orderid=order.getRandOrderID();         
		String orderDate=order.getPostdate().substring(0,8);             
		String orderTime=order.getPostdate().substring(8,14);             
		String tranType="02"; //B2C              
		String amount=String.valueOf(new BigDecimal(order.getAmount()).movePointRight(2));                
		String curType=order.getCurrency();               
		String orderContent=order.getOrdernum();      //商家填写的其他订单信息，在个客户页面显示    
		if(orderContent.length()>20) orderContent=orderContent.substring(0, 20);
		String orderMono=order.getOrdernum();             
		           
		String selfURI=this.getRecurl()+"&NR="+this.idx;            //后台      
		String pageURI=this.getRecurl();  //页面跳转            
		String jumpSeconds="3";   
		String  cardType="";
	
			
		String  sourceMsg = tranType+"|";
		if("".equals(cardtype))
			sourceMsg+=cardType+"|";
	    sourceMsg+=merID + "|" + orderDate + "|" + orderTime + "|"+ orderid  + "|"  + curType +  "|"  + amount + "|" 
		+ orderContent + "|" + orderMono  + "|" + pageURI + "|" + selfURI + "|" + jumpSeconds  ;

		log.info(sourceMsg);
		String signMsg=getCrypt(sourceMsg, this.bankcode, null);
		
	    StringBuffer sf=new StringBuffer();
	    sf.append("<form name=sendOrder method=\"post\" action=\""+BEASetting.OrderURL+"\">");	   
	    sf.append("<input  type=\"hidden\" name=\"merID\"  value=\""+merID+"\">");
 	    sf.append("<input  type=\"hidden\" name=\"orderid\" value=\""+orderid+"\">");
        sf.append("<input  type=\"hidden\" name=\"orderDate\" value=\""+orderDate+"\">");
		sf.append("<input  type=\"hidden\" name=\"orderTime\" value=\""+orderTime+"\">");
		sf.append("<input  type=\"hidden\" name=\"tranType\" value=\""+tranType+"\" >");
	    sf.append("<input  type=\"hidden\" name=\"amount\" value=\""+amount+"\">");
		sf.append("<input  type=\"hidden\" name=\"currencyType\" value=\""+curType+"\">");
		sf.append("<input  type=\"hidden\" name=\"orderMono1\" value=\""+orderContent+"\">");
		sf.append("<input  type=\"hidden\" name=\"orderMono2\" value=\""+orderMono+"\">");
	
		sf.append("<input  type=\"hidden\" name=\"selfURI\" value=\""+selfURI+"\">");
		sf.append("<input  type=\"hidden\" name=\"pageURI\" value=\""+pageURI+"\">");
		sf.append("<input  type=\"hidden\" name=\"jumpSeconds\" value=\""+jumpSeconds+"\">");
		if(!"".equals(cardtype))
		  sf.append("<input  type=\"hidden\" name=\"cardType\" value=\""+cardType+"\">");//1.网银 2.shoppingcard 3.信用卡
		sf.append("<input  type=\"hidden\" name=\"merSignMsg\" value=\""+signMsg+"\">");
	    sf.append("</form>");
		return sf.toString();
	}
	private String getCrypt(String plain,String bankcode,String cmd) throws ServiceException{
		getClient();		
		NetSignServer nss = new NetSignServer();					
		String  merchantDN = BEASetting.MerchantCertDN;	
		log.info(merchantDN);
		String signMsg=null;
		try {
			nss.NSSetPlainText(plain.getBytes("GBK"));
			byte bSignMsg[] = nss.NSDetachedSign(merchantDN);
			signMsg=new String(bSignMsg); 			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return signMsg;
	}
	private boolean verify(String plain,String bankcode,String cmd) throws ServiceException{
		getClient();
		NetSignServer nss = new NetSignServer();
		try {
			nss.NSDetachedVerify(cmd.getBytes("GBK"),plain.getBytes("GBK"));
			log.debug(String.valueOf(nss.getLastErrnum()));
			if(nss.getLastErrnum()>=0) return true;			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return false;
	}

	
	
	public void setPath(String path) {
		this.path = path;
		
	}
	

}
