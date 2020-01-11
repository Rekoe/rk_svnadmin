<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
<#include "/template/admin/head.ftl"/>
<script type="text/javascript">
	$().ready(function(){
    function redirectUrl(){
        window.location.href = "list.action"
    }
})
function getTableForm() {
	return document.getElementById('tableForm');
}

function lockUser(id,isLocked) {
	$.dialog({
		type: "warn",
		content: '<@s.m "admin.dialog.updateConfirm"/>',
		ok: '<@s.m "admin.dialog.ok"/>',
		cancel: '<@s.m "admin.dialog.cancel"/>',
		onOk: function() {
			$.ajax({
				url: "lock.rk",
				type: "POST",
				data: {"id":id},
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
}
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos"><@s.m "global.position"/>: <@s.m "cmsAdminGlobal.function"/> - <@s.m "global.list"/></div>
	<div class="clear"></div>
</div>
<div class="body-box">
<form id="tableForm" method="post">
<input type="hidden" name="pageNumber" value="${pageNo!}"/>
<@p.table value=obj;cmsUser,i,has_next><#rt/>
	<@p.column title="ID" align="center">${i+1}</@p.column><#t/>
	<@p.column code="login.username" align="center">${cmsUser.name}</@p.column><#t/>
	<@p.column code="user.lastlogintime" align="center">${cmsUser.createDate?string('yyyy-MM-dd HH:mm:ss')}</@p.column><#t/>
	<@p.column code="user.lastloginip" align="center">${cmsUser.registerIp}</@p.column><#t/>
	<@shiro.hasPermission name="system.user.update">
	<@p.column code="user.lock" align="center"><div id="lock_${cmsUser.id}"><#if cmsUser.locked><span style="color:red"><@s.m "global.true"/></span><#else><@s.m "global.false"/></#if></div></@p.column><#t/>
	<@p.column code="global.operate" align="center">
		<a href="edit?id=${cmsUser.id}" class="pn-opt"><@s.m "global.edit"/></a> <#rt/>
		<a href="javascript:void(0)" onclick="lockUser('${cmsUser.id}','<#if cmsUser.locked>true<#else>false</#if>')" class="pn-opt"><#if cmsUser.locked><span style="color:red"><@s.m "user.unlock"/></span><#else><@s.m "user.lock"/></#if></a><#t/>
	</@p.column><#t/>
	</@shiro.hasPermission>
</@p.table>
</form>
</div>
</body>
</html>