
<form action="/CashierConfirm.do" method="post" name="__cashierfm">
<input type="hidden" name="_persistence" value="${m._persistence}"/>
<input type="hidden" name="_orderId" value="${m._sorder.ordernum}"/>
<input type="hidden" name="_id" value="${m._id}"/>
<input type="hidden" name="_bankname" value="${m._channelcode}"/>
<input type="hidden" name="_compositemode" value="0"/>
<input type="hidden" name="_paytype" value="8"/>
<input type="hidden" name="serviceCode" value="${m.serviceCode}"/>
             
             	<div style="padding-bottom:10px; margin:10px 10px 0 15px; float:left; height:50px;"><span class="left" style="margin-top:10px; margin-right:10px;"><input type="radio" checked name="_channelcode" id="FXPPAYCARD-B2C" value="FXPPAYCARD" onclick="setBankName(&quot;FXPPAYCARD&quot;)"></span>
             		<span class="left">
             		<label for="FXPPAYCARD-B2C">
             		    <a href="#"><img src="images/bank/FXPPAYCARD.gif" width="52" height="47" border="0" onclick="setBankName(&quot;FXPPAYCARD&quot;)"></a>
  		             </label>
                    </span>
               </div>
               <div style="padding-bottom:10px; margin:10px 10px 0 15px; float:left; height:50px;"><span class="left" style="margin-top:10px; margin-right:10px;"><input type="radio"  name="_channelcode" id="CREDITCARD-B2C" value="CREDITCARD" onclick="setBankName(&quot;CREDITCARD&quot;)"></span>
             		<span class="left">
             		<label for="CREDITCARD-B2C">
             		    <a href="#"><img src="images/bank/CREDITCARD.gif" width="52" height="47" border="0" onclick="setBankName(&quot;CREDITCARD&quot;)"></a>
  		             </label>
                    </span>
               </div>
                <div style="padding-bottom:10px; margin:10px 10px 0 15px; float:left; height:50px;"><span class="left" style="margin-top:10px; margin-right:10px;"><input type="radio"  name="_channelcode" id="FXCCARD-B2C" value="FXCCARD" onclick="setBankName(&quot;FXCCARD&quot;)"></span>
             		<span class="left">
             		<label for="FXCCARD-B2C">
             		    <a href="#"><img src="images/bank/FXCCARD.gif" width="52" height="47" border="0" onclick="setBankName(&quot;FXCCARD&quot;)"></a>
  		             </label>
                    </span>
               </div>
               <div style="padding-bottom:10px; margin:10px 10px 0 15px; float:left; height:50px;"><span class="left" style="margin-top:10px; margin-right:10px;"><input type="radio"  name="_channelcode" id="PREPAYCARD-B2C" value="PREPAYCARD" onclick="setBankName(&quot;PREPAYCARD&quot;)"></span>
             		<span class="left">
             		<label for="PREPAYCARD-B2C">
             		    <a href="#"><img src="images/bank/PREPAYCARD.gif" width="52" height="47" border="0" onclick="setBankName(&quot;PREPAYCARD&quot;)"></a>
  		             </label>
                    </span>
               </div>
			
                        	
    <div class="anniu xuxian"  style="margin-left:20px">
       <p style="margin-left:15px;color:#000"> 邮箱或手机号: <input type="text" name="buyer_contact" value=""/>本次付款凭证将发到该邮箱或手机中 </p>
   	   <span class="next_anniu" style="margin-left:540px"><a href="javascript:document.forms['__cashierfm'].submit();">下一步</a></span>
   	</div>
</form>