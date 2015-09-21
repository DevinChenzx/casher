package ebank.core.bank.b2b;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import beartool.PKCS7Tool;

import ebank.core.STEngineService;
import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class BOC extends BankExt implements BankService {

	private String dn;
	private String keyStorePath;
	private String keyStorePassword;
	private String rootCertificatePath;
	private STEngineService engineService;
	
	private Log log=LogFactory.getLog(this.getClass());

	public String sendOrderToBank(BankOrder order) throws ServiceException {
		
		String plain=this.corpid+"|"+order.getRandOrderID()+"|001|"+order.getAmount()+"|"+order.getPostdate();
		String signData=getSignData(plain);
		if(signData==null) throw new ServiceException(EventCode.BOC_SIGNERROR);
		StringBuffer sf=new StringBuffer();
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" name=\"bocNo\" value=\""+this.corpid+"\" />");  //商户号
		sf.append("<input type=\"hidden\" name=\"orderNo\" value=\""+order.getRandOrderID()+"\">"); //商户订单号
		sf.append("<input type=\"hidden\" name=\"curCode\" value=\"001\" />");  //订单币种
		sf.append("<input type=\"hidden\" name=\"orderAmount\" value=\""+order.getAmount()+"\" />");//订单金额
		sf.append("<input type=\"hidden\" name=\"orderTime\" value=\""+order.getPostdate()+"\" />");//订单时间
		sf.append("<input type=\"hidden\" name=\"orderUrl\" value=\""+this.getRecurl()+"\">");//返回商户页面
		String orderNote=order.getOrdernum();
		if(orderNote.length()>30) orderNote=orderNote.substring(0, 29);
		else orderNote=order.getRandOrderID();
		sf.append("<input type=\"hidden\" name=\"orderNote\" value=\""+orderNote+"\" />");	//订单说明
		sf.append("<input type=\"hidden\" name=\"signData\" value=\""+signData+"\">");
		sf.append("</form>");
		log.info("BOC b2b:"+sf.toString());
		return sf.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		
		log.info(reqs);
		String tranSeq = String.valueOf(reqs.get("tranSeq"));//交易流水number
		String tranCode = String.valueOf(reqs.get("tranCode"));//交易类型
		String tranStatus = String.valueOf(reqs.get("tranStatus"));//交易状态
		String tranTime = String.valueOf(reqs.get("tranTime"));//交易时间
		String payAmount = String.valueOf(reqs.get("orderAmount"));//订单金额number
		String feeAmount = String.valueOf(reqs.get("feeAmount"));//订单费用number
		String curCode = String.valueOf(reqs.get("curCode"));//货币
		
		String bocNo = String.valueOf(reqs.get("bocNo"));//网关商户号
		String orderSeq = String.valueOf(reqs.get("orderSeq"));//订单流水
		String orderNo = String.valueOf(reqs.get("orderNo"));//商户订单号
		
		String signData = String.valueOf(reqs.get("signData"));
		//交易流水|交易类型|交易状态|交易时间|交易金额|交易费用|商户号|商户订单号|币种|
		String plain=tranSeq+"|"+tranCode+"|"+tranStatus+"|"+tranTime+"|"+payAmount+"|"+feeAmount+"|"+bocNo+"|"+orderNo+"|"+curCode;		
		PayResult bean=null;		
		if(signData!=null&&!"null".equalsIgnoreCase(signData)){
			if(verify(signData,plain)){ //支付成功
				if("1".equals(tranStatus)){
					bean=new PayResult(orderNo,this.bankcode,new BigDecimal(payAmount),
					           "1".equals(tranStatus)?1:-1);
					bean.setBanktransseq(orderSeq);
					bean.setBankresult(tranStatus);					
					bean.setEnableFnotice(false);	
			    }else{
			    	throw new ServiceException("501125"); //支付失败
			    }
		    }else{
		    	throw new ServiceException(EventCode.CRYPT_VALIADATESIGN); //验证签名失败
		    }
			if(this.idx.equals(reqs.get("NR"))){
				reqs.put("RES",orderSeq+orderNo);
			}				
		}			
		return bean;
	}
	
	/***
	 * 得到接收验证URL
	 * @return String
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	public String getRcvURL(HashMap reqs){
		String strOutput="notfrombank";		
		try{		
			PayResult result=this.getPayResult(reqs);
			if(result!=null&&result.getTrxsts()==1){
				strOutput="CBMD5";
				String state=result.getTrxsts()==1?Constants.STATE_STR_OK:Constants.STATE_STR_FAIL;	
				engineService.post(result);	
			}			
		}catch(Exception e){
			e.printStackTrace();
		}
		return this.recurl+(this.recurl.indexOf("?")>0?"&":"?")+"v_md5info="+strOutput;
	}
	
	public String getSignData(String plain){
		try {
			String keypass = this.keyPassword;
			return PKCS7Tool.getSigner(this.keyStorePath, this.keyStorePassword, keypass).sign(plain.getBytes());																			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean verify(String signData,String plain){
		try {
			PKCS7Tool tools=PKCS7Tool.getVerifier(rootCertificatePath);
			if(tools!=null){
				tools.verify(signData, plain.getBytes(), this.dn);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	public String getRootCertificatePath() {
		return rootCertificatePath;
	}
	public void setRootCertificatePath(String rootCertificatePath) {
		this.rootCertificatePath = rootCertificatePath;
	}
	public STEngineService getEngineService() {
		return engineService;
	}
	public void setEngineService(STEngineService engineService) {
		this.engineService = engineService;
	}
}
