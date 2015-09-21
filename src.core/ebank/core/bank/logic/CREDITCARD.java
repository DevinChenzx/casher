package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.HandleException;
import ebank.core.common.util.MD5sign;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class CREDITCARD extends BankExt implements BankService{

	private String pubkey;		
	
	

	public String sendOrderToBank(BankOrder order) throws ServiceException {
		StringBuffer sf=new StringBuffer("");   

		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\"/>");
		sf.append("<input type=\"hidden\" name=\"partner\" value=\""+this.getCorpid()+"\" />");
		sf.append("<input type=\"hidden\" name=\"_input_charset\" value=\"utf-8\" />");
		sf.append("<input type=\"hidden\" name=\"out_trade_no\" value=\""+order.getRandOrderID()+"\" />");
		sf.append("<input type=\"hidden\" name=\"amount\" value=\""+order.getAmount()+"\" />");
		
		sf.append("<input type=\"hidden\" name=\"unit_price\" value=\"\" />");
		sf.append("<input type=\"hidden\" name=\"num\" value=\"\" />");
		sf.append("<input type=\"hidden\" name=\"info\" value=\"消费\" />");
		
		
		sf.append("<input type=\"hidden\" name=\"notify_url\" value=\""+this.getRecurl()+"&NR=SID_"+this.idx+"\" />");
		sf.append("<input type=\"hidden\" name=\"return_url\" value=\""+this.getRecurl()+"\" />");
		sf.append("<input type=\"hidden\" name=\"version\" value=\"1.0.0\" />");
		sf.append("<input type=\"hidden\" name=\"sign_type\" value=\"MD5\" />");		
		sf.append("<input type=\"hidden\" name=\"sign_msg\" value=\""+generateSignMsg(order)+"\" />");	
		System.out.println(generateSignMsg(order));
		return sf.toString();
	}

	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		PayResult bean = null;
		
		String  signature= String.valueOf(reqs.get("sign_msg"));
		try{
			if(signature.equals(verifySignMsg(reqs))){//签名验证通过				
				if("T".equals(String.valueOf(reqs.get("return_status")))){
				    bean=new PayResult(String.valueOf(reqs.get("out_trade_no")),this.bankcode,new BigDecimal(String.valueOf(reqs.get("amount"))),1);
			        if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(String.valueOf(reqs.get("NR")))){
					  reqs.put("RES","T");					
				    }				   
				}else{
					bean=new PayResult(String.valueOf(reqs.get("out_trade_no")),this.bankcode,new BigDecimal(String.valueOf(reqs.get("amount"))),-1);				
				}				
				bean.setBankresult(String.valueOf(reqs.get("return_status")));
				bean.setBanktransseq(String.valueOf(reqs.get("ori_out_trade_no")));						
			}else{
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);				
			}			
		}catch (Exception e) {
			HandleException.handle(e);
		}
		return bean;
	}
	
	private String generateSignMsg(BankOrder order) 
	{
		String signature="";
		Map sPara = new HashMap();
		sPara.put("partner",this.getCorpid());
		sPara.put("_input_charset","utf-8");
		sPara.put("out_trade_no", order.getRandOrderID());
		sPara.put("amount", order.getAmount());
		sPara.put("unit_price", "");
		
		sPara.put("num","");
		sPara.put("info","消费");
		sPara.put("notify_url", this.getRecurl()+"&NR=SID_"+this.idx);
		sPara.put("return_url", this.getRecurl());

		
		Map sParaNew = MD5sign.ParaFilter(sPara); //除去数组中的空值和签名参数
	    signature = MD5sign.BuildMysign(sParaNew, this.pubkey,"utf-8");//生成签名结
		return signature;
	}
	
	private String verifySignMsg(HashMap reqs) 
	{
		String signature="";
		Map sPara = new HashMap();
		sPara.put("partner",String.valueOf(reqs.get("partner")));
		sPara.put("_input_charset",String.valueOf(reqs.get("_input_charset")));
		sPara.put("out_trade_no", String.valueOf(reqs.get("out_trade_no")));
		sPara.put("ori_out_trade_no", String.valueOf(reqs.get("ori_out_trade_no")));
		sPara.put("amount",String.valueOf(reqs.get("amount")));
		if(null!=reqs.get("unit_price")){
			sPara.put("unit_price",String.valueOf(reqs.get("unit_price")));
		}
		if(null!=reqs.get("num")){
			sPara.put("num",String.valueOf(reqs.get("num")));
		}
        if(null!=reqs.get("info")){
        	sPara.put("info",String.valueOf(reqs.get("info")));
		}
        sPara.put("return_status",String.valueOf(reqs.get("return_status")));
		sPara.put("notify_url",String.valueOf(reqs.get("notify_url")));
		sPara.put("return_url",String.valueOf(reqs.get("return_url")));

		
		Map sParaNew = MD5sign.ParaFilter(sPara); //除去数组中的空值和签名参数
	    signature = MD5sign.BuildMysign(sParaNew, this.pubkey,String.valueOf(reqs.get("_input_charset")));//生成签名结
		return signature;
	}
	
	public String getPubkey() {
		return pubkey;
	}

	public void setPubkey(String pubkey) {
		this.pubkey = pubkey;
	}
	
	
	
	

}
