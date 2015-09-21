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
		    
		//2、生成商户订单查询请求对象
		QueryOrderRequest tRequest = new QueryOrderRequest();
		tRequest.setOrderNo       (tOrderNo          );  //订单号           （必要信息）
		tRequest.enableDetailQuery(tEnableDetailQuery);  //是否查询详细信息 （必要信息）

		//3、传送商户订单查询请求并取得订单查询结果
		TrxResponse tResponse = tRequest.postRequest();

		//4、判断商户订单查询结果状态，进行后续操作
		if (tResponse.isSuccess()) {
		  //5、生成订单对象
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
		  
		  //6、取得订单明细
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
		  //7、商户订单查询失败
			System.out.println("ReturnCode   = [" + tResponse.getReturnCode  () + "]<br>");
			System.out.println("ErrorMessage = [" + tResponse.getErrorMessage() + "]<br>");
		}
	}
	
	public void test(){
		VoidPaymentRequest reqest=new VoidPaymentRequest();
		reqest.setOrderNo("307062500012369744");
		
		TrxResponse tResponse = reqest.postRequest();

//		4、判断取消支付结果状态，进行后续操作
		if (tResponse.isSuccess()) {
		  //5、取消支付成功
		  System.out.println("TrxType   = [" + tResponse.getValue("TrxType"  ) + "]<br>");
		  System.out.println("OrderNo   = [" + tResponse.getValue("OrderNo"  ) + "]<br>");
		  System.out.println("PayAmount = [" + tResponse.getValue("PayAmount") + "]<br>");
		  System.out.println("BatchNo   = [" + tResponse.getValue("BatchNo"  ) + "]<br>");
		  System.out.println("VoucherNo = [" + tResponse.getValue("VoucherNo") + "]<br>");
		  System.out.println("HostDate  = [" + tResponse.getValue("HostDate" ) + "]<br>");
		  System.out.println("HostTime  = [" + tResponse.getValue("HostTime" ) + "]<br>");
		}
		else {
		  //6、取消支付失败
			 System.out.println("ReturnCode   = [" + tResponse.getReturnCode  () + "]<br>");
			 System.out.println("ErrorMessage = [" + tResponse.getErrorMessage() + "]<br>");
      }
	}
	public void test_refund(){
		String tTrxAmountStr = "100";
		float  tTrxAmount    = Float.parseFloat(tTrxAmountStr);

//		2、生成退货请求对象
		RefundRequest tRequest = new RefundRequest();
		tRequest.setOrderNo  ("307061900012330425");  //订单号   （必要信息）
		tRequest.setTrxAmount(tTrxAmount);  //退货金额 （必要信息）

//		3、传送退货请求并取得退货结果
		TrxResponse tResponse = tRequest.postRequest();

//		4、判断退货结果状态，进行后续操作
		if (tResponse.isSuccess()) {
		  //5、退货成功
			System.out.println("TrxType   = [" + tResponse.getValue("TrxType"  ) + "]<br>");
			System.out.println("OrderNo   = [" + tResponse.getValue("OrderNo"  ) + "]<br>");
			System.out.println("TrxAmount = [" + tResponse.getValue("TrxAmount") + "]<br>");
			System.out.println("BatchNo   = [" + tResponse.getValue("BatchNo"  ) + "]<br>");
			System.out.println("VoucherNo = [" + tResponse.getValue("VoucherNo") + "]<br>");
			System.out.println("HostDate  = [" + tResponse.getValue("HostDate" ) + "]<br>");
			System.out.println("HostTime  = [" + tResponse.getValue("HostTime" ) + "]<br>");
		}
		else {
		  //6、退货失败
			System.out.println("ReturnCode   = [" + tResponse.getReturnCode  () + "]<br>");
			System.out.println("ErrorMessage = [" + tResponse.getErrorMessage() + "]<br>");
		}	
	}
	public void test_list(){
		
	    
//		2、生成商户对账单下载请求对象
		String tSettleDate="2003/12/11";
		SettleRequest tRequest = new SettleRequest();
		tRequest.setSettleDate(tSettleDate);               //对账日期YYYY/MM/DD （必要信息）
		tRequest.setSettleType(SettleFile.SETTLE_TYPE_TRX);//对账类型 （必要信息）
		                     //SettleFile.SETTLE_TYPE_TRX：交易对账单

//		3、传送商户对账单下载请求并取得对账单
		TrxResponse tResponse = tRequest.postRequest();

//		4、判断商户对账单下载结果状态，进行后续操作
		if (tResponse.isSuccess()) {
		  //5、商户对账单下载成功，生成对账单对象
		  SettleFile tSettleFile = new SettleFile(tResponse);
		  System.out.println("SettleDate        = [" + tSettleFile.getSettleDate       () + "]<br>");
		  System.out.println("SettleType        = [" + tSettleFile.getSettleType       () + "]<br>");
		  System.out.println("NumOfPayments     = [" + tSettleFile.getNumOfPayments    () + "]<br>");
		  System.out.println("SumOfPayAmount    = [" + tSettleFile.getSumOfPayAmount   () + "]<br>");
		  System.out.println("NumOfRefunds      = [" + tSettleFile.getNumOfRefunds     () + "]<br>");
		  System.out.println("SumOfRefundAmount = [" + tSettleFile.getSumOfRefundAmount() + "]<br>");

		  //6、取得对账单明细
		  String[] tRecords = tSettleFile.getDetailRecords();
		  for(int i = 0; i < tRecords.length; i++) {
			  System.out.println("Record-" + i + " = [" + tRecords[i] + "]<br>");
		  }
		}
		else {
		  //7、商户账单下载失败
			System.out.println("ReturnCode   = [" + tResponse.getReturnCode  () + "]<br>");
			System.out.println("ErrorMessage = [" + tResponse.getErrorMessage() + "]<br>");
		}
	}
}

