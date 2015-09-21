<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common/tags.jsp"%>
<%@ include file="/common/style.jsp"%>

<link rel="stylesheet" type="text/css" href="/js/ext/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/js/ext/css/style.css" />
<script type="text/javascript" src="/js/ext/js/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/js/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/js/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="/js/ext/js/common.js"></script>

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
	if (obj.style.display=='none')
	
	 {obj.style.display='block';menu.src="images/Arrow_08.gif";}
	 else
	 {obj.style.display='none';menu.src="images/Arrow_07.gif";}
	}
	function setBankName(bankname){
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
	function check(obj) {if (obj.style.display=='none') {obj.style.display='block'}else{obj.style.display='none'}}
	
	function checkCMB_B2B()
	{
		var cmbMerNo = document.getElementById("cmbMerNo").value;
		if("${m.acquireCode}"=="CMB_B2B" || "${m.acquireCode}"=="BOCM_B2B")
		{
			  var str="";
			  if("${m.acquireCode}"=="CMB_B2B"){
				  str="招商银行企业版";
			  }else if("${m.acquireCode}"=="BOCM_B2B"){
				  str="交通银行企业版";
			  }
		      var url = "/cmbCtl.do?bankName="+"${m.acquireCode}";
			  win1=new Ext.Window({
					   id:'win1',
			           title:str,
			           width:400,
			           modal:true,
			           height:100,
			           html: '<iframe src='+url +' height="100%" width="100%" name="popSett" scrolling="auto" frameBorder="0" onLoad="Ext.MessageBox.hide();">',
			           maximizable:true
		            });
		      win1.show();
		}else{
			document.forms[0].submit();
		}
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


<body onload="setBankName('${m._channel.acquire_code}');">

	<div class="main">

       <%@ include file="/common/head.jsp"%>
     
       <div class="tu_jingdu"><img src="images/olind_2.gif" width="948" height="29" /></div>

       <%@ include file="orderinfo.jsp"%>
       
       <form name="ebankPayForm" id="ebankPayForm" method="POST" action="/Ebank" target="_blank">
	     <input type="hidden" name="_channelToken" value="${m._channelToken}"/>
	     <input type="hidden" name="_persistence"  value="${m._persistence}"/>
         <input type="hidden" name="_orderId"      value="${m._sorder.ordernum}"/> 
         <input type="hidden" name="_id"      value="${m._id}"/>        
		
		 <input type="hidden" name="acquireCode" value="${m.acquireCode}"/>
		 <input type="hidden" id="cmbMerNo" name="cmbMerNo" value="">		
         <div class="content">
            <h1>确认付款方式和金额:</h1>

                <div class="content_t">
                    <div class="w936">
                    <div id="tb_" class="tb_">
                        <ul style="padding-left:10px;">
                                <li style="width:150px; padding-top:7px;">请确认您的付款方式:</li>
                                <c:if test="${m._paytype=='8'}"> 
                                    <li id="tb_1" class="hovertab">预付费卡</li>
                                </c:if>
                                <c:if test="${m._paytype=='0'}">
                                   <li id="tb_1" class="hovertab">银行卡</li>
                                </c:if>
                                <c:if test="${m._paytype=='9'}">
                                	<li id="tb_1" class="hovertab">企业支付</li>
                                </c:if>
                        </ul>
                    </div>
                 <div class="ctt">

                <div class="dis" id="tbc_01" >
	                <ol class="SelectBank clearfix" id="linebank2">
			            <ul>
			                <li class="Selected">
	                    		<label for="${m._bankname}">		
									<a href="#"><img  src="images/bank/${m._bankname}_OUT.gif"width="100" height="20" border="0"></a>
	                    		</label>
	                    		<span class="syt_hs_yw" style="line-height:25px;" >&nbsp;
	                    			${m._channel.acquire_name} 
	                    			支付金额：<fmt:formatNumber value="${(m._sorder.amount-m._directPayAmt)/100}" pattern="0.00"/> 元 
	                    		</span>
			                </li>
		    		    </ul>
		    		    
		    		  <c:if test="${m._directPayAmt>0}">
			    		   	<input type="hidden" name="_compositemode" value="1"/>
			    		  	<p class="syt_hs_yw" style="line-height:25px; padding-left:120px;">吉卡账户余额支付  支付金额<fmt:formatNumber value="${m._directPayAmt/100}" pattern="0.00"/> 元  </p>
	                  </c:if>
	                  
	               </ol>
	               
	                <div class="anniu" style="margin-top:20px;">
	               		<span class="next_anniu144" style="float:left;margin-left:30px"><a href="#" onclick="javascript:checkCMB_B2B();">
	               		登录<c:if test="${m._paytype=='8'}">卡系统</c:if><c:if test="${m._paytype=='0'||m._paytype=='9'}">网上银行</c:if>进行支付</a> </span> <span style="float:left;margin-left:30px;line-height:22px" class="font_hesss"><a href="/Cashier?_id=${m._id}"> 返回选择 </a></span>
	               	</div>
              	</form>
                   <%@include file="/common/bank-tips.jsp"%>
              </div>
                <div class="anniu"> 
                </div>                               
           </div>
        </div>
           <%@ include file="/common/tail.jsp"%>
      </div>
</body>
</html>
