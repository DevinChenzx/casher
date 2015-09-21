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

function showB2BDiv(){
document.getElementById('popB2BDiv').style.display='block';
document.getElementById('B2Bbg').style.display='block';
}
function closeB2BDiv(){
document.getElementById('popB2BDiv').style.display='none';
document.getElementById('B2Bbg').style.display='none';
}

function showCDiv(){
document.getElementById('popCDiv').style.display='block';
document.getElementById('Cbg').style.display='block';
}
function closeCDiv(){
document.getElementById('popCDiv').style.display='none';
document.getElementById('Cbg').style.display='none';
}
</script>


<body>
<input type="hidden" id="paytype_show" value="${m.tab_index}"/>
<%@ include file="/common/head.jsp"%>
<%@ include file="orderinfo.jsp"%>


    <div class="boxContent PAY_contentItem">
        <div class="tabsArea">
            <div class="tabsTop">
                <div class="tabsLabel clearFloat">
                    <h4>选择付款方式</h4>
                    	<c:if test="${m.isDebitCard>='1'}"><a class="tabsItem " >储蓄卡</a></c:if><c:if test="${m.isCreditCard>='1'}"><a class="tabsItem">信用卡</a></c:if><c:if test="${m.b2bChannelNum>='1'}"><a class="tabsItem">企业网银</a></c:if><c:if test="${m.litepayNum=='2'}"><a class="tabsItem">快捷支付</a></c:if>
                    	
                </div>
            </div>

            <div class="tabsContent">
            <div class="item"  id="easyTab_1">
            	<div class="selectBankArea">
		       	 <c:if test="${m.isDebitCard>='1'}"><div><%@include file="bankpay.jsp" %></div></c:if>
		       	 <c:if test="${m.isCreditCard>='1'}"><div><%@include file="creditforbankpay.jsp" %></div></c:if>
		         <c:if test="${m.b2bChannelNum>='1'}"><div><%@include file="b2bforbankpay.jsp" %></div></c:if>
		         <c:if test="${m.litepayNum=='2'}"><div><%@include file="litepay.jsp" %></div></c:if>

                    </div>
                
            </div>
        </div>
    </div>
</div>
</div>

<%@ include file="/common/tail.jsp"%>
</body>
</html>
