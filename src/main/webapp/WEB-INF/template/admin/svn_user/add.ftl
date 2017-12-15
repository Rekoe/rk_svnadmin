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
	<div class="rpos"><@s.m "global.position"/>: SVN账号 - <@s.m "global.add"/></div>
	<form class="ropt">
		<input type="submit" value="<@s.m "global.backToList"/>" onclick="this.form.action='list.rk';" class="return-button"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<@p.form id="jvForm" action="o_save" labelWidth="12" onsubmit="return false;">
<@p.text width="30" label="姓名" id="user.name" name="user.name" value="" maxlength="100" class="required" required="true"/><@p.tr/>
<@p.text width="30" label="用户名" id="user.usr" name="user.usr" value="" maxlength="100" class="required" required="true"/><@p.tr/>
<@p.radio width="30" colspan="1" label="是否101办公室" name="user.local" value="false" list={"true":"global.true","false":"global.false"} required="true" help="是否101办公室"/><@p.tr/>
<@p.text width="30" label="邮箱地址" id="user.email" name="user.email" value="" maxlength="100" class="required" required="true"/><@p.tr/>
<@p.td width="50" label="角色"  required="true">
	<@p.select id="user.role" name="user.role" value='0' list={"guest":"选择角色","admin":"admin","small":"small"} required="true"/>
</@p.td><@p.tr/>
<@p.td colspan="2">
<@p.submit code="global.submit" onclick="Cms.add('o_save.rk','list.rk');"/>
</@p.td><@p.tr/>
</@p.form>
</div>
</body>
</html>