package ebank.web.common.util;

import net.sf.json.JSONObject;

public class PremainTest {

	public static void main(String[] args) {
		
		
		String json="{no:'1',name:'lee'}";
		
		JSONObject object=JSONObject.fromObject(json);
		
		object.remove("no");
		
		System.out.println(object.toString());
	}
}
