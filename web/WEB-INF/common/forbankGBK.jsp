<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@include file="/common/tags.jsp"%>
<%@ include file="/common/styleGBK.jsp"%>
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
    	<div class="quern_yes">
    	  <p><h1><strong><span class="blue">${page.bank}</span></strong>页面加载中，请稍候...</h1></p>
    	  <p><img src="images/img/loading.gif" width="280" height="13" /></p> 

          <p>如果系统长时间停止响应<strong><span class="red">
  <a href="javascript:window.close();">单击此处</a>
  </span></strong>关闭本窗口重新选择银行支付</p>
        </div>
         <div class="quern_link">了解更多吉卡相关产品请登陆吉卡官网：www.xxxxxxx.com.cn</div>
</div>
</body>
</html>
