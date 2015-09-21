/*
 * @Id: RequestUtil.java 11:06:53 2006-2-21
 * 
 * @author 
 * @version 1.0
 * PAYGW_WEB_V6 PROJECT
 */
package ebank.web.common.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.HtmlUtils;

import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;


/**
 * @author xiexh
 * 
 */
public class RequestUtil {
	/***
	 */
	public static HashMap getFormInput (HttpServletRequest req) throws ServiceException
	{
		HashMap<String,Object> formInput=new HashMap<String,Object>();
		Map paramMap = req.getParameterMap();
		Set<Entry> params = paramMap.entrySet();
		for(Iterator<Entry> it = params.iterator(); it.hasNext(); )
		{
			Entry param = (Entry) it.next();
			String name = (String) param.getKey();
			String[] values = (String[])param.getValue() ;
			String value = null ;
			int length = values.length ;
			if(length > 1) {
				StringBuffer sb = new StringBuffer() ;
				for(int i=0; i<length; i++) {
					sb.append(values[i]) ;
					if (i != length -1) sb.append(",") ;
				}
				value = sb.toString() ;
				sb.delete(0, sb.length()) ;
			} else {
				value = values[0] ;
			}
			if (name.length()>0&&value.toLowerCase().indexOf("script")<0)
				formInput.put(name,value);			    
			param=null;
			name=null;
			value=null;
		}
		formInput.put("RemoteIP",RequestUtil.getIpAddr(req));
		formInput.put("queryString", req.getQueryString());
		params=null;
		paramMap=null;
		return formInput;
	}
	public static HashMap getFormParams (HttpServletRequest req) throws ServiceException
	{
		HashMap<String,Object> formInput=new HashMap<String,Object>();
		Map paramMap = req.getParameterMap();
		Set<Entry> params = paramMap.entrySet();
		for(Iterator<Entry> it = params.iterator(); it.hasNext(); )
		{
			Entry param = (Entry) it.next();
			String name = (String) param.getKey();
			String[] values = (String[])param.getValue() ;
			String value = null ;
			int length = values.length ;
			if(length > 1) {
				StringBuffer sb = new StringBuffer() ;
				for(int i=0; i<length; i++) {
					sb.append(values[i]) ;
					if (i != length -1) sb.append(",") ;
				}
				value = sb.toString() ;
				sb.delete(0, sb.length()) ;
			} else {
				value = values[0] ;
			}
			if (name.length()>0&&value.toLowerCase().indexOf("script")<0)
				formInput.put(name,value);			    
			param=null;
			name=null;
			value=null;
		}		
		params=null;
		paramMap=null;
		return formInput;
	}
	
	public static boolean verifyInputBoxReq(HttpServletRequest req) throws ServiceException{
		Map paramMap = req.getParameterMap();
		Set params = paramMap.entrySet();
		String[] xkeys = { ";","|","<",">"};
		for(Iterator it = params.iterator(); it.hasNext(); )
		{
			Entry param = (Entry) it.next();
			String name = (String) param.getKey();
			String value = ((String[]) param.getValue())[0];
			for (int i = 0; i < xkeys.length; i++) {
				if(value.toLowerCase().contains(xkeys[i])){
					int j=value.indexOf(xkeys[i]);
					int k=j+xkeys[i].length();
					if((j==0||(j>=1&&value.charAt(j-1)!=32))&&((value.length()==k)||(value.length()>k&&value.charAt(k)==32))){
						throw new ServiceException(EventCode.WEB_PARAM_FILTER,new String[]{value});
					}				
				}
			}
			
					
		}
		return false;
	}	
	
	public static boolean illegalKeys(String va){
		if(va==null) return false;		
		String[] xkeys={"/*","*/","\\u","insert","select", "delete", "update","create","drop"}; //
		String[] trimKeys={ "and", "exec", "count", "chr", "mid", "master", "or", "truncate", "char", "declare", "join"};
		for (int i = 0; i < xkeys.length; i++) {
			if(va.toLowerCase().contains(xkeys[i])){
				int j=va.indexOf(xkeys[i]);
				int k=j+xkeys[i].length();
				if((j==0||(j>=1&&va.charAt(j-1)!=32))&&((va.length()==k)||(va.length()>k&&va.charAt(k)==32))){
					return true;
				}				
			}
		}
		for (int i = 0; i < trimKeys.length; i++) {
			if(va.toLowerCase().contains(trimKeys[i])){				
				int j=va.indexOf(trimKeys[i]);
				int k=j+trimKeys[i].length();				
				if(j>=1&&va.charAt(j-1)==32&&va.length()>k&&va.charAt(k)==32){
					return true;
				}				
			}
		}				
		return false;
	}
	
	public static Object HtmlEscape(Object obj) throws ServiceException{
		
		if(obj!=null&&obj instanceof String) return HtmlUtils.htmlEscape(String.valueOf(obj));
		Method[] methods=obj.getClass().getMethods();		
		for (int i = 0; i < methods.length; i++) {
			Method method=methods[i];
			try {
				String methodname=method.getName();
				String setmethod=methodname.replaceAll("get", "set");
				if(methodname.startsWith("get")&&method.getReturnType().equals(String.class)){				
					String value=(String)method.invoke(obj,(Object[])null);					
					if(RequestUtil.illegalKeys(value)){
						throw new ServiceException(EventCode.WEB_PARAM_FILTER,new String[]{value});
					}
					obj.getClass().getMethod(setmethod, new Class[]{String.class}).invoke(obj,(Object[])new String[]{HtmlUtils.htmlEscape(value)});
				}
			} catch (Exception e) {				
				e.printStackTrace();
				if(e instanceof ServiceException ) throw (ServiceException)e;
			}			
		}		
		return obj;		
	}
	public static String HtmlEscape(String x) throws ServiceException{
		if(RequestUtil.illegalKeys(x)){			
			throw new ServiceException(EventCode.WEB_PARAM_FILTER,new String[]{x});
		}
		return HtmlUtils.htmlEscape(x);
	}
	public static Map<String,Object> HtmlEscapeMap(Map<String,Object> mp) throws ServiceException{
		for (Iterator<String> iterator = mp.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			if(mp.get(key) instanceof String){
				if(RequestUtil.illegalKeys(String.valueOf(mp.get(key)))){
					throw new ServiceException(EventCode.WEB_PARAM_FILTER,new String[]{String.valueOf(mp.get(key))});
				}
				mp.put(key, HtmlUtils.htmlEscape(String.valueOf(mp.get(key))));
			}
			
		}
		return mp;
	}
	
	public static Map<String,String> HtmlEscapeStringMap(Map<String,String> mp) throws ServiceException{
		for (Iterator<String> iterator = mp.keySet().iterator(); iterator.hasNext();) {
			String key =  iterator.next();
				if(RequestUtil.illegalKeys(String.valueOf(mp.get(key)))){
					throw new ServiceException(EventCode.WEB_PARAM_FILTER,new String[]{String.valueOf(mp.get(key))});
				}
				mp.put(key, HtmlUtils.htmlEscape(mp.get(key)));
			
		}
		return mp;
	}
	
	public static String toString(Object obj){
		Method[] methods=obj.getClass().getMethods();	
		String str=obj.getClass().getName()+"->";
		for (int i = 0; i < methods.length; i++) {
			Method method=methods[i];
			try {
				String methodname=method.getName();				
				if(methodname.startsWith("get")){				
					String value=(String)method.invoke(obj,(Object[])null);
					str+=methodname+"["+value+"]";					
				}
			} catch (Exception e) {	
				
			}			
		}
		return str;
	}	
	public static String getAction(String ss){
		if(Validator.isNull(ss)||ss.equalsIgnoreCase("null")){
			return "#";
		}
		return ss;
	}	
	
	public static String getIpAddr(HttpServletRequest request) {
	       String ip = request.getHeader("x-forwarded-for");
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	           ip = request.getHeader("Proxy-Client-IP");
	       }
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	           ip = request.getHeader("WL-Proxy-Client-IP");
	       }
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	           ip = request.getHeader("X-Real-IP");
	       }
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	           ip = request.getRemoteAddr();
	       }
	       if(ip.indexOf(",")>0) ip=ip.split(",")[0];
	       return ip;
	   }

}
