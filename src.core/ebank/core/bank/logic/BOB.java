/*
 * @Id: BOB.java 13:38:28 2006-7-17
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.Logger;


import com.bccb.ebusiness.client.MerPaymentInfo;
import com.bccb.ebusiness.client.MerPaymentRequest;
import com.bccb.ebusiness.client.XMLDocument;

import ebank.core.STEngineService;
import ebank.core.bank.BankService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.infosec.NetSignServer;

/**
 * @author xiexh
 * Description: ��������
 * 
 */
public class BOB extends BankExt implements BankService{
	private static Logger log = Logger.getLogger(BOB.class);
	private String merchantDN;
	private String serverDN;		
	private STEngineService engineService;

	//20λ����
	public String generateOrderID() throws ServiceException{
		String RandOrderID=sequenceservice.getCode();	//yyDDD(5)+8λ����			
		Random rd=new Random();
		String str="";			
		for(int i=0;i<5-this.prefixnum.length();i++) str+=rd.nextInt(10);
		rd=null;		
		return prefixnum+RandOrderID+str;
	}
	public String getRcvURL(HashMap reqs){	
		PayResult result=null;
		try{	
			result=this.getPayResult(reqs);
			//���ɹ��������£����ҷ���֧����ַ			
			if(result!=null&&1==result.getTrxsts()){				
				String state=Constants.STATE_STR_OK;				
				engineService.post(result);	
			}			
		}catch(Exception e){
			e.printStackTrace();
		}
		String inf=CryptUtil.encrypt(result.getBankdate()+"="+result.getTransnum());//ordernumʹ�����ڼ򵥴���    		
		return this.recurl+"/PayResult.do?xids="+inf;
		
	}

	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#getPayResult(java.util.HashMap)
	 */
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		 String sign=String.valueOf(reqs.get("bankSignData")); //����ǩ������		 
		 String plain=this.verifySignData(sign,this.serverDN);//��֤ǩ�����ݷ���
		 PayResult bean=null;
		 if(plain!=null){
			 log.debug("from BOB xml = " + plain);
			 XMLDocument resXML = new XMLDocument(plain);
	         String retCode = resXML.getValueAt("retCode").toString().trim();
	         String errMsg = resXML.getValueAt("errMsg").toString().trim();
	         log.debug("retCode:"+retCode+" msg:"+errMsg);
	         if(retCode!=null&&retCode.length()== 4){
	             String orderNum = resXML.getValueAt("orderNum").toString().trim();
	             String orderAmt = String.valueOf(new BigDecimal(resXML.getValueAt("orderAmt").toString()).movePointLeft(2));
	             String orderStatus = resXML.getValueAt("orderStatus").toString().trim();
	             String accDate = resXML.getValueAt("accDate").toString().trim();
	            // successFlag = true;
	             //check orderStatus
	             bean=new PayResult(orderNum,this.bankcode,new BigDecimal(orderAmt),orderStatus.equals("4")?1:-1);
	             bean.setBankresult(retCode+" "+orderStatus);	            
	             bean.setBankdate(accDate);	             
	         }else{
	        	 throw new ServiceException(retCode+" "+errMsg);	        	 
	         }
         }else{
        	//ǩ����֤ʧ��			
			throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);	        
         }
		return bean;
	}

	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#sendOrderToBank(ebank.core.domain.BankOrder)
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException {		
		/*   �����̻���Ϣ����   */
		MerPaymentInfo merPayInfo = new MerPaymentInfo();
		// 1.���ö�����
		merPayInfo.setOrderNum(order.getRandOrderID()); //�20λ������
		// 2.���ö������
		BigDecimal amount = new BigDecimal(order.getAmount());
		amount=amount.movePointRight(2);
		log.debug("bccb amount:"+amount.intValue());
		merPayInfo.setOrderAmt(String.valueOf(amount.intValue())); //�������		
		// 3.����֪ͨURL
		merPayInfo.setNotifyUrl(this.recurl+"/PayRec?idx="+this.idx);//�õ�ַ��Ҫ�ϱ�����
		merPayInfo.setResponseUrl(this.recurl+"/PayRec?idx="+this.idx);
		// 4.�����̻���ע
		merPayInfo.setMerRemarks("");
		// 5.�����̻�����֪ͨ��ʽ
		merPayInfo.setNotifyType("0"); //֪ͨ���� 0 http
		// 6.�����Ƿ�����ȡ����־
		merPayInfo.setGoodsType("2"); //����ȡ����0,ȡ����ַ��������2��ȡ����ַ����������
		// 7.���ý�������
		merPayInfo.setActionType("0"); //0֧�� 1.��ѯ
		//8 .���ö��ʲ�ѯ����		
		merPayInfo.setMerCheckDate(order.getPostdate());
		//			
		MerPaymentRequest merReq = new MerPaymentRequest(merPayInfo);
		if(merPayInfo.checkRelationInfo()==0){
			String xml=BOB.getXml(this.corpid,merPayInfo,order.getPostdate()).toString(); //:)���ñ������е�API,�̻��Ŵ������ļ�ȡ;
			String sign=this.signData(xml,merchantDN);				
			if(sign==null) throw new ServiceException(EventCode.SERVICE_NOTPROVIED);
			StringBuffer sf = new StringBuffer("");	
			sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
			sf.append("<input type=\"hidden\" name=\"netType\" value=\"7\">");
	        sf.append("<input type=\"hidden\" name=\"merReqData\" value=\""+sign+"\">");
	        sf.append("</form>");	        
			return sf.toString();			
		}else{
			log.info("payinfo:"+merPayInfo.checkRelationInfo()+" BCCB Error:"+merReq.getErrMsg());
			throw new ServiceException(EventCode.WEB_PARAMNULL,merReq.getErrMsg());
			
		}
	}
	public String signData(String src,String merchantdn){
		    NetSignServer netSign = new NetSignServer();
	        try{
	            netSign.NSSetPlainText(src.getBytes("GBK"));
	        }
	        catch(Exception temE){
	        	temE.printStackTrace();
	            netSign.NSSetPlainText(src.getBytes());
	        }
	        try{	        	
	            byte signedBytes[] = netSign.NSAttachedSign(merchantdn);
	            int retNum = netSign.getLastErrnum();
	            if(retNum == 0){
	                return new String(signedBytes);
	            }else{
	                log.debug("******client  util: ǩ��ʧ��  ****** �������Ϊ [" + retNum + "]");
	                return null;
	            }
	        }
	        catch(Exception temE){
	            temE.printStackTrace();
	        }
	        return null;
	}
	 public String verifySignData(String src,String serverDn){	
		    NetSignServer netSign = new NetSignServer();
	        try{
	            netSign.NSAttachedVerify(src.getBytes("GBK"));
	        }
	        catch(Exception temE)
	        {
	            netSign.NSAttachedVerify(src.getBytes());
	        }
	        int retNum = netSign.getLastErrnum();
	        if(retNum == 0){
	            String certDN = netSign.NSGetSignerCertInfo(1).trim();
	            if(!certDN.equals(serverDn))
	            {
	                log.warn("******client  util: �Ƿ�֧��ƽ̨֤��  ****** ");
	                return null;
	            } else
	            {
	                return new String(netSign.NSGetPlainText());
	            }
	        } else
	        {
	            log.warn("******client  util: ��֤ǩ��ʧ��  ****** �������Ϊ [" + retNum + "]");
	            return null;
	        }
	 }
	 public static String getXml(String merchantid,
			 String orderNum,
			 String orderAmt,
			 String goodsType,
			 String notifyType,
			 String responseUrl,
			 String actionType,
			 String MerCheckDate,
			 String notifyUrl,
			 String merRemarks){
		 return  "<?xml version=\"1.0\" encoding=\"GB2312\" ?><BCCBMcInfoData><ReqParam><merID>" 
		 +merchantid + "</merID><orderNum>" + orderNum + "</orderNum><orderAmt>" + orderAmt + "</orderAmt><goodsType>" 
		 + goodsType + "</goodsType><notifyType>" + notifyType + "</notifyType><responseUrl>" 
		 + responseUrl + "</responseUrl><actionType>" 
		 + actionType + "</actionType><merCheckDate>" + MerCheckDate + "</merCheckDate><notifyUrl>" + notifyUrl + "</notifyUrl><merRemarks>" 
		 + merRemarks + "</merRemarks></ReqParam></BCCBMcInfoData>";
	 }
	 
	 public static String getXml(String merchantid,MerPaymentInfo payment,String checkDate){
		 return getXml(merchantid,
				 payment.getOrderNum(),
				 payment.getOrderAmt(),
				 payment.getGoodsType(),
				 payment.getNotifyType(),
				 payment.getResponseUrl(),
				 payment.getActionType(),
				 checkDate,
				 payment.getNotifyUrl(),
				 payment.getMerRemarks());
		 
	 }
	
	/**
	 * @param merchantDN The merchantDN to set.
	 */
	public void setMerchantDN(String merchantDN) {
		this.merchantDN = merchantDN;
	}

	/**
	 * @param serverDN The serverDN to set.
	 */
	public void setServerDN(String serverDN) {
		this.serverDN = serverDN;
	}
	/**
	 * @param engineService The engineService to set.
	 */
	public void setEngineService(STEngineService engineService) {
		this.engineService = engineService;
	}	
	
}
