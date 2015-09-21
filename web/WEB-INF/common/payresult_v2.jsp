<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common/style.jsp"%>
<%@ include file="/common/tags.jsp"%>
<body>

  <div class="main">

    <%@ include file="/common/head.jsp"%>

    <div class="content">

      <h1>
        提示类信息
      </h1>

      <div class="content_t">

        <c:if test="${res.trx.trxsts=='1'}">
          <img src="/images/jingshid.gif" width="89" height="89" class="content_t_img" />
        </c:if>
        <c:if test="${res.trx.trxsts=='-1'}">
          <img src="/images/jingshiX.gif" width="89" height="89" class="content_t_img" />
        </c:if>

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

      </div>

    </div>

    <%@ include file="/common/tail.jsp"%>

  </div>

</body>

</html>
