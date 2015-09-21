/*
 * @Id: ABC_Test.java 16:23:11 2007-6-19
 * 
 * @author xiexh@chinabank.com.cn
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.test;



import java.util.ArrayList;

import com.hitrust.trustpay.client.TrxResponse;
import com.hitrust.trustpay.client.XMLDocument;
import com.hitrust.trustpay.client.b2c.Order;
import com.hitrust.trustpay.client.b2c.OrderItem;
import com.hitrust.trustpay.client.b2c.QueryOrderRequest;
import com.hitrust.trustpay.client.b2c.RefundRequest;
import com.hitrust.trustpay.client.b2c.SettleFile;
import com.hitrust.trustpay.client.b2c.SettleRequest;
import com.hitrust.trustpay.client.b2c.VoidPaymentRequest;

import junit.framework.TestCase;

public class ABC_Test extends TestCase{

	public void testquery(){
		String tOrderNo   = "30003";
		String tQueryType = "1";
		boolean tEnableDetailQuery = false;
		if (tQueryType.equals("1"))
		    tEnableDetailQuery = true;
		    
		//2�������̻�������ѯ�������
		QueryOrderRequest tRequest = new QueryOrderRequest();
		tRequest.setOrderNo       (tOrderNo          );  //������           ����Ҫ��Ϣ��
		tRequest.enableDetailQuery(tEnableDetailQuery);  //�Ƿ��ѯ��ϸ��Ϣ ����Ҫ��Ϣ��

		//3�������̻�������ѯ����ȡ�ö�����ѯ���
		TrxResponse tResponse = tRequest.postRequest();

		//4���ж��̻�������ѯ���״̬�����к�������
		if (tResponse.isSuccess()) {
		  //5�����ɶ�������
		  Order tOrder = new Order(new XMLDocument(tResponse.getValue("Order")));
		  System.out.println("OrderNo      = [" + tOrder.getOrderNo     () + "]<br>");
		  System.out.println("OrderAmount  = [" + tOrder.getOrderAmount () + "]<br>");
		  System.out.println("OrderDesc    = [" + tOrder.getOrderDesc   () + "]<br>");
		  System.out.println("OrderDate    = [" + tOrder.getOrderDate   () + "]<br>");
		  System.out.println("OrderTime    = [" + tOrder.getOrderTime   () + "]<br>");
		  System.out.println("OrderURL     = [" + tOrder.getOrderURL    () + "]<br>");
		  System.out.println("PayAmount    = [" + tOrder.getPayAmount   () + "]<br>");
		  System.out.println("RefundAmount = [" + tOrder.getRefundAmount() + "]<br>");
		  System.out.println("OrderStatus  = [" + tOrder.getOrderStatus () + "]<br>");
		  
		  //6��ȡ�ö�����ϸ
		  ArrayList tOrderItems = tOrder.getOrderItems();
		  for(int i = 0; i < tOrderItems.size(); i++) {
		    OrderItem tOrderItem = (OrderItem) tOrderItems.get(i);
		    System.out.println("ProductID   = [" + tOrderItem.getProductID  () + "]<br>");
		    System.out.println("ProductName = [" + tOrderItem.getProductName() + "]<br>");
		    System.out.println("UnitPrice   = [" + tOrderItem.getUnitPrice  () + "]<br>");
		    System.out.println("Qty         = [" + tOrderItem.getQty        () + "]<br>");
		  }
		}
		else {
		  //7���̻�������ѯʧ��
			System.out.println("ReturnCode   = [" + tResponse.getReturnCode  () + "]<br>");
			System.out.println("ErrorMessage = [" + tResponse.getErrorMessage() + "]<br>");
		}
	}
	
	public void test(){
		VoidPaymentRequest reqest=new VoidPaymentRequest();
		reqest.setOrderNo("307062500012369744");
		
		TrxResponse tResponse = reqest.postRequest();

//		4���ж�ȡ��֧�����״̬�����к�������
		if (tResponse.isSuccess()) {
		  //5��ȡ��֧���ɹ�
		  System.out.println("TrxType   = [" + tResponse.getValue("TrxType"  ) + "]<br>");
		  System.out.println("OrderNo   = [" + tResponse.getValue("OrderNo"  ) + "]<br>");
		  System.out.println("PayAmount = [" + tResponse.getValue("PayAmount") + "]<br>");
		  System.out.println("BatchNo   = [" + tResponse.getValue("BatchNo"  ) + "]<br>");
		  System.out.println("VoucherNo = [" + tResponse.getValue("VoucherNo") + "]<br>");
		  System.out.println("HostDate  = [" + tResponse.getValue("HostDate" ) + "]<br>");
		  System.out.println("HostTime  = [" + tResponse.getValue("HostTime" ) + "]<br>");
		}
		else {
		  //6��ȡ��֧��ʧ��
			 System.out.println("ReturnCode   = [" + tResponse.getReturnCode  () + "]<br>");
			 System.out.println("ErrorMessage = [" + tResponse.getErrorMessage() + "]<br>");
      }
	}
	public void test_refund(){
		String tTrxAmountStr = "100";
		float  tTrxAmount    = Float.parseFloat(tTrxAmountStr);

//		2�������˻��������
		RefundRequest tRequest = new RefundRequest();
		tRequest.setOrderNo  ("307061900012330425");  //������   ����Ҫ��Ϣ��
		tRequest.setTrxAmount(tTrxAmount);  //�˻���� ����Ҫ��Ϣ��

//		3�������˻�����ȡ���˻����
		TrxResponse tResponse = tRequest.postRequest();

//		4���ж��˻����״̬�����к�������
		if (tResponse.isSuccess()) {
		  //5���˻��ɹ�
			System.out.println("TrxType   = [" + tResponse.getValue("TrxType"  ) + "]<br>");
			System.out.println("OrderNo   = [" + tResponse.getValue("OrderNo"  ) + "]<br>");
			System.out.println("TrxAmount = [" + tResponse.getValue("TrxAmount") + "]<br>");
			System.out.println("BatchNo   = [" + tResponse.getValue("BatchNo"  ) + "]<br>");
			System.out.println("VoucherNo = [" + tResponse.getValue("VoucherNo") + "]<br>");
			System.out.println("HostDate  = [" + tResponse.getValue("HostDate" ) + "]<br>");
			System.out.println("HostTime  = [" + tResponse.getValue("HostTime" ) + "]<br>");
		}
		else {
		  //6���˻�ʧ��
			System.out.println("ReturnCode   = [" + tResponse.getReturnCode  () + "]<br>");
			System.out.println("ErrorMessage = [" + tResponse.getErrorMessage() + "]<br>");
		}	
	}
	public void test_list(){
		
	    
//		2�������̻����˵������������
		String tSettleDate="2003/12/11";
		SettleRequest tRequest = new SettleRequest();
		tRequest.setSettleDate(tSettleDate);               //��������YYYY/MM/DD ����Ҫ��Ϣ��
		tRequest.setSettleType(SettleFile.SETTLE_TYPE_TRX);//�������� ����Ҫ��Ϣ��
		                     //SettleFile.SETTLE_TYPE_TRX�����׶��˵�

//		3�������̻����˵���������ȡ�ö��˵�
		TrxResponse tResponse = tRequest.postRequest();

//		4���ж��̻����˵����ؽ��״̬�����к�������
		if (tResponse.isSuccess()) {
		  //5���̻����˵����سɹ������ɶ��˵�����
		  SettleFile tSettleFile = new SettleFile(tResponse);
		  System.out.println("SettleDate        = [" + tSettleFile.getSettleDate       () + "]<br>");
		  System.out.println("SettleType        = [" + tSettleFile.getSettleType       () + "]<br>");
		  System.out.println("NumOfPayments     = [" + tSettleFile.getNumOfPayments    () + "]<br>");
		  System.out.println("SumOfPayAmount    = [" + tSettleFile.getSumOfPayAmount   () + "]<br>");
		  System.out.println("NumOfRefunds      = [" + tSettleFile.getNumOfRefunds     () + "]<br>");
		  System.out.println("SumOfRefundAmount = [" + tSettleFile.getSumOfRefundAmount() + "]<br>");

		  //6��ȡ�ö��˵���ϸ
		  String[] tRecords = tSettleFile.getDetailRecords();
		  for(int i = 0; i < tRecords.length; i++) {
			  System.out.println("Record-" + i + " = [" + tRecords[i] + "]<br>");
		  }
		}
		else {
		  //7���̻��˵�����ʧ��
			System.out.println("ReturnCode   = [" + tResponse.getReturnCode  () + "]<br>");
			System.out.println("ErrorMessage = [" + tResponse.getErrorMessage() + "]<br>");
		}
	}
}

