package ebank.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ebank.core.OrderService;
import ebank.core.PayResultService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.PayResult;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwTrxs;
import ebank.core.remote.QueryResultService;
import ebank.web.common.util.LocaleUtil;
import ebank.web.common.util.RequestUtil;
import ebank.web.common.util.Validator;

public class ExportResultPage implements Controller {

	private Log log=LogFactory.getLog(this.getClass());
	private OrderService orderService;
	private QueryResultService queryResultService;
	private PayResultService payResultService;	
	
	public void setPayResultService(PayResultService payResultService) {
		this.payResultService = payResultService;
	}
	public void setQueryResultService(QueryResultService queryResultService) {
		this.queryResultService = queryResultService;
	}
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	public ModelAndView handleRequest(HttpServletRequest reqs,
			HttpServletResponse resp) throws Exception {
		
		Map<String,Object> mp=new HashMap<String,Object>();
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		String gwid = CryptUtil.decrypt(reqs.getParameter("_id")==null?"":reqs.getParameter("_id").toString());
		String paytype = reqs.getParameter("_paytype")==null?"":reqs.getParameter("_paytype").toString();
		GwOrders order=orderService.findOrderByPk(gwid);
		boolean recheck=false;
		if(order!=null){
			if("0".equals(order.getOrdersts())){ //unpay
				GwTrxs trxs = orderService.findFirstTrxByPId(gwid);
				if(trxs!=null){
					mp = queryResultService.getHttpResp(trxs.getId());
					log.info("syn ismsapp return:"+mp);					
					if(mp!=null&&!Validator.isNull(mp.get("response"))){
						JSONObject json = JSONObject.fromObject(mp.get("response").toString());
						log.info("ISMSApp return bank info :"+json);
						if("0".equals(String.valueOf(json.get("response")))){
							recheck=true;
						}						
					}										
				}
				else{
					throw new ServiceException(EventCode.ORDER_PAYNOTFOUND);
				}
			}
			if(("1".equals(order.getOrdersts())||"2".equals(order.getOrdersts())||"3".equals(order.getOrdersts())) || "5".equals(order.getOrdersts())||recheck){
				    if(recheck){
				    	order=orderService.findOrderByPk(gwid);						//导向反馈页面									    		
				    }
				    if(("1".equals(order.getOrdersts())||"2".equals(order.getOrdersts())||"3".equals(order.getOrdersts())||"5".equals(order.getOrdersts()))){
					    GwTrxs updateTrxs = orderService.findFirstTrxByPId(gwid);
						//导向付款tongzhi页面
						PayResult result = new PayResult(updateTrxs.getTrxnum());
						result.setPayer_ip(RequestUtil.getIpAddr(reqs));
						result.setOrder(order);
						result.setTrx(updateTrxs);
						map.put("result",result);
						map.put("action","#");			
						if(result.getOrder()!=null){
							//get update order
							result.setOrder(orderService.findOrderByPk(result.getOrder().getId()));			
							map.put("action",RequestUtil.getAction(result.getOrder().getReturn_url()));
							map.put("locale",LocaleUtil.getLocale(result.getOrder().getLocale(),"CN"));
							map.put("forms", payResultService.mapresult(result.getOrder(),false));
						}						
						return new ModelAndView(Constants.APP_VERSION+"/resultExport","res",RequestUtil.HtmlEscapeMap(map));
				    }
			}
			//导向反馈页面
			JSONObject json = null;
			if(mp!=null&&!Validator.isNull(mp.get("response"))){
				json = JSONObject.fromObject(mp.get("response").toString());
			}
			map.put("reason", json==null?"交易失败":String.valueOf(json.get("resmsg")));
			map.put("gwid", gwid);
			map.put("paytype", paytype);
			map.put("orderid", reqs.getParameter("_id")==null?"":reqs.getParameter("_id"));
			return new ModelAndView(Constants.APP_VERSION+"/faq","m",map);	
			
		}else{
			throw new ServiceException(EventCode.ORDER_NOTFOUND);
		}		
	}

}
