/*
 * @Id: CCB.java ����02:10:00 2010-1-15
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
 * Description: ��������
 * 
 */
public class CCB  extends BankExt implements BankService{
	static Logger logger = Logger.getLogger(CCB.class);		
	
	//private static String CCBPOSID="000000000";//�̻���̨�����ɽ���ͳһ���䣬ȱʡΪ000000000		
	private static String CCBCURCODE="01";//���Ҵ���,ȱʡΪ01������� 
	private static String CCBTXCODE="690401";//�����룬�ɽ���ͳһ����Ϊ690401
	private static String CCBREMARK1="V20";//��ע1������������ֱ�Ӵ���������
	private static String CCBREMARK2="";//��ע2������������ֱ�Ӵ���������	
	

	private String ccbposid;
	private String ccbranchid;
	private String pubkey;		
	
	private String generateSignMsg(BankOrder order){
		String CCBText="MERCHANTID="+this.getCorpid()+"&POSID="+this.ccbposid+"&BRANCHID="
		+this.ccbranchid+"&ORDERID="+order.getRandOrderID()+"&PAYMENT="+order.getAmount()
		+"&CURCODE="+CCBCURCODE+"&TXCODE="+CCBTXCODE+"&REMARK1="+CCBREMARK1+"&REMARK2="+CCBREMARK2;
		//+"&PUB32="+this.pubkey.substring(0, 30); //�½ӿ�
		String CCBMAC =  ibsmac.getMD5Mac(CCBText);  //MACУ������ñ�׼MD5�㷨�����̻�ʵ��
		CCBText=null;
		return CCBMAC;
	}
	/**
	 * ����ҳ����ת�Ĵ���
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
		String POSID = String.valueOf(reqs.get("MPOSID"));//�̻���̨����
		String ORDERID = String.valueOf(reqs.get("ORDER_NUMBER"));//������
		String CUSTID = String.valueOf(reqs.get("CUST_ID"));//����ͻ���
		String ACCNO = String.valueOf(reqs.get("ACC_NO"));//�����˺�
		String ACCNAME = String.valueOf(reqs.get("ACC_NAME"));//�����˻�����
		String AMOUNT = String.valueOf(reqs.get("AMOUNT"));//������
		String STATUS = String.valueOf(reqs.get("STATUS"));//֧�����
		String TRANFLAG = String.valueOf(reqs.get("TRAN_FLAG"));//���ʽ
		String TRANTIME = String.valueOf(reqs.get("TRAN_TIME"));//����ʱ��
		String REMARK1 = String.valueOf(reqs.get("REMARK1"));
		String REMARK2 = String.valueOf(reqs.get("REMARK2"));
		String BRANCHNAME = String.valueOf(reqs.get("BRANCH_NAME"));//�����������
		String CHECKOK = String.valueOf(reqs.get("CHECKOK"));//���һ������Ա�Ƿ����ͨ��
		String SIGNSTRING = String.valueOf(reqs.get("SIGNSTRING"));//����ǩ�����ܴ�
		
		
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
			if(reqs.get("ACC_TYPE")!=null){//���к�̨֪ͨ��Ӧ
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
