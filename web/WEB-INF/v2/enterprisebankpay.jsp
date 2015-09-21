
<form action="/CashierConfirm.do" method="post" name="_cashierfm">
<input type="hidden" name="_persistence" value="${m._persistence}"/>
<input type="hidden" name="_orderId" value="${m._sorder.ordernum}"/>
<input type="hidden" name="_id" value="${m._id}"/>
<input type="hidden" name="_bankname" value="${m._channelcode}"/>
<input type="hidden" name="_compositemode" value="0"/>
<input type="hidden" name="_paytype" value="9"/>

<input type="hidden" name="serviceCode" value="${m.serviceCode}"/>

				<!--
				<ul class="SelectBank clearfix" id="linebank2">
					<li class="Selected">
						<br/>&nbsp;&nbsp;&nbsp;
						<input type="radio" name="_channelcode" id="CCB-B2B" value="CCB_B2B" checked="" onclick="setBankName(&quot;CCB&quot;)">
                		<label for="CCB-B2B">
							<a href="#"><img  src="images/bank/CCB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;CCB&quot;)"></a>
                		</label>
                	</li>
				</ul>
				-->
				
                <ol class="SelectBank clearfix" id="linebank2">
						<div>
							
							<li class="Selected">
								<input type="radio" name="_channelcode" id="${m._channelcode}_B2B" value="${m._channelcode}_B2B" checked="" onclick="setBankName('${m._channelcode}')">
                        		<label for="CMB-B2B">
									<a href="#"><img  src="images/bank/${m._channelcode}_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;CMB&quot;)"></a>
                        		</label>
                        	</li>
                        	<li>
								<input type="radio" name="_channelcode" id="ICBC_B2B" value="ICBC_B2B" onclick="setBankName(&quot;ICBC&quot;)">
                        		<label for="ICBC-B2B">
									<a href="#"><img src="images/bank/ICBC_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;ICBC&quot;)"></a>
                        		</label>
                        	</li>
                        	<li>
							  <input type="radio" name="_channelcode" id="CCB-B2B" value="CCB_B2B" onclick="setBankName(&quot;CCB&quot;)">
                        		<label for="CCB-B2B">
									<a href="#"><img src="images/bank/CCB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;CCB&quot;)"></a>
                        		</label>
                        	</li>
                            <li>
								<input type="radio" name="_channelcode" id="BOC-B2B" value="BOC_B2B" onclick="setBankName(&quot;BOC&quot;)">
                        		<label for="BOC-B2B">
									<a href="#"><img src="images/bank/BOC_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;BOC&quot;)"></a>
                        		</label>
                        	</li>
							<li>
								<input type="radio" name="_channelcode" id="ABC-B2B" value="ABC_B2B" onclick="setBankName(&quot;ABC&quot;)">
                        		<label for="ABC-B2B">
									<a href="#"><img src="images/bank/ABC_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;ABC&quot;)"></a>
                        		</label>
                        	</li>
                        	<li>
								<input type="radio" name="_channelcode" id="BOCM_B2B" value="BOCM_B2B" onclick="setBankName(&quot;COMM&quot;)">
                        		<label for="BOCM-B2B">
									<a href="#"><img src="images/bank/COMM_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;COMM&quot;)"></a>
                        		</label>
                        	</li>
                        	<li>
								<input type="radio" name="_channelcode" id="SPDB-B2B" value="SPDB_B2B" onclick="setBankName(&quot;SPDB&quot;)">
                        		<label for="SPDB-B2B">
									<a href="#"><img src="images/bank/SPDB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;SPDB&quot;)"></a>
                        		</label>
                        	</li>
                        	<li>
								<input type="radio" name="_channelcode" id="CEB-B2B" value="CEB_B2B" onclick="setBankName(&quot;CEB&quot;)">
                        		<label for="CEB-B2B">
									<a href="#"><img src="images/bank/CEB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;CEB&quot;)"></a>
                        		</label>
                        	</li>
                        	<li>
								<input type="radio" name="_channelcode" id="HSBK_B2B" value="HSBK_B2B" onclick="setBankName(&quot;HSBK&quot;)">
                        		<label for="HSBK-B2B">
									<a href="#"><img src="images/bank/HSBK_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;HSBK&quot;)"></a>
                        		</label>
                        	</li>
                        	<li>
								<input type="radio" name="_channelcode" id="SDB_B2B" value="SDB_B2B" onclick="setBankName(&quot;SDB&quot;)">
                        		<label for="SDB-B2B">
									<a href="#"><img src="images/bank/SDB_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;SDB&quot;)"></a>
                        		</label>
                        	</li>
                        	<li>
								<input type="radio" name="_channelcode" id="CITICV6_B2B" value="CITICV6_B2B" onclick="setBankName(&quot;CITICV&quot;)">
                        		<label for="CITICV6-B2B">
									<a href="#"><img src="images/bank/CITIC_OUT.gif" width="120" height="20" border="0" onclick="setBankName(&quot;CITICV&quot;)"></a>
                        		</label>
                        	</li>
                          </div>

    				</ol>
    				
						    <div class="anniu xuxian"  style="margin-left:20px"> 
                               <p style="margin-left:15px;color:#000"> 邮箱或手机号: <input type="text" name="buyer_contact" value=""/>本次付款凭证将发到该邮箱或手机中 </p>
                           	   <span class="next_anniu" style="margin-left:540px"><a href="javascript:document.forms['_cashierfm'].submit();">下一步</a></span>
                           	</div>
                          
                           	</form>