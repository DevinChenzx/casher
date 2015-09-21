package ebank.core.remote;

import org.apache.commons.httpclient.methods.PostMethod;

public interface HttpMethodCallback {
	public PostMethod initMethod(PostMethod method);

}
