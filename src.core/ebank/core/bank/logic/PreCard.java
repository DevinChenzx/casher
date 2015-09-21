
package ebank.core.bank.logic;


import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import ebank.core.bank.BankService;

import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.MD5sign;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.web.common.WebConstants;

/**
 * @author 
 * Description: 银联支付新版本
 * 依赖属性文件:
 * 
 */
public class PreCard extends BankExt implements BankService{
	
	static Logger logger = Logger.getLogger(PreCard.class); 		

	private String notifyUrl;
				   
	public String getNotifyUrl() {
		return notifyUrl;
	}



	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}



	@Override
	public String generateOrderID() throws ServiceException {
		String RandOrderID=sequenceservice.getCode();	//yyDDD(6)+8位序列
		Random rd=new Random();
		String str="";	
		int length=this.prefixnum==null?0:this.prefixnum.length();
		for(int i=0;i<4-length;i++) str+=rd.nextInt(10);			
		return prefixnum+str+this.getCorpid().substring(10)+RandOrderID.substring(RandOrderID.length()-7);				

	}


	
	@SuppressWarnings({ "unchecked", "static-access" })
	public String sign(String MerID, String MorderID, String ProductName,String ProductNum,String Amount, String CurCode, String OrderTime, String MerRcvURL,String Ext1,String Ext2) 
	{
		
		List list=new ArrayList();
		list.add("MerID="+MerID);
		list.add("MorderID="+MorderID);
		list.add("ProductName="+ProductName);
		list.add("ProductNum="+ProductNum);
		list.add("Amount="+Amount);
		list.add("CurCode="+CurCode);
		list.add("MerRcvURL="+MerRcvURL);
		list.add("OrderTime="+OrderTime);
		list.add("Ext1="+Ext1);
		list.add("Ext2="+Ext2);
		
		MD5sign t = new MD5sign();
		list=t.ParaFilterList(list);
		String CheckValue=t.BuildMysign(list,WebConstants.KEY); //获得对应商户的签名数据。
		log.debug(CheckValue);
		return CheckValue;
	}
	/**
	 * 产生页面跳转的代码
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException{
		//产生订单号
		
		
		String SignType = (String)order.getMp().get("sign_type");	
		if(SignType.equals("MD5")){
			SignType="01";
			
		}
		
		String version = "1.0.0";
		
		String amt= Amount.getFormatAmount(order.getAmount(), 0);
		String TransDate= new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String ProductName = (String)order.getMp().get("subject");
		//String notifyUrl = (String)order.getMp().get("notifyUrl");
		//String returnUrl = (String)order.getMp().get("returnUrl");
		
		String ext1 = (String)order.getMp().get("bodys");
		String ProductNum=String.valueOf(order.getMp().get("ProductNum"));
		String CurCode="CNY";
		
		String ext2 = "";
		StringBuffer sf=new StringBuffer("");	
		String orderID=order.getRandOrderID();//本地订单号
		String outTradeNo=(String)order.getMp().get("outTradeNo");//商户订单号
		
		//String action="http://10.68.76.96/init";
		//String action="https://paygate.wonderpay.net/init";
		sf.append("<form name=sendOrder  METHOD=\"get\" action=\""+this.getDesturl()+"\">");
		//sf.append("<form name=sendOrder  METHOD=\"get\" action=\""+action+"\">");
		sf.append("<input type=\"hidden\" name=\"SignType\" value=\""+ SignType +"\"/>");
		sf.append("<input type=\"hidden\" name=\"Version\" value=\""+ version +"\"/>");
		sf.append("<input type=\"hidden\" name=\"ProductName\" value=\""+ProductName+"\"/>");
		sf.append("<input type=\"hidden\" name=\"ProductNum\" value=\""+ProductNum+"\"/>");
		sf.append("<input type=\"hidden\" name=\"CurCode\" value=\""+CurCode+"\"/>");
		sf.append("<input type=\"hidden\" name=\"MerID\" value=\""+order.getMerchantid()+"\"/>");
		sf.append("<input type=\"hidden\" name=\"MorderID\" value=\""+outTradeNo+"\"/>");//商户订单号
		sf.append("<input type=\"hidden\" name=\"OutSysNo\" value=\""+orderID+"\"/>");//传本地订单号
		sf.append("<input type=\"hidden\" name=\"Amount\" value=\""+amt+"\"/>");
		sf.append("<input type=\"hidden\" name=\"OrderTime\" value=\""+TransDate+"\"/>");
		/*sf.append("<input type=\"hidden\" name=\"MerRcvURL\" value=\""+this.getRecurl()+"\"/>");
		sf.append("<input type=\"hidden\" name=\"NotifyURL\" value=\""+this.getNotifyUrl()+"\"/>");*/
		sf.append("<input type=\"hidden\" name=\"MerRcvURL\" value=\""+this.getRecurl()+"\"/>");
		sf.append("<input type=\"hidden\" name=\"NotifyURL\" value=\""+this.getNotifyUrl()+"\"/>");
		sf.append("<input type=\"hidden\" name=\"Ext1\"  value=\""+ ext1 + "\"/>");
		//sf.append("<input type=\"hidden\" name=\"Ext2\" value=\""+ ext2 + "\"/>");
		sf.append("<input type=\"hidden\" name=\"SignMsg\" value=\""+sign(order.getMerchantid(),outTradeNo,ProductName,ProductNum,amt,CurCode,TransDate,this.getRecurl(), ext1, ext2)+"\"/>");
		sf.append("</form>");		
		if(logger.isDebugEnabled()) logger.debug("str to unionpay:"+sf.toString());
		return sf.toString();
	}	
	
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
}
