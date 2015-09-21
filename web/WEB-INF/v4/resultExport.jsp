<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common/style.jsp"%>
<%@ include file="/common/tags.jsp"%>
<style type="text/css">
body{background:#eeeeee}
</style>
<body>
	
		<%@ include file="/common/head.jsp"%>
	
	    <Form name=frmToMerchant Action="${res.action}" method="post">
      <c:forEach items="${res.forms}" var="map">
        <input type="hidden" name="${map.key}" value="${map.value}"/>
      </c:forEach>
    </form>
		
<div class="InContent">
	<div class="boxContent">

        <div class="sucessContent">
            <div class="dialogTipsArea">
                <span class="TipIco_sucess"></span>
                <span class="TipText">支付成功！ <br><span>感谢使用吉高，您有任何问题或建议可以发送邮件至service@gigold.net</span></span>
                <div style="clear:both"></div>
            </div>

            <div class="sucessResult">
                <div class="resultItem">本次交易金额：
             <c:if test="${res.result.order!=null}">
             <fmt:formatNumber value="${res.result.order.amount/100.00}" pattern="0.00"/>
            </c:if>
            <c:if test="${res.result.order==null}">
            <fmt:formatNumber value="${res.result.trx.amount/100.00}" pattern="0.00"/>
            </c:if> 元</div>
             <div class="resultItem">
                   <c:if test="${!(res.result.order==null||res.result.order.ordernum==null)}">
                                       商家订单号：${res.result.order.ordernum}
                </c:if>  
                </div>
                <div class="resultItem">    	     <c:if test="${!(res.result.trx==null||res.result.trx.trxnum==null)}">
			            银行交易号：${res.result.trx.trxnum}<br/>
			   </c:if></div>
			   <c:if test="${res.action!='#'}">
                <div class="resultItem"><button id="button" value="返回商户网站" onclick="javascript:document.frmToMerchant.submit();">返回商户网站</button></div>
                <div class="resultItem">本次交易<span id="msg" class="red"></span>秒后自动跳转，如不能自动返回，请点击这里进行跳转</div>
	     	 </c:if>   
	     	 
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
	      
            </div>
        </div>
    </div>
</div>
		
		
       
<%@ include file="/common/tail.jsp"%>



       
  
  </body>

</html>
