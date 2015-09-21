<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common/style.jsp"%>
<%@ include file="/common/tags.jsp"%>


<script language="javascript" type="text/javascript">
function showDiv(){
document.getElementById('popDiv').style.display='block';
document.getElementById('bg').style.display='block';
}

function closeDiv(){
document.getElementById('popDiv').style.display='none';
document.getElementById('bg').style.display='none';
}
function shoulimit(flag, bankname)
	{ 
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
function rePay()
    {
    	document.forms[0].target="_self";
    	document.forms[0].action="/Pay?_id=${m._id}&_paytype=${m._paytype}&_rePay=y&userID=${m.userID}&fraudcheck=${fraudcheck}";
    	document.forms[0].submit();
    }
 function closeTab()
	{ 
		var allDivs = document.getElementsByTagName("div");
		for (var i = 0; i < allDivs.length; ++i)
		{
			var div = allDivs[i];
			if (div.getAttribute("ctrlshow") == "true")
			{
				div.style.display="none";				
			}			
		}
		document.getElementById("xianelb").style.display="none";  
	}
</script>


<body>
<%@ include file="/common/head.jsp"%>
<%@ include file="orderinfo.jsp"%>
<form name="ebankPayForm" id="ebankPayForm" method="POST" action="/Ebank" target="_blank" >
 <input type="hidden" name="_channelToken" value="${m._channelToken}"/>
 <input type="hidden" name="_persistence"  value="${m._persistence}"/>
 <input type="hidden" name="_orderId"      value="${m._sorder.ordernum}"/> 
 <input type="hidden" name="_id"      value="${m._id}"/>
 
 <input type="hidden" name="feeAmount"      value="${m.feeAmount}"/>
 <input type="hidden" name="actualAmount"      value="${m.actualAmount}"/>
 <input type="hidden" name="feeSign"      value="${m.feeSign}"/>
 <input type="hidden" name="feeType"      value="${m.feeType}"/>
 <input type="hidden" name="feeRate"      value="${m.feeRate}"/>
 <input type="hidden" name="isRefundFee"      value="${m.isRefundFee}"/>

 <input type="hidden" name="acquireCode" value="${m.acquireCode}"/>
 <input type="hidden" id="cmbMerNo" name="cmbMerNo" value=""/>
 <input type="hidden" id="gCorpName" name="gCorpName" value=""/>
 <input type="hidden" id="gFinanceContact" name="gFinanceContact" value=""/>
 <input type="hidden" id="gContactPhone" name="gContactPhone" value=""/>
 <input type="hidden" id="paytype" name="paytype" value="${m._paytype}"/>
 <input type="hidden" id="terminal" name="terminal" value="${m.terminal}"/>

 
 <input type="hidden" name="userID" value="${m.userID}"/>
 <input type="hidden" name="fraudcheck" value="${m.fraudcheck}"/>
 <input type="hidden" name="payment_type" value="${m.payment_type}"/>
 <input type="hidden" name="pay_type" value="${m.pay_type}"/>
 <input type="hidden" name="directBankCode" value="${m.directBankCode}"/>
  <input type="hidden" name="merchantjson" value="${m.merchantjson}"/>
  
 
  <input type="hidden" name="pay_type" value="${m.pay_type}"/>
  <input type="hidden" name="ext_bankcode" value="${m.bankImg}"/>
  
  
  
 
 <div class="boxContent PAY_contentItem">

            <div class="tabsContent">
                <div class="item"  id="easyTab_1">
                    <div class="payBankArea" style="">
                        <span>付款方式：</span><span><img src="images/bank_v3/${m.bankImg}_OUT.png" width="127" height="40" style="vertical-align: middle;"/></span><span class="payTextTitle">支付：</span><span class="payTextNum"><fmt:formatNumber value="${(m._sorder.amount-m._directPayAmt)/100}" pattern="0.00"/>元</span>
                    </div>
                    <div class="seePaylimit" id="xiane"  >查看付款限额>></div>
					<div class="sxbankbox" id="xianelb" style="display:none">
					            <h1 class="txtLeft">请确保您已经在银行柜台开通了网上支付功能，否则将无法支付成功。<span class="right"><a href="javascript:void(0);" onclick="closeTab()" class="blue">关闭</a></span></h1>
					            <div id="xe"><%@include file="/common/bank-tips-new.jsp"%></div>
					</div>
                    <div class="payBtnArea"><button class="loginBankBtn" id="button" onclick="location.href='#';javascript:showDiv()">登录网银支付</button><a href="#" class="selectOtherPay" onclick="javascript:rePay();">选择其它支付方式<span></span></a></div>
                </div>
            </div>

    </div>
</div>
</form>
<div id="popDiv" class="mydiv" style="display:none;">
  <div class="morebank"><span class="left">登录网上银行付款</span><a class="right blue" href="javascript:closeDiv()"></a></div>
  <div class="openico">
    <p>请到打开的新窗口进行银行卡支付。</p>
  </div>
  <div class="opencnt">
    <h1>付款没有成功？</h1>
    <h2>请到网上银行查看您的银行卡有没有扣款。去<a href="${m.bankUrl}" class=" blue" target="_blank">${m.bankName}</a>» 银行咨询电话：${m.bankContact}</h2></h2>
    <h1> 银行已扣款，但没有提示成功？ </h1>
    <h2>由于网络传输问题造成，请点击<strong><a id="queryResult" href="#" class="blue" onClick="javascript:doQueryResult();">交易结果查询</a></strong>，我们为您恢复此交易。</h2>
    <h1>银行没有扣款？</h1>
    <h2>建议您<a class="blue b" href="javascript:rePay();">重新支付</a>一次试试看，如果仍无法成功,<br />请咨询服务:400-022-6816</h2>
  </div>
</div>
<div id="bg" class="bg" style="display:none;"></div>
<%@ include file="/common/tail.jsp"%>
</body>
</html>
