package ebank.core.bank.b2b;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.Logger;


import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class CMB extends BankExt implements BankService
{
	
	private String accNo;
	private String accName;
	private String accBranch;
	private String accPro;
	private String accCity;
	static Logger logger = Logger.getLogger(CMB.class);	

	//10λ��1+8(����)+1λ���
	public String generateOrderID() throws ServiceException{
		String str="";
		Random rd=new Random();
		for(int i=0;i<2-this.prefixnum.length();i++) str+=rd.nextInt(10);	
		rd=null;
		return prefixnum+sequenceservice.getCode().substring(6,14);
	}
	
	public String generateRandom() throws ServiceException{
		String str="";
		Random rd=new Random();
		for(int i=0;i<2-this.prefixnum.length();i++) str+=rd.nextInt(10);			
		rd=null;
		String aa= prefixnum+sequenceservice.getCode().substring(6,14);
		return prefixnum+sequenceservice.getCode().substring(6,14)+aa.substring(0, 5);
	}
	
	public String sendOrderToBank(BankOrder order) throws ServiceException 
	{
		String plain = "MCHNBR="+order.getMerchantid()+" ;"+"REFORD="+order.getRandOrderID()+" ;"+"SUBORD="+generateRandom()+" ;"+
		"CCYNBR=10"+" ;"+"TRSAMT="+order.getAmount()+" ;"+"CRTACC="+accNo+" ;"+"CRTNAM="+accName+" ;"+"CRTBNK="+accBranch+" ;"+
		"CRTPVC="+accPro+" ;"+"CRTCTY="+accCity+" ;"+"CNLDAT="+dateStr(new Date())+" ;"+"KEYACC=N"+" ;"+"ORDCFM=Y"+" ;"+"RETURL="+this.getRecurl();
		
		StringBuffer sf=new StringBuffer("");		
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" id=\"order\" name=\"order\" value=\""+plain+"\">");
		sf.append("</form>");
		return sf.toString();
	}

	@SuppressWarnings("rawtypes")
	public PayResult getPayResult(HashMap reqs) throws ServiceException 
	{
		//��ʼ��resultbean
		PayResult bean=null;	
		try
		{
			String orderNum = "";
			String amount = "";
			String reqStat = "";
			String tranStat = "";
			
			String results = reqs.get("result").toString().replaceAll(" ", "");
			String[] resultsArray = results.split(";");
			if(resultsArray.length>0)
			{
				orderNum = resultsArray[7].substring(resultsArray[7].indexOf("=")+1, resultsArray[7].length());
				amount = resultsArray[9].substring(resultsArray[9].indexOf("=")+1, resultsArray[9].length());
				//Ŀǰֻ��OPR������������״̬
				//INP�����ɷѡ�OPR����ҵ���нɷѾ��졢WCF���ȴ��̻�ȷ�ϡ�CFM��ȷ����ϡ�MRV�����շ������С�FIN�������������
				reqStat = resultsArray[3].substring(resultsArray[3].indexOf("=")+1, resultsArray[3].length());
				//S����ҵ����֧���ɹ������շ�֧���ɹ���B����ҵ����֧���ɹ������շ�֧�����ɹ���M���̻�����������E����ҵ����������H�������������ϡ�F��ʧ��
				tranStat = resultsArray[4].substring(resultsArray[4].indexOf("=")+1, resultsArray[4].length());
			}
			
			if("OPR".equals(reqStat))
			{
				bean=new PayResult(orderNum,this.bankcode,new BigDecimal(amount),"".equals(tranStat)?1:-1);
				bean.setBankresult(reqStat);
				bean.setBankamount(new BigDecimal(amount));
			}
			else{
				throw new ServiceException(EventCode.ORDER_XMLRESOVERL);
			}
		}
		catch(Exception e)
		{
			HandleException.handle(e);
		}
		return bean;	
	}
	
	public String dateStr(Date date)
	{
		Calendar cd = Calendar.getInstance(); 
		cd.setTime(date); 
		int aa = 3; 
		cd.add(Calendar.DATE,aa); 
		Date newDate = cd.getTime();
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
		return dateFmt.format(newDate);
	}
	
	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getAccBranch() {
		return accBranch;
	}

	public void setAccBranch(String accBranch) {
		this.accBranch = accBranch;
	}

	public String getAccPro() {
		return accPro;
	}

	public void setAccPro(String accPro) {
		this.accPro = accPro;
	}
	public String getAccCity() {
		return accCity;
	}

	public void setAccCity(String accCity) {
		this.accCity = accCity;
	}
}
