   <div class="syt_content">

        	<h1>
        	<c:choose>
				  <c:when test="${m._sorder.royalty_type==12}">
				     <span class="left" style="margin-left:20px;">订单详情</span>
		        	<span class="right" style="margin-right:300px;">金额</span>
				  </c:when>
				  <c:otherwise>
				    <span class="left" style="margin-left:20px;">订单名称</span>
        	<span class="left" style="margin-left:300px;">收款方</span>
        	<span class="right" style="margin-right:100px;">订单金额</span>
				  </c:otherwise>
		    </c:choose>
        	
        	</h1>        	
       		<h2>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<c:choose>
				  <c:when test="${m._sorder.royalty_type==12}">
				       <td width="30%"><div id="dwn"><span class="left" style="margin-left:60px;"><A  onclick="" id="xiangqing">详情</A></span></div></td>
					   <td width="16%" class="syt_hs"><b><fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/></b> 元</td>
				  </c:when>
				  <c:otherwise>
				  <tr>
				    <td width="42%"><span class="left" style="margin-left:40px;"><c:choose><c:when test="${fn:length(m._sorder.subject)>15}">${fn:substring(m._sorder.subject,0,15)}...</c:when><c:otherwise>${m._sorder.subject}</c:otherwise></c:choose>
				    <span class="syt_hs">详情<A onclick=check1(t1,mt1) href="javascript:"><IMG src="images/Arrow_07.gif" name="mt1" border=0 id=mt1></A>
				    </span></td>
				    <td width="42%">${m._sorder.seller_name} ${m._sorder.seller_remarks}</td>
				    <td width="16%" class="syt_hs"><b><fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/></b> 元</td>
				  </tr>
				  </c:otherwise>
				</c:choose>
				
				  
				</table>
			</h2>

        <h4 id=t1 style="display:none">
        <p style="padding:10px;">
                商品名称(数量)：<c:choose><c:when test="${fn:length(m._sorder.subject)>15}">${fn:substring(m._sorder.subject,0,15)}...</c:when><c:otherwise>${m._sorder.subject}</c:otherwise></c:choose>
      <c:if test="${m._sorder.quantity>1}">(${m._sorder.quantity})</c:if> 
      &nbsp;&nbsp;&nbsp;&nbsp;商品描述：<c:choose><c:when test="${fn:length(m._sorder.bodys)>50}">${fn:substring(m._sorder.bodys,0,50)}...</c:when><c:otherwise>${m._sorder.bodys}</c:otherwise></c:choose> <br />
 
               交易金额：<fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/>元  &nbsp;&nbsp;&nbsp;&nbsp;购买时间：<fmt:formatDate value="${m._sorder.createdate}" pattern="yyyyMMdd HH:mm:ss"/> </p>

        </h4>        
        <h4><p style="padding:10px">交易号：${m._sorder.id}&nbsp;&nbsp;&nbsp;&nbsp;订单号：${m._sorder.ordernum}</p></h4>
      </div>
      <div class="xiangqingbox" id="qipao" style="display:none; z-index:50">
			<div class="guanbis"><a  onclick="" id="guanbi" style="color:#03F">关闭</a></div>
			  <div class="ico_jiantou"></div>
			</p>  
			<table>
			   <tr>
			     <th width="10%" scope="col"><b>序号</b></th>
			     <th width="18%" scope="col"><b>商品名称</b></th>
			     <th width="16%" scope="col"><b>&nbsp;&nbsp;收款账号</b></th>
			     <th width="15%" scope="col"><b>交易金额（元）</b></th>
			     <!--<th width="20%" scope="col"><b>购买时间</b></th>-->
			     <th width="15%" scope="col"><b>订单号</b></th>
			     <th width="16%" scope="col"><b>描述</b></th>
			   </tr>
			   <c:forEach items="${m._subOrderslist}" var="suborder" varStatus="c">
				   <tr>
				     <td scope="col">${c.index+1}</td>
				     <td scope="col"><c:choose><c:when test="${fn:length(m._sorder.subject)>17}">${fn:substring(m._sorder.subject,0,17)}...</c:when><c:otherwise>${m._sorder.subject}</c:otherwise></c:choose></td>
				     <td scope="col">&nbsp;&nbsp;<c:choose><c:when test="${fn:length(suborder.seller_name)>17}">${fn:substring(suborder.seller_name,0,17)}...</c:when><c:otherwise>${suborder.seller_name}</c:otherwise></c:choose></td>
				     <td scope="col" align="center"><strong style="color:#F00"><b><fmt:formatNumber value="${suborder.amount/100}" pattern="0.00"/></b> 元</strong></td>
				      <!--<td scope="col"><fmt:formatDate value="${suborder.createdate}" pattern="yyyyMMdd HH:mm:ss"/></td>-->
				     <td scope="col">${suborder.outtradeno}</td>
				     <td scope="col"><c:choose><c:when test="${fn:length(suborder.seller_ext)>17}">${fn:substring(suborder.seller_ext,0,17)}...</c:when><c:otherwise>${suborder.seller_ext}</c:otherwise></c:choose></td>
				   </tr>
			   </c:forEach>
			</table><br />
			</p>
		</div>
        