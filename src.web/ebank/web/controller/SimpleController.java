/*
 * @Id: SimpleController.java 17:02:48 2007-1-9
 * 
 * @author 
 * @version 1.0
 * web_payment PROJECT
 */
package ebank.web.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.util.RequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.util.HtmlUtils;


import ebank.core.common.ServiceException;
import ebank.web.common.WebConstants;
import ebank.web.common.WebError;
import ebank.web.common.WebEvent;
import ebank.web.common.util.RequestUtil;

/**
 * @author xiexh
 * Description: ¼òµ¥×ª·¢
 * 
 */
public class SimpleController implements Controller{

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		if(req.getParameter("_to_")==null
		  ||req.getParameter("_to_").length()>50
		  ||String.valueOf(req.getAttribute("_uri_")).length()>300) 
			return new ModelAndView(WebConstants.ERROR_PAGE,
		                            WebConstants.ERROR_MODEL,new WebError(new ServiceException(WebEvent.REQUEST_PARAM_VALIDATE)));
		Enumeration names=req.getParameterNames();
		Map mp=new HashMap();
		mp.put("_uri_", req.getAttribute("_uri_"));
		for (;names.hasMoreElements();) {
             String element = (String) names.nextElement();
             mp.put(HtmlUtils.htmlEscape(element),req.getParameter(element));                   
        }		
		return new ModelAndView(req.getParameter("_to_"),"request",RequestUtil.HtmlEscapeMap(mp));
	}
	

}
