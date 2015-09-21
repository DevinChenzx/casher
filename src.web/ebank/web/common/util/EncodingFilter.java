/*
 * @Id: EncodingFilter.java ����10:50:41 2007-9-24
 * 
 * @author 
 * @version 1.0
 * web_payment PROJECT
 */
package ebank.web.common.util;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class EncodingFilter extends OncePerRequestFilter {
	
	
	private Log log=LogFactory.getLog(this.getClass());

	private String encoding;

	private boolean forceEncoding;

	/**
	 * Set the encoding to use for requests. This encoding will be
	 * passed into a ServletRequest.setCharacterEncoding call.
	 * <p>Whether this encoding will override existing request
	 * encodings depends on the "forceEncoding" flag.
	 * @see #setForceEncoding
	 * @see javax.servlet.ServletRequest#setCharacterEncoding
	 */
	public void setEncoding(String encoding) { 
		this.encoding = encoding;
	}

	/**
	 * Set whether the encoding of this filter should override existing
	 * request encodings. Default is false, i.e. do not modify encoding
	 * if ServletRequest.getCharacterEncoding returns a non-null value.
	 * @see #setEncoding
	 * @see javax.servlet.ServletRequest#getCharacterEncoding
	 */
	public void setForceEncoding(boolean forceEncoding) {
		this.forceEncoding = forceEncoding;
	}

	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {	
		if (this.forceEncoding) {			
			request.setCharacterEncoding(this.encoding);
		}	
		else if(request.getQueryString()!=null){
			log.debug("From requestQueryString:"+request.getQueryString());
			boolean find=false;
			String s=request.getQueryString();
			int i=s.toLowerCase().indexOf("charset=");
			if(i>=0) find=true;		
			if(find){	
				int j=request.getQueryString().indexOf("&", i);								
				if(j>=i)request.setCharacterEncoding(request.getQueryString().substring(i+8,j));
				else
				request.setCharacterEncoding(request.getQueryString().substring(i+8));				
				
			}else{
				if(request.getRequestURI().toUpperCase().indexOf("GBK")>0){
					request.setCharacterEncoding("GBK");
				}else if(request.getRequestURI().toUpperCase().indexOf("UTF-8")>0){
					request.setCharacterEncoding("UTF-8");
				}else
				request.setCharacterEncoding(this.encoding);
			}
		}else{
			request.setCharacterEncoding(this.encoding);			
		}
		filterChain.doFilter(request, response);
	 }	
}
