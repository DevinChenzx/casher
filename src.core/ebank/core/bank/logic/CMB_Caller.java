/*
 * @Id: CMB_Caller.java 9:46:05 2007-6-15
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.io.BufferedInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import ebank.core.SequenceService;
import ebank.core.bank.BankService;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.ext.AuthSSLProtocolSocketFactory;

/**
 * @author 
 * Description: �������е绰����
 * 
 */
public class CMB_Caller extends BankExt implements BankService{
		
	private Log log=LogFactory.getLog(this.getClass());
	
	private HttpClient client=new HttpClient();
	private PostMethod method=null;	
	private static Protocol myhttps=null;
	
	private String merchantid;
	private String merchantName;
	private String cmbBranchID;//�̻������������кţ�����ѯ�������������з�֧����
	private String desturl;
	private String jkspath;
	private String jkspwd;	
	private String prefixnum;
	private SequenceService sequenceservice;
	
    //10λ��1+8(����)+1λ���
	public String generateOrderID() throws ServiceException{
		String str="";
		Random rd=new Random();
		for(int i=0;i<2-this.prefixnum.length();i++) str+=rd.nextInt(10);			
		rd=null;
		return prefixnum+sequenceservice.getCode().substring(6,14)+str;
	}  	

	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#getPayResult(java.util.HashMap)
	 */
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#sendOrderToBank(ebank.core.domain.BankOrder)
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		String authcode=""; //�ݲ���ֵ
		StringBuffer sf=new StringBuffer();
		sf.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		sf.append("<PayData>");
	    sf.append("<CommandType>0001</CommandType>");  // <!���������ͣ� 0001Ϊ���ɶ���-- >
	    sf.append("<Merchant>");//  <!--�̻���Ϣ -->
		sf.append("<Merchant_ID>"+merchantid+"</Merchant_ID>");//  <!--�̻���005392 -->
		sf.append("<Merchant_Name>"+merchantName+"</Merchant_Name>");//  <!--�̻����� -->
		sf.append("<Merchant_BranchId>"+cmbBranchID+"</Merchant_BranchId>");// <!--�̻����� -->
		sf.append("<Merchant_NotifyFlag>Y</Merchant_NotifyFlag>"); // <!-- ֪ͨ��־ -->
		sf.append("<Merchant_NotifyUrl>"+this.recurl+"</Merchant_NotifyUrl>");// <!��֪ͨURL -->
		sf.append("<Merchant_NotifyParams>"+this.getIdx()+"</Merchant_NotifyParams>");// <!��֪ͨ���� -->
		sf.append("<Merchant_ExtInfo></Merchant_ExtInfo>");// <!--�̻���չ���� -->
	    sf.append("</Merchant>");
	    sf.append("<Buyer>");// <!--�����Ϣ -->
		sf.append("<Buyer_Telephone>"+order.getPayaccount()+"</Buyer_Telephone>"); //  <!--�µ��绰 -->
		sf.append("<Buyer_OrderKey>"+authcode+"</Buyer_OrderKey>");//  <!������������ -->
		sf.append("<Buyer_ExtInfo></Buyer_ExtInfo>");// <!-- ��չ���� -->
		sf.append("</Buyer>");//
		sf.append("<Order>");// <!--������Ϣ -->
		sf.append("<Order_No>"+order.getRandOrderID()+"</Order_No>"); // <!--������ -->
		sf.append("<Order_Amount>"+order.getAmount()+"</Order_Amount>"); // <!--������� -->
		sf.append("<Order_Currency>RMB</Order_Currency>"); // <!--���� -->
		Calendar cd=Calendar.getInstance();		
		sf.append("<Order_TransTime>"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cd.getTime())+"</Order_TransTime>"); //<!--����ʱ��-->
		cd.add(Calendar.DAY_OF_MONTH,1);
		sf.append("<Order_ExpireTime>"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cd.getTime())+"</Order_ExpireTime>"); // <!--����ʱ�� -->
		sf.append("<Order_Summary></Order_Summary>");//	<!--�������� -->
		sf.append("<Order_ExtInfo></Order_ExtInfo>");// <!����չ��Ϣ -->
		sf.append("</Order>");//
		sf.append("</PayData>");//
		log.debug(sf.toString());		
		SendThread send=new SendThread();
		send.setSendxml(sf.toString());	
		Thread t=new Thread(send);
		t.start();
		try {
			t.join(15000);							
		} catch (Exception e) {
			log.info("������ͨ�ų�ʱ�쳣");
			e.printStackTrace();
		}					
		String[] x=send.getRes(); //httpclient send to bank	
		
		StringBuffer sf2=new StringBuffer();
		sf2.append("<body onload=\"javascript:document.sendOrder.submit();\">");
		sf2.append("<form name=\"sendOrder\" action=\"/Caller.do\" method=post>");
		String plain=order.getOrdernum()+"="+order.getRandOrderID()+"="+order.getAmount()+"="+order.getPayaccount();
		String info=CryptUtil.encrypt(plain);
		//ȡ����Ϣ
		String xids=CryptUtil.encrypt(order.getOrdernum()+"="+order.getRandOrderID()); 
		sf2.append("<input type=\"hidden\" name=\"info\" value=\""+info+"\"/>");
		sf2.append("<input type=\"hidden\" name=\"xids\" value=\""+xids+"\"/>");
		sf2.append("<input type=\"hidden\" name=\"servicecode\" value=\""+order.getBankID()+"\"/>");		
		if("1".equals(x[0])){//ת��ɹ�ҳ			
			//����һ����Ϣ��ʾ
			sf2.append("<input type=\"hidden\" name=\"bankRes\" value=\"1\"/>");
			sf2.append("<input type=\"hidden\" name=\"authcode\" value=\""+authcode+"\"/>");
			//ȡ����ַ����						
		}else{
		    sf2.append("<input type=\"hidden\" name=\"bankRes\" value=\"0\"/>");
		    sf2.append("<input type=\"hidden\" name=\"resMsg\" value=\""+x[1]+"\"/>");
		}
		sf2.append("</form>");
		return sf2.toString();
		//return wait page;
	}
	private class SendThread implements Runnable{
		String[] res=new String[]{"0","������ͨ�ų�ʱ"};
		private String sendxml;
		public void run(){
			String result="";			
			try{			
				URL url=new URL(desturl);
				method=new PostMethod(url.getPath()+"?PrePayTel");			
				method.setRequestHeader("Content-type", "text/xml; charset=GB2312");
				method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
			    		new DefaultHttpMethodRetryHandler()); 
				
				if(desturl.indexOf("https")==0){//https
					if(myhttps==null)
						myhttps = new Protocol("https", 
						                           new AuthSSLProtocolSocketFactory(
							                       new URL(jkspath), jkspwd,
							                       new URL(jkspath), jkspwd), 8443);
						     
						client.getHostConfiguration().setHost(url.getHost(),
			                    443,
			                    myhttps);
				}else{
					client.getHostConfiguration().setHost(url.getHost(),80,url.getProtocol());
				}
				log.debug("PayData��"+sendxml);
				method.addParameter(new NameValuePair("PayData",sendxml));								
				log.debug(""+url.getHost()+url.getPort()+method.getPath());			

				int rescode=client.executeMethod(method);
				//int rescode=uc.getResponseCode();
				if(rescode==200){
					BufferedInputStream bis = new BufferedInputStream(method.getResponseBodyAsStream());
					byte[] bytes = new byte[ 4096 ];
					int count = bis.read( bytes );
					while( count != -1 && count <= 4096 ) {    			      
					 count = bis.read( bytes );
					}    			      
					bis.close();
					result=new String(bytes);
					log.debug("reponse:"+result);
					if(result!=null&&result.indexOf("<SuccessFlag>Y</SuccessFlag>")>0){ //���ͳɹ�
						res[0]="1";						
					}
					else{
						res[1]=getNode(result,"ErrorMessage");						
					}
					
				}else{
					res[1]="����ϵͳæ,����ܾ�...";
					log.error("cmb caller error:"+rescode);
				}
			}catch(Exception ex){
				ex.printStackTrace();
				res[1]="����������ͨ�ų�ʱ";
			}finally{
				method.releaseConnection();
			}			
		}
		/**
		 * @return Returns the res.
		 */
		public String[] getRes() {
			return res;
		}
		/**
		 * @param sendxml The sendxml to set.
		 */
		public void setSendxml(String sendxml) {
			this.sendxml = sendxml;
		}	
	}
	
	private String getNode(String plain, String nodename) {
		if(plain.indexOf("<" + nodename + ">")<0) return "";
		return plain.substring(plain.indexOf("<" + nodename + ">")
				+ nodename.length() + 2, plain.indexOf("</" + nodename + ">"));
	}
	/**
	 * @param cmbBranchID The cmbBranchID to set.
	 */
	public void setCmbBranchID(String cmbBranchID) {
		this.cmbBranchID = cmbBranchID;
	}

	/**
	 * @param merchantid The merchantid to set.
	 */
	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}

	/**
	 * @param prefixnum The prefixnum to set.
	 */
	public void setPrefixnum(String prefixnum) {
		this.prefixnum = prefixnum;
	}

	/**
	 * @param sequenceservice The sequenceservice to set.
	 */
	public void setSequenceservice(SequenceService sequenceservice) {
		this.sequenceservice = sequenceservice;
	}
	/**
	 * @param merchantName The merchantName to set.
	 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	/**
	 * @param desturl The desturl to set.
	 */
	public void setDesturl(String desturl) {
		this.desturl = desturl;
	}
	/**
	 * @param jkspath The jkspath to set.
	 */
	public void setJkspath(String jkspath) {
		this.jkspath = jkspath;
	}
	/**
	 * @param jkspwd The jkspwd to set.
	 */
	public void setJkspwd(String jkspwd) {
		this.jkspwd = jkspwd;
	}
}
