<%@ page contentType="text/html; charset=utf-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>吉卡收银台</title>
<link href="${base}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${base}/css/shouyintai.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="/js/jquery/jquery-1.4.4.js"></script>
<script language="javascript" src="/js/jquery/jquery.validate.min.js"></script>
<script language="javascript" src="/js/jquery/jquery.divbox.js"></script>
</head>
<%@ include file="/common/tags.jsp"%>

<body>
<%@ include file="/common/head.jsp"%>
<div class="process">
	<div class="welcome">
			<p class="txtLeft pdleft">您好吉卡用户 ：${m.buyername}</p>
	</div>
	<div class="process2"></div>
</div>
<form name="faqForm" id="faqForm" method="post" action="/gwFaq.do">
<input type="hidden" name="_id" value="${m.gwid}"/>
<div class="zhifu">
		<table class="smtable">
				<tr>
					<td width="25%" align="center" scope="col">
						<span class="txtLeft" style="padding-top:15px;">
							<input type="button" name="button" id="button" value="重新支付" class="rest_button" onclick="javascript:rePay();"/>
						</span>
					</td>
					<td width="75%" scope="col" class="font14 b txtLeft">${m.reason}。通过下方表单将这个问题提交给我们。</td>
				</tr>
		</table>
</div>
<div class="smtcnt">
	<div class="faqfrm">
			<table class="faqfrmtlb">
					<tr>
							<td height="60" colspan="2" class="txtLeft" scope="col" style="padding-left:45px;">
							<input type="radio" name="radio" id="radio" checked="true" value="y" /><label for="radio">银行已扣款
							<input type="radio" name="radio" id="radio2" value="n" />银行未扣款</label></td>
						</tr>
					<tr>
							<td width="15%" height="50" class="txtRight">联系方式：</td>
							<td width="85%" class="txtLeft"><input type="text" name="contact" id="contact" /></td>
					</tr>
					<tr>
							<td height="80" class="txtRight">问题描述：</td>
							<td class="txtLeft">
								<textarea name="queDesc" id="queDesc" rows="5" cols="50"></textarea>
							</td>
					</tr>
				</table>
	</div>
	<div class="faqfbtn"><p style="padding-left:160px;"><input type="button" name="button" id="button" value="提交问题" onclick="javascript:doSubmit();" class="zf_button" /></p></div>
</div>
</form>
<%@ include file="/common/tail.jsp"%>
</body>
</html>
<script>

	//防止SQL注入
	function AntiSqlValid(oField)
	{
	    var re= /select|update|delete|exec|count|drop|create|'|"|=|;|>|<|%/i;
	    if(!re.test(oField.value.toLowerCase())) return true;
	    if(re.test(oField.value.toLowerCase()))
	    {
		    //alert("请您不要在参数中输入特殊字符和SQL关键字！"); //注意中文乱 码
		    oField.value="";
		    //oField.className="errInfo";
		    //oField.focus();
		    return false;
	    } 
	}

	String.prototype.len=function()
	{
		return this.replace(/[^\x00-\xff]/g,"**").length;
	}
	
	function trim(s)
	{
		return s.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	function doSubmit()
	{
		var patternPhone = /^(^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\(\d{3}\))|(\d{3}\-))?(1[358]\d{9})$)$/;
		var contact = document.getElementById("contact").value;
		var queDesc = document.getElementById("queDesc").value;
		if(trim(contact)==""){
			alert("请填写联系方式");
			return false;
		}else{
			if(!patternPhone.test(trim(contact))){
				alert("联系电话格式有误，请重新填写!如：010-53257458、13566668888");
				return false;
			}
		}
		if(trim(queDesc)==""){
			alert("请填写问题描述,以便更方便的给您解决问题");
			return false;
		}else{
			if(!AntiSqlValid(document.getElementById("queDesc"))){
				alert("请您不要在文本框中输入特殊字符和SQL关键字！");
				return false;
			}
			if(trim(queDesc).len()>200){
				alert("问题描述最多输入200个字符");
				return false;
			}
		}
		document.forms[0].action="/gwFaq.do";
		document.forms[0].submit();
	}

	function rePay()
	{
		document.forms[0].target="_self";
		document.forms[0].action="/Pay?_id=${m.orderid}&_paytype=${m.paytype}&_rePay=y";
		document.forms[0].submit();
	}
	if('${m.msg}'!='')
		alert('${m.msg}');
</script>
