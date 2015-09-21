<%@ page contentType="text/html; charset=utf-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>吉卡收银台</title>
<link href="${base}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${base}/css/shouyintai.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="/js/jquery/jquery-1.4.4.js"></script>
<script language="javascript" src="/js/jquery/jquery.validate.min.js"></script>
<script language="javascript" src="/js/jquery/jquery.divbox.js"></script>

<%@ include file="/common/tags.jsp"%>
<style>
#divSCA
{
    position: absolute;
    width: 350px;
    height: 280px;
    font-size: 12px;
    background: #fff;
    border: 0px solid #000;
    z-index: 10001;
    display: none;
}

.reda,.reda a{color:#c00!important;text-decoration:none;}
.reda a:hover{color:#c00!important;text-decoration:none;}
a{outline-style:none;color:#333;text-decoration:none}
a:hover{color:#c00;text-decoration:none;}
</style>
<script type="text/javascript" language="javascript">
  //<!CDATA[
	$(document).ready(function(){
	    var bheight=document.body.clientHeight;
	    function layer(){
	 $("#brg").css("display","block");
	 $("#showdiv").css("display","block");
	 };
	layer();
	 $("#over").click(function(){
	    $("#brg").css("display","none");
	 $("#showdiv").css("display","none");
	 });
	 });  
function g(o){return document.getElementById(o);}

function HoverLi(n){

  //如果有N个标签,就将i<=N;

  //本功能非常OK,兼容IE7,FF,IE6

  for(var i=1;i<=2;i++){g('tb_'+i).className='normaltab';g('tbc_0'+i).className='undis';}g('tbc_0'+n).className='dis';g('tb_'+n).className='hovertab';

}

//如果要做成点击后再转到请将<li>中的onmouseover 改成 onclick;

//]]>
</script>

<SCRIPT language=JavaScript type=text/javascript>
	function check1(obj,menu) {
	if (obj.style.display=='none')
	
	 {obj.style.display='block';menu.src="images/Arrow_08.gif";}
	 else
	 {obj.style.display='none';menu.src="images/Arrow_07.gif";}
	}
	function setBankName(bankname){
	    var allDivs = document.getElementsByTagName("div");
		for (var i = 0; i < allDivs.length; ++i)
		{
			var div = allDivs[i];
			if (div.getAttribute("ctrlshow") == "true")
			{
				div.style.display="none";
				if(div.id==bankname){
				 	div.style.display="";
			    }
			}				
		} 
	}
	function check(obj) {if (obj.style.display=='none') {obj.style.display='block'}else{obj.style.display='none'}}
	
	function trim(s)
	{
		return s.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	var bflag = "1";
	//firefox兼容键盘事件脚本
	document.onkeydown=function(event){
		e=event?event:(window.event?window.event:null); 
		if(e.keyCode==13){  
			//执行的方法
			if(bflag=="0"){
				doConfirm();
			}
		}
	}
	
    function doConfirm()
    {
    	var flag=0;
    	var merNo="";
    	var corpName="";
    	var financeContact="";
    	var contactPhone="";
    	
    	var pattern2 = /^[0-9]*$/;
    	//固定电话+手机号
    	var patternPhone = /^(^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\(\d{3}\))|(\d{3}\-))?(1[358]\d{9})$)$/;

    	if(${m.acquireCode=='BOCM_B2B'})
    		merNo = document.getElementById("merchantNo").value;
    	if(${m._paytype=='9' && m.terminal=='999999'})
    	{
    		corpName = document.getElementById("corpName").value;
        	financeContact = document.getElementById("financeContact").value;
        	contactPhone = document.getElementById("contactPhone").value;
    	}
    	if(${m.acquireCode=='BOCM_B2B' && m.terminal=='000000'})
    	{
    		if(trim(merNo)==""){
    			flag=1;
    			alert("付款账号为必填项，请填写完整!");
    		}else{
    			if(!pattern2.test(trim(merNo))){
    				flag=1;
        			alert("付款账号只能输入数字!");
    			}
    		}
    	}
    	if(${m.acquireCode=='BOCM_B2B' && m.terminal=='999999'})
    	{
    		if(trim(merNo)==""){
    			flag=1;
    			alert("付款账号为必填项，请填写完整!");
    		}else{
    			if(!pattern2.test(trim(merNo))){
    				flag=1;
        			alert("付款账号只能输入数字!");
    			}
    		}
    		if(trim(corpName)==""){
    			flag=1;
    			alert("企业名称为必填项，请填写完整!");
    		}else{
    			if(!AntiSqlValid($("#corpName"))){
    				flag=1;
    				alert("请您不要在文本框中输入特殊字符和SQL关键字！");
    			}
    		}
    		if(trim(financeContact)==""){
    			flag=1;
    			alert("财务联系人为必填项，请填写完整!");
    		}else{
    			if(!AntiSqlValid($("#financeContact"))){
    				flag=1;
    				alert("请您不要在文本框中输入特殊字符和SQL关键字！");
    			}
    		}
    		if(trim(contactPhone)==""){
    			flag=1;
    			alert("联系电话为必填项，请填写完整!");
    		}else{
    			if(!patternPhone.test(trim(contactPhone))){
    				flag=1;
    				alert("联系电话格式有误，请重新填写!如：010-53257458、13566668888");
    			}
    		}
    	}
    	if(${m.acquireCode!='BOCM_B2B' && m.terminal=='999999'})
    	{
    		if(trim(corpName)==""){
    			flag=1;
    			alert("企业名称为必填项，请填写完整!");
    		}else{
    			if(!AntiSqlValid($("#corpName"))){
    				flag=1;
    				alert("请您不要在文本框中输入特殊字符和SQL关键字！");
    			}
    		}
    		if(trim(financeContact)==""){
    			flag=1;
    			alert("财务联系人为必填项，请填写完整!");
    		}else{
    			if(!AntiSqlValid($("#financeContact"))){
    				flag=1;
    				alert("请您不要在文本框中输入特殊字符和SQL关键字！");
    			}
    		}
    		if(trim(contactPhone)==""){
    			flag=1;
    			alert("联系电话为必填项，请填写完整!");
    		}else{
    			if(!patternPhone.test(trim(contactPhone))){
    				flag=1;
    				alert("联系电话格式有误，请重新填写!如：010-53257458、13566668888");
    			}
    		}
    	}
    	if(flag==0){
    		if(${m.acquireCode=='BOCM_B2B'}){
        		window.parent.document.getElementById("cmbMerNo").value=trim(merNo);
        		window.parent.document.getElementById("merchantjson").value=trim(merNo);
        	}
    		if(${m._paytype=='9' && m.terminal=='999999'})
    		{
    			window.parent.document.getElementById("gCorpName").value=trim(corpName);
    	    	window.parent.document.getElementById("gFinanceContact").value=trim(financeContact);
    	    	window.parent.document.getElementById("gContactPhone").value=trim(contactPhone);
    		}
			if(!confirm("确定要提交吗？")) return false;
			window.parent.document.forms[0].submit();
			closeDiv();
    	}
    }
    
  //防止SQL注入
    function AntiSqlValid(oField)
    {
        var re= /select|update|delete|exec|count|drop|create|'|"|=|;|>|<|%/i;
        if(!re.test(oField.val().toLowerCase())) return true;
        if(re.test(oField.val().toLowerCase()))
        {
    	    //alert("请您不要在参数中输入特殊字符和SQL关键字！"); //注意中文乱 码
    	    oField.val("");
    	    //oField.className="errInfo";
    	    //oField.focus();
    	    return false;
        } 
    }
    
    function rePay()
    {
    	document.forms[0].target="_self";
    	document.forms[0].action="/Pay?_id=${m._id}&_paytype=${m._paytype}&_rePay=y";
    	document.forms[0].submit();
    }
    
	function openDiv() {
		$("#merchantNo").val("");
		$("#corpName").val("");
    	$("#financeContact").val("");
    	$("#contactPhone").val("");
    	bflag = "0";
		$("#divSCA").OpenDiv();
	}

	function closeDiv() {
		$("#merchantNo").val("");
		$("#corpName").val("");
    	$("#financeContact").val("");
    	$("#contactPhone").val("");
    	bflag = "1";
		$("#divSCA").CloseDiv();
	}
	
	function closeShowDiv()
	{
		$("#brg").css("display","none");
		$("#showdiv").css("display","none");
	}
	
	var flag = "0";
	function doQueryResult()
	{
		var url = "<%=request.getContextPath()%>/queryResult.do";
		$.post(url,{
			_id:"${m._id}",
			_paytype:"${m._paytype}"
		},function (data, textStatus){
			if(textStatus=="success"){
				if(data=="success"){
					if(flag=="0"){
						document.getElementById("queryResult").href="<%=request.getContextPath()%>/exportResult.do?_id=${m._id}&_paytype=${m._paytype}";
						document.getElementById("queryResult").click();
						flag = "1";
					}
				}
				if(data=="failure"){
					alert("订单尚未支付，请先支付。");
					return false;
				}
			}
	});
	}
    
 $(document).ready(function(){
	  $("#xiangqing").click(function(){
	  $("#qipao").toggle();
	  });
	  $("#guanbi").click(function(){
	  $("#qipao").fadeOut("slow");
  });
});
$(function(){
	$(".tab dl dt>a:eq(0)").addClass("tabActive");
	$(".tab dl dd ul").not(":eq(0)").hide();
	$(".tab dl dt>a").unbind("click").bind("click", function(){
		$(this).siblings("a").removeClass("tabActive").end().addClass("tabActive");
		var index = $(".tab dl dt>a").index( $(this) );
		$(".tab dl dd ul").eq(index).siblings(".tab dl dd ul").hide().end().fadeIn("slow");
   });
});
</SCRIPT>


<body onload="setBankName('${m._channel.acquire_code}');">

<%@ include file="/common/head.jsp"%>
<div class="process">
  <div class="welcome">
    <p class="txtLeft pdleft">您好,吉卡用户 ${m._sorder.buyer_name}</p>
  </div>
  <div class="process2"></div>
</div>
<%@ include file="orderinfo.jsp"%>

<form name="ebankPayForm" id="ebankPayForm" method="POST" action="/Ebank" target="_blank" onClick="javascript:closeShowDiv();">
	     <input type="hidden" name="_channelToken" value="${m._channelToken}"/>
	     <input type="hidden" name="_persistence"  value="${m._persistence}"/>
         <input type="hidden" name="_orderId"      value="${m._sorder.ordernum}"/> 
         <input type="hidden" name="_id"      value="${m._id}"/>
         
         <input type="hidden" name="feeAmount"      value="${m.feeAmount}"/>
         <input type="hidden" name="actualAmount"      value="${m.actualAmount}"/>
         <input type="hidden" name="feeSign"      value="${m.feeSign}"/>
         <input type="hidden" name="feeType"      value="${m.feeType}"/>
         <input type="hidden" name="feeRate"      value="${m.feeRate}"/>
         <input type="hidden" name="isRefundFee"      value="${m.isRefundFee}"/>
		
		 <input type="hidden" name="acquireCode" value="${m.acquireCode}"/>
		 <input type="hidden" id="cmbMerNo" name="cmbMerNo" value=""/>
		 <input type="hidden" id="gCorpName" name="gCorpName" value=""/>
		 <input type="hidden" id="gFinanceContact" name="gFinanceContact" value=""/>
		 <input type="hidden" id="gContactPhone" name="gContactPhone" value=""/>
		 <input type="hidden" id="paytype" name="paytype" value="${m._paytype}"/>
		 <input type="hidden" id="terminal" name="terminal" value="${m.terminal}"/>
		 <input type="hidden" id="merchantjson" name="merchantjson" value=""/>
		 <div id="brg"></div>
		 <div id="showdiv">
		   <div id="close"></div>
		   <div id="testdiv">
		   		<div id="over"><a href="#"></a></div>
		   			<div class="ddlb">
				        <div class="neword_bt">
				          <p class=" txtRight">订单金额： </p>
				        </div>
				        <div class="neword_cnt">
				          <p class=" txtLeft pdleft8">
				          	<span class="left pdleft8"><a class="reda b"><fmt:formatNumber value="${(m._sorder.amount-m._directPayAmt)/100}" pattern="0.00"/> 元 </a></span> 
				          </p>
				        </div>
				        <c:if test="${m.feeAmount!=0 && m.feeAmount!=null}">
				        <div class="neword_bt">
				          <p class=" txtRight"><font style="color:red">手续费金额： </font></p>
				        </div>
				        <div class="neword_cnt">
				          <p class=" txtLeft pdleft8">
				          	<span class="left pdleft8"><a class="reda b"><fmt:formatNumber value="${m.feeAmount}" pattern="0.00"/> 元</a></span> 
				            <span class="left pdleft8 txtRight"><font style="color:red">实际支付金额： </font></span>
				          	<span class="left pdleft8"><a class="reda b"><fmt:formatNumber value="${m.actualAmount}" pattern="0.00"/> 元 </a></span> 
				          </p>
				        </div>
				        </c:if>
				       
				        <div class="neword_bt">
				          <p class="font14">付款方式： </p>
				        </div>
				        <div class="neword_cnt txtLeft pdleft8">
				        <c:if test="${m._paytype=='8'}">
				        <label> <a href="#"><img src="images/bank/${m._bankname}.gif" width="52" height="51" border="0"></a> </label>
				        </c:if>
				        <c:if test="${m._paytype=='0'||m._paytype=='9'}">
				          <label> <a href="#"><img src="images/bank_v3/${m._bankname}_OUT.png" width="127" height="40" border="0" ></a> </label>
				          <c:if test="${m._directPayAmt>0}">
							<input type="hidden" name="_compositemode" value="1"/>
							<p class="syt_hs_yw txtLeft" >吉卡账户余额支付  支付金额<fmt:formatNumber value="${m._directPayAmt/100}" pattern="0.00"/> 元  </p>
						  </c:if>
				        </c:if>
				        </div>
						
				        <div class="neword_cnt">
				          <p class=" txtCenter pdleft8">
				          <input type="<c:if test="${(m.acquireCode=='BOCM_B2B') || (m.terminal=='999999' && m._paytype=='9')}">button</c:if><c:if test="${m.acquireCode!='BOCM_B2B' && m.terminal=='000000'}">submit</c:if>" onclick="<c:if test="${(m.acquireCode=='BOCM_B2B') || (m.terminal=='999999' && m._paytype=='9')}">javascript:openDiv()</c:if>" name="button" id="button" value="登录<c:if test="${m._paytype=='8'}">卡系统</c:if><c:if test="${m._paytype=='0'||m._paytype=='9'}">网上银行</c:if>付款" class="zf_button"/>
				        </div>
				  </div>
				</div>
		  </div>
		<div class="smtcnt">
				<div class="frm">
						<p class="txtLeft font14 b" >以下上本次交易详情：</p>
						<br />
						<table width="100%" border="0" cellspacing="0" cellpadding="0" class="txtLeft">
								<tr>
									<td width="31%" scope="col"><strong>订单号： </strong><br /></font></td>
									<td width="69%" scope="col"><strong><a href="#" onClick="javascript:doQueryResult();">${m._sorder.ordernum}</a></strong></td>
								</tr>
								<tr>
									<td width="31%" scope="col">交易号 ：<br /></td>
									<td width="69%" scope="col">${m._sorder.id}</td>
								</tr>
								<tr>
									<td>商品名称(数量)：</td>
									<td>${m._sorder.quantity}</td>
								</tr>
								<tr>
									<td>交易金额（元）：</td>
									<td><fmt:formatNumber value="${(m._sorder.amount-m._directPayAmt)/100}" pattern="0.00"/></td>
								</tr>
								<tr>
									<td>购买时间：</td>
									<td>${m._sorder.orderdate}</td>
								</tr>
								<tr>
									<td>商品描述：</td>
									<td>${m._sorder.subject}</td>
								</tr>
						</table>
						<p style="padding-top:15px;" class="txtLeft">
								<input type="button" name="button" id="button" value="重新支付" class="rest_button" onclick="javascript:rePay();"/>
						</p>
				</div>
				<div class="sharp color1"> <b class="b1"></b><b class="b2"></b>
						<div class="content_new txtLeft">
								<h1>付款没有成功？</h1>
								<h2>请到网上银行查看您的银行卡有没有扣款。</h2>
								<h2> 去<a href="${m.bankUrl}" class=" blue" target="_blank">${m.bankName}</a>» 银行咨询电话：${m.bankContact}</h2>
								<h3>• 银行已扣款，但没有提示成功？ </h3>
								<h4>由于网络传输问题造成，请点击<strong><a id="queryResult" href="#" class="blue" onClick="javascript:doQueryResult();">交易结果查询</a></strong>，我们为 </h4>
								<h4>您恢复此交易，或者等待系统第二个工作日自动回复</h4>
								<h3>• 银行没有扣款？ </h3>
								<h4>建议您重新支付一次试试看。 </h4>
						</div>
						<b class="b7"></b><b class="b8"></b> 
				</div>
		</div>	 
<div id="divSCA">
<div class="loadingmsgbox">
	<table border="0" width="100%" height="60">
	<tr>
	  <td align="left" width="50%" colspan="2">
	  <img src="images/bank_v3/${m._bankname}_OUT.png"/>
	  </td>
	</tr>
	<c:if test="${m.acquireCode=='BOCM_B2B'}">
	<tr>
	  <td align="right" width="40%" height="35">付款账号：</td>
	  <td align="left" width="60%" height="35"><input type="text" id="merchantNo" name="merchantNo" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" value=""/></td>
	</tr>
	</c:if>
	<c:if test="${m._paytype=='9' && m.terminal=='999999'}">
	<tr>
	  <td align="right" width="40%" height="35">企业名称：</td>
	  <td align="left" width="60%" height="35"><input type="text" id="corpName" name="corpName" onkeyup="value=value.replace(/[^\一-\龥^a-z^A-Z^0-9]/g, '')" maxlength="20" value=""/></td>
	</tr>
	<tr>
	  <td align="right" width="40%" height="35">财务联系人：</td>
	  <td align="left" width="60%" height="35"><input type="text" id="financeContact" name="financeContact" onkeyup="value=value.replace(/[^\一-\龥^a-z^A-Z]/g, '')" maxlength="5" value=""/></td>
	</tr>
	<tr>
	  <td align="right" width="40%" height="35">联系电话：</td>
	  <td align="left" width="60%" height="35"><input type="text" id="contactPhone" name="contactPhone" maxlength="20" value=""/></td>
	</tr>
	<br/><br/>
	<tr>
	  <td align="center" width="100%" colspan="2"><strong>注：退款时吉卡将通过您的预留信息核实您的相关信息，为保证贵司的资金安全及退款的顺畅，请您务必准确填写。</strong></td>
	</tr>
	</c:if>
	<tr>
	  <td colspan="2" align="center" height="45">
	    <span><input type="button" onclick="javascript:doConfirm()" value="提交"></span>
	    <span><input type="button" onclick="javascript:closeDiv()" value="关闭"></span>
	  </td>
	</tr>
	
	</table>
	
</div>
</div>
</form>

</div>
<%@ include file="/common/tail.jsp"%>
</body>
</html>
