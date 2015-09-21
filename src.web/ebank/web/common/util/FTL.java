package ebank.web.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * freemarker通用类
 * @since 2013.01.15
 * @version 1.0.0_1
 * 
 */
public class FTL {
	
	/**
     * 通过模板输出文件
     * 
     * @param map 模板输入参数
     * @param ftlPath 模板文件路径
     * @param ftlName 模板文件名
     * @param fileName 输出文件
     * @return true/false
     */
	public static boolean doFile(Map<String, Object> map, String ftlPath, String ftlName, String fileName) {
		boolean result = false;
		
		Writer out = null;
		try{
			Configuration configuration = new Configuration();
			configuration.setClassForTemplateLoading(FTL.class, ftlPath);
			Template template = configuration.getTemplate(ftlName);
			File file = new File(fileName); 
	        out =new OutputStreamWriter(new FileOutputStream(file));
			template.process(map, out);
			result = true;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
				out.close();
			}
			catch(Exception e){}
		}
		
		return result;
	}
	
	/**
     * 通过模板输出内容
     * 
     * @param map 模板输入参数
     * @param ftlPath 模板文件路径
     * @param ftlName 模板文件名
     * @return 输出内容
     */
	public static String doString(Map<String, Object> map, String ftlPath, String ftlName) {
		String result = "";
		
		Writer out = null;
		try{
			Configuration configuration = new Configuration();
			configuration.setClassForTemplateLoading(FTL.class, ftlPath);
			Template template = configuration.getTemplate(ftlName);
	        out = new StringWriter();
			template.process(map, out);
			result = out.toString();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
				out.close();
			}
			catch(Exception e){}
		}
		
		return result;
	}
}