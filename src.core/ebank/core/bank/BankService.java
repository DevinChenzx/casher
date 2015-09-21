/*
 * @Id: BankService.java 17:02:39 2006-2-13
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.bank;

import java.util.HashMap;

import ebank.core.common.ServiceException;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;


/**
 * @author xiexh
 * Description: ���з��ʷ���ӿ�
 * 
 */
public interface BankService{
	public String sendOrderToBank(BankOrder order) throws ServiceException;//�õ�����֧����Ϣ�����е�ҳ��HTML	
	
	public PayResult getPayResult(HashMap reqs)throws ServiceException;            //ȡ��֧�����:�����һ��list,��һ��������PayResult
	public String getBankname();                                       //�õ���������
	public String getBankcode();                                       //֧�������ʹ�����б���	
	public String getCorpid();                                         //�õ����������е��̻����
	public String generateOrderID() throws ServiceException;           //���ɶ�����
	public String getRcvURL(HashMap reqs);                             //�õ����յ���֤URL
	public String getBillaccount();                                    //�������������ʻ�
	public String getPaytype();                                        //ȡ֧������
	public String getKeyPassword();                                    //ȡ����
	public String getCardtype();                                       //ȡ�⿨����
	public boolean isEnabled();                                        //�Ƿ�ͨ
	public boolean isMultiCurrency();                                  //�Ƿ�����
	public String getDesturl();                                        //get host url;	
	

}
