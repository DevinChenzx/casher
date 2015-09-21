/*
 * @Id: ABC.java ����02:21:10 2009-7-7
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
 * Description: ũҵ����
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
		String tMerchantTrnxNo           =order.getRandOrderID(); // �̻�������
		String tTrnxAmountStr            =order.getAmount();      // ������� 
		String tAccountDBNo              = this.billaccount;      //�趨�տ�˺�      ����Ҫ��Ϣ��
		String tAccountDBName			 = this.corpname;         //�趨�տ�˻���    ����Ҫ��Ϣ��
		String tAccountDBBank            = this.banknum;               //�趨�տ�˻����������кţ���Ҫ��Ϣ��
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
	
		//2������TrnxInfo����
		TrnxItems tTrnxItems = new TrnxItems();  
		tTrnxItems.addTrnxItem(new TrnxItem(tMerchantTrnxNo,order.getOrderName()==null?"B2B":order.getOrderName(),new Float(tTrnxAmountStr).floatValue(), 1));		

		TrnxRemarks tTrnxRemarks = new TrnxRemarks();
		tTrnxRemarks.addTrnxRemark(new TrnxRemark("������",order.getOrdernum()==null?order.getRandOrderID():order.getOrdernum()));
		tTrnxRemarks.addTrnxRemark(new TrnxRemark("ʱ��",order.getPostdate()));
		tTrnxRemarks.addTrnxRemark(new TrnxRemark("��������","B2B"));
		tTrnxRemarks.addTrnxRemark(new TrnxRemark("����˵��","����"));

		TrnxInfo tTrnxInfo = new TrnxInfo();
		tTrnxInfo.setTrnxOpr("0001");
		tTrnxInfo.setTrnxRemarks(tTrnxRemarks);
		tTrnxInfo.setTrnxItems(tTrnxItems);

		//3������ֱ��֧���������
		FundTransferRequest tFundTransferRequest = new FundTransferRequest();
		tFundTransferRequest.setTrnxInfo(tTrnxInfo);                           //�趨����ϸ��        ����Ҫ��Ϣ��
		tFundTransferRequest.setMerchantTrnxNo(tMerchantTrnxNo);               //�趨�̻����ױ��    ����Ҫ��Ϣ��
		tFundTransferRequest.setTrnxAmount(Double.parseDouble(tTrnxAmountStr));                       //�趨���׽��        ����Ҫ��Ϣ��
		tFundTransferRequest.setTrnxDate(tTrnxDate);                           //�趨��������        ����Ҫ��Ϣ��
		tFundTransferRequest.setTrnxTime(tTrnxTime);                           //�趨����ʱ��        ����Ҫ��Ϣ��
		tFundTransferRequest.setAccountDBNo(tAccountDBNo);                     //�趨�տ�˺�      ����Ҫ��Ϣ��
		tFundTransferRequest.setAccountDBName(tAccountDBName);                 //�趨�տ�˻���    ����Ҫ��Ϣ��
		tFundTransferRequest.setAccountDBBank(tAccountDBBank);                 //�趨�տ�˻����������кţ���Ҫ��Ϣ��
		tFundTransferRequest.setResultNotifyURL(tResultNotifyURL);             //�趨���׽���ش���ַ����Ҫ��Ϣ��
		tFundTransferRequest.setMerchantRemarks(tMerchantRemarks);             //�趨�̻���ע��Ϣ
		 
		//4������ֱ��֧������ȡ��֧����ַ
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
	 * ����ҳ����ת�Ĵ���
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException{
		//����������
		//order.setRandOrderID(generateOrderID());
		StringBuffer sf=new StringBuffer("");
		String strAction=getPAURL(order);
		if(strAction.startsWith("E:")){
			String[] strs=strAction.split("@#@");
			sf.append("�ܱ�Ǹ���й�ũҵ����B2B֧���ӿ�������ά���У����Ժ���֧��<br>");
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
	 * ֧�����
	 * @param ����request�õ��Ĳ���Map
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
			//2���ж�֧�����״̬�����к�������						
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
	 * ��ѯũ�н���
	 * @param trxnum
	 * @param remarks
	 * @return
	 */
	public TrxResponse query(String trxnum,String remarks){
		QueryTrnxRequest tQueryTrnxRequest = new QueryTrnxRequest();
		tQueryTrnxRequest.setMerchantTrnxNo(trxnum);               //�趨�̻����ױ��    ����Ҫ��Ϣ��
		tQueryTrnxRequest.setMerchantRemarks(remarks);             //�趨�̻���ע��Ϣ
		 
		//3�����ͽ��ײ�ѯ����ȡ��֧����ַ
		TrxResponse tTrxResponse = tQueryTrnxRequest.postRequest();
		return tTrxResponse;		
	}
	
	public TrxResponse download(String tMerchantTrnxDate,String remarks){
		DownloadTrnxRequest tDownloadTrnxRequest = new DownloadTrnxRequest();
		tDownloadTrnxRequest.setMerchantTrnxDate(tMerchantTrnxDate);           //�趨�̻����ױ��    ����Ҫ��Ϣ��
		tDownloadTrnxRequest.setMerchantRemarks(remarks);             //�趨�̻���ע��Ϣ
		 
		//3���������ؽ��׼�¼����ȡ��֧����ַ
		TrxResponse tTrxResponse = tDownloadTrnxRequest.postRequest();
		return tTrxResponse;
	}
	
	public TrxResponse print(String trx){
		PrintTrnxVoucherRequest tPrintTrnxVoucherRequest = new PrintTrnxVoucherRequest();
		tPrintTrnxVoucherRequest.setMerchantTrnxNo(trx);               //�趨�̻����ױ��    ����Ҫ��Ϣ��
		//3�����ͽ��ײ�ѯ����ȡ��֧����ַ
		TrxResponse tTrxResponse = tPrintTrnxVoucherRequest.postRequest();
		return tTrxResponse;
		
	}
}

