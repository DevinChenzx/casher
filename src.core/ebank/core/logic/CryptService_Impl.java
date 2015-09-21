/*
 * @Id: CryptService_Impl.java 13:38:45 2006-2-18
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.logic;

import org.apache.log4j.Logger;

import com.chinabank.crypto.Constant;
import com.chinabank.crypto.Crypto;
import com.chinabank.crypto.CryptoException;
import com.chinabank.crypto.CryptoFactory;

import ebank.core.CryptService;
import ebank.core.common.Constants;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;



/**
 * @author 
 * Description: ��ȫ��֤����ʵ��
 * 
 */
public class CryptService_Impl implements CryptService {

	private Logger logger = Logger.getLogger(CryptService_Impl.class); 

	static CryptoFactory cryptoFactory_rsa =CryptoFactory.getInstance(Constant.CONF_RSA);
	static CryptoFactory cryptoFactory_md5 =CryptoFactory.getInstance(Constant.CONF_MD5);
	private String certmerchant; //������Կ֤������̻�
	
	/* (non-Javadoc)
	 * @see ebank.core.service.CryptService#getCertEncodeMsg(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getCryptMsg(String strOrig, String merchantid,
			String mode) throws ServiceException{
		String r=null;//�̻��Ĺ�Կ����
	    try{
	    	Crypto chinabankCrypto=null;
		    if(Constants.CRYPT_RSA.equalsIgnoreCase(mode)){
		    	chinabankCrypto= cryptoFactory_rsa.getCrypto(merchantid);
		        
		    }else if(Constants.CRYPT_MD5.equalsIgnoreCase(mode)){
		    	chinabankCrypto= cryptoFactory_md5.getCrypto(merchantid); //base64
		    }else{
		    	logger.debug("mode="+mode);
		    	throw new ServiceException(EventCode.CRYPT_MODE_NOTSUPPORT,mode);
		    }		    		    
		    r=chinabankCrypto.encryptData(strOrig);
		   
		} catch (CryptoException e) {
			if(logger.isDebugEnabled()) e.printStackTrace();
			throw new ServiceException(EventCode.CRYPT_EXCEPTION);
		}
		return r; 		
	}
	/* (non-Javadoc)
	 * @see ebank.core.CryptService#getSignMsg(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getSignMsg(String strOrig, String merchantid,
			String mode) throws ServiceException{
		String r=null;//���ǵ�˽Կǩ����MD5�̻�MD5˽Կǩ����
	    try{	        
	    	Crypto chinabankCrypto=null;
		    if(Constants.CRYPT_RSA.equalsIgnoreCase(mode)){
		    	chinabankCrypto= cryptoFactory_rsa.getCrypto(certmerchant); //��֤��
		        
		    }else if(Constants.CRYPT_MD5.equalsIgnoreCase(mode)){
		    	chinabankCrypto= cryptoFactory_md5.getCrypto(merchantid); //TODO
		    }else{
		    	logger.debug("mode="+mode);
		    	logger.debug("merchantid="+merchantid);
		    	throw new ServiceException(EventCode.CRYPT_MODE_NOTSUPPORT,mode);
		    }	    
		    r=chinabankCrypto.signData(strOrig);
	    }catch (CryptoException e) {
			if(logger.isDebugEnabled()) e.printStackTrace();
			throw new ServiceException(EventCode.CRYPT_EXCEPTION);
		}
		return r; 	
    }
	

	/* (non-Javadoc)
	 * @see ebank.core.service.CryptService#getCertDecodeMsg(java.lang.String, java.lang.String)
	 */
	public String getCertDecodeMsg(String strEncode, String mode)throws ServiceException {
		//MD5: ������˽Կ����
		//TODO �̻���Կ����(*)��
		try {
			CryptoFactory cryptoFactory;
		    if(Constants.CRYPT_RSA.equals(mode)){
		        cryptoFactory=cryptoFactory_rsa;
		    }else if(Constants.CRYPT_MD5.equals(mode)){
		        cryptoFactory=cryptoFactory_md5;
		    }else{		    	
		    	throw new ServiceException(EventCode.CRYPT_MODE_NOTSUPPORT,mode);
		    }		    
		    Crypto chinabankCrypto = cryptoFactory.getCrypto(certmerchant);		    
			String decode = chinabankCrypto.decryptData(strEncode);
			return decode;
		}catch(Exception ex){
			if(logger.isDebugEnabled()) ex.printStackTrace();
			    throw new ServiceException(EventCode.CRYPT_EXCEPTION);
		}		
	}

	/* (non-Javadoc)
	 * @see ebank.core.service.CryptService#verfiysign(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean verfiysign(String merchantid,String mode,String str, String sign) {
		try{
			CryptoFactory cryptoFactory;
			if(Constants.CRYPT_RSA.equals(mode)){
		        cryptoFactory=cryptoFactory_rsa;
		    }else if(Constants.CRYPT_MD5.equals(mode)){
		        cryptoFactory=cryptoFactory_md5;
		    }else{
		    	throw new ServiceException(EventCode.CRYPT_MODE_NOTSUPPORT,mode);
		    }
			
			Crypto crypto = cryptoFactory.getCrypto(merchantid); //�̻���Կ��֤ǩ��
			
			if(logger.isDebugEnabled()){
				logger.debug("mode="+mode);
				logger.debug("class="+crypto.getClass().getName());
				logger.debug("merchantid="+merchantid);
				logger.debug("to verfiystr="+str);
				logger.debug("sign="+sign);
			}
		    //��֤
			boolean flag = crypto.verifyData(str, sign);
			return flag;
		}catch(Exception ex){
			if(logger.isDebugEnabled()) ex.printStackTrace();						
		}
		return false;
	}
	/**
	 * @param certmerchant The certmerchant to set.
	 */
	public void setCertmerchant(String certmerchant) {
		this.certmerchant = certmerchant;
	}
	

}
