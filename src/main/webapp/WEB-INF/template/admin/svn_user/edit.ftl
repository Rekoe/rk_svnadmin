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
<@p.hidden name="usr" value='${obj.usr}' />
<@p.text width="30" label="密码" id="pwd" name="pwd" value='' maxlength="100" class="required" required="true"/><@p.tr/>
<@p.radio width="30" colspan="1" label="是否101办公室" name="user.local" value=obj.local list={"true":"global.true","false":"global.false"} required="true" help="是否101办公室"/><@p.tr/>
<@p.td width="50" label="角色"  required="true">
	<@p.select id="user.role" name="role" value='${obj.role}' list={"guest":"选择角色","admin":"admin","small":"small"} required="true"/>
</@p.td><@p.tr/>
<@p.td colspan="2">
<@p.submit code="global.submit" onclick="Cms.update();"/>
</@p.td><@p.tr/>
</@p.form>
</div>
</body>
</html>