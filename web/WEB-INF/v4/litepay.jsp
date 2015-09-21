<script>
	var kai=true;
	function change2(){
		if(kai){
			kai=false;
			kaikai2.innerHTML="隐藏部分银行"
		}else{
			kai=true;
			kaikai2.innerHTML="显示更多银行"
		}
	}	
</script>
<form action="/PayConfirm.do" method="post" id="cashierfm" name="cashierfm" onsubmit="return setBanknamev(this);">
<input type="hidden" name="_persistence" value="${m._persistence}"/>
<input type="hidden" name="_orderId" value="${m._sorder.ordernum}"/>
<input type="hidden" name="_id" value="${m._id}"/>
<input type="hidden" name="_bankname" value="${m._channelcode}"/>
<input type="hidden" name="_compositemode" value="0"/>
<input type="hidden" name="_paytype" value="4"/>

<input type="hidden" name="serviceCode" value="${m.serviceCode}"/>
<input type="hidden" name="userID" value="${m.userID}"/>
<input type="hidden" name="fraudcheck" value="${m.fraudcheck}"/>
<input type="hidden" name="payment_type" value="1"/>
   
     <c:if test="${m.fraudcheck!='1'}"> 
      <div class="ord_btye"><p >您正在使用即时到账交易：付款后资金直接进入对方账户</p></div>
      </c:if>  
	    <div class="ord_bt"><p class="font14">选择支付银行：</p></div>
	    <div class="guideye ">
	              <div class="SelectBank">
		               <li id="bb">
			             <input type="radio" name="_channelcode" id="LITEPAY" value="UNIONGATEWAY" checked="" onclick="setBankName(2,'UNIONGATEWAY')">
			             <label for="LITEPAY"> <img src="images/bank_v3/TFT_OUT.png"  width="127" height="40" border="0" onclick="setBankName(2,'UNIONGATEWAY')"></label>
			             </li>
		            </div>
	     </div>
	    <div class="ord_cntbox">
	      <p class="txtLeft">
	        <input type="submit" name="button" onclick="location.href='index_fk.html'" id="button" value="下一步" class="btn-default" />
	      </p>
	    </div>   
         
</form>