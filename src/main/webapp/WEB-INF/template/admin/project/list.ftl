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
function init(pj){
	$.dialog({
		type: "warn",
		content: '确定要创建模板目录?',
		ok: 'Ok',
		cancel: 'Cancel',
		onOk: function() {
			$.ajax({
				url: "init.rk",
				type: "POST",
				data: {"pj":pj},
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					if (message.type == "success")
					{
						window.location.href = "list.rk"
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
	<div class="rpos"><@s.m "global.position"/>: 项目 - <@s.m "global.list"/></div>
	<form class="ropt">
		<input type="submit" class="add" value="<@s.m "global.add"/>" onclick="this.form.action='add';"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<@p.form id="tableForm" method="post">
<@p.hidden name="pageNumber" value="${pageNo!}" />
<@p.table value=obj;project,i,has_next><#rt/>
	<@p.column title="ID" align="center">${i+1}</@p.column><#t/>
	<@p.column title="姓名" align="center">${project.pj}</@p.column><#t/>
	<@p.column title="类型" align="center">${project.type}</@p.column><#t/>
	<@p.column title="描述" align="center">${project.des}</@p.column><#t/>
	<@shiro.hasPermission name="project.group">	
	<@p.column title="设置用户组" align="center"><a href="${base}/admin/project/group/list?pj=${project.pj}">设置用户组</a></@p.column><#t/>
	</@shiro.hasPermission>
	<@p.column title="设置权限" align="center"><a href="rep?pj=${project.pj}">设置权限</a></@p.column><#t/>
	<@p.column title="初始化" align="center"><a href="onclick="javascript:void(0);" init('${project.pj}')">初始化</a></@p.column><#t/>
	<@shiro.hasPermission name="svn.project:auth.manager">	
	<@p.column title="编辑" align="center">
		<a href="javascript:void(0);" onclick="Cms.deleted('${project.pj}')" class="pn-opt">删除</a><#rt/>
	</@p.column><#t/>
	</@shiro.hasPermission>
</@p.table>
</@p.form>
</div>
</body>
</html>