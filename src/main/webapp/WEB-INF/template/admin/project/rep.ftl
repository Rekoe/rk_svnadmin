<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
<script src="${base}/resources/jquery-1.7.min.js" type="text/javascript"></script>
<span style="color:green;font-weight:bold;"><a href="${base}/pj">项目管理(${pj})</a>　|　权限管理</span><br><br>
<script type="text/javascript" src="${base}/resources/treeview/treeview.js"></script>
<link rel="stylesheet" href="${base}/resources/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="${base}/resources/treeview/treeview.css"></link>
<script src="${base}/resources/svnadmin.js"></script>
<style>
#svnroot li{
	line-height: 11px;
}
#svnroot span{
	line-height: 11px;
}
</style>
<script type="text/javascript">
AjaxTreeView.config.onclick=function(o,a){
	var p=o.getAttribute("param");
	if(p==null)p="";
	var url="pjauth";
	if(url!=""){
	  if(p!=""){
		  if(url.indexOf("?")>0){
		  	url=url+"&"+p;
		  }else{
		  	url=url+"?"+p;
		  }
	   }
	   //alert(url);
	   go(url,"pjauthWindow");
	   return false;
	}
};
$(document).ready(function (){
	AjaxTreeView.open(document.getElementById("svnroot"));
	$('#path').bind('keyup', function(event){
	   if (event.keyCode=="13"){
		   freshTree();
	   }
	});
});
function freshTree(){
	var $p = $("#path");
	var p = $p.val();
	if(p==""){
		//p="/";
		$p.val(p);
	}else if(p.substring(0,1)!="/"){
		//p = "/"+p;
		$p.val(p);
	}
	var $r = $("#svnroot");
	$r.children("ul").first().remove();
	$("#rootlink").text("${root}"+p);
	AjaxTreeView.close($r[0]);
	$r.attr("param","pj=${pj}&path="+p);
	$r[0].loading = false;
	$r[0].loaded = false;
	AjaxTreeView.open($r[0]);
}
</script>
<table style="width:100%;height:100%;" class="table table-striped table-bordered">
	<tr>
		<td valign="top" style="width:300px;">
			<input type="text" style="width:210px;" id="path" value="${path}">　<input onclick="freshTree();" class="btn btn-primary" type="button" value="刷新">
			<div class="filetree treeview" style="width:300px;height:500px;overflow: auto;">
				<ul>
					<li id="svnroot" class="closed lastclosed" treeId="rep" param="pj=${pj}&path=${path}">
						<div class="hit closed-hit lastclosed-hit" onclick='$att(this);'></div>
						<span class="folder" onclick='$att(this);'>
						<a id="rootlink" href='javascript:void(0);' onclick='$atc(this)'>${root}${path}</a>
						</span>
					</li>
				</ul>
			</div>
		</td>
		<td valign="top" height="100%">
			<iframe height="100%" width="100%" style="border:0px;" frameBorder="0" name="pjauthWindow" src="pjauth?pj=${pj}"></iframe>
		</td>
	</tr>
</table>
</body>
</html>