<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
<#include "/template/admin/head.ftl"/>
</head>
<body>
<div class="box-positon">
	<div class="rpos"><@s.m "global.position"/>: <@s.m "cmsAdminGlobal.function"/> - <@s.m "global.add"/></div>
	<div class="clear"></div>
</div>
<div class="body-box">
<@p.form id="jvForm" action="update" labelWidth="12" onsubmit="return false;">
<@p.hidden id="conf.id" name="conf.id" value='${obj.id}' />
<@p.text width="30" label="仓库路径" id="conf.repositoryPath" name="conf.repositoryPath" value="${obj.repositoryPath}" maxlength="100" class="required" required="true"/><@p.tr/>
<@p.text width="30" label="访问url" id="conf.domainPath" name="conf.domainPath" value="${obj.domainPath}" maxlength="100" class="required" required="true"/><@p.tr/>
<@p.td colspan="1"><@p.submit code="global.submit" onclick="Cms.updateAll('conf/update','conf.rk');"/> &nbsp; <@p.reset code="global.reset"/></@p.td><@p.tr/>
</@p.form>
</div>
</body>
</html>