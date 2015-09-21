package ebank.test;

import java.io.BufferedReader;
import java.io.FileReader;

public class ParamsUtil {
	private static String dir="I:/payment/payment/filters\\";
	
	private static void println(String file){
		try {			
			FileReader fr = new FileReader(dir+"filter\\params\\"+file+".properties");//����FileReader����������ȡ�ַ���
            BufferedReader br = new BufferedReader(fr);    //����ָ���ļ�������
            while (br.ready()) {
                String line = br.readLine();//��ȡһ��                 
                if(line.indexOf("@")>0){
                	String[] x=line.split("@");
                	if(x!=null&&x.length>=2){
                		System.out.println(x[1]+"=");
                	}
                	
                }
               
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void antStr(String file){
		try {			
			FileReader fr = new FileReader(dir+"\\filterParam-"+file+".properties");//����FileReader����������ȡ�ַ���
            BufferedReader br = new BufferedReader(fr);    //����ָ���ļ�������
            while (br.ready()) {
                String line = br.readLine();//��ȡһ��                 
                if(line.indexOf("=")>0){
                	String[] x=line.split("=");
                	if(x!=null&&x.length>=2){
                		System.out.println("<filter token=\""+x[0]+"\" value=\"${"+x[0]+"}\" />");
                	}
                	
                }
               
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ParamsUtil.antStr("testing");
		
	}

}
