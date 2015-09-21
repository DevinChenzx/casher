/*
 * @Id: PayResultController.java 14:57:49 2006-4-19
 * 
 * @author xiexh@chinabank.com.cn
 * @version 1.0
 * payment_web_v2.0 PROJECT
 */
package ebank.web.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ebank.core.OrderService;
import ebank.core.PayResultService;
import ebank.core.bank.BankService;
import ebank.core.bank.BankServiceFactory;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.common.util.HandleException;
import ebank.core.domain.PayResult;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwViewUser;
import ebank.core.remote.agent.AgentFacadeService;
import ebank.core.remote.agent.AgentTransaction;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.WebEvent;
import ebank.web.common.util.LocaleUtil;
import ebank.web.common.util.RequestUtil;

public class PayResultController implements Controller{

	private Log log=LogFactory.getLog(this.getClass());
	
	private OrderService orderService;
	private PayResultService payResultService;	
	private BankServiceFactory services;
	private String resultExport;
	private String v40Export;
	private AgentFacadeService agentFacadeService;
	private boolean enableAgent;
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/**
	 * 消费者取货导向支付结果页面
	 */
	public ModelAndView handleRequest(HttpServletRequest request, 
			                          HttpServletResponse response) throws Exception {
	/*
		try{		
			String ordernum="";
			GwOrders gw10=null;
			//适配支付接口
			if(request.getParameter("netBankTraceNo")!=null){//兴业银行成功结果
				String transnum=request.getParameter("orderID");
				String merchantid=String.valueOf(request.getParameter("merchantID"));
				String orderid=String.valueOf(request.getParameter("orderID"));
				String orderamount=String.valueOf(request.getParameter("orderAmount"));
				String cur=String.valueOf(request.getParameter("orderCurrencyCode"));
				String tracenum=String.valueOf(request.getParameter("netBankTraceNo"));
				String pstatus=String.valueOf(request.getParameter("payStatus"));
				String mac=String.valueOf(request.getParameter("mac"));
				String str=merchantid+orderid+orderamount+cur+tracenum+pstatus;
				if(log.isDebugEnabled()) log.debug("rev from CIB:"+str);
				BankService bank=null;
				try{
				    bank=services.getBank("309");
				    if(bank==null){
				    	log.debug("cib bank not found");
				    	throw new ServiceException(WebEvent.SERVICE_NOTPROVIDED);
				    }
				}catch(Exception ex){
					HandleException.handle(ex);
				}
				boolean flag = DesUtil.checkMac(bank.getKeyPassword(),str,mac);
				if(!flag){
					throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);  //不是兴业银行返回
				}
				gw10=orderService.getOrder(transnum,null);
				
			}else{				
				String inf=request.getParameter("xids");	
				log.debug("inf="+inf);
				if(inf==null){
					throw new ServiceException(WebEvent.SERVICE_ARGS);  //订单信息
				}
				else{
					try {
						inf=CryptUtil.decrypt(inf);
						if(inf!=null){
							String[] result=inf.split("=");
							if(result!=null&&result.length==3){	
								if(log.isDebugEnabled()) 
									log.debug("ordernum="+result[0]+" amount="+result[1]+" merchantid="+result[2]);								
								//去银行
								gw10=orderService.findOrder(result[0],result[1],result[2]); //正常商户
								//
							}else if(result!=null&&result.length==2){
								//从银行返回
								log.debug("from bank dispatch: ordernum["+result[0]+"] transnum["+result[1]+"]");
								gw10=orderService.getOrder(result[1],null);
							}else{
								throw new ServiceException(WebEvent.SERVICE_ARGS);
							}
							ordernum=result[0];
						}else{
							throw new ServiceException(WebEvent.SERVICE_ARGS);							
						}
					} catch (Exception e){
						HandleException.handle(e);
					}
				}			    
			}		
			PayResult result=null;
			if(gw10!=null){
				GwViewUser gw00=gw10.getMerchant();				
				//070118启用查询代理服务
				if((WebConstants.STATE_STR_WAIT.equalsIgnoreCase(gw10.getOrderstate())
				   ||WebConstants.STATE_STR_HANDING.equalsIgnoreCase(gw10.getOrderstate()))
				   &&isEnableAgent()){
					AgentTransaction trans=null;
					try{
						log.info("enable agent query:"+gw10.getTransnum());
					    trans=agentFacadeService.execute(true,"0",gw10);
					}catch (Exception e) {
						e.printStackTrace();
					}
					//查询成功,回调成功,状态成功
					if(trans!=null){
						if(trans.isSuccess()
							&&trans.getExeresult()==1
							&&WebConstants.STATE_STR_OK.equalsIgnoreCase(trans.getTranscode())){
							gw10.setOrderstate(WebConstants.STATE_STR_OK);							
						}else{						
							log.debug("agent result："+trans.getExeresult());
						}
					}
				}else{
					log.info("disable the agent query:"+gw10.getTransnum());
				}
				log.debug("ORDERstate="+gw10.getOrderstate()+" length="+gw10.getOrderstate().length());
				String errorid="";
				if(WebConstants.STATE_STR_DEAD.equals(gw10.getOrderstate())){					
					throw new ServiceException(WebEvent.ORDER_UNPAY); //未支付
				}else if(WebConstants.STATE_STR_HANDING.equals(gw10.getOrderstate().trim())
						  ||WebConstants.STATE_STR_WAIT.equals(gw10.getOrderstate().trim())){
					long i=Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))-Long.parseLong(gw10.getCreateday());
					Map mp=new HashMap();
					mp.put("datediff", new Long(i));
					mp.put("gw10", gw10);
					mp.put("xids", request.getParameter("xids"));
					return new ModelAndView("resultexport/defer","mp",mp);					
				}else if(WebConstants.STATE_STR_FAIL.equals(gw10.getOrderstate().trim())){					
					if(gw10.getSyndesc()!=null&&gw10.getSyndesc().length()>=5){//细化错误信息
						String [] x=gw10.getSyndesc().split(":");
						if(x!=null&&x.length>=2&&x[0].indexOf("E")==0){	
							errorid=x[0];
							
							//Map mp=new HashMap();
							//mp.put("eventid", x[0]);
							//return new ModelAndView(WebConstants.ERROR_PAGE3,
                                                    WebConstants.ERROR_MODEL,mp);
                            
						}else
							throw new ServiceException(gw10.getSyndesc());
					}
					//throw new ServiceException(WebEvent.ORDER_PAYFAILURE); //支付失败
				}else if(!WebConstants.STATE_STR_OK.equals(gw10.getOrderstate())){
					throw new ServiceException(WebEvent.ORDER_ILLSTATE); //状态异常
				}
				result=new PayResult(gw10.getTransnum(),gw10.getCreatedate());
				result.setResultsucc(WebConstants.STATE_STR_OK.equals(gw10.getOrderstate())?true:false); //支付成功				
				result.setBankamount(gw10.getOrderprice());
				result.setCurrency(gw10.getCurrency());	
				
				result.setOrder(gw10);
				result.setMerchant(gw00);
                //取加密数据
	            //设置缺省加密方式	
				result=payResultService.getPostResult(result,true);
//				控制导向
				String exporter=this.getResultExport();
				if("4.0".equals(gw10.getApiversion())){
					exporter=this.v40Export;
				}
				if(gw00.getReturnexport()!=null&&!"".equals(gw00.getReturnexport().trim())){
					exporter=gw00.getReturnexport();
				}
//				指定模型导向action
				Map mp=new HashMap();
				mp.put("result",result);				
				mp.put("action",RequestUtil.getAction(result));
				//i18n
				String loc=LocaleUtil.getLocalByIndex(gw10.getViewtype());
				mp.put("locale",loc);
				//errorid
				mp.put("v_errid", errorid);	
				Map form=new HashMap();
				if(errorid.startsWith("E")){
					form.put("v_errid", errorid);
					String v_msg="";
					try {
						v_msg=String.valueOf(ResourceBundle.getBundle("conf/message", new Locale("zh_"+loc)).getObject(errorid));
					} catch (Exception e) {
						v_msg="undefined error messages";
					}
					
					form.put("v_errmsg", v_msg);
				}				
				mp.put("forms", OrderAdaptor.getPageForm(result, form));				
				return new ModelAndView(exporter,"res",mp);				
			}else{
				throw new ServiceException(WebEvent.ORDER_NOTFOUNDBYSEQ,ordernum);				
			}
			
		}catch(Exception e){			
			if(!(e instanceof ServiceException))
				e.printStackTrace();
			else
				if(log.isDebugEnabled()) log.debug("error code:..."+((ServiceException)e).getEventID());
		   
			return new ModelAndView(WebConstants.ERROR_PAGE,
                                    WebConstants.ERROR_MODEL,new WebError(e));	
			
		}
		*/
		return null;
	}
	

	/**
	 * @return Returns the resultExport.
	 */
	public String getResultExport() {
		return resultExport;
	}
	
	/**
	 * @param orderService The orderService to set.
	 */
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	/**
	 * @param resultExport The resultExport to set.
	 */
	public void setResultExport(String resultExport) {
		this.resultExport = resultExport;
	}

	/**
	 * @param payResultService The payResultService to set.
	 */
	public void setPayResultService(PayResultService payResultService) {
		this.payResultService = payResultService;
	}

	/**
	 * @param export The v40Export to set.
	 */
	public void setV40Export(String export) {
		v40Export = export;
	}

	/**
	 * @return Returns the enableAgent.
	 */
	public boolean isEnableAgent() {
		return enableAgent;
	}

	/**
	 * @param enableAgent The enableAgent to set.
	 */
	public void setEnableAgent(boolean enableAgent) {
		this.enableAgent = enableAgent;
	}

	/**
	 * @param agentFacadeService The agentFacadeService to set.
	 */
	public void setAgentFacadeService(AgentFacadeService agentFacadeService) {
		this.agentFacadeService = agentFacadeService;
	}
	
	
	
	
	
	
	

}
