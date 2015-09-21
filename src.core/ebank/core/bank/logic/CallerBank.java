/*
 * @Id: CallerBank.java 15:50:59 2006-12-11
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.util.HashMap;
import java.util.Random;


import ebank.core.bank.BankService;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

/**
 * @author 
 * Description: 电话银行载体
 * 
 */
public class CallerBank extends BankExt implements BankService{
	
	public String generateOrderID() throws ServiceException{
		String RandOrderID=sequenceservice.getCode();	//yyDDD(6)+8位序列			
		Random rd=new Random();
		String str="";			
		for(int i=0;i<2-this.prefixnum.length();i++) str+=rd.nextInt(10);
		rd=null;		
		return prefixnum+RandOrderID+str;
	}

	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#getPayResult(java.util.HashMap)
	 */
	public PayResult getPayResult(HashMap reqs) throws ServiceException {		
		return null;
	}
	

	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#sendOrderToBank(ebank.core.domain.BankOrder)
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		StringBuffer sf=new StringBuffer();
		sf.append("<body onload=\"javascript:document.sendOrder.submit();\">");
		sf.append("<form name=\"sendOrder\" action=\"/Caller.do\" method=post>");
		//加密一次信息显示
		String plain=order.getOrdernum()+"="+order.getRandOrderID()+"="+order.getAmount()+"="+order.getPayaccount();
		String info=CryptUtil.encrypt(plain);
		//取货信息
		String xids=CryptUtil.encrypt(order.getOrdernum()+"="+order.getRandOrderID()); 
		sf.append("<input type=\"hidden\" name=\"info\" value=\""+info+"\"/>");
		sf.append("<input type=\"hidden\" name=\"xids\" value=\""+xids+"\"/>");
		sf.append("<input type=\"hidden\" name=\"servicecode\" value=\""+order.getBankID()+"\"/></form>");
		//取货地址加密
		return sf.toString();
	}
	

	

}
