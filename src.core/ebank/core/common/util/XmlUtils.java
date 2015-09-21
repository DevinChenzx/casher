/*
 * @Id: XmlUtil.java 11:18:01 2006-12-8
 * 
 * @author 
 * @version 1.0
 * payment_agent PROJECT
 */
package ebank.core.common.util;

import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlUtils {
	
	/**
	 * 取节点值
	 * @param plain
	 * @param nodename
	 * @return
	 */
	public static String getNode(String plain, String nodename) {
		if(plain.indexOf("<" + nodename + ">")<0) return "";
		return plain.substring(plain.indexOf("<" + nodename + ">")
				+ nodename.length() + 2, plain.indexOf("</" + nodename + ">"));
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDate(String date){
		
		if("".equals(date) || date==null || "null".equals(date)){
			return null;
		}else{
			String[] obj = date.split("-");
		    int year = Integer.valueOf((String)obj[0]).intValue();
			int month=Integer.valueOf((String)obj[1]).intValue();
			int day= Integer.valueOf((String)obj[2]).intValue(); 
			Calendar calendar =new GregorianCalendar(year,month-1,day);
			return calendar.getTime();
		}
	}
	public static Document getDocument(String xml){
		if(xml==null||xml.indexOf("<?xml")<0) return null;
	    try {
	    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(xml.getBytes()));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	public static String getNodeValue(Document doc,String tagname){		
		if(doc!=null) return ((Element)doc.getElementsByTagName(tagname).item(0)).getFirstChild().getNodeValue();
		else return "";
	}
	
}
