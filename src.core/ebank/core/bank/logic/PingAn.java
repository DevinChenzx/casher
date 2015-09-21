package ebank.core.bank.logic;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import ebank.core.bank.BankService;
import ebank.core.common.ServiceException;
import ebank.core.common.util.NetpaySignature;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.web.common.util.Validator;

public class PingAn extends BankExt implements BankService {
    
	private Logger log = Logger.getLogger(this.getClass());
	
	private static String Currency="RMB";
    private static String TermNo="00000000";
    private static String ReservedField="";
    private static String Version="2.0";
    private String rootCertPath;
    private String fileKeyPath;


	@Override
	public String sendOrderToBank(BankOrder order) throws ServiceException {

		
		StringBuffer sb = new StringBuffer();
		sb.append("<form name=\"sendOrder\" method=\"post\" action=\""+this.getDesturl()+"\">");
		sb.append("<input type=hidden name=\"MechantNo\" value=\""+this.corpid+"\">");
		sb.append("<input type=hidden name=\"OrderNo\" value=\""+order.getRandOrderID()+"\">");
		sb.append("<input type=hidden name=\"OrderDate\" value=\""+getformatDate(order)+"\">");
		sb.append("<input type=hidden name=\"PayAmount\" value=\""+order.getAmount()+"\">");
		sb.append("<input type=hidden name=\"Currency\" value=\""+Currency+"\" >");
		sb.append("<input type=hidden name=\"ReturnURL\" value=\""+this.getRecurl()+"&NR=SID_"+this.idx+"\" >");
		sb.append("<input type=hidden name=\"TermNo\" value=\""+TermNo+"\" >");
		sb.append("<input type=hidden name=\"JumpURL\" value=\""+this.getRecurl()+"\" >");
		sb.append("<input type=hidden name=\"ReservedField\" value=\""+ReservedField+"\" >");
		sb.append("<input type=hidden name=\"Version\" value=\""+Version+"\" >");
		sb.append("<input type=hidden name=\"Signature\" value=\""+generatesignMsg(order)+"\" >");
		sb.append("</form>");
		log.info("sb.toString():"+sb.toString());
		return sb.toString();
	}
	
	
	
	private String generatesignMsg(BankOrder order){
		
		StringBuffer srcData = new StringBuffer();
		 srcData.append("MechantNo=").append(this.corpid)
				.append("&OrderNo=").append(order.getRandOrderID())
				.append("&OrderDate=").append(getformatDate(order))
				.append("&PayAmount=").append(order.getAmount())
				.append("&Currency=").append(Currency)
				.append("&ReturnURL=").append(this.getRecurl()+"&NR=SID_"+this.idx)
				.append("&TermNo=").append(TermNo)
				.append("&JumpURL=").append(this.getRecurl())
				.append("&ReservedField=").append(ReservedField)
				.append("&Version=").append(Version);
		System.out.println("srcData:"+srcData);
		NetpaySignature sign = new NetpaySignature();
		String mysign="";
		try {
			mysign = sign.sign("SHA1withRSA", srcData.toString().getBytes(), fileKeyPath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("mysign:"+mysign);
		return mysign;
	}
	
	

	@Override
	public PayResult getPayResult(HashMap reqs){

		PayResult bean=null;
		//接收平安银行后台通知商户支付接口返回的支付结果数据
		String flag = String.valueOf(reqs.get("PayFlag"));  
		String idx = String.valueOf(reqs.get("idx")); 
		if(Validator.isNull(flag)&&!Validator.isNull(idx)&&idx.indexOf("PayFlag")>0){
			String[] aa = idx.split("\\?");
			String[] bb = aa[1].split("=");
			flag = bb[1];
		}
		String tTime = String.valueOf(reqs.get("TranTime")); 
		String orderno = String.valueOf(reqs.get("OrderNo")); 
		String amt = String.valueOf(reqs.get("PayAmount")); 
		String cur = String.valueOf(reqs.get("Currency")); 
		String signature = String.valueOf(reqs.get("Signature")); 
		StringBuffer srcData = new StringBuffer();
		srcData.append("OrderNo=").append(orderno)
				.append("&PayAmount=").append(amt)
				.append("&Currency=").append(cur)
				.append("&PayFlag=").append(flag)
				.append("&TranTime=").append(tTime);
		NetpaySignature sign = new NetpaySignature();
		try {
			boolean signatureVerifyResult = sign.verify("SHA1withRSA",srcData.toString().getBytes(),new BASE64Decoder().decodeBuffer(signature),rootCertPath);
			
			//判断签名验证结果，处理订单数据，记录支付结果，并返回“商户结果页面”地址
			if (signatureVerifyResult){
				if("1".equals(flag))
					bean = new PayResult(orderno,this.bankcode,new BigDecimal(amt),1);
				else 
					bean = new PayResult(orderno,this.bankcode,new BigDecimal(amt),-1);
				bean.setBankresult(flag);
				
				String bankDate = "";
				if(!Validator.isNull(tTime)){
					
					String cc =  tTime.substring(0, 10);
					bankDate = cc.replaceAll("-", "");
				}
				bean.setBankdate(bankDate); //清算日期
				if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(String.valueOf(reqs.get("NR")))){
					 reqs.put("RES",this.getRecurl());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}



	public String getRootCertPath() {
		return rootCertPath;
	}



	public void setRootCertPath(String rootCertPath) {
		this.rootCertPath = rootCertPath;
	}
	


	public String getFileKeyPath() {
		return fileKeyPath;
	}



	public void setFileKeyPath(String fileKeyPath) {
		this.fileKeyPath = fileKeyPath;
	}



	
	private String getformatDate(BankOrder order){
		
		String sDate = "";
		if(order!=null&&!"".equals(order.getPostdate())&&order.getPostdate().length()>7){
			 sDate = order.getPostdate().substring(0, 4) + "-" + order.getPostdate().substring(4, 6)  + "-" + order.getPostdate().substring(6, 8);
		}
		return sDate;
		
	}



}
