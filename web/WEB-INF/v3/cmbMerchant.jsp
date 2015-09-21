<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common/tags.jsp"%>
<%@ include file="/common/style.jsp"%>
<html>
<head>
    <title>招商银行企业版</title>
    <script type="text/javascript">
        function trim(s)
		{
			return s.replace(/(^\s*)|(\s*$)/g, "");
		}
        function doConfirm()
        {
        	var merNo = document.getElementById("merchantNo").value;
        	if(trim(merNo)=="")
        	{
        		alert("请输入客户编号!");
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
    <table border="1" width="100%" height="60">
      <tr>
	    <td align="right" width="50%">支付银行：</td>
	    <td align="left" width="50%"><img src=""/></td>
      </tr>
      <tr>
        <td align="right" width="50%">客户编号：</td>
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