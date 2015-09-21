package ebank.core.bank.third;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class IPS extends BankExt implements BankService {

	
	private Logger log = Logger.getLogger(IPS.class);
	private String pubkey;
	
	
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		
		StringBuffer sbHtml = new StringBuffer();		
		HashMap mp=order.getMp();
		JSONObject jo=null;
		if(mp!=null&&mp.get("outJson")!=null){
			String outjson=CryptUtil.decrypt(String.valueOf(mp.get("outJson")));
			log.info(outjson);
			if(!"".equals(outjson)){
				jo=JSONObject.fromObject(outjson);
			}
		}
		//商户号
		String Mer_code = this.corpid;
		//商户订单编号
		String Billno = order.getRandOrderID();
		//订单金额(保留2位小数)
		String Amount = order.getAmount();		 
		//订单日期
		String Date = order.getPostdate();
		if(Date!=null&&!"".equals(Date)&&Date.length()>8){
			Date = Date.substring(0,8);
		}
		System.out.println("Date:"+Date);
		//币种
		String Currency_Type = getJsonParams(jo,"Currency_Type","RMB");
		//支付卡种
		String Gateway_Type = getJsonParams(jo,"Gateway_Type","01");
		//语言
		String Lang = getJsonParams(jo,"Lang","GB");
		//支付结果成功返回的商户URL
		String Merchanturl = this.getRecurl();
		//支付结果失败返回的商户URL
		String FailUrl = getJsonParams(jo,"FailUrl","");
		//支付结果错误返回的商户URL
		String ErrorUrl = getJsonParams(jo,"ErrorUrl","");
		//商户数据包
		String Attach = getJsonParams(jo,"Attach","");
		//订单支付接口加密方式
		String OrderEncodeType = getJsonParams(jo,"OrderEncodeType","2");
		//交易返回接口加密方式 
		String RetEncodeType = getJsonParams(jo,"RetEncodeType","12");
		//返回方式
		String Rettype = getJsonParams(jo,"Rettype","1");
		//Server to Server 返回页面URL
		String ServerUrl = getJsonParams(jo,"ServerUrl",this.getRecurl()+"&NR=SID_"+this.getIdx());
		
		
		//订单支付接口的Md5摘要，原文=订单号+金额+日期+支付币种+商户证书 
		cryptix.jce.provider.MD5 b=new cryptix.jce.provider.MD5();
		String SignMD5 = b.toMD5(Billno + Amount + Date + Currency_Type + this.pubkey).toLowerCase();
		
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("Mer_code",Mer_code);//
		sParaTemp.put("Billno",Billno);//
		sParaTemp.put("Amount",Amount);
		sParaTemp.put("Date",Date);
		sParaTemp.put("Currency_Type",Currency_Type);
		sParaTemp.put("Gateway_Type",Gateway_Type);
		sParaTemp.put("Lang",Lang);		
		sParaTemp.put("Merchanturl",Merchanturl);
		sParaTemp.put("FailUrl",FailUrl);
		sParaTemp.put("ErrorUrl",ErrorUrl);
		sParaTemp.put("Attach",Attach);
		sParaTemp.put("OrderEncodeType",OrderEncodeType);
		sParaTemp.put("RetEncodeType",RetEncodeType);
		sParaTemp.put("Rettype",Rettype);
		sParaTemp.put("ServerUrl",ServerUrl);
		
		  //拼接form
		List<String> keys = new ArrayList<String>(sParaTemp.keySet());
		sbHtml.append("<form name=\"sendOrder\" action=\"" +this.desturl+"\" method=\"post\">");
        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sParaTemp.get(name);
            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }
            sbHtml.append("<input type=\"hidden\" name=\"SignMD5\" value=\""+SignMD5+"\"/>");
        if(log.isDebugEnabled()) log.debug("str to alipay:"+sbHtml.toString());
		return sbHtml.toString();
		
		
	}

	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		
		PayResult bean = null;
		String billno=String.valueOf(reqs.get("billno"));
		String currency_type=String.valueOf(reqs.get("Currency_type"));
		String amount=String.valueOf(reqs.get("amount"));
		String mydate=String.valueOf(reqs.get("date"));
		String succ=String.valueOf(reqs.get("succ"));
		String msg=String.valueOf(reqs.get("msg"));
		String attach=String.valueOf(reqs.get("attach"));
		String ipsbillno=String.valueOf(reqs.get("ipsbillno"));
		String retEncodeType=String.valueOf(reqs.get("retencodetype"));
		String signature=String.valueOf(reqs.get("signature"));
		
		String content=billno + amount + mydate + succ + ipsbillno + currency_type;  //明文：订单编号+订单金额+订单日期+成功标志+IPS订单编号+币种
		
		if(retEncodeType.equals("12"))
		{
			cryptix.jce.provider.MD5 b=new cryptix.jce.provider.MD5();
			String SignMD5 = b.toMD5(content + this.pubkey).toLowerCase();			
			if(SignMD5.equals(signature))
			{
				if("Y".equals(succ)){
					bean=new PayResult(billno,this.bankcode,new BigDecimal(amount),1);
					if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(String.valueOf(reqs.get("NR")))){
						 reqs.put("RES","ipscheckok");					
					}
				}else{
					bean=new PayResult(billno,this.bankcode,new BigDecimal(amount),-1);
				}
				
				bean.setBankresult(succ);
				bean.setBanktransseq(ipsbillno);	
				bean.setBankdate(mydate.substring(0,8));
				
			}else{
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);	
			}
		
		}else{
			throw new ServiceException(EventCode.WEB_PARAMEMPTY);	
		}
		
		
		return bean;
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
