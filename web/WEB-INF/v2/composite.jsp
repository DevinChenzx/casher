<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common/style.jsp"%>
<%@ include file="/common/tags.jsp"%>
<script type="text/javascript" language="javascript">
  //<!CDATA[

function g(o){return document.getElementById(o);}

function HoverLi(n){

  //如果有N个标签,就将i<=N;

  //本功能非常OK,兼容IE7,FF,IE6

  for(var i=1;i<=2;i++){g('tb_'+i).className='normaltab';g('tbc_0'+i).className='undis';}g('tbc_0'+n).className='dis';g('tb_'+n).className='hovertab';

}

  //如果要做成点击后再转到请将<li>中的onmouseover 改成 onclick;

//]]>
</script>

<SCRIPT language=JavaScript type=text/javascript>
	function check1(obj,menu) {
	  if (obj.style.display=='none'){obj.style.display='block';menu.src="images/Arrow_08.gif";}
	  else
	 {obj.style.display='none';menu.src="images/Arrow_07.gif";}
	}
	
	function check(obj) {if (obj.style.display=='none') {obj.style.display='block'}else{obj.style.display='none'}}
	function setBankName(bankname){
	    document.forms["cashierfm"]._bankname.value=bankname;
	    var allDivs = document.getElementsByTagName("div");
		for (var i = 0; i < allDivs.length; ++i)
		{
			var div = allDivs[i];
			if (div.getAttribute("ctrlshow") == "true")
			{
				div.style.display="none";
				if(div.id==bankname){
				 	div.style.display="";
			    }
			}				
		}  
	}
	function swithpaymode(){	    
	    with(document.forms["cashierfm"]){
			if(_compositemode.value==0){
				_compositemode.value=1;
			}else{
				_compositemode.value=0;
			}
			
		}		
	}

	function countDown()
	{
		 var djs = document.getElementById("daojishi");
		 var notice = document.getElementById("notice");
	     if(djs.innerHTML == 0){
	    	  document.getElementById("sendSMS").setAttribute("href","javascript:sendSMS();");
	    	  notice.style.display="none";
	          return false;
	     }
	     djs.innerHTML = djs.innerHTML - 1;
	}
	
	function sendSMS()
	{
		var notice = document.getElementById("notice");
		var djs = document.getElementById("daojishi");
		var buyer_name=document.getElementById("_buyer_name").value;
		var _orderId=document.getElementById("_orderId").value;
		var _persistence=document.getElementById("_persistence").value;
		
		var url = "<%=request.getContextPath()%>"+"/SendSMS";
		
		notice.style.display="";
		djs.innerHTML=60;
		document.getElementById("sendSMS").removeAttribute("href");
		$.post(url,{
				buyer_name:buyer_name,
				_orderId:_orderId,
				_persistence:_persistence
			},function (data, textStatus){
				if(textStatus=="success"){
					if(data=="success"){
						countDown();
						window.setInterval("countDown()", 1000);
						alert("手机验证码发送成功，请查收。");
					}
					else if(data=="failure"){
						notice.style.display="none";
						document.getElementById("sendSMS").setAttribute("href","javascript:sendSMS();");
						alert("手机验证码发送失败，请重新点击发送。");
					}
					else if(data=="failureMobile"){
						notice.style.display="none";
						document.getElementById("sendSMS").setAttribute("href","javascript:sendSMS();");
						alert("商户未绑定手机号，请先绑定手机号。");
					}
					else if(data=="failureUser"){
						notice.style.display="none";
						document.getElementById("sendSMS").setAttribute("href","javascript:sendSMS();");
						alert("请检查商户状态。");
					}
				}
				return false;
		});
	}
	
	function inputMobile()
	{
		var obj = document.getElementById("mverify_code");
		if(obj.value!=""){
			document.forms["cashierfm"]._compositemobile.value=obj.value;
		}
		/*else{
			alert("请输入手机验证码");
			return false;
		}*/
	}
	
$(document).ready(function(){
  $("#xiangqing").click(function(){
  $("#qipao").toggle();
  });
  $("#guanbi").click(function(){
  $("#qipao").fadeOut("slow");
  });
});
</SCRIPT>



<body>

	<div class="main">

    	<%@ include file="/common/head.jsp"%>    
            
        <div class="tu_jingdu"><img src="images/olind_2.gif" width="948" height="29" /></div>

   <div class="syt_content">

        	<h1>
	        	<span class="left" style="margin-left:20px;">定单名称</span>
	        	<span class="left" style="margin-left:300px;">收款方</span>
	        	<span class="right" style="margin-right:100px;">订单金额</span>
        	</h1>
       		<h2>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				  <tr>
				    <td width="42%"><span class="left" style="margin-left:40px;">${m._sorder.subject}
				    <span class="syt_hs">详情<A onclick=check1(t1,mt1) href="javascript:"><IMG src="images/Arrow_07.gif" name="mt1" border=0 id=mt1></A>
				    </span></td>
				    <td width="42%">${m._sorder.seller_name} ${m._sorder.seller_remarks}</td>
				    <td width="16%"><fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/> 元</td>
				  </tr>
				</table>
			</h2>

        <h4 id=t1 style="display:none">

        <p style="padding:10px;">交易号：${m._sorder.id}   商品名称(数量)：${m._sorder.subject}(${m._sorder.quantity}) 订单号：${m._sorder.ordernum} 商品描述：${m._sorder.bodys} <br />
            	

交易金额：<fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/>元  购买时间：<fmt:formatDate value="${m._sorder.createdate}" pattern="yyyyMMdd HH:mm:ss"/> </p>

        </h4>	

            <h3 style="height:160px">

                                   您的吉卡账户：<strong>${m._sorder.buyer_name}</strong><br />
              <p class="xuxian">
	              <span class="syt_hs">
	              <c:if test="${m._buyer_balance==null||m._buyer_balance==0}">无法使用账户余额付款,请使用其他方式付款。</c:if>
	              <c:if test="${m._buyer_balance>0}">吉卡账户可支付余额: <fmt:formatNumber value="${m._buyer_balance/100}" pattern="0.00"/> 元</c:if>              
	              </span>
              </p>              
              <c:if test="${m._buyer_balance>=m._sorder.amount}">                 
	              <p class="xuxian">
	               <form name="acctcashierfm" action="/AccountCashier" method="post">
                   <input type="hidden" name="_id" value="${m._id}"/>
                   <input type="hidden" id="_buyer_name" name="_buyer_name" value="${m._buyer_name}"/>
                   <input type="hidden" id="_persistence" name="_persistence" value="${m._persistence}"/>
                   <input type="hidden" id="_orderId" name="_orderId" value="${m._sorder.ordernum}"/>
	               <span class="syt_hs">
	                                                     使用吉卡账户支付金额: <fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/>元 
                        <br/><input id="mverify_code" type="text" name="mverify_code" class="sj_inpt"/>
      	   			    <a href="javascript:sendSMS()" id="sendSMS">获取手机验证码</a> <font style="color:red"><label id="notice" style="display:none"><span id="daojishi">60</span>秒后方可点击重发 </label></font>                 
	                    <span class="next_anniu" style="margin-top:15px"><a href="javascript:document.forms['acctcashierfm'].submit();">确认付款</a></span>    
	               </span>
	              </p>
	              </form>
              </c:if>
              <c:if test="${m._buyer_balance<m._sorder.amount&&m._buyer_balance>0&&m._ebankenable!='0'}">
	              <input type="hidden" name="_id" value="${m._id}"/>
	              <input type="hidden" id="_buyer_name" name="_buyer_name" value="${m._buyer_name}"/>
	              <input type="hidden" id="_persistence" name="_persistence" value="${m._persistence}"/>
	              <input type="hidden" id="_orderId" name="_orderId" value="${m._sorder.ordernum}"/>
	              <p class="xuxian">
	               <span class="syt_hs">
	                   <input type="checkbox" value=0 name="_compositemode" onclick="swithpaymode();"> 使用吉卡账户支付金额：<fmt:formatNumber value="${m._buyer_balance/100}"  pattern="0.00"/> 元,剩余金额 <fmt:formatNumber value="${(m._sorder.amount-m._buyer_balance)/100}" pattern="0.00"/> 元使用其它方式支付    
	                   <br/><input id="mverify_code" type="text" name="mverify_code" onblur="javascript:inputMobile()" class="sj_inpt"/>
     	   			    <a href="javascript:sendSMS()" id="sendSMS">获取手机验证码</a> <font style="color:red"><label id="notice" style="display:none"><span id="daojishi">60</span>秒后方可点击重发 </label></font>
	               </span>
	              </p>
              </c:if>
              
            </h3>

		

      </div>
      
     <c:if test="${m._ebankenable!='0'}">
      <div class="content">

            <h1>您可以使用其它方式付款:</h1>

                <div class="content_t">

                    <div class="w936">

	                        <div id="tb_" class="tb_">
	
	                                        <ul style="padding-left:10px;">
	
	                                                <li style="width:150px; padding-top:7px;">请选择您的付款方式:</li>
	
	                                                <li id="tb_1" class="hovertab">银行卡</li>                                                                                          
	
	                                        </ul>
	
	                        </div>

                        <div class="ctt">                          
                          <div class="dis" id="tbc_01" >                         
                             <%@include file="bankpay.jsp" %>
                          </div>
                          <div class="undis" id="tbc_02">
                            <%@include file="ismspay.jsp" %>
                          </div>

                          <div class="anniu"> </div>
                               

                        </div>

                   </div>
                </div>

      </div>
      </c:if>
                <%@ include file="/common/tail.jsp"%>
        
      
    </div>

</body>

</html>
