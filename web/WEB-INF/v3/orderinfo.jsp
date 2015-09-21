<!--订单详细 合单支付-->        
<div class="xiangqingbox" id="qipao" style="display:none;">
<div class="guanbi"><A  onclick="" id="guanbi" class="pint" style="color:#03F">关闭</A></div>
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
	      <td scope="col" class="txtLeft">${c.index+1}</td>
	      <td scope="col" class="txtLeft"><c:choose><c:when test="${fn:length(m._sorder.subject)>17}">${fn:substring(m._sorder.subject,0,17)}...</c:when><c:otherwise>${m._sorder.subject}</c:otherwise></c:choose></td>
	      <td scope="col" class="txtLeft"><c:choose><c:when test="${fn:length(suborder.seller_name)>17}">${fn:substring(suborder.seller_name,0,17)}...</c:when><c:otherwise>${suborder.seller_name}</c:otherwise></c:choose></td>
	      <td scope="col" class="txtLeft"><strong style="color:#F00"><b><fmt:formatNumber value="${suborder.amount/100}" pattern="0.00"/></b> 元</strong></td>
	      <td scope="col" class="txtLeft">${suborder.outtradeno}</td>
	      <td scope="col" class="txtLeft"><c:choose><c:when test="${fn:length(suborder.seller_ext)>17}">${fn:substring(suborder.seller_ext,0,17)}...</c:when><c:otherwise>${suborder.seller_ext}</c:otherwise></c:choose></td>
	    </tr>
	    </c:forEach>
	  </table>
	</c:when>
	<c:otherwise>
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
	      <td scope="col" class="txtLeft"><c:choose><c:when test="${fn:length(m._sorder.subject)>15}">${fn:substring(m._sorder.subject,0,15)}...</c:when><c:otherwise>${m._sorder.subject}</c:otherwise></c:choose><c:if test="${m._sorder.quantity>1}">(${m._sorder.quantity})</c:if></td>
	      <td scope="col" class="txtLeft"><strong style="color:#F00"><b><fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/></b> 元</strong></td>
	      <td scope="col" class="txtLeft"><fmt:formatDate value="${m._sorder.createdate}" pattern="yyyyMMdd HH:mm:ss"/></td>
	      <td scope="col" class="txtLeft"><c:choose><c:when test="${fn:length(m._sorder.bodys)>50}">${fn:substring(m._sorder.bodys,0,50)}...</c:when><c:otherwise>${m._sorder.bodys}</c:otherwise></c:choose></td>
	      <td scope="col" class="txtLeft">${m._sorder.id}</td>
	      <td scope="col" class="txtLeft">${m._sorder.ordernum}</td>
	    </tr>
	  </table>
	</c:otherwise>
	</c:choose>
  <br />
</div>
<!--订单详细结束 合单支付-->
<div class="zhifu">
  <table class="zhifutab">
    <tr>
      <th colspan="2" scope="col" class="txtLeft">订单名称 <c:choose><c:when test="${fn:length(m._sorder.subject)>15}">${fn:substring(m._sorder.subject,0,15)}...</c:when><c:otherwise>${m._sorder.subject}</c:otherwise></c:choose><a onclick="" id="xiangqing" class="blue">详情</a></th>
      <th width="23%" class="red" scope="col">订单金额: <fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/> 元</th>
    </tr>
    <tr>
      <td width="8%" class=" txtRight">收款方:</td>
      <td width="69%" class="txtLeft">${m._sorder.seller_remarks}</td>
      <td class="red">&nbsp;</td>
    </tr>
    <tr>
      <td class=" txtRight">&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
  </table>
</div>

