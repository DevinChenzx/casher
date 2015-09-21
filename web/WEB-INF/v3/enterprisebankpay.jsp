<script>
	var kai=true;
	function change3(){
		if(kai){
			kai=false;
			kaikai3.innerHTML="隐藏更多银行"
		}else{
			kai=true;
			kaikai3.innerHTML="显示更多银行"
		}
	}
</script>
<form action="/PayConfirm.do" method="post" name="_cashierfm" onsubmit="return setBanknamev(this);">
<input type="hidden" name="_persistence" value="${m._persistence}"/>
<input type="hidden" name="_orderId" value="${m._sorder.ordernum}"/>
<input type="hidden" name="_id" value="${m._id}"/>
<input type="hidden" name="_bankname" value="${m._channelcode}"/>
<input type="hidden" name="_compositemode" value="0"/>
<input type="hidden" name="_paytype" value="9"/>

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
       <p class="font14">选择支付银行： </p>
       </div>
       <div class="ord_cnt">
         <div class="SelectBanktop clearfix">
         	<div class="morcd red pint">
         	<a href="#" id="qyback"><span id="kaikai3" onclick="change3()">
         	<!--显示更多银行-->
         	</span></a></div>
         	<li id="cc" class="Selected">
            <c:if test="${m._paytype=='9'}">
            <input type="radio" name="_channelcode" id="${m._channelcode}-B2B" value="${m._channelcode}_B2B" checked="" onclick="setBankName(2,'${m._channelcode}')">
            <label for="CMB-B2B"> <img src="images/bank_v3/${m._channelcode}_OUT.png"  width="127" height="40" border="0" onclick="setBankName(1,'${m._channelcode}')"> </label>
            </c:if>           
            <c:if test="${m._paytype!='9'}">
             <c:set var="default" value="ICBC_B2B"/>            
             <input type="radio" name="_channelcode" id="ICBC-B2B" value="ICBC_B2B" checked="" onclick="setBankName(2,&quot;ICBC&quot;)">
             <label for="ICBC-B2B"> <img src="images/bank_v3/ICBC_OUT.png" alt="工商银行" width="127" height="40" border="0" onclick="setBankName(2,'ICBC')"> </label>
            </c:if>
            </li>
          </div>
          <div class="SelectBank">
          <c:if test="${m._channelcode!='ICBC_B2B'&&default!='ICBC_B2B'}">
             <li>
               <input type="radio" name="_channelcode" id="ICBC-B2B" value="ICBC_B2B" onclick="setBankName(2,&quot;ICBC&quot;)">
               <label for="ICBC-B2B"> <img src="images/bank_v3/ICBC_OUT.png" alt="工商银行" width="127" height="40" border="0" onclick="setBankName(2,&quot;ICBC&quot;)"> </label>
             </li>
          </c:if>
          <c:if test="${m._channelcode!='CCB_B2B'}">
             <li>
               <input type="radio" name="_channelcode" id="CCB-B2B" value="CCB_B2B" onclick="setBankName(2,&quot;CCB&quot;)">
               <label for="CCB-B2B"> <img src="images/bank_v3/CCB_OUT.png" alt="建设银行" width="127" height="40" border="0" onclick="setBankName(2,&quot;CCB&quot;)"> </label>
             </li>
          </c:if>
          		<!--
				          <li>
					          <input type="radio" name="_channelcode" id="CMB-B2B" value="CMB_B2B" onclick="setBankName(2,&quot;CMB&quot;)">
					          <label for="CMB-B2B"> <img src="images/bank_v3/CMB_OUT.png" alt="招商银行" width="127" height="40" border="0" onclick="setBankName(2,&quot;CMB&quot;)"> </label>
				          </li>
			             <li>
			               <input type="radio" name="_channelcode" id="BOC-B2B" value="BOC_B2B" onclick="setBankName(2,&quot;BOC&quot;)">
			               <label for="BOC-B2B"> <img src="images/bank_v3/BOC_OUT.png" alt="中国银行" width="127" height="40" border="0" onclick="setBankName(2,&quot;BOC&quot;)"> </label>
			             </li>
                --> 
				<c:if test="${m._channelcode!='ABC_B2B'}">
			             <li>
			               <input type="radio" name="_channelcode" id="ABC-B2B" value="ABC_B2B" onclick="setBankName(2,&quot;ABC&quot;)">
			               <label for="ABC-B2B"> <img src="images/bank_v3/ABC_OUT.png" alt="农业银行" width="127" height="40" border="0" onclick="setBankName(2,&quot;ABC&quot;)"> </label>
			             </li>
				</c:if>
				<!--
			             <li>
			               <input type="radio" name="_channelcode" id="COMM-B2B" value="BOCM_B2B" onclick="setBankName(2,&quot;COMM&quot;)">
			               <label for="BOCM-B2B"> <img src="images/bank_v3/COMM_OUT.png" alt="交通银行" width="127" height="40" border="0" onclick="setBankName(2,&quot;COMM&quot;)"> </label>
			             </li>
			             <li>
			               <input type="radio" name="_channelcode" id="SPDB-B2B" value="SPDB_B2B" onclick="setBankName(2,&quot;SPDB&quot;)">
			               <label for="SPDB_B2B"> <img src="images/bank_v3/SPDB_OUT.png" alt="浦发银行" width="127" height="40" border="0" onclick="setBankName(2,&quot;SPDB&quot;)"> </label>
			             </li>
			     -->
			 <c:if test="${m._channelcode!='CEB_B2B'}">
             <li>
               <input type="radio" name="_channelcode" id="CEB-B2B" value="CEB_B2B" onclick="setBankName(2,&quot;CEB&quot;)">
               <label for="CEB-B2B"> <img src="images/bank_v3/CEB_OUT.png" alt="光大银行" width="127" height="40" border="0" onclick="setBankName(2,&quot;CEB&quot;)"> </label>
             </li>
			 </c:if>
                <!--
				             <li>
				               <input type="radio" name="_channelcode" id="HSBK-B2B" value="HSBK_B2B" onclick="setBankName(2,&quot;HSBK&quot;)">
				               <label for="HSBK-B2B"> <img src="images/bank_v3/HSBK_OUT.png" alt="徽商银行" width="127" height="40" border="0" onclick="setBankName(2,&quot;HSBK&quot;)"> </label>
				             </li>
				             
				             <li>
				             <input type="radio" name="_channelcode" id="CITIC-B2B" value="CITIC_B2B" onclick="setBankName(2,&quot;CITIC&quot;)">
				             <label for="CITIC-B2B"> <img src="images/bank_v3/CITIC_OUT.png" alt="中信银行" width="127" height="40" border="0" onclick="setBankName(2,&quot;CITIC&quot;)"> </label>
				             </li>
				-->             
				<c:if test="${m._channelcode!='ABC_B2B'}">
				             <li>
				               <input type="radio" name="_channelcode" id="SDB-B2B" value="SDB_B2B" onclick="setBankName(2,&quot;SDB&quot;)">
				               <label for="SDB-B2B"> <img src="images/bank_v3/SDB_OUT.png" alt="深圳发展银行" width="127" height="40" border="0" onclick="setBankName(2,&quot;SDB&quot;)"> </label>
				             </li>
				</c:if>
				<!--            
				             <li>
				               <input type="radio" name="_channelcode" id="CMBC-B2B" value="CMBC_B2B" onclick="setBankName(2,&quot;CMBC&quot;)">
				               <label for="CMBC-B2B"> <img src="images/bank_v3/CMBC_OUT.png" alt="民生银行" width="127" height="40" border="0" onclick="setBankName(2,&quot;CMBC&quot;)"> </label>
				             </li>
				-->  
             </div>
         </div>
   
<!--	  <div class="ord_bt">-->
<!--	    <p class="font14">邮箱或手机号：</p>-->
<!--	  </div>-->
<!--	  <div class="ord_cnt">-->
<!--	    <p class="txtLeft pdleft8">-->
<!--	      <label for="textfield"></label>-->
<!--	      <input type="text" name="buyer_contact" id="" class="youxiang" value=""/>-->
<!--	      <span class="red ">本次付款凭证将发送到该邮箱或手机中</span></p>-->
<!--	  </div>-->
	  <div class="ord_bt">
	    <p class="font14">&nbsp;</p>
	  </div>
	  <div class="ord_cnt">
	  <p class="txtLeft pdleft8">
	      <input type="submit" name="button" id="button" value="下一步" class="zf_button" />
	    </p>
	  </div>                          
</form>