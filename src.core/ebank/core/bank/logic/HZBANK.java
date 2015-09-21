/*
 * Created on 2004-11-30
 */
package ebank.core.bank.logic;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResource;

import com.hzbank.netpay.b2cAPI.HZBANKB2CClient;
import com.hzbank.netpay.b2cAPI.HZBANKSetting;

import ebank.core.STEngineService;
import ebank.core.bank.BankService;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;


/**
 * @author 
 * Description: 杭州银行
 * 
 */
public class HZBANK  extends BankExt implements BankService{
	static Logger logger = Logger.getLogger(HZBANK.class);		
	
	private int a = 1;
	private static String path;
	private static String spath;
	private static String repParams;
	
	public static void setRepParams(String repParams) {
		HZBANK.repParams = repParams;
	}

	public static void setSpath(String spath) {
		HZBANK.spath = spath;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private static String fmt = "yyyyMMdd";
	private static String fmtT = "HHmmss";
	
	
	private STEngineService engineService;
	
	public STEngineService getEngineService() {
		return engineService;
	}

	public void setEngineService(STEngineService engineService) {
		this.engineService = engineService;
	}
	
	public void init()
	{
		HZBANKB2CClient aa = new HZBANKB2CClient();
		try{
			a = aa.initialize(new ClassPathResource("B2CMerchant.xml").getInputStream());
			System.out.println(aa.getLastErr()+"========================================");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//10位（1+8(序列)+1位随机
	public String generateOrderID() throws ServiceException{
		String str="";
		Random rd=new Random();
		for(int i=0;i<2-this.prefixnum.length();i++) str+=rd.nextInt(10);	
		rd=null;
		return prefixnum+sequenceservice.getCode().substring(6,14);
	}
	
	/**
	 * 产生页面跳转的代码
	 */
	public String sendOrderToBank(BankOrder order)throws ServiceException
	{
		String orderDate = dateStr(new Date(),fmt);
		String orderTime = dateStr(new Date(),fmtT);
		String orderId = order.getRandOrderID();
		
		String plain = "02" + "|" + getCorpid() +  "|" + orderDate + "|" + orderTime + "|" 
			+ orderId + "|" + "00" + "|" + order.getAmount() + "|"
			+"payTest"+"|" + this.getRecurl() + "|" + this.getRecurl()+ "|" + "";
		log.info(plain);
		StringBuffer sf=new StringBuffer("");		
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" name=\"dse_operationName\" value=\"PY8000SrvOp\" >");
		sf.append("<input type=\"hidden\" name=\"merID\" value=\""+getCorpid()+"\" >");
		sf.append("<input type=\"hidden\" name=\"orderId\" value=\""+orderId+"\" >");
		sf.append("<input type=\"hidden\" name=\"orderDate\" value=\""+orderDate+"\" >");
		sf.append("<input type=\"hidden\" name=\"orderTime\" value=\""+orderTime+"\" >");
		sf.append("<input type=\"hidden\" name=\"tranType\" value=\"02\" >");
		sf.append("<input type=\"hidden\" name=\"amount\" value=\""+order.getAmount()+"\" >");
		sf.append("<input type=\"hidden\" name=\"currencyType\" value=\"00\" >");
		sf.append("<input type=\"hidden\" name=\"remark\" value=\"payTest\" >");
		sf.append("<input type=\"hidden\" name=\"pageURI\" value=\""+this.getRecurl()+"\" >");
		sf.append("<input type=\"hidden\" name=\"selfURI\" value=\""+this.getRecurl()+"\" >");
		sf.append("<input type=\"hidden\" name=\"jumpSeconds\" value=\"\" >");
		sf.append("<input type=\"hidden\" name=\"sourceMsg\" value=\""+generateSignMsg(plain)+"\" >");
		sf.append("</form>");
		log.info("sf.toString()::::"+sf.toString());
		return sf.toString();
	}		
	
	@SuppressWarnings("rawtypes")
	public PayResult getPayResult(HashMap reqs) throws ServiceException
	{			
		PayResult bean=null;
		try{
			logger.info("reqsString:==="+reqs.toString());
			Vector vc = verifySign(reqs.get("notifyMsg").toString());
			if(vc!=null)
			{
				bean=new PayResult(vc.get(3).toString(),this.bankcode,new BigDecimal(vc.get(5).toString()),"1".equals(vc.get(7).toString())?1:-1);
				bean.setTransnum(vc.get(3).toString());
				bean.setBankamount(new java.math.BigDecimal(vc.get(5).toString()));
				bean.setBankresult(vc.get(7).toString());
				bean.setBankdate(vc.get(8).toString());
			}
		}catch(Exception e){
			HandleException.handle(e);
		}		
		return bean;
	}
	
	/**
	 * 签名信息
	 * @param plain
	 * @return
	 */
	public String generateSignMsg(String plain)
	{
		String signMsg = "";
		init();
		if(a==0)
		{
			com.hzbank.netpay.b2cAPI.NetSignServer nss = new com.hzbank.netpay.b2cAPI.NetSignServer();
		    String  merchantDN = HZBANKSetting.MerchantCertDN;
		    try{
				nss.NSSetPlainText(plain.getBytes("GBK"));
			} 
		    catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}
		    byte bSignMsg [] = nss.NSDetachedSign(merchantDN);
		    if (nss.getLastErrnum() < 0) {
		       log.info("ERROR:商户端签名失败");
		       signMsg = "";
		       return signMsg;
		    }
		    try {
				signMsg = new String(bSignMsg, "GBK");
			} 
		    catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else{
			log.info("ERROR:商户端签名初始化失败");
			signMsg = "";
		    return signMsg;
		}
		
		return signMsg;
	}
	
	/**
	 * 验证签名信息
	 * @param sign
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector verifySign(String sign)
	{
		String notifyMsg = sign;//获取银行通知结果
		int lastIndex = notifyMsg.lastIndexOf("|");
		Vector vc = new Vector();
		String signMsg = notifyMsg.substring(lastIndex+1, notifyMsg.length());//获取签名信息
		String srcMsg = notifyMsg.substring(0, lastIndex+1);
		int veriyCode = -1;
		if(a==0)
		{
			com.hzbank.netpay.b2cAPI.NetSignServer nss = new com.hzbank.netpay.b2cAPI.NetSignServer();
			log.info(nss.getLastErrnum()+"::::=======================================");
			try {
				nss.NSDetachedVerify( signMsg.getBytes("GBK"),srcMsg.getBytes("GBK"));
			} 
			catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}  
			//对通知结果进行验签
			veriyCode = nss.getLastErrnum();
			if (veriyCode<0)
			{
				//验签出错
				vc = null;
				log.info("商户端验证签名失败：return code:"+veriyCode);
				return vc;
			}
			java.util.StringTokenizer stName = new java.util.StringTokenizer(srcMsg, "|");//拆解通知结果到Vector
			int i =0;
			while (stName.hasMoreTokens()) 
			{
				String value = (String) stName.nextElement();
				if (value.equals("")) value ="&nbsp;";
				vc.add(i++,value);
			}
		}else{
			log.info("商户端签名初始化失败::::=========================================");
		}
		
		return vc;
	}
	
	public String dateStr(Date date, String fmt)
	{
		SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
		return dateFmt.format(date);
	}
}
