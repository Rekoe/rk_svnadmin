<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
<#include "/template/admin/head.ftl"/>
<script type="text/javascript">
$(function() {
	$("#jvForm").validate();
	});
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos"><@s.m "global.position"/>: OauthUser - <@s.m "global.edit"/></div>
	<form class="ropt">
		<input type="submit" value="<@s.m "global.backToList"/>" onclick="this.form.action='list.rk';" class="return-button"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<@p.form id="jvForm" action="o_update" labelWidth="12" onsubmit="return false;">
<@p.hidden name="user.id" value='${obj.id}' />
<@p.text width="30" label="名称" id="user.userName" name="user.userName" value="${obj.userName!}" maxlength="100" class="required" required="true" readonly="true"/><@p.tr/>
<@p.td colspan="2">
<@p.submit code="global.submit" onclick="Cms.update();"/>
</@p.td><@p.tr/>
</@p.form>
</div>
</body>
</html>