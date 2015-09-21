package ebank.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ebank.core.common.util.Amount;
class TestConn
{
        public static void main(String[] args)
        {
        	for(int i=0;i<800; i++){ 
        		System.out.println("thread "+i);
        	new Thread(){
        	    	public void run(){
		                Connection conn =null;
		                try{
		                        Class.forName("oracle.jdbc.driver.OracleDriver");
		                        conn = DriverManager.getConnection( "jdbc:oracle:thin:@10.68.76.96:1521:orcl", "test_ismp", "test_ismp");
		                        if (conn!=null)
		                                System.out.println("connection successful");
		                        else
		                                System.out.println("connection failure");
		                        this.sleep(100);
		                }catch (Exception ex) {
		                        System.out.println("conn:"+ex.getMessage());
		                }finally{
		                        try{
		                        conn.close();
		                        }catch(Exception e){}
		                }
        	    	}
        	    	
        	    }.start();
        }
        }
}

