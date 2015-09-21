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
	
	function setBankName(flag, bankname)
	{
		if ((flag == 1)||(flag == 3)) {
			document.getElementById("B2C-" + bankname).checked="checked";
		} else if (flag == 2) {
			document.getElementById(bankname + "-B2B").checked="checked";
		}
		aa = "-1";
		if (flag == 1) {
			document.forms["cashierfm"]._bankname.value=bankname; 
		} else if (flag == 2) {
			document.forms["_cashierfm"]._bankname.value=bankname; 
		} else if (flag == 3) {
			document.forms["__cashierfm"]._bankname.value=bankname; 
		}   
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
	function setBanknamev(xform){
		var ico
		={"ICBC-icbc1025":"ICBC","CCB-ccb102":"CCB","ABC-abc101":"ABC","BOCM":"COMM","SPA-spa101":"SPABANK","NBBK-nbbk101":"NBBANK","FDBK-fdbk101":"FDB",
				"WZMBK-wzmbk101":"WZCBK"};
		with(document){
			for(var i=0;i<xform._channelcode.length;i++){
				if(xform._channelcode[i].checked){
					if(xform._channelcode[i].value in ico)
						xform._bankname.value=ico[xform._channelcode[i].value];
					else
						xform._bankname.value=xform._channelcode[i].value;
					break;
				}
			}
		}
		return true;
	}

</SCRIPT>

<body>
<input type="hidden" id="paytype_show" value="${m.tab_index}"/>
<%@ include file="/common/head.jsp"%>
<div class="process">
  <div class="welcome">
    <p class="txtLeft pdleft">您好,吉卡用户 ${m._sorder.buyer_name}</p>
  </div>
  <div class="process2"></div>
</div>
<%@ include file="orderinfo.jsp"%>

<div class="tab">

  <dl>
    <div class="choose">
      <div class="fangshi">
        <p class="txtLeft pdleft">请选择您的付款方式: </p>
      </div>
      <dt><a>银行卡</a><a>企业网银</a></dt>
    </div>
    <dd>
    
      <!-- 支付导航 -->
<!--      <ul><%@include file="navigation.jsp" %></ul>-->
      
      
      <!-- 银行卡 -->
      <ul><%@include file="bankpay.jsp" %></ul>
      
      <!-- 企业网银 -->
      <ul><%@include file="enterprisebankpay.jsp" %></ul>
      
    </dd>
  </dl>
</div>

<%@ include file="/common/tail.jsp"%>
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