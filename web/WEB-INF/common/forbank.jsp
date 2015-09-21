<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@include file="/common/tags.jsp"%>
<%@ include file="/common/style.jsp"%>
<fmt:setLocale value="zh_${page.locale}"/>
<html>
<head>
<style>
body{background:#f0f0f0}
</style>
</head>

<script LANGUAGE="JavaScript">
  window.status="<fmt:message key="communication_bank" bundle="${lang}"/>";
  window.focus();
</script>

<body>
<div class="queren">
${page.form}
    	<div class="quern_yes" style="margin-top:80px">
    	  <p><span class="qunrenzit red"><!-- <strong><span class="blue">${page.bank}</span></strong>  -->银行页面加载中，请稍候...</p>
          <p><img src="images/img/loading.gif" width="280" height="13" /></p>
          <p>如果系统长时间停止响应<strong><span class="red"><a href="javascript:window.close();">单击此处</a></span></strong>关闭本窗口重新选择银行支付</p>
        </div>
        <div class="quern_link2">了解更多吉高相关产品请登陆吉高官网：www.gicard.net</div>
</div>
</body>
</html>
