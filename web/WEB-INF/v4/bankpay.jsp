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
    <input type="hidden" name="_paytype" value="0"/>
    <input type="hidden" name="acquireCode" value="${m.acquireCode}"/>    
    <input type="hidden" id="_compositemobile" name="_compositemobile" value=""/>
    <input type="hidden" name="userID" value="${m.userID}"/>
    <input type="hidden" name="fraudcheck" value="${m.fraudcheck}"/>
    <input type="hidden" name="payment_type" value="1"/>
    
   
      <c:if test="${m.fraudcheck!='1'}"> 
      <div class="ord_btye"><p >您正在使用即时到账交易：付款后资金直接进入对方账户</p></div>
      </c:if>  
	    <div class="ord_bt"><p class="font14">选择支付银行：</p></div>
	    <div class="guideye ">
	              <div class="SelectBank"> 
	                   <c:if test="${m.b2cChannelNum>'1'}"><div class="moreblkye"><a href="javascript:showDiv()">全部银行</a></div></c:if>
			                <li id="bb">
				             <input type="radio" name="_channelcode" id="B2C-${m._b2cchannelcode}" value="${m._b2cchannelcode}" checked="" onclick="setBankName(1,'${m._b2cchannelcode}')">
				             <label for="B2C-${m._b2cchannelcode}"> <img src="images/bank_v3/${m._b2cchannelcode}_OUT.png"  width="127" height="40" border="0" onclick="javascript:showDiv()" style="cursor:pointer;"> </label>
				             </li>
		           </div>
	       </div>
	    <div class="ord_cntbox">
	      <p class="txtLeft">
	        <input type="submit" name="button" onclick="location.href='index_fk.html'" id="button" value="下一步" class="btn-default" />
	      </p>
	    </div>   
   <div id="popDiv" class="mydiv" style="display:none;">
      <div class="morebank"><span class="left">选择银行</span><a class="right blue" href="javascript:closeDiv()"></a></div> 
          <div class="SelectBank">
            <c:forEach var="B2Cchannel" items="${m._channelList}" varStatus="l">
               <c:if test="${B2Cchannel.channel_type=='1'&&m._b2cchannelcode!=B2Cchannel.channel_code&&B2Cchannel.channel_code!='UNIONGATEWAY'&&B2Cchannel.channel_code!='ONLINE_FIVE'&&B2Cchannel.channel_code!='ONLINE'&&B2Cchannel.channel_code!='YINTONG'&&B2Cchannel.channel_code!='CHINABANK'&&B2Cchannel.channel_code!='RONGXIN'&&B2Cchannel.channel_code!='LEFU_MOBILE'&&B2Cchannel.channel_code!='FENGPAY'&&B2Cchannel.channel_code!='UNIONMOBILE'}">
				  <li>
				    <input type="radio" name="_channelcode" id="B2C-${B2Cchannel.channel_code}" value="${B2Cchannel.channel_code}" onclick="setBankName(1,&quot;${B2Cchannel.channel_code}&quot;)">
             		<label for="B2C-${B2Cchannel.channel_code}">
						<a href="#"><img src="images/bank_v3/${B2Cchannel.channel_code}_OUT.png" width="127" height="40" border="0" onclick="setBankName(1,&quot;${B2Cchannel.channel_code}&quot;)"></a>                        		</label>
				  </li>
				</c:if>
			</c:forEach>
          </div>
          <div class="ord_cnt">
          <p class="txtLeft pdleft40">
            <input type="submit" name="button" onclick="location.href='index_fk.html'" id="button" value="下一步" class="btn-default" />
          </p>
        </div>
        </div>
        <div id="bg" class="bg" style="display:none;"></div>
         
</form>