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
 * Description: ��������
 * 
 */
public class CCB  extends BankExt implements BankService{
	static Logger logger = Logger.getLogger(CCB.class);		
	
	//private static String CCBPOSID="000000000";//�̻���̨�����ɽ���ͳһ���䣬ȱʡΪ000000000		
	private static String CCBCURCODE="01";//���Ҵ���,ȱʡΪ01������� 
	private static String CCBTXCODE="520100";//�����룬�ɽ���ͳһ����Ϊ520100
	private static String CCBREMARK1="";//��ע1������������ֱ�Ӵ���������
	private static String CCBREMARK2="";//��ע2������������ֱ�Ӵ���������	
	private static String TYPE="1";//�ӿ����ͣ�0- �ǵ���ӿ�1- ������ӿ�Ŀǰ���ֶ������п���Ϊ׼������и��ֶ�����Ҫ���������ֶ�
	private static String REFERER="";//������
	private static String GATEWAY="";//��������
	private static String REGINFO="";//�ͻ�ע����Ϣ
	private static String PROINFO="";//��Ʒ��Ϣ
	
	
	

	private String ccbposid;
	private String ccbranchid;
	private String pubkey;	
	
	
	private String generateSignMsg(BankOrder order){		
		String CCBText="MERCHANTID="+this.getCorpid()+"&POSID="+this.ccbposid+"&BRANCHID="
		+this.ccbranchid+"&ORDERID="+order.getRandOrderID()+"&PAYMENT="+order.getAmount()
		+"&CURCODE="+CCBCURCODE+"&TXCODE="+CCBTXCODE+"&REMARK1="+CCBREMARK1+"&REMARK2="+CCBREMARK2
		+"&TYPE="+TYPE+"&PUB="+this.pubkey.substring(this.pubkey.length()-30)+"&GATEWAY="+GATEWAY+"&CLIENTIP="+order.getCustip()+"&REGINFO="+REGINFO+"&PROINFO="+PROINFO+"&REFERER="+REFERER; //�½ӿ�
		
		if(logger.isDebugEnabled()) logger.debug(CCBText);
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
			String ip=String.valueOf(reqs.get("RemoteIP"));//�Է�IP
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
			if(reqs.get("ACC_TYPE")!=null){//���к�̨֪ͨ��Ӧ
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
