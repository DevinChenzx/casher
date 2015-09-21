<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common/tags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>铻嶅疂鏀堕摱鍙�</title>
<link href="/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/js/jquery/jquery-1.7.js"></script>
<script type="text/javascript" src="/js/jquery/jqstyle.js"></script>
</head>
<body>

<!--璁㈠崟璇︾粏-->
<div class="xiangqingbox" id="qipao" style="display:none;">
  <div class="guanbi"><A  onclick="" id="guanbi" class="pint" style="color:#03F">鍏抽棴</A></div>
  <div class="ico_jiantou"></div>
  <table>
    <tr>
      <th width="18%" scope="col">鍟嗗搧鍚嶇О</th>
      <th width="15%" scope="col">浜ゆ槗閲戦锛堝厓锛�</th>
      <th width="16%" scope="col">璐拱鏃堕棿</th>
      <th width="20%" scope="col">鏀惰揣鍦板潃</th>
      <th width="16%" scope="col">浜ゆ槗绫诲瀷</th>
      <th width="15%" scope="col">浜ゆ槗鍙�</th>
    </tr>
    <tr>
      <td scope="col">缁忓吀鏈ㄧ汗鍞ら啋淇濇姢濂�</td>
      <td scope="col" align="center"><strong style="color:#F00">6532.00</strong></td>
      <td scope="col">2011骞�11鏈�11鏃� 13:44:26</td>
      <td scope="col">钖涘織浼燂紙鏀讹級</td>
      <td scope="col"><span>鏀粯瀹濇媴淇濅氦鏄�</span></td>
      <td scope="col">101010</td>
    </tr>
  </table>
  <br />
  <p align="left"><A  style="color:#03F">浜ゆ槗璇存槑浜ゆ槗璇存槑浜ゆ槗璇存槑浜ゆ槗璇存槑浜ゆ槗璇存槑</A> </p>
</div>
<!--璁㈠崟璇︾粏缁撴潫-->
<div class="topbox">
  <div class="logo"><img src="/images/img/logo.gif" alt="铻嶅疂-鏀堕摱鍙癓OGO" width="306" height="87" /></div>
</div>
<div class="process">
  <div class="welcome">
    <p class="txtLeft pdleft">鎮ㄥソ铻嶅疂鐢ㄦ埛 锛氭杩庝娇鐢ㄦ敮浠樻敹閾跺彴!</p>
  </div>
  <div class="process2"></div>
</div>
<div class="zhifu">
  <table class="zhifutab">
    <tr>
      <th scope="col">璁㈠崟鍚嶇О </th>
      <th scope="col">鏀舵鏂�</th>
      <th class="red" scope="col">璁㈠崟閲戦</th>
    </tr>
    <tr>
      <td><div id="dwn"><c:choose><c:when test="${fn:length(m._sorder.subject)>15}">${fn:substring(m._sorder.subject,0,15)}...</c:when><c:otherwise>${m._sorder.subject}</c:otherwise></c:choose><A  onclick="" id="xiangqing" class="blue">璇︽儏</A></div></td>
      <td>${m._sorder.seller_name} ${m._sorder.seller_remarks}</td>
      <td class="red"><b><fmt:formatNumber value="${m._sorder.amount/100}" pattern="0.00"/></b> 鍏�</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
  </table>
</div>
<div class="tab">
  <dl>
    <div class="choose">
      <div class="fangshi">
        <p class="txtLeft pdleft">璇烽�夋嫨鎮ㄧ殑浠樻鏂瑰紡: </p>
      </div>
      <dt><a>鏀粯瀵艰埅</a><a>娑堣垂鍗�</a><a>铻嶅疂浣欓</a><a>閾惰鍗�</a><a>浼佷笟缃戦摱</a></dt>
    </div>
    <dd>
      <ul>
        <div class="guide pdleft">
          <p class="txtLeft pdleft8 font14 b"><a href="#">璇锋牴鎹偍鐨勬儏鍐甸�夋嫨鍚堥�傜殑鏀粯鏂瑰紡锛�</a></p>
          <p class="txtLeft pdleft8"><img src="/images/img/security_ico.png" width="13" height="15" />浠ヤ笅鎵�鏈夋敮浠樻柟寮忓潎鐢辫瀺瀹濇彁渚涖�佸畨鍏ㄣ�佷究鎹锋棤浠讳綍鎵嬬画璐广��</p>
          <table id="jstlb" class="hs">
            <tr>
              <th>鎮ㄧ殑鎯呭喌</th>
              <th>寤鸿鏀粯鏂瑰紡</th>
              <th>鐗圭偣</th>
            </tr>
            <tr>
              <td>娌℃湁寮�閫氱綉涓婇摱琛� </td>
              <td>璇峰厛<a href="#" class="blue">寮�閫氱綉涓婇摱琛�</a></td>
              <td><a href="#" class="blue">鏌ョ湅鏀寔鐨勭綉涓婇摱琛屽強閾惰鍗°�佷俊鐢ㄥ崱绫诲瀷</a></td>
            </tr>
            <tr>
              <td>鎸佹湁濡傛剰鍗°�佹剰褰㈠崱銆佸叾瀹冮浠樺崱</td>
              <td><input type="submit" name="button2" id="button2" class="btn_gid" value="棰勪粯璐瑰崱" /></td>
              <td>鍙渶杈撳叆鍗″彿瀵嗙爜锛屾柟渚垮揩鎹�</td>
            </tr>
            <tr>
              <td>鏈夎瀺瀹濊处鎴蜂綑棰�</td>
              <td><input type="submit" name="button3" id="button3" class="btn_gid"  value="铻嶅疂浣欓鏀粯" /></td>
              <td>浠呴渶杈撳叆璐﹀彿瀵嗙爜锛屾柟渚垮揩鎹�<br />
                鏃犳敮浠橀檺棰�</td>
            </tr>
            <tr>
              <td>鏈夐摱琛屽崱骞跺凡寮�閫氱綉涓婇摱琛�</td>
              <td><input type="submit" name="button4" id="button4" class="btn_gid"  value="閾惰鍗�" /></td>
              <td>鏀寔鍥藉唴澶у鏁伴摱琛�</td>
            </tr>
            <tr>
              <td>寮�閫氫紒涓氱綉閾剁殑浼佷笟璐︽埛</td>
              <td><input type="submit" name="button5" id="button5" class="btn_gid"  value="浼佷笟鏀粯 " /></td>
              <td>鏀寔鍥藉唴涓绘祦閾惰鐨勪紒涓氱綉閾�</td>
            </tr>
          </table>
          <p class="red txtLeft">璇锋牴鎹偍鐨勬儏鍐甸�夋嫨鍚堥�傜殑鏀粯鏂瑰紡</p>
          <p class="txtRight">娌℃湁寮�閫氱綉涓婇摱琛�  璇峰厛寮�閫氱綉涓婇摱琛�  鏌ョ湅鏀寔鐨勭綉涓婇摱琛屽強閾惰鍗°�佷俊鐢ㄥ崱绫诲瀷</p>
        </div>
      </ul>
      <ul>
        <div class="ord_bt">
          <p class=" txtRight">搴斾粯鎬讳环锛� </p>
        </div>
        <div class="ord_cnt">
          <p class="red b txtLeft pdleft8">24.00鍏�</p>
          <p class="txtLeft blue pdleft8"><a href="#">鐧诲綍鍚庡彲浠ヤ繚瀛樹氦鏄撹褰�</a></p>
        </div>
        <div class="ord_bt">
          <p class="font14">閫夋嫨鏀粯鍗＄被鍨嬶細 </p>
        </div>
        <div class="ord_cnt">
          <div class="SelectBank2 clearfix" id="linebank2">
          	<div class="morback red pint"><a href="#" id="card">鐐瑰嚮閫夋嫨鏇村閾惰</a></div>
            <li id="aa" class="Selected2">
              <input type="radio" name="pay_channel" id="B2C-CMB" value="CMB-cmb101" checked="" onclick="setBankName(&quot;bank_CMB&quot;)">
              <label for="B2C-CMB"> <a href="#"><img  src="/images/img/rycard.gif"width="52" height="51" border="0" onclick="setBankName(&quot;bank_CMB&quot;)"></a> </label>
            </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/hycard.gif"width="52" height="51"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              </div>
          </div>
       
        <div class="ord_bt">
          <p class="font14">閫夋嫨鏀粯鍗＄被鍨嬶細 </p>
        </div>
        <div class="ord_cnt">
        <p class="txtLeft pdleft8">
            <input type="submit" name="button" id="button" value="涓嬩竴姝�" class="zf_button" />
          </p>
        </div>
      </ul>
      <ul>
        <div class="guide pdleft">
          <table class="hs">
            <tr>
              <td width="14%" scope="col"><p class="txtRight">铻嶅疂璐︽埛锛�</p></td>
              <td width="86%" scope="col"><label for="textfield2"></label>
              <input type="text" name="textfield2" id="textfield2"  class="ye_input" /></td>
            </tr>
            <tr>
              <td><p class="txtRight">鏀粯瀵嗙爜锛�</p></td>
              <td><input type="text" name="textfield3" id="textfield3" class="ye_input" />
              <img src="/images/img/jp.gif" width="28" height="13" /></td>
            </tr>
            <tr>
              <td><p class="txtRight">楠岃瘉鐮侊細</p></td>
              <td><input type="text" class="ye_input_yzm" name="textfield4" id="textfield4" />
              <img src="/images/img/yzm.gif" width="58" height="24" /></td>
            </tr>
            <tr>
              <td height="81">&nbsp;</td>
              <td height="81">&nbsp;</td>
            </tr>
            <tr>
              <td height="81">&nbsp;</td>
              <td height="81"><input type="submit" name="button6" id="button6" value="涓嬩竴姝�" class="zf_button" /></td>
            </tr>
          </table>
        </div>
      </ul>
      <ul>
        <div class="ord_bt">
          <p class=" txtRight">搴斾粯鎬讳环锛� </p>
        </div>
        <div class="ord_cnt">
          <p class=" txtLeft pdleft8"><a class="red b">24.00鍏�</a><br />
			<a href="#" class="txtLeft blue ">鐧诲綍鍚庡彲浠ヤ繚瀛樹氦鏄撹褰�</a>
</p>
        </div>
        <div class="ord_bt">
          <p class="font14">閫夋嫨鏀粯閾惰锛� </p>
        </div>
        <div class="ord_cnt">
          <div class="SelectBank" id="linebank">
            <div class="morcd red pint"><a href="#" id="back">鐐瑰嚮閫夋嫨鏇村閾惰</a></div>
            <li class="Selected" id="bb">
              <input type="radio" name="pay_channel" id="B2C-CMB" value="CMB-cmb101" checked="" onclick="setBankName(&quot;bank_CMB&quot;)">
              <label for="B2C-CMB"> <a href="#"><img  src="/images/img/abc.png"width="127" height="40" border="0" onclick="setBankName(&quot;bank_CMB&quot;)"></a> </label>
            </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/icbc.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-CCB" value="CCB-ccb102" onclick="setBankName(&quot;bank_CCB&quot;)">
                <label for="B2C-CCB"> <a href="#"><img src="/images/img/ccb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_CCB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-BOC" value="BOC-boc101" onclick="setBankName(&quot;bank_BOC&quot;)">
                <label for="B2C-BOC"> <a href="#"><img src="/images/img/boc.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_BOC&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/cmb.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-ABC" value="ABC-abc101" onclick="setBankName(&quot;bank_ABC&quot;)">
                <label for="B2C-ABC"> <a href="#"><img src="/images/img/abc.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_ABC&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-COMM" value="COMM-comm101" onclick="setBankName(&quot;bank_COMM&quot;)">
                <label for="B2C-COMM"> <a href="#"><img src="/images/img/visa.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_COMM&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb101" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2C-SPDB"> <a href="#"><img src="/images/img/unionpay.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-GDB" value="GDB-gdb101" onclick="setBankName(&quot;bank_GDB&quot;)">
                <label for="B2C-GDB"> <a href="#"><img src="/images/img/spdb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_GDB&quot;)"></a> </label>
              </li>
            <li>
                <input type="radio" name="pay_channel" id="B2C-CITIC" value="CITIC-citic102" onclick="setBankName(&quot;bank_CITIC&quot;)">
                <label for="B2C-CITIC"> <a href="#"><img src="/images/img/shrcb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_CITIC&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-CEB" value="CEB-ceb101" onclick="setBankName(&quot;bank_CEB&quot;)">
                <label for="B2C-CEB"> <a href="#"><img src="/images/img/sdb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_CEB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-CIB" value="CIB-cib101" onclick="setBankName(&quot;bank_CIB&quot;)">
                <label for="B2C-CIB"> <a href="#"><img src="/images/img/psbc.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_CIB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SDB" value="SDB-sdb101" onclick="setBankName(&quot;bank_SDB&quot;)">
                <label for="B2C-SDB"> <a href="#"><img src="/images/img/pingan.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_SDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-CMBC" value="CMBC-cmbc101" onclick="setBankName(&quot;bank_CMBC&quot;)">
                <label for="B2C-CMBC"> <a href="#"><img src="/images/img/njcb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_CMBC&quot;)"></a> </label>
              </li>
        <li>
                <input type="radio" name="pay_channel" id="B2C-HZCB" value="HZCB-hzcb101" onclick="setBankName(&quot;bank_HZCB&quot;)">
                <label for="B2C-HZCB"><a href="#"><img src="/images/img/nbcb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_HZCB&quot;)" /></a></label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-BJRCB" value="BJRCB-bjrcb101" onclick="setBankName(&quot;bank_BJRCB&quot;)">
                <label for="B2C-BJRCB"> <a href="#"><img src="/images/img/master.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_BJRCB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-NBBANK" value="NBBANK-nbbank101" onclick="setBankName(&quot;bank_NBBANK&quot;)">
                <label for="B2C-NBBANK"> <a href="#"><img src="/images/img/jsbchina.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_NBBANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SHBANK" value="SHBANK-shbank101" onclick="setBankName(&quot;bank_SHBANK&quot;)">
                <label for="B2C-SHBANK"> <a href="#"><img src="/images/img/hxb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_SHBANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-FDB" value="FDB-fdb101" onclick="setBankName(&quot;bank_FDB&quot;)">
                <label for="B2C-FDB"> <a href="#"><img src="/images/img/hsbank.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_FDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-PSBC" value="PSBC-psbc102" onclick="setBankName(&quot;bank_PSBC&quot;)">
                <label for="B2C-PSBC"> <a href="#"><img src="/images/img/hccb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_PSBC&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-WZCB" value="WZCB-wzcb101" onclick="setBankName(&quot;bank_WZCB&quot;)">
                <label for="B2C-WZCB"> <a href="#"><img src="/images/img/gzcb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_WZCB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-BJBANK" value="BJBANK-bjbank101" onclick="setBankName(&quot;bank_BJBANK&quot;)">
                <label for="B2C-BJBANK"> <a href="#"><img src="/images/img/gdb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_BJBANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-ICBC" value="ICBC-icbc102" onclick="setBankName(&quot;bank_ICBC&quot;)">
                <label for="B2B-ICBC"> <a href="#"><img src="/images/img/ecitic.png"width="127" height="40"  alt="涓浗宸ュ晢閾惰" border="0" onclick="setBankName(&quot;bank_ICBC&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-CCB" value="CCB-ccb104" onclick="setBankName(&quot;bank_CCB&quot;)">
                <label for="B2B-CCB"> <a href="#"><img src="/images/img/czbank.png"width="127" height="40"  alt="涓浗寤鸿閾惰" border="0" onclick="setBankName(&quot;bank_CCB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-ABC" value="ABC-abc102" onclick="setBankName(&quot;bank_ABC&quot;)">
                <label for="B2B-ABC"> <a href="#"><img src="/images/img/cmbc.png"width="127" height="40"  alt="涓浗鍐滀笟閾惰" border="0" onclick="setBankName(&quot;bank_ABC&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/cib.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/cbhb.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/bos.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/bob.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/bjrcb.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/bankcomm.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/abc.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/961111.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
          </div>
          <div class="sxbankbox">
            <p class="txtLeft"><span>璇风‘淇濇偍宸茬粡鍦ㄩ摱琛屾煖鍙板紑閫氫簡缃戜笂鏀粯鍔熻兘锛屽惁鍒欏皢鏃犳硶鏀粯鎴愬姛銆�<a href="#" class="blue">濡備綍寮�閫�?</a></span><span><A  onclick="toggle()" id="ckxe" class="red pint" >鏌ョ湅闄愰</A></span></p>
            <table width="670" class="right_list_table" style="display:none"  id="xe" align="center">
              <tr>
                <th width="15%" scope="col"> 閾惰鍚嶇О </th>
                <th width="9%" scope="col">瀹㈡湇鐢佃瘽 </th>
                <th colspan="2" scope="col">闄愬埗閲戦</th>
                <th width="12%" scope="col">浜ゆ槗娆℃暟</th>
              </tr>
              <tr>
                <td rowspan="3">涓浗宸ュ晢閾惰</td>
                <td rowspan="3">95588</td>
                <td width="12%">鏌滃彴娉ㄥ唽锛�</td>
                <td width="45%">鍗曠瑪浜ゆ槗闄愰涓�300鍏冿紝鏃ョ疮璁￠檺棰濅负300锛屾�讳粯闄愰涓�300鍏�</td>
                <td rowspan="3">涓嶉檺</td>
              </tr>
              <tr>
                <td>鐢靛瓙閾惰锛�</td>
                <td>鍗曠瑪浜ゆ槗闄愰涓�300鍏冿紝鏃ョ疮璁￠檺棰濅负300锛屾�讳粯闄愰涓�300鍏�</td>
              </tr>
              <tr>
                <td>U璐ㄥ鎴�</td>
                <td>鍗曠瑪浜ゆ槗闄愰涓�300鍏冿紝鏃ョ疮璁￠檺棰濅负300锛屾�讳粯闄愰涓�300鍏�</td>
              </tr>
            </table>
          </div>
        </div>
        <div class="ord_bt">
          <p class="font14">閭鎴栨墜鏈哄彿锛�</p>
        </div>
        <div class="ord_cnt">
          <p class="txtLeft pdleft8">
            <label for="textfield"></label>
            <input type="text" name="textfield" id="textfield" class="youxiang" />
            <span class="red ">鏈浠樻鍑瘉灏嗗彂閫佸埌璇ラ偖绠辨垨鎵嬫満涓�</span></p>
        </div>
        <div class="ord_bt">
          <p class="font14">&nbsp;</p>
        </div>
        <div class="ord_cnt">
          <p class="txtLeft pdleft8">
            <input type="submit" name="button" id="button" value="涓嬩竴姝�" class="zf_button" />
          </p>
          
        </div>
      </ul>
   <ul>
        <div class="ord_bt">
          <p class=" txtRight">搴斾粯鎬讳环锛� </p>
        </div>
        <div class="ord_cnt">
          <p class=" txtLeft pdleft8"><a class="red b">24.00鍏�</a><br />
			<a href="#" class="txtLeft blue ">鐧诲綍鍚庡彲浠ヤ繚瀛樹氦鏄撹褰�</a>
</p>
        </div>
        <div class="ord_bt">
          <p class="font14">閫夋嫨鏀粯閾惰锛� </p>
        </div>
        <div class="ord_cnt">
      <div class="SelectBank clearfix" id="linebank2">
            	<li class="Selected">
              <input type="radio" name="pay_channel" id="B2C-CMB" value="CMB-cmb101" checked="" onclick="setBankName(&quot;bank_CMB&quot;)">
              <label for="B2C-CMB"> <a href="#"><img  src="/images/img/abc.png"width="127" height="40" border="0" onclick="setBankName(&quot;bank_CMB&quot;)"></a> </label>
            </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPABANK" value="SPABANK-spabank101" onclick="setBankName(&quot;bank_SPABANK&quot;)">
                <label for="B2C-SPABANK"> <a href="#"><img src="/images/img/icbc.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_SPABANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-CCB" value="CCB-ccb102" onclick="setBankName(&quot;bank_CCB&quot;)">
                <label for="B2C-CCB"> <a href="#"><img src="/images/img/ccb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_CCB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-BOC" value="BOC-boc101" onclick="setBankName(&quot;bank_BOC&quot;)">
                <label for="B2C-BOC"> <a href="#"><img src="/images/img/boc.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_BOC&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/cmb.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-ABC" value="ABC-abc101" onclick="setBankName(&quot;bank_ABC&quot;)">
                <label for="B2C-ABC"> <a href="#"><img src="/images/img/abc.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_ABC&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-COMM" value="COMM-comm101" onclick="setBankName(&quot;bank_COMM&quot;)">
                <label for="B2C-COMM"> <a href="#"><img src="/images/img/visa.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_COMM&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb101" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2C-SPDB"> <a href="#"><img src="/images/img/unionpay.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-GDB" value="GDB-gdb101" onclick="setBankName(&quot;bank_GDB&quot;)">
                <label for="B2C-GDB"> <a href="#"><img src="/images/img/spdb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_GDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-CITIC" value="CITIC-citic102" onclick="setBankName(&quot;bank_CITIC&quot;)">
                <label for="B2C-CITIC"> <a href="#"><img src="/images/img/shrcb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_CITIC&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-CEB" value="CEB-ceb101" onclick="setBankName(&quot;bank_CEB&quot;)">
                <label for="B2C-CEB"> <a href="#"><img src="/images/img/sdb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_CEB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-CIB" value="CIB-cib101" onclick="setBankName(&quot;bank_CIB&quot;)">
                <label for="B2C-CIB"> <a href="#"><img src="/images/img/psbc.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_CIB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SDB" value="SDB-sdb101" onclick="setBankName(&quot;bank_SDB&quot;)">
                <label for="B2C-SDB"> <a href="#"><img src="/images/img/pingan.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_SDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-CMBC" value="CMBC-cmbc101" onclick="setBankName(&quot;bank_CMBC&quot;)">
                <label for="B2C-CMBC"> <a href="#"><img src="/images/img/njcb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_CMBC&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-HZCB" value="HZCB-hzcb101" onclick="setBankName(&quot;bank_HZCB&quot;)">
                <label for="B2C-HZCB"><a href="#"><img src="/images/img/nbcb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_HZCB&quot;)" /></a></label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-BJRCB" value="BJRCB-bjrcb101" onclick="setBankName(&quot;bank_BJRCB&quot;)">
                <label for="B2C-BJRCB"> <a href="#"><img src="/images/img/master.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_BJRCB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-NBBANK" value="NBBANK-nbbank101" onclick="setBankName(&quot;bank_NBBANK&quot;)">
                <label for="B2C-NBBANK"> <a href="#"><img src="/images/img/jsbchina.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_NBBANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SHBANK" value="SHBANK-shbank101" onclick="setBankName(&quot;bank_SHBANK&quot;)">
                <label for="B2C-SHBANK"> <a href="#"><img src="/images/img/hxb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_SHBANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-FDB" value="FDB-fdb101" onclick="setBankName(&quot;bank_FDB&quot;)">
                <label for="B2C-FDB"> <a href="#"><img src="/images/img/hsbank.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_FDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-PSBC" value="PSBC-psbc102" onclick="setBankName(&quot;bank_PSBC&quot;)">
                <label for="B2C-PSBC"> <a href="#"><img src="/images/img/hccb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_PSBC&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-WZCB" value="WZCB-wzcb101" onclick="setBankName(&quot;bank_WZCB&quot;)">
                <label for="B2C-WZCB"> <a href="#"><img src="/images/img/gzcb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_WZCB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-BJBANK" value="BJBANK-bjbank101" onclick="setBankName(&quot;bank_BJBANK&quot;)">
                <label for="B2C-BJBANK"> <a href="#"><img src="/images/img/gdb.png"width="127" height="40"  border="0" onclick="setBankName(&quot;bank_BJBANK&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-ICBC" value="ICBC-icbc102" onclick="setBankName(&quot;bank_ICBC&quot;)">
                <label for="B2B-ICBC"> <a href="#"><img src="/images/img/ecitic.png"width="127" height="40"  alt="涓浗宸ュ晢閾惰" border="0" onclick="setBankName(&quot;bank_ICBC&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-CCB" value="CCB-ccb104" onclick="setBankName(&quot;bank_CCB&quot;)">
                <label for="B2B-CCB"> <a href="#"><img src="/images/img/czbank.png"width="127" height="40"  alt="涓浗寤鸿閾惰" border="0" onclick="setBankName(&quot;bank_CCB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-ABC" value="ABC-abc102" onclick="setBankName(&quot;bank_ABC&quot;)">
                <label for="B2B-ABC"> <a href="#"><img src="/images/img/cmbc.png"width="127" height="40"  alt="涓浗鍐滀笟閾惰" border="0" onclick="setBankName(&quot;bank_ABC&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/cib.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/cbhb.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/bos.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/bob.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/bjrcb.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/bankcomm.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/abc.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
              <li>
                <input type="radio" name="pay_channel" id="B2C-SPDB" value="SPDB-spdb102" onclick="setBankName(&quot;bank_SPDB&quot;)">
                <label for="B2B-SPDB"> <a href="#"><img src="/images/img/961111.png"width="127" height="40"  alt="涓婃捣娴︿笢鍙戝睍閾惰" border="0" onclick="setBankName(&quot;bank_SPDB&quot;)"></a> </label>
              </li>
          </div>
          <div class="sxbankbox">
            <p class="txtLeft"><span>璇风‘淇濇偍宸茬粡鍦ㄩ摱琛屾煖鍙板紑閫氫簡缃戜笂鏀粯鍔熻兘锛屽惁鍒欏皢鏃犳硶鏀粯鎴愬姛銆�<a href="#" class="blue">濡備綍寮�閫�?</a></span><span><A  onclick="toggle()" id="ckxe" class="red pint" >鏌ョ湅闄愰</A></span></p>
            <table width="670" class="right_list_table" style="display:none"  id="xe" align="center">
              <tr>
                <th width="15%" scope="col"> 閾惰鍚嶇О </th>
                <th width="9%" scope="col">瀹㈡湇鐢佃瘽 </th>
                <th colspan="2" scope="col">闄愬埗閲戦</th>
                <th width="12%" scope="col">浜ゆ槗娆℃暟</th>
              </tr>
              <tr>
                <td rowspan="3">涓浗宸ュ晢閾惰</td>
                <td rowspan="3">95588</td>
                <td width="12%">鏌滃彴娉ㄥ唽锛�</td>
                <td width="45%">鍗曠瑪浜ゆ槗闄愰涓�300鍏冿紝鏃ョ疮璁￠檺棰濅负300锛屾�讳粯闄愰涓�300鍏�</td>
                <td rowspan="3">涓嶉檺</td>
              </tr>
              <tr>
                <td>鐢靛瓙閾惰锛�</td>
                <td>鍗曠瑪浜ゆ槗闄愰涓�300鍏冿紝鏃ョ疮璁￠檺棰濅负300锛屾�讳粯闄愰涓�300鍏�</td>
              </tr>
              <tr>
                <td>U璐ㄥ鎴�</td>
                <td>鍗曠瑪浜ゆ槗闄愰涓�300鍏冿紝鏃ョ疮璁￠檺棰濅负300锛屾�讳粯闄愰涓�300鍏�</td>
              </tr>
            </table>
          </div>
        </div>
        <div class="ord_bt">
          <p class="font14">閭鎴栨墜鏈哄彿锛�</p>
        </div>
        <div class="ord_cnt">
          <p class="txtLeft pdleft8">
            <label for="textfield"></label>
            <input type="text" name="textfield" id="textfield" class="youxiang" />
            <span class="red ">鏈浠樻鍑瘉灏嗗彂閫佸埌璇ラ偖绠辨垨鎵嬫満涓�</span></p>
        </div>
        <div class="ord_bt">
          <p class="font14">&nbsp;</p>
        </div>
        <div class="ord_cnt">
          <p class="txtLeft pdleft8">
            <input type="submit" name="button" id="button" value="涓嬩竴姝�" class="zf_button" />
          </p>
        </div>
      </ul>
      
    </dd>
  </dl>
</div>
<div class="footbox">
	<p>鍏充簬铻嶅疂 | 缁忛攢鍟嗕綋绯� | 浣撻獙璁″垝 | 瀹樻柟鍗氬 | 璇氬緛鑻辨墠 | 鑱旂郴鎴戜滑 | International Business | About 吉卡</p>
    <p>铻嶅疂鐗堟潈</p>
</div>
</body>
</html>
