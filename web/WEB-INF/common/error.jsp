<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@include file="/common/tags.jsp"%>
<c:set value="conf/event" var="event" scope="application"/>
<c:import url="/common/style.jsp" scope="application"/>
<c:if test="${sessionScope.e_lang!='CN'&&sessionScope.e_lang!=null}">
  <fmt:setLocale value="zh_US"/>
  <c:set value="en" var="locals" scope="session"/>
</c:if>

<body>

<%@ include file="/common/head.jsp"%>


<div class="InContent">
	<div class="boxContent">

        <div class="errorContent">
            <div class="dialogTipsArea">
                <span class="TipIco_error"></span>
                <span class="TipText">
                	   	<fmt:message key="error_msg" bundle="${lang}"/>:
      					<span><c:choose>
			              <c:when test="${we.eventid-1>0}">
			               ${we.eventmsg}
			              </c:when>
			              <c:otherwise>
			                ${we.eventid}
			              </c:otherwise>
			            </c:choose>   
			            <fmt:message key="error_id" bundle="${lang}"/>!
			            <c:choose>
			              <c:when test="${we.eventid-1>
			                0}">${we.eventid}
			              </c:when>
			              <c:otherwise>
			                800000
			              </c:otherwise>
			            </c:choose>       
               		 </span>
                		 <c:if test="${fn:substring(we.eventid,0,1)==3}">
				        	<p class="pdleft8 blue" style="cursor:pointer"><a onclick="javascript:history.back()">点击此处返回</a></p>
				        </c:if>
				        <c:if test="${fn:substring(we.eventid,0,1)!=3}">
				        	<p class="pdleft8 blue" style="cursor:pointer"><a onclick="javascript:window.close()">点击此处关闭</a></p>
				        </c:if>	
				     </span>   
                <div style="clear:both"></div>
            </div>


            <div class="helpQA">
                <div class="help_question">付款没有成功？</div>
                <div class="help_answer">请到网上银行查看您的银行卡有没有扣款。去» 银行咨询电话：</div>

                <div class="help_question">银行已扣款，但没有提示成功？</div>
                <div class="help_answer">由于网络传输问题造成，请点击交易结果查询，我们为您恢复此交易。</div>

                <div class="help_question">银行没有扣款？</div>
                <div class="help_answer">建议您重新支付一次试试看，如果仍无法成功,<br>
            请咨询服务:400-022-6816</div>
            </div>
        </div>

     
    </div>




</div>

<%@ include file="/common/tail.jsp"%>

</body>

</html>
