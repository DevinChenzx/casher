<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common/style.jsp"%>
<%@ include file="/common/tags.jsp"%>
<SCRIPT language=JavaScript type=text/javascript>
  function check1(obj,menu) {
    if (obj.style.display=='none')

  {obj.style.display='block';menu.src="images/Arrow_08.gif";}

  else

{obj.style.display='none';menu.src="images/Arrow_07.gif";}
}

function check(obj) {if (obj.style.display=='none') {obj.style.display='block'}else{obj.style.display='none'}}</SCRIPT>

<body>
<div class="main">
<%@ include file="/common/head.jsp"%>
<div class="syt_content">
<div class="tubxx">您正在使用即时到账交易</div>
<div class="tu_jingdu"><img src="images/olind_1.gif" width="948" height="29" /></div>
<div class="syt_content">

<h1>
<span class="left" style="margin-left:20px;">订单名称</span>
<span class="left" style="margin-left:300px;">收款方</span>
<span class="right" style="margin-right:100px;">订单金额</span>
</h1>
<h2>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="42%"><span class="left" style="margin-left:40px;">${m._sorder.subject}
    <span class="syt_hs">详情<A onclick=check1(t1,mt1) href="javascript:"><IMG src="images/Arrow_07.gif" name="mt1" border=0 id=mt1></A>
    </span></td>
    <td width="42%">${m._sorder.seller_name} ${m._sorder.seller_remarks}</td>
    <td width="16%"><fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/> 元</td>
  </tr>
</table>
</h2>
<h4 id=t1 style="display:none">

<p style="padding:10px;">交易号：${m._sorder.id}   商品名称(数量)：${m._sorder.subject}(${m._sorder.quantity}) 订单号：${m._sorder.ordernum} 商品描述：${m._sorder.bodys} <br />

交易金额：<fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/>元  购买时间：<fmt:formatDate value="${m._sorder.createdate}" pattern="yyyyMMdd HH:mm:ss" /> </p>

</h4>

</div>

<div class="syt_zck">

<div class="sty_lbt">

<h1>我有吉卡账户,轻松付款:</h1>

<ul>
<form id="fastpayLoginForm" method="post" action="/InCashier.do?action=login">
<input type="hidden" name="_token" value="${m._token}"/>
<input type="hidden" name="_persistence" value="${m._persistence}"/>
<li><strong style="text-align:right;">吉卡账户名:</strong></li>

<li style="width:190px;"><input name="buyerUserName" class="sty_input" type="text" /></li>

<li><a href="#">忘记账户名？</a></li>

<li><strong style="text-align:right;">支付密码:</strong></li>

<li style="width:190px;"><input name="password" class="sty_input" type="password" /></li>

<li><a href="#">找回支付密码</a></li>

<li class="sty_xlwz" style="width:330px; ">请输入账户的支付密码，<span class="syt_hs">不是登录密码</span>。</li>

</ul>

<div class="anniu"> <label for="" class="left"><font color=red>&nbsp;${m.LEFT_RESP_MSG}</font></label>
<span class="next_anniu" style="margin-left:200px;"><a href="javascript:document.forms[0].submit();">下一步</a></span>
</div>

</div>
</form>
<div class="sty_lbt" style="margin-bottom:60px; margin-left:3px;">

<h1>我没有吉卡账户，也能付款:</h1>

<h2>吉卡支持国内二十多家主流银行与机构的储蓄卡、信用卡的网上付款。

点击下一步，选择银行，完成付款。</h2>
<form method="post" action="/InCashier.do?action=quickLogin" id="fastpayDirectForm">
<input type="hidden" name="_token" value="${m._token}"/>
<input type="hidden" name="_persistence" value="${m._persistence}"/>
<ul style="margin-top:10px; height:80px">

<li><strong style="text-align:right;">邮箱或手机号:</strong></li>

<li style="width:230px;"><input type="text" name="buyerUserEmail" style="width:230px;" class="sty_input" value="本次付款凭证将发送到该邮箱或手机中" onclick="clearJS(this)" /></li>

<li class="sty_xlwz" style="width:290px; ">请正确填写，以便接收付款凭证。</li>

</ul>

<div class="anniu">
<label for="" class="left"><font color=red>&nbsp;${m.RIGHT_RESP_MSG}</font></label>
<span class="next_anniu" style="margin-left:200px"><a href="javascript:document.forms[1].submit();">下一步</a></span>

</div>

</div></form>

</div>
<%@ include file="/common/tail.jsp"%>
</div>
</body>
<script language="javascript">
function clearJS(obj)
{
if(obj.value == "本次付款凭证将发送到该邮箱或手机中")
{
obj.value = "";
obj.style.color = "#000000";
}
}
</script>
</html>
