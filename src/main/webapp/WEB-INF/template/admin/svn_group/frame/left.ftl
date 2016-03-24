<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>cms-left</title>
<#include "/template/admin/head.ftl"/>
<script type="text/javascript">
    $(function(){
        Cms.lmenu('lmenu');
    });
</script>
<style>
h3{ padding:0; margin:0; font-weight:normal; font-size:12px;}
</style>
</head>
<body class="lbody">
<div class="left">
<#include "/template/admin/date.ftl"/>
	 <ul class="w-lful">
		<li><a href="${base}/admin/server/right" target="rightFrame"><@s.m "global.admin.index"/></a></li>
		<@perm_chow perm="svn.group">
		<li><a href="${base}/admin/svn/group/list.rk" target="rightFrame">svn组列表</a></li>
		</@perm_chow>
	 </ul>
</div>
</body>
</html>