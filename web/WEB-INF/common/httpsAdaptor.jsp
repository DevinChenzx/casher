<%@include file="/common/tags.jsp"%>
<!-- <result>0</result> -->
<noscript>
  <br>
  <br>
  <center>
    <h1>
      Processing your Secure Transaction
    </h1>
    <h2>
      JavaScript is currently disabled or is not supported by your browser.
      <br>
    </h2>
    <h3>
      Please click Submit to continue the processing of your transaction.
    </h3> 
  </center>
</noscript>
<c:set var="hosts" value="${pageContext.request.serverName}"/>
<c:if test="${fn:startsWith(hosts,'10.')||fn:startsWith(hosts,'172.')}">
  <c:set var="hosts" value="epay.gicard.net"/>
</c:if>
<body onload="PAForm.submit();" oncontextmenu="return false">
  <form name="PAForm" action="https://${hosts}${request._uri_}" method="post">
    <c:forEach items="${request}" var="map">
      <input type=hidden name="${map.key}" value="${map.value}"/>
    </c:forEach>
  </form>
  <script language="javascript">
    window.status="redirect to secure https site...";
    window.focus();
  </script>
</body>