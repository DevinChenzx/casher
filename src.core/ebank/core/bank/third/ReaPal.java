package ebank.core.bank.third;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import reapal.RongpayFunction;


import alipay.AlipayNotify;
import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.common.util.MD5sign;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class ReaPal extends BankExt implements BankService {

	static Logger logger = Logger.getLogger(ReaPal.class); 	
	private String sellerEmail;
	private String input_charsert;
	private String pubkey;
	

	public String sendOrderToBank(BankOrder order) throws ServiceException {
		
		    StringBuffer sbHtml = new StringBuffer();
		    HashMap mp=order.getMp();
			JSONObject jo=null;
			if(mp!=null&&mp.get("outJson")!=null){
				String outjson=CryptUtil.decrypt(String.valueOf(mp.get("outJson")));
				logger.info(outjson);
				jo=JSONObject.fromObject(outjson);
			}
			String service = "online_pay";
			String partner = this.corpid;
			String return_url = this.getRecurl();
			String notify_url = this.getRecurl()+"&NR=SID_"+this.idx;
			String sign_type="MD5";
			String charset=getJsonParams(jo,"charset",this.input_charsert);
			String title=getJsonParams(jo,"subject","B2C");
			String body=getJsonParams(jo,"body",order.getRandOrderID());
			String out_trade_no = order.getRandOrderID();
			String total_fee=order.getAmount();
			String payment_type = "1";
			String paymethod = getJsonParams(jo,"paymethod","bankPay");
			String _seller_email=this.sellerEmail;
			String gateID = "";
			if (order.getMp() != null && order.getMp().get("outChannel") != null) {
				gateID = String.valueOf(order.getMp().get("outChannel"));
			}
			String defaultbank = getJsonParams(jo,"defaultbank",gateID);
			
			
			//重新加密
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service",service);//
			sParaTemp.put("merchant_ID",partner);
			sParaTemp.put("return_url",return_url);
			sParaTemp.put("notify_url",notify_url);
			sParaTemp.put("sign_type",sign_type);
			sParaTemp.put("charset",charset);//
			sParaTemp.put("title",title);
			sParaTemp.put("body",body);
			sParaTemp.put("order_no",out_trade_no);
			sParaTemp.put("total_fee",total_fee);
			sParaTemp.put("payment_type",payment_type);
			sParaTemp.put("paymethod",paymethod);
			sParaTemp.put("seller_email",_seller_email);
			sParaTemp.put("defaultbank",defaultbank);
			
				
			
			MD5sign t = new MD5sign();
			Map<String, String> sPara=t.ParaFilter(sParaTemp);
			String CheckValue=t.BuildMysign(sPara,this.pubkey,charset); //获得对应商户的签名数据。
			logger.debug(CheckValue);
		     //拼接form
			List<String> keys = new ArrayList<String>(sParaTemp.keySet());
			sbHtml.append("<form name=\"sendOrder\" action=\"" +this.desturl+ "charset=" + charset + "\" method=\"post\">");
	        for (int i = 0; i < keys.size(); i++) {
	            String name = (String) keys.get(i);
	            String value = (String) sParaTemp.get(name);
	            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
	        }
	            sbHtml.append("<input type=\"hidden\" name=\"sign\" value=\""+CheckValue+"\"/>");
	        if(logger.isDebugEnabled()) logger.debug("str to alipay:"+sbHtml.toString());
			return sbHtml.toString();
	}

	public PayResult getPayResult(HashMap request) throws ServiceException {
		
		PayResult bean=null;
		String NR = String.valueOf(request.get("NR"));
		request.remove("RemoteIP");
		request.remove("queryString");
		request.remove("idx");
		request.remove("NR");
		String mysign = RongpayFunction.BuildMysign(request,this.pubkey);
		String responseTxt = RongpayFunction.Verify(request.get("notify_id").toString(),this.corpid);
		String orderId = (String)request.get("order_no");
	    String payAmount=(String)request.get("total_fee");
	    String payResult=(String)request.get("trade_status"); 
	    String dealId=(String)request.get("trade_no"); 
	    String sign=(String)request.get("sign"); 
		if(mysign.equals(sign) && responseTxt.equals("true")){
			if("TRADE_FINISHED".equals(payResult)||"TRADE_SUCCESS".equals(payResult)){
				    bean=new PayResult(orderId,this.bankcode,new BigDecimal(payAmount),1);
					bean.setBanktransseq(dealId);
				    if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(NR)){
				    	request.put("RES","success");					
					}
				}else{
					bean=new PayResult(orderId,this.bankcode,new BigDecimal(payAmount),-1);			
				}	
		 }else{
				if(("SID_"+this.idx).equals(NR)){
					 request.put("RES","false");
				}else{
					throw new ServiceException(EventCode.SIGN_VERIFY);
				}
		}		
		return bean;
	}
	
	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	public String getInput_charsert() {
		return input_charsert;
	}

	public void setInput_charsert(String input_charsert) {
		this.input_charsert = input_charsert;
	}

	public String getPubkey() {
		return pubkey;
	}

	public void setPubkey(String pubkey) {
		this.pubkey = pubkey;
	}
	
	private String getJsonParams(JSONObject jo,String key,String defaults){
		try{
			if(jo!=null) return jo.getString(key)==null?defaults:jo.getString(key);
		}catch(Exception e){
			
		}
		return defaults;
	}
	

}
