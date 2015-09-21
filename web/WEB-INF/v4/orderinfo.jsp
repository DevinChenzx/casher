<!--订单详细 合单支付-->        
<!--订单详细结束 合单支付-->

<div class="InContent">
	<div class="boxContent">
        <div class="normalContent" style="position:relative">
            <h1>来自${m._sorder.seller_remarks}的交易</h1>
            <div class="tradeNum">
                <p>本次交易金额：</p>
                <p class="tradeNumber"><fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/>元</p>
            </div>
            <p class="tradeInfoList mt20">
                <span class="txtLabel">收款方：</span><span>${m._sorder.seller_remarks}</span>
            </p>
            <p class="tradeInfoList">
                <span class="txtLabel">商品名称：</span><span>在线支付订单  (${m._sorder.ordernum}) </span>
            </p>
            
            
            
			<div id="qipao" style="display:none">
					  <c:choose>
					  <c:when test="${m._sorder.royalty_type==12}">
						  <table>
						    <tr>
						      <th width="18%" scope="col">序号</th>
						      <th width="15%" scope="col">商品名称</th>
						      <th width="16%" scope="col">收款账号</th>
						      <th width="20%" scope="col">交易金额（元）</th>
						      <th width="16%" scope="col">订单号</th>
						      <th width="15%" scope="col">描述</th>
						    </tr>
						    <c:forEach items="${m._subOrderslist}" var="suborder" varStatus="c">
						    <tr>
						      <td scope="col" >${c.index+1}</td>
						      <td scope="col" ><c:choose><c:when test="${fn:length(m._sorder.subject)>17}">${fn:substring(m._sorder.subject,0,17)}...</c:when><c:otherwise>${m._sorder.subject}</c:otherwise></c:choose></td>
						      <td scope="col" ><c:choose><c:when test="${fn:length(suborder.seller_name)>17}">${fn:substring(suborder.seller_name,0,17)}...</c:when><c:otherwise>${suborder.seller_name}</c:otherwise></c:choose></td>
						      <td scope="col" ><strong style="color:#F00"><b><fmt:formatNumber value="${suborder.amount/100}" pattern="0.00"/></b> 元</strong></td>
						      <td scope="col" >${suborder.outtradeno}</td>
						      <td scope="col" ><c:choose><c:when test="${fn:length(suborder.seller_ext)>17}">${fn:substring(suborder.seller_ext,0,17)}...</c:when><c:otherwise>${suborder.seller_ext}</c:otherwise></c:choose></td>
						    </tr>
						    </c:forEach>
						  </table>
						</c:when>
						<c:otherwise>
						  <!-- 
						  <table>
						    <tr>
						      <th width="15%" scope="col">商品名称(数量)</th>
						      <th width="15%" scope="col">交易金额（元）</th>
						      <th width="15%" scope="col">购买时间</th>
						      <th width="15%" scope="col">商品描述</th>
						      <th width="20%" scope="col">交易号</th>
						      <th width="20%" scope="col">订单号</th>
						    </tr>
						    <tr>
						      <td scope="col" ><c:choose><c:when test="${fn:length(m._sorder.subject)>15}">${fn:substring(m._sorder.subject,0,15)}...</c:when><c:otherwise>${m._sorder.subject}</c:otherwise></c:choose><c:if test="${m._sorder.quantity>1}">(${m._sorder.quantity})</c:if></td>
						      <td scope="col" ><strong style="color:#F00"><b><fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/></b> 元</strong></td>
						      <td scope="col" ><fmt:formatDate value="${m._sorder.createdate}" pattern="yyyyMMdd HH:mm:ss"/></td>
						      <td scope="col" ><c:choose><c:when test="${fn:length(m._sorder.bodys)>50}">${fn:substring(m._sorder.bodys,0,50)}...</c:when><c:otherwise>${m._sorder.bodys}</c:otherwise></c:choose></td>
						      <td scope="col" >${m._sorder.id}</td>
						      <td scope="col" >${m._sorder.ordernum}</td>
						    </tr>
						  </table>
						 -->  
						  
			<p class="tradeInfoList">
                <span class="txtLabel">商品名称(数量)：</span><span><c:choose><c:when test="${fn:length(m._sorder.subject)>15}">${fn:substring(m._sorder.subject,0,15)}...</c:when><c:otherwise>${m._sorder.subject}</c:otherwise></c:choose><c:if test="${m._sorder.quantity>1}">(${m._sorder.quantity})</c:if></span>
            </p>
             <p class="tradeInfoList">
                <span class="txtLabel">交易金额（元）：</span><span><fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/></b> 元</span>
            </p>
             <p class="tradeInfoList">
                <span class="txtLabel">购买时间：</span><span><fmt:formatDate value="${m._sorder.createdate}" pattern="yyyyMMdd HH:mm:ss"/></span>
            </p>                        
             <p class="tradeInfoList">
                <span class="txtLabel">商品描述：</span><span><c:choose><c:when test="${fn:length(m._sorder.bodys)>50}">${fn:substring(m._sorder.bodys,0,50)}...</c:when><c:otherwise>${m._sorder.bodys}</c:otherwise></c:choose></span>
            </p>
             <p class="tradeInfoList">
                <span class="txtLabel">交易号：</span><span>${m._sorder.id}</span>
            </p>                        
             <p class="tradeInfoList">
                <span class="txtLabel">订单号：</span><span>${m._sorder.ordernum}</span>
            </p>     
						  
						</c:otherwise>
						</c:choose>
					  <br />
					  

					</div>
            <div class="seeMore"><a href="javascript:void(0);" id="xiangqing" >交易详细</a></div>
        </div>
    </div>



