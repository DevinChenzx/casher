package ebank.core.logic;

import java.util.HashMap;
import java.util.Map;

import com.hitrust.trustpay.client.TrxResponse;
import com.hitrust.trustpay.client.XMLDocument;
import com.hitrust.trustpay.client.b2c.Order;
import com.hitrust.trustpay.client.b2c.QueryOrderRequest;

import ebank.core.HttpInvokeService;
import ebank.core.common.IsmsConfig;

public class ABCService implements HttpInvokeService {

	
	public Map abc_query(String tOrderNo, String tQueryType) {

		Map resultMap= new HashMap();
        boolean tEnableDetailQuery = false;
        if (tQueryType.equals("1"))
            tEnableDetailQuery = true;

        //2�������̻�������ѯ�������
        QueryOrderRequest tRequest = new QueryOrderRequest();
        tRequest.setOrderNo(tOrderNo);  //������           ����Ҫ��Ϣ��
        tRequest.enableDetailQuery(tEnableDetailQuery);  //�Ƿ��ѯ��ϸ��Ϣ ����Ҫ��Ϣ��

        //3�������̻�������ѯ����ȡ�ö�����ѯ���
        TrxResponse tResponse = tRequest.postRequest();

        //4���ж��̻�������ѯ���״̬�����к�������
        if (tResponse.isSuccess()) {
            //5�����ɶ�������
            Order tOrder = new Order(new XMLDocument(tResponse.getValue("Order")));
            if(tOrder==null){
                resultMap.put("RESCODE","200-01");
                resultMap.put("RESMSG","not found");
            }
            System.out.println("OrderNo      = [" + tOrder.getOrderNo() + "]<br>");
            System.out.println("OrderAmount  = [" + tOrder.getOrderAmount() + "]<br>");
            System.out.println("OrderDesc    = [" + tOrder.getOrderDesc() + "]<br>");
            System.out.println("OrderDate    = [" + tOrder.getOrderDate() + "]<br>");
            System.out.println("OrderTime    = [" + tOrder.getOrderTime() + "]<br>");
            System.out.println("OrderURL     = [" + tOrder.getOrderURL() + "]<br>");
            System.out.println("PayAmount    = [" + tOrder.getPayAmount() + "]<br>");
            System.out.println("RefundAmount = [" + tOrder.getRefundAmount() + "]<br>");
            System.out.println("OrderStatus  = [" + tOrder.getOrderStatus() + "]<br>");
            String result = tOrder.getOrderStatus();
             if(result!=null&&!"".equals(result)){
                 String sts="";
                 if("00".equals(result)){
                	 sts=IsmsConfig.getTrxsts(IsmsConfig.TrxSTS.NOTFOUND);
                 }else if("01".equals(result)){
                	 sts=IsmsConfig.getTrxsts(IsmsConfig.TrxSTS.UNPAID);
                 }else if("02".equals(result)){
                	 sts=IsmsConfig.getTrxsts(IsmsConfig.TrxSTS.UNPAID);
                 }else if("03".equals(result)){
                	 sts=IsmsConfig.getTrxsts(IsmsConfig.TrxSTS.SUCCESS);
                 }else if("04".equals(result)){
                	 sts=IsmsConfig.getTrxsts(IsmsConfig.TrxSTS.SUCCESS);
                 }else if("05".equals(result)){
                	 sts=IsmsConfig.getTrxsts(IsmsConfig.TrxSTS.NOTFOUND);
                 }else {
                	 sts = IsmsConfig.getTrxsts(IsmsConfig.TrxSTS.FAILURE);
                 }
//                  resultMap=[RESCODE:"200-00",BANKCODE: 'ABC', TRXNUM:tOrder.getOrderNo() , TRXAMOUNT: (tOrder.getOrderAmount())*100 as long, TRXDATE: tOrder.getOrderDate(), STS: sts]
                 resultMap.put("RESCODE","200-00");
                 resultMap.put("BANKCODE","ABC");
                 resultMap.put("TRXNUM",tOrder.getOrderNo());
                 resultMap.put("TRXAMOUNT",Long.valueOf((long) ((tOrder.getOrderAmount())*100)));
                 resultMap.put("TRXDATE",tOrder.getOrderDate());
                 resultMap.put("STS",sts);
             }else{
                resultMap.put("RESCODE","200-01");
                resultMap.put("RESMSG","not found");
             }
        }
        else {
            //7���̻�������ѯʧ��
        	System.out.println("ReturnCode   = [" + tResponse.getReturnCode() + "]<br>");
        	System.out.println("ErrorMessage = [" + tResponse.getErrorMessage() + "]<br>");
        	 resultMap.put("RESCODE","200-01");
             resultMap.put("RESMSG","not found");
        }
		return resultMap;
	}

	public String abc_refund(String tOrderNo, String tNewOrderNo,
			long tTrxAmount) {
		// TODO Auto-generated method stub
		return null;
	}

	public String abc_getSettleList(long tSettleDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean tx_next(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	public int tx_batchNext(String batchnum) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean tx_publish() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
