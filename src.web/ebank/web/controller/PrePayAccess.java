package ebank.web.controller;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import beartool.Md5Encrypt;

import ebank.core.OrderService;
import ebank.core.SLAService;
import ebank.core.SequenceService;
import ebank.core.TradePrecardService;
import ebank.core.UserService;
import ebank.core.bank.BankService;
import ebank.core.bank.BankServiceFactory;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.Clazz;
import ebank.core.common.util.CryptUtil;
import ebank.core.common.util.MD5sign;
import ebank.core.domain.BankOrder;
import ebank.core.domain.MerchantOrder;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwViewUser;
import ebank.core.model.domain.TradePrecard;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.util.LocaleUtil;
import ebank.web.common.util.PartnerFactory;
import ebank.web.common.util.PartnerInterface;
import ebank.web.common.util.RequestUtil;
import ebank.web.common.util.Validator;
import ebank.web.common.util.XSerialize;


public class PrePayAccess implements Controller {

	private Log log=LogFactory.getLog(this.getClass());
	private PartnerFactory factory;
	private TradePrecardService tradePrecardService;
	private BankServiceFactory services;
	private UserService userService;
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setServices(BankServiceFactory services) {
		this.services = services;
	}

	public TradePrecard getTradePrecard(HttpServletRequest reqs) throws ServiceException{
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		  String TransDate= new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String ProductName	=(String) reqs.getParameter("subject");
			Clazz.Annotation(TradePrecard.class, "productname", ProductName);
			long amount=0;
			String ProductNum="";
			if("".equals(reqs.getParameter("quantity"))||reqs.getParameter("quantity")==null){
				ProductNum="1";
			}else{
			 ProductNum	=(String) reqs.getParameter("quantity") ;
			Clazz.Annotation(TradePrecard.class, "productnum", ProductNum);
			}
			if(!"".equals(reqs.getParameter("total_fee"))&&reqs.getParameter("total_fee")!=null){
			 amount=Amount.getIntAmount(reqs.getParameter("total_fee"), 2);	
			// Double.parseDouble(Amount.getFormatAmount(String.valueOf(tradePrecardTemp.getAmount()),-2))
			}else if(!"".equals(reqs.getParameter("price"))&&reqs.getParameter("price")!=null) {
				
				String Money		=(String) reqs.getParameter("price");
				Clazz.Annotation(TradePrecard.class, "amount", Money);
				amount=Amount.getIntAmount(Money, 2)*Long.valueOf(ProductNum);
			}			
			String CurCode		="CNY" ;
			String MerRcvURL	=(String) reqs.getParameter("return_url") ;
			Clazz.Annotation(TradePrecard.class, "merrcvurl", MerRcvURL);	
			String NotifyUrl	=(String) reqs.getParameter("notify_url");
			Clazz.Annotation(TradePrecard.class, "notifyurl", NotifyUrl);			
			String OrderTime	= TransDate;
				String Ext1			=(String)reqs.getParameter("body");
				Clazz.Annotation(TradePrecard.class, "Ext1", Ext1);				
				String Ext2			=(String)reqs.getParameter("Ext2");	
			String Sellerid		=(String)reqs.getParameter("partner");
			String charsets		=(String)reqs.getParameter("charset");
			String sign_type	=(String)reqs.getParameter("sign_type");
			String signMsg		=(String)reqs.getParameter("sign");
			String outTradeNo   =(String)reqs.getParameter("out_trade_no");
			Clazz.Annotation(TradePrecard.class, "outTradeNo", outTradeNo);
			String paymentType	=(String)reqs.getParameter("payment_type");
			TradePrecard result=null;
		try
		{	
			result=new TradePrecard();
			result.setAmount(amount);//以分为单位，存入到数据库
			result.setCurcode(CurCode);
			result.setExt1(Ext1);
			result.setExt2(Ext2);
			result.setSellerid(Sellerid);
			result.setMerrcvurl(MerRcvURL);
			result.setNotifyurl(NotifyUrl);
			result.setOrdertime(sdf.parse(OrderTime));
			result.setProductname(ProductName);
			
			result.setProductnum(Integer.valueOf(ProductNum));
			result.setCharsets(charsets);
			result.setSignMsg(signMsg);
			
			result.setSigntype(sign_type);
			result.setCharsets(charsets);
			result.setOuttradeno(outTradeNo);
			result.setPaymentType(paymentType);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
				return null;
		}
		return result;
	}
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {	
	try {
			
			TradePrecard tradePrecard=this.getTradePrecard(request);
			/*if(tradePrecard==null){
				throw new ServiceException(EventCode.WEB_PARAMNULL);
			}	
			if(tradePrecard!=null){
				validateOrder(tradePrecard);
			}
			GwViewUser user=userService.getViewUser(String.valueOf(tradePrecard.getSellerid()),"online");
			if(user==null||user.getMstate()!=1){
				throw new ServiceException(EventCode.MERCHANT_STATEILL);
			}
			if(!"normal".equals(user.getStatus())){
				throw new ServiceException(EventCode.MERCHANT_STATEILL);
			}
			//验证签名
			log.debug("plain:"+PartnerInterface.getFormOrderStr(request));			
			if(Validator.isNull(tradePrecard.getSignMsg())||!tradePrecard.getSignMsg().equals(Md5Encrypt.md5(PartnerInterface.getFormOrderStr(request)+user.getMd5Key(),tradePrecard.getCharsets()))){
				
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);				
			}*/
			TradePrecard tradePrcard=tradePrecardService.saveTradePreCard(tradePrecard);
			
			BankService bankService=services.getBank("0800");
			BankOrder bkorder=new BankOrder();
			bkorder.setMerchantid(request.getParameter("partner"));
			/*if(request.getParameter("total_fee")!=null){
				bkorder.setAmount(request.getParameter("total_fee"));
			}else{
				bkorder.setAmount(request.getParameter("price"));
			}*/
			
			//bkorder.setAmount(String.valueOf(tradePrecard.getAmount()));
			bkorder.setAmount(Amount.getFormatAmount(String.valueOf(tradePrecard.getAmount()),-2));//传给前台转化成元
			HashMap orderMp = new HashMap();
			orderMp.put("subject", request.getParameter("subject"));
			orderMp.put("bodys", request.getParameter("body"));
			orderMp.put("returnUrl", tradePrcard.getMerrcvurl());
			orderMp.put("notifyUrl",tradePrcard.getNotifyurl());
			orderMp.put("sign_type", tradePrecard.getSigntype());
			orderMp.put("ProductNum", tradePrecard.getProductnum());
			orderMp.put("outTradeNo", tradePrecard.getOuttradeno());
			bkorder.setMp(orderMp);
			
			bkorder.setRandOrderID(tradePrcard.getId());
			
			String html=bankService.sendOrderToBank(bkorder);
			Map<String,Object> mp=new HashMap<String,Object>();
			String s="<body onload=\"javascript:document.sendOrder.submit();\">";
			mp.put("bank",bankService.getBankname());
			mp.put("form" ,s+html);		
			log.debug(mp);
			return new ModelAndView("/common/forbank","page",mp);
	
		} catch (Exception e) {
			log.error(e.getMessage());
			if(!(e instanceof ServiceException))
				e.printStackTrace();
			return new ModelAndView(WebConstants.ERROR_PAGE,
		                            WebConstants.ERROR_MODEL,new WebError(e));
		}
		
	}
	
	public void setFactory(PartnerFactory factory) {
		this.factory = factory;
	}
	public TradePrecardService getTradePrecardService() {
		return tradePrecardService;
	}
	public void setTradePrecardService(TradePrecardService tradePrecardService) {
		this.tradePrecardService = tradePrecardService;
	}
	/**
	 * 组合校验
	 * @param order
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unused")
	private boolean validateOrder(TradePrecard tradePrecard) throws ServiceException{				
		if(Validator.isNull(tradePrecard.getSellerid())){
			throw new ServiceException(EventCode.SELLER_INF_LOST);
		}
		
		if(tradePrecard.getAmount()<=0){
			throw new ServiceException(EventCode.ORDER_VALIDATE_AMOUNT);
		}		
					
		return true;	
	
}
	
	
	

}
