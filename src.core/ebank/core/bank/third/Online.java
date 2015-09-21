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
import beartool.Md5Encrypt;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class Online extends BankExt implements BankService {

	static Logger logger = Logger.getLogger(Online.class); 	

	private String pubkey;
	private String moneytype;
	private String input_charset;

	public String sendOrderToBank(BankOrder order) throws ServiceException {
		
		    HashMap mp=order.getMp();
			JSONObject jo=null;
			if(mp!=null&&mp.get("outJson")!=null){
				String outjson=CryptUtil.decrypt(String.valueOf(mp.get("outJson")));
				logger.info(outjson);
				jo=JSONObject.fromObject(outjson);
			}
			
			String v_md5info;
			String v_mid = this.corpid;
			String v_url = this.getRecurl();
			String v_oid = order.getRandOrderID();
			String v_amount=order.getAmount();
			String v_moneytype=this.moneytype;
			String key=this.pubkey;
			
			String pay_type=mp.get("pay_type").toString().equals("8")?"2":mp.get("pay_type").toString();//1:d 2:c 3:all
			String text = v_amount+v_moneytype+v_oid+v_mid+v_url+key;   // 拼凑加密串
			v_md5info = Md5Encrypt.md5(text,input_charset).toUpperCase();
			logger.debug(v_md5info);
			
			String gateID = "";
			if (order.getMp() != null && order.getMp().get("outChannel") != null) {
				gateID = String.valueOf(order.getMp().get("outChannel"));
			}
			String defaultbank = getBankCode(getJsonParams(jo,"defaultbank",gateID),pay_type);
				
		     //拼接form
			StringBuffer sf=new StringBuffer("");		
			sf.append("<form name=\"sendOrder\" action=\"" +this.desturl+ "\" method=\"post\">");
			sf.append("<input type=\"hidden\" name=\"v_md5info\" value=\""+v_md5info+"\" >");
			sf.append("<input type=\"hidden\" name=\"v_mid\" value=\""+v_mid+"\" >");
			sf.append("<input type=\"hidden\" name=\"v_oid\" value=\""+v_oid+"\" >");
			sf.append("<input type=\"hidden\" name=\"v_amount\" value=\""+v_amount+"\" >");
			sf.append("<input type=\"hidden\" name=\"v_moneytype\" value=\""+v_moneytype+"\" >");
			sf.append("<input type=\"hidden\" name=\"v_url\" value=\""+v_url+"\" >");
			sf.append("<input type=\"hidden\" name=\"remark2\" value=\"[url:="+v_url+"]\" >");
			sf.append("<input type=\"hidden\" name=\"pmode_id\" value=\""+defaultbank+"\" >");
			sf.append("</form>");		
			if(logger.isDebugEnabled()) logger.debug(sf.toString());
			return sf.toString();
	}

	public PayResult getPayResult(HashMap request) throws ServiceException {
		
		PayResult bean=null;
		String NR = String.valueOf(request.get("NR"));
		request.remove("RemoteIP");
		request.remove("queryString");
		request.remove("idx");
		request.remove("NR");
		String v_oid = (String)request.get("v_oid");		// 订单号
		String v_pmode = (String)request.get("v_pmode");		// 支付方式中文说明，如"中行长城信用卡"
	    String v_pstatus = (String)request.get("v_pstatus");	// 支付结果，20支付完成；30支付失败；
	    String v_pstring = (String)request.get("v_pstring");	// 对支付结果的说明，成功时（v_pstatus=20）为"支付成功"，支付失败时（v_pstatus=30）为"支付失败"
		String v_amount = (String)request.get("v_amount");		// 订单实际支付金额
	    String v_moneytype = (String)request.get("v_moneytype");	// 币种
		String v_md5str = (String)request.get("v_md5str");		// MD5校验码

		String text = v_oid+v_pstatus+v_amount+v_moneytype+this.pubkey;
		String v_md5text = Md5Encrypt.md5(text,input_charset).toUpperCase();
		logger.debug("v_md5text:"+v_md5text);	
		logger.debug("v_md5str:"+v_md5str);	
		if(v_md5text.equals(v_md5str)){
			if("20".equals(v_pstatus)){
				    bean=new PayResult(v_oid,this.bankcode,new BigDecimal(v_amount),1);

				    if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(NR)){
				    	request.put("RES","ok");					
					}
				}else{
					bean=new PayResult(v_oid,this.bankcode,new BigDecimal(v_amount),-1);			
				}	
		 }else{
				if(("SID_"+this.idx).equals(NR)){
					 request.put("RES","error");
				}else{
					throw new ServiceException(EventCode.SIGN_VERIFY);
				}
		}		
		return bean;
	}
	
	public String getMoneytype() {
		return moneytype;
	}

	public void setMoneytype(String moneytype) {
		this.moneytype = moneytype;
	}

	public String getInput_charsert() {
		return input_charset;
	}

	public void setInput_charsert(String input_charsert) {
		this.input_charset = input_charsert;
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
	
	private String getBankCode(String code,String payType)
	{
		String bankcode="";
		
		if(code.equals("ICBC"))
		{	
			if("1".equals(payType)){//借记
				bankcode="1025";
			}else{
				bankcode="1027";
			}
		}else if(code.equals("CCB")){
			if("1".equals(payType)){//借记
				bankcode="1051";
			}else{
				bankcode="1054";
			}
		}else if(code.equals("ABC")){

//			bankcode="103";
			if("1".equals(payType)){//借记
				bankcode="103";
			}else{//无可用通道
				bankcode="1031";
			}
		
		}else if(code.equals("CMB")){
		
			if("1".equals(payType)){//借记
				bankcode="3080";
			}else{
				bankcode="308";
			}
			
		}else if(code.equals("BOC")){
		
			if("1".equals(payType)){//借记
				bankcode="104";
			}else{
				bankcode="106";
			}
		}else if(code.equals("BOCM")){

			if("1".equals(payType)){//借记
				bankcode="301";
			}else{
				bankcode="301";
			}
			
		}else if(code.equals("HXB")){
		
			if("1".equals(payType)){//借记
				bankcode="311";
			}else{
				bankcode="3112";
			}
//			bankcode="3283";
		}else if(code.equals("CIB")){
			
			if("1".equals(payType)){//借记
				bankcode="309";
			}else{
				bankcode="3091";
			}
			
		}else if(code.equals("CMBC")){
		
			if("1".equals(payType)){//借记
				bankcode="305";
			}else{
				bankcode="3051";
			}
			
		}else if(code.equals("GDB")){
			
			if("1".equals(payType)){//借记
				bankcode="3061";
			}else{//无可用通道
				bankcode="306";
			}
		}
		else if(code.equals("SDB")){
			
			if("1".equals(payType)){//借记
				bankcode="307";
			}else{
				bankcode="3071";
			}
		}
		else if(code.equals("SPDB")){
		
			if("1".equals(payType)){//借记
				bankcode="314";
			}else{//无可用通道
				bankcode="3141";
			}
		}else if(code.equals("CITIC")){
			
			if("1".equals(payType)){//借记
				bankcode="313";
			}else{//无可用通道
				bankcode="3131";
			}
		}else if(code.equals("CEB")){
			
			if("1".equals(payType)){//借记
				bankcode="312";
			}else{//无可用通道
				bankcode="3121";
			}
			
		}else {
		
			bankcode="327";
		}
	
		return bankcode;
	}
}
