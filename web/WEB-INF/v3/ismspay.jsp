<script type="text/javascript" charset="UTF-8"></script>
<style type="text/css">
.borders{
	border:solid 1px #7bb8da
}
</style>
<script language="javascript" src="/js/ocx/PassCtrl.js"></script>
<script language="javascript" src="/js/ocx/PassGuardCtrl1026.js"></script>
<script language="javascript" src="/js/jquery/jquery.validate.min.js"></script>
<script type="text/javascript" src="/js/skeyboard.js" charset="gb2312"></script>
<script>
	//定义当前需用软键盘的表单和控件的名称
	var curEditName;
	curEditName="compositefm.buyer_paypwd";
</script>
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
function doSub() {
    if (checkIn()) {
		document.getElementById("compositefm").submit();
    }
}
</script>
<form id="compositefm" name="compositefm" action="/CompositeCashier" method="Post">
       <input type="hidden" name="_persistence" value="${m._persistence}"/>
       <input type="hidden" name="_orderId" value="${m._sorder.ordernum}"/>
       <input type="hidden" name="_id" value="${m._id}"/>
       <input type="hidden" name="_ebankenable" value="${m._ebankenable}"/>
       
	   	<input type="hidden" name="buyer_paypwd" id="buyer_paypwd" value=""/>
	   	<input type="hidden" name="local_network" id="local_network" value=""/>
	   	<input type="hidden" name="local_disk" id="local_disk" value=""/>
	   	<input type="hidden" name="local_nic" id="local_nic" value=""/>
	   	<input type="hidden" name="backpage" id="backpage" value="./login.jsp"/>			
	   	<input type="hidden" name="doaction" id="doaction" value="login"/>
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
         <p class="txtLeft pdleft8">仅需输入账号密码，方便快捷; 无支付限额</p>
       </div>
	  <div class="guide pdleft">
          <table class="hs">
            <tr>
              <td width="14%" height="40" scope="col"><p class="txtRight">吉卡账户：</p></td>
              <td width="86%" height="40" scope="col"><label for="textfield2"></label>
              <input type="text" name="buyer_name" id="buyer_name" value="${m._sorder.buyer_name}" <c:if test="${m._sorder.buyer_name!=''}">readonly</c:if> class="ye_input" />
              </td>
            </tr>
            <tr>
              <td height="40"><p class="txtRight">支付密码：</p></td>
              <td height="40">
              	<!--<input type="password" name="buyer_paypwd" id="buyer_paypwd" onclick="showkeyboard(curEditName)"/>-->
              	<script type="text/javascript">IntPassGuardCtrl("_ocx_password","0","","","ye_input");</script>
              	<script type="text/javascript">SetPassGuardCtrl("_ocx_password",20,"","","");</script> 
              </td>
              </tr>
            <tr>
              <td><p class="txtRight">验证码：</p></td>
              <td><input type="text" class="ye_input_yzm" name="verify_code" id="verify_code"/>
              <a><img src=/captcha.do witdh=120 height=19 align="absmiddle" onclick="javascript:this.src='/captcha.do?'+Math.random();" style="cursor: hand;" alt="看不清楚?点击刷新验证码"/></a>
              </td>
            </tr>
            <tr>
            <td height="40">&nbsp;</td>
          </tr>
            <tr>
              <td height="40">&nbsp;</td>
              <td height="40">
              <input type="submit" name="button_isms" id="button_isms" value="下一步" class="zf_button" />
              </td>
            </tr>
          </table>
    </div>
</form>
</body>

<script>
 $(document).ready(function() {
            //jQuery.validator.addMethod("total_fee",function(a,b){return this.optional(b)||/^\d+(\.\d{0,2})?$/i.test(a)},"输入有效金额");                 
            $("#compositefm").validate({
            	submitHandler:function(form){
            		if(!FormSubmit()) return false;
					form.submit();
		        },    
                rules: {
		        	buyer_name:{required:true},  
                    verify_code:{required:true}                    
                },
                messages: {
                	buyer_name:{required:"<font color=red>请输入账户名称</font>"}, 
                    verify_code:{required:"<font color=red>请输入校验码</font>"}                                           
                }
            });
        });
</script>
