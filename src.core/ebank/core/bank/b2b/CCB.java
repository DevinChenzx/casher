/*
 * @Id: CCB.java 下午02:10:00 2010-1-15
 * 
 * @author Kitian@chinabank.com.cn
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.b2b;
import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.log4j.Logger;


import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.web.common.util.Validator;

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
	private static String CCBTXCODE="690401";//交易码，由建行统一分配为690401
	private static String CCBREMARK1="V20";//备注1，网银不处理，直接传到城综网
	private static String CCBREMARK2="";//备注2，网银不处理，直接传到城综网	
	

	private String ccbposid;
	private String ccbranchid;
	private String pubkey;		
	
	private String generateSignMsg(BankOrder order){
		String CCBText="MERCHANTID="+this.getCorpid()+"&POSID="+this.ccbposid+"&BRANCHID="
		+this.ccbranchid+"&ORDERID="+order.getRandOrderID()+"&PAYMENT="+order.getAmount()
		+"&CURCODE="+CCBCURCODE+"&TXCODE="+CCBTXCODE+"&REMARK1="+CCBREMARK1+"&REMARK2="+CCBREMARK2;
		//+"&PUB32="+this.pubkey.substring(0, 30); //新接口
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
		sf.append("<input type=\"hidden\" name=\"MAC\" value=\""+generateSignMsg(order)+"\" >");
		sf.append("</form>");		
		if(logger.isDebugEnabled()) logger.debug(sf.toString());
		return sf.toString();
	}		
	
	public PayResult getPayResult(HashMap reqs) throws ServiceException{			
		String POSID = String.valueOf(reqs.get("MPOSID"));//商户柜台代码
		String ORDERID = String.valueOf(reqs.get("ORDER_NUMBER"));//订单号
		String CUSTID = String.valueOf(reqs.get("CUST_ID"));//付款客户号
		String ACCNO = String.valueOf(reqs.get("ACC_NO"));//付款账号
		String ACCNAME = String.valueOf(reqs.get("ACC_NAME"));//付款账户名称
		String AMOUNT = String.valueOf(reqs.get("AMOUNT"));//付款金额
		String STATUS = String.valueOf(reqs.get("STATUS"));//支付结果
		String TRANFLAG = String.valueOf(reqs.get("TRAN_FLAG"));//付款方式
		String TRANTIME = String.valueOf(reqs.get("TRAN_TIME"));//交易时间
		String REMARK1 = String.valueOf(reqs.get("REMARK1"));
		String REMARK2 = String.valueOf(reqs.get("REMARK2"));
		String BRANCHNAME = String.valueOf(reqs.get("BRANCH_NAME"));//付款分行名称
		String CHECKOK = String.valueOf(reqs.get("CHECKOK"));//最后一级复核员是否审核通过
		String SIGNSTRING = String.valueOf(reqs.get("SIGNSTRING"));//数字签名加密串
		
		
		PayResult bean=null;
		try{
			String plain=(Validator.isNull(POSID)?"":POSID)+(Validator.isNull(ORDERID)?"":ORDERID)+(Validator.isNull(CUSTID)?"":CUSTID)+(Validator.isNull(ACCNO)?"":ACCNO)+(Validator.isNull(ACCNAME)?"":ACCNAME)+(Validator.isNull(AMOUNT)?"":AMOUNT)+(Validator.isNull(STATUS)?"":STATUS)+(Validator.isNull(REMARK1)?"":REMARK1)+(Validator.isNull(REMARK2)?"":REMARK2)+(Validator.isNull(TRANFLAG)?"":TRANFLAG)+(Validator.isNull(TRANTIME)?"":TRANTIME)+(Validator.isNull(BRANCHNAME)?"":BRANCHNAME);
			System.out.println(plain);		
			RSASig test1 = new RSASig();
			test1.setPublicKey(pubkey);		
			System.out.println(pubkey);
			boolean verifyresult = test1.verifySigature(SIGNSTRING,plain);					
			if(logger.isDebugEnabled()) logger.debug("CCB:verifyresult="+verifyresult+",SUCCESS="+STATUS+"plain:"+plain);
			String SUCCESS = "";
			if(verifyresult){			
				if(!"5".equals(STATUS)&&!"6".equals(STATUS)){
					SUCCESS="Y";
				}
				bean=new PayResult(ORDERID,this.bankcode,new BigDecimal(AMOUNT),"Y".equals(SUCCESS)?1:-1);
				bean.setEnableFnotice(false);
			}else{
				throw new ServiceException(EventCode.CCB_MD5VERIFY);
			}
			if(reqs.get("ACC_TYPE")!=null){//银行后台通知响应
				logger.info("CCB B2B backbone response..."+plain);
				reqs.put("NR", "SID_"+this.idx);
				reqs.put("RES","success");
			}
			bean.setBankresult(STATUS);			
			
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
