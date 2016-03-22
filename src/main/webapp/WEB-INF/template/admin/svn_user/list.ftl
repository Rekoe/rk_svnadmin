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
	<div class="rpos"><@s.m "global.position"/>: OauthUser - <@s.m "global.list"/></div>
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
	<@p.column title="姓名" align="center">${user.name}</@p.column><#t/>
	<@p.column title="名称" align="center">${user.usr}</@p.column><#t/>
	<@p.column title="email" align="center">${user.email!}</@p.column><#t/>
	<@shiro.hasPermission name="svn.user:edit">	
	<@p.column title="编辑" align="center">
		<a href="edit.rk?id=${user.usr}" class="pn-opt">编辑</a><#rt/>
	</@p.column><#t/>
	</@shiro.hasPermission>
</@p.table>
</@p.form>
</div>
</body>
</html>