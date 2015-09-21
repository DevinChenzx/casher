/*
 * Created on 2004-11-30
 */
package ebank.core.bank.logic;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.Logger;


import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

import beartool.RSASig;
import beartool.ibsmac;


/**
 * @author 
 * Description: 建设银行
 * 
 */
public class CCB  extends BankExt implements BankService{
	static Logger logger = Logger.getLogger(CCB.class);		
	
	//private static String CCBPOSID="000000000";//商户柜台代码由建行统一分配，缺省为000000000		
	private static String CCBCURCODE="01";//货币代码,缺省为01－人民币 
	private static String CCBTXCODE="520100";//交易码，由建行统一分配为520100
	private static String CCBREMARK1="";//备注1，网银不处理，直接传到城综网
	private static String CCBREMARK2="";//备注2，网银不处理，直接传到城综网	
	private static String TYPE="1";//接口类型，0- 非钓鱼接口1- 防钓鱼接口目前该字段以银行开关为准，如果有该字段则需要传送以下字段
	private static String REFERER="";//防钓鱼
	private static String GATEWAY="";//网关类型
	private static String REGINFO="";//客户注册信息
	private static String PROINFO="";//商品信息
	
	
	

	private String ccbposid;
	private String ccbranchid;
	private String pubkey;	
	
	
	private String generateSignMsg(BankOrder order){		
		String CCBText="MERCHANTID="+this.getCorpid()+"&POSID="+this.ccbposid+"&BRANCHID="
		+this.ccbranchid+"&ORDERID="+order.getRandOrderID()+"&PAYMENT="+order.getAmount()
		+"&CURCODE="+CCBCURCODE+"&TXCODE="+CCBTXCODE+"&REMARK1="+CCBREMARK1+"&REMARK2="+CCBREMARK2
		+"&TYPE="+TYPE+"&PUB="+this.pubkey.substring(this.pubkey.length()-30)+"&GATEWAY="+GATEWAY+"&CLIENTIP="+order.getCustip()+"&REGINFO="+REGINFO+"&PROINFO="+PROINFO+"&REFERER="+REFERER; //新接口
		
		if(logger.isDebugEnabled()) logger.debug(CCBText);
		String CCBMAC =  ibsmac.getMD5Mac(CCBText);  //MAC校验域采用标准MD5算法，由商户实现
		CCBText=null;
		return CCBMAC;
	}
	/**
	 * 产生页面跳转的代码
	 */
	public String sendOrderToBank(BankOrder order)throws ServiceException{		
		StringBuffer sf=new StringBuffer("");		
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" name=\"MERCHANTID\" value=\""+this.getCorpid()+"\" >");
		sf.append("<input type=\"hidden\" name=\"POSID\" value=\""+this.ccbposid+"\" >");
		sf.append("<input type=\"hidden\" name=\"BRANCHID\" value=\""+this.ccbranchid+"\" >");
		sf.append("<input type=\"hidden\" name=\"ORDERID\" value=\""+order.getRandOrderID()+"\" >");
		sf.append("<input type=\"hidden\" name=\"PAYMENT\" value=\""+order.getAmount()+"\" >");

		sf.append("<input type=\"hidden\" name=\"CURCODE\" value=\""+CCBCURCODE+"\" >");
		sf.append("<input type=\"hidden\" name=\"TXCODE\" value=\""+CCBTXCODE+"\" >");
		sf.append("<input type=\"hidden\" name=\"REMARK1\" value=\""+CCBREMARK1+"\" >");
		sf.append("<input type=\"hidden\" name=\"REMARK2\" value=\""+CCBREMARK2+"\" >");
		sf.append("<input type=\"hidden\" name=\"TYPE\" value=\""+TYPE+"\">");
		sf.append("<input type=\"hidden\" name=\"GATEWAY\" value=\""+GATEWAY+"\">");
		sf.append("<input type=\"hidden\" name=\"CLIENTIP\" value=\""+order.getCustip()+"\">");
		sf.append("<input type=\"hidden\" name=\"REGINFO\" value=\""+REGINFO+"\">");
		sf.append("<input type=\"hidden\" name=\"PROINFO\" value=\""+PROINFO+"\">");
		sf.append("<input type=\"hidden\" name=\"REFERER\" value=\""+REFERER+"\">");
		sf.append("<input type=\"hidden\" name=\"MAC\" value=\""+generateSignMsg(order)+"\" >");
		sf.append("</form>");		
		if(logger.isDebugEnabled()) logger.debug(sf.toString());
		return sf.toString();
	}		
	
	public PayResult getPayResult(HashMap reqs) throws ServiceException{			
		String POSID = String.valueOf(reqs.get("POSID"));
		String BRANCHID = String.valueOf(reqs.get("BRANCHID"));
		String ORDERID = String.valueOf(reqs.get("ORDERID"));
		String PAYMENT = String.valueOf(reqs.get("PAYMENT"));
		String CURCODE = String.valueOf(reqs.get("CURCODE"));
		String REMARK1 = String.valueOf(reqs.get("REMARK1"));
		if(REMARK1==null||"null".equals(REMARK1)){REMARK1="";}		
		String REMARK2 = String.valueOf(reqs.get("REMARK2"));
		if(REMARK2==null||"null".equals(REMARK2)){REMARK2="";}
		String SUCCESS = String.valueOf(reqs.get("SUCCESS"));
		String REFERER = String.valueOf(reqs.get("REFERER"));
		String TYPE = String.valueOf(reqs.get("TYPE"));
		String CLIENTIP = String.valueOf(reqs.get("CLIENTIP"));
		String SIGN = String.valueOf(reqs.get("SIGN"));
		PayResult bean=null;
		try{
			String ip=String.valueOf(reqs.get("RemoteIP"));//对方IP
			if(logger.isDebugEnabled()) logger.debug("CCB IP:"+ip);	
			String plain="POSID="+POSID+"&BRANCHID="+BRANCHID+"&ORDERID="+ORDERID+"&PAYMENT="+PAYMENT+"&CURCODE="+CURCODE+"&REMARK1="+REMARK1+"&REMARK2="+REMARK2;
			if(reqs.get("ACC_TYPE")!=null){
				plain+="&ACC_TYPE="+String.valueOf(reqs.get("ACC_TYPE"))+"&SUCCESS="+SUCCESS;
			}else{
				plain+="&SUCCESS="+SUCCESS;
			}	
			plain+="&TYPE="+TYPE+"&REFERER="+REFERER+"&CLIENTIP="+CLIENTIP;
			RSASig test1 = new RSASig();
			test1.setPublicKey(pubkey);				
			boolean verifyresult = test1.verifySigature(SIGN,plain);			
			if(logger.isDebugEnabled()) logger.debug("CCB:verifyresult="+verifyresult+",SUCCESS="+SUCCESS+"plain:"+plain);
			if(verifyresult){				
				bean=new PayResult(ORDERID,this.bankcode,new BigDecimal(PAYMENT),"Y".equals(SUCCESS)?1:-1);
				bean.setEnableFnotice(false);
			}else{
				throw new ServiceException(EventCode.CCB_MD5VERIFY);
			}
			if(reqs.get("ACC_TYPE")!=null){//银行后台通知响应
				logger.info("CCB backbone response..."+plain);
				reqs.put("NR", "SID_"+this.idx);
				reqs.put("RES",plain);
			}			
			bean.setCurrency("01".equals(CURCODE)?"CNY":CURCODE);
			bean.setBankresult(String.valueOf(verifyresult));
						
			
		}catch(Exception e){
			HandleException.handle(e);
		}		
		return bean;
	}

	/**
	 * @param ccbranchid The ccbranchid to set.
	 */
	public void setCcbranchid(String ccbranchid) {
		this.ccbranchid = ccbranchid;
	}	

	/**
	 * @param pubkey The pubkey to set.
	 */
	public void setPubkey(String pubkey) {
		this.pubkey = pubkey;
	}
	/**
	 * @param ccbposid The ccbposid to set.
	 */
	public void setCcbposid(String ccbposid) {
		this.ccbposid = ccbposid;
	}
	

	
	
}
