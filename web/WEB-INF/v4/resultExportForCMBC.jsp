<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common/tags.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>吉高收银台</title>
</head>
<style type="text/css">
.orders, .tab dt, .tab dt a.on,.moreblkye,.openico ,.queren,.quern_yes,.quern_no,.quern_warning{ background: url(http://epay.gicard.net.cn/images/img/bg.png); }


body{font-family:'宋体';text-align:center;margin:0 auto;padding:0;background:#FFF;font-size:12px;color:#333;}
body > div{text-align:center;margin-right:auto;margin-left:auto;} 
div,form,ul,ol,li,span,p{margin:0;padding:0;border:0;}
img,a img{border:0;margin:0;padding:0;}
h1,h2,h3,h4,h5,h6,dl,dt,dd{margin:0;padding:0;font-size:12px;font-weight:normal;}
ul,ol,li{list-style:none}
table,td,input{font-size:12px;padding:0}



/*颜色*/
.red,.red a{color:#c00!important;text-decoration:none;}
.red a:hover{color:#c00!important;text-decoration:underline;}
.blue,.blue a{color:#00a0e9!important;text-decoration:none}
.blue a:hover{color:#00a0e9!important;text-decoration:underline}
.black,.black a{color:#000!important;text-decoration:none}
.black a:hover{color:#000!important;text-decoration:underline}
.white{background:#fff;}

body{background:#eeeeee}

.qunrenzit{font-family:"微软雅黑"; font-size:14px;}
.queren{ width:689px; height:289px; background-position: 0px -133px; margin-top:10%;position:relative;}
.queren_nobox{ width:689px; height:289px; background-position: 0px -133px; margin-top:1%}
.quern_yes{ width:575px; height:50px; float:left; background-position:0px -426px;background:no-repeat;margin:10px 0 0 50px;}
.quern_yes p{padding-left:60px; line-height:40px; text-align:left;color:#565656;}
.quern_link{width:680px; height:30px; float:left; line-height:30px; text-align:right; margin-top:190px;}
.quern_link2{width:680px; height:30px; left:0px; top:250px; line-height:30px; text-align:right;position:absolute}

/*支付按钮*/
.zf_button { background:url(http://epay.gicard.net.cn/images/img/button.gif) no-repeat; width:125px; height:38px; line-height:38px; color:#fff; font-family:"微软雅黑"; font-weight:bold; border:none; cursor:pointer; }
.pdleft40 { padding-left:40px; }
</style>
<body>
	
    <Form name=frmToMerchant Action="${res.action}" method="post">
      <c:forEach items="${res.forms}" var="map">
        <input type="hidden" name="${map.key}" value="${map.value}"/>
      </c:forEach>
    </form>
        <div class="queren">
    	<div class="quern_yes">
    	  <p><span class="qunrenzit red">支付成功!</span> 感谢使用吉高，您有任何问题或建议发送邮件至service@gicard.net.cn</p>
    	  <p> 交易金额：
            <c:if test="${res.result.order!=null}">
            <span class="red">
             <fmt:formatNumber value="${res.result.order.amount/100.00}" pattern="0.00"/>
             </span>
            </c:if>
            <c:if test="${res.result.order==null}"><span class="red">
            <fmt:formatNumber value="${res.result.trx.amount/100.00}" pattern="0.00"/>
            </span>
            </c:if> 元
          </p>
    	  <p> 
    	        <c:if test="${!(res.result.order==null||res.result.order.ordernum==null)}">
                                      商家订单号：${res.result.order.ordernum}<br />
                </c:if>  
         </p>
    	  <p>
    	     <c:if test="${!(res.result.trx==null||res.result.trx.trxnum==null)}">
			           银行交易号：${res.result.trx.trxnum} <span class="red">支付金额:<fmt:formatNumber value="${res.result.trx.amount/100.00}" pattern="0.00"/></span><br/>
			   </c:if>
    	  </p> 
    	  <c:if test="${res.action!='#'}">
	        <p>
	         <input type="submit" name="button" id="button" value="返回商户网站" class="zf_button" onclick="javascript:document.frmToMerchant.submit();"/>
	          <br/>本次交易<span id="msg" class="red"></span>秒自动跳转,如不能自动返回，请点击此跳转链接
	        </p>
	      </c:if>   	 
        </div>
        <div class="quern_link2">了解更多吉卡相关产品请登陆吉卡官网：www.gicard.net</div> 
      </div>
      
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
