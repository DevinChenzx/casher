package ebank.core.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import ebank.core.common.ServiceException;

public class Amount {
	public static String currencyStanderize(String cur) throws ServiceException{		
		if("0".equals(cur)||cur==null||cur.equals("156")||cur.equalsIgnoreCase("RMB")||cur.equalsIgnoreCase("CNY")){
			return "CNY"; 
		}
        //840 ��Ԫ USD
		else if("1".equals(cur)||cur.equals("840")||cur.equalsIgnoreCase("USD")){
			return "USD"; 
		}
		//036 ����Ԫ AUD 
		else if(cur.equals("036")||cur.equalsIgnoreCase("AUD")){
			return "AUD"; 
		}
		//124 ���ô�Ԫ CAD 
		else if(cur.equals("124")||cur.equalsIgnoreCase("CAD")){
			return "CAD"; 
		}
		//910 ŷԪ EURO 
		else if(cur.equals("910")||cur.equalsIgnoreCase("EURO")||cur.equalsIgnoreCase("EUR")||"ERO".equalsIgnoreCase(cur)){
			return "EUR"; 
		}		
		//392 ��Ԫ JPY 
		else if(cur.equals("392")||cur.equalsIgnoreCase("JPY")){
			return "JPY"; 
		}
		//344 �۱� HKD 
		else if(cur.equals("344")||cur.equalsIgnoreCase("HKD")){
			return "HKD"; 
		}
		//764 ̩���� THB  
		else if(cur.equals("764")||cur.equalsIgnoreCase("THB")){
			return "THB"; 
		}
        //446 ����Ԫ MOP   
		else if(cur.equals("446")||cur.equalsIgnoreCase("MOP")){
			return "MOP"; 
		}
		//702 �¼���Ԫ SGD 
		else if(cur.equals("702")||cur.equalsIgnoreCase("SGD")){
			return "SGD"; 
		}
		else if("TWD".equalsIgnoreCase(cur)){
			return "TWD"; 
		}
		else if("RUB".equalsIgnoreCase(cur)){//¬��
			return "RUB"; 
		}
		else if("KRW".equalsIgnoreCase(cur)){//��Ԫ
			return "KRW"; 
		}
		//¬��
		else if(cur.equalsIgnoreCase("SUR")){
			return "SUR"; 
		}
		//������������
		else if(cur.equalsIgnoreCase("HUF")){
			return "HUF"; 
		}
		else if("GBP".equalsIgnoreCase(cur)){//Ӣ��
			return "GBP"; 
		}else if(cur.equalsIgnoreCase("NZD")||"554".equals(cur)){//554 ������Ԫ NZD 
			return "NZD";
		}
	    else if(cur.equalsIgnoreCase("FRF")||"250".equals(cur)){//554 ������Ԫ FRF 
	    	return "FRF";
	    }
		else{
			throw new ServiceException("��֧�ֵı��ִ���:"+cur);
		}
		/*
		 * RUB\GBP\\TWD\KRW\MOP

		040 �µ�������  
		056 ����ʱ����	
		158 ��̨���  
		208 ������� DKK 
		246 ������� FIM 
		250 �������� FRF 
		276 �¹���� DEM	
		380 ��������� ITL 		
		458 �������Ǳ� MYR 
		528 ������  		
		578 Ų������  
		608 ���ɱ����� PHP 	
		724 ������������  
		752 ������ RKS 
		756 ��ʿ���� SEK 	
		*/		
	}
	public static String getFormatAmount(String amount,int exp) {
		BigDecimal big = new BigDecimal(amount) ;
		BigDecimal big2=big.multiply(new BigDecimal(Math.pow(10,exp)));		
		return new DecimalFormat("0.00").format(big2);
	}
	
	public static long getIntAmount(String amount,int exp) {
		BigDecimal big = new BigDecimal(amount) ;
		BigDecimal big2=big.multiply(new BigDecimal(Math.pow(10,exp)));		
		return new BigDecimal(new DecimalFormat("#.00").format(big2)).longValue();
	}
	
	public static void main(String[] args){
		
		System.out.println(getFormatAmount("1.0",-2));
		
	}
	
}
