<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@include file="/common/tags.jsp"%>
<c:set value="conf/event" var="event" scope="application"/>
<c:import url="/common/style.jsp" scope="application"/>
<c:if test="${sessionScope.e_lang!='CN'&&sessionScope.e_lang!=null}">
  <fmt:setLocale value="zh_US"/>
  <c:set value="en" var="locals" scope="session"/>
</c:if>
<body>

  <div class="main">
    <%@ include file="/common/head.jsp"%>
    <div class="content">

      <h1>
        错误信息
      </h1>

      <div class="content_t">

        <img src="images/jingshi.gif" width="89" height="89" class="content_t_img" />

        <h1>
          <fmt:message key="error_msg" bundle="${lang}"/>
          :
          <strong class="red">
            <c:choose>
              <c:when test="${we.eventid-1>0}">
               ${we.eventmsg}
              </c:when>
              <c:otherwise>
                ${we.eventid}
              </c:otherwise>
            </c:choose>
          </strong>
        </h1>

        <h2>
          <fmt:message key="error_id" bundle="${lang}"/>:
          <strong class="red">
            <c:choose>
              <c:when test="${we.eventid-1>
                0}">${we.eventid}
              </c:when>
              <c:otherwise>
                800000
              </c:otherwise>
            </c:choose>
          </strong>
        </h2>

      </div>

    </div>

    <%@ include file="/common/tail.jsp"%>

  </div>

</body>

</html>
