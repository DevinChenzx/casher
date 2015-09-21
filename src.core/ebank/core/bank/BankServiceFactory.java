/*
 * @Id: BeanFatory.java 14:06:11 2006-2-14
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.bank;



import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;


import ebank.core.bank.logic.BankCode;



/**
 * @author 
 * Description: 取得银行bean的工厂
 * 
 */
public class BankServiceFactory implements BeanFactoryAware{	
	
	private BeanFactory factory;
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	public void setBeanFactory(BeanFactory arg0) throws BeansException {
		this.factory=arg0;		
	}
	
	/**
	 * @return Returns the factory.
	 */
	public BeanFactory getFactory() {
		return factory;
	}

	/**
	 * 根据不同的银行编号返回各银行接口
	 * @param bankID
	 * @return
	 */
	public BankService getBank(ApplicationContext ctx,String BCIID){	
		String service=getServiceName(BCIID);
		if("".equals(service)){
			return null;
		}		
		return (BankService)ctx.getBean(service);
	}
	/**
	 * 取银行服务
	 * @param BCIID
	 * @return
	 */
	public BankService getBank(String BCIID){
		String service=getServiceName(BCIID);
		if("".equals(service)){
			return null;
		}	
		return (BankService)factory.getBean(service);
		
	}	
	public String getBankCode(String BCIID){
		try {
			for (int i = 0; i < BankCode.BKID.length; i++) {
				for(int j=0;j<BankCode.BKID[i].length;j++){				
					if(BCIID.equalsIgnoreCase(BankCode.BKID[i][j])){
						BCIID=BankCode.BKID[i][0];						
						break;
					}
				}
			}		
		} catch (Exception e) {			
			return BCIID;
		}
		
		return BCIID;		
	}
		
	public String getServiceName(String BCIID){			
		return "SID_"+getBankCode(BCIID);
		
	}	
	
	public static void main(String[] args) {
		String str="";
		System.out.println("old code="+str+ "new code="+new BankServiceFactory().getBankCode(str));
	}
	

}
