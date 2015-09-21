<%@ page contentType="text/html; charset=utf-8" language="java"%>

<%@ include file="/common/style.jsp"%>

<%@ include file="/common/tags.jsp"%>
<SCRIPT language=JavaScript type=text/javascript>
	 
	var aa = "";
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
	
	function check(obj) 
	{
		if (obj.style.display=='none') 
		{
			obj.style.display='block'
		}
		else
		{
			obj.style.display='none'
		}
	}
	
	function setBankName(bankname)
	{
		aa = "-1";
		document.forms["cashierfm"]._bankname.value=bankname; 
		document.forms["_cashierfm"]._bankname.value=bankname;   
		document.forms["__cashierfm"]._bankname.value=bankname;   
		var allDivs = document.getElementsByTagName("div");
		for (var i = 0; i < allDivs.length; ++i)
		{
			var div = allDivs[i];
			if (div.getAttribute("ctrlshow") == "true")
			{
				div.style.display="none";
				if(div.id==bankname)
				{
					div.style.display="";
				}
			}			
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

<body>

	<div class="main">

    	<%@ include file="/common/head.jsp"%>    
          
        <div class="tu_jingdu"><img src="images/olind_2.gif" width="948" height="29" /></div>
      	<%@ include file="orderinfo.jsp"%>

      <div class="content">
            <h1>您可以使用其它方式付款:</h1>
                <div class="content_t">
                     <div class="w936">
                        <div id="tb_" class="tb_">
                            <ul style="padding-left:10px;">
                                    <li style="width:150px; padding-top:7px;">请选择您的付款方式:</li>
                                    <li id="tb_4" class="normaltab" onClick="i:HoverLi(4);">预付费卡支付</li>                                    
                               		<li id="tb_2" class="normaltab" onClick="i:HoverLi(2);">吉卡余额支付</li>
                               		<li id="tb_1" class="hovertab" onClick="x:HoverLi(1);">银行卡</li> 
									<li id="tb_3" class="normaltab" onClick="i:HoverLi(3);">企业支付</li>
									
                            </ul>
                        </div>
                        <div class="ctt">
                          
                          <div class="dis" id="tbc_01" >                         
                            <%@include file="bankpay.jsp" %>
                          </div>
						  <div class="undis" id="tbc_02">
						     <%@include file="ismspay.jsp" %>                          
						  </div>
						  <div class="undis" id="tbc_03" >                         
                            <%@include file="enterprisebankpay.jsp" %>
                          </div>
                          <div class="undis" id="tbc_04" >                         
                            <%@include file="prepaymentcard.jsp" %>
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

</html>
<script Language="JavaScript" type="text/JavaScript">
//<!CDATA[
	function g(o)
	{
		return document.getElementById(o);
	}
	function HoverLi(n)
	{

	    //如果有N个标签,就将i<=N;
		//本功能非常OK,兼容IE7,FF,IE6  
		for(var i=1;i<=4;i++)
		{
			g('tb_'+i).className='normaltab';
			g('tbc_0'+i).className='undis';
		}
		g('tbc_0'+n).className='dis';
		g('tb_'+n).className='hovertab';
		//radioShow(n);		
	 }
//如果要做成点击后再转到请将<li>中的onmouseover 改成 onclick;

//]]>
	if(aa==""){
		document.getElementById("CMB").style.display="";
	}

	function radioShow(n)
	{
		if(n!=2)
		{
			var allRadio = document.getElementsByName('_channelcode');
			for(var i=0;i<allRadio.length;i++)
			{
				if(allRadio[i].checked)
				{
					if(n==1)
					{
						if(allRadio[i].id.indexOf("B2C")!=-1)
							setBankName(allRadio[i].id.replace(/B2C-/,""));
					}else if(n==3)
					{
						if(allRadio[i].id.indexOf("B2B")!=-1)
							setBankName(allRadio[i].value.replace(/_B2B/,""));
					}
				}				
			}
		}
	}
</script>