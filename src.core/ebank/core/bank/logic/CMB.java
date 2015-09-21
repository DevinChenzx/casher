/*
 * Created on 2004-11-30
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import org.apache.log4j.Logger;



import cmb.netpayment.Security;
import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

/**
 * @author xiexh
 * Description: 招商银行
 * 
 */
public class CMB  extends BankExt implements BankService{
	static Logger logger = Logger.getLogger(CMB.class);	
		
	private String cmbBranchID;//商户开户北京分行号，请咨询开户的招商银行分支机构	
	private Security payer;
	
	//10位（1+8(序列)+1位随机
	public String generateOrderID() throws ServiceException{
		String str="";
		Random rd=new Random();
		for(int i=0;i<2-this.prefixnum.length();i++) str+=rd.nextInt(10);			
		rd=null;
		return prefixnum+sequenceservice.getCode().substring(6,14)+str;
	}  	
	
	/**
	 * 产生页面跳转的代码
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException
	{
		//产生订单号
		//order.setRandOrderID(generateOrderID());
		String date=new SimpleDateFormat("yyyyMMdd",Locale.US).format(new Date());
		String merchantcode=cmb.MerchantCode.genMerchantCode(this.getKeyPassword(), date, cmbBranchID, this.getCorpid(), 
				order.getRandOrderID(),order.getAmount(), this.getIdx(), this.recurl, "", "");
		StringBuffer sf=new StringBuffer("");		
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" name=\"cono\" value=\""+this.getCorpid()+"\">");
		sf.append("<input type=\"hidden\" name=\"branchid\" value=\""+cmbBranchID+"\">");
		sf.append("<input type=\"hidden\" name=\"billno\" value=\""+order.getRandOrderID()+"\">");
		sf.append("<input type=\"hidden\" name=\"amount\" value=\""+order.getAmount()+"\">");
		sf.append("<input type=\"hidden\" name=\"date\" value=\""+date+"\">");
		sf.append("<input type=\"hidden\" name=\"MerchantUrl\" value=\""+this.recurl+"\">");  //不带参数
		sf.append("<input type=\"hidden\" name=\"MerchantPara\" value=\""+this.getIdx()+"\">");
		sf.append("<input type=\"hidden\" name=\"MerchantCode\" value=\""+merchantcode+"\">");
		sf.append("</form>");		
		if(logger.isDebugEnabled()) logger.debug("cmb:"+sf.toString());
		return sf.toString();
	}
	
	public PayResult getPayResult(HashMap reqs) throws ServiceException
	{
		
		String CoNo = String.valueOf(reqs.get("CoNo"));
		String BillNo =String.valueOf(reqs.get("BillNo"));
		String Amount = String.valueOf(reqs.get("Amount"));
		String Date = String.valueOf(reqs.get("Date"));
		String Msg = String.valueOf(reqs.get("Msg"));
		String MerchantPara = String.valueOf(reqs.get("MerchantPara"));
		String Signature = String.valueOf(reqs.get("Signature"));
		String Succeed = String.valueOf(reqs.get("Succeed"));
		String ip = String.valueOf(reqs.get("RemoteIP"));		
		PayResult bean=null;
		try{
			String str="";
			if("null".equalsIgnoreCase(MerchantPara)||"null".equalsIgnoreCase(CoNo)){
				str=String.valueOf(reqs.get("queryString"));				
			}else
			    str = "Succeed="+Succeed+"&CoNo="+CoNo+"&BillNo="+BillNo+"&Amount="+Amount+"&Date="+Date+"&MerchantPara="+MerchantPara+"&Msg="+Msg+"&Signature="+Signature;
			
			if(logger.isDebugEnabled()) logger.debug("cmb:"+str+"******"+ip);						
			byte[] bytestr = str.getBytes();				
			//Security pay =new cmb.netpayment.Security(fileKey);
			boolean bRet = payer.checkInfoFromBank(bytestr);
			bRet = true;
			if(bRet){				
				bean=new PayResult(BillNo,this.bankcode,new BigDecimal(Amount),"Y".equals(Succeed)?1:-1);
				bean.setAquiremerchant("null".equalsIgnoreCase(CoNo)?this.getCorpid():CoNo);
			}else{
				throw new ServiceException(EventCode.CMB_MD5VERIFY);
			}
			//msg: 4位分行号＋6位商户号＋8位银行接受交易的日期＋20位银行流水号；
			bean.setBanktransseq(Msg.substring(18)); //银行交易流水
			bean.setBankamount(new BigDecimal(Amount));
			bean.setBankresult(Succeed); //银行返回结果				
			bean.setBankdate(Date);      //银行交易日期			
			
			//bean.setXmls("<seq>"+Msg+"</seq>");			
			
		}catch(Exception e){
			HandleException.handle(e);
		}		
		return bean;
	}

	/**
	 * @param cmbBranchID The cmbBranchID to set.
	 */
	public void setCmbBranchID(String cmbBranchID) {
		this.cmbBranchID = cmbBranchID;
	}

	/**
	 * @param payer The payer to set.
	 */
	public void setPayer(Security payer) {
		this.payer = payer;
	}
	
	
	
	
	
	
}
