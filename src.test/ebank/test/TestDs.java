package ebank.test;

import java.lang.reflect.Field;

import org.apache.commons.dbcp.BasicDataSource;

public class TestDs extends BasicDataSource{
	public void config(){
		try{
			Field filed2=BasicDataSource.class.getDeclaredField("username");
			System.out.println(this.driverClassName);
			System.out.print(this.username);
			filed2.setAccessible(true);					
			filed2.set(this, "test_ismp");
		}catch(Exception ex){			
			ex.printStackTrace();
		}
	}

}
