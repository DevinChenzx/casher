package ebank.test.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;

import beartool.Md5Encrypt;


public class TestAccess implements Controller {	
	private Log log=LogFactory.getLog(this.getClass());

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse rep) throws Exception {
		
		Map<String, Object> mp=new HashMap<String, Object>();
		String partner=req.getParameter("partner");
		mp.put("partner", req.getParameter("partner"));
		
		String partnerkey=req.getParameter("key");
		
		String seller_email=req.getParameter("seller_email");
		mp.put("seller_email", req.getParameter("seller_email"));
		
		String out_trade_no=req.getParameter("out_trade_no");
		if(out_trade_no==null){
			out_trade_no=partner+"-"+new SimpleDateFormat("yyMMddHHmissSSS").format(new Date())+"-"+new Random().nextInt(10);
		}
		mp.put("out_trade_no", out_trade_no);
		
		String subject=req.getParameter("subject");
		mp.put("subject", subject);
		
		String price=req.getParameter("price");
		mp.put("price", price);
		
		if(partner==null){
			throw new ServiceException(EventCode.WEB_PARAMNULL,new String[]{"partner"});
		}
		if(seller_email==null){
			throw new ServiceException(EventCode.WEB_PARAMNULL,new String[]{"seller_email"});
		}		
		if(subject==null){
			throw new ServiceException(EventCode.WEB_PARAMNULL,new String[]{"subject"});
		}
		if(price==null){
			throw new ServiceException(EventCode.WEB_PARAMNULL,new String[]{"price"});
		}
		
		String return_url=req.getParameter("return_url");
		mp.put("return_url", return_url);
		
		String notify_url=req.getParameter("notify_url");
		mp.put("notify_url", notify_url);
		
		String show_url=req.getParameter("show_url");
		mp.put("show_url", show_url);			
		
		
		String body=req.getParameter("body");
		mp.put("body", body);
		
				
		String logistics_fee=req.getParameter("logistics_fee");
		mp.put("logistics_fee", logistics_fee);
		
		String logistics_type=req.getParameter("logistics_type");
		mp.put("logistics_type", logistics_type);
		
		String logistics_payment=req.getParameter("logistics_payment");
		mp.put("logistics_payment", logistics_payment);
		
		String quantity=req.getParameter("quantity");
		mp.put("show_url", quantity);
		
		String receive_name=req.getParameter("receive_name");
		mp.put("receive_name", receive_name);
		
		String receive_address=req.getParameter("receive_address");
		mp.put("receive_address", receive_address);
		
		
		String receive_zip=req.getParameter("receive_zip");
		mp.put("receive_zip", receive_zip);
		
		
		String receive_phone=req.getParameter("receive_phone");
		mp.put("receive_phone", receive_phone);
		
		
		String receive_mobile=req.getParameter("receive_mobile");
		mp.put("receive_mobile", receive_mobile);
		
		
		String logistics_fee_1=req.getParameter("logistics_fee_1");
		mp.put("logistics_fee_1", logistics_fee_1);
		
		String logistics_type_1=req.getParameter("logistics_type_1");
		mp.put("logistics_type_1", logistics_type_1);
		
		
		String logistics_payment_1=req.getParameter("logistics_payment_1");
		mp.put("logistics_payment_1", logistics_payment_1);
		
		
		String logistics_fee_2=req.getParameter("logistics_fee_2");
		mp.put("logistics_fee_2", logistics_fee_2);
		
		
		String logistics_type_2=req.getParameter("logistics_type_2");
		mp.put("logistics_type_2", logistics_type_2);
		
		
		String logistics_payment_2=req.getParameter("logistics_payment_2");
		mp.put("logistics_payment_2", logistics_payment_2);
		
		String buyer_email=req.getParameter("buyer_email");
		mp.put("buyer_email", buyer_email);
		
		
		String discount=req.getParameter("discount");
		mp.put("discount", discount);
		
		
		String input_charset=req.getParameter("input_charset");
		if(input_charset==null) input_charset="GBK";
		mp.put("input_charset", input_charset);
		
		
		
		String sign_type=req.getParameter("sign_type");
		if(sign_type==null) sign_type="MD5";
		mp.put("sign_type", sign_type);		
		
		String key=TestAccess.BuildMysign(mp, partnerkey,input_charset);	
		
		String form=TestAccess.BuildForm(partner, seller_email, return_url, notify_url, show_url, out_trade_no, subject, body, price, logistics_fee, logistics_type, 
				logistics_payment, quantity, receive_name, receive_address, 
				receive_zip, receive_phone, receive_mobile, logistics_fee_1, 
				logistics_type_1, logistics_payment_1, logistics_fee_2,
				logistics_type_2, logistics_payment_2, 
				buyer_email, 
				discount, input_charset, key, sign_type);
		
		rep.getOutputStream().write(form.getBytes());
		rep.getOutputStream().close();
		return null;
	}
	
	public static String BuildForm(String partner,
			String seller_email,
			String return_url,
			String notify_url,
			String show_url,
			String out_trade_no,
			String subject,
			String body,
			String price,
			String logistics_fee,
			String logistics_type,
			String logistics_payment,
			String quantity,
			String receive_name,
			String receive_address,
			String receive_zip,
            String receive_phone,
            String receive_mobile,
            String logistics_fee_1,
            String logistics_type_1,
            String logistics_payment_1,
            String logistics_fee_2,
            String logistics_type_2,
            String logistics_payment_2,
            String buyer_email,
            String discount,
            String input_charset,
            String key,
            String sign_type){
		Map<String,Object> sPara = new HashMap<String,Object>();
		sPara.put("service","trade_create_by_buyer");
		sPara.put("payment_type","1");
		sPara.put("partner", partner);
		sPara.put("seller_email", seller_email);
		sPara.put("return_url", return_url);
		sPara.put("notify_url", notify_url);
		sPara.put("_input_charset", input_charset);
		sPara.put("show_url", show_url);
		sPara.put("out_trade_no", out_trade_no);
		sPara.put("subject", subject);
		sPara.put("body", body);
		sPara.put("price", price);
		sPara.put("logistics_fee", logistics_fee);
		sPara.put("logistics_type", logistics_type);
		sPara.put("logistics_payment", logistics_payment);
		sPara.put("quantity", quantity);
		sPara.put("receive_name", receive_name);
		sPara.put("receive_address", receive_address);
		sPara.put("receive_zip", receive_zip);
		sPara.put("receive_phone", receive_phone);
		sPara.put("receive_mobile", receive_mobile);
		sPara.put("logistics_fee_1", logistics_fee_1);
		sPara.put("logistics_type_1", logistics_type_1);
		sPara.put("logistics_payment_1", logistics_payment_1);
		sPara.put("logistics_fee_2", logistics_fee_2);
		sPara.put("logistics_type_2", logistics_type_2);
		sPara.put("logistics_payment_2", logistics_payment_2);
		sPara.put("buyer_email", buyer_email);
		sPara.put("discount", discount);
		
		Map sParaNew = TestAccess.ParaFilter(sPara); //除去数组中的空值和签名参数
		String mysign = TestAccess.BuildMysign(sParaNew, key,input_charset);//生成签名结果
		
		StringBuffer sbHtml = new StringBuffer();
		List<String> keys = new ArrayList(sParaNew.keySet());
		
		String gateway="/portal?";
		
		sbHtml.append("<form id=\"easypsubmit\" name=\"easypsubmit\" action=\"" + gateway + "_input_charset=" + input_charset + "\" method=\"post\">");
		//POST方式传递（GET与POST二必选一）
		//sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + gateway + "_input_charset=" + input_charset + "\" method=\"post\">");
		
		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			String value = (String) sParaNew.get(name);
			
			sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
		}
        sbHtml.append("<input type=\"hidden\" name=\"sign\" value=\"" + mysign + "\"/>");
        sbHtml.append("<input type=\"hidden\" name=\"sign_type\" value=\"" + sign_type + "\"/>");           
        sbHtml.append("<script>document.forms['easypsubmit'].submit();</script>");
		
		return sbHtml.toString();
	}
	
	public static String BuildMysign(Map sArray, String key,String charset) {
		String prestr = CreateLinkString(sArray);  //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		prestr = prestr + key;                     //把拼接后的字符串再与安全校验码直接连接起来
		String mysign = Md5Encrypt.md5(prestr,charset);
		return mysign;
	}
	
	/** 
	 * 功能：除去数组中的空值和签名参数
	 * @param sArray 签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String,Object> ParaFilter(Map sArray){
		List<String> keys = new ArrayList(sArray.keySet());
		Map<String,Object> sArrayNew = new HashMap<String,Object>();
		
		for(int i = 0; i < keys.size(); i++){
			String key = (String) keys.get(i);
			String value = (String) sArray.get(key);
			
			if(value.equals("") || value == null || 
					key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")){
				continue;
			}
			
			sArrayNew.put(key, value);
		}
		
		return sArrayNew;
	}
	/** 
	 * 功能：把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * @param params 需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String CreateLinkString(Map params){
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) params.get(key);

			if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}
	

}
