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
 * Description: 银行访问服务接口
 * 
 */
public interface BankService{
	public String sendOrderToBank(BankOrder order) throws ServiceException;//得到发送支付信息到银行的页面HTML	
	
	public PayResult getPayResult(HashMap reqs)throws ServiceException;            //取得支付结果:结果是一个list,第一个对象是PayResult
	public String getBankname();                                       //得到银行名称
	public String getBankcode();                                       //支付网关送达的银行编码	
	public String getCorpid();                                         //得到网银在银行的商户编号
	public String generateOrderID() throws ServiceException;           //生成订单号
	public String getRcvURL(HashMap reqs);                             //得到接收的验证URL
	public String getBillaccount();                                    //结算网关配置帐户
	public String getPaytype();                                        //取支付类型
	public String getKeyPassword();                                    //取密码
	public String getCardtype();                                       //取外卡类型
	public boolean isEnabled();                                        //是否开通
	public boolean isMultiCurrency();                                  //是否多币种
	public String getDesturl();                                        //get host url;	
	

}
