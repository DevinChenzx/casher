// JavaScript Document
/*隔行换色*/
$(function(){
	$("#jstlb tr:even").addClass("tdOdd");
});   
/*选项卡*/

$(function(){
	var tabTitle = "div[class='tabsLabel clearFloat'] a";
	var tabContent = "#easyTab_1>.selectBankArea>div";
	var aa=$(tabContent);
	$(tabTitle + ":first").addClass("selected");
	$(tabContent).not(":first").hide();
	$(tabTitle).unbind("click").bind("click", function(){
		$(this).siblings("a").removeClass("selected").end().addClass("selected");
		var index = $(tabTitle).index( $(this) );
		$(tabContent).eq(index).siblings(tabContent).hide().end().fadeIn("slow");
   });
});
/*订单详情*/
$(document).ready(function(){
  $("#xiangqing").click(function(){
//  var p=$("#xiangqing").offset();
//  $("#qipao").css({"position":"absolute","top":""+(p.top-116)+"px","left":""+ (p.left-640)+"px","z-index":"2"});
  $("#qipao").toggle('slow');
  });
  
  
  
  $("#guanbi").click(function(){
  $("#qipao").fadeOut("slow");
  });
});
/*订单详情*/
$(document).ready(function(){
  $("#xiane").click(function(){
  var p=$("#xiane").position();
  var bankCode=$('input[name=acquireCode]').val();
  var paymentType=$('input[name=payment_type]').val();
  var bankname=$('input[name=ext_bankcode]').val();
  
  
	var allDivs = document.getElementsByTagName("table");
	for (var i = 0; i < allDivs.length; ++i)
	{
		var div = allDivs[i];
		if (div.getAttribute("ctrlshow") == "true")
		{
			div.style.display="none";
			if(div.id==bankCode+"_"+bankname+"_"+paymentType)
			{
				div.style.display="";
			}
		}			
	}  
	
  $("#xianelb").css({"position":"absolute","top":""+(p.top)+"px","left":""+ (p.left+200)+"px","z-index":"2"});
  $("#xianelb").toggle();
  
  });
  $("#guanbi").click(function(){
  $("#qipao").fadeOut("slow");
  });
});


/*一行列表样式*/
$(document).ready(function(){
  $("#dacf").click(function(){
  $("#divObj").toggle();
  });
});
$(document).ready(function(){
  $("#ckxe").click(function(){
  $("#xe").toggle();
  });
});
//$(document).ready(function(){
//  $("#xiangqing").click(function(){
//  var p=$("#xiangqing").offset();
//  $("#qipao1").toggle();
//  $("#qipao1").css({"left":"\""+p.top+"px\"","left":"\""+ p.left+"px\""});
//  });
//});
$(document).ready(function(){
  $("#back").click(function(){
  $("#backlist").toggle(200);
  });
});
/*易生卡控制*/
$ (document).ready(function(){
	$("#linebank2 li").not(".Selected2").addClass("hidden"); 
	});

$(document).ready(function(){
  $("#card").click(function(){
  $("#linebank2 li").toggle(200,function(){
	  document.getElementById("aa").style.display="";
	  });
  });
});
/*银行卡卡控制*/
$ (document).ready(function(){
	$("#linebank li").not(".Selected").addClass("hidden"); 
	});

$(document).ready(function(){
  $("#back").click(function(){
  $("#linebank li").toggle(200,function(){
	  document.getElementById("bb").style.display="";
	  });
  });
});

/*企业网银控制*/
$ (document).ready(function(){
	$("#linebank3 li").not(".Selected").addClass("hidden"); 
	});

$(document).ready(function(){
  $("#qyback").click(function(){
  $("#linebank3 li").toggle(200,function(){
	  document.getElementById("cc").style.display="";
	  });
  });
});





//银行选择
$(document).ready(function(){
                            try{
                                var Password_Edit=AlieditControl.getAliedit(document.paymentForm)[0];
                                if(document.getElementsByName('buyer_email_2')[0]&&Password_Edit){
                                    document.getElementsByName('buyer_email_2')[0].tabIndex="10";
                                    Password_Edit.tabIndex="11";
                                }
                            }catch(er){}
                        });
                                    
           
            
            function doSubmitForSpecialParter(){
            
                document.selbank.buyer_email.value = document.selbank.buyer_email_2.value;
                
                var nobanksel = true;
            
                    var abank_info=document.getElementsByTagName("input");
                    for(i=0;i<abank_info.length;i++){
                        if(abank_info[i].name=="pay_channel"){
                           if(abank_info[i].checked){
                           nobanksel=false;
                            break;
                           }
                        }		
                    }
                    if(nobanksel){
                        alert("请选择银行");
                        return false;
                    }
                     
                 try{
                    var Password_Edit=AlieditControl.getAliedit(document.selbank)[0];
                    document.selbank._seaside_gogo_.value = Password_Edit.ci1();
                    return true;
                  } catch(er) {
                  return true;
                  }
                    
            }
            
            function doSubmit2() {
                document.selbank.buyer_email.value = document.selbank.buyer_email_2.value;
                if(document.selbank.buyer_email.value==""||document.selbank.buyer_email.value=="支付宝会员请输入账户名"||document.selbank.buyer_email.value=="输入常用的邮箱或手机号码") {
                    alert("请输入您的email或支付宝账户。");
                    document.selbank.buyer_email_2.focus();
                    return false;
                }
                
                var nobanksel = true;
            
                    var abank_info=document.getElementsByTagName("input");
                    
                    for(i=0;i<abank_info.length;i++){
                        if(abank_info[i].name=="pay_channel"){
                           if(abank_info[i].checked){
                           nobanksel=false;
                            break;
                           }
                        }		
                    }
                    if(nobanksel){
                        alert("请选择银行");
                        return false;
                    }
                     try{		
                    var Password_Edit= AlieditControl.getAliedit(document.selbank)[0];
                    document.selbank._seaside_gogo_.value = Password_Edit.ci1();
                    return true;
                  } catch(er) {
                  return true;
                  }
                    
            }
			
