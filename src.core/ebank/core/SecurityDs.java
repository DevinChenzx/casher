package ebank.core;

import java.lang.reflect.Field;

import org.apache.commons.dbcp.BasicDataSource;

import beartool.CipherUtil;

public class SecurityDs extends BasicDataSource{
	private String dskey;
	
	public void init(){
		try{			
			
			Field f_username=BasicDataSource.class.getDeclaredField("username");
			Field f_url=BasicDataSource.class.getDeclaredField("url");
		    Field f_password=BasicDataSource.class.getDeclaredField("password");		    
						
		    f_username.setAccessible(true);					
		    f_username.set(this, CipherUtil.decryptData(this.username, this.dskey));
		    		    		   
		    f_url.setAccessible(true);					
		    f_url.set(this, CipherUtil.decryptData(this.url, this.dskey));		    
		       
		    
		    f_password.setAccessible(true);					
		    f_password.set(this, CipherUtil.decryptData(this.password, this.dskey));		    		    	    
		    
		}catch(Exception ex){			
			ex.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		
		String s="f94aa415f68478063b8e9d89146f9d8e9b1bf4613351164168dcf72faa4b8f1217be378eda31b8ea96cf65634a9ac478";
		try {
			System.out.println(CipherUtil.decryptData(s, "1a8d9d6a45713f79"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getDskey() {
		return dskey;
	}
	public void setDskey(String dskey) {
		this.dskey = dskey;
	}
	
	

}
