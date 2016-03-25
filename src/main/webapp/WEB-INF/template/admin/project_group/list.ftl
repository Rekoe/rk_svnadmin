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
function deleted(pj,gr){
	$.dialog({
		type: "warn",
		content: '确定要删除此记录?',
		ok: 'Ok',
		cancel: 'Cancel',
		onOk: function() {
			$.ajax({
				url: "delete.rk",
				type: "POST",
				data: {"pj":pj,"gr":gr},
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					if (message.type == "success")
					{
						window.location.href = back;
					}
				}
			});
		}
	}); 
	return false;
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
<@p.table value=obj;group,i,has_next><#rt/>
	<@p.column title="ID" align="center">${i+1}</@p.column><#t/>
	<@p.column title="项目" align="center">${group.pj}</@p.column><#t/>
	<@p.column title="用户组" align="center">${group.gr}</@p.column><#t/>
	<@p.column title="描述" align="center">${group.des!}</@p.column><#t/>
	<@p.column title="设置用户" align="center">设置用户</@p.column><#t/>
	<@shiro.hasPermission name="project.group:delete">	
	<@p.column title="删除" align="center">
		<#if group.gr != "manager"><a href="javascript:void(0);" onclick="deleted('${group.pj}','${group.gr}')" class="pn-opt">删除</a></#if><#rt/>
	</@p.column><#t/>
	</@shiro.hasPermission>
</@p.table>
</@p.form>
</div>
</body>
</html>