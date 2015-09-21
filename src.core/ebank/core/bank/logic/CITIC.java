/*
 * @Id: CITIC.java 下午03:13:51 2008-12-31
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.XmlUtils;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.core.remote.JniProxyService;

/**
 * @author xiexh
 * Description: 中信银行
 * 
 */
public class CITIC extends BankExt implements BankService{

	private Log log=LogFactory.getLog(this.getClass());
	private JniProxyService jniproxy;
	
	private String userid;
	private String pswd;
	//12位订单号
	public String generateOrderID() throws ServiceException{
		String RandOrderID=sequenceservice.getCode().substring(6,14);	//yyDDDD(6)+8位序列			
		Random rd=new Random();
		String str="";			
		for(int i=0;i<7-this.prefixnum.length();i++) str+=rd.nextInt(10);
		rd=null;		
		return prefixnum+RandOrderID+str;
	}
	
	
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		String msgs=String.valueOf(reqs.get("SIGNMSG"));
		PayResult result=null;
		if(msgs!=null){
			String[] m=msgs.split("\\|");
			if(m!=null&&m.length==2){	
				try {
					String designMsg = this.decrypt(m[1]);
					if(designMsg==null) throw new ServiceException();
					log.debug("CITIC:"+designMsg);				

					String STATUS_ID = XmlUtils.getNode(designMsg, "STATUS_ID");
					String SHOPNO = XmlUtils.getNode(designMsg, "SHOPNO");
					String ORDERNO = XmlUtils.getNode(designMsg, "ORDERNO");
					
					if(!(this.corpid.equals(m[0])&&this.corpid.equals(SHOPNO))) return null;
					
					String USER_NAME= XmlUtils.getNode(designMsg, "USER_NAME");					
					String AMOUNT = XmlUtils.getNode(designMsg, "AMOUNT");
					String ORDERDATE = XmlUtils.getNode(designMsg, "ORDERDATE");
					String TRNO = XmlUtils.getNode(designMsg, "TRNO");
					String ACC_NO = XmlUtils.getNode(designMsg, "ACC_NO");					
					result=new PayResult(ORDERNO,this.bankcode,new BigDecimal(AMOUNT),
							"1".equals(STATUS_ID)?1:-1);	
					
					result.setBankresult(STATUS_ID);					
					result.setBanktransseq(TRNO);
					result.setPayinfo(USER_NAME+" "+ACC_NO);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}else{
				throw new ServiceException(EventCode.ORDER_PAYNOTFOUND);
			}
		}
		return result;
	}

	public String sendOrderToBank(BankOrder order) throws ServiceException {
		String memo=order.getOrdernum();
		if(memo.length()>20) memo=memo.substring(0,20);
		String plain=
		"<?xml version=\"1.0\" encoding=\"GB2312\"?>"+            
        "<root txcode=\"2a1100\">"+
	    "<SHOPURL>"+this.getRecurl()+"</SHOPURL>"+
	    "<USERID>"+this.userid+"</USERID>"+
	    "<PSWD>"+this.pswd+"</PSWD>"+
	    "<SHOPNO>"+this.corpid+"</SHOPNO>"+
	    "<ORDERDATE>"+order.getPostdate().substring(0,8)+"</ORDERDATE>"+
	    "<ORDERTIME>"+order.getPostdate().substring(8)+"</ORDERTIME>"+
	    "<ORDERNO>"+order.getRandOrderID()+"</ORDERNO>"+
	    "<AMOUNT>"+order.getAmount()+"</AMOUNT>"+	    
	    "<MEMO>"+memo+"</MEMO>"+	
        "</root>"; 
		log.info(plain);
		System.out.println("plain:"+plain);
		String	sign=sign(plain);
		
		StringBuffer sf=new StringBuffer("");		
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" name=\"SIGNMSG\" value=\""+sign+"\" >");
		sf.append("<input type=\"hidden\" name=\"txcode\" value=\"2a1400\" >");
		sf.append("</form>");	
		System.out.println("sf:"+sf.toString());
		return sf.toString();
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}
	public String sign(String plain){
		return jniproxy.getCrypt(plain,this.idx,null);
	}

	public void setJniproxy(JniProxyService jniproxy) {
		this.jniproxy = jniproxy;
	}
	public String decrypt(String plain){
		return jniproxy.decrypt(plain,this.idx,null);
	}
	
	

}
