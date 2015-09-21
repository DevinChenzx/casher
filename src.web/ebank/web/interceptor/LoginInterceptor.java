package ebank.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ebank.web.common.WebConstants;

public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	private Logger log = Logger.getLogger(this.getClass());
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(request.getSession().getAttribute(WebConstants.SESSION_USER_LOGIN_KEY)!=null)
			return true;
		else{
			log.info("session invalidate,force to login..");
			//request.getRequestDispatcher("/forward?_to_=/common/httpsAdaptor").forward(request,response);
			return false;
		}
	}

}
