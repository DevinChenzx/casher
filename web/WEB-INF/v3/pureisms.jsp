<%@ page contentType="text/html; charset=utf-8" language="java"%>

<%@ include file="/common/style.jsp"%>

<%@ include file="/common/tags.jsp"%>
<SCRIPT language=JavaScript type=text/javascript>
	 
function check1(obj,menu) 
	 {
		  if (obj.style.display=='none')
		  {
			  obj.style.display='block';
			  menu.src="images/Arrow_08.gif";
		  }
		  else
		  {
			  obj.style.display='none';
			  menu.src="images/Arrow_07.gif";
		  }
	 }
</SCRIPT>
<body>

	<div class="main">

      <%@ include file="/common/head.jsp"%>    
          
      <!--<div class="tu_jingdu"><img src="images/olind_2.gif" width="948" height="29" /></div>-->
      <div class="process">
      <div class="welcome">
        <p class="txtLeft pdleft">您好,吉卡用户 ${m._sorder.buyer_name}</p>
      </div>
      <div class="process2"></div>
    </div>
      <%@ include file="orderinfo.jsp"%>
      <div class="tabye">
        <dl>
	        <div class="choose">
		          <div class="fangshi">
		            <p class="txtLeft pdleft">请选择您的付款方式: </p>
		          </div>
	        </div>
          <dd> 
          	<ul>
				<%@include file="ismspay.jsp" %>
			</ul>
		  </dd>
		 </dl> 
       </div>
     <%@ include file="/common/tail.jsp"%>
        

    </div>
</body>