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
    	  <p><h1><strong><span class="blue">${page.bank}</span></strong>ҳ������У����Ժ�...</h1></p>
    	  <p><img src="images/img/loading.gif" width="280" height="13" /></p> 

          <p>���ϵͳ��ʱ��ֹͣ��Ӧ<strong><span class="red">
  <a href="javascript:window.close();">�����˴�</a>
  </span></strong>�رձ���������ѡ������֧��</p>
        </div>
         <div class="quern_link">�˽���༪����ز�Ʒ���½����������www.xxxxxxx.com.cn</div>
</div>
</body>
</html>
