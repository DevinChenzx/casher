/*
 * @Id: ServiceRunTimeException.java 10:42:29 2005-11-25
 * 
 * @author 
 * @version 1.0
 * ccpay PROJECT
 */
package ebank.core.common;

/**
 * @author 
 * Description: 运行时异常
 * 
 */
public class ServiceRuntimeException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
    public ServiceRuntimeException(){
        super();
    }
    public ServiceRuntimeException(String code){
       this.code=code;       
    }
	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
    

}
