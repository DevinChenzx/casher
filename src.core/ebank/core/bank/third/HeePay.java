package ebank.core.bank.third;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class HeePay extends BankExt implements BankService {
	
	static Logger logger = Logger.getLogger(HeePay.class); 	
	
	private static String version = "1";
	private static String pay_type = "20";	
	private String pubkey;
	
	public String sendOrderToBank(BankOrder order) throws ServiceException {

		String notify_url = "http://epay.gicard.net/PayNotify";
			
	    String return_url = this.httprecurl;
		
		StringBuffer sf=new StringBuffer();
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" name=\"version\" value=\""+version+"\" />");//��ǰ�ӿڰ汾�� 1
		sf.append("<input type=\"hidden\" name=\"pay_type\" value=\""+pay_type+"\" />");//֧������
		
		
		try {
			 HashMap mp=order.getMp();
				JSONObject jo=null;
				if(mp!=null&&mp.get("outJson")!=null){
					String outjson=CryptUtil.decrypt(String.valueOf(mp.get("outJson")));
					logger.info(outjson);
					jo=JSONObject.fromObject(outjson);
				}
				String gateID = "";
				if (order.getMp() != null && order.getMp().get("outChannel") != null) {
					gateID = String.valueOf(order.getMp().get("outChannel"));
				}
				String defaultbank = getBankCode(getJsonParams(jo,"defaultbank",gateID));
			
				sf.append("<input type=\"hidden\" name=\"pay_code\" value=\""+defaultbank+"\" />");//֧�����ͱ���
				sf.append("<input type=\"hidden\" name=\"agent_id\" value=\""+this.getCorpid()+"\"/>");//�̻����
				sf.append("<input type=\"hidden\" name=\"agent_bill_id\" value=\""+order.getRandOrderID()+"\" />");//�̻�ϵͳ�ڲ��Ķ����ţ�Ҫ��֤Ψһ���������50 �ַ�
				sf.append("<input type=\"hidden\" name=\"pay_amt\" value=\""+order.getAmount()+"\" />");//�����ܽ��
				sf.append("<input type=\"hidden\" name=\"notify_url\" value=\""+notify_url+"\" />");//֧���󷵻ص��̻�����ҳ��(��̨����)
				sf.append("<input type=\"hidden\" name=\"return_url\" value=\""+return_url+"\" />");//֧���󷵻ص��̻���ʾҳ(ǰ̨��ʾ)
				sf.append("<input type=\"hidden\" name=\"user_ip\" value=\""+order.getCustip()+"\" />");//�û����ڿͻ��˵���ʵip
					
				sf.append("<input type=\"hidden\" name=\"agent_bill_time\" value=\""+order.getPostdate()+"\" />");//�ύ���ݵ�ʱ��yyyyMMddHHmmss �磺20100225102000
				sf.append("<input type=\"hidden\" name=\"goods_name\" value=\""+URLEncoder.encode("����֧��","utf-8")+"\" />");//��Ʒ����, �����50 �ַ�������Ϊ�գ����μ�ǩ����
				sf.append("<input type=\"hidden\" name=\"goods_num\" value=\"1\" />");//��Ʒ����,�����20 �֣����μ�ǩ����
				sf.append("<input type=\"hidden\" name=\"remark\" value=\""+this.idx+"\" />");//�̻��Զ��� ԭ������,�����50 �ַ�������Ϊ�ա������μ�ǩ����
//				sf.append("<input type=\"hidden\" name=\"is_test\" value=\"1\" />");//�Ƿ���ԣ�1=���ԣ��ǲ����벻�ô�������(�紫�˴˲���,�����μ�MD5 ����֤)
				sf.append("<input type=\"hidden\" name=\"goods_note\" value=\""+URLEncoder.encode("����֧��","utf-8")+"\" />");//֧��˵��, ����50 �ַ������μ�ǩ����
				
				StringBuffer signSf=new StringBuffer();
				signSf.append("version=").append(version);
				signSf.append("&agent_id=").append(this.getCorpid());
				signSf.append("&agent_bill_id=").append(order.getRandOrderID());
				signSf.append("&agent_bill_time=").append(order.getPostdate());
				signSf.append("&pay_type=").append(pay_type);
				signSf.append("&pay_amt=").append(order.getAmount());
				signSf.append("&notify_url=").append(notify_url);
				signSf.append("&return_url=").append(return_url);
				signSf.append("&user_ip=").append(order.getCustip());
//				signSf.append("&is_test=1");
				
				logger.info("�㸶��֧������ǩ���ַ�����"+signSf.toString());
				signSf.append("&key=").append(this.pubkey);		
				sf.append("<input type=\"hidden\" name=\"sign\" value=\""+MD5(signSf.toString())+"\" />");//MD5ǩ�����
				sf.append("</form>");
				
				logger.info("�㸶��֧�������ַ�����"+sf.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sf.toString();
	}
	
	

	public PayResult getPayResult(HashMap request) throws ServiceException {
		
		PayResult bean=null;		
		
		String NR = (String)request.get("NR");
		
		String result = (String)request.get("result");
		String pay_message = (String)request.get("pay_message");
		String agent_id = (String)request.get("agent_id");
		String jnet_bill_no = (String)request.get("jnet_bill_no");
		String agent_bill_id = (String)request.get("agent_bill_id");
		String pay_type = (String)request.get("pay_type");
		String pay_amt = (String)request.get("pay_amt");
		String remark = (String)request.get("remark");
		String sign = (String)request.get("sign");
	
		StringBuffer signSf=new StringBuffer();
		signSf.append("result=").append(result);
		signSf.append("&agent_id=").append(agent_id);
		signSf.append("&jnet_bill_no=").append(jnet_bill_no);
		signSf.append("&agent_bill_id=").append(agent_bill_id);
		signSf.append("&pay_type=").append(pay_type);
		signSf.append("&pay_amt=").append(pay_amt);
		signSf.append("&remark=").append(remark);
		
		logger.info("�㸶��֧�����ز���ֵ��"+signSf.toString()+"&sign="+sign);
	    signSf.append("&key=").append(this.pubkey);
	    
	    String reSign = MD5(signSf.toString());
	    
	    logger.info("�㸶��֧�����ز��������ַ�����"+reSign);
	    
		if(sign.equals(reSign)){		
			if(result.equals("1")){
				bean=new PayResult(agent_bill_id,this.bankcode,new BigDecimal(pay_amt),1);				
			    if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(NR)){
			    	request.put("RES","ok");					
				}							    
			    bean.setBanktransseq(jnet_bill_no);
			}else{
				bean=new PayResult(agent_bill_id,this.bankcode,new BigDecimal(pay_amt),-1);
				logger.info("�㸶��֧��ʧ�ܣ�"+pay_message);
			}		
		}else{
			   if(("SID_"+this.idx).equals(NR)){
				    request.put("RES","error");
			    	logger.info("��̨֪ͨǩ��ʧ��,���ؼ��ܴ�:"+sign+",�������ܴ���"+reSign);
				}else{
					logger.info("ǰ̨֪ͨǩ��ʧ��,���ؼ��ܴ�:"+sign+",�������ܴ���"+reSign);	
				}
			   throw new ServiceException(EventCode.SIGN_VERIFY);
		}
		return bean;
	}
	
	private String getJsonParams(JSONObject jo,String key,String defaults){
		try{
			if(jo!=null) return jo.getString(key)==null?defaults:jo.getString(key);
		}catch(Exception e){
			
		}
		return defaults;
	}
	
	
	 public static String MD5(String s) {  
	        char hexDigits[] = { '0', '1', '2', '3', '4',  
	                             '5', '6', '7', '8', '9',  
	                             'A', 'B', 'C', 'D', 'E', 'F' };  
	        try {  
	            byte[] btInput = s.getBytes();  
	            //���MD5ժҪ�㷨�� MessageDigest ����  
	            MessageDigest mdInst = MessageDigest.getInstance("MD5");  
	            //ʹ��ָ�����ֽڸ���ժҪ  
	            mdInst.update(btInput);  
	            //�������  
	            byte[] md = mdInst.digest();  
	            //������ת����ʮ�����Ƶ��ַ�����ʽ  
	            int j = md.length;  
	            char str[] = new char[j * 2];  
	            int k = 0;  
	            for (int i = 0; i < j; i++) {  
	                byte byte0 = md[i];  
	                str[k++] = hexDigits[byte0 >>> 4 & 0xf];  
	                str[k++] = hexDigits[byte0 & 0xf];  
	            }  
	            return new String(str).toLowerCase();  
	        }  
	        catch (Exception e) {  
	            e.printStackTrace();  
	            return null;  
	        }  
	    }  
	 
		public String getPubkey() {
			return pubkey;
		}

		public void setPubkey(String pubkey) {
			this.pubkey = pubkey;
		}
		
		
		private String getBankCode(String code)
		{
			
			String bankcode="";
			if(code.equals("ICBC"))
			{
				bankcode="001";
			}else if(code.equals("CMB")){
			
				bankcode="002";
			}else if(code.equals("CCB")){
			
				bankcode="003";
			}else if(code.equals("BOC")){
			
				bankcode="004";
			}else if(code.equals("ABC")){
			
				bankcode="005";
			}else if(code.equals("BOCM")){
			
				bankcode="006";
			}else if(code.equals("SPDB")){
			
				bankcode="007";
			}else if(code.equals("GDB")){
			
				bankcode="008";
			}else if(code.equals("CITIC")){
			
				bankcode="009";
			}else if(code.equals("CEB")){
			
				bankcode="010";
			}else if(code.equals("CIB")){
			
				bankcode="011";
			}else if(code.equals("SPA")||code.equals("SDB")){
			
				bankcode="012";
			}else if(code.equals("CMBC")){
			
				bankcode="013";
			}else if(code.equals("HXB")){
			
				bankcode="014";
			}	
			return bankcode;
		}
}
