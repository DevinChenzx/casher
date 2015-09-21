package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import beartool.Md5Encrypt;

import ebank.core.STEngineService;
import ebank.core.bank.BankService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.core.remote.HttpClientService;
import ebank.core.remote.HttpMethodCallback;

/**
 * @author Kitian
 * virtual Bank
 */
public class VirtualBank extends BankExt implements BankService{
	private static Logger log = Logger.getLogger(VirtualBank.class);
	private HttpClientService httpClientSerivce;
	private String crypt;
	private String decrypt;
	
	public String sendOrderToBank(BankOrder order) throws ServiceException {		
		final BankOrder forder=order;
		log.info("virtual request Form from:"+forder.getRandOrderID()+" "+this.getBankcode());
		Map<String,Object> mp=httpClientSerivce.getHttpResp("utf-8",
				                       this.getDesturl()+this.crypt,
				                        new HttpMethodCallback() {											
											public PostMethod initMethod(PostMethod method) {
												method.setRequestBody(new NameValuePair[]{
												    new NameValuePair("req-action","req-form"),
													new NameValuePair("req-data",JSONObject.fromObject(forder).toString())
												});
												return method;
											}
										},null,null);
		return mp!=null?String.valueOf(mp.get("response")):"<error>";
	}

	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		final HashMap reqmp=reqs;
		log.info("virtual request PayResult from:"+this.getBankcode());
		Map<String,Object> mp=httpClientSerivce.getHttpResp("utf-8",
                this.getDesturl()+this.decrypt,
                 new HttpMethodCallback() {											
						public PostMethod initMethod(PostMethod method) {
							method.setRequestBody(new NameValuePair[]{
							    new NameValuePair("req-action","res-payresult"),
								new NameValuePair("req-data",JSONObject.fromObject(reqmp).toString())
							});
							return method;
						}
					},null,null);		
		String result_str=String.valueOf(mp.get("response"));
		JSONObject jo=JSONObject.fromObject(result_str);
		String sig = null;
		if(!jo.isEmpty() && !jo.isNullObject()){
			sig=Md5Encrypt.md5(jo.getString("trxnum")+jo.getString("amount")+jo.getString("sts")+this.keyPassword,jo.getString("_input_charset"));
			if(jo.getString("signdata")==null||!jo.getString("signdata").equals(sig)){
				throw new ServiceException(EventCode.SIGN_VERIFY);
			}
			if(mp!=null&&jo!=null){
				PayResult result= new PayResult(jo.getString("trxnum"),
						this.bankcode,
						new BigDecimal(new String(jo.getString("amount"))),
						jo.getInt("sts"));
				if(jo.get("aquiremerchant")!=null) result.setAquiremerchant(String.valueOf(jo.get("aquiremerchant")));
				if(jo.get("authcode")!=null) result.setAuthcode(String.valueOf(jo.get("authcode")));
				if(jo.get("bankamount")!=null) result.setBankamount(new BigDecimal(String.valueOf(jo.get("ankamount"))));
				if(jo.get("bankdate")!=null) result.setBankdate(String.valueOf(jo.get("bankdate")));
				if(jo.get("bankresult")!=null) result.setBankresult(String.valueOf(jo.get("bankresult")));
				if(jo.get("banktransseq")!=null) result.setBanktransseq(String.valueOf(jo.get("banktransseq")));
				if(jo.get("payinfo")!=null) result.setPayinfo(String.valueOf(jo.get("payinfo")));
				if(jo.get("refnum")!=null) result.setRefnum(String.valueOf(jo.get("refnum")));
				return result;
			}
		}
		return null;
	}

	public void setHttpClientSerivce(HttpClientService httpClientSerivce) {
		this.httpClientSerivce = httpClientSerivce;
	}
	
	public void setCrypt(String crypt) {
		this.crypt = crypt;
	}

	public void setDecrypt(String decrypt) {
		this.decrypt = decrypt;
	}

}
