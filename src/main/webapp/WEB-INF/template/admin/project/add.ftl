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
	<div class="rpos"><@s.m "global.position"/>: 项目管理 - <@s.m "global.add"/></div>
	<form class="ropt">
		<input type="submit" value="<@s.m "global.backToList"/>" onclick="this.form.action='list.rk';" class="return-button"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<@p.form id="jvForm" action="o_save" labelWidth="12" onsubmit="return false;">
<@p.text width="30" label="项目" id="pj.pj" name="pj.pj" value="" maxlength="100" class="required" required="true"/>
<@p.td width="50" label="类型"  required="true">
	<@p.select id="pj.type" name="pj.type" value='http-mutil' list={"svn":"svn","http":"http","http-mutil":"http(多库)"} required="true"/>
</@p.td><@p.tr/>
<@p.text width="30" label="描述" id="pj.des" name="pj.des" value="" maxlength="100" class="required" required="true"/><@p.tr/>
<@p.td colspan="2">
<@p.submit code="global.submit" onclick="Cms.add();"/>
</@p.td><@p.tr/>
</@p.form>
</div>
</body>
</html>