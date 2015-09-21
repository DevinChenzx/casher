<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common/style.jsp"%>
<%@ include file="/common/tags.jsp"%>

<body>
	<%@ include file="/common/head.jsp"%>
    <Form name=frmToMerchant Action="${res.action}" method="post">
      <c:forEach items="${res.forms}" var="map">
        <input type="hidden" name="${map.key}" value="${map.value}"/>
      </c:forEach>
    </form>
<div class="okbox">
	<div class="okmain">
    	<div class="enrry_img">
    	<c:if test="${res.result.trx.trxsts=='1'||res.result.order.ordersts=='3'}">
          <img src="/images/img/ok.gif" width="32" height="32" />
        </c:if>
        <c:if test="${res.result.trx.trxsts=='-1'||res.result.order.ordersts=='4'}">
          <img src="/images/img/enrry.gif" width="32" height="32" />
        </c:if>
    	</div>
      <div class="okmain_txt txtLeft">
      <h1>
          您本次交易已完成！
      </h1>
      <p>
             交易金额：
            <c:if test="${res.result.order!=null}">
            <span class="red">
             <fmt:formatNumber value="${res.result.order.amount/100.00}" pattern="0.00"/>
             </span>
            </c:if>
            <c:if test="${res.result.order==null}"><span class="red">
            <fmt:formatNumber value="${res.result.trx.amount/100.00}" pattern="0.00"/>
            </span>
            </c:if> 元<br/>
             交易结果：
            <c:if test="${res.result.trx.trxsts=='1'&&(res.result.order.ordersts==''||res.result.order==null)}">
              充值成功
            </c:if>
            <c:if test="${res.result.trx.trxsts=='-1'&&(res.result.order.ordersts==''||res.result.order==null)}">
              充值失败
            </c:if>               
            <c:if test="${res.result.order.ordersts=='3'||res.result.order.ordersts=='1'}">
             支付成功
            </c:if>
            <c:if test="${res.result.order.ordersts=='4'||res.result.order.ordersts=='5'}">
             支付失败
            </c:if>
           
            <br />
     <c:if test="${!(res.result.order==null||res.result.order.ordernum==null)}">
             商家订单号：${res.result.order.ordernum}<br />
     </c:if>  
     <c:if test="${!(res.result.trx==null||res.result.trx.trxnum==null)}">
            银行交易号：${res.result.trx.trxnum} <span class="red">支付金额:<fmt:formatNumber value="${res.result.trx.amount/100.00}" pattern="0.00"/></span><br/>
     </c:if>
     <c:if test="${res.result.order.paymethod=='balancePay'}">
            余额付款交易号：${res.result.order.id}
     </c:if>
            <br />
          </p>
      <c:if test="${res.action!='#'}">
        <p>
         <input type="submit" name="button" id="button" value="返回商户网站" class="zf_button" onclick="javascript:document.frmToMerchant.submit();"/>
          <br/>本次交易<span id="msg" class="red"></span>秒自动跳转,如不能自动返回，请点击此跳转链接
        </p>
      </c:if>
      </div>
    </div>
</div>
      <%@ include file="/common/tail.jsp"%>
    <script language="javascript">
      <!--
      //document.all.msg.innerText=5;
      <c:if test="${res.action!='#'}">
      	document.getElementById("msg").innerText=5;
        var i=5;
        window.status=i+"秒自动跳转,稍后...";	    
	    function clock(){
		   //document.all.msg.innerText=i;
	    	document.getElementById("msg").innerText=i;
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
