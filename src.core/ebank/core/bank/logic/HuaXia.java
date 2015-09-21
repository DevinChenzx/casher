/*
 * @Id: HuaXia.java 14:04:14 2006-8-23
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.log4j.Logger;

//import com.eitop.mer.api.CertController;
import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.core.remote.JniProxyService;

/**
 * @author 
 * Description: ��������
 * 
 */
public class HuaXia extends BankExt implements BankService{
	private String iniFile;
	private String terminal;
	private String serverip;
	private JniProxyService jniproxy;
	
	Logger log = Logger.getLogger(this.getClass());

	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#getPayResult(java.util.HashMap)
	 */
	public PayResult getPayResult(HashMap request) throws ServiceException {
		String signedData = String.valueOf(request.get("transResult"));
		log.debug("from Huaxia��"+signedData);	
		if(signedData==null) return null;
//		��ʼ���ܴ�����ȡ���з��ص�ԭ����Ϣ
		try{
	        //��ʼ��
			//CertController encrypt = new CertController(iniFile);
	        //����
			String plain = jniproxy.decrypt(signedData,this.bankcode,null);//encrypt.decryptAndVerifySignData(signedData);
			//������|�̼Һ�|���׽��|��������|����ʱ��|����״̬
			//������|�̻�����|���׽�� |��������|����ʱ��|֧������|����״̬
			if(plain!=null){
				String[] str=plain.split("\\|");
				log.debug("Huaxia:"+plain);
				if(str!=null&&str.length>=6&&"00".equals(str[str.length-1])&&this.corpid.equals(str[1])){
					PayResult result=new PayResult(str[0]);
					result.setTrxsts(1);
					result.setBankresult(str[str.length-1]);					
					result.setBankamount(new BigDecimal(str[2]).movePointLeft(2));
					return result;
				}							
			}
			else{
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
			}
		
		}catch(Exception ex){
			log.error(ex.getMessage());
			HandleException.handle(ex);			
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#sendOrderToBank(ebank.core.domain.BankOrder)
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException {
			String split="|";
		    StringBuffer sf=new StringBuffer();		    
			sf.append(order.getRandOrderID());
			sf.append(split);
			sf.append(this.getCorpid());
			sf.append(split);
			sf.append(this.terminal);
			sf.append(split);
			sf.append(this.serverip);
			sf.append(split);
			String amount=String.valueOf(new BigDecimal(order.getAmount()).movePointRight(2).intValue());
			sf.append(amount);
			sf.append(split);
			sf.append("156");
			sf.append(split);
			sf.append(order.getPostdate().substring(0,8));
			sf.append(split);
			sf.append(order.getPostdate().substring(8));
			sf.append(split);
			sf.append(this.getRecurl());		   
			String sendToBankData = null;
			try{
				//��ʼ��
				//CertController encrypt= new CertController(this.iniFile);
//			��ȡ���ܺ������
				sendToBankData =jniproxy.getCrypt(sf.toString(),this.bankcode,null);// encrypt.encryptAndSignDataToBank(sf.toString());
				if(log.isDebugEnabled()) log.debug("form:"+sf.toString());
				String form="<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">";
				form+="<input type=\"hidden\" name=\"orderinfo\" value=\""+sendToBankData+"\"/>";				
				form+="</form>";
				return form;
			}catch(Exception ex){
				log.error("HuaXia Error:"+ex);
				throw new ServiceException();
				
			}		
	}

	/**
	 * @param iniFile The iniFile to set.
	 */
	public void setIniFile(String iniFile) {
		this.iniFile = iniFile;
	}

	/**
	 * @param serverip The serverip to set.
	 */
	public void setServerip(String serverip) {
		this.serverip = serverip;
	}

	/**
	 * @param terminal The terminal to set.
	 */
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	/**
	 * @param jniproxy The jniproxy to set.
	 */
	public void setJniproxy(JniProxyService jniproxy) {
		this.jniproxy = jniproxy;
	}
	
	
	
	

}
