package ebank.web.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import ebank.core.common.Constants;
import ebank.web.common.util.RequestUtil;


public class CMBController implements Controller {
	private Log log=LogFactory.getLog(this.getClass());
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{	
			log.info("remote host:"+request.getHeader("host")+" service url:"+request.getHeader("REFERER")+" ips:"+RequestUtil.getIpAddr(request));
			String bankName = request.getParameter("bankName").toString();
			Map<String,Object> mp=new HashMap<String,Object>();
			mp.put("bankName", bankName);
			if("CMB_B2B".equals(bankName)){
				mp.put("labelName", "¿Í»§ºÅ£º");
			}else if("BOCM_B2B".equals(bankName)){
				mp.put("labelName", "¸¶¿îÕËºÅ£º");
			}
			return new ModelAndView(Constants.APP_VERSION+"/cmbMerchant","m",mp);
	}
}
