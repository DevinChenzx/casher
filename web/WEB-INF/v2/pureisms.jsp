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
          
      <div class="tu_jingdu"><img src="images/olind_2.gif" width="948" height="29" /></div>
      <%@ include file="orderinfo.jsp"%>
      <div class="content">

            <h1>您可以使用吉卡账户付款:</h1>

                <div class="content_t">

                                    <div class="w936">

                        <div id="tb_" class="tb_">

                                        <ul style="padding-left:10px;">                                                
                                                
                                        <li id="tb_2" class="normaltab">吉卡余额支付</li>											

                                        </ul>

                        </div>

                        <div class="ctt">                          
                         
						  <div class="dis" id="tbc_02">
						     <%@include file="ismspay.jsp" %>                          
						  </div>						 

                          <div class="anniu">                     

                          </div>                             

                        </div>

                </div>
                </div>
      </div>

                <%@ include file="/common/tail.jsp"%>
        

    </div>
</body>