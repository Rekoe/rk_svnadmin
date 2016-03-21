<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
<#include "/template/admin/head.ftl"/>
<script type="text/javascript">
$(function() {
	$("#serverDialog").dialog({
		autoOpen: false,
		modal: true,
		width: 280,
		height: 420,
		position: ["center",20],
		buttons: {
			"OK": function() {
				$(this).dialog("close");
			}
		},
		close: function(event, ui) {
			var s="";
			$('#serverids option:selected').each(function(){
				s += "<input type='hidden' name='serverIds' value='" +$(this).val()+ "'/>";
			});
			$("#serverIdsContainer").empty().append(s);
		}
	});
	var servers_opened = false;
	$('#server').click(function(){
		if(!servers_opened) {
			$.get("${base}/admin/server/v_servers_edit",{"id":${obj.id}},function(s) {
				$(s).appendTo("#serverids");
			});
			servers_opened = true;
		}
		$('#serverDialog').dialog('open');
		return false;
	});	
});
function disservers(chk) {
	$("#allServer").val(chk);
	if(chk) {
		$("#serverids").addClass("disabled").attr("disabled","disabled").children().each(function(){$(this).removeAttr("selected")});
		$("#serverIdsContainer").empty();
	} else {
		$("#serverids").removeAttr("disabled").removeClass("disabled");
	}
}
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos"><@s.m "global.position"/>: <@s.m "cmsAdminGlobal.function"/> - <@s.m "global.add"/></div>
	<form class="ropt">
		<input type="submit" value="<@s.m "global.backToList"/>" onclick="this.form.action='list';" class="return-button"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<@p.form id="jvForm" action="update" labelWidth="12">
<input type="hidden" name="id" value=${obj.id}>
<@p.td width="50" colspan="1" label="cmsUser.username">${obj.name}</@p.td><@p.tr/>
<@p.td colspan="1" label="cmsRole.group">
<#list roleList as role>
<#assign isContain = false>
<#list obj.roles as urole>
	<#if role.id== urole.id>
		<#assign isContain = true>
		<#break>
	</#if>
</#list>
<label><input value="${role.id}" type="checkbox" name="roleIds" <#if isContain>checked="checked"</#if> />${role.description}</label><br />
</#list></@p.td><@p.tr/>
<@p.td colspan="1" label="cms.server.set" required="true">
	<input id="server" type="button" value="<@s.m "cms.server.select"/>"/>
	<input type="hidden" id="allServer" name="allServer" value="${allServer?string('true','false')}"/>
	<span id="serverIdsContainer">
		<#list serverList as cid>
			<#list obj.servers?keys as key>
				<#if key==cid.id>
				<input type="hidden" name="serverIds" value="${cid.id}"/>
				<#break>
				</#if>
			</#list>
		</#list>
		</span>
	<div id="serverDialog" title="<@s.m "cmsUser.channels"/><@s.m "cms.server.list"/>" style="display:none;">
		<label><input type="checkbox" onclick="disservers(this.checked)"<#if allServer> checked="checked"</#if>/><@s.m "cmsUser.servers.allserver"/></label>
		<select id="serverids"<#if allServer> disabled="disabled" class="disabled"</#if> multiple="multiple" size="15" style="width:100%;" name="serverids"></select>
	</div>
<div style="clear:both"></div>
</@p.td><@p.tr/>
<@p.td colspan="1"><@p.submit code="global.submit"/> &nbsp; <@p.reset code="global.reset"/></@p.td><@p.tr/>
</@p.form>
</div>
</body>
</html>