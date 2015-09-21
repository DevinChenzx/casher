package ebank.core.bank.third;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;


import alipay.AlipayNotify;
import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.common.util.MD5sign;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class Alipay extends BankExt implements BankService {

	static Logger logger = Logger.getLogger(Alipay.class); 	
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
			String service = "create_direct_pay_by_user";
			String partner = this.corpid;
			String return_url = this.getRecurl();
			String notify_url = this.getRecurl()+"&NR=SID_"+this.idx;
			String sign_type="MD5";
			String payment_type = "1";
			String out_trade_no = order.getRandOrderID();
			String _input_charset=getJsonParams(jo,"_input_charset",this.input_charsert);
			String _seller_email=this.sellerEmail;
			String subject=getJsonParams(jo,"subject","B2C");
			String total_fee=order.getAmount();
			String body=getJsonParams(jo,"body",order.getRandOrderID());
			String paymethod = getJsonParams(jo,"paymethod","bankPay");
			String gateID = "";
			if (order.getMp() != null && order.getMp().get("outChannel") != null) {
				gateID = String.valueOf(order.getMp().get("outChannel"));
			}
			String defaultbank = getJsonParams(jo,"defaultbank",gateID);
			String anti_phishing_key = getJsonParams(jo,"anti_phishing_key","");
			String exter_invoke_ip = getJsonParams(jo,"exter_invoke_ip","");
			String extra_common_param = getJsonParams(jo,"extra_common_param","");
			String buyer_email = getJsonParams(jo,"buyer_email","");
			String royalty_type = getJsonParams(jo,"royalty_type","");
			String royalty_parameters = getJsonParams(jo,"royalty_parameters","");
			String error_notify_url = getJsonParams(jo,"error_notify_url","");
			String seller_id = getJsonParams(jo,"seller_id","");
			String buyer_id = getJsonParams(jo,"buyer_id","");
			String seller_account_name = getJsonParams(jo,"seller_account_name","");
			String buyer_account_name = getJsonParams(jo,"buyer_account_name","");
			String price = getJsonParams(jo,"price","");
			String quantity = getJsonParams(jo,"quantity","1");
			String show_url = getJsonParams(jo,"show_url","");
			String need_ctu_check = getJsonParams(jo,"need_ctu_check","");
			String extend_param = getJsonParams(jo,"extend_param","");
			String it_b_pay = getJsonParams(jo,"it_b_pay","");
			String default_login = getJsonParams(jo,"default_login","");
			String product_type = getJsonParams(jo,"product_type","");
			String token = getJsonParams(jo,"token","");
			String item_orders_info = getJsonParams(jo,"item_orders_info","");
			
			//重新加密
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service",service);//
			sParaTemp.put("_input_charset",_input_charset);//
			sParaTemp.put("partner",partner);
			sParaTemp.put("return_url",return_url);
			sParaTemp.put("notify_url",notify_url);
			sParaTemp.put("sign_type",sign_type);
			sParaTemp.put("payment_type",payment_type);
			sParaTemp.put("seller_email",_seller_email);
			sParaTemp.put("out_trade_no",out_trade_no);
			sParaTemp.put("subject",subject);
			sParaTemp.put("total_fee",total_fee);
			sParaTemp.put("body",body);
			sParaTemp.put("paymethod",paymethod);
			sParaTemp.put("defaultbank",defaultbank);
			sParaTemp.put("anti_phishing_key",anti_phishing_key);///
			sParaTemp.put("exter_invoke_ip",exter_invoke_ip);//
			sParaTemp.put("extra_common_param",extra_common_param);//
			sParaTemp.put("buyer_email",buyer_email);
			sParaTemp.put("royalty_type",royalty_type);//
			sParaTemp.put("royalty_parameters",royalty_parameters);	//		
			sParaTemp.put("error_notify_url",error_notify_url);//
			sParaTemp.put("seller_id",seller_id);
			sParaTemp.put("buyer_id",buyer_id);
			sParaTemp.put("seller_account_name",seller_account_name);//
			sParaTemp.put("buyer_account_name",buyer_account_name);//
			sParaTemp.put("price",price);
			sParaTemp.put("quantity",quantity);
			sParaTemp.put("show_url",show_url);
			sParaTemp.put("need_ctu_check",need_ctu_check);//
			sParaTemp.put("extend_param",extend_param);//
			sParaTemp.put("it_b_pay",it_b_pay); //
			sParaTemp.put("default_login",default_login);//
			sParaTemp.put("product_type",product_type);//
			sParaTemp.put("token",token);//
			sParaTemp.put("item_orders_info",item_orders_info);		//	
			
			MD5sign t = new MD5sign();
			Map<String, String> sPara=t.ParaFilter(sParaTemp);
			String CheckValue=t.BuildMysign(sPara,this.pubkey,_input_charset); //获得对应商户的签名数据。
			logger.debug(CheckValue);
		     //拼接form
			List<String> keys = new ArrayList<String>(sParaTemp.keySet());
			sbHtml.append("<form name=\"sendOrder\" action=\"" +this.desturl+ "_input_charset=" + _input_charset + "\" method=\"post\">");
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
		request.remove("RemoteIP");
		request.remove("queryString");
		request.remove("idx");
		boolean verify_result = AlipayNotify.verify(request,this.pubkey,this.corpid);
		String orderId = (String)request.get("out_trade_no");
	    String payAmount=(String)request.get("total_fee");
	    String payResult=(String)request.get("trade_status"); 
	    String dealId=(String)request.get("trade_no"); 
		if(verify_result){
			if("TRADE_FINISHED".equals(payResult)||"TRADE_SUCCESS".equals(payResult)){
				    bean=new PayResult(orderId,this.bankcode,new BigDecimal(payAmount),1);
					bean.setBanktransseq(dealId);
				    if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(String.valueOf(request.get("NR")))){
				    	request.put("RES","success");					
					}
				}else{
					bean=new PayResult(orderId,this.bankcode,new BigDecimal(payAmount),-1);			
				}	
			}else{
				if(("SID_"+this.idx).equals(request.get("NR"))){
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
