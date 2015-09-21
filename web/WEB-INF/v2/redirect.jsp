<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@include file="/common/tags.jsp"%>
<body onload="PAForm.submit();" oncontextmenu="return false">
  <form 
      name="PAForm"
      action="${PaRes.action}"
      method="post">

    <c:forEach items="${PaRes}" var="map">
      <input type=hidden name="${map.key}" value="${map.value}"/>
    </c:forEach>
  </form>
  <script language="javascript">
    window.status="redirect to secure https site...";
    window.focus();
  </script>
</body>
