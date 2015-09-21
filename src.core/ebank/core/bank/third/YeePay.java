package ebank.core.bank.third;
import java.math.BigDecimal;
import java.util.*;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.web.common.util.DigestUtil;

/**
 * @author Description: �ױ�֧�����������ļ�:
 * 
 */
public class YeePay extends BankExt implements BankService {

	static Logger log = Logger.getLogger(YeePay.class);
	
	@Override
	public String generateOrderID() throws ServiceException {		
		String RandOrderID = sequenceservice.getCode(); // yyDDD(6)+8λ����
		Random rd = new Random();
		String str = "";
		int length = this.prefixnum == null ? 0 : this.prefixnum.length();
		for (int i = 0; i < 4 - length; i++)
			str += rd.nextInt(10);
		return prefixnum + str + this.getCorpid().substring(10)
				+ RandOrderID.substring(RandOrderID.length() - 7);

	}

	public String  formatString(String text){ 
		if(text == null) {
			return ""; 
		}
		return text;
	}
	public static String getReqMd5HmacForOnlinePayment(String p0_Cmd,String p1_MerId,
			String p2_Order, String p3_Amt, String p4_Cur,String p5_Pid, String p6_Pcat,
			String p7_Pdesc,String p8_Url, String p9_SAF,String pa_MP,String pd_FrpId,
			String pr_NeedResponse,String keyValue) {
		  StringBuffer sValue = new StringBuffer();
		// ҵ������
		sValue.append(p0_Cmd);
		// �̻����
		sValue.append(p1_MerId);
		// �̻�������
		sValue.append(p2_Order);
		// ֧�����
		sValue.append(p3_Amt);
		// ���ױ���
		sValue.append(p4_Cur);
		// ��Ʒ����
		sValue.append(p5_Pid);
		// ��Ʒ����
		sValue.append(p6_Pcat);
		// ��Ʒ����
		sValue.append(p7_Pdesc);
		// �̻�����֧���ɹ����ݵĵ�ַ
		sValue.append(p8_Url);
		// �ͻ���ַ
		sValue.append(p9_SAF);
		// �̻���չ��Ϣ
		sValue.append(pa_MP);
		// ���б���
		sValue.append(pd_FrpId);
		// Ӧ�����
		sValue.append(pr_NeedResponse);
		
		String sNewString = null;

		sNewString = DigestUtil.hmacSign(sValue.toString(), keyValue);
		return (sNewString);
	}
	private String getJsonParams(JSONObject jo,String key,String defaults){
		if(jo!=null) return jo.getString(key)==null?defaults:jo.getString(key);
		return defaults;
	}
	/**
	 * ����ҳ����ת�Ĵ���
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		HashMap mp=order.getMp();
		JSONObject jo=null;		
		if(mp!=null&&mp.get("outJson")!=null){
			String outjson=CryptUtil.decrypt(String.valueOf(mp.get("outJson")));
			log.info(outjson);
			jo=JSONObject.fromObject(outjson);
		}
		String keyValue   		     		= formatString(this.getKeyPassword());   					// �̼���Կ
		String nodeAuthorizationURL  	= formatString(this.getDesturl());  							// ���������ַ
		// �̼������û�������Ʒ��֧����Ϣ
		String    p0_Cmd 		= formatString("Buy");                  // ����֧�����󣬹̶�ֵ ��Buy��
		String    p1_MerId 		= formatString(this.getCorpid()); 		// �̻����
		String    p2_Order      = formatString(order.getRandOrderID()); // �̻�������
		String	  p3_Amt        = formatString(order.getAmount());      	   			// ֧�����
		String	  p4_Cur    	= formatString("CNY");	   		   		// ���ױ���
		String	  p5_Pid 		= formatString((String)getJsonParams(jo,"p5_Pid",""));	  // ��Ʒ����
		String	  p6_Pcat  		= formatString((String)getJsonParams(jo,"p6_Pcat",""));	   // ��Ʒ����
		String 	  p7_Pdesc   	= formatString((String)getJsonParams(jo,"p7_Pdesc",""));		// ��Ʒ����
		String 	  p8_Url 	    = formatString(this.getRecurl()); 		       				// �̻�����֧���ɹ����ݵĵ�ַ
		String 	  p9_SAF 		= formatString("0"); 			   							// ��Ҫ��д�ͻ���Ϣ 0������Ҫ  1:��Ҫ
		String 	  pa_MP 		= formatString((String)getJsonParams(jo,"pa_MP",""));        // �̻���չ��Ϣ
		String    pd_FrpId      = formatString((String)getJsonParams(jo,"pd_FrpId",""));     // ֧��ͨ������
		// ���б�ű����д
		pd_FrpId = pd_FrpId.toUpperCase();
		String 	  pr_NeedResponse    	= formatString("1");    // Ĭ��Ϊ"1"����ҪӦ�����
		String 	  hmac 			     	= formatString("");							        // ����ǩ����
	    // ���MD5-HMACǩ��
	    hmac = getReqMd5HmacForOnlinePayment(p0_Cmd,
				p1_MerId,p2_Order,p3_Amt,p4_Cur,p5_Pid,p6_Pcat,p7_Pdesc,
				p8_Url,p9_SAF,pa_MP,pd_FrpId,pr_NeedResponse,keyValue);
		StringBuffer sf=new StringBuffer();
		sf.append("<form name=sendOrder  METHOD=\"POST\" action=\""+ nodeAuthorizationURL + "\">");

		sf.append("<input type=\"hidden\" name=\"p0_Cmd\" value=\""+ p0_Cmd + "\"/>");
		sf.append("<input type=\"hidden\" name=\"p1_MerId\" value=\""+ p1_MerId + "\"/>");
		sf.append("<input type=\"hidden\" name=\"p2_Order\" value=\"" + p2_Order+ "\"/>");
		sf.append("<input type=\"hidden\" name=\"p3_Amt\" value=\""+ p3_Amt + "\"/>");
		sf.append("<input type=\"hidden\" name=\"p4_Cur\" value=\"" + p4_Cur+ "\"/>");
		sf.append("<input type=\"hidden\" name=\"p5_Pid\" value=\""+ p5_Pid + "\"/>");
		sf.append("<input type=\"hidden\" name=\"p6_Pcat\" value=\""+ p6_Pcat + "\"/>");
		sf.append("<input type=\"hidden\" name=\"p7_Pdesc\" value=\"" + p7_Pdesc+ "\"/>");
		sf.append("<input type=\"hidden\" name=\"p8_Url\" value=\""+ p8_Url + "\"/>");
		sf.append("<input type=\"hidden\" name=\"p9_SAF\" value=\""+ p9_SAF + "\"/>");
		sf.append("<input type=\"hidden\" name=\"pa_MP\"  value=\"" + pa_MP+ "\"/>");
		sf.append("<input type=\"hidden\" name=\"pd_FrpId\" value=\"" + pd_FrpId+ "\"/>");
		sf.append("<input type=\"hidden\" name=\"pr_NeedResponse\" value=\""+ pr_NeedResponse+ "\"/>");
		sf.append("<input type=\"hidden\" name=\"hmac\" value=\""+ hmac+ "\"/>");
		sf.append("</form>");
		log.info(sf.toString());
		return sf.toString();
	}

	public PayResult getPayResult(HashMap reqs) throws ServiceException {	
		PayResult result = null;
		try{
		
		String keyValue   = formatString(this.getKeyPassword());   // �̼���Կ
		String r0_Cmd 	  = formatString((String) reqs.get("r0_Cmd")); // ҵ������
		String p1_MerId   = formatString(this.getCorpid());   // �̻����
		String r1_Code    = formatString((String) reqs.get("r1_Code"));// ֧�����
		String r2_TrxId   = formatString((String) reqs.get("r2_TrxId"));// �ױ�֧��������ˮ��
		String r3_Amt     = formatString((String) reqs.get("r3_Amt"));// ֧�����
		String amt 		  = Amount.getFormatAmount(r3_Amt, 0);
		String r4_Cur     = formatString((String) reqs.get("r4_Cur"));// ���ױ���
		String r5_Pid     = new String(formatString((String) reqs.get("r5_Pid")).getBytes("iso-8859-1"),"gbk");// ��Ʒ����
		String r6_Order   = formatString((String) reqs.get("r6_Order"));// �̻�������
		String r7_Uid     = formatString((String) reqs.get("r7_Uid"));// �ױ�֧����ԱID
		String r8_MP      = new String(formatString((String) reqs.get("r8_MP")).getBytes("iso-8859-1"),"gbk");// �̻���չ��Ϣ
		String r9_BType   = formatString((String) reqs.get("r9_BType"));// ���׽����������
		String hmac       = formatString((String) reqs.get("hmac"));// ǩ������
		
		result = new PayResult(r6_Order, this.getBankcode(), new BigDecimal(amt),
				"1".equals(r1_Code) ? 1 : -1);
		boolean isOK = false;
		// У�鷵�����ݰ�
		isOK = verifyCallback(hmac,p1_MerId,r0_Cmd,r1_Code, 
				r2_TrxId,r3_Amt,r4_Cur,r5_Pid,r6_Order,r7_Uid,r8_MP,r9_BType,keyValue);
		if (!isOK) {
			throw new ServiceException(EventCode.CRYPT_VALIADATESIGN); // ��֤ǩ��ʧ��
		}		
		result.setBanktransseq(r2_TrxId);
		result.setBankresult(r1_Code);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return result;
		
	}
	
	public static boolean verifyCallback(String hmac, String p1_MerId,
			String r0_Cmd, String r1_Code, String r2_TrxId, String r3_Amt,
			String r4_Cur, String r5_Pid, String r6_Order, String r7_Uid,
			String r8_MP, String r9_BType, String keyValue) {
		StringBuffer sValue = new StringBuffer();
		// �̻����
		sValue.append(p1_MerId);
		// ҵ������
		sValue.append(r0_Cmd);
		// ֧�����
		sValue.append(r1_Code);
		// �ױ�֧��������ˮ��
		sValue.append(r2_TrxId);
		// ֧�����
		sValue.append(r3_Amt);
		// ���ױ���
		sValue.append(r4_Cur);
		// ��Ʒ����
		sValue.append(r5_Pid);
		// �̻�������
		sValue.append(r6_Order);
		// �ױ�֧����ԱID
		sValue.append(r7_Uid);
		// �̻���չ��Ϣ
		sValue.append(r8_MP);
		// ���׽����������
		sValue.append(r9_BType);
		String sNewString = null;
		sNewString = DigestUtil.hmacSign(sValue.toString(), keyValue);

		if (hmac.equals(sNewString)) {
			return (true);
		}
		return (false);
	}
	

}
