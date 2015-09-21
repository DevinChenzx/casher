<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common/style.jsp"%>
<%@ include file="/common/tags.jsp"%>

<body>
    <Form name=frmToMerchant Action="${res.action}" method="post">
      <c:forEach items="${res.forms}" var="map">
        <input type="hidden" name="${map.key}" value="${map.value}"/>
      </c:forEach>
    </form>
    <div class="main">    

      <div class="content">

        <h1>
          提示类信息
        </h1>

        <div class="content_t">
          <c:if test="${res.result.trx.trxsts=='1'||res.result.order.ordersts=='3'}">
            <img src="images/jingshid.gif" width="89" height="89" class="content_t_img" />
          </c:if>
          <c:if test="${res.result.trx.trxsts=='-1'||res.result.order.ordersts=='4'}">
            <img src="images/jingshiX.gif" width="89" height="89" class="content_t_img" />
          </c:if>
          <h1>
            您本次交易已完成!
          </h1>

          <h2>
             交易金额：
            <c:if test="${res.result.order!=null}">
             <fmt:formatNumber value="${res.result.order.amount/100.00}" pattern="0.00"/>
            </c:if>
            <c:if test="${res.result.order==null}">
            <fmt:formatNumber value="${res.result.trx.amount/100.00}" pattern="0.00"/>
            </c:if> 元
             交易结果：
            <c:if test="${res.result.trx.trxsts=='1'&&(res.result.order.ordersts==''||res.result.order==null)}">
              充值成功
            </c:if>
            <c:if test="${res.result.trx.trxsts=='-1'&&(res.result.order.ordersts==''||res.result.order==null)}">
              充值失败
            </c:if>               
            <c:if test="${res.result.order.ordersts=='3'||res.result.order.ordersts=='1'}">
             &nbsp;支付成功
            </c:if>
            <c:if test="${res.result.order.ordersts=='4'||res.result.order.ordersts=='5'}">
             &nbsp;支付失败
            </c:if>
           
            <br />
     <c:if test="${!(res.result.order==null||res.result.order.ordernum==null)}">
             商家订单号：${res.result.order.ordernum}<br />
     </c:if>  
     <c:if test="${!(res.result.trx==null||res.result.trx.trxnum==null)}">
            银行交易号：${res.result.trx.trxnum}<br/>
     </c:if>
     <c:if test="${res.result.order.paymethod=='balancePay'}">
            余额付款交易号：${res.result.order.id}
     </c:if>
            <br />

          </h2>

        </div>
      <c:if test="${res.action!='#'}">
        <div class="anniu xuxian">

          <span class="next_anniu" style="margin-left:210px;">
            <a href="javascript:document.frmToMerchant.submit();">
              点击
            </a>
          </span>

          本次交易<span id=msg></span>秒自动跳转,如不能自动返回，请点击此跳转链接
        </div>
      </c:if>
      </div>

      <%@ include file="/common/tail.jsp"%>

    </div>
    <script language="javascript">
      <!--
      document.all.msg.innerText=5;      
      <c:if test="${res.action!='#'}">
        var i=5;
        window.status=i+"秒自动跳转,稍后...";	    
	    function clock(){
		   document.all.msg.innerText=i;
	       i=i-1;
	       if(i>0) setTimeout( "clock(); ",1000);
	       else document.forms["frmToMerchant"].submit();
	    }
	    clock();
       //setTimeout('document.forms["frmToMerchant"].submit();',5000);
      </c:if>
      -->
    </script>
  </body>

</html>
