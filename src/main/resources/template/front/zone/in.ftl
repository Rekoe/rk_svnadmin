<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "//www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="//www.w3.org/1999/xhtml" xml:lang="en" lang="en" xmlns:fb="//www.facebook.com/2008/fbml">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Hero</title>
<script type="text/javascript">
	document.domain = "shanggame.net";
</script>
<script type="text/javascript" src="${base}/res/common/js/jquery.js"></script>
<script src="${base}/res/front/facebook/js/facebook.js?20140923" type="text/javascript"></script>
<script type="text/javascript" src="${base}/res/front/facebook/js/common.js?2010043004"></script>
<link rel="stylesheet" href="${base}/res/front/facebook/css/style.css?2015100814" />
<script type="text/javascript" src="${base}/res/front/facebook/js/full.js?10"></script>
<div id="fb-root"></div>
<script>
	  window.fbAsyncInit = function() {
	    FB.init({
	      appId      : '${appId}', // App ID from the App Dashboard
	      channelUrl : '', // Channel File for x-domain communication
	      status     : true, // check the login status upon init?
	      cookie     : true, // set sessions cookies to allow your server to access the session?
	      xfbml      : true,  // parse XFBML tags on this page?
	      version    : 'v2.0'
	    });
	  };
	  // Load the SDK's source Asynchronously
	  // Note that the debug version is being actively developed and might 
	  // contain some type checks that are overly strict. 
	  // Please report such bugs using the bugs tool.
	  (function(d, debug){
	     var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
	     if (d.getElementById(id)) {return;}
	     js = d.createElement('script'); js.id = id; js.async = true;
	     js.src = "//connect.facebook.net/en_US/sdk.js#version=v2.0&xfbml=1&appId=${appId}";
	     ref.parentNode.insertBefore(js, ref);
	   }(document, /*debug*/ false));
	function game_feed(picture, name, caption, description, shareid) {
		//postToFeed("https://apps.facebook.com/${namespace}/", picture, name, caption, description, shareid);
		postToFeed("${feed_upstream_url}", picture, name, caption, description, shareid);
	}
	function friendsList()
	{
		friendList('${user.id}');
	}
	var server_sid = ${zone.sid};
	$(function(){
		$("a[href^='mailto:']").on("click",function() {
		    window.top.location = $(this).prop("href");
		    return false;
		});
		$("#one5").bind("click",function(){
			flush_v(1);
		}); 
		$("#inputForm").submit();
	});
	function selectServer(sid) {
		window.onbeforeunload = function(e) {
			return;
		};
		if (server_sid == sid) {
			$(".header_tab li").eq(0).click();
		} else {
			$.ajax({
		        type: "POST",
		        contentType: "application/json",
		        dataType: "json",
		        url: '${base}/facebook/change/' + sid +'/'+${user.id},
		        success: function (data) {
			        if(data.ok)
					{
						$("#inputForm").attr("action", data.data.url+'/facebook/login/');
						$("#v").val(0);
						$("#inputForm").submit();
						$("#server_select").html('S'+data.data.sid+':'+data.data.name);
						server_sid = data.data.sid;
					}
					$(".header_tab li").eq(0).click();
		       }
			});
		}
	}
	function flush_v(v) {
		window.onbeforeunload = function(e) {
			return;
		};
		$("#v").val(v);
		$("#inputForm").submit();
	}
</script>
</head>
<body style="background:url(${base}/res/front/facebook/images/body_bg.jpg) repeat 0px 5px;">
<div class="oas_top">
      <div class="oastop_right">
          <div class="oastop_close"><img src="${base}/res/front/facebook/images/close.png" alt="close"></div>
          <div class="time">
                <span>Server time:</span>
          <p class="time_num"></p>
		 </div>
    </div>
    <script type="text/javascript">
    function Estime(){
            d=new Date(); //创建一个Date对象 
            localTime = d.getTime(); 
            localOffset=d.getTimezoneOffset()*60000; //获得当地时间偏移的毫秒数 
            utc = localTime + localOffset; //utc即GMT时间 
            offset = +17; //时区
            hawaii = utc + (3600000*offset); 
            nd = new Date(hawaii); 
            var Hours = nd.getHours();
            if(Hours<10){
                Hours = "0"+Hours;
            }
            var Minutes = nd.getMinutes();
            if(Minutes<10){
                Minutes = "0"+Minutes;
            }
            var Seconds = nd.getSeconds();
            if(Seconds<10){
                Seconds = "0"+Seconds;
            }
            $(".time_num").html(Hours+":"+Minutes+":"+Seconds);
            setTimeout("Estime()",1000);
    }
    Estime();		  
</script>
   </div>
	<div id='wrap' class='w980'>
		<div class='wrap_content'>
			<div id='mask'></div>
			<div class='tc_box' style='display:none;'>
				<a href='javascript:void(0);' class='close_c'><img src='${base}/res/front/facebook/images/close_c.png' border='0' /></a>
				<p style='margin-top:30px;'>
					<b>添加到收藏夹:</b><br /> 首页在Facebook 保护发现左侧边栏点击铅笔标记, 您可以添加游戏到您的收藏夹。然后我的最爱 您可以轻松地登录使用的防护收藏夹.
				</p>
				<p class='clearfix' style='padding-bottom:40px;'>
					<img src='${base}/res/front/facebook/images/tc_img01.jpg' border='0' />
					<img src='${base}/res/front/facebook/images/tc_img03.jpg' border='0' style='margin-top:190px;' />
					<img src='${base}/res/front/facebook/images/tc_img02.jpg' border='0' />
				</p>
			</div>
			<div class='wrap_header'>
				<div class='logo'>
					<img src='${base}/res/front/facebook/images/logo.png' border='0' />
				</div>
				<!-- <div style='z-index:9;'>
					<div id='invited_id' onclick="facebook_inviteFriend();"></div>
				</div> -->
				<ul class='header_tab clearfix'>
					<li id='one1' class='tab_bg_a'></li>
					<li id='one2' class='tab_bg_b'></li>
					<li id='one3' class='tab_bg_c' onclick="pay();return false;"></li>
					<li id="one4" onclick="window.open('https://www.facebook.com/mjfxtd')" class="tab_bg_d"></li>
					<!-- li id="one7" onclick="window.open('https://apps.facebook.com/${namespace}/faq/content.html')" class="tab_bg_e"></li -->
					<li id='one5' class='tab_bg_f'></li>
				</ul>
				<p class='fwq' id="server_select" style="cursor:pointer" title="(click to change server)">S${zone.sid}:${zone.name}</p>
				<div class='like'>
					<iframe src="//www.facebook.com/plugins/like.php?href=https%3A%2F%2Fwww.facebook.com%2Fherodefenseonline&amp;width&amp;layout=button_count&amp;action=like&amp;show_faces=true&amp;share=false&amp;height=21&amp;appId=${appId}" scrolling="no" frameborder="0" style="border:none; overflow:hidden; height:21px;" allowTransparency="true"></iframe>
				</div>
			</div>
			<div class='game_box' style='min-height:600px;'>
				<div id='con_one_1' class='con_main m1'>
					<div id="content_game" style='width:100%;height:100%;'>
						<iframe allowtransparency="true" frameborder="0" name="gameiframe" id="gameiframe" src="javascript:''" scrolling="no" style='background:none;'></iframe>
						<form id="inputForm" method=post action="${zone.url}/facebook/login/" target="gameiframe">
						    <input type="hidden" name="signed_request" value="${code}">
						    <input type="hidden" name="ads" value="${ads!}">
						</form>
					</div>
				</div>
				<div id='con_one_2' class='con_main m2'>
					<div class="service_list" id="service_list">
					<a class="close_to_game" href="javascript:void(0);" style="position:absolute;right:10px;top:10px;"><img src="${base}/res/front/facebook/images/close.png" border="0"></a>
						<div class="list_main clearfix">
							<div class="list_msg">
								<p><strong>UserName : </strong>${user.name}</p>
								<p><strong>UID : </strong>${user.id}</p>
								<h3>Last Login Server</h3>
								<ul>
								 	<li onclick="selectServer(${userHistory.sid})">S${userHistory.sid}: <@sid_name sid=userHistory.sid pid=1002 /></li>
								</ul>
							</div>
							<div class="list_content">
								<p>Recommend :</p>
								<ul class="clearfix">
									<li onclick="selectServer(${top.sid})">S${top.sid}: ${top.name }</li>
								</ul>
								<div class="list_content_main">
									<ul class="clearfix">
										<#list zones as zone>
									 	<li onclick="selectServer(${zone.sid})">S${zone.sid}: ${zone.name}</li>
				                        </#list>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class='user_title'>
				<em>UID:${user.id}</em>
				<span><b><font color="#000000">Copyright © 2014 Top1Play. All Rights Reserved.</font></b></span><span align="right"><a href="https://apps.facebook.com/${namespace}/content/privacy.html" target="_blank">Privacy</a> | <a href="https://apps.facebook.com/${namespace}/content/terms.html" target="_blank">Terms</a> | <a href="mailto:cs@top1play.com">Support</a></span>
			</div>
		</div>
		<div class='wrap_bottom'></div>
	</div>
</body>
</html>