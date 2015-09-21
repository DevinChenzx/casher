/*
 * Created on 2004-11-30
 *
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import org.apache.log4j.Logger;


import com.hitrust.trustpay.client.TrxResponse;
import com.hitrust.trustpay.client.b2c.Order;
import com.hitrust.trustpay.client.b2c.PaymentRequest;
import com.hitrust.trustpay.client.b2c.PaymentResult;

import ebank.core.bank.BankService;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;



/**
 * @author 
 * Description: ũҵ����
 * 
 */
public class ABC extends BankExt implements BankService{
	static Logger logger = Logger.getLogger(ABC.class); 	

	private String getPAURL(BankOrder order){
		String PAURL="";
		//1��ȡ��֧����������Ҫ����Ϣ
		String tOrderURL        = this.getRecurl();//"http://manager.chinaebank.cn/abcchina/MerchantQueryOrder.jsp?ON="+order.getRandOrderID()+"&QueryType=1";
		String tProductType     = "1";//��Ʒ����
		//���ݿͻ���������֧�����ͣ��ж��Ƿ�֧����ʽ
		String tPaymentType="";
		if(order.getPayment_type()!=null&&!"".equals(order.getPayment_type())&&order.getPayment_type().equals("1")){
			 tPaymentType     = "1";//1��ũ�п�֧��  
		}else if(order.getPayment_type()!=null&&!"".equals(order.getPayment_type())&&order.getPayment_type().equals("2")){
			tPaymentType     = "3";//3�����ǿ�֧��  
		}else{
			tPaymentType     = "A";//3����ǿ������ǿ�֧��
		}
		//String tResultNotifyURL = "http://receive.chinaebank.cn/abcchina/payment.jsp";//֧������ش���ַ
		//String tMerchantRemarks = v_url;//�̻���ע��Ϣ
//		2�����ɶ�������
		Order tOrder = new Order();
		tOrder.setOrderNo    (order.getRandOrderID()); //�趨������� ����Ҫ��Ϣ��
		tOrder.setOrderDesc  (order.getOrdernum()==null?order.getRandOrderID():order.getOrdernum());    //�趨����˵��
		Date curdate=null;
		try {
			curdate=new SimpleDateFormat("yyyyMMddHHmmss").parse(order.getPostdate());
		} catch (Exception e) {
			logger.warn("paser date exception,use sysdate");
			curdate=new Date();
		}	
		//
		String hour=new SimpleDateFormat("HH").format(curdate);
		int min=curdate.getMinutes();
		if((hour.equals("01")&&min<28)
		   ||(hour.equals("00")&&min>55)){
			return "F:<font color=red>ũҵ���� 1:00-1:30 ʱ��ϵͳ����,ϵͳ���ȶ�,<br>��ʱ����޷�����,�����׿��ܵ���֧��ʧ�ܡ�����ϵͳʱ��:"+hour+"ʱ"+min+"��,���Ժ�֧��</font>";			
		}
		tOrder.setOrderDate  (new SimpleDateFormat("yyyy/MM/dd",Locale.US).format(curdate)); //�趨�������� ����Ҫ��Ϣ - YYYY/MM/DD��
		tOrder.setOrderTime  (new SimpleDateFormat("HH:mm:ss",Locale.US).format(curdate)); //�趨����ʱ�� ����Ҫ��Ϣ - HH:MM:SS��
		tOrder.setOrderAmount(Double.parseDouble(order.getAmount())); //�趨������� ����Ҫ��Ϣ��
		tOrder.setOrderURL   (tOrderURL   ); //�趨������ַ
		
		
//		3�����ɶ����������󣬲���������ϸ���붨���У���ѡ��Ϣ��
//		tOrder.addOrderItem(new OrderItem("IP000001", "�й��ƶ�IP��", 100.00f, 1));
//		tOrder.addOrderItem(new OrderItem("IP000002", "��ͨIP��"    ,  90.00f, 2));

//		4������֧���������
		PaymentRequest tPaymentRequest = new PaymentRequest();
		tPaymentRequest.setOrder      (tOrder      ); //�趨֧������Ķ��� ����Ҫ��Ϣ��
		tPaymentRequest.setProductType(tProductType); //�趨��Ʒ���� ����Ҫ��Ϣ��
		                                              //PaymentRequest.PRD_TYPE_ONE����ʵ����Ʒ�������IP��������MP3��...
		                                              //PaymentRequest.PRD_TYPE_TWO��ʵ����Ʒ
		tPaymentRequest.setPaymentType(tPaymentType); //�趨֧������
		                                              //PaymentRequest.PAY_TYPE_ABC��ũ�п�֧��
		                                              //PaymentRequest.PAY_TYPE_INT�����ʿ�֧��
		tPaymentRequest.setNotifyType("1");
		tPaymentRequest.setResultNotifyURL(this.httprecurl+"?idx=103"); //�趨֧������ش���ַ ����Ҫ��Ϣ��
		tPaymentRequest.setMerchantRemarks(order.getMerchantid()); //�趨�̻���ע��Ϣ
		TrxResponse tTrxResponse = tPaymentRequest.postRequest();
		if (tTrxResponse.isSuccess()) {
			PAURL=tTrxResponse.getValue("PaymentURL");
		}else{
			PAURL="E:"+tTrxResponse.getReturnCode()+"@#@"+tTrxResponse.getErrorMessage();
		}
		tPaymentRequest=null;
		tTrxResponse=null;
		tOrder=null;
		curdate=null;
		tOrderURL=null;
		tProductType=null;
		tPaymentType=null;
		return PAURL;
	}
	
	/**
	 * ����ҳ����ת�Ĵ���
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException{
		//����������
		//order.setRandOrderID(generateOrderID());
		StringBuffer sf=new StringBuffer("");
		String strAction=getPAURL(order);
		if(strAction.startsWith("E:")){
			String[] strs=strAction.split("@#@");
			sf.append("ũҵ����֧���ӿڹ���,�ܱ�Ǹ���Ժ���֧��<br>");
			sf.append("ReturnCode   = [" + strs[0] + "]<br>");
			sf.append("ErrorMessage = [" + strs[1] + "]<br>");
		}else if(strAction.startsWith("F:")){
			sf.append(strAction.substring(2));			
		}else{
			sf.append("<form name=sendOrder method=\"post\" action=\""+strAction+"\"></form>");
			//sf.append("<body onload=\"javascript:window.location.href='"+strAction+"';\">");
			if(logger.isDebugEnabled()) logger.debug("abc action url="+strAction);			
		}
		strAction=null;
		logger.info(sf.toString());
		return sf.toString();
	}
	
	/***
	 * ֧�����
	 * @param ����request�õ��Ĳ���Map
	 * @return 
	 */
	public PayResult getPayResult(HashMap reqs) throws ServiceException
	{		
		PayResult bean=null;
		boolean bln=false;
		String msg=String.valueOf(reqs.get("MSG"));
		try{
			PaymentResult tResult = new PaymentResult(msg);
			String OrderNo = tResult.getValue("OrderNo");
			String Amount = tResult.getValue("Amount");				
			//2���ж�֧�����״̬�����к�������						
			bln=tResult.isSuccess();			
			bean=new PayResult(OrderNo,this.bankcode,new BigDecimal(Amount),bln?1:-1);			
			bean.setBankresult(String.valueOf(bln));						
			tResult=null;		
			OrderNo=null;
			Amount=null;
		}catch(Exception e){
			HandleException.handle(e);
		}
		return bean;
	}	
	
}
