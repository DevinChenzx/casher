package ebank.core.bank.b2b;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.hitrust.B2B.CMBC.common.MessageTagName;
import com.hitrust.B2B.CMBC.request.DirectPayRequest;
import com.hitrust.B2B.CMBC.request.entry.ItemEntry;
import com.hitrust.B2B.CMBC.request.entry.OrderDetailEntry;
import com.hitrust.B2B.CMBC.request.entry.OrderEntry;
import com.hitrust.B2B.CMBC.request.entry.PayInfoEntry;
import com.hitrust.B2B.CMBC.response.GenericResponse;
import com.hitrust.B2B.CMBC.util.XMLDocument;

import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.ServiceException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.web.common.util.Validator;

public class CMBC extends BankExt implements BankService {
	
	static Logger logger = Logger.getLogger(CMBC.class); 
	
	private String accNo;
	private String accName;

	public String sendOrderToBank(BankOrder order) throws ServiceException {		
		
		
		DirectPayRequest tDirectPayRequest = new DirectPayRequest();
		OrderEntry       tOrderEntry       = new OrderEntry();
		PayInfoEntry     tPayInfoEntry     = new PayInfoEntry();
		OrderDetailEntry tOrderDetailEntry = new OrderDetailEntry();
		ItemEntry[]        tItemEntry      = new ItemEntry[1];
		
		//���̻���Ϣ����
		tDirectPayRequest.setMerchantTrnxNo(order.getRandOrderID());
		tDirectPayRequest.setTrnxCode("P006");
		tDirectPayRequest.setResultNotifyURL(this.recurl);
		//�Ѷ�����Ϣ����
		tOrderEntry.setOrderNo(order.getOrdernum()==null?order.getRandOrderID():order.getOrdernum());
		tOrderEntry.setOrderAmt(order.getAmount());
		tOrderEntry.setOrderDate(order.getPostdate());
		tOrderEntry.setSellerCorporationAcctNo(this.accNo);  
		tOrderEntry.setSellerCorporationAcctName(this.accName);
		tOrderEntry.setPayType("0");		
		
		//������ϸ
		tOrderDetailEntry.setOperName("001");
		tItemEntry[0] = new ItemEntry();
		tItemEntry[0].setPID(order.getOrdernum()==null?order.getRandOrderID():order.getOrdernum());
		tItemEntry[0].setPN("B2B֧��");
		tItemEntry[0].setUP(order.getAmount());
		tItemEntry[0].setQTY("1");
		tItemEntry[0].setDES("B2B");
		tOrderDetailEntry.setItems(tItemEntry);

		tOrderEntry.setOrderDetail(tOrderDetailEntry);
		tDirectPayRequest.setOrder(tOrderEntry);
		//��֧����Ϣ����
		tPayInfoEntry.setPayerCustNo(order.getPayaccount());
		tPayInfoEntry.setTrnxAmount(order.getAmount());
		tDirectPayRequest.setPayInfo(tPayInfoEntry);

		GenericResponse tDirectPayResponse = tDirectPayRequest.postRequest();
		StringBuffer sf=new StringBuffer("");
		if(tDirectPayResponse.isSuccessed()){
			String amt = new XMLDocument(tDirectPayResponse.getValue("TrnxAmount")).getFormatDocument("&nbsp").toString();
			amt.replaceAll("[<]", "&lt;");
			amt.replaceAll("[>]", "&gt;");
			String orderno = new XMLDocument(tDirectPayResponse.getValue("OrderNo")).getFormatDocument("&nbsp").toString();
			orderno.replaceAll("[<]", "&lt;");
			orderno.replaceAll("[>]", "&gt;");
			
			sf.append("<div class=\"quern_warning\">");
			sf.append("<p>");
			sf.append("   <span class=\"qunrenzit red\">�ύ�ɹ�!</span>  ��лʹ�ü����������κ�������鷢���ʼ���service@gicard.net");
			sf.append("</p>");
			sf.append("&nbsp;");
			sf.append("<p> ���׽�");
			sf.append("  <span class=\"red\">");
			sf.append(""+amt+"Ԫ");	
			sf.append("  </span>");
			sf.append("<p> ������:");
			sf.append(""+orderno);
			sf.append("</p>");
			sf.append("<p><span class=\"red\">������¼����������ҵ����ϵͳ��ѡ���ȷ�ϵ�ֱ��֧����ת�˼�¼����������֧��ȷ��!</span></p>");
			sf.append("</div>");
		}else{
			String msg = new XMLDocument(tDirectPayResponse.getValue(MessageTagName.MESSAGE_TAG)).getFormatDocument("&nbsp").toString();
		  	msg.replaceAll("[<]", "&lt;");
		  	msg.replaceAll("[>]", "&gt;");
		  	String messgae="����ʧ��,";
		  	if(!"".equals(msg)){
		  		messgae +=new XMLDocument(tDirectPayResponse.getValue("Message")).getFormatDocument("&nbsp").toString();
		  	}
		  	messgae+="�������ύ������ϵ�ͷ���";
		  	sf.append("&nbsp;");
		  	sf.append("&nbsp;");
		  	sf.append("<div class=\"quern_no\">");
		  	sf.append("<p>");
			sf.append("   <span class=\"qunrenzit red\">�ύʧ��!</span>   ��лʹ�ü�����������֧������ϵ�ͷ�!");
			sf.append("</p>");;
			sf.append("&nbsp;");
		  	sf.append("<p> <span class=\"qunrenzit red\">������ʾ��</span>" + messgae + "</p>");
		  	sf.append("</div>");
		}
		return sf.toString();
	}

		@Override
		public PayResult getPayResult(HashMap reqs) throws ServiceException {
			
			PayResult bean=null;		
			String payresult=String.valueOf(reqs.get("payresult"));
			logger.info("CMBC_B2B b2b:"+payresult);
			if(!Validator.isNull(payresult)){
				try {
					com.hitrust.B2B.CMBC.util.SecurityUtil.verifyNotifyMessage(new XMLDocument(payresult));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			GenericResponse genericResponse = new GenericResponse(new XMLDocument(payresult));
			String amt = new XMLDocument(genericResponse.getValue("TrnxAmount")).getFormatDocument("&nbsp").toString();
			String Status = new XMLDocument(genericResponse.getValue("Status")).getFormatDocument("&nbsp").toString();
			String TrnxNo = new XMLDocument(genericResponse.getValue("TrnxNo")).getFormatDocument("&nbsp").toString();
			String status = new XMLDocument(genericResponse.getValue("Status")).getFormatDocument("&nbsp").toString();
			bean=new PayResult(TrnxNo,this.bankcode,new BigDecimal(amt),"0".equals(Status)?1:-1);
			bean.setBanktransseq(TrnxNo);
			bean.setBankresult(Status);					
			bean.setEnableFnotice(false);
			if(status!=null&&"0".equals(status)){
				reqs.put("RES","0000");
			}
			return bean;
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


}
