package ebank.web.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ebank.core.PayResultService;
import ebank.core.TradePrecardService;

import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.MD5sign;
import ebank.core.model.domain.TradePrecard;
import ebank.web.common.WebConstants;


public class PreCardNotify implements Controller {
	private TradePrecardService tradePrecardService;
	private PayResultService payResultService;
	
	public void setTradePrecardService(TradePrecardService tradePrecardService) {
		this.tradePrecardService = tradePrecardService;
	}
	
	
	public void setPayResultService(PayResultService payResultService) {
		this.payResultService = payResultService;
	}


	private Log log=LogFactory.getLog(this.getClass());	
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {	
		//TradePrecard tradePrecard=new TradePrecard();
		String  SystemID	=(String) request.getParameter("SystemID");
		
		TradePrecard tradePrecardTemp = tradePrecardService
		.selectTradePreCard(request.getParameter("MerID"),request.getParameter("MorderID"),request.getParameter("OrderTime"));
		if(tradePrecardTemp==null){
			log.error("not found:"+request.getParameter("MorderID"));
			response.getWriter().print("fail");
			return null;
		}
		tradePrecardTemp.setSystemid(SystemID);
	
		boolean flag=this.getTradePrecard(request);
		log.info("验证结果:"   +flag);
		if(flag&&"Y".equalsIgnoreCase(request.getParameter("PayStatus"))){
			if(Amount.getIntAmount(request.getParameter("Amount"),2)!=tradePrecardTemp.getAmount()){
				log.info(tradePrecardTemp.getAmount()+"<>"+Amount.getIntAmount(
						String.valueOf(request.getParameter("Amount")), 2));
				response.getWriter().print("fail");
				return null;
			}
			tradePrecardTemp.setPaystatus("Y");
			int i=tradePrecardService.updateTradePreCard(tradePrecardTemp);
			if(i==1){
				log.error("update sucess:"+SystemID);
				payResultService.mapPrecardResult(tradePrecardTemp, true);
				response.getWriter().print("success");
			}else{
				response.getWriter().print("fail");
			}
		}
		else{
			tradePrecardTemp.setPaystatus("N");
			tradePrecardService.updateTradePreCard(tradePrecardTemp);
			response.getWriter().print("fail");
		}
		return null;
			
		
	}
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public boolean getTradePrecard(HttpServletRequest reqs) throws ServiceException{
		
			String signtype		=(String) reqs.getParameter("SignType");
			String Version		=(String) reqs.getParameter("Version");
			String MerId		=(String) reqs.getParameter("MerID") ;
			String  MorderID	=(String) reqs.getParameter("MorderID") ;			
			String  SystemID	=(String) reqs.getParameter("SystemID");	
			String ProductName	=(String) reqs.getParameter("ProductName") ;
			String ProductNum	=(String) reqs.getParameter("ProductNum") ;
			String Amount		=(String) reqs.getParameter("Amount");	
			String CurCode		=(String) reqs.getParameter("CurCode") ;
			String MerRcvURL	=(String) reqs.getParameter("MerRcvURL") ;
			String OrderTime	=(String) reqs.getParameter("OrderTime") ;
			String PayStatus	=(String) reqs.getParameter("PayStatus");
			String SignMsg		=(String) reqs.getParameter("SignMsg");
			String Ext1			=(String) reqs.getParameter("Ext1");
			String Ext2			=(String) reqs.getParameter("Ext2");
			List list=new ArrayList();
			list.add("MerID="+MerId);
			list.add("MorderID="+MorderID);
			list.add("SystemID="+SystemID);
			list.add("ProductName="+ProductName);
			list.add("ProductNum="+ProductNum);
			list.add("Amount="+Amount);
			list.add("CurCode="+CurCode);
			list.add("MerRcvURL="+MerRcvURL);
			list.add("OrderTime="+OrderTime);
			list.add("Ext1="+Ext1);
			list.add("Ext2="+Ext2);
			list.add("PayStatus="+PayStatus);
			MD5sign t = new MD5sign();
			list=t.ParaFilterList(list);
			String CheckValue=t.BuildMysign(list,WebConstants.KEY);
			
			TradePrecard result=null;
			boolean flag=(SignMsg.equals(CheckValue));
			return flag;
		
		
	}
	public static String Verify(String notify_id){
		//获取远程服务器ATN结果，验证是否是君付通服务器发来的请求
		String transport = "http";
		String partner = "100000000000001";
		String veryfy_url = "";
		if(transport.equalsIgnoreCase("https")){
			veryfy_url = "http://epay.reapal.com/cooperate/gateway.do?service=notify_verify";
		} else{
			veryfy_url = "http://interface.reapal.com/verify/notify?";
		}
		veryfy_url = veryfy_url + "partner=" + partner + "&notify_id=" + notify_id;
		String responseTxt = CheckUrl(veryfy_url);
		return responseTxt;
	}
	public static String CheckUrl(String urlvalue){
		String inputLine = "";

		try {
			URL url = new URL(urlvalue);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			inputLine = in.readLine().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return inputLine;
	}
}
