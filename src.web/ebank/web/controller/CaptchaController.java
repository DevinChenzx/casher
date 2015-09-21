package ebank.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class CaptchaController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse response) throws Exception {
		int WIDTH= 160;
		int HEIGHT=50;
		Captcha captcha=new Captcha.Builder(WIDTH, HEIGHT).addText()
        .addNoise()
        .build();
		
		req.getSession().setAttribute("captcha",captcha);
        CaptchaServletUtil.writeImage(response, captcha.getImage());
		
		return null;
	}
	

}
