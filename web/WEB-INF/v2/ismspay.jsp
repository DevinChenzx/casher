<script language="javascript" src="/js/jquery/jquery-1.4.4.js"></script>
<script language="javascript" src="/js/jquery/jquery.validate.min.js"></script>
<script type="text/javascript" src="/js/keyboard.js" charset="UTF-8"></script>
<link rel="stylesheet" type="text/css" href="/css/keyboard.css">
<script>
function checkIn(){    
	with(document.forms["compositefm"]){
		if(buyer_name.value.length==0){
			alert("请输入吉卡账户");
			return false;
		}
		if(buyer_paypwd.value.length==0){
			alert("请输入支付密码");
			return false;			
		}
		if(verify_code.value.length<5){
			alert("校验码长度不足");
			return false;
		}
		return true;
	}
	alert("校验错误");
	return false;
}
</script>
<form id="compositefm" name="compositefm" action="/CompositeCashier" method="Post" onsubmit="return false;">
       <input type="hidden" name="_persistence" value="${m._persistence}"/>
       <input type="hidden" name="_orderId" value="${m._sorder.ordernum}"/>
       <input type="hidden" name="_id" value="${m._id}"/>
       <input type="hidden" name="_ebankenable" value="${m._ebankenable}"/>
<div style=" float:left; margin-left:30px; margin-top:10px;">
<table>
	<tr>
		<td class="font_12" height="30">吉卡账户:</td>
		<td><input id="buyer_name" type="text" <c:if test="${m._sorder.buyer_name!=''}">readonly</c:if> name="buyer_name" class="sj_inpt" value="${m._sorder.buyer_name}"/></td>
	</tr>
	<tr>
		<td class="font_12" height="30">支付密码:</td>
		<td><input id="buyer_paypwd" type="password" name="buyer_paypwd" class="keyboardInput"/></td>
	</tr>
	<tr>
		<td class="font_12" height="40">验证码:</td>
		<td><input id="verify_code" type="text" name="verify_code" class="sj_inpt"/><a><img src=/captcha.do witdh=120 height=19 align="absmiddle" 
		    onclick="javascript:this.src='/captcha.do?'+Math.random();" style="cursor: hand;"  alt="看不清楚?点击刷新验证码"/> </a></td>
	</tr>
	<tr>
		<td></td>
		<td><span class="next_anniu" style="margin-left:70px"><a href="#" onclick="javascript:document.forms['compositefm'].submit();">下一步</a></span>
                        
	</tr>
</table>
</div>

<script>
 $(document).ready(function() {
            //jQuery.validator.addMethod("total_fee",function(a,b){return this.optional(b)||/^\d+(\.\d{0,2})?$/i.test(a)},"输入有效金额");                 
            $("#compositefm").validate({
                rules: {
                    buyer_name:{required:true},                   
                    buyer_paypwd:{required:true},
                    verify_code:{required:true}                    
                },
                messages: {
                    buyer_name:{required:"<font color=red>请输入邮箱或手机号</font>"},
                    buyer_paypwd:{required:"<font color=red>请输入支付密码</font>"},
                    verify_code:{required:"<font color=red>请输入校验码</font>"}                                           
                }
            });
        });
</script>
</form>