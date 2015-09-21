/*
 * @Id: WebError.java 19:05:46 2006-2-20
 * 
 * @author 
 * @version 1.0
 * PAYGW_WEB_V6 PROJECT
 */
package ebank.web.common;

//import java.util.MissingResourceException;
//import java.util.ResourceBundle;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;

/**
 * @author xiexh
 * Description: web error ∂‘œÛ
 * 
 */
/*
 * Created on 2005-11-14
 * @author xiexh
 * @version 1.0
 * C2C_PACKET PROJECT
 */
public class WebError {
    private String eventid;
    private String eventmsg;    
    
    public WebError(Exception ex){
        if(ex instanceof ServiceException){
        	ServiceException x=(ServiceException)ex;
            eventid=String.valueOf(x.getEventID());              
            if(x.getParam()!=null){        	
            	eventmsg=MessageFormat.format(WebError.decode(eventid), x.getParam());        	
            }else
            	eventmsg=WebError.decode(eventid);
        }
        else{
            ex.printStackTrace(); //           
            eventid=String.valueOf(EventCode.UNEXCPECTED_EXCEPTION);     
            eventmsg=WebError.decode(eventid);
        }
    }
    public WebError(ServiceException ex){
        eventid=String.valueOf(((ServiceException)ex).getEventID());
        if(ex.getParam()!=null){        	
        	eventmsg=MessageFormat.format(WebError.decode(eventid), ex.getParam());        	
        }else
        	eventmsg=WebError.decode(eventid);
    }
   
    public static String decode(String eventname){    	      
        ResourceBundle rb=ResourceBundle.getBundle("conf/event"); 
		if(eventname==null)
			return "NO DEFINED EVENT CODE";
		else{
			try{
				return rb.getString(eventname);
			}catch(MissingResourceException ms){
				return rb.getString("100101");
			}
			catch(Exception ex){			   
				return eventname;
			}
		}
		
    	   
    }
   
    
    /**
     * @return Returns the eventid.
     */
    public String getEventid() {
        return eventid;
    }
    /**
     * @param eventid The eventid to set.
     */
    public void setEventid(String eventid) {
        this.eventid = eventid;
    }
    /**
     * @return Returns the eventmsg.
     */
    public String getEventmsg() {
        return eventmsg;
    }
    /**
     * @param eventmsg The eventmsg to set.
     */
    public void setEventmsg(String eventmsg) {
        this.eventmsg = eventmsg;
    }
}

