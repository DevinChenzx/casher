package ebank.web.controller;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import beartool.Md5Encrypt;

import ebank.core.SLAService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.Clazz;
import ebank.core.common.util.CryptUtil;
import ebank.core.common.util.Udate;
import ebank.core.model.domain.GwOrders;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.util.PartnerInterface;
import ebank.web.common.util.RequestUtil;
import ebank.web.common.util.Validator;
import ebank.web.common.util.XSerialize;

/**
 * @author Kitian-XIE
 * 自身充值,没有订单
 */
public class BuyerCharge implements Controller{
	private SLAService slaService;
	private static Key key=XSerialize.getKey(null);
	private Log log=LogFactory.getLog(this.getClass());
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse arg1) throws Exception {
		try {		
			log.info("charge trx from:"+request.getRemoteAddr());
			HashMap<String,Object> mp=new HashMap<String,Object>();   
			//verify sign
			GwOrders order=new GwOrders();
			String service=request.getParameter("service");			
			if(!"create_charge".equals(service)){
				throw new ServiceException(EventCode.SLA_SERVICENOTFOUND);
			}
			String amount=request.getParameter("amount");
			if(Validator.isNull(amount)){
				throw new ServiceException(EventCode.WEB_PARAMNULL,new String[]{"amount"});
			}
			Clazz.Annotation(GwOrders.class, "amount", amount);
			order.setAmount(Amount.getIntAmount(amount,2));
			
			String buyer_id=request.getParameter("buyer_id");
			Clazz.Annotation(GwOrders.class, "buyer_id", buyer_id);
			order.setBuyer_id(buyer_id);
			
			String buyer_name=request.getParameter("buyer_name");
			Clazz.Annotation(GwOrders.class, "buyer_name", buyer_name);
			order.setBuyer_name(buyer_name);
			
			String interval_expired=request.getParameter("create_date");
			Date intervaldate = Udate.getDate(interval_expired);
			Date date = new Date();
			if(Validator.isNull(intervaldate)){
				throw new ServiceException(EventCode.WEB_PARAM_LOST);	
			}
			double dintervaldate = ((double)date.getTime()-(double)intervaldate.getTime())/60000;
			log.info("dintervaldate:"+dintervaldate);
			if(dintervaldate>5){
				throw new ServiceException(EventCode.ORDER_URL_OUTTIME);	
			}
			if(!Validator.isNull(request.getParameter("cust_ip"))){
				if(!request.getParameter("cust_ip").equals(RequestUtil.getIpAddr(request))){
					throw new ServiceException("201105");
				}
			}
			String preference = request.getParameter("preference");
			if(!Validator.isNull(preference)){
				if(preference.equals("CMBC_B2B")||preference.equals("SPDB_B2B")){//民生银行B2B，浦发银行B2B，交通B2B支付时需要付款企业客户号
					String payNo=request.getParameter("pay_cus_no");
					if(Validator.isNull(payNo))throw new ServiceException(EventCode.ORDER_PAYERNOTFOUND);
					mp.put("payerNo",payNo);	
				}
				if(preference.equals("BOCM_B2B")){           //交通B2B支付时需要付款企业账号
					String payNo=request.getParameter("pay_cus_no");
				 if(Validator.isNull(payNo))throw new ServiceException(EventCode.ORDER_PAYENONOTFOUND);
					mp.put("payerNo",payNo);	
				}
				
			}
			order.setPreference(request.getParameter("preference"));
			
			String currency=request.getParameter("currency")==null?"CNY":request.getParameter("currency");
			Clazz.Annotation(GwOrders.class, "currency", currency);
			order.setCurrency(currency);
			
			order.setPaymethod("directPay");
			
			String rst=PartnerInterface.getFormOrderStr(request);
			if(Validator.isNull(request.getParameter("sign"))||!request.getParameter("sign").equals(Md5Encrypt.md5(rst+WebConstants.ISMS_MD5_KEY,request.getParameter("charset")))){
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);				
			}
			
			order=(GwOrders)RequestUtil.HtmlEscape(order);
			
			    
	        mp.put(WebConstants.MAP_KEY_ORDERID, order.getOrdernum());	       
			mp.put(WebConstants.MAP_KEY_PERSISTENCE,XSerialize.serialize(order,key));
			mp.put(WebConstants.Action,"/Ebank");	
			log.info(order.getPreference());
			String[] cha=order.getPreference().split("-");
			String pref="";
			if(cha.length>0){ //BANKCODE-BANKCODESERVICECODE ICBC-icbc1025 适配
				pref=cha[0];
			}else{
				pref=order.getPreference();
			}				
			int channelid=slaService.getRouteChannelExt("", pref, order.getAmount(), order.getBuyer_id());
			log.info("charge channel:"+channelid+" pref:"+pref+" amt:"+order.getAmount()+" buyerid:"+order.getBuyer_id());
			if(channelid==0)				
				throw new ServiceException(EventCode.SLA_NOSERVICECHANNEL);			
			mp.put(WebConstants.MAP_KEY_CHANNELTOKEN, CryptUtil.randomcrypt(new int[]{channelid}));
			log.debug(mp);
			return new ModelAndView(Constants.APP_VERSION+"/redirect",WebConstants.PaRes,RequestUtil.HtmlEscapeMap(mp));
		} catch (Exception e) {
			if(!(e instanceof ServiceException))
				e.printStackTrace();
			return new ModelAndView(WebConstants.ERROR_PAGE,
		                            WebConstants.ERROR_MODEL,new WebError(e));
		}
	}

	public void setSlaService(SLAService slaService) {
		this.slaService = slaService;
	}
	
	
	
	

}
