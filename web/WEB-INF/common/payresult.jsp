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
<div class="enrrybox">
	<div class="enrry">
    	<div class="enrry_img">
    	<c:if test="${res.trx.trxsts=='1'}">
          <img src="/images/img/ok.gif" width="32" height="32" />
        </c:if>
        <c:if test="${res.trx.trxsts=='-1'}">
          <img src="/images/img/enrry.gif" width="32" height="32" />
        </c:if>
    	</div>
      <div class="enrry_txt txtLeft">
      <h1>
          您本次交易已完成！
      </h1>
      <h2>
          交易金额：
          <fmt:formatNumber value="${res.trx.amount/100.00}" pattern="0.00"/>
          元 交易结果：
          <c:if test="${res.trx.trxsts=='1'}">
            充值成功
          </c:if>
          <c:if test="${res.trx.trxsts=='-1'}">
            充值失败
          </c:if>
          <c:if test="${res.order.ordersts=='3'||res.order.ordersts=='1'}">
            支付成功
          </c:if>
          <c:if test="${res.order.ordersts=='4'}">
            支付失败
          </c:if>
          <br />
	      <c:if test="${!(res.order==null||res.order.ordernum=='')}">
	          商家订单号：${res.order.ordernum}
	      </c:if>
          <br />

          银行交易号：${res.trx.trxnum}
          <br />

        </h2>
      <p class="pdleft8 blue"><a href="/Pay?_id=${m._id}">点击此处返回</a></p>
      </div>
    </div>
</div>
<%@ include file="/common/tail.jsp"%>

</body>

</html>
