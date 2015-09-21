/*
 * @Id: BOB.java 13:38:28 2006-7-17
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.Logger;


import com.bccb.ebusiness.client.MerPaymentInfo;
import com.bccb.ebusiness.client.MerPaymentRequest;
import com.bccb.ebusiness.client.XMLDocument;

import ebank.core.STEngineService;
import ebank.core.bank.BankService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.infosec.NetSignServer;

/**
 * @author xiexh
 * Description: 北京银行
 * 
 */
public class BOB extends BankExt implements BankService{
	private static Logger log = Logger.getLogger(BOB.class);
	private String merchantDN;
	private String serverDN;		
	private STEngineService engineService;

	//20位订单
	public String generateOrderID() throws ServiceException{
		String RandOrderID=sequenceservice.getCode();	//yyDDD(5)+8位序列			
		Random rd=new Random();
		String str="";			
		for(int i=0;i<5-this.prefixnum.length();i++) str+=rd.nextInt(10);
		rd=null;		
		return prefixnum+RandOrderID+str;
	}
	public String getRcvURL(HashMap reqs){	
		PayResult result=null;
		try{	
			result=this.getPayResult(reqs);
			//做成功订单更新，并且返回支付地址			
			if(result!=null&&1==result.getTrxsts()){				
				String state=Constants.STATE_STR_OK;				
				engineService.post(result);	
			}			
		}catch(Exception e){
			e.printStackTrace();
		}
		String inf=CryptUtil.encrypt(result.getBankdate()+"="+result.getTransnum());//ordernum使用日期简单代替    		
		return this.recurl+"/PayResult.do?xids="+inf;
		
	}

	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#getPayResult(java.util.HashMap)
	 */
	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		 String sign=String.valueOf(reqs.get("bankSignData")); //银行签名数据		 
		 String plain=this.verifySignData(sign,this.serverDN);//验证签名数据返回
		 PayResult bean=null;
		 if(plain!=null){
			 log.debug("from BOB xml = " + plain);
			 XMLDocument resXML = new XMLDocument(plain);
	         String retCode = resXML.getValueAt("retCode").toString().trim();
	         String errMsg = resXML.getValueAt("errMsg").toString().trim();
	         log.debug("retCode:"+retCode+" msg:"+errMsg);
	         if(retCode!=null&&retCode.length()== 4){
	             String orderNum = resXML.getValueAt("orderNum").toString().trim();
	             String orderAmt = String.valueOf(new BigDecimal(resXML.getValueAt("orderAmt").toString()).movePointLeft(2));
	             String orderStatus = resXML.getValueAt("orderStatus").toString().trim();
	             String accDate = resXML.getValueAt("accDate").toString().trim();
	            // successFlag = true;
	             //check orderStatus
	             bean=new PayResult(orderNum,this.bankcode,new BigDecimal(orderAmt),orderStatus.equals("4")?1:-1);
	             bean.setBankresult(retCode+" "+orderStatus);	            
	             bean.setBankdate(accDate);	             
	         }else{
	        	 throw new ServiceException(retCode+" "+errMsg);	        	 
	         }
         }else{
        	//签名验证失败			
			throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);	        
         }
		return bean;
	}

	/* (non-Javadoc)
	 * @see ebank.core.bank.BankService#sendOrderToBank(ebank.core.domain.BankOrder)
	 */
	public String sendOrderToBank(BankOrder order) throws ServiceException {		
		/*   生产商户信息对象   */
		MerPaymentInfo merPayInfo = new MerPaymentInfo();
		// 1.设置订单号
		merPayInfo.setOrderNum(order.getRandOrderID()); //最长20位订单号
		// 2.设置订单金额
		BigDecimal amount = new BigDecimal(order.getAmount());
		amount=amount.movePointRight(2);
		log.debug("bccb amount:"+amount.intValue());
		merPayInfo.setOrderAmt(String.valueOf(amount.intValue())); //金额整数		
		// 3.设置通知URL
		merPayInfo.setNotifyUrl(this.recurl+"/PayRec?idx="+this.idx);//该地址需要上报银行
		merPayInfo.setResponseUrl(this.recurl+"/PayRec?idx="+this.idx);
		// 4.设置商户备注
		merPayInfo.setMerRemarks("");
		// 5.设置商户接收通知方式
		merPayInfo.setNotifyType("0"); //通知类型 0 http
		// 6.设置是否在线取货标志
		merPayInfo.setGoodsType("2"); //在线取货，0,取货地址带冲正，2有取货地址，不带冲正
		// 7.设置交易类型
		merPayInfo.setActionType("0"); //0支付 1.查询
		//8 .设置对帐查询日期		
		merPayInfo.setMerCheckDate(order.getPostdate());
		//			
		MerPaymentRequest merReq = new MerPaymentRequest(merPayInfo);
		if(merPayInfo.checkRelationInfo()==0){
			String xml=BOB.getXml(this.corpid,merPayInfo,order.getPostdate()).toString(); //:)不用北京银行的API,商户号从配置文件取;
			String sign=this.signData(xml,merchantDN);				
			if(sign==null) throw new ServiceException(EventCode.SERVICE_NOTPROVIED);
			StringBuffer sf = new StringBuffer("");	
			sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
			sf.append("<input type=\"hidden\" name=\"netType\" value=\"7\">");
	        sf.append("<input type=\"hidden\" name=\"merReqData\" value=\""+sign+"\">");
	        sf.append("</form>");	        
			return sf.toString();			
		}else{
			log.info("payinfo:"+merPayInfo.checkRelationInfo()+" BCCB Error:"+merReq.getErrMsg());
			throw new ServiceException(EventCode.WEB_PARAMNULL,merReq.getErrMsg());
			
		}
	}
	public String signData(String src,String merchantdn){
		    NetSignServer netSign = new NetSignServer();
	        try{
	            netSign.NSSetPlainText(src.getBytes("GBK"));
	        }
	        catch(Exception temE){
	        	temE.printStackTrace();
	            netSign.NSSetPlainText(src.getBytes());
	        }
	        try{	        	
	            byte signedBytes[] = netSign.NSAttachedSign(merchantdn);
	            int retNum = netSign.getLastErrnum();
	            if(retNum == 0){
	                return new String(signedBytes);
	            }else{
	                log.debug("******client  util: 签名失败  ****** 错误代码为 [" + retNum + "]");
	                return null;
	            }
	        }
	        catch(Exception temE){
	            temE.printStackTrace();
	        }
	        return null;
	}
	 public String verifySignData(String src,String serverDn){	
		    NetSignServer netSign = new NetSignServer();
	        try{
	            netSign.NSAttachedVerify(src.getBytes("GBK"));
	        }
	        catch(Exception temE)
	        {
	            netSign.NSAttachedVerify(src.getBytes());
	        }
	        int retNum = netSign.getLastErrnum();
	        if(retNum == 0){
	            String certDN = netSign.NSGetSignerCertInfo(1).trim();
	            if(!certDN.equals(serverDn))
	            {
	                log.warn("******client  util: 非法支付平台证书  ****** ");
	                return null;
	            } else
	            {
	                return new String(netSign.NSGetPlainText());
	            }
	        } else
	        {
	            log.warn("******client  util: 验证签名失败  ****** 错误代码为 [" + retNum + "]");
	            return null;
	        }
	 }
	 public static String getXml(String merchantid,
			 String orderNum,
			 String orderAmt,
			 String goodsType,
			 String notifyType,
			 String responseUrl,
			 String actionType,
			 String MerCheckDate,
			 String notifyUrl,
			 String merRemarks){
		 return  "<?xml version=\"1.0\" encoding=\"GB2312\" ?><BCCBMcInfoData><ReqParam><merID>" 
		 +merchantid + "</merID><orderNum>" + orderNum + "</orderNum><orderAmt>" + orderAmt + "</orderAmt><goodsType>" 
		 + goodsType + "</goodsType><notifyType>" + notifyType + "</notifyType><responseUrl>" 
		 + responseUrl + "</responseUrl><actionType>" 
		 + actionType + "</actionType><merCheckDate>" + MerCheckDate + "</merCheckDate><notifyUrl>" + notifyUrl + "</notifyUrl><merRemarks>" 
		 + merRemarks + "</merRemarks></ReqParam></BCCBMcInfoData>";
	 }
	 
	 public static String getXml(String merchantid,MerPaymentInfo payment,String checkDate){
		 return getXml(merchantid,
				 payment.getOrderNum(),
				 payment.getOrderAmt(),
				 payment.getGoodsType(),
				 payment.getNotifyType(),
				 payment.getResponseUrl(),
				 payment.getActionType(),
				 checkDate,
				 payment.getNotifyUrl(),
				 payment.getMerRemarks());
		 
	 }
	
	/**
	 * @param merchantDN The merchantDN to set.
	 */
	public void setMerchantDN(String merchantDN) {
		this.merchantDN = merchantDN;
	}

	/**
	 * @param serverDN The serverDN to set.
	 */
	public void setServerDN(String serverDN) {
		this.serverDN = serverDN;
	}
	/**
	 * @param engineService The engineService to set.
	 */
	public void setEngineService(STEngineService engineService) {
		this.engineService = engineService;
	}	
	
}
