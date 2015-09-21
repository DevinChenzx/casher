package ebank.core.bank.third;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Random;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.log4j.Logger;

import com.huifupay.util.MD5Hash;

import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class ChinaPnr extends BankExt implements BankService {

	static Logger logger = Logger.getLogger(ChinaPnr.class); 
	private String pubkey;
	private String respUrl;
	
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		
	    StringBuffer sbHtml = new StringBuffer();
	    HashMap mp=order.getMp();
		JSONObject jo=null;
		if(mp!=null&&mp.get("outJson")!=null&&!"".equals(mp.get("outJson").toString())){
			String outjson=CryptUtil.decrypt(String.valueOf(mp.get("outJson")));
			logger.info(outjson);
			jo=JSONObject.fromObject(outjson);
		}
		String Version = "10";//版本号为10(必输)
		String CmdId="Buy";//消息类型(必输)
		String UsrId=this.corpid;//商户号6位(必输)
		String OrdId=order.getRandOrderID();//订单号10-20位(必输)
		String OrdAmt=order.getAmount();//订单金额(必输)		
		String gateID="";
		if (order.getMp() != null && order.getMp().get("outChannel") != null) {
			gateID = String.valueOf(order.getMp().get("outChannel"));
		}
		String GateId = getJsonParams(jo,"GateId","16");
		String UsrMp = getJsonParams(jo,"UsrMp","");
		String MerPriv=getJsonParams(jo,"MerPriv","");//商户私有域
		String ChkValue="";//签名(必输)
		//获取签名
		String signMsgVal="";
		signMsgVal=appendParam(signMsgVal,Version);
		signMsgVal=appendParam(signMsgVal,CmdId);
		signMsgVal=appendParam(signMsgVal,UsrId);
		signMsgVal=appendParam(signMsgVal,OrdId);
		signMsgVal=appendParam(signMsgVal,OrdAmt);
		signMsgVal=appendParam(signMsgVal,GateId);
		signMsgVal=appendParam(signMsgVal,UsrMp);
		signMsgVal=appendParam(signMsgVal,MerPriv);
		signMsgVal=appendParam(signMsgVal,this.pubkey);
		//生成页签
		MD5Hash m = new MD5Hash();
		System.out.println("signMsgVal:"+signMsgVal);
		ChkValue = m.getMD5ofStr(signMsgVal);
		//拼form串
		StringBuffer sf=new StringBuffer("");		
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" name=\"Version\" value=\""+Version+"\" >");
		sf.append("<input type=\"hidden\" name=\"CmdId\" value=\""+CmdId+"\" >");
		sf.append("<input type=\"hidden\" name=\"UsrId\" value=\""+UsrId+"\" >");
		sf.append("<input type=\"hidden\" name=\"OrdId\" value=\""+OrdId+"\" >");
		sf.append("<input type=\"hidden\" name=\"OrdAmt\" value=\""+OrdAmt+"\" >");
		sf.append("<input type=\"hidden\" name=\"GateId\" value=\""+GateId+"\" >");
		sf.append("<input type=\"hidden\" name=\"UsrMp\" value=\""+UsrMp+"\" >");
		sf.append("<input type=\"hidden\" name=\"MerPriv\" value=\""+MerPriv+"\" >");
		sf.append("<input type=\"hidden\" name=\"ChkValue\" value=\""+ChkValue+"\" >");
		
		sf.append("</form>");		
		if(logger.isDebugEnabled()) logger.debug(sf.toString());
		return sf.toString();
	}

	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		
		PayResult bean = null;
		String 	CmdId  = String.valueOf(reqs.get("CmdId"));
		String 	RespCode = String.valueOf(reqs.get("RespCode"));
		String 	UsrId = String.valueOf(reqs.get("UsrId"));
		String 	TrxId = String.valueOf(reqs.get("TrxId"));
		String 	OrdAmt = String.valueOf(reqs.get("OrdAmt"));
		String 	OrdId = String.valueOf(reqs.get("OrdId"));
		String 	MerPriv = String.valueOf(reqs.get("MerPriv"));
		String 	RetType = String.valueOf(reqs.get("RetType"));
		String 	GateId = String.valueOf(reqs.get("GateId"));
		String 	ChkValue = String.valueOf(reqs.get("ChkValue"));
		
		NameValuePair[] nameValutPair = new NameValuePair[11];
		nameValutPair[0] = new NameValuePair("Version", "10");
		nameValutPair[1] = new NameValuePair("CmdId", CmdId);
		nameValutPair[2] = new NameValuePair("RespCode", RespCode);
		nameValutPair[3] = new NameValuePair("UsrId", UsrId);
		nameValutPair[4] = new NameValuePair("TrxId", TrxId);
		nameValutPair[5] = new NameValuePair("OrdAmt", OrdAmt);
		nameValutPair[6] = new NameValuePair("OrdId", OrdId);
		nameValutPair[7] = new NameValuePair("MerPriv", MerPriv);
		nameValutPair[8] = new NameValuePair("RetType", RetType);
		nameValutPair[9] = new NameValuePair("GateId", GateId);
		nameValutPair[10] = new NameValuePair("ChkValue", ChkValue);
		HttpClient client = new HttpClient(); 
		client.getHttpConnectionManager().getParams().setConnectionTimeout(3000); 
	    // 设置代理服务器地址和端口       
	    HttpMethod method=new GetMethod(this.respUrl);
	    method.setQueryString(EncodingUtil.formUrlEncode(nameValutPair, "GBK"));	
	   
		//判断是后台通知(SID_)还是返回

		    try {
		    	client.executeMethod(method);
		 		String urlContent = method.getResponseBodyAsString();
		 		
				if (urlContent.indexOf("RespCode=000000") >= 0) {
					
					if(RespCode.equals("000000")){
						bean=new PayResult(OrdId,this.bankcode,new BigDecimal(OrdAmt),1);
						if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(String.valueOf(reqs.get("NR")))){
							 reqs.put("RES","RECV_ORD_ID_"+OrdId);					
						}
					}else {
						bean=new PayResult(OrdId,this.bankcode,new BigDecimal(OrdAmt),-1);	
					}
					
					bean.setBankresult(RespCode);
					bean.setBanktransseq(TrxId);						
				}else {
					throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);	
						
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
              //释放连接
			      method.releaseConnection();
			}
			
		return bean;
	}
	
	/**
	 * 拼串
	 * @param returnStr
	 * @param paramValue
	 * @return
	 */
	public String appendParam(String returnStr,String paramValue)
	{
			if(!returnStr.equals(""))
			{
				if(null!=paramValue&&!paramValue.equals(""))
				{
					returnStr=returnStr+paramValue.trim();
				}
			}
			else
			{
				if(null!=paramValue&&!paramValue.equals(""))
				{
				  returnStr=paramValue.trim();
				}
			}	
			return returnStr;
	}
	
	private String getJsonParams(JSONObject jo,String key,String defaults){
		try{
			if(jo!=null) return jo.getString(key)==null?defaults:jo.getString(key);
		}catch(Exception e){
			
		}
		return defaults;
	}

	
	public String getPubkey() {
		return pubkey;
	}

	public void setPubkey(String pubkey) {
		this.pubkey = pubkey;
	}

	public String getRespUrl() {
		return respUrl;
	}

	public void setRespUrl(String respUrl) {
		respUrl = respUrl;
	}
	
	public String generateOrderID() throws ServiceException{
		String str="";
		Random rd=new Random();
		for(int i=0;i<6-this.prefixnum.length();i++) str+=rd.nextInt(10);			
		rd=null;
		return prefixnum+sequenceservice.getCode().substring(6,14)+str;
	} 

}
