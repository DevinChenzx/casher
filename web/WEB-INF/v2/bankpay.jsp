<script>
var kai=true;
function change(){
 if(kai){
  document.getElementById("sDiv").style.display="";
  kai=false;
  kaikai.innerHTML="隐藏更多银行"
 }else{
  document.getElementById("sDiv").style.display="none";
  kai=true;
    kaikai.innerHTML="显示更多银行"
 }
}
</script>
<form action="/CashierConfirm.do" method="post" name="cashierfm">
       <input type="hidden" name="_persistence" value="${m._persistence}"/>
       <input type="hidden" name="_orderId" value="${m._sorder.ordernum}"/>
       <input type="hidden" name="_id" value="${m._id}"/>
       <input type="hidden" name="_bankname" value="${m._channelcode}"/>
       <input type="hidden" name="_compositemode" value="0"/>
       <input type="hidden" name="_paytype" value="0"/>
       
       <input type="hidden" id="_compositemobile" name="_compositemobile" value=""/>
       
       <input type="hidden" name="acquireCode" value="${m.acquireCode}"/>


                <ol class="SelectBank clearfix" id="linebank2">

		       

		       <!--

		       <ul class="yui-nav2" id="lineBank2"  style="padding-top:0px;" >

					-->

					
                      <div>
					    					    																		
                         <c:if test="${m._channelcode!='ICBC'}">
							<li>
								<input type="radio" name="_channelcode" id="B2C-ICBC" value="ICBC-icbc1025" onclick="setBankName(&quot;ICBC&quot;)">

                        		<label for="B2C-ICBC">
									<a href="#"><img src="images/bank/ICBC_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;ICBC&quot;)"></a>

                        		</label>

                        	</li>
                         </c:if>
                         
                            <li class="Selected">
								<input type="radio" name="_channelcode" id="B2C-${m._channelcode}" value="${m._channelcode}" checked="" onclick="setBankName('${m._channelcode}')">
                                
                        		<label for="B2C-CMB">

									<a href="#"><img  src="images/bank/${m._channelcode}_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;CMB&quot;)"></a>
                                    
                        		</label>
                                <span class="syt_hs" style="line-height:35px;float:right; text-align:right;" id="kaikai"  onclick="change()">选择更多银行</span>
                        	</li>
                        	
                        	<c:if test="${m._channelcode!='CMB'}">
                        	<li>
								<input type="radio" name="_channelcode" id="B2C-ICBC" value="CMB-cmb308" onclick="setBankName(&quot;CMB&quot;)">

                        		<label for="B2C-ICBC">
									<a href="#"><img src="images/bank/CMB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;CMB&quot;)"></a>

                        		</label>
                        	</li>
                        	</c:if>
                        	<c:if test="${m._channelcode!='CCB'}">
							<li>  
                                <input type="radio" name="_channelcode" id="B2C-CCB" value="CCB-ccb102" onclick="setBankName(&quot;CCB&quot;)">

                        		<label for="B2C-CCB">

									<a href="#"><img src="images/bank/CCB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;CCB&quot;)"></a>

                        		</label>

                        	</li>
                        	</c:if>
                        	<c:if test="${m._channelcode!='BOC'}">
                             <li>
								<input type="radio" name="_channelcode" id="B2C-BOC" value="BOC" onclick="setBankName(&quot;BOC&quot;)">

                        		<label for="B2C-BOC">

									<a href="#"><img src="images/bank/BOC_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;BOC&quot;)"></a>

                        		</label>

                        	</li>
                            </c:if>
                            <c:if test="${m._channelcode!='ABC'}">
							<li>
									<input type="radio" name="_channelcode" id="B2C-ABC" value="ABC-abc101" onclick="setBankName(&quot;ABC&quot;)">
									<label for="B2C-ABC">
										<a href="#"><img src="images/bank/ABC_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;ABC&quot;)"></a>
									</label>
                        	</li>
                            </c:if>
                            <c:if test="${m._channelcode!='BOCM'}">
							<li>
									<input type="radio" name="_channelcode" id="B2C-BOCM" value="BOCM" onclick="setBankName(&quot;COMM&quot;)">
									<label for="B2C-BOCM">
										<a href="#"><img src="images/bank/COMM_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;COMM&quot;)"></a>
									</label>
                        	</li>
                        	</c:if>
                            <c:if test="${m._channelcode!='SPDB'}">
							<li>
									<input type="radio" name="_channelcode" id="B2C-SPDB" value="SPDB" onclick="setBankName(&quot;SPDB&quot;)">
									<label for="B2C-SPDB">
										<a href="#"><img src="images/bank/SPDB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;SPDB&quot;)"></a>
									</label>
                        	</li>
                            </c:if>
                            
                            <c:if test="${m._channelcode!='GDB'}">
							<li>
									<input type="radio" name="_channelcode" id="B2C-GDB" value="GDB-gdb101" onclick="setBankName(&quot;GDB&quot;)">
	                        		<label for="B2C-GDB">
										<a href="#"><img src="images/bank/GDB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;GDB&quot;)"></a>
	                        		</label>
                        	</li>
                            </c:if>
                            
                            <c:if test="${m._channelcode!='CITIC'}">
							<li>
									<input type="radio" name="_channelcode" id="B2C-CITIC" value="CITIC" onclick="setBankName(&quot;CITIC&quot;)">
	                        		<label for="B2C-CITIC">
										<a href="#"><img src="images/bank/CITIC_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;CITIC&quot;)"></a>
	                        		</label>
                        	</li>                      	
                            </c:if>
                            <c:if test="${m._channelcode!='CEB'}">
							<li>
									<input type="radio" name="_channelcode" id="B2C-CEB" value="CEB" onclick="setBankName(&quot;CEB&quot;)">
	                        		<label for="B2C-CEB">
										<a href="#"><img src="images/bank/CEB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;CEB&quot;)"></a>
	                        		</label>
                        	</li>
                            </c:if>
                           <c:if test="${m._channelcode!='CIB'}"> 
							<li>
									<input type="radio" name="_channelcode" id="B2C-CIB" value="CIB-cib101" onclick="setBankName(&quot;CIB&quot;)">
	                        		<label for="B2C-CIB">
										<a href="#"><img src="images/bank/CIB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;CIB&quot;)"></a>
	                        		</label>
                        	 </li>
                           </c:if>
                           <c:if test="${m._channelcode!='SDB'}">
							<li>				
									<input type="radio" name="_channelcode" id="B2C-SDB" value="SDB-sdb101" onclick="setBankName(&quot;SDB&quot;)">
	                        		<label for="B2C-SDB">
										<a href="#"><img src="images/bank/SDB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;SDB&quot;)"></a>
	                        		</label>
                        	</li>
                           </c:if>
                           <c:if test="${m._channelcode!='CMBC'}">
							<li>
									<input type="radio" name="_channelcode" id="B2C-CMBC" value="CMBC-cmbc101" onclick="setBankName(&quot;CMBC&quot;)">
	                        		<label for="B2C-CMBC">
										<a href="#"><img src="images/bank/CMBC_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;CMBC&quot;)"></a>
	                        		</label>
                        	</li>
                           </c:if>
                           
                           <!--华夏-->
                           <c:if test="${m._channelcode!='HXB'}">
                        	<li>        												
									<input type="radio" name="_channelcode" id="B2C-HXB" value="HXB" onclick="setBankName(&quot;HXB&quot;)">
	                        		<label for="B2C-HXB">
										<a href="#"><img src="images/bank/HXB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;HXB&quot;)"></a>
	                        		</label>
                        	</li>
                           </c:if>
                           <c:if test="${m._channelcode!='SPA'}">
							<li>
									<input type="radio" name="_channelcode" id="B2C-SPA" value="SPA-spa101" onclick="setBankName(&quot;SPABANK&quot;)">
	                        		<label for="B2C-SPA">
										<a href="#"><img src="images/bank/SPABANK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;SPABANK&quot;)"></a>
	                        		</label>
                        	</li>							
                           </c:if>
                           <c:if test="${m._channelcode!='PSBC'}">
							<li>
									<input type="radio" name="_channelcode" id="B2C-PSBC" value="PSBC-psbc102" onclick="setBankName(&quot;PSBC&quot;)">
	                        		<label for="B2C-PSBC">
										<a href="#"><img src="images/bank/PSBC_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;PSBC&quot;)"></a>
	                        		</label>
                        	</li>
                           </c:if>
                           <c:if test="${m._channelcode!='BHBK'}">                       	
                        	  <li>
								<input type="radio" name="_channelcode" id="B2C-BHBK" value="BHBK-bhbk101" onclick="setBankName(&quot;BHBK&quot;)">
                        		<label for="B2C-BHBK">
									<a href="#"><img src="images/bank/BHBK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;BHBK&quot;)"></a>
                        		</label>
                        	</li>
                           </c:if>
                       </div>
                        	
                        <div id="sDiv" style="display:none" >  
                           <c:if test="${m._channelcode!='BEA'}">
                        	 <li>
								<input type="radio" name="_channelcode" id="B2C-BEA" value="BEA-bea101" onclick="setBankName(&quot;BEA&quot;)">
                        		<label for="B2C-BEA">
									<a href="#"><img src="images/bank/BEA_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;BEA&quot;)"></a>
                        		</label>
                        	</li>
                        	</c:if>
                        	<c:if test="${m._channelcode!='NBBK'}">
                        	<li>        												

									<input type="radio" name="_channelcode" id="B2C-NBBK" value="NBBK-nbbk101" onclick="setBankName(&quot;NBBANK&quot;)">

                        		<label for="B2C-NBBK">

									<a href="#"><img src="images/bank/NBBANK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;NBBANK&quot;)"></a>

                        		</label>

                        	</li>
                        	</c:if>
                          <!--  <li>
									<input type="radio" name="_channelcode" id="B2C-BJBANK" value="BJBANK-bjbank101" onclick="setBankName(&quot;BJBANK&quot;)">

                        		<label for="B2C-BJBANK">

									<a href="#"><img src="images/bank/BJBANK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;BJBANK&quot;)"></a>

                        		</label>

                        	</li>
                             <li>
							    <input type="radio" name="_channelcode" id="B2C-${it.acquire_code}" value="${it.acquire_code}" onclick="setBankName(&quot;${it.acquire_code}&quot;)">
                         		<label for="B2B-${it.acquire_code}">
									<a href="#"><img src="images/bank/BA_ABC_OUT.gif" width="120" height="20" alt="${it.acquire_name}" border="0" onclick="setBankName(&quot;${it.acquire_code}&quot;)"></a>                        		</label>
							  </li>	-->
							  
							  <c:if test="${m._channelcode!='HSBK'}">
							  <li>
								     <input type="radio" name="_channelcode" id="B2C-HSBK" value="HSBK-hsbk101" onclick="setBankName(&quot;HSBK&quot;)">
	                        		<label for="B2C-HSBK">
										<a href="#"><img src="images/bank/HSBK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;HSBK&quot;)"></a>
	                        		</label>
                        	 </li>
                        	 </c:if>
                        	 <c:if test="${m._channelcode!='FDBK'}">
                        	<li>
									<input type="radio" name="_channelcode" id="B2C-FDBK" value="FDBK-fdbk101" onclick="setBankName(&quot;FDB&quot;)">
                        		    <label for="B2C-FDBK">
									<a href="#"><img src="images/bank/FDB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;FDB&quot;)"></a>
                        		</label>

                        	</li>
                        	</c:if>
                        	<c:if test="${m._channelcode!='ZCBK'}">
                        	  <li>
								<input type="radio" name="_channelcode" id="B2C-GZCBK" value="GZCBK-gzcbk101" onclick="setBankName(&quot;GZCBK&quot;)">
                        		<label for="B2C-GZCBK">
									<a href="#"><img src="images/bank/GZCBK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;GZCBK&quot;)"></a>
                        		</label>
                        	  </li>
                        	</c:if>
                        	<c:if test="${m._channelcode!='SHRCB'}">
                        	<li>
								<input type="radio" name="_channelcode" id="B2C-SHRCB" value="SHRCB-shrcb101" onclick="setBankName(&quot;SHRCB&quot;)">
                        		<label for="B2C-SHRCB">
									<a href="#"><img src="images/bank/SHRCB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;SHRCB&quot;)"></a>
                        		</label>
                        	</li>
                        	</c:if>
                        	<c:if test="${m._channelcode!='DLCBK'}">
                        	<li>
								<input type="radio" name="_channelcode" id="B2C-GZCBK" value="DLCBK-dlcbk101" onclick="setBankName(&quot;DLCBK&quot;)">
                        		<label for="B2C-DLCBK">
									<a href="#"><img src="images/bank/DLCBK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;DLCBK&quot;)"></a>
                        		</label>
                        	</li>
                        	</c:if>
                        	<c:if test="${m._channelcode!='DGCBK'}">
                        	<li>
								<input type="radio" name="_channelcode" id="B2C-DGCBK" value="DGCBK-dgcbk101" onclick="setBankName(&quot;DGCBK&quot;)">
                        		<label for="B2C-DGCBK">
									<a href="#"><img src="images/bank/DGCBK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;DGCBK&quot;)"></a>
                        		</label>
                        	</li>
                        	</c:if>
                        	<!--
                        	<c:if test="${m._channelcode!='GZMBK'}">
                        	<li>
								<input type="radio" name="_channelcode" id="B2C-GZMBK" value="GZMBK-gzmbk101" onclick="setBankName(&quot;GZMBK&quot;)">
                        		<label for="B2C-GZMBK">
									<a href="#"><img src="images/bank/GZMBK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;GZMBK&quot;)"></a>
                        		</label>
                        	</li>
                        	</c:if>
                        	-->
                        	<c:if test="${m._channelcode!='HBBK'}">
                        	<li>
								<input type="radio" name="_channelcode" id="B2C-HBBK" value="HBBK-hbbk101" onclick="setBankName(&quot;HBBK&quot;)">
                        		<label for="B2C-HBBK">
									<a href="#"><img src="images/bank/HBBK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;HBBK&quot;)"></a>
                        		</label>
                        	</li>
                        	</c:if>
                        	<c:if test="${m._channelcode!='JSBK'}">
                        	<li>
								<input type="radio" name="_channelcode" id="B2C-JSBK" value="JSBK-jsbk101" onclick="setBankName(&quot;JSBK&quot;)">
                        		<label for="B2C-JSBK">
									<a href="#"><img src="images/bank/JSBK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;JSBK&quot;)"></a>
                        		</label>
                        	</li>
                        	</c:if>
                        	<c:if test="${m._channelcode!='NXBK'}">
                        	<li>
								<input type="radio" name="_channelcode" id="B2C-NXBK" value="NXBK-nxbk101" onclick="setBankName(&quot;NXBK&quot;)">
                        		<label for="B2C-NXBK">
									<a href="#"><img src="images/bank/NXBK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;NXBK&quot;)"></a>
                        		</label>
                        	</li>
                        	</c:if>
                        	<c:if test="${m._channelcode!='QLBK'}">
                        	<li>
								<input type="radio" name="_channelcode" id="B2C-QLBK" value="JSBK-jsbk101" onclick="setBankName(&quot;QLBK&quot;)">
                        		<label for="B2C-QLBK">
									<a href="#"><img src="images/bank/QLBK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;QLBK&quot;)"></a>
                        		</label>
                        	</li>
                        	</c:if>
                        	<c:if test="${m._channelcode!='XMCBK'}">
                        	<li>
								<input type="radio" name="_channelcode" id="B2C-XMCBK" value="XMCBK-xmcbk101" onclick="setBankName(&quot;XMCBK&quot;)">
                        		<label for="B2C-XMCBK">
									<a href="#"><img src="images/bank/XMCBK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;XMCBK&quot;)"></a>
                        		</label>
                        	</li>
                        	</c:if>
                        	
                        	<!--<li>
								<input type="radio" name="_channelcode" id="B2C-SHRCB" value="SHRCB-shrcb101" onclick="setBankName(&quot;SHRCB&quot;)">
                        		<label for="B2C-SHRCB">
									<a href="#"><img src="images/bank/SHRCB_OUT1.gif" width="120" height="20" border="0" onclick="setBankName(&quot;SHRCB&quot;)"></a>
                        		</label>
                        	</li>-->
                        	<c:if test="${m._channelcode!='SZCBK'}">
                        	<li>
								<input type="radio" name="_channelcode" id="B2C-SZCBK" value="SZCBK-szcbk101" onclick="setBankName(&quot;SZCBK&quot;)">
                        		<label for="B2C-SZCBK">
									<a href="#"><img src="images/bank/SZCBK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;SZCBK&quot;)"></a>
                        		</label>
                        	</li>
                        	</c:if>
                        	<c:if test="${m._channelcode!='WZMBK'}">
                        	<li>
								<input type="radio" name="_channelcode" id="B2C-WZMBK" value="WZMBK-wzmbk101" onclick="setBankName(&quot;WZCBK&quot;)">
                        		<label for="B2C-WZMBK">
									<a href="#"><img src="images/bank/WZCBK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;WZCBK&quot;)"></a>
                        		</label>
                        	</li>
                        	</c:if>
                        	
                        	<c:if test="${m._channelcode!='SHBANK'}">
                        	<!--
                        		<li>
									<input type="radio" name="_channelcode" id="B2C-SHBANK" value="SHBANK-wzmbk101" onclick="setBankName(&quot;SHBANK&quot;)">
	                        		<label for="B2C-SHBANK">
										<a href="#"><img src="images/bank/SHBANK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;SHBANK&quot;)"></a>
	                        		</label>
	                        	</li>
	                        -->
                        	</c:if>
                        	<c:if test="${m._channelcode!='HZBANK'}">
                        	<li>
								<input type="radio" name="_channelcode" id="B2C-HZBANK" value="HZBANK-hz100" onclick="setBankName(&quot;HZBANK&quot;)">
	                    		<label for="B2C-HZBANK">
									<a href="#"><img src="images/bank/HZCB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;HZBANK&quot;)"></a>
	                    		</label>
                    		</li>
                    		</c:if>
                    		
                        	<c:if test="${m._channelcode!='NJB'}">
                    		<li>
								<input type="radio" name="_channelcode" id="B2C-NJB" value="NJB-hz100" onclick="setBankName(&quot;NJB&quot;)">
	                    		<label for="B2C-NJB">
									<a href="#"><img src="images/bank/NJB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;NJB&quot;)"></a>
	                    		</label>
                    		</li>
                            </c:if>
                            
                        	<c:if test="${m._channelcode!='HZBANK'}">
							 <c:forEach var="entchannel" items="${m._entChannel}" varStatus="it">
							  <li>
							    <input type="radio" name="_channelcode" id="B2C-${it.acquire_code}" value="${it.acquire_code}" onclick="setBankName(&quot;${it.acquire_code}&quot;)">
                         		<label for="B2B-${it.acquire_code}">
									<a href="#"><img src="images/BA_${it.acquire_code}_OUT.gif" width="120" height="20" alt="${it.acquire_name}" border="0" onclick="setBankName(&quot;${it.acquire_code}&quot;)"></a>                        		</label>
							  </li>
							</c:forEach>
							
                           </div>
												

										 

                                         

    			  </ul>

				  

   </ol> 
                           <div class="anniu xuxian"  style="margin-left:20px"> 
                               <p style="margin-left:15px;color:#000"> 邮箱或手机号: <input type="text" name="buyer_contact" value="${m._sorder.buyer_contact}"/> 本次付款凭证将发送到该邮箱或手机中</p>
                           	   <span class="next_anniu" style="margin-left:540px"><a href="javascript:document.forms['cashierfm'].submit();">下一步</a></span>
                           	</div>
                           	
                           	</form>

                           <%@include file="/common/bank-tips.jsp"%>