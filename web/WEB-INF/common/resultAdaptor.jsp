<%@ page language="java" pageEncoding="utf-8"%>
<body onload="javascript:document.toResult.submit();">
  <form name="toResult" method="post" action="/PayDispatch.do" target="_self">
    <input type="hidden" name="result_id" value="${infos}" />
  </form>
  <script language="javascript">
    <!--
    setTimeout('document.forms["toResult"].submit);',60000);
    -->
  </script>
</body>

