<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
<#include "/template/admin/head.ftl"/>
<script type="text/javascript">
$(function() {
	$("#jvForm").validate();
    //移到右边
    $('#add').click(function() {
           //获取选中的选项，删除并追加给对方
           $('#select1 option:selected').appendTo('#select2');
    });
    //移到左边
    $('#remove').click(function() {
           $('#select2 option:selected').appendTo('#select1');
    });
    //全部移到右边
    $('#add_all').click(function() {
           //获取全部的选项,删除并追加给对方
           $('#select1 option').appendTo('#select2');
    });
    //全部移到左边
    $('#remove_all').click(function() {
           $('#select2 option').appendTo('#select1');
    });
    //双击选项
    $('#select1').dblclick(function(){     //绑定双击事件
           //获取全部的选项,删除并追加给对方
           $("option:selected",this).appendTo('#select2');     //追加给对方
    });
    //双击选项
    $('#select2').dblclick(function(){
           $("option:selected",this).appendTo('#select1');
    });
});
function checkForm(f){
    var x = f.elements["usrs"];
	if(x.value==""){
		alert("用户不可以为空");
		f.elements["usrs"].focus();
		return false;
	}
	var s="";
	for (var i=0;i<x.length;i++)
	{
	  s += x[i].value;
	  s += ",";
	} 
	$.dialog({
		type: "warn",
		content: '确定要添加此记录?',
		ok: 'Ok',
		cancel: 'Cancel',
		onOk: function() {
			$.ajax({
				url: "o_save.rk",
				type: "POST",
				data: {"pj":'${pj}',"gr":'${gr}',"usrs":s},
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					if (message.type == "success")
					{
						window.location.href = "list.rk?pj=${pj}&gr=${gr}"
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
	<div class="rpos"><@s.m "global.position"/>: SVN项目组账号 - <@s.m "global.add"/></div>
	<form class="ropt">
		<input type="hidden" name="pj" id="pj" value="${pj}" />
		<input type="submit" value="<@s.m "global.backToList"/>" onclick="this.form.action='${base}/admin/project/group/list.rk';" class="return-button"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<@p.form action="o_save" method="post" labelWidth="12" onsubmit="return checkForm(this);">
	<@p.hidden name="pgu.pj" id="pgu.pj" value="${pj}" />
	<@p.hidden name="pgu.gr" id="gr" value="${gr}" />
	<@p.td width="50"required="true">
		<select multiple="multiple" id="select1" style="height: 150px;width: 480px;">
		<#list usrList as usr>
			<option value="${usr.usr}">${usr.name!usr.usr}(${usr.usr})</option>
		</#list>
		</select>
	</@p.td>
	<@p.td width="50" required="true">
		<input id="add" type="button" value="&gt;" style="width:30px;"><br>
		<input id="add_all" type="button" value="&gt;&gt;" style="width:30px;"><br><br>
		<input id="remove" type="button" value="&lt;" style="width:30px;"><br>
		<input id="remove_all" type="button" value="&lt;&lt;" style="width:30px;"><br><br>
	</@p.td>
	<@p.td width="50" required="true">
		<select id="select2" name="usrs" multiple="multiple" style="height: 150px;width: 480px;"></select>
	</@p.td><@p.tr/>
	<@p.td colspan="3">
	<@p.submit code="global.submit"/>
	</@p.td><@p.tr/>
</@p.form>
</div>
</body>
</html>