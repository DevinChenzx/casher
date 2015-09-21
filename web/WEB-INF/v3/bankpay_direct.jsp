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
    <input type="hidden" name="cashiertravel" value="${m.istravel}"/>
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
		<p class="txtLeft pdleft8">支持国内大多数银行；如无网上银行请先<a href="" class="blue" target="_blank">开通网上银行</a></p>
	</div>
    <div class="ord_bt">
    <p class="font14">选择支付银行： </p>
    </div>
    <div class="ord_cnt">
       <div class="SelectBanktop clearfix">
           <div class="morcd red pint"><a href="#" id="back">
             <span id="kaikai2" onclick="change2()">显示更多银行</span></a></div>             
             <li id="bb" class="Selected">
             <c:if test="${m._paytype=='0'}">
             <input type="radio" name="_channelcode" id="B2C-${m._channelcode}" value="${m._channelcode}" checked="" onclick="setBankName(1,'${m._channelcode}')">
             <label for="B2C-CMB"> <img src="images/bank_v3/${m._channelcode}_OUT.png"  width="127" height="40" border="0" onclick="setBankName(1,'${m._channelcode}')"> </label>
             </c:if>           
             <c:if test="${m._paytype!='0'}">
              <c:set var="default" value="CMB"/>
              <input type="radio" name="_channelcode" id="B2C-CMB" value="CMB" checked="" onclick="setBankName(1,&quot;CMB&quot;)">
              <label for="B2C-CMB"> <img src="images/bank_v3/CMB_OUT.png" alt="招商银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;CMB&quot;)"> </label>      
             </c:if>
             </li>
          </div>
          <div class="SelectBank">
             <c:if test="${m._channelcode!='ICBC'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-ICBC" value="ICBC-icbc1025" onclick="setBankName(1,&quot;ICBC&quot;)">
               <label for="B2C-ICBC"> <img src="images/bank_v3/ICBC_OUT.png" alt="工商银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;ICBC&quot;)"> </label>
             </li>
             </c:if>
			 <c:if test="${m._channelcode!='CMB'&&default!='CMB'}">
			 <li>
               <input type="radio" name="_channelcode" id="B2C-CMB" value="CMB" onclick="setBankName(1,&quot;CMB&quot;)">
               <label for="B2C-CMB"> <img src="images/bank_v3/CMB_OUT.png" alt="招商银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;CMB&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='CCB'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-CCB" value="CCB-ccb102" onclick="setBankName(1,&quot;CCB&quot;)">
               <label for="B2C-CCB"> <img src="images/bank_v3/CCB_OUT.png" alt="建设银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;CCB&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='BOC'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-BOC" value="BOC" onclick="setBankName(1,&quot;BOC&quot;)">
               <label for="B2C-BOC"> <img src="images/bank_v3/BOC_OUT.png" alt="中国银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;BOC&quot;)"> </label>
             </li>
             </c:if>
			 
             <c:if test="${m._channelcode!='ABC'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-ABC" value="ABC-abc101" onclick="setBankName(1,&quot;ABC&quot;)">
               <label for="B2C-ABC"> <img src="images/bank_v3/ABC_OUT.png" alt="农业银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;ABC&quot;)"> </label>
             </li>
             </c:if>
			 
             <c:if test="${m._channelcode!='BOCM'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-COMM" value="BOCM" onclick="setBankName(1,&quot;COMM&quot;)">
               <label for="B2C-BOCM"> <img src="images/bank_v3/COMM_OUT.png" alt="交通银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;COMM&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='SPDB'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-SPDB" value="SPDB" onclick="setBankName(1,&quot;SPDB&quot;)">
               <label for="B2C-SPDB"> <img src="images/bank_v3/SPDB_OUT.png" alt="浦发银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;SPDB&quot;)"> </label>
             </li>
             </c:if>
			 <!--
             <c:if test="${m._channelcode!='GDB'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-GDB" value="GDB" onclick="setBankName(1,&quot;GDB&quot;)">
               <label for="B2C-GDB"> <img src="images/bank_v3/GDB_OUT.png" width="127" alt="广州发展银行" height="40" border="0" onclick="setBankName(1,&quot;GDB&quot;)"> </label>
             </li>
             </c:if>
			 -->
             <c:if test="${m._channelcode!='CITIC'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-CITIC" value="CITIC" onclick="setBankName(1,&quot;CITIC&quot;)">
               <label for="B2C-CITIC"> <img src="images/bank_v3/CITIC_OUT.png" alt="中信银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;CITIC&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='CEB'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-CEB" value="CEB" onclick="setBankName(1,&quot;CEB&quot;)">
               <label for="B2C-CEB"> <img src="images/bank_v3/CEB_OUT.png" alt="光大银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;CEB&quot;)"> </label>
             </li>
             </c:if>
			 <!--
             <c:if test="${m._channelcode!='CIB'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-CIB" value="CIB" onclick="setBankName(1,&quot;CIB&quot;)">
               <label for="B2C-CIB"> <img src="images/bank_v3/CIB_OUT.png" alt="兴业银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;CIB&quot;)"> </label>
             </li>
             </c:if>
             -->
             <c:if test="${m._channelcode!='SDB'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-SDB" value="SDB" onclick="setBankName(1,&quot;SDB&quot;)">
               <label for="B2C-SDB"> <img src="images/bank_v3/SDB_OUT.png" alt="深圳发展银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;SDB&quot;)"> </label>
             </li>
             </c:if>
             <!--
             <c:if test="${m._channelcode!='CMBC'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-CMBC" value="CMBC" onclick="setBankName(1,&quot;CMBC&quot;)">
               <label for="B2C-CMBC"> <img src="images/bank_v3/CMBC_OUT.png" alt="民生银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;CMBC&quot;)"> </label>
             </li>
             </c:if>
             -->
             <!--
             <c:if test="${m._channelcode!='HXB'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-HXB" value="HXB" onclick="setBankName(1,&quot;HXB&quot;)">
               <label for="B2C-HXB"> <img src="images/bank_v3/HXB_OUT.png" alt="华夏银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;HXB&quot;)"> </label>
             </li>
             </c:if>
             
             <c:if test="${m._channelcode!='SPA'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-SPABANK" value="SPA-spa101" onclick="setBankName(1,&quot;SPABANK&quot;)">
               <label for="B2C-SPA"> <img src="images/bank_v3/SPABANK_OUT.png" alt="平安银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;SPABANK&quot;)"> </label>
             </li>
             </c:if>
			 -->
             <c:if test="${m._channelcode!='PSBC'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-PSBC" value="PSBC" onclick="setBankName(1,&quot;PSBC&quot;)">
               <label for="B2C-PSBC"> <img src="images/bank_v3/PSBC_OUT.png" alt="中国邮政" width="127" height="40" border="0" onclick="setBankName(1,&quot;PSBC&quot;)"> </label>
             </li>
             </c:if>
			 <c:if test="${m._channelcode!='HSBK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-HSBK" value="HSBK" onclick="setBankName(1,&quot;HSBK&quot;)">
               <label for="B2C-HSBK"> <img src="images/bank_v3/HSBK_OUT.png" alt="徽商银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;HSBK&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='HZBANK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-HZBANK" value="HZBANK" onclick="setBankName(1,&quot;HZBANK&quot;)">
               <label for="B2C-HZBANK"> <img src="images/bank_v3/HZBANK_OUT.png" alt="杭州银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;HZBANK&quot;)"> </label>
             </li>
             </c:if>
			 <c:if test="${m._channelcode!='NJB'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-NJB" value="NJB" onclick="setBankName(1,&quot;NJB&quot;)">
               <label for="B2C-NJB"> <img src="images/bank_v3/NJB_OUT.png" alt="南京银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;NJB&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='SHBANK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-SHBANK" value="SHBANK" onclick="setBankName(1,&quot;SHBANK&quot;)">
               <label for="B2C-SHBANK"> <img src="images/bank_v3/SHBANK_OUT.png" alt="上海银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;SHBANK&quot;)"> </label>
             </li>
             </c:if>
			 <!--
             <c:if test="${m._channelcode!='BHBK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-BHBK" value="BHBK" onclick="setBankName(1,&quot;BHBK&quot;)">
               <label for="B2C-BHBK"> <img src="images/bank_v3/BHBK_OUT.png" alt="渤海银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;BHBK&quot;)"> </label>
             </li>
             </c:if>
			 -->
          </div>
          <div class="SelectBank" id="linebank">
		     <!--
             <c:if test="${m._channelcode!='BEA'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-BEA" value="BEA" onclick="setBankName(1,&quot;BEA&quot;)">
               <label for="B2C-BEA"> <img src="images/bank_v3/BEA_OUT.png" alt="东亚银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;BEA&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='NBBK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-NBBANK" value="NBBK-nbbk101" onclick="setBankName(1,&quot;NBBANK&quot;)">
               <label for="B2C-NBBK"> <img src="images/bank_v3/NBBANK_OUT.png" alt="宁波银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;NBBANK&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='HSBK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-HSBK" value="HSBK" onclick="setBankName(1,&quot;HSBK&quot;)">
               <label for="B2C-HSBK"> <img src="images/bank_v3/HSBK_OUT.png" alt="徽商银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;HSBK&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='FDBK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-FDB" value="FDBK-fdbk101" onclick="setBankName(1,&quot;FDB&quot;)">
               <label for="B2C-FDBK"> <img src="images/bank_v3/FDB_OUT.png" alt="富滇银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;FDB&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='ZCBK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-GZCBK" value="GZCBK" onclick="setBankName(1,&quot;GZCBK&quot;)">
               <label for="B2C-GZCBK"> <img src="images/bank_v3/GZCBK_OUT.png" alt="广州银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;GZCBK&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='SHRCB'}">
             
             <li>
               <input type="radio" name="_channelcode" id="B2C-SHRCB" value="SHRCB" onclick="setBankName(1,&quot;SHRCB&quot;)">
               <label for="B2C-SHRCB"> <img src="images/bank_v3/SHRCB_OUT.png" alt="上海农商银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;SHRCB&quot;)"> </label>
             </li>
             
             </c:if>
             -->
			 <!--
             <c:if test="${m._channelcode!='DLCBK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-DLCBK" value="DLCBK" onclick="setBankName(1,&quot;DLCBK&quot;)">
               <label for="B2C-DLCBK"> <img src="images/bank_v3/DLCBK_OUT.png" alt="大连银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;DLCBK&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='DGCBK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-DGCBK" value="DGCBK" onclick="setBankName(1,&quot;DGCBK&quot;)">
               <label for="B2C-DGCBK"> <img src="images/bank_v3/DGCBK_OUT.png" alt="东莞银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;DGCBK&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='HBBK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-HBBK" value="HBBK" onclick="setBankName(1,&quot;HBBK&quot;)">
               <label for="B2C-HBBK"> <img src="images/bank_v3/HBBK_OUT.png" alt="河北银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;HBBK&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='JSBK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-JSBK" value="JSBK" onclick="setBankName(1,&quot;JSBK&quot;)">
               <label for="B2C-JSBK"> <img src="images/bank_v3/JSBK_OUT.png" alt="江苏银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;JSBK&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='NXBK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-NXBK" value="NXBK" onclick="setBankName(1,&quot;NXBK&quot;)">
               <label for="B2C-NXBK"> <img src="images/bank_v3/NXBK_OUT.png" alt="宁夏银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;NXBK&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='QLBK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-QLBK" value="QLBK" onclick="setBankName(1,&quot;QLBK&quot;)">
               <label for="B2C-QLBK"> <img src="images/bank_v3/QLBK_OUT.png" alt="齐鲁银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;QLBK&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='XMCBK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-XMCBK" value="XMCBK" onclick="setBankName(1,&quot;XMCBK&quot;)">
               <label for="B2C-XMCBK"> <img src="images/bank_v3/XMCBK_OUT.png" alt="厦门银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;XMCBK&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='SZCBK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-SZCBK" value="SZCBK" onclick="setBankName(1,&quot;SZCBK&quot;)">
               <label for="B2C-SZCBK"> <img src="images/bank_v3/SZCBK_OUT.png" alt="苏州银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;SZCBK&quot;)"> </label>
             </li>
             </c:if>
             <c:if test="${m._channelcode!='WZMBK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-WZCBK" value="WZMBK-wzmbk101" onclick="setBankName(1,&quot;WZCBK&quot;)">
               <label for="B2C-WZMBK"> <img src="images/bank_v3/WZCBK_OUT.png" alt="温州银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;WZCBK&quot;)"> </label>
             </li>
             </c:if>
             
             <c:if test="${m._channelcode!='SHBANK'}">
             
             <li>
               <input type="radio" name="_channelcode" id="B2C-SHBANK" value="SHBANK" onclick="setBankName(1,&quot;SHBANK&quot;)">
               <label for="B2C-SHBANK"> <img src="images/bank_v3/SHBANK_OUT.png" alt="上海银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;SHBANK&quot;)"> </label>
             </li>
             
             </c:if>
             
             <c:if test="${m._channelcode!='HZBANK'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-HZBANK" value="HZBANK" onclick="setBankName(1,&quot;HZBANK&quot;)">
               <label for="B2C-HZBANK"> <img src="images/bank_v3/HZBANK_OUT.png" alt="杭州银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;HZBANK&quot;)"> </label>
             </li>
             </c:if>
			        
             <c:if test="${m._channelcode!='NJB'}">
             <li>
               <input type="radio" name="_channelcode" id="B2C-NJB" value="NJB" onclick="setBankName(1,&quot;NJB&quot;)">
               <label for="B2C-NJB"> <img src="images/bank_v3/NJB_OUT.png" alt="南京银行" width="127" height="40" border="0" onclick="setBankName(1,&quot;NJB&quot;)"> </label>
             </li>
             </c:if>
             
             --> 
             
<!--             <c:if test="${m._channelcode!='HZBANK'}">-->
<!--             <c:forEach var="entchannel" items="${m._entChannel}" varStatus="it">-->
<!--             <li>-->
<!--               <input type="radio" name="_channelcode" id="B2C-${it.acquire_code}" value="${it.acquire_code}" onclick="setBankName(1,&quot;${it.acquire_code}&quot;)">-->
<!--               <label for="B2B-${it.acquire_code}"> <img src="images/bank_v3/BA_${it.acquire_code}_OUT.gif" alt="${it.acquire_name}" width="127" height="40" border="0" onclick="setBankName(1,&quot;${it.acquire_code}&quot;)"> </label>-->
<!--             </li>-->
<!--             </c:forEach>-->
<!--             </c:if>-->
             </div>
             
            <div class="sxbankbox">
            <p class="txtLeft"><span>请确保您已经在银行柜台开通了网上支付功能，否则将无法支付成功。<a href="#" class="blue"><a href="#" class="blue" target="_balnk">如何开通?</a></span></p>
			<div id="xe"><%@include file="/common/bank-tips.jsp"%></div>
         </div>
      </div>
   
<!--	  <div class="ord_bt">-->
<!--	    <p class="font14">邮箱或手机号：</p>-->
<!--	  </div>-->
<!--	  <div class="ord_cnt">-->
<!--	    <p class="txtLeft pdleft8">-->
<!--	      <label for="textfield"></label>-->
<!--	      <input type="text" name="buyer_contact" id="" class="youxiang" value="${m._sorder.buyer_contact}"/>-->
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