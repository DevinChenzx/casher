package ebank.web.common.util;


import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import ebank.core.UserService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.domain.MerchantOrder;
import ebank.core.model.domain.GwModel;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwSuborders;
import ebank.core.model.domain.GwViewUser;


public class PartnerFactory 
	implements BeanFactoryAware{	
		
		private static Log log=LogFactory.getLog(PartnerFactory.class);
		private BeanFactory factory;
		/* (non-Javadoc)
		 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
		 */
		public void setBeanFactory(BeanFactory arg0) throws BeansException {
			this.factory=arg0;		
		}
		
		/**
		 * @return Returns the factory.
		 */
		public BeanFactory getFactory() {
			return factory;
		}

	public IPartnerInterface getThirdPartInstance(HttpServletRequest req) throws ServiceException{
		String format = req.getParameter(Constants.FORMAT_FILED);		
		if(!Validator.isNull(format)){
			log.info("Api from......"+format);
			if("BHE".equalsIgnoreCase(format)||Constants.API_WONDERPAY.equalsIgnoreCase(format)){
				return (IPartnerInterface)factory.getBean("wonderpay");
			}
			if(Constants.API_REAPAL.equalsIgnoreCase(format)){
				return (IPartnerInterface)factory.getBean("reapal");
			}
			if(Constants.API_99BILL.equalsIgnoreCase(format)){
				return (Bill99Interface)factory.getBean("bill");
			}
			else if(Constants.API_YEEPAY.equalsIgnoreCase(format)){
				return (YeePayInterface)factory.getBean("yeepay");
			}
			else if("ALIPAY".equalsIgnoreCase(format)||Constants.API_APLIPAY_OLD.equalsIgnoreCase(format)){
				return (AlipayInterface)factory.getBean("alipay");
			}
			else if("CHINAPNR".equalsIgnoreCase(format)){
				return (ChinaPnrInterface)factory.getBean("chinapnr");
			}
		    else if("IPS".equalsIgnoreCase(format)){
					return (IPSInterface)factory.getBean("ips");
			}else
				throw new ServiceException(EventCode.SLA_SERVICENOTFOUND);
		}else{
			//兼容之前版本
			String service = req.getParameter("service");
			String precard = req.getParameter("precard");
			if(!Validator.isNull(service)){
				return (IPartnerInterface)factory.getBean("wonderpay");
			}
			if(!Validator.isNull(precard)){
				return (PreCardInterface)factory.getBean("PreCard");
			}
			throw new ServiceException(EventCode.WEB_PARAM_LOST,new String[]{"service"});
		}
		
	}

	public MerchantOrder getInstance(HttpServletRequest req) throws ServiceException{
		String service = req.getParameter("service");
		String precard = req.getParameter("precard");
		String p2_Order=req.getParameter("p2_Order");//易宝商家订单号
		
		 
		IPartnerInterface interfaces=null;
		if(!Validator.isNull(service)){
			interfaces=(IPartnerInterface)factory.getBean("wonderpay");
		}				
		if(!Validator.isNull(p2_Order)){
			interfaces=(YeePayInterface)factory.getBean("YeePay");
		}
		
		if(!Validator.isNull(precard)){
			interfaces=(PreCardInterface)factory.getBean("PreCard");
		}
		if(interfaces==null){
			throw new ServiceException(EventCode.SLA_SERVICENOTFOUND);
		}
		MerchantOrder order=interfaces.getMerchantOrderByService(req);		
				
		return order;
	}
	
	/**
	 * 验证组合条件
	 * @param order
	 * @return
	 * @throws ServiceException
	 */
	public static boolean validateOrder(MerchantOrder order) throws ServiceException{				
		if(Validator.isNull(order.getOrders().getSeller_id())&&Validator.isNull(order.getOrders().getSeller_name())){
			throw new ServiceException(EventCode.SELLER_INF_LOST);
		}
		if((!Validator.isNull(order.getOrders().getSeller_id())&&order.getOrders().getSeller_id()==order.getOrders().getBuyer_id())||
				(order.getOrders().getSeller_name()!=null&&order.getOrders().getSeller_name().equals(order.getOrders().getBuyer_name()))){
			throw new ServiceException(EventCode.SELLER_SAME_BUYER);
		}	
		if(order.getOrders().getAmount()<=0||order.getOrders().getPrice()<=0){
			throw new ServiceException(EventCode.ORDER_VALIDATE_AMOUNT);
		}		
		if(Validator.isNull(order.getOrders().getSeller_id())&&Validator.isNull(order.getOrders().getSeller_name())){
			throw new ServiceException(EventCode.WEB_PARAMNULL);
		}
		if(!Validator.isNull(order.getOrders().getSeller_id())&&!Validator.isNull(order.getOrders().getSeller_name())){
			throw new ServiceException(EventCode.WEB_PARAM_CONFLICT,new String[]{"seller_id","seller_name"});
		}			
		return true;	
	
	}
	public static GwViewUser ValidateRoyaltyParam(UserService userService,String royaltyparam,long amount,String parternerid) throws ServiceException{
		GwViewUser user=userService.getViewUser(parternerid,"royalty");
		if(user==null||user.getMstate()!=1){
			throw new ServiceException(EventCode.MERCHANT_STATEILL);
		}		
		if(!Validator.isNull(royaltyparam)){
			String feepayer_custno="";
			long feepayer_amount=0;
			if(Validator.isNull(user.getService_params())){
				throw new ServiceException("530015"); //fee parameter not configurate.
			}else{
				JSONObject jo=JSONObject.fromObject(user.getService_params());
				if(jo!=null){
					feepayer_custno=jo.getString("payfee_customer_no");
				}else{;
					throw new ServiceException("530015");
				}
				GwViewUser user_temp=userService.getViewUser(parternerid,"online");
				if(user_temp==null){
					throw new ServiceException("530007");
				}
				if(Validator.isNull(user_temp.getFee_params())){
					throw new ServiceException("530016");
				}else{
					//compute fee
					try {						
						feepayer_amount=Amount.getIntAmount(String.valueOf(amount*Double.parseDouble(user_temp.getFee_params())),-2);
					} catch (Exception e) {
						throw new ServiceException("530016");
					}
				}					
			}
			if(feepayer_custno==""){
				throw new ServiceException("530015");
			}
			String[] reg=royaltyparam.split("\\|");
			boolean isexist=false;
			if(reg!=null&&reg.length>=1){
				HashSet<String> payees=new HashSet<String>();
				for (int i = 0; i < reg.length; i++) {
					String[] items=reg[i].split("\\^");
					if(items!=null&&items.length>=2){
						for (int j = 0; j < items.length; j++) {
							if(j<=1&&items[j].matches(Constants.REG_EMAIL)){
								GwViewUser itemusers=userService.getUserWithIdAndName("",items[j]);
								if(itemusers==null) throw new ServiceException(EventCode.INVALIDATE_USER,new String[]{items[j]});
								if(!items[j+1].matches(Constants.REG_EMAIL)){							
									if(payees.contains(itemusers.getCustomer_no())){
										throw new ServiceException("530019",new String[]{items[j]});
									}
									payees.add(itemusers.getCustomer_no());
								}
								if(itemusers.getCustomer_no().equalsIgnoreCase(feepayer_custno)){
									if(!items[j+1].matches(Constants.REG_EMAIL)){
										isexist=true;
									}
									if(items[j+1].matches(Constants.REG_MONEY)&&feepayer_amount>Amount.getIntAmount(items[j+1], 2)){
										throw new ServiceException("530017");
									}
								}else{
									if(items[j+1].matches(Constants.REG_MONEY)&&Amount.getIntAmount(items[j+1], 2)<=0){//add not payee amount equal 0 exception
										throw new ServiceException("501114");
									}
								}
								
								if(!String.valueOf(parternerid).equals(itemusers.getCustomer_no())
								   &&!userService.checkBind(Long.parseLong(parternerid),items[j])){
									throw new ServiceException("530014",new String[]{items[j]});
								}
							}
						}								
					}else{
						throw new ServiceException(EventCode.WEB_PARAMFORMAT,new String[]{"royalty_parameter"});
					}
				}
			}else{
				throw new ServiceException(EventCode.WEB_PARAMFORMAT,new String[]{"royalty_parameter"});
			}
			if(!isexist){
				throw new ServiceException("530018");
			}
	    }
		return user;
	}
	
	public static GwViewUser ValidateRoyaltyParamForSub(UserService userService,String royaltyparam,long amount,String parternerid,MerchantOrder morder) throws ServiceException{
		GwOrders gworder = morder.getOrders();
        GwViewUser user=userService.getViewUser(parternerid,"online");
		if(user==null||user.getMstate()!=1){
			throw new ServiceException(EventCode.SERVICE_NOTPROVIED,new String[]{gworder.getSeller_name()});
		}
		
		//判断支付账户是否开通了合单支付功能
		
		GwModel gwModel=userService.findModelByCustomID(parternerid);
	    if(gwModel==null) throw new ServiceException(EventCode.SERVICE_NOTPROVIED,new String[]{gworder.getSeller_name()});//平台方
	    if(!"merge".equals(gwModel.getPay_model())){
	    	throw new ServiceException(EventCode.SERVICE_NOTPROVIED,new String[]{gworder.getSeller_name()});//没有开通合单支付服务
	    }		
		//判断该收款账户是否存在
		List<GwSuborders> suborderslist = morder.getSubOrdersList();
		if(suborderslist!=null&&suborderslist.size()>0)
		for (Iterator<GwSuborders> iterator1 = suborderslist.iterator(); iterator1.hasNext();) {
			GwSuborders subObj = (GwSuborders) iterator1.next();
			List<GwViewUser> li = userService.findUserBylogin_recepit(subObj.getSeller_code());
			if(li==null||li.size()<1) throw new ServiceException("530023",new String[]{subObj.getSeller_code()});//不存在该收款账户
			
			//判断商户是否开通了在线支付功能
			boolean flag = true;
			for (Iterator<GwViewUser> iterator2 = li.iterator(); iterator2.hasNext();) {
				GwViewUser viewObj = (GwViewUser) iterator2.next();	
				if("online".equals(viewObj.getService_code())){
					flag = false;
					break;
				}
			}
		  if(flag)throw new ServiceException(EventCode.SERVICE_NOTPROVIED,new String[]{subObj.getSeller_code()});//没有开通在线支付功能
		}
		return user;
	}
}
