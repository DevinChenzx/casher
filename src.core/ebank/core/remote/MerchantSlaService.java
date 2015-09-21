/*
 * @Id: MerchantSlaService.java 9:25:39 2007-3-15
 * 
 * @author 
 * @version 1.0
 * payment_core PROJECT
 */
package ebank.core.remote;

import java.util.List;

import ebank.core.model.domain.GW01Service;
import ebank.core.model.domain.GwViewUser;

/**
 * @author 
 * Description: ����ֹͨͣά���ӿ�
 * 
 */
public interface MerchantSlaService {
	/**
	 * @param merchantid
	 * @return ��ѯ�̻�����
	 */
	public List getServices(String merchantid);
	/**
	 * ���»��߲������
	 * @param gw01
	 * @return
	 */
	public void doServices(GW01Service gw01);
	
	/**
	 * ��ѯ�̻�
	 * @param merchantid
	 * @return
	 */
	public GwViewUser getSlaMerchant(String merchantid);
	/**
	 * �����̻�
	 * @param gw00
	 * @return
	 */
	public int doSlaMerchant(GwViewUser gw00);

}
