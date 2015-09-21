package ebank.web.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ebank.core.OrderService;
import ebank.core.common.util.CryptUtil;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwTrxs;

public class QueryResult implements Controller {

	private OrderService orderService;
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		String gwid = CryptUtil.decrypt(req.getParameter("_id"));
		//GwOrders order=orderService.findOrderByPk(gwid);
		GwTrxs trxs = orderService.findFirstTrxByPId(gwid);
		if(trxs==null){
			resp.getWriter().print("failure");
			return null;
		}else{
			resp.getWriter().print("success");
			return null;
		}
	}

}
