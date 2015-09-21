
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import com.EasyLink.OpenVendorV34.NetTran;

import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

/**
 * @author 
 * Description: 银联支付新版本
 * 依赖属性文件:
 * 
 */
public class UNIONPAY extends BankExt implements BankService{
	static Logger logger = Logger.getLogger(UNIONPAY.class); 		
	
	private String sendCertFile;       	//发送方证书主题(商户证书)	
	private String keyPassword;         //发送方私钥
	private String recCertFile;       	//接收方证书主题(银联证书)	
	private static NetTran objNetTran=null;
	static{
		objNetTran=new NetTran();
	}	
	private String[] generateSignMsg(BankOrder order){
		String[] strSignMsgs=new String[2];
		try{
			boolean ret;	
			Date myDate = new Date();		
			String CurrCode	= "CNY";		//货币代码，值为：CNY
			String ResultMode  = "0";				//支付结果返回方式(0-成功和失败支付结果均返回；1-仅返回成功支付结果)
			String Reserved01 	= new SimpleDateFormat("yyyyMMdd").format(myDate);				//保留域1
			String Reserved02 	= "";				//保留域2
			String SourceText 	= "MerId=" + this.getCorpid() + "&" +
					  "OrderNo="+ order.getRandOrderID() + "&" +
					  "OrderAmount=" + order.getAmount() + "&" +
					  "CurrCode=" + CurrCode + "&" +
					  "CallBackUrl=" + this.getRecurl() + "&" +
					  "ResultMode=" + ResultMode + "&" +
					  "Reserved01=" + Reserved01 + "&" +
					  "Reserved02=" + Reserved02;
		  
		   //对原始信息进行加密  
		   ret=objNetTran.EncryptMsg(SourceText,getRecCertFile());   
		   if(ret==true){
		       strSignMsgs[0]=objNetTran.getLastResult();
		   }else{
			   logger.info("encryt error:"+objNetTran.getLastErrMsg());
		   }
		   //对原始信息进行签名
		   ret=objNetTran.SignMsg(SourceText,getSendCertFile(),getKeyPassword());   
		   if(ret==true){
		       strSignMsgs[1]=objNetTran.getLastResult();
		   }else{
			   logger.debug("sign error:"+objNetTran.getLastErrMsg());
		   }
		   if(logger.isDebugEnabled()){
			  logger.debug("src text:"+SourceText);
			  logger.debug("加密后的信息: "+strSignMsgs[0]+"<br>");
			  logger.debug("签名后的信息: "+strSignMsgs[1]+"<br>");		
		   }
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return strSignMsgs;
	}	

	/**
	 * 产生页面跳转的代码
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException{
		//产生订单号
		//order.setRandOrderID(generateOrderID());
		String[] paras=generateSignMsg(order);
		StringBuffer sf=new StringBuffer("");		
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");		
		sf.append("<input type=\"hidden\" name=\"EncodeMsg\" value=\""+paras[0]+"\">");
		sf.append("<input type=\"hidden\" name=\"SignMsg\" value=\""+paras[1]+"\">");		
		sf.append("</form>");		
		paras=null;
		if(logger.isDebugEnabled()) logger.debug("str to unionpay:"+sf.toString());
		return sf.toString();
	}	
	
	public PayResult getPayResult(HashMap reqs) throws ServiceException{
		
		PayResult bean=null;
		String EncodeMsg = String.valueOf(reqs.get("EncodeMsg"));
		String SignMsg = String.valueOf(reqs.get("SignMsg"));
		try{
			boolean ret=objNetTran.DecryptMsg(EncodeMsg,this.getSendCertFile(),this.getKeyPassword());
			String DecryptedMsg="";
			if(ret==true){
		   		DecryptedMsg=objNetTran.getLastResult();
		   	}
			String OrderNo=getContent(DecryptedMsg,"OrderNo");		   	
		   	String PayNo=getContent(DecryptedMsg,"PayNo");		   	
		   	String PayAmount=getContent(DecryptedMsg,"PayAmount");		   
		   	String CurrCode=getContent(DecryptedMsg,"CurrCode");		   
		   	String SystemSSN=getContent(DecryptedMsg,"SystemSSN");		   
		   	String RespCode=getContent(DecryptedMsg,"RespCode");		   	
		   	String SettDate=getContent(DecryptedMsg,"SettDate");		   
		   	String Reserved01=getContent(DecryptedMsg,"Reserved01");		   
		   	String Reserved02=getContent(DecryptedMsg,"Reserved02");		   	
			ret = objNetTran.VerifyMsg(SignMsg, DecryptedMsg,this.getRecCertFile());			
					
			if(ret==true){
				bean=new PayResult(OrderNo,this.bankcode,new BigDecimal(PayAmount),"00".equals(RespCode)?1:-1);				
				bean.setBankdate(SettDate);
				bean.setCurrency(CurrCode);
				bean.setBanktransseq(PayNo);
				bean.setBankresult(SystemSSN+" "+RespCode);
			}else{
				throw new ServiceException(EventCode.CMB_MD5VERIFY);
			}
			
		}catch(Exception e){
			HandleException.handle(e);
		}
		return bean;
	}
	
	//获取参数
	private String getContent(String input, String para){
	    if (input.equals("") || para.equals("")){
	      return "";
	    }
	    String vv = "";
	    StringTokenizer st = new StringTokenizer(input, "&");
	    while (st.hasMoreElements()){
	    	vv = st.nextToken(); 
	    	if (vv.indexOf(para) != -1 && vv.substring(0,vv.indexOf("=") ).equals(para)) {
	    		vv = vv.substring(vv.indexOf("=") + 1);
	    		return vv;
	    	}
	    }
	    return "";
  }

	/**
	 * @return Returns the keyPassword.
	 */
	public String getKeyPassword() {
		return keyPassword;
	}

	/**
	 * @param keyPassword The keyPassword to set.
	 */
	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}

	/**
	 * @return Returns the recCertFile.
	 */
	public String getRecCertFile() {
		return recCertFile;
	}

	/**
	 * @param recCertFile The recCertFile to set.
	 */
	public void setRecCertFile(String recCertFile) {
		this.recCertFile = recCertFile;
	}

	/**
	 * @return Returns the sendCertFile.
	 */
	public String getSendCertFile() {
		return sendCertFile;
	}

	/**
	 * @param sendCertFile The sendCertFile to set.
	 */
	public void setSendCertFile(String sendCertFile) {
		this.sendCertFile = sendCertFile;
	}
	
	
	
	
}
