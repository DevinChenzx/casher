package ebank.web.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ebank.core.common.EventCode;
import ebank.core.common.util.Udate;
import ebank.web.common.util.RequestUtil;
import ebank.web.common.util.Validator;

public class AntiFishInterceptor extends HandlerInterceptorAdapter {
	
	private Log log=LogFactory.getLog(this.getClass());
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		log.info("request is:"+RequestUtil.getFormInput(request));
		String exter_invoke_ip=request.getParameter("cust_ip");
		String interval_expired=request.getParameter("create_date");
		if(!Validator.isNull(exter_invoke_ip)&&!RequestUtil.getIpAddr(request).equals(exter_invoke_ip)){
			request.getRequestDispatcher("/PayRec?ERR=201105").forward(request, response);
			return false;
		}
		if(!Validator.isNull(interval_expired)){
			Date intervaldate = Udate.getDate(interval_expired);
			Date date = new Date();
			if(Validator.isNull(intervaldate)){
				request.getRequestDispatcher("/PayRec?ERR="+EventCode.WEB_PARAM_LOST).forward(request, response);
				return false;
			}
			double dintervaldate = ((double)date.getTime()-(double)intervaldate.getTime())/60000;			
			if(dintervaldate>5){
				request.getRequestDispatcher("/PayRec?ERR="+EventCode.ORDER_URL_OUTTIME).forward(request, response);
				return false;
			}
		}
		return true;
	}
	

}
