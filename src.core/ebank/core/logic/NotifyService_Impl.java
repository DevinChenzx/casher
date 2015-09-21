package ebank.core.logic;

import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import ebank.core.NoticeService;
import ebank.core.common.ServiceException;
import ebank.core.model.dao.GwNotifyDAO;
import ebank.core.model.domain.MapiAsyncNotify;
import ebank.core.remote.HttpClientService;
import ebank.core.remote.HttpMethodCallback;

public class NotifyService_Impl implements NoticeService{
	private GwNotifyDAO nfDAO;
	private HttpClientService httpClientService;
	
	
	public Long tx_saveAsynNotice(MapiAsyncNotify man)
			throws ServiceException {
		return nfDAO.saveAsyNotify(man);		
	}
	
		

	public boolean tx_updateNoticeStatus(MapiAsyncNotify notice)
			throws ServiceException {		
		return nfDAO.updateAsyNotify(notice)==1?true:false;
	}





	public String tx_responseNotice(String charset,String address,final NameValuePair[] gnt) throws ServiceException {
		Map mp=httpClientService.getHttpResp(charset,address, new HttpMethodCallback() {			
			public PostMethod initMethod(PostMethod method) {				
				method.setRequestBody(gnt);			
				return method;
			}
		}, null, null);
		if(mp!=null&&"200".equals(String.valueOf(mp.get("statusCode"))))
		return String.valueOf(mp.get("response"));
		else{
			return "fail";
		}
	}	
	
	public GwNotifyDAO getNfDAO() {
		return nfDAO;
	}

	public void setNfDAO(GwNotifyDAO nfDAO) {
		this.nfDAO = nfDAO;
	}



	public void setHttpClientService(HttpClientService httpClientService) {
		this.httpClientService = httpClientService;
	}	
	

}
