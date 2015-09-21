<script>
	var kai1=true;
	function change1(){
		if(kai1){
			kai1=false;
			kaikai1.innerHTML="隐藏更多支付卡"
		}else{
			kai1=true;
			kaikai1.innerHTML="显示更多支付卡"
		}
	}
</script>
<form action="/PayConfirm.do" method="post" name="__cashierfm">
	<input type="hidden" name="_persistence" value="${m._persistence}"/>
	<input type="hidden" name="_orderId" value="${m._sorder.ordernum}"/>
	<input type="hidden" name="_id" value="${m._id}"/>
	<input type="hidden" name="_bankname" value="FXPPAYCARD"/>
	<input type="hidden" name="_compositemode" value="0"/>
	<input type="hidden" name="_paytype" value="8"/>
	<input type="hidden" name="serviceCode" value="${m.serviceCode}"/>
	    <c:if test="${m.fraudcheck!='1'}"> 
		    <div class="ord_bt">
				<p class=" txtRight">温馨提示： </p>
			</div>
			<div class="ord_cnt">
				<p class="txtLeft pdleft8 red">您正在向${m._sorder.seller_remarks}付款，请谨慎核对付款信息</p>
			</div>
	    </c:if>  
		<div class="ord_bt">
          <p class=" txtRight">支付特点： </p>
        </div>
        <div class="ord_cnt">
          <p class="txtLeft pdleft8">持有如意卡、易行卡、其它预付卡的用户，只需输入卡号密码，方便快捷</p>
        </div>
       <div class="ord_bt">
       <p class="font14">选择支付卡类型： </p>
       </div>
       <div class="ord_cnt">
         <div class="SelectBank2 clearfix">
         	<div class="morback red pint"><a href="#" id="card"><span id="kaikai1" onclick="change1()">选择更多支付卡</span></a></div>
         	<li id="aa" class="Selected2">
	         	<c:if test="${m._paytype=='8'}">    
	         		<input type="radio" name="_channelcode" id="B2C-${m._channelcode}" value="${m._channelcode}" checked="" onclick="setBankName(3,'${m._channelcode}')">
	         		<label for="CARD-B2C"> <img src="images/bank/${m._channelcode}.gif"  width="52" height="47" border="0" onclick="setBankName(3,'${m._channelcode}')"> </label>
	            </c:if>
	            <c:if test="${m._paytype!='8'}">
		            <c:set var="default" value="FXPPAYCARD"/>
		     		<input type="radio" name="_channelcode" id="B2C-FXPPAYCARD" value="FXPPAYCARD" checked="" onclick="setBankName(3,'FXPPAYCARD')">
		     		<label for="CARD-B2C"> <img src="images/bank/FXPPAYCARD.gif"  width="52" height="47" border="0" onclick="setBankName(3,'FXPPAYCARD')"> </label>
	            </c:if>
            </li>
          </div>
          <div class="SelectBank2"  id="linebank2">
             <c:if test="${m._channelcode!='FXPPAYCARD'&&default!='FXPPAYCARD'}">
             <li id="aa">
              <input type="radio" name="_channelcode" id="B2C-FXPPAYCARD" value="FXPPAYCARD" onclick="setBankName(3,&quot;FXPPAYCARD&quot;)">
              <label for="FXPPAYCARD-B2C"> <img src="images/bank/FXPPAYCARD.gif" width="52" height="47" border="0" onclick="setBankName(3,&quot;FXPPAYCARD&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='CREDITCARD'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-CREDITCARD" value="CREDITCARD" onclick="setBankName(3,&quot;CREDITCARD&quot;)">
               <label for="CREDITCARD-B2C"> <img src="images/bank/CREDITCARD.gif" width="52" height="47"  border="0" onclick="setBankName(3,&quot;CREDITCARD&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='FXCCARD'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-FXCCARD" value="FXCCARD" onclick="setBankName(3,&quot;FXCCARD&quot;)">
               <label for="FXCCARD-B2C"> <img src="images/bank/FXCCARD.gif" width="52" height="47"  border="0" onclick="setBankName(3,&quot;FXCCARD&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='PREPAYCARD'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-PREPAYCARD" value="PREPAYCARD" onclick="setBankName(3,&quot;PREPAYCARD&quot;)">
               <label for="PREPAYCARD-B2C"> <img src="images/bank/PREPAYCARD.gif" width="52" height="47"  border="0" onclick="setBankName(3,&quot;PREPAYCARD&quot;)"> </label>
             </li>
             </c:if>
           </div>
         </div>
<!--		  <div class="ord_bt">-->
<!--		    <p class="font14">邮箱或手机号：</p>-->
<!--		  </div>-->
<!--		  <div class="ord_cnt">-->
<!--		    <p class="txtLeft pdleft8">-->
<!--		      <label for="textfield"></label>-->
<!--		      <input type="text" name="buyer_contact" id="" class="youxiang" value=""/>-->
<!--		      <span class="red ">本次付款凭证将发送到该邮箱或手机中</span></p>-->
<!--		  </div>-->
		  <div class="ord_bt">
		    <p class="font14">&nbsp;</p>
		  </div>
		  <div class="ord_cnt">
		  <p class="txtLeft pdleft8">
		      <input type="submit" name="button" id="button" value="下一步" class="zf_button" />
		    </p>
		  </div>
      </form>