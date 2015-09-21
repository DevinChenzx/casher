<%@ page contentType="text/html; charset=utf-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>吉卡收银台</title>
<link href="${base}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${base}/css/shouyintai.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="/js/jquery/jquery-1.4.4.js"></script>
<script language="javascript" src="/js/jquery/jquery.validate.min.js"></script>
<%@ include file="/common/tags.jsp"%>
<script type="text/javascript" language="javascript">
  //<!CDATA[

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
	  if (obj.style.display=='none'){obj.style.display='block';menu.src="images/Arrow_08.gif";}
	  else
	 {obj.style.display='none';menu.src="images/Arrow_07.gif";}
	}
	
	function check(obj) {if (obj.style.display=='none') {obj.style.display='block'}else{obj.style.display='none'}}
	function setBankName(bankname){
	    document.forms["cashierfm"]._bankname.value=bankname;
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
	function swithpaymode(){	    
	    with(document.forms["cashierfm"]){
			if(_compositemode.value=="0"){
				_compositemode.value="1";
			}else{
				_compositemode.value="0";
			}
		}		
	}
	
	function countDown()
	{
		 var djs = document.getElementById("daojishi");
		 var notice = document.getElementById("notice");
	     if(djs.innerHTML == 0){
	    	  document.getElementById("sendSMS").setAttribute("href","javascript:sendSMS();");
	    	  notice.style.display="none";
	          return false;
	     }
	     djs.innerHTML = djs.innerHTML - 1;
	}
	
	function sendSMS()
	{
		var notice = document.getElementById("notice");
		var djs = document.getElementById("daojishi");
		var buyer_name=document.getElementById("_buyer_name").value;
		var _orderId=document.getElementById("_orderId").value;
		var _persistence=document.getElementById("_persistence").value;
		
		var url = "<%=request.getContextPath()%>"+"/SendSMS";
		
		notice.style.display="";
		djs.innerHTML=60;
		document.getElementById("sendSMS").removeAttribute("href");
		$.post(url,{
				buyer_name:buyer_name,
				_orderId:_orderId,
				_persistence:_persistence
			},function (data, textStatus){
				if(textStatus=="success"){
					if(data=="success"){
						countDown();
						window.setInterval("countDown()", 1000);
						alert("手机验证码发送成功，请查收。");
					}
					else if(data=="failure"){
						notice.style.display="none";
						document.getElementById("sendSMS").setAttribute("href","javascript:sendSMS();");
						alert("手机验证码发送失败，请重新点击发送。");
					}
					else if(data=="failureMobile"){
						notice.style.display="none";
						document.getElementById("sendSMS").setAttribute("href","javascript:sendSMS();");
						alert("商户未绑定手机号，请先绑定手机号。");
					}
					else if(data=="failureUser"){
						notice.style.display="none";
						document.getElementById("sendSMS").setAttribute("href","javascript:sendSMS();");
						alert("请检查商户状态。");
					}
				}
				return false;
		});
	}
	
	function inputMobile()
	{
		var obj = document.getElementById("mverify_code");
		if(obj.value!=""){
			document.forms["cashierfm"]._compositemobile.value=obj.value;
		} else{
			alert("请输入手机验证码");
			return false;
		}
	}
	
$(document).ready(function(){
  $("#xiangqing").click(function(){
  $("#qipao").toggle();
  });
  $("#guanbi").click(function(){
  $("#qipao").fadeOut("slow");
  });
});

   
/*选项卡*/
$(function(){
	$(".tab dl dt>a:eq(0)").addClass("tabActive");
	$(".tab dl dd ul").not(":eq(0)").hide();
	$(".tab dl dt>a").unbind("click").bind("click", function(){
		$(this).siblings("a").removeClass("tabActive").end().addClass("tabActive");
		var index = $(".tab dl dt>a").index( $(this) );
		$(".tab dl dd ul").eq(index).siblings(".tab dl dd ul").hide().end().fadeIn("slow");
   });
});

/*银行卡卡控制*/
$ (document).ready(function(){
	$("#linebank li").not(".Selected").addClass("hidden"); 
	});

$(document).ready(function(){
  $("#back").click(function(){
  $("#linebank li").toggle(200,function(){
	  document.getElementById("bb").style.display="";
	  });
  });
});

/*一行列表样式*/
$(document).ready(function(){
  $("#ckxe").click(function(){
  $("#xe").toggle();
  });
});
</SCRIPT>



<body>

<%@ include file="/common/head.jsp"%>
<div class="process">
  <div class="welcome">
    <p class="txtLeft pdleft">您好吉卡用户 ${m._sorder.buyer_name}</p>
  </div>
  <div class="process2"></div>
</div>
<div class="zhifu">
	<table class="zhifutab">
	    <tr align="center">
	      <td><font size="3"><b>订单名称 </b></font></td>
	      <td><font size="3"><b>收款方</b></font></td>
	      <td class="red"><font size="3"><b>订单金额</b></font></td>
	    </tr>
	    <tr>
	      <td><div id="dwn"><c:choose><c:when test="${fn:length(m._sorder.subject)>15}">${fn:substring(m._sorder.subject,0,15)}...</c:when><c:otherwise>${m._sorder.subject}</c:otherwise></c:choose><a onclick="check1(t1,mt1)" id="" class="blue">详情<img src="images/Arrow_07.gif" name="mt1" border=0 id=mt1/></a></div></td>
	      <td>${m._sorder.seller_name} ${m._sorder.seller_remarks}</td>
	      <td class="red"><b><fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/></b> 元</td>
	    </tr>
	    <tr>
	    <tr id="t1" style="display:none" align="center">
	      <td colspan="3" scope="col" >
	      	<p style="line-height:25px; padding-left:30px;" class="txtLeft">
			交易号：${m._sorder.id}&nbsp;&nbsp;&nbsp;&nbsp;商品名称(数量)：<c:choose><c:when test="${fn:length(m._sorder.subject)>15}">${fn:substring(m._sorder.subject,0,15)}...</c:when><c:otherwise>${m._sorder.subject}</c:otherwise></c:choose>
			&nbsp;&nbsp;&nbsp;&nbsp;订单号：${m._sorder.ordernum}
			<c:if test="${m._sorder.quantity>1}">(${m._sorder.quantity})</c:if> 
			&nbsp;&nbsp;&nbsp;&nbsp;商品描述：<c:choose><c:when test="${fn:length(m._sorder.bodys)>50}">${fn:substring(m._sorder.bodys,0,50)}...</c:when><c:otherwise>${m._sorder.bodys}</c:otherwise></c:choose> 
			<br/></p>
			<p style="line-height:25px; padding-left:30px;" class="txtLeft">
			交易金额：<fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/>元  &nbsp;&nbsp;&nbsp;&nbsp;购买时间：<fmt:formatDate value="${m._sorder.createdate}" pattern="yyyyMMdd HH:mm:ss"/>
	        </p>
	      </td>
	    </tr>
	</table>
	
	<p ></p>
	<p style="line-height:25px;padding-left:30px;" class="txtLeft">
         您的吉卡账户：<strong>${m._sorder.buyer_name}</strong><br/>
       <p style="line-height:25px;padding-left:30px;" class="txtLeft">
        <p style="line-height:25px;padding-left:30px;" class="txtLeft">
	        <c:if test="${m._buyer_balance==null||m._buyer_balance==0}">无法使用账户余额付款,请使用其他方式付款。</c:if>
	        <c:if test="${m._buyer_balance>0}">吉卡账户可支付余额: <fmt:formatNumber value="${m._buyer_balance/100}" pattern="0.00"/> 元</c:if>              
        </p>
       </p>              
       <c:if test="${m._buyer_balance>=m._sorder.amount}">                 
        <p style="line-height:25px;padding-left:30px;" class="txtLeft" >
         <form name="acctcashierfm" action="/AccountCashier" method="post">
            <input type="hidden" name="_id" value="${m._id}"/>
            <input type="hidden" id="_buyer_name" name="_buyer_name" value="${m._buyer_name}"/>
            <input type="hidden" name="_persistence" value="${m._persistence}"/>
            <input type="hidden" name="_orderId" value="${m._sorder.ordernum}"/>
            <input type="hidden" name="accttravel" value="${m.istravel}"/>
	         <span>
	          使用吉卡账户支付金额: <fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/>元
	          <c:if test="${m.istravel==''}">    
	          <br/><input id="mverify_code" type="text" name="mverify_code" class="sj_inpt"/>
      	   			    <a href="javascript:sendSMS()" id="sendSMS">获取手机验证码</a> <font style="color:red"><label id="notice" style="display:none"><span id="daojishi">60</span>秒后方可点击重发 </label></font>
	          </c:if>
      	   		<br/><input type="submit" name="button" id="button" value="确认付款" class="zf_button" />    
	         </span>
        </form>
	    </p>
       </c:if>
       <c:if test="${m._buyer_balance<m._sorder.amount&&m._buyer_balance>0&&m._ebankenable!='0'}">
		  <input type="hidden" name="_id" value="${m._id}"/>
          <input type="hidden" id="_buyer_name" name="_buyer_name" value="${m._buyer_name}"/>
          <input type="hidden" id="_persistence" name="_persistence" value="${m._persistence}"/>
          <input type="hidden" id="_orderId" name="_orderId" value="${m._sorder.ordernum}"/>
        <p class="txtLeft"  style="line-height:25px;padding-left:30px;">
         <span class="syt_hs">
             <input type="checkbox" value="0" name="_compositemode" id="_compositemode" onclick="swithpaymode();"/> 使用吉卡账户支付金额：<fmt:formatNumber value="${m._buyer_balance/100}"  pattern="0.00"/> 元,剩余金额 <fmt:formatNumber value="${(m._sorder.amount-m._buyer_balance)/100}" pattern="0.00"/> 元使用其它方式支付    
             <c:if test="${m.istravel==''}"> 
             <br/><input id="mverify_code" type="text" name="mverify_code" onblur="javascript:inputMobile()" class="sj_inpt"/>
     	   			    <a href="javascript:sendSMS()" id="sendSMS">获取手机验证码</a> <font style="color:red"><label id="notice" style="display:none"><span id="daojishi">60</span>秒后方可点击重发 </label></font>
     	     </c:if>
         </span>
        </p>
       </c:if>
     </h3>
	
 </div>
 
 <c:if test="${m._ebankenable!='0'}">
 <div class="tab">
  <dl>
    <div class="choose">
      <div class="fangshi">
        <p class="txtLeft pdleft">您可以选择其他付款方式: </p>
      </div>
      <dt><a>银行卡</a></dt>
    </div>
    <dd>
      <!-- 银行卡 -->
      <ul>
      <div class="dis" id="tbc_01" >                         
         <%@include file="bankpay_direct.jsp" %>
      </div>
      <div class="undis" id="tbc_02">
        <%@include file="ismspay.jsp" %>
      </div>
	  </ul>
    </dd>
  </dl>
</div>
</c:if>
<%@ include file="/common/tail.jsp"%>
</body>
</html>
