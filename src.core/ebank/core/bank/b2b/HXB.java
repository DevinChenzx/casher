package ebank.core.bank.b2b;

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

public class HXB extends BankExt implements BankService {
	
	static Logger logger = Logger.getLogger(HXB.class); 
	
	private String accNo;
	private String accName;

	public String sendOrderToBank(BankOrder order) throws ServiceException {		
		
		
		DirectPayRequest tDirectPayRequest = new DirectPayRequest();
		OrderEntry       tOrderEntry       = new OrderEntry();
		PayInfoEntry     tPayInfoEntry     = new PayInfoEntry();
		OrderDetailEntry tOrderDetailEntry = new OrderDetailEntry();
		ItemEntry[]        tItemEntry      = new ItemEntry[1];
		
		//把商户信息加入
		tDirectPayRequest.setMerchantTrnxNo(order.getRandOrderID());
		tDirectPayRequest.setTrnxCode("P006");
		tDirectPayRequest.setResultNotifyURL(this.getRecurl());
		//把定单信息加入
		tOrderEntry.setOrderNo(order.getOrdernum()==null?order.getRandOrderID():order.getOrdernum());
		tOrderEntry.setOrderAmt(order.getAmount());
		tOrderEntry.setOrderDate(order.getPostdate());
		tOrderEntry.setSellerCorporationAcctNo(this.accNo);  
		tOrderEntry.setSellerCorporationAcctName(this.accName);
		tOrderEntry.setPayType("0");		
		
		//订单明细
		tOrderDetailEntry.setOperName("001");
		tItemEntry[0] = new ItemEntry();
		tItemEntry[0].setPID(order.getOrdernum()==null?order.getRandOrderID():order.getOrdernum());
		tItemEntry[0].setPN("B2B支付");
		tItemEntry[0].setUP(order.getAmount());
		tItemEntry[0].setQTY("1");
		tItemEntry[0].setDES("B2B");
		tOrderDetailEntry.setItems(tItemEntry);

		tOrderEntry.setOrderDetail(tOrderDetailEntry);
		tDirectPayRequest.setOrder(tOrderEntry);
		//把支付信息加入
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
			sf.append("   <span class=\"qunrenzit red\">提交成功!</span>  感谢使用吉卡，您有任何问题或建议发送邮件至service@gicard.net");
			sf.append("</p>");
			sf.append("&nbsp;");
			sf.append("<p> 交易金额：");
			sf.append("  <span class=\"red\">");
			sf.append(""+amt+"元");	
			sf.append("  </span>");
			sf.append("<p> 订单号:");
			sf.append(""+orderno);
			sf.append("</p>");
			sf.append("<p><span class=\"red\">请您登录民生银行企业网银系统，选择待确认的直接支付的转账记录，进行最终支付确认!</span></p>");
			sf.append("</div>");
		}else{
			String msg = new XMLDocument(tDirectPayResponse.getValue(MessageTagName.MESSAGE_TAG)).getFormatDocument("&nbsp").toString();
		  	msg.replaceAll("[<]", "&lt;");
		  	msg.replaceAll("[>]", "&gt;");
		  	String messgae="交易失败,";
		  	if(!"".equals(msg)){
		  		messgae +=new XMLDocument(tDirectPayResponse.getValue("Message")).getFormatDocument("&nbsp").toString();
		  	}
		  	messgae+="请重新提交或者联系客服！";
		  	sf.append("&nbsp;");
		  	sf.append("&nbsp;");
		  	sf.append("<div class=\"quern_no\">");
		  	sf.append("<p>");
			sf.append("   <span class=\"qunrenzit red\">提交失败!</span>   感谢使用吉卡，请重新支付或联系客服!");
			sf.append("</p>");;
			sf.append("&nbsp;");
		  	sf.append("<p> <span class=\"qunrenzit red\">错误提示：</span>" + messgae + "</p>");
		  	sf.append("</div>");
		}
		return sf.toString();
	}

		@Override
		public PayResult getPayResult(HashMap reqs) throws ServiceException {
			
			String idx=String.valueOf(reqs.get("idx"));
			System.out.println("payresult:"+idx);
			if(!Validator.isNull(idx)){
				String[] idxValue=idx.split("\\?");
				String payresult=idxValue[1].split("=")[1]+idxValue[1].split("=")[2];
				XMLDocument xmldoc = new XMLDocument();
				xmldoc.getValue(payresult);
				GenericResponse tDirectPayResponse = new GenericResponse(xmldoc);
				System.out.println(tDirectPayResponse.getValue("Signature"));
				
			}
			
			return null;
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
