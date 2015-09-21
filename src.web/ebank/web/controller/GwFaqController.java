package ebank.web.controller;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ebank.core.FaqService;
import ebank.core.common.Constants;
import ebank.core.model.domain.GwFaq;
import ebank.web.common.util.RequestUtil;

public class GwFaqController implements Controller {

	private FaqService faqService;
	public void setFaqService(FaqService faqService) {
		this.faqService = faqService;
	}
	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		
		String forward = "/faqOK";
		GwFaq gw200 = new GwFaq();
		gw200.setTrxno(RequestUtil.HtmlEscape(req.getParameter("_id"))==null?"":RequestUtil.HtmlEscape(req.getParameter("_id")));
		gw200.setIspay(RequestUtil.HtmlEscape(req.getParameter("radio"))==null?"":RequestUtil.HtmlEscape(req.getParameter("radio")));
		gw200.setContact(RequestUtil.HtmlEscape(req.getParameter("contact"))==null?"":RequestUtil.HtmlEscape(req.getParameter("contact")));
		gw200.setQuedesc(RequestUtil.HtmlEscape(req.getParameter("queDesc"))==null?"":RequestUtil.HtmlEscape(req.getParameter("queDesc")));
		HashMap<String, Object> map = new HashMap<String, Object>();
		try{
			GwFaq gwFaq = faqService.findGwFaqByTrxno(gw200.getTrxno());
			if(gwFaq==null) {
				faqService.saveGwFaq(gw200);
				map.put("msginfo", "您的问题我们已收到，我们会及时回复给您，感谢您对吉高的支持！");
			}else{
				faqService.updateGwFaq(gw200);
				map.put("msginfo", "您的问题我们已收到，我们会及时回复给您，感谢您对吉高的支持！");
			}
		}catch(Exception e){
			map.put("msginfo", "问题提交失败，感谢您对吉卡的支持！");
			e.printStackTrace();
		}

		return new ModelAndView(Constants.APP_VERSION+forward,"m",map);
	}

}
