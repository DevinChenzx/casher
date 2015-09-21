package ebank.core.logic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;

import ebank.core.AccountService;
import ebank.core.common.ServiceException;
import ebank.core.model.dao.BaseDAO;
import ebank.core.model.domain.AccountCommand;
import ebank.core.model.domain.GwViewAccount;
import ebank.core.remote.HttpClientService;
import ebank.core.remote.HttpMethodCallback;

public class AccountService_Impl extends BaseDAO implements AccountService{

	private HttpClientService httpClientService;
	private String httpUrl;
	
	public GwViewAccount getAccount(final String acctnum){
		Map<String,Object> respmp=httpClientService.getHttpResp(httpUrl+"/rpc/queryAcc", new HttpMethodCallback() {				
			public PostMethod initMethod(PostMethod method) {
				Map<String,String> tempmp=new HashMap<String,String>();
				tempmp.put("accountNo",acctnum);			    
				
				JSONObject jo=new JSONObject();
				jo.putAll(tempmp);			
				String body=jo.toString();
				
				log.debug("request body:"+body);
				
			    method.setRequestEntity(new StringRequestEntity(body));				    
				return method;
			}
		},null,null);
		log.info("httpclient response:"+respmp);		
		if(respmp!=null&&"200".equals(String.valueOf(respmp.get("statusCode")))){
			String result=String.valueOf(respmp.get("response"));
			final JSONObject jo=JSONObject.fromObject(result);
			if("true".equals(jo.getString("result"))){
				GwViewAccount va=new GwViewAccount();
				va.setAcct_amount(jo.getLong("balance")+jo.getLong("freezBal"));
				va.setAcct_balance(jo.getLong("balance"));
				va.setAcct_name(jo.getString("accName"));
				va.setAcct_sts(jo.getString("status"));			
				return va;
			}else{
				log.debug(jo.getString("errorMsg"));
				return null;
			}
			
		}else{
			return null;
		}
		
	}
	
	
	public String tx_postpayment(String paymentid) {
		final Map<String, Object> mp=new HashMap<String, Object>();
		mp.put("v_paymentid", paymentid);
		mp.put("v_step","");
		mp.put("v_result","");
		this.getSqlMapClientTemplate().update("gw30.postpayment", mp);
		if("1".equals(String.valueOf(mp.get("v_step")))){
			final String commandNo=String.valueOf(mp.get("v_result"));
			log.debug("tx_postpayment proc call:"+mp);
			  log.info("commandno value syn :"+commandNo);
		       getSqlMapClientTemplate().execute(new SqlMapClientCallback() {  
		           public Object doInSqlMapClient(SqlMapExecutor executor)  
		           throws SQLException {                   
		               Map<String,Object> temp = new HashMap<String,Object>();                           
		                temp.put("accRespCode", "");  
		                temp.put("accSyncFlag", "F"); 
		                temp.put("redo_flag", "F");
		                temp.put("accTransCode","");
		                temp.put("accTransId", "");
		                temp.put("accCommandNo",commandNo);                                           
		                executor.update("gw30.updateCmd",temp);              
		               return null;  
		           }
		       });       
		       try{
		    	   return commandNo; //this.command_action(commandNo);
		       	}catch(Exception e){
		       		e.printStackTrace();
		       		return null;
		       	}
		}else{
			log.debug("postpayment proc exception:" +String.valueOf(mp.get("v_step"))+ String.valueOf(mp.get("v_result")));
			return null;
		}
		
			
			
	}
	
	
    /*
    * 处理账务指令
    */
	public Map<String,Object > tx_command_action(final String commandno) {
		 List<AccountCommand> li=(List<AccountCommand>)this.getSqlMapClientTemplate().queryForList("gw30.getComandsByNo",commandno);
		 final List<Map<String,Object>> clist=new ArrayList<Map<String,Object>>();
		 for (Iterator<AccountCommand> iterator = li.iterator(); iterator.hasNext();) {
			AccountCommand accountCommand = (AccountCommand) iterator
					.next();
			Map<String,Object> cmp=new HashMap<String,Object>();
			cmp.put("commandType", "transfer");
			cmp.put("fromAccountNo", accountCommand.getFrom_account_no());
			cmp.put("toAccountNo", accountCommand.getTo_account_no());
			cmp.put("amount", accountCommand.getAmount());
			cmp.put("transferType", accountCommand.getTransfer_type());
			cmp.put("tradeNo", accountCommand.getTrade_no());
			if( accountCommand.getNote()!=null&& accountCommand.getNote().length()>=64)
				cmp.put("subjict", accountCommand.getNote().substring(0, 63));
			else
				cmp.put("subjict", accountCommand.getNote());
			cmp.put("outTradeNo", accountCommand.getOut_trade_no());
			clist.add(cmp);
		}
		JSONArray ja=new JSONArray();
		ja.addAll(clist);
		final String cmds=ja.toString();
	
		Map<String,Object> respmp = httpClientService.getHttpResp(httpUrl+"/rpc/batchCommand", new HttpMethodCallback() {				
			public PostMethod initMethod(PostMethod method) {				
				
				Map<String,String> tempmp=new HashMap<String,String>();
				tempmp.put("commandSeqno",commandno);
			    tempmp.put("commandLs",cmds);
				
				JSONObject jo=new JSONObject();
				jo.putAll(tempmp);			
				String body=jo.toString();
				
				log.debug("request body:"+body+tempmp);
				
			    method.setRequestEntity(new StringRequestEntity(body));				    
				return method;
			}
		}, null, null);
		
		log.info("httpclient response:"+respmp);
		HashMap<String,Object> retmp=new HashMap<String,Object>();
		if(respmp!=null&&"200".equals(String.valueOf(respmp.get("statusCode")))) {
			String result=String.valueOf(respmp.get("response"));
			final JSONObject jo=JSONObject.fromObject(result);
			retmp.put("acctcode", jo.getString("result"));
			if(jo!=null&&"true".equals(jo.getString("result"))){					 
				final Object[] txids=(Object[])jo.getJSONArray("transIds").toArray();
				 
				 getSqlMapClientTemplate().execute(new SqlMapClientCallback() {  
					 public Object doInSqlMapClient(SqlMapExecutor executor)  
					 throws SQLException {  
					     Map<String,Object> temp = new HashMap<String,Object>();  
					     executor.startBatch();  
					     for(Object eachID : txids) {  
					         temp.put("accRespCode", jo.getString("errorCode"));  
					         temp.put("accSyncFlag", "S"); 
					         temp.put("redo_flag", "F");
					         temp.put("accTransCode", jo.getString("transCode"));
					         temp.put("accTransId", eachID);
					         temp.put("accCommandNo",commandno);					         				         
					         executor.update("gw30.updateCmd",temp);  
					     }
					     executor.executeBatch();  
					     return null;  
					 }
				 });					
			}else{
				log.debug("http请求"+commandno+" result failure:"+jo.getString("result"));
				retmp.put("responseResult", jo.getString("result"));
				retmp.put("responseMsg", jo.getString("errorMsg"));
			}
		}
		
		retmp.put("responseCode", String.valueOf(respmp.get("statusCode")));  
		
		return retmp;
		
		
	}



	public void setHttpClientService(HttpClientService httpClientService) {
		this.httpClientService = httpClientService;
	}
	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}
	
	

}
