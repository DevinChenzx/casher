package ebank.core.bank.b2b;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import beartool.PKCS7Tool;

import ebank.core.STEngineService;
import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class BOC extends BankExt implements BankService {

	private String dn;
	private String keyStorePath;
	private String keyStorePassword;
	private String rootCertificatePath;
	private STEngineService engineService;
	
	private Log log=LogFactory.getLog(this.getClass());

	public String sendOrderToBank(BankOrder order) throws ServiceException {
		
		String plain=this.corpid+"|"+order.getRandOrderID()+"|001|"+order.getAmount()+"|"+order.getPostdate();
		String signData=getSignData(plain);
		if(signData==null) throw new ServiceException(EventCode.BOC_SIGNERROR);
		StringBuffer sf=new StringBuffer();
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" name=\"bocNo\" value=\""+this.corpid+"\" />");  //�̻���
		sf.append("<input type=\"hidden\" name=\"orderNo\" value=\""+order.getRandOrderID()+"\">"); //�̻�������
		sf.append("<input type=\"hidden\" name=\"curCode\" value=\"001\" />");  //��������
		sf.append("<input type=\"hidden\" name=\"orderAmount\" value=\""+order.getAmount()+"\" />");//�������
		sf.append("<input type=\"hidden\" name=\"orderTime\" value=\""+order.getPostdate()+"\" />");//����ʱ��
		sf.append("<input type=\"hidden\" name=\"orderUrl\" value=\""+this.getRecurl()+"\">");//�����̻�ҳ��
		String orderNote=order.getOrdernum();
		if(orderNote.length()>30) orderNote=orderNote.substring(0, 29);
		else orderNote=order.getRandOrderID();
		sf.append("<input type=\"hidden\" name=\"orderNote\" value=\""+orderNote+"\" />");	//����˵��
		sf.append("<input type=\"hidden\" name=\"signData\" value=\""+signData+"\">");
		sf.append("</form>");
		log.info("BOC b2b:"+sf.toString());
		return sf.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		
		log.info(reqs);
		String tranSeq = String.valueOf(reqs.get("tranSeq"));//������ˮnumber
		String tranCode = String.valueOf(reqs.get("tranCode"));//��������
		String tranStatus = String.valueOf(reqs.get("tranStatus"));//����״̬
		String tranTime = String.valueOf(reqs.get("tranTime"));//����ʱ��
		String payAmount = String.valueOf(reqs.get("orderAmount"));//�������number
		String feeAmount = String.valueOf(reqs.get("feeAmount"));//��������number
		String curCode = String.valueOf(reqs.get("curCode"));//����
		
		String bocNo = String.valueOf(reqs.get("bocNo"));//�����̻���
		String orderSeq = String.valueOf(reqs.get("orderSeq"));//������ˮ
		String orderNo = String.valueOf(reqs.get("orderNo"));//�̻�������
		
		String signData = String.valueOf(reqs.get("signData"));
		//������ˮ|��������|����״̬|����ʱ��|���׽��|���׷���|�̻���|�̻�������|����|
		String plain=tranSeq+"|"+tranCode+"|"+tranStatus+"|"+tranTime+"|"+payAmount+"|"+feeAmount+"|"+bocNo+"|"+orderNo+"|"+curCode;		
		PayResult bean=null;		
		if(signData!=null&&!"null".equalsIgnoreCase(signData)){
			if(verify(signData,plain)){ //֧���ɹ�
				if("1".equals(tranStatus)){
					bean=new PayResult(orderNo,this.bankcode,new BigDecimal(payAmount),
					           "1".equals(tranStatus)?1:-1);
					bean.setBanktransseq(orderSeq);
					bean.setBankresult(tranStatus);					
					bean.setEnableFnotice(false);	
			    }else{
			    	throw new ServiceException("501125"); //֧��ʧ��
			    }
		    }else{
		    	throw new ServiceException(EventCode.CRYPT_VALIADATESIGN); //��֤ǩ��ʧ��
		    }
			if(this.idx.equals(reqs.get("NR"))){
				reqs.put("RES",orderSeq+orderNo);
			}				
		}			
		return bean;
	}
	
	/***
	 * �õ�������֤URL
	 * @return String
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	public String getRcvURL(HashMap reqs){
		String strOutput="notfrombank";		
		try{		
			PayResult result=this.getPayResult(reqs);
			if(result!=null&&result.getTrxsts()==1){
				strOutput="CBMD5";
				String state=result.getTrxsts()==1?Constants.STATE_STR_OK:Constants.STATE_STR_FAIL;	
				engineService.post(result);	
			}			
		}catch(Exception e){
			e.printStackTrace();
		}
		return this.recurl+(this.recurl.indexOf("?")>0?"&":"?")+"v_md5info="+strOutput;
	}
	
	public String getSignData(String plain){
		try {
			String keypass = this.keyPassword;
			return PKCS7Tool.getSigner(this.keyStorePath, this.keyStorePassword, keypass).sign(plain.getBytes());																			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean verify(String signData,String plain){
		try {
			PKCS7Tool tools=PKCS7Tool.getVerifier(rootCertificatePath);
			if(tools!=null){
				tools.verify(signData, plain.getBytes(), this.dn);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	public String getRootCertificatePath() {
		return rootCertificatePath;
	}
	public void setRootCertificatePath(String rootCertificatePath) {
		this.rootCertificatePath = rootCertificatePath;
	}
	public STEngineService getEngineService() {
		return engineService;
	}
	public void setEngineService(STEngineService engineService) {
		this.engineService = engineService;
	}
}
