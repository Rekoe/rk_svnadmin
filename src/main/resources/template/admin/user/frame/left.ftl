<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>GM-ManagerSystem</title>
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
     <ul class="w-lful"><li><a href="right.rk" target="rightFrame"><@s.m "global.admin.index"/></a></li></ul>
	<@perm_chow perm="system.user">
	<h3>账号管理</h3>
	<div style="margin:0; padding:0;">
		<div class="leftmenuBG"><a href="${base}/admin/user/list" target="rightFrame"><@s.m "cmsAdminGlobal.function"/></a></div>
		<div class="leftmenuBG"><a href="${base}/admin/user/add_user" target="rightFrame"><@s.m "cms.function.add.user"/></a></div>
	</div>
	</@perm_chow>
	<@perm_chow perm="system.role">
	<ul class="w-lful"><li><a href="${base}/admin/role/list" target="rightFrame"><@s.m "cmsRole.function"/></a></li></ul>
   </@perm_chow>
	<@perm_chow perm="system.permission">
	<h3>权限管理</h3>
	<div style="margin:0; padding:0;">
		<div class="leftmenuBG"><a href="${base}/admin/permission/list" target="rightFrame">权限列表</a></div>
		<div class="leftmenuBG"><a href="${base}/admin/permission/category/list" target="rightFrame"><@s.m "admin.main.permissionCategory"/></a></div>
	</div>
	</@perm_chow>
</div>
</body>
</html>