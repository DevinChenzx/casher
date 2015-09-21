/*
 * @Id: Validator.java 11:16:09 2006-4-3
 * 
 * @author 
 * @version 1.0
 * PAYGW_WEB_V6 PROJECT
 */
package ebank.web.common.util;

import org.apache.log4j.Logger;


/**
 * @author xiexh
 * Description: 校验参数
 * 
 */
public class Validator {
	public static Logger log = Logger.getLogger(Validator.class); 	
	
	public static boolean isNull(Object obj){
		if(obj==null||"".equals(obj.toString().trim())||"null".equalsIgnoreCase(String.valueOf(obj))){
			return true;			
		}
		return false;
	}
	public static boolean isNum(Object obj){
		if(obj!=null){
			return String.valueOf(obj).matches("^(0|[1-9][0-9]*)$");
		}else return false;
	}
	public static String currencyStanderize(String cur) {		
		if("0".equals(cur)||cur==null||cur.equals("156")||cur.equalsIgnoreCase("RMB")||cur.equalsIgnoreCase("CNY")){
			return "CNY"; 
		}
        //840 美元 USD
		else if("1".equals(cur)||cur.equals("840")||cur.equalsIgnoreCase("USD")){
			return "USD"; 
		}
		//036 澳洲元 AUD 
		else if(cur.equals("036")||cur.equalsIgnoreCase("AUD")){
			return "AUD"; 
		}
		//124 加拿大元 CAD 
		else if(cur.equals("124")||cur.equalsIgnoreCase("CAD")){
			return "CAD"; 
		}
		//910 欧元 EURO 
		else if(cur.equals("910")||cur.equalsIgnoreCase("EURO")||cur.equalsIgnoreCase("EUR")||"ERO".equalsIgnoreCase(cur)){
			return "EUR"; 
		}		
		//392 日元 JPY 
		else if(cur.equals("392")||cur.equalsIgnoreCase("JPY")){
			return "JPY"; 
		}
		//344 港币 HKD 
		else if(cur.equals("344")||cur.equalsIgnoreCase("HKD")){
			return "HKD"; 
		}
		//764 泰国铢 THB  
		else if(cur.equals("764")||cur.equalsIgnoreCase("THB")){
			return "THB"; 
		}
        //446 澳门元 MOP   
		else if(cur.equals("446")||cur.equalsIgnoreCase("MOP")){
			return "MOP"; 
		}
		//702 新加坡元 SGD 
		else if(cur.equals("702")||cur.equalsIgnoreCase("SGD")){
			return "SGD"; 
		}
		else if("TWD".equalsIgnoreCase(cur)){
			return "TWD"; 
		}
		else if("RUB".equalsIgnoreCase(cur)){//卢布
			return "RUB"; 
		}
		else if("KRW".equalsIgnoreCase(cur)){//韩元
			return "KRW"; 
		}
		//卢布
		else if(cur.equalsIgnoreCase("SUR")){
			return "SUR"; 
		}
		//匈牙利：福林
		else if(cur.equalsIgnoreCase("HUF")){
			return "HUF"; 
		}
		else if("GBP".equalsIgnoreCase(cur)){//英镑
			return "GBP"; 
		}else if(cur.equalsIgnoreCase("NZD")||"554".equals(cur)){//554 新西兰元 NZD 
			return "NZD";
		}
	    else if(cur.equalsIgnoreCase("FRF")||"250".equals(cur)){//554 新西兰元 FRF 
	    	return "FRF";
	    }
		else{
			return null;
		}
		/*
		 * RUB\GBP\\TWD\KRW\MOP

		040 奥地利先令  
		056 比利时法郎	
		158 新台湾币  
		208 丹麦克朗 DKK 
		246 芬兰马克 FIM 
		250 法国法郎 FRF 
		276 德国马克 DEM	
		380 意大利里拉 ITL 		
		458 马来西亚币 MYR 
		528 荷兰盾  		
		578 挪威克朗  
		608 菲律宾比索 PHP 	
		724 西班牙比塞塔  
		752 瑞典克朗 RKS 
		756 瑞士法郎 SEK 	
		*/		
	}
}
