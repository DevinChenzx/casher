package ebank.core.model.dao;
import ebank.core.model.domain.MerchantCertificate;
import ebank.core.model.domain.MerchantCertificates;

public class MerchantCertsDAO extends BaseDAO{	

	public MerchantCertificates obtainMerchantCertsByMerchantNo(String merchantNo){
		return (MerchantCertificates)this.getSqlMapClientTemplate().queryForObject("mcerts.obtionMerKey", merchantNo);
	}
	public MerchantCertificate obtainMerchantCertsByMerchantId(String customerId){
		return (MerchantCertificate)this.getSqlMapClientTemplate().queryForObject("mcerts.obtionMerKeyById", customerId);
	}
}
