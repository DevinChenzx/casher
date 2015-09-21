package ebank.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class HttpInterceptor extends HandlerInterceptorAdapter {

	private Logger log = Logger.getLogger(this.getClass());

	public boolean preHandle(HttpServletRequest req,
			HttpServletResponse response, Object obj) throws Exception {
		if ("http".equalsIgnoreCase(req.getScheme())) {
			log.debug("Adapter..." + req.getRequestURI());
			req.setAttribute("_uri_", req.getRequestURI());
			req.getRequestDispatcher("/forward?_to_=/common/httpsAdaptor")
					.forward(req, response);
			return false;
		}
		return true;
	}

}
