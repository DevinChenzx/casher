<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common/style.jsp"%>
<%@ include file="/common/tags.jsp"%>

<body>
<%@ include file="/common/head.jsp"%>

<!--顶部-->



<!--内容区开始-->
<div class="InContent">
	<div class="boxContent">
        <div class="payForContent clearFloat mb30">
        <h1 class="margin30">您正在使用直接付款交易</h1>
 	<form  id="sub" name="sub" action="/urlPay.do" method="post">
 	<input type="hidden" name="charset" value="utf-8"/>
        <div class="payForlayoutL borderL">
            <div class="payForListItem clearFloat mt30">
                <div class="labelText fl width150">收款客户号：</div>
                <div class="labelContent fl"><input id="merchant_ID" name="merchant_ID" type="text" class="normalInput width220" value="" /></div>
            </div>
            <div class="payForListItem clearFloat">
                <div class="labelText fl width150">收款客户邮箱：</div>
                <div class="labelContent fl"><input type="text" id="seller_email" name="seller_email"  class="normalInput width220" value="" /></div>
            </div>
            <div class="payForListItem clearFloat">
                <div class="labelText fl width150">付款金额：</div>
                <div class="labelContent fl"><input type="text" id="total_fee" name="total_fee" value="" class="normalInput" value="" /></div>
            </div>
            <div class="payForListItem clearFloat">
                <div class="labelText fl width150">付款说明：</div>
                <div class="labelContent fl"><input type="text" id="title" name="title"  class="normalInput width220" value="" /></div>
            </div>
            <div class=" payForListItem clearFloat">
                <div class="labelText fl width150">商品描述：</div>
                <div class="labelContent fl"><input type="text" d="body" name="body" class="normalInput width220" value="" /></div>
            </div>
        </div>

        <div class="payForlayoutR">
            <div class="payForTips">尊敬的客户：<br />
为了防止短信、电话、邮件和假网站等诈骗活动，保护您账户资金安全，特别提示如下：
1.本通道为直接付款通道,请勿给陌生人付款;
2.客服电话:400-888-8888 对外联系邮箱：service@gicard.net
3.请务必与收款客户确认好订单和货款后，再付款</div>
        </div>
        <div style="clear:both"></div>


        <div class="payBtnArea mt30">
        	<input type="submit" name="button" id="button" class="btn-default" value="下一步" class="zf_button" />
        </div>
        
        <script>
				$(document).ready(function() { 	
		            jQuery.validator.addMethod("total_fee",function(a,b){return this.optional(b)||/^\d+(\.\d{0,2})?$/i.test(a)},"输入有效金额");
		            $("#sub").validate({
		                rules: {
		                    total_fee:{required:true,total_fee:true,min:0.01,max:99999999},
		                    partner:{required:true},
		                    seller_email:{required:true,email:true},
		                    subject:{required:true,maxlength:128},
		                    body:{required:true,maxlength:512}
		                },
		                messages: {
		                    partner:{required:"<font color=red>请输入收款人客户号</font>"},
		                    seller_email:{required:"<font color=red>请输入收款人邮箱账户</font>",email:"<font color=red>请输入正确的收款人邮箱账户</font>"},
		                    total_fee:{required:"<font color=red>请输入付款金额</font>",total_fee:"<font color=red>无效金额</font>",min:'<font color=red>金额值必须大于{0}</font>',max:'<font color=red>金额值必须小于{0}</font>'},		            
		                    body:{required:"<font color=red>请输入付款摘要</font>",maxlength:"<font color=red>长度不能超过{0}</font>"},
		                    subject:{required:"<font color=red>请输入付款说明</font>",maxlength:"<font color=red>长度不能超过{0}</font>"}
		                }
		            });
		        });
			</script>
	 	</form>
    </div>
</div>
<!--内容区结束-->


<!--底部版权区-->
<div class="InFooter">
	<div class="friends">
    	<div class="banks">
        	<h2>合作银行</h2>
            <ul>
            	<li><a href="#" title="工商银行" class="bank1"></a></li>
            	<li><a href="#" title="招商银行" class="bank2"></a></li>
            	<li><a href="#" title="建设银行" class="bank3"></a></li>
            	<li><a href="#" title="农业银行" class="bank4"></a></li>
            	<li><a href="#" title="中国银行" class="bank5"></a></li>
            	<li><a href="#" title="光大银行" class="bank6"></a></li>
            	<li><a href="#" title="中信银行" class="bank7"></a></li>
            	<li><a href="#" title="兴业银行" class="bank8"></a></li>
            	<li><a href="#" title="邮政银行" class="bank9"></a></li>
                <li><a href="#" title="交通银行" class="bank10"></a></li>
                <li><a href="#" title="浦发银行" class="bank11"></a></li>
                <li><a href="#" title="广发银行" class="bank12"></a></li>
                <li><a href="#" title="民生银行" class="bank13"></a></li>
                <li><a href="#" title="深圳发展银行" class="bank14"></a></li>
                <li><a href="#" title="北京银行" class="bank15"></a></li>
                <li><a href="#" title="中国平安" class="bank16"></a></li>
                <li><a href="#" title="银联" class="bank17"></a></li>
                <li><a href="#" title="江苏银行" class="bank18"></a></li>
                <li><a href="#" title="上海农商银行" class="bank19"></a></li>
                <li><a href="#" title="宁波银行" class="bank20"></a></li>
                <li><a href="#" title="南京银行" class="bank21"></a></li>
                <li><a href="#" title="广州银行" class="bank22"></a></li>
                <li><a href="#" title="上海银行" class="bank23"></a></li>
                <li><a href="#" title="华夏银行" class="bank24"></a></li>
            </ul>
            <div class="clearFloat"></div>
        </div>
        <div class="shops">
            <h2>合作商家</h2>
            <ul>
            	<li><a href="#" class="shop_taobao"></a></li>
            	<li><a href="#" class="shop_tmall"></a></li>
            	<li><a href="#" class="shop_jhs"></a></li>
            	<li><a href="#" class="shop_vancl"></a></li>
            	<li><a href="#" class="shop_haier"></a></li>
            </ul>
            <div class="clearFloat"></div>
        </div>
        <div class="clearFloat"></div>
    </div>



<%@ include file="/common/tail.jsp"%>
</div>
</body>
</html>