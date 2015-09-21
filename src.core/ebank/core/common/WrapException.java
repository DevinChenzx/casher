package ebank.core.common;

public class WrapException {
	private String key;
	private String msg;
	public WrapException(Exception ex){
		ex.printStackTrace();		
		if(ex instanceof ServiceException){			
			this.key=((ServiceException)ex).getEventID();
			
			this.msg="";
		}else{
			this.key="";
			this.msg=ex.getMessage();
		}
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	

}
