package ebank.core.bank.logic;

import java.math.BigDecimal;
import java.util.HashMap;

import com.koalii.svs.SvsSign;
import com.koalii.svs.SvsVerify;

import ebank.core.bank.BankService;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;

public class SHBANK extends BankExt implements BankService {
	
	private String fileCert;
	private String filePfx;
	private String pfxKey;

	public String sendOrderToBank(BankOrder order) {
		
		long amount = Amount.getIntAmount(order.getAmount(), 2);
		//签名数字源参数顺序：商户编号，订单号，订单金额（分），支付币种
		String signDataStr=this.corpid+"|"+order.getRandOrderID()+"|"+amount+"|156";
		
		log.info("shanghai send signDataStr: "+signDataStr);
		
		SvsSign signer = new SvsSign();
		StringBuffer sf=new StringBuffer("");		
		//初始化签名证书和私钥
		try {
			signer.initSignCertAndKey(this.filePfx,this.pfxKey);
			// 签名，获得签名结果（Base64编码）
			String signData = signer.signData(signDataStr.getBytes());
			
			log.info("shanghai send signData: "+signData);
			// 获得签名证书的Base64编码
			String szCert = signer.getEncodedSignCert (); 
			
			sf.append("<form name=sendOrder method=\"post\" action=\"").append(this.getDesturl()).append("\" />");
			sf.append("<input type=\"hidden\" name=\"merchantID\" value=\"").append(this.corpid).append("\" />");
			sf.append("<input type=\"hidden\" name=\"merOrderNum\" value=\"").append(order.getRandOrderID()).append("\" />");
			sf.append("<input type=\"hidden\" name=\"merOrderAmt\" value=\"").append(amount).append("\" />");
			sf.append("<input type=\"hidden\" name=\"curType\" value=\"156\" />");
			sf.append("<input type=\"hidden\" name=\"orderDate\" value=\"").append(order.getPostdate().substring(0, 8)).append("\" />");
			sf.append("<input type=\"hidden\" name=\"orderTime\" value=\"").append(order.getPostdate().substring(8, order.getPostdate().length())).append("\" />");
			sf.append("<input type=\"hidden\" name=\"merNotifyUrl\" value=\"").append(this.getHttprecurl()).append("&NR=SID_").append(this.idx).append("\" />");
			sf.append("<input type=\"hidden\" name=\"merNotifySign\" value=\"1\" />");
			sf.append("<input type=\"hidden\" name=\"merGetGoodsUrl\" value=\"").append(this.getRecurl()).append("\" />");
			sf.append("<input type=\"hidden\" name=\"merGetGoodsSign\" value=\"1\" />");
			sf.append("<input type=\"hidden\" name=\"KoalB64Cert\" value=\"").append(szCert).append("\" />");
			sf.append("<input type=\"hidden\" name=\"signDataStr\" value=\"").append(signDataStr).append("\" />");
			sf.append("<input type=\"hidden\" name=\"signData\" value=\"").append(signData).append("\" />");	
			sf.append("</form>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("shanghai send formInfo: "+sf.toString());
		return sf.toString();
	}

	public PayResult getPayResult(HashMap reqs) throws ServiceException {
		String signDataStr = String.valueOf(reqs.get("signDataStr"));
		String signData = String.valueOf(reqs.get("signData"));
		log.info("shanghai return signDataStr : "+signDataStr+"\n"+" return signData : "+signData);
		PayResult bean = null;
		try {
			int nRet = SvsVerify.verify (signDataStr.getBytes(), signData, this.fileCert);
			if(nRet==0){//验签成功
				//0: 成功   1: 失败    2: 处理中    3: 可疑
				String tranResult = String.valueOf(reqs.get("tranResult"));
				log.info("return tranResult :"+tranResult);
				
				bean = new PayResult(String.valueOf(reqs.get("merOrderNum")), this.bankcode, new BigDecimal(Amount.getFormatAmount(String.valueOf(reqs.get("merOrderAmt")),-2)), "0".equals(tranResult)?1:-1);
				
				bean.setBankdate(String.valueOf(reqs.get("tranTime")));
				bean.setBanktransseq(String.valueOf(reqs.get("tranSerialNo")));
				bean.setBankresult(String.valueOf(reqs.get("tranResult")));
			}else{//验签失败
				throw new ServiceException(EventCode.CRYPT_VALIADATESIGN);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void setFilePfx(String filePfx) {
		this.filePfx = filePfx;
	}

	public void setPfxKey(String pfxKey) {
		this.pfxKey = pfxKey;
	}
	
	public String getFileCert() {
		return fileCert;
	}

	public void setFileCert(String fileCert) {
		this.fileCert = fileCert;
	}

}
