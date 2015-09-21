/*
 * @Id: SPDB.java 14:13:12 2006-11-21
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.anxin.ipg.util.MD5;

import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.AnXinMd5;
import ebank.core.common.util.HandleException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

/**
 * @author 
 * Description: 安欣预付费卡接口
 * 
 */
public class PREPAYCARD extends BankExt implements BankService{
	private Logger log = Logger.getLogger(this.getClass());
	private String servicetype;
	private String datetime;
	private String keystr;
	private String signVal;

	/**
	 * (non-Javadoc)
	 * @see ebank.core.bank.BankService#getPayResult(java.util.HashMap)
	 */
	@SuppressWarnings({ "rawtypes","unchecked" })
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		
		PayResult bean = null;
		//参照安欣接口文档中的规定，组装用于从安欣接收的用于签名的字符串
		String signString = String.valueOf(reqs.get("ax_order_id"))
				+ String.valueOf(reqs.get("mer_id"))
				+ String.valueOf(reqs.get("order_id"))
				+ String.valueOf(reqs.get("order_date"))
				+ String.valueOf(reqs.get("pay_acc_no"))
				+ String.valueOf(reqs.get("trans_amt"))
				+ String.valueOf(reqs.get("order_status"))
				+ String.valueOf(reqs.get("trans_time"));
		
		String chkValue = String.valueOf(reqs.get("sign_value"));
		
		String verifySign = MD5.toStr(signString.concat(this.getKeystr()),"UTF-8");
		log.info("sign_value:"+chkValue+"***verifySign:"+verifySign);
		//得到签名验证结果
		try{
			if(verifySign.equalsIgnoreCase(chkValue)){//签名验证通过				
				if("SUCCESS".equals(String.valueOf(reqs.get("ret_code")))){
				    bean=new PayResult(String.valueOf(reqs.get("order_id")),this.bankcode,new BigDecimal(String.valueOf(reqs.get("trans_amt"))),1);
				}else{
					bean=new PayResult(String.valueOf(reqs.get("order_id")),this.bankcode,new BigDecimal(String.valueOf(reqs.get("trans_amt"))),-1);				
				}				
				bean.setBankresult(String.valueOf(reqs.get("ret_code")));
				//bean.setBanktransseq(String.valueOf(mp.get("AcqSsn"))); //银行流水
				bean.setBankdate(reqs.get("trans_time").equals("")?"":String.valueOf(reqs.get("trans_time")).substring(0, 8)); //清算日期
			}else{
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);				
			}
			if(String.valueOf("SID_"+this.idx).equalsIgnoreCase(String.valueOf(reqs.get("NR")))){
				 reqs.put("RES","true");					
			}
		}catch (Exception e) {
			HandleException.handle(e);
		}
		return bean;
	}

	/**
	 *  (non-Javadoc)
	 * @see ebank.core.bank.BankService#sendOrderToBank(ebank.core.domain.BankOrder)
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException {

		StringBuffer sf=new StringBuffer("");
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\"/>");
		sf.append("<input type=\"hidden\" name=\"service\" value=\""+this.getServicetype()+"\" />");
		sf.append("<input type=\"hidden\" name=\"mer_id\" value=\""+this.getCorpid()+"\" />");
		sf.append("<input type=\"hidden\" name=\"order_id\" value=\""+order.getRandOrderID()+"\" />");
		sf.append("<input type=\"hidden\" name=\"pay_acc_no\" value=\"\" />");
		sf.append("<input type=\"hidden\" name=\"order_date\" value=\""+order.getPostdate().substring(0,8)+"\" />");
		sf.append("<input type=\"hidden\" name=\"trans_amt\" value=\""+order.getAmount()+"\" />");
		sf.append("<input type=\"hidden\" name=\"bg_ret_url\" value=\""+this.getHttprecurl()+"&NR=SID_"+this.idx+"\" />");
		sf.append("<input type=\"hidden\" name=\"page_ret_url\" value=\""+this.getRecurl()+"\" />");
		sf.append("<input type=\"hidden\" name=\"order_desc\" value=\"wonderpay\" />");
		this.setDatetime(dateStr());
		sf.append("<input type=\"hidden\" name=\"sign_time\" value=\""+this.getDatetime()+"\" />");
		sf.append("<input type=\"hidden\" name=\"sign_type\" value=\"1\" />");
		sf.append("<input type=\"hidden\" name=\"sign_value\" value=\""+generateSignMsg(order)+"\" />");
		sf.append("</form>");
		log.info("sf.toString():"+sf.toString());
		return sf.toString();
	}
	
	private String generateSignMsg(BankOrder order)
	{
		//参照安欣接口文档中的规定，组装用于签名的字符串
		String plain = this.getServicetype()+this.getCorpid()+order.getRandOrderID()+""+order.getPostdate().substring(0,8)+order.getAmount()+this.getHttprecurl()+"&NR=SID_"+this.idx+this.getRecurl()+"wonderpay"+this.getDatetime()+"";
		//得到签名字符串
		signVal = AnXinMd5.toStr(plain.concat(this.getKeystr()));
		return signVal;
	}
	
	public String dateStr()
	{
		String date = "";
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		date = sdf.format(d);
		return date;
	}
	
	public String getSignVal() {
		return signVal;
	}

	public void setSignVal(String signVal) {
		this.signVal = signVal;
	}

	public String getKeystr() {
		return keystr;
	}

	public void setKeystr(String keystr) {
		this.keystr = keystr;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getServicetype() {
		return servicetype;
	}

	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}
}
