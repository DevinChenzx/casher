<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common/style.jsp"%>
<%@ include file="/common/tags.jsp"%>

<script language="javascript" src="/js/jquery/jquery-1.4.4.js"></script>
<script language="javascript" src="/js/jquery/jquery.validate.min.js"></script>



<style type="text/css">

.sj_inpt{

	border:solid 1px #d2d2d2; width:180px; line-height:18px; height:18px; float:left;

}
.zjfk{
	background: url(/images/syt_dlyhfk.gif); width:144px; height:26px; line-height:26px; text-align:center; color:#fff; border:none;
}


</style>

<body>

<div class="main">

  <%@ include file="/common/head.jsp"%>

  <div class="syt_content">

    <div  style="background: url(images/zfht_h1.gif) repeat-x; border-bottom:solid 1px #d2d2d2; height:31px; float:left; width:928px; font-size:14px;padding-left:20px; line-height:31px; margin-bottom:10px; color:#fff; font-weight:bold;">您正在使用直接付款交易</div>
    <form id="sub" name="sub" action="/urlPay.do" method="post">
    <input type="hidden" name="input_charset" value="utf-8"/>
    <table width="80%" border="0" cellspacing="0" cellpadding="0" style="padding:5px; float:left;">    
                  <tr>

                    <td width="19%" height="40" align="right">收款客户号:</td>

                    <td width="44%" style="padding:5px;"><input type="text" id="partner" name="partner" value="" class="sj_inpt"/></td>

                     <td width="52%" rowspan="5"><p><strong>尊敬的客户：</strong></p>

                      <p>  为了防止短信、电话、邮件和假网站等诈骗活动，保护您账户资金安全，特别提示如下：<br />

                        1.本通道为直接付款通道,请勿给陌生人付款;<br />
                        
                        2.对外联系邮箱：rongpayservice@gigold.com<br/>
                        
                        3.请务必与收款客户确认好订单和货款后，再付款
                      </p>
                    <br /></td>


                  </tr>

                  <tr>

                    <td height="40" align="right">收款客户邮箱:</td>

                    <td style="padding:5px;"><input type="text" id="seller_email" name="seller_email" value="" class="sj_inpt"/></td>

                    <td>&nbsp;</td>

                  </tr>

                  <tr>

                    <td height="40" align="right">付款金额(元):</td>

                    <td style="padding:5px;"><input type="text" id="total_fee" name="total_fee" value="" class="sj_inpt"/></td>

                    <td>&nbsp;</td>

                  </tr>

                  <tr>

                    <td height="40" align="right">付款说明:</td>

                    <td style="padding:5px;"><input type="text" id="subject" name="subject" value="" class="sj_inpt"/></td>

                    <td>&nbsp;</td>

                  </tr>

                  <tr>

                    <td height="40" align="right">商品描述:</td>

                    <td style="padding:5px;"><input type="text" id="body" name="body" value="" class="sj_inpt"/></td>

                    <td>&nbsp;</td>

                  </tr>

                  <tr>

                    <td height="40">&nbsp;</td>

                    <td style="padding:5px;">
                   <input type="submit" name="buttons" id="buttons" value="确认付款" class="zjfk" />
                   
                     </td>

                    <td>&nbsp;</td>

                  </tr>

    </table>
   
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
  </div>
</form>
 <%@ include file="/common/tail.jsp"%>

</body>

</html>
