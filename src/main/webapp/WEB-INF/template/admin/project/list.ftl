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
	<div class="rpos"><@s.m "global.position"/>: 项目 - <@s.m "global.list"/></div>
	<form class="ropt">
		<input type="submit" class="add" value="<@s.m "global.add"/>" onclick="this.form.action='add';"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<@p.form id="tableForm" method="post">
<@p.hidden name="pageNumber" value="${pageNo!}" />
<@p.table value=obj;user,i,has_next><#rt/>
	<@p.column title="ID" align="center">${i+1}</@p.column><#t/>
	<@p.column title="姓名" align="center">${user.pj}</@p.column><#t/>
	<@p.column title="路径" align="center">${user.path}</@p.column><#t/>
	<@p.column title="URL" align="center"><a href="rep?pj=${user.pj}">${user.url}</a></@p.column><#t/>
	<@p.column title="类型" align="center">${user.type}</@p.column><#t/>
	<@p.column title="描述" align="center">${user.des}</@p.column><#t/>
	<@p.column title="设置用户组" align="center">设置用户组</@p.column><#t/>
	<@p.column title="设置权限" align="center">设置权限</@p.column><#t/>
	<@shiro.hasPermission name="svn.user:edit">	
	<@p.column title="编辑" align="center">
		<a href="delete.rk?id=${user.pj}" class="pn-opt">删除</a><#rt/>
	</@p.column><#t/>
	</@shiro.hasPermission>
</@p.table>
</@p.form>
</div>
</body>
</html>