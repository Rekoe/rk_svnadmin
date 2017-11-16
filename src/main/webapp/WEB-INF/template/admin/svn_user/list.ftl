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
function rest(usr){
	$.dialog({
		type: "warn",
		content: '确定要重置用户密码?',
		ok: 'Ok',
		cancel: 'Cancel',
		onOk: function() {
			$.ajax({
				url: "restpwd.rk",
				type: "POST",
				data: {"usr":usr},
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					if (message.type == "success")
					{
						window.location.href = list.rk;
					}
				}
			});
		}
	}); 
	return false;
}

function lock(usr,lock){
	var rl = 'lock.rk';
	var text =  '确定要锁定此账号?';
	if(lock==1){
		text =  '确定要解锁此账号?';
		rl = 'unlock.rk';
	}
	$.dialog({
		type: "warn",
		content:	text,
		ok: 'Ok',
		cancel: 'Cancel',
		onOk: function() {
			$.ajax({
				url: rl,
				type: "POST",
				data: {"usr":usr},
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					if (message.type == "success")
					{
						window.location.href = "${action}?pageNumber=${page}";
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
<@p.table value=obj;user,i,has_next><#rt/>
	<@p.column title="ID" align="center">${i+1}</@p.column><#t/>
	<@p.column title="姓名" align="center">${user.name}</@p.column><#t/>
	<@p.column title="名称" align="center">${user.usr}</@p.column><#t/>
	<@p.column title="email" align="center">${user.email!}</@p.column><#t/>
	<@shiro.hasPermission name="svn.user:edit">	
	<@p.column title="编辑" align="center">
		<a href="edit.rk?id=${user.usr}" class="pn-opt">编辑</a><#rt/>
	</@p.column><#t/>
	<@p.column title="密码重置" align="center">
		<a href="javascript:void(0);" onclick="rest('${user.usr}')" class="pn-opt">密码重置</a><#rt/>
	</@p.column><#t/>
	<@p.column title="账号锁定" align="center">
		<#if lock==0>
				<a href="javascript:void(0);" onclick="lock('${user.usr}','${lock}')" class="pn-opt">账号锁定</a><#rt/>
			<#else>
				<a href="javascript:void(0);" onclick="lock('${user.usr}','${lock}')" class="pn-opt">账号解锁</a><#rt/>
		</#if>
	</@p.column><#t/>
	</@shiro.hasPermission>
</@p.table>
</@p.form>
</div>
</body>
</html>