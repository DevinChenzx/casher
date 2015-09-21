/*
 * @Id: LocaleUtil.java 18:25:55 2006-9-8
 * 
 * @author 
 * @version 1.0
 * payment_web_v2.0 PROJECT
 */
package ebank.web.common.util;

/**
 * @author 
 * Description: 本地化方法
 * 
 */
public class LocaleUtil {
	public static String[] lang={"CN","US","EN","FR","JP","KR","BIG5"};
	public static String getLocale(String e,String defaults){
		if(e==null) return defaults;
		for (int i = 0; i < lang.length; i++) {
			if(e.equalsIgnoreCase(lang[i])){
				if("EN".equalsIgnoreCase(lang[i])) return "US";
				return lang[i];
			}
		}		
		return defaults;			
	}
	public static String getLocalByIndex(int index){
		if(index>lang.length||index<0){ return lang[0];}
		else if(index==2) return lang[1];
		else return lang[index];
	}
	public static int getLocalIndex(String e){
		if(e==null) return 0;
		for (int i = 0; i < lang.length; i++) {
			if("EN".equalsIgnoreCase(e)) return 1;
			if(e.equalsIgnoreCase(lang[i])){				
				return i;
			}
		}		
		return 0;
	}

}
