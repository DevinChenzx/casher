/*
 * @Id: BankExt.java 11:42:51 2006-2-22
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.bank.logic;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;


import ebank.core.SequenceService;
import ebank.core.common.ServiceException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

/**
 * @author 
 * Description: 银行配置属性
 * 
 */
public class BankExt {
	
	Logger log = Logger.getLogger(this.getClass());
	
	protected String bankname;
	protected String bankcode;
	
	protected String idx;       //服务索引
	
	protected String paytype;   //支付类型（业务类型)
	
	protected String corpid;    //商户号	
	protected String corpname;  //
	protected String feerate;
	protected String billaccount;	
	protected String prefixnum=""; //订单号前缀
	
	protected String recurl;    //接收URL
	protected String desturl;   //服务url
	protected String httprecurl;    //后台接收URL
	
	protected SequenceService sequenceservice;  //序列服务
	protected String keyPassword;
	protected String cardtype;  //卡类型(0+取卡号第一位)
	protected boolean enabled=true;  //是否开通
	protected boolean multiCurrency; //是否支持多币种
	
	protected String paramJson;      //JSON参数
	protected String paramPath;      //参数资源位置
	protected String relativePath;   //相对路径
	
	//for 快捷支付
	protected String des;
	protected String md5;
	protected String version;
	protected String terminal;
	
	
	
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	//18位订单
	public String generateOrderID() throws ServiceException{
		String RandOrderID=sequenceservice.getCode();	//yyDDD(6)+8位序列			
		Random rd=new Random();
		String str="";	
		int length=this.prefixnum==null?0:this.prefixnum.length();
		for(int i=0;i<4-length;i++) str+=rd.nextInt(10);
		rd=null;		
		return prefixnum+RandOrderID+str;
	}
	public static void main(String[] args) throws Exception {
		Random rd=new Random();
		String prefixnum="";
		String str="";	
		int length=prefixnum==null?0:prefixnum.length();
		for(int i=0;i<4-length;i++) str+=rd.nextInt(10);
//		rd=null;		
		System.out.println(str); 
	}
	public void config(){		
		if(paramPath!=null){
			File f=null;
			try {				
				f = new File(paramPath);
				if(!f.exists())
				{
					System.out.println("file:"+paramPath+" does not exist");
				    return ;
				}
				FileInputStream fis = new FileInputStream(f);
				byte[] b = new byte[(int)f.length()];
				fis.read(b);
				this.paramJson=new String(b);
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					if(f!=null) f.delete();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
			}
			
		}
		if(paramJson!=null&&!"".equals(paramJson)){
			JSONObject jo=JSONObject.fromObject(paramJson);
			if(jo!=null&&!jo.isNullObject())
			for (Iterator iterator = jo.keys(); iterator.hasNext();) {
				String key=(String)iterator.next();	
				try {									
					Field field = BankExt.class.getDeclaredField(key);
					field.setAccessible(true);					
					field.set(this, jo.get(key));
				} catch (Exception e) {					
					log.debug("Field not found key:"+key+ " on BankExt");
					try{
						Field field2=this.getClass().getDeclaredField(key);
						field2.setAccessible(true);	
						if(jo.get(key) instanceof String){
							String x=jo.getString(key);
							if((x.contains(File.separator)||x.contains("\\")||x.contains("/"))
									&&this.relativePath!=null&&!x.contains("//")){ //exclude http:// d://
								field2.set(this, this.relativePath+File.separator+x);
							}else{
								field2.set(this, jo.get(key));
							}
						}else
							field2.set(this, jo.get(key));
					}catch(Exception ex){
						log.debug("Field not found key:"+key+ " on "+this.getClass().getName());
						ex.printStackTrace();
					}
					
				}			
			}
		}		
		//be overide
	}
	
	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	public String getParamJson() {
		return paramJson;
	}
	public void setParamJson(String paramJson) {
		this.paramJson = paramJson;
	}
	public String getParamPath() {
		return paramPath;
	}
	public void setParamPath(String paramPath) {
		this.paramPath = paramPath;
	}
	/**
	 * @return Returns the enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}


	/**
	 * @param enabled The enabled to set.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	/**
	 * @return Returns the cardtype.
	 */
	public String getCardtype() {
		return cardtype;
	}


	/**
	 * @param cardtype The cardtype to set.
	 */
	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}


	/**
	 * @return Returns the idx.
	 */
	public String getIdx() {
		return idx;
	}

	/**
	 * @param idx The idx to set.
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}

	/* (non-Javadoc)
	 * @see ebank.core.service.bank.BankService#getRcvURL(java.util.HashMap)
	 */
	public String getRcvURL(HashMap reqs) {		
		return null;
	}

	/**
	 * @return Returns the bankcode.
	 */
	public String getBankcode() {
		return bankcode;
	}

	/**
	 * @param bankcode The bankcode to set.
	 */
	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}

	/**
	 * @return Returns the bankname.
	 */
	public String getBankname() {
		return bankname;
	}

	/**
	 * @param bankname The bankname to set.
	 */
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	/**
	 * @return Returns the billaccount.
	 */
	public String getBillaccount() {
		if(billaccount==null) return this.corpid;
		return billaccount;
	}

	/**
	 * @param billaccount The billaccount to set.
	 */
	public void setBillaccount(String billaccount) {
		this.billaccount = billaccount;
	}

	/**
	 * @return Returns the corpid.
	 */
	public String getCorpid() {
		return corpid;
	}

	/**
	 * @param corpid The corpid to set.
	 */
	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}

	/**
	 * @return Returns the corpname.
	 */
	public String getCorpname() {
		return corpname;
	}

	/**
	 * @param corpname The corpname to set.
	 */
	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	/**
	 * @return Returns the desturl.
	 */
	public String getDesturl() {
		return desturl;
	}

	/**
	 * @param desturl The desturl to set.
	 */
	public void setDesturl(String desturl) {
		this.desturl = desturl;
	}

	/**
	 * @return Returns the feerate.
	 */
	public String getFeerate() {
		return feerate;
	}

	/**
	 * @param feerate The feerate to set.
	 */
	public void setFeerate(String feerate) {
		this.feerate = feerate;
	}

	/**
	 * @return Returns the recurl.
	 */
	public String getRecurl() {
		return recurl+(recurl.indexOf("?")>0?"&":"?")+"idx="+this.getIdx();
	}

	/**
	 * @param recurl The recurl to set.
	 */
	public void setRecurl(String recurl) {
		this.recurl = recurl;
	}

	/**
	 * @param prefixnum The prefixnum to set.
	 */
	public void setPrefixnum(String prefixnum) {
		this.prefixnum = prefixnum;
	}

	/**
	 * @param sequenceservice The sequenceservice to set.
	 */
	public void setSequenceservice(SequenceService sequenceservice) {
		this.sequenceservice = sequenceservice;
	}

	/**
	 * @return Returns the paytype.
	 */
	public String getPaytype() {
		return paytype;
	}

	/**
	 * @param paytype The paytype to set.
	 */
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	/**
	 * @return Returns the keyPassword.
	 */
	public String getKeyPassword() {
		return keyPassword;
	}

	/**
	 * @param keyPassword The keyPassword to set.
	 */
	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}
	/**
	 * @return Returns the multiCurrency.
	 */
	public boolean isMultiCurrency() {
		return multiCurrency;
	}
	/**
	 * @param multiCurrency The multiCurrency to set.
	 */
	public void setMultiCurrency(boolean multiCurrency) {
		this.multiCurrency = multiCurrency;
	}
	
	public String getHttprecurl() {
		return httprecurl+(httprecurl.indexOf("?")>0?"&":"?")+"idx="+this.getIdx();
	}
	public void setHttprecurl(String httprecurl) {
		this.httprecurl = httprecurl;
	}
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
