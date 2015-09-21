<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common/tags.jsp"%>
<%@ include file="/common/style.jsp"%>
<html>
<head>
    <script type="text/JavaScript">
        function trim(s)
		{
			return s.replace(/(^\s*)|(\s*$)/g, "");
		}
        function doConfirm()
        {
        	var merNo = document.getElementById("merchantNo").value;
        	var bankName = document.getElementById("bankName").value;
        	var sMsg = "";
        	if(bankName=="CMB_B2B"){
        		sMsg = "请输入客户编号!";
        	}else if(bankName=="BOCM_B2B"){
        		sMsg = "请输入付款账号!";
        	} 
        	if(trim(merNo)=="")
        	{
        		alert(sMsg);
        		return false;
        	}else{
        		window.parent.document.getElementById("cmbMerNo").value=trim(merNo);
        		window.parent.document.forms[0].submit();
        		winClose();
        	}
        }        
        function winClose()
        {
            window.parent.win1.close();
        }
    </script>
  </head>
<body>
	<input type="hidden" id="bankName" name="bankName" value="${m.bankName}"/>
    <table border="1" width="100%" height="60">
      <tr>
        <td id="labelName" align="right" width="50%">${m.labelName}</td>
        <td align="left" width="50%"><input type="text" id="merchantNo" name="merchantNo" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"/></td>
      </tr>
      <tr>
        <td colspan="2" align="center">
          <span><input type="button" onclick="doConfirm()" value="确定"></span>
          <span><input type="button" onclick="winClose()"  value="关闭"></span>
        </td>
      </tr>
    </table>
</body>
</html>
<script type="text/JavaScript" language="JavaScript">
	document.getElementById("merchantNo").focus();
</script>