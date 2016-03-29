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
<#assign isSend=obj.emailNotify>
<@p.form id="jvForm" action="update" labelWidth="12" onsubmit="return false;">
<@p.hidden id="conf.id" name="conf.id" value='${obj.id}' />
<@p.text width="30" label="仓库路径" id="conf.repositoryPath" name="conf.repositoryPath" value="${obj.repositoryPath}" maxlength="100" class="required" required="true"/><@p.tr/>
<@p.text width="30" label="访问url" id="conf.domainPath" name="conf.domainPath" value="${obj.domainPath}" maxlength="100" class="required" required="true"/><@p.tr/>
<@p.radio width="50" colspan="1" label="是否发送重置密码邮件" id="conf.emailNotify" name="conf.emailNotify" value=isSend list={"true":"global.true","false":"global.false"} required="true"/><@p.tr/>
<@p.td label="默认开启的文件夹" colspan="6">
<table border="0" id="attachTable">
<tr colspan="3">
	<td align="center"><input type="button" value="<@s.m 'system.add'/>" onclick="addAttachLine();" class="add"></td>
	<td align="center" colspan="2">默认开启的文件夹</td>
</tr>
<#list obj.dirs as item>
<#assign index = item_index>
<tr id="attachTr${index}">
	<td align="center"><a onclick="$('#attachTr${index}').remove();" href="javascript:void(0);" class="pn-opt"><@s.m "system.delete"/></a></td>
	<td align="center"><input type="text" id="conf.dirs[${index}]" name="conf.dirs[${index}]" value="${item}" /></td>
</tr>
</#list>
</table>
</@p.td><@p.tr/>
<textarea style="display:none" id="attachTr">
<tr id="attachTr{0}" colspan="3">
	<td align="center"><a onclick="$('#attachTr{0}').remove();" href="javascript:void(0);" class="pn-opt"><@s.m "system.delete"/></a></td>
	<td align="center" colspan="2"><input type="text" id="conf.dirs[{0}]" name="conf.dirs[{0}]"/></td>
</tr>
</textarea>
<script type="text/javascript">
var attachIndex =${obj.dirs?size};
var attachTpl = $.format($("#attachTr").val());
function addAttachLine() {
	$('#attachTable').append(attachTpl(attachIndex++));
}
</script>
<@p.td colspan="1"><@p.submit code="global.submit" onclick="Cms.updateAll('conf/update','conf.rk');"/> &nbsp; <@p.reset code="global.reset"/></@p.td><@p.tr/>
</@p.form>
</div>
</body>
</html>