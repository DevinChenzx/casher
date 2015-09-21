
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.Logger;


import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.core.remote.JniProxyService;


/**
 * @author 
 * Description: 交通银行
 * 
 */

/**
 * @author 
 * Description: 交通银行1.0
 * 
 */
public class COMM  extends BankExt implements BankService{
	static Logger logger = Logger.getLogger(COMM.class);

	private String prefixnum;	
	private JniProxyService jniproxy;
	private JniProxyService jniHttpProxy;
	
	
	/* (non-Javadoc)
	 * @see ebank.core.bank.service.BankService#generateOrderID()
	 */
	public String generateOrderID() throws ServiceException{
		String RandOrderID=sequenceservice.getCode().substring(6,14);	//yyMMdd(6)+8位序列			
		Random rd=new Random();
		String str="";			
		for(int i=0;i<3-this.prefixnum.length();i++) str+=rd.nextInt(10);
		rd=null;		
		return prefixnum+RandOrderID+str;
	}		

	/**
	 * 产生页面跳转的代码
	 */
	public String sendOrderToBank(BankOrder order)throws ServiceException
	{	
		String interfaceVersion="1.0.0.0";      
		String merID=this.corpid ;                
		String orderid=order.getRandOrderID();         
		String orderDate=order.getPostdate().substring(0,8);             
		String orderTime=order.getPostdate().substring(8,14);             
		String tranType="0"; //B2C              
		String amount=order.getAmount();                
		String curType=order.getCurrency();               
		String orderContent=order.getOrdernum();      //商家填写的其他订单信息，在个客户页面显示    
		String orderMono=order.getOrdernum();             
		String phdFlag="0";    //不配送          
		String notifyType="1"; //1通知             
		String merURL=this.getRecurl()+"&NR=SID_"+this.idx;            //后台      
		String goodsURL=this.getRecurl();  //页面跳转            
		String jumpSeconds="5";           
		String payBatchNo=order.getPostdate().substring(8);           
		String proxyMerName="";          
		String proxyMerType="";          
		String proxyMerCredentials="";   
		String netType="0"; 	
		String  sourceMsg = interfaceVersion + "|" + merID + "|" + orderid + "|" + orderDate + "|" + orderTime + "|" + tranType + "|" + amount + "|" + curType + "|" + orderContent + "|" + orderMono + "|" + phdFlag + "|" + notifyType + "|" + merURL + "|" + goodsURL + "|" + jumpSeconds + "|" + payBatchNo + "|" + proxyMerName + "|" + proxyMerType + "|" + proxyMerCredentials + "|" + netType;

		//String signMsg=jniproxy.getCrypt(sourceMsg, this.bankcode, null);
		String signMsg=jniHttpProxy.getCrypt(sourceMsg, this.bankcode, null);
		
	    StringBuffer sf=new StringBuffer();
	    sf.append("<form name=sendOrder method=\"post\" action=\""+this.desturl+"\">");
	    sf.append("<input  type=\"hidden\" name=\"interfaceVersion\" value=\""+interfaceVersion+"\">");
	    sf.append("<input  type=\"hidden\" name=\"merID\"  value=\""+merID+"\">");
 	    sf.append("<input  type=\"hidden\" name=\"orderid\" value=\""+orderid+"\">");
        sf.append("<input  type=\"hidden\" name=\"orderDate\" value=\""+orderDate+"\">");
		sf.append("<input  type=\"hidden\" name=\"orderTime\" value=\""+orderTime+"\">");
		sf.append("<input  type=\"hidden\" name=\"tranType\" value=\""+tranType+"\" >");
	    sf.append("<input  type=\"hidden\" name=\"amount\" value=\""+amount+"\">");
		sf.append("<input  type=\"hidden\" name=\"curType\" value=\""+curType+"\">");
		sf.append("<input  type=\"hidden\" name=\"orderContent\" value=\""+orderContent+"\">");
		sf.append("<input  type=\"hidden\" name=\"orderMono\" value=\""+orderMono+"\">");
		sf.append("<input  type=\"hidden\" name=\"phdFlag\" value=\""+phdFlag+"\" >");
		sf.append("<input  type=\"hidden\" name=\"notifyType\" value=\""+notifyType+"\">");
		sf.append("<input  type=\"hidden\" name=\"merURL\" value=\""+merURL+"\">");
		sf.append("<input  type=\"hidden\" name=\"goodsURL\" value=\""+goodsURL+"\">");
		sf.append("<input  type=\"hidden\" name=\"jumpSeconds\" value=\""+jumpSeconds+"\">");
		sf.append("<input  type=\"hidden\" name=\"payBatchNo\" value=\""+payBatchNo+"\">");
		sf.append("<input  type=\"hidden\" name=\"proxyMerName\" value=\""+proxyMerName+"\">");
		sf.append("<input  type=\"hidden\" name=\"proxyMerType\" value=\""+proxyMerType+"\">");
		sf.append("<input  type=\"hidden\" name=\"proxyMerCredentials\" value=\""+proxyMerCredentials+"\">");
		sf.append("<input  type=\"hidden\" name=\"netType\" value=\""+netType+"\" >");
		sf.append("<input  type=\"hidden\" name=\"merSignMsg\" value=\""+signMsg+"\">");
	    sf.append("</form>");
		return sf.toString();
	}
	
	public PayResult getPayResult(HashMap request)throws ServiceException {				
		String notifyMsg = String.valueOf(request.get("notifyMsg"));//获取银行通知结果
		if(notifyMsg==null) throw new ServiceException(EventCode.WEB_PARAMNULL);
		int lastIndex = notifyMsg.lastIndexOf("|");
		String signMsg = notifyMsg.substring(lastIndex+1, notifyMsg.length());//获取签名信息
		String srcMsg = notifyMsg.substring(0, lastIndex+1);	
		logger.info("COM Result:"+notifyMsg);
		//301310053110018|99990001|100.00|CNY|14124|412444|20060729|120902|0000052|1|10.21|2|||QWR+QWRQWEQD+G\RQWRFVBNJKFSFAF
		System.out.println("srcMsg"+srcMsg);
		String[] s=srcMsg.split("\\|");
		if(s==null||(s.length!=16&&s.length!=17)) throw new ServiceException(EventCode.WEB_PARAMNULL);		
				
		PayResult bean=null;
		if(jniHttpProxy.verify(srcMsg, this.bankcode, signMsg)){
			bean=new PayResult(s[1],this.bankcode,new BigDecimal(s[2]),"1".equals(s[9])?1:-1);
			bean.setEnableFnotice(false);
			bean.setBanktransseq(s[8]);
			bean.setBankresult(s[9]);			
			if(("SID_"+this.idx).equals(request.get("NR"))){
				request.put("RES",s[1]);
			}
		}else{
			throw new ServiceException(EventCode.SIGN_VERIFY);
		}
		return bean;
	}

	/**
	

	/**
	 * @param prefixnum The prefixnum to set.
	 */
	public void setPrefixnum(String prefixnum) {
		this.prefixnum = prefixnum;
	}

	public void setJniHttpProxy(JniProxyService jniHttpProxy) {
		this.jniHttpProxy = jniHttpProxy;
	}

	public void setJniproxy(JniProxyService jniproxy) {
		this.jniproxy = jniproxy;
	}
	

	
	
	
	
	
	
}
