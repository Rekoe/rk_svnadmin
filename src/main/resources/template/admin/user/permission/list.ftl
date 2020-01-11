<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
<#include "/template/admin/head.ftl"/>
<script type="text/javascript">
function getTableForm() {
	return document.getElementById('tableForm');
}
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos"><@s.m "global.position"/>: <@s.m "cmsRole.function"/> - <@s.m "global.list"/></div>
	<@shiro.hasPermission name="system.role.permission">
	<form class="ropt">
		<input class="add" type="submit" value="<@s.m "global.add"/>" onclick="this.form.action='add';"/>
	</form>
	</@shiro.hasPermission>
	<div class="clear"></div>
</div>
<div class="body-box">
<form id="tableForm" method="post">
<input type="hidden" name="pageNumber" value="${pageNumber!}"/>
<@p.table value=obj;permission,i,has_next><#rt/>
	<@p.column title="ID" align="center">${permission.id}</@p.column><#t/>
	<@p.column title="名称" align="center">&nbsp;&nbsp;${permission.name}</@p.column><#t/>
	<@p.column title="描述" align="center">&nbsp;&nbsp;<@htmlCut s=permission.description len=50 append='...' /></@p.column><#t/>
	<@shiro.hasPermission name="system.permission:edit">
	<@p.column code="button.submit.opt" align="center">
		<a href="edit?id=${permission.id}" class="pn-opt"><@s.m "global.edit.role.permission"/></a><#rt/>
	</@p.column><#t/>
	</@shiro.hasPermission>
	<@shiro.hasPermission name="system.permission:delete">
	<@p.column code="button.submit.opt" align="center">
		<a href="edit?id=${permission.id}" class="pn-opt"><@s.m "global.edit.role.permission"/></a><#rt/>
	</@p.column><#t/>
	</@shiro.hasPermission>
</@p.table>
</form>
</div>
</body>
</html>