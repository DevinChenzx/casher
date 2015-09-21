package ebank.core.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import embed.misc.DateTimeUtil;

import beartool.Base64Encoder;

public class Udate {
	public static String format(Date date){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);		
	}
	public static String format(Date date,String pattn){
		return new SimpleDateFormat(pattn).format(date);		
	}
	public static String getNotifyId(long partner,String seq,boolean salt){
		if(salt)
			return Base64Encoder.codeSha(partner+seq+Udate.format(new Date()));
		else
			return Base64Encoder.codeSha(partner+seq+"");
	}
	/***
	 * 按照指定格式返回当前时间
	 * @param format 格式
	 * @return String
	 */
	public static String getCurTime(String format)
	{
		String datestr =  "" ;
		SimpleDateFormat sf=null;
	   	try {
			sf = new java.text.SimpleDateFormat(format) ;
	   		Date date = new Date() ;
	   		datestr = sf.format(date);
	   		date=null;
	   	}catch (Exception ex){

	   	}finally{
	   		sf=null;
	   	}
		return datestr ;
	}
	
	/***
	 * 得到某日期前或者后n天的日期
	 * @param date
	 * @param i
	 * @return
	 */
	public static String getDate(Date date,int i)
	{
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.DATE,i);//你要加减的日期
		String strDay=formatter.format(rightNow.getTime());
		formatter=null;
		rightNow=null;
		return strDay;
	}
	
	/***
	 * 得到某日期前或者后n月的日期
	 * @param date
	 * @param i
	 * @return
	 */
	public static String getMonth(Date date,int i)
	{
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.MONTH,i);//你要加减的月份
		String strDay=formatter.format(rightNow.getTime());
		formatter=null;
		rightNow=null;
		return strDay;
	}
	/**
	 * 转换时间
	 * @param datestr
	 * @return
	 */
	public static Date getDate(String datestr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		if(datestr.length()==14){
			String sDate = datestr.substring(0, 4) + "-" + datestr.substring(4, 6)  + "-" + datestr.substring(6, 8)+" "+datestr.substring(8, 10)+":"+datestr.substring(10, 12)+":"+datestr.substring(12,14);
			try {
				date = sdf.parse(sDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
        return date;
	}
	
	
	/**
	 * 转换日期
	 * @param datestr
	 * @return
	 */
	public static String getDate(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
 		Date currentTime_1 = date;
 		String dateString = formatter.format(currentTime_1);
 		return dateString.trim();
	}
	
	/**
	 * 转换时间
	 * @param datestr
	 * @return
	 */
	public static String getTime(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
 		Date currentTime_1 = date;
 		String dateString = formatter.format(currentTime_1);
 		return dateString.trim();
	}
	public static void main(String[] args) {
		System.out.println(getTime(new Date()));
	}

}
