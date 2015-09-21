package ebank.core.bank.third;

import java.math.BigDecimal;
import java.util.HashMap;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import bill99.MD5Util;

import ebank.core.bank.BankService;
import ebank.core.bank.logic.BankExt;
import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.BankOrder;
import ebank.core.domain.PayResult;
import ebank.web.common.util.RequestUtil;

/**
 * @author Kitian
 * 快钱
 */
public class Bill99 extends BankExt implements BankService{
	static Logger logger = Logger.getLogger(Bill99.class);
	
	public String sendOrderToBank(BankOrder order) throws ServiceException {
		//accumlate the merchant params
		HashMap mp=order.getMp();
		JSONObject jo=null;
		if(mp!=null&&mp.get("outJson")!=null){
			String outjson=CryptUtil.decrypt(String.valueOf(mp.get("outJson")));
			logger.info(outjson);
			jo=JSONObject.fromObject(outjson);
		}
		
		final String inputCharset="1";
		String pageUrl=this.getRecurl();
		String  bgUrl=this.getRecurl()+"&NR=SID_"+this.getIdx()+"&";
		final String version="v2.0";
		final String language="1";
		final String signType="1";
		final String merchantAcctId=this.getCorpid();
		String payerName=getJsonParams(jo,"payerName","");
		final String payerContactType="1";
		String payerContact=getJsonParams(jo,"payerContact","");
		
		String orderId=order.getRandOrderID();
		String orderAmount=String.valueOf(Amount.getIntAmount(order.getAmount(), 2));
		String orderTime=order.getPostdate();
		String productName=getJsonParams(jo,"productName","");
		String productNum=getJsonParams(jo,"productNum","");		
		String productId=getJsonParams(jo,"productId","");
		String productDesc=getJsonParams(jo,"productDesc","");
		String ext1=getJsonParams(jo,"ext1","");
		String ext2=getJsonParams(jo,"ext2","");
		
		String gateID="";
		if (order.getMp() != null && order.getMp().get("outChannel") != null) {
			gateID = String.valueOf(order.getMp().get("outChannel"));
		}
		String payType=getJsonParams(jo,"payType","".equals(gateID)?"00":"10");
		
		String bankId=getJsonParams(jo,"bankId",gateID);
		
		String redoFlag=getJsonParams(jo,"redoFlag","0");
		String pid=getJsonParams(jo,"pid","");
		
		String key=this.getKeyPassword();
		
		
		String signMsgVal="";
		signMsgVal=appendParam(signMsgVal,"inputCharset",inputCharset);
		signMsgVal=appendParam(signMsgVal,"pageUrl",pageUrl);
		signMsgVal=appendParam(signMsgVal,"bgUrl",bgUrl);
		signMsgVal=appendParam(signMsgVal,"version",version);
		signMsgVal=appendParam(signMsgVal,"language",language);
		signMsgVal=appendParam(signMsgVal,"signType",signType);
		signMsgVal=appendParam(signMsgVal,"merchantAcctId",merchantAcctId);
		signMsgVal=appendParam(signMsgVal,"payerName",payerName);
		signMsgVal=appendParam(signMsgVal,"payerContactType",payerContactType);
		signMsgVal=appendParam(signMsgVal,"payerContact",payerContact);
		signMsgVal=appendParam(signMsgVal, "orderId",orderId);
		signMsgVal=appendParam(signMsgVal,"orderAmount",orderAmount);
		signMsgVal=appendParam(signMsgVal,"orderTime",orderTime);
		signMsgVal=appendParam(signMsgVal,"productName",productName);
		signMsgVal=appendParam(signMsgVal,"productNum",productNum);
		signMsgVal=appendParam(signMsgVal,"productId",productId);
		signMsgVal=appendParam(signMsgVal,"productDesc",productDesc);
		signMsgVal=appendParam(signMsgVal,"ext1",ext1);
		signMsgVal=appendParam(signMsgVal,"ext2",ext2);
		signMsgVal=appendParam(signMsgVal,"payType",payType);
		signMsgVal=appendParam(signMsgVal,"bankId",bankId);
		
		signMsgVal=appendParam(signMsgVal,"redoFlag",redoFlag);
		signMsgVal=appendParam(signMsgVal,"pid",pid);
		signMsgVal=appendParam(signMsgVal,"key",key);
		String signMsg="";
        try {
        	signMsg=MD5Util.md5Hex(signMsgVal.getBytes("UTF-8")).toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		StringBuffer sf=new StringBuffer("");		
		sf.append("<form name=sendOrder method=\"post\" action=\""+this.getDesturl()+"\">");
		sf.append("<input type=\"hidden\" name=\"inputCharset\" value=\""+inputCharset+"\" >");
		sf.append("<input type=\"hidden\" name=\"pageUrl\" value=\""+pageUrl+"\" >");
		sf.append("<input type=\"hidden\" name=\"bgUrl\" value=\""+bgUrl+"\" >");
		sf.append("<input type=\"hidden\" name=\"version\" value=\""+version+"\" >");
		sf.append("<input type=\"hidden\" name=\"language\" value=\""+language+"\" >");
		sf.append("<input type=\"hidden\" name=\"signType\" value=\""+signType+"\" >");

		sf.append("<input type=\"hidden\" name=\"merchantAcctId\" value=\""+merchantAcctId+"\" >");
		sf.append("<input type=\"hidden\" name=\"payerName\" value=\""+payerName+"\" >");
		sf.append("<input type=\"hidden\" name=\"payerContactType\" value=\""+payerContactType+"\" >");
		sf.append("<input type=\"hidden\" name=\"payerContact\" value=\""+payerContact+"\" >");
		
		sf.append("<input type=\"hidden\" name=\"orderId\" value=\""+orderId+"\" >");
		sf.append("<input type=\"hidden\" name=\"orderAmount\" value=\""+orderAmount+"\" >");
		sf.append("<input type=\"hidden\" name=\"orderTime\" value=\""+orderTime+"\" >");
		sf.append("<input type=\"hidden\" name=\"productName\" value=\""+productName+"\" >");
		sf.append("<input type=\"hidden\" name=\"productNum\" value=\""+productNum+"\" >");		
		sf.append("<input type=\"hidden\" name=\"productId\" value=\""+productId+"\" >");
		sf.append("<input type=\"hidden\" name=\"productDesc\" value=\""+productDesc+"\" >");
		sf.append("<input type=\"hidden\" name=\"ext1\" value=\""+ext1+"\" >");
		sf.append("<input type=\"hidden\" name=\"ext2\" value=\""+ext2+"\" >");		
		sf.append("<input type=\"hidden\" name=\"payType\" value=\""+payType+"\" >");
		sf.append("<input type=\"hidden\" name=\"bankId\" value=\""+bankId+"\" >");
		sf.append("<input type=\"hidden\" name=\"redoFlag\" value=\""+redoFlag+"\" >");
		sf.append("<input type=\"hidden\" name=\"pid\" value=\""+pid+"\" >");
		sf.append("<input type=\"hidden\" name=\"signMsg\" value=\""+signMsg+"\" >");
		
		sf.append("</form>");		
		if(logger.isDebugEnabled()) logger.debug(sf.toString());
		return sf.toString();
		
	}
	
	public String appendParam(String returnStr,String paramId,String paramValue)
	{
			if(!returnStr.equals(""))
			{
				if(!paramValue.equals(""))
				{
					returnStr=returnStr+"&"+paramId+"="+paramValue.trim();
				}
			}
			else
			{
				if(!paramValue.equals(""))
				{
				returnStr=paramId+"="+paramValue;
				}
			}	
			return returnStr;
	}


	public PayResult getPayResult(HashMap request) throws ServiceException {
		//获取人民币网关账户号
		String merchantAcctId=(String)request.get("merchantAcctId");

		//设置人民币网关密钥
		///区分大小写
		String key=this.getKeyPassword();

		//获取网关版本.固定值
		///快钱会根据版本号来调用对应的接口处理程序。
		///本代码版本号固定为v2.0
		String version=(String)request.get("version");

		//获取语言种类.固定选择值。
		///只能选择1、2、3
		///1代表中文；2代表英文
		///默认值为1
		String language=(String)request.get("language");

		//签名类型.固定值
		///1代表MD5签名
		///当前版本固定为1
		String signType=(String)request.get("signType");

		//获取支付方式
		///值为：10、11、12、13、14
		///00：组合支付（网关支付页面显示快钱支持的各种支付方式，推荐使用）10：银行卡支付（网关支付页面只显示银行卡支付）.11：电话银行支付（网关支付页面只显示电话支付）.12：快钱账户支付（网关支付页面只显示快钱账户支付）.13：线下支付（网关支付页面只显示线下支付方式）.14：B2B支付（网关支付页面只显示B2B支付，但需要向快钱申请开通才能使用）
		String payType=(String)request.get("payType");

		//获取银行代码
		///参见银行代码列表
		String bankId=(String)request.get("bankId");

		//获取商户订单号
		String orderId=(String)request.get("orderId");

		//获取订单提交时间
		///获取商户提交订单时的时间.14位数字。年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
		///如：20080101010101
		String orderTime=(String)request.get("orderTime");

		//获取原始订单金额
		///订单提交到快钱时的金额，单位为分。
		///比方2 ，代表0.02元
		String orderAmount=(String)request.get("orderAmount");

		//获取快钱交易号
		///获取该交易在快钱的交易号
		String dealId=(String)request.get("dealId");

		//获取银行交易号
		///如果使用银行卡支付时，在银行的交易号。如不是通过银行支付，则为空
		String bankDealId=(String)request.get("bankDealId");

		//获取在快钱交易时间
		///14位数字。年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
		///如；20080101010101
		String dealTime=(String)request.get("dealTime");

		//获取实际支付金额
		///单位为分
		///比方 2 ，代表0.02元
		String payAmount=(String)request.get("payAmount");

		//获取交易手续费
		///单位为分
		///比方 2 ，代表0.02元
		String fee=(String)request.get("fee");

		//获取扩展字段1
		String ext1=(String)request.get("ext1");

		//获取扩展字段2
		String ext2=(String)request.get("ext2");

		//获取处理结果
		///10代表 成功11代表 失败
		String payResult=(String)request.get("payResult");

		//获取错误代码
		///详细见文档错误代码列表
		String errCode=(String)request.get("errCode");

		//获取加密签名串
		String signMsg=(String)request.get("signMsg");



		//生成加密串。必须保持如下顺序。
		String merchantSignMsgVal="";
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"merchantAcctId",merchantAcctId);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"version",version);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"language",language);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"signType",signType);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"payType",payType);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"bankId",bankId);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"orderId",orderId);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"orderTime",orderTime);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"orderAmount",orderAmount);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"dealId",dealId);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"bankDealId",bankDealId);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"dealTime",dealTime);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"payAmount",payAmount);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"fee",fee);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"ext1",ext1);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"ext2",ext2);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"payResult",payResult);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"errCode",errCode);
		merchantSignMsgVal=appendParam(merchantSignMsgVal,"key",key);
    
		String merchantSignMsg=null;
		try {
			merchantSignMsg=MD5Util.md5Hex(merchantSignMsgVal.getBytes("UTF-8")).toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PayResult bean=null;
		if(signMsg.toUpperCase().equals(merchantSignMsg.toUpperCase())){
			bean=new PayResult(orderId,this.bankcode,new BigDecimal(Amount.getFormatAmount(payAmount, -2)),"10".equals(payResult)?1:-1);
			bean.setBankresult(payResult);
			bean.setBanktransseq(dealId);
			bean.setBankdate(dealTime.substring(0,8));
			if(("SID_"+this.idx).equals(request.get("NR"))){
				request.put("RES","<result>1</result><redirecturl>"+this.getRecurl()+"</redirecturl>");
			}
		}else{
			if(("SID_"+this.idx).equals(request.get("NR"))){
				request.put("RES","<result>0</result><redirecturl>"+this.getRecurl()+"&ERR="+EventCode.SIGN_VERIFY+"</redirecturl>");
			}else{
				throw new ServiceException(EventCode.SIGN_VERIFY);
			}
		}		
		return bean;
	}
	
	private String getJsonParams(JSONObject jo,String key,String defaults){
		try{
			if(jo!=null) return jo.getString(key)==null?defaults:jo.getString(key);
		}catch(Exception e){
			
		}
		return defaults;
	}
	

}
