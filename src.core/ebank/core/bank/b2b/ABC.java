/*
 * @Id: ABC.java 下午02:21:10 2009-7-7
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.b2b;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.apache.log4j.Logger;


import com.hitrust.b2b.trustpay.client.*;
import com.hitrust.b2b.trustpay.client.b2b.*;

import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;



/**
 * @author 
 * Description: 农业银行
 * 
 */
public class ABC extends BankExt implements BankService{
	static Logger logger = Logger.getLogger(ABC.class); 
	private String banknum;
	
	public String getBanknum() {
		return banknum;
	}

	public void setBanknum(String banknum) {
		this.banknum = banknum;
	}

	private String getPAURL(BankOrder order){
		String tMerchantTrnxNo           =order.getRandOrderID(); // 商户订单号
		String tTrnxAmountStr            =order.getAmount();      // 订单金额 
		String tAccountDBNo              = this.billaccount;      //设定收款方账号      （必要信息）
		String tAccountDBName			 = this.corpname;         //设定收款方账户名    （必要信息）
		String tAccountDBBank            = this.banknum;               //设定收款方账户开户行联行号（必要信息）
		String tResultNotifyURL          = this.getRecurl();
		String tMerchantRemarks          = "B2B";
		
		Date curdate=null;
		
		try {
			curdate=new SimpleDateFormat("yyyyMMddHHmmss").parse(order.getPostdate());
		} catch (Exception e) {
			logger.warn("paser date exception,use sysdate");
			curdate=new Date();
		}		
		String tTrnxDate                 = new SimpleDateFormat("yyyy/MM/dd").format(curdate);
		String tTrnxTime                 = new SimpleDateFormat("HH:mm:ss").format(curdate);
	
		//2、生成TrnxInfo对象
		TrnxItems tTrnxItems = new TrnxItems();  
		tTrnxItems.addTrnxItem(new TrnxItem(tMerchantTrnxNo,order.getOrderName()==null?"B2B":order.getOrderName(),new Float(tTrnxAmountStr).floatValue(), 1));		

		TrnxRemarks tTrnxRemarks = new TrnxRemarks();
		tTrnxRemarks.addTrnxRemark(new TrnxRemark("订单号",order.getOrdernum()==null?order.getRandOrderID():order.getOrdernum()));
		tTrnxRemarks.addTrnxRemark(new TrnxRemark("时间",order.getPostdate()));
		tTrnxRemarks.addTrnxRemark(new TrnxRemark("交易类型","B2B"));
		tTrnxRemarks.addTrnxRemark(new TrnxRemark("其它说明","吉高"));

		TrnxInfo tTrnxInfo = new TrnxInfo();
		tTrnxInfo.setTrnxOpr("0001");
		tTrnxInfo.setTrnxRemarks(tTrnxRemarks);
		tTrnxInfo.setTrnxItems(tTrnxItems);

		//3、生成直接支付请求对象
		FundTransferRequest tFundTransferRequest = new FundTransferRequest();
		tFundTransferRequest.setTrnxInfo(tTrnxInfo);                           //设定交易细项        （必要信息）
		tFundTransferRequest.setMerchantTrnxNo(tMerchantTrnxNo);               //设定商户交易编号    （必要信息）
		tFundTransferRequest.setTrnxAmount(Double.parseDouble(tTrnxAmountStr));                       //设定交易金额        （必要信息）
		tFundTransferRequest.setTrnxDate(tTrnxDate);                           //设定交易日期        （必要信息）
		tFundTransferRequest.setTrnxTime(tTrnxTime);                           //设定交易时间        （必要信息）
		tFundTransferRequest.setAccountDBNo(tAccountDBNo);                     //设定收款方账号      （必要信息）
		tFundTransferRequest.setAccountDBName(tAccountDBName);                 //设定收款方账户名    （必要信息）
		tFundTransferRequest.setAccountDBBank(tAccountDBBank);                 //设定收款方账户开户行联行号（必要信息）
		tFundTransferRequest.setResultNotifyURL(tResultNotifyURL);             //设定交易结果回传网址（必要信息）
		tFundTransferRequest.setMerchantRemarks(tMerchantRemarks);             //设定商户备注信息
		 
		//4、传送直接支付请求并取得支付网址
		TrxResponse tTrxResponse = tFundTransferRequest.postRequest();
		
		String PAURL="";
		if (tTrxResponse.isSuccess()) {
			PAURL=tTrxResponse.getValue("PaymentURL");
		}else{
			PAURL="E:"+tTrxResponse.getReturnCode()+"@#@"+tTrxResponse.getErrorMessage();
		}		
		return PAURL;
	}
	
	/**
	 * 产生页面跳转的代码
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException{
		//产生订单号
		//order.setRandOrderID(generateOrderID());
		StringBuffer sf=new StringBuffer("");
		String strAction=getPAURL(order);
		if(strAction.startsWith("E:")){
			String[] strs=strAction.split("@#@");
			sf.append("很抱歉，中国农业银行B2B支付接口正升级维护中，请稍后尝试支付<br>");
			sf.append("ReturnCode   = [" + strs[0] + "]<br>");
			sf.append("ErrorMessage = [" + strs[1] + "]<br>");
		}else{			
			sf.append("<form name=sendOrder method=\"post\" action=\""+strAction+"\"></form>");			
			if(logger.isDebugEnabled()) logger.debug("abc-B2B action url="+strAction);			
		}
		strAction=null;		
		return sf.toString();
	}
	
	/***
	 * 支付结果
	 * @param 根据request得到的参数Map
	 * @return 
	 */
	@SuppressWarnings("rawtypes")
	public PayResult getPayResult(HashMap reqs) throws ServiceException
	{		
		PayResult bean=null;
		boolean bln=false;
		String msg=String.valueOf(reqs.get("MSG"));
		logger.info("reqs: "+reqs);
		logger.info("MSG: "+msg);
//	    msg="PE1TRz48TWVzc2FnZT48VHJ4UmVzcG9uc2U+PE1lcmNoYW50SUQ+MjEyMDk5OTAwODMyQjAxPC9NZXJjaGFudElEPjxDb3Jwb3JhdGlvbkN1c3RvbWVyTm8+MTE5OTk3NTM5MjM8L0NvcnBvcmF0aW9uQ3VzdG9tZXJObz48TWVyY2hhbnRUcm54Tm8+MzExMzA0MDMwMDEyNzM3MTcxPC9NZXJjaGFudFRybnhObz48VHJueFNOPjkwMTMwNDAzMTUyNDIzMTY2NTA8L1RybnhTTj48VHJueFR5cGU+RlVORF9UUkFOU0ZFUjwvVHJueFR5cGU+PFRybnhBTVQ+MTM5MDA4PC9Ucm54QU1UPjxPcmdpbmFsRnJlZXplTm8+IDwvT3JnaW5hbEZyZWV6ZU5vPjxGcmVlemVObz4gPC9GcmVlemVObz48QWNjb3VudE5vPjExLTA1MDEwMTA0MDA1NjE4MTwvQWNjb3VudE5vPjxBY2NvdW50TmFtZT6xsb6puN+3ybuq1LbJzMOz09DP3rmry748L0FjY291bnROYW1lPjxBY2NvdW50QmFuaz6xsb6pt9bQ0DwvQWNjb3VudEJhbms+PEFjY291bnREQk5vPjAyLTE0MDkwMTA0MDAwMDA5MzwvQWNjb3VudERCTm8+PEFjY291bnREQk5hbWU+zOy98sjZs8zN+MLnv8a8vNPQz965q8u+PC9BY2NvdW50REJOYW1lPjxBY2NvdW50REJCYW5rPszsvfK31tDQPC9BY2NvdW50REJCYW5rPjxUcm54VGltZT4yMDEzLTQtMyAxNToyNDoyMzwvVHJueFRpbWU+PFRybnhTdGF0dXM+MjwvVHJueFN0YXR1cz48UmV0dXJuQ29kZT4wMDAwPC9SZXR1cm5Db2RlPjwvVHJ4UmVzcG9uc2U+PC9NZXNzYWdlPjxTaWduYXR1cmUtQWxnb3JpdGhtPlNIQTF3aXRoUlNBPC9TaWduYXR1cmUtQWxnb3JpdGhtPjxTaWduYXR1cmU+M0JQbzl2elBjZXo4UWE5S2lrVTEzT2dsb2U0R1RrQU9zTUU1dzRYb05HOCtkenF2b1o2OTRyTnpHbkJPdVpkNEpjVE50V21sYzY2TDdwUWdNMFIyajhLc0dSS2Nnb2JRVDBJWHhUUTdIWVFoSVBUWVI4UHVyazR6OVJ2bXdCejJaVVlTTEt6N2lGT0w0TGFCYXhBV0xVMHpPTHlXZS9ZaGFEOFBBbmM4NUh3PTwvU2lnbmF0dXJlPjwvTVNHPg==";
		try{
			TrnxResult tResult = new TrnxResult(msg);
			String OrderNo = tResult.getValue("MerchantTrnxNo");
			String Amount = tResult.getValue("TrnxAMT");	
			
			System.out.println("OrderNo:"+OrderNo);
			System.out.println("Amount:"+Amount);
			//2、判断支付结果状态，进行后续操作						
			bln=tResult.isSuccess();			
			bean=new PayResult(OrderNo,this.bankcode,new BigDecimal(Amount),bln?1:-1);
			if(bln){
				bean.setBankresult(tResult.getValue("TrnxStatus"));	
				bean.setBanktransseq(tResult.getValue("TrnxSN"));
				bean.setBankdate(tResult.getValue("TrnxTime").substring(0, 8));
			}
		}catch(Exception e){
			HandleException.handle(e);
		}
		return bean;
	}
	
	public static String toString(Object obj){
		Method[] methods=obj.getClass().getMethods();	
		String str=obj.getClass().getName()+"->";
		for (int i = 0; i < methods.length; i++) {
			Method method=methods[i];
			try {
				String methodname=method.getName();				
				if(methodname.startsWith("get")){				
					String value=(String)method.invoke(obj,null);
					str+=methodname+"["+value+"]";					
				}
			} catch (Exception e) {	
				
			}			
		}
		return str;
	}	
	/**
	 * 查询农行交易
	 * @param trxnum
	 * @param remarks
	 * @return
	 */
	public TrxResponse query(String trxnum,String remarks){
		QueryTrnxRequest tQueryTrnxRequest = new QueryTrnxRequest();
		tQueryTrnxRequest.setMerchantTrnxNo(trxnum);               //设定商户交易编号    （必要信息）
		tQueryTrnxRequest.setMerchantRemarks(remarks);             //设定商户备注信息
		 
		//3、传送交易查询请求并取得支付网址
		TrxResponse tTrxResponse = tQueryTrnxRequest.postRequest();
		return tTrxResponse;		
	}
	
	public TrxResponse download(String tMerchantTrnxDate,String remarks){
		DownloadTrnxRequest tDownloadTrnxRequest = new DownloadTrnxRequest();
		tDownloadTrnxRequest.setMerchantTrnxDate(tMerchantTrnxDate);           //设定商户交易编号    （必要信息）
		tDownloadTrnxRequest.setMerchantRemarks(remarks);             //设定商户备注信息
		 
		//3、传送下载交易记录请求并取得支付网址
		TrxResponse tTrxResponse = tDownloadTrnxRequest.postRequest();
		return tTrxResponse;
	}
	
	public TrxResponse print(String trx){
		PrintTrnxVoucherRequest tPrintTrnxVoucherRequest = new PrintTrnxVoucherRequest();
		tPrintTrnxVoucherRequest.setMerchantTrnxNo(trx);               //设定商户交易编号    （必要信息）
		//3、传送交易查询请求并取得支付网址
		TrxResponse tTrxResponse = tPrintTrnxVoucherRequest.postRequest();
		return tTrxResponse;
		
	}
}

