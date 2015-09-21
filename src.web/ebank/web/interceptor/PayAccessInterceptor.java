package ebank.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ebank.core.common.Constants;
import ebank.web.common.util.RequestUtil;


public class PayAccessInterceptor extends HandlerInterceptorAdapter{
	private Log log=LogFactory.getLog(this.getClass());
	private String[] keys=new String[]{"baidu.com"};
	@Override
	public boolean preHandle(HttpServletRequest req,
			HttpServletResponse resp, Object handler) throws Exception {
		String preCard = req.getParameter("version");		
		String format = req.getParameter(Constants.FORMAT_FILED);		
		if(preCard!=null&&format==null){
			req.getRequestDispatcher("/PrePayAccess").forward(req, resp);
			return false;
		}
		for (int i = 0; i < keys.length; i++) {
			if(req.getHeader("REFERER")!=null&&req.getHeader("REFERER").contains(keys[i])){
				log.info("rule "+keys[i]+" deny remote host:"+req.getHeader("host")+" service url:"+req.getHeader("REFERER")+" ip:"+RequestUtil.getIpAddr(req));
		    	return false;
			}
		}
		
		return super.preHandle(req, resp, handler);
	}

}
