package ebank.core.common.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ocx.GetRandom;

@SuppressWarnings("serial")
public class PassGuardCtl extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		 String  mcrypt_key=GetRandom.generateString(32);
		 //String  mcrypt_key="qcsyopmt4gitrllmm4yigai4n8dncnnt";
		 //String  mcrypt_key=new String(mcrypt_key1.getBytes("ISO-8859-1"),"UTF-8");
		 req.getSession().setAttribute("mcrypt_key",mcrypt_key);
		 resp.getWriter().print(mcrypt_key);
		 resp.getWriter().close();
	}

}
