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
	<div class="rpos"><@s.m "global.position"/>: 目录管理 - <@s.m "global.add"/></div>
	<form class="ropt">
		<input type="submit" value="<@s.m "global.backToList"/>" onclick="this.form.action='list.rk';" class="return-button"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<@p.form id="jvForm" action="file_save" labelWidth="12" onsubmit="return false;">
<@p.hidden name="pj" id="pj" value='${obj}' />
<@p.text width="30" label="目录" id="file" name="file" value="" maxlength="100" class="required" required="true" help="说明: 若trunk下如果要增加目录请使用 trunk/filename"/><@p.tr/>
<@p.td>
<@p.submit code="global.submit" onclick="Cms.add('file_save','file_add.rk?pj=${obj}');"/>
</@p.td><@p.tr/>
</@p.form>
</div>
</body>
</html>