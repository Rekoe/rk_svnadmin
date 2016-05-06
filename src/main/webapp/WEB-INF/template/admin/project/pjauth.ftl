<html>
	<head>
		<script src="${base}/resources/jquery-1.7.min.js" type="text/javascript"></script>
		<script src="${base}/resources/sorttable.js"></script>
		<script src="${base}/resources/svnadmin.js"></script>
		<link rel="stylesheet" href="${base}/resources/bootstrap.min.css" />
		<link rel="stylesheet" href="${base}/resources/svnadmin.css" />
		<script>
		$(function(){
		    //移到用户组右边
		    $('#group_add').click(function() {
		           //获取选中的选项，删除并追加给对方
		           $('#select3 option:selected').appendTo('#select4');
		    });
		    //移到用户组左边
		    $('#group_del').click(function() {
		           $('#select4 option:selected').appendTo('#select3');
		    });
		    //全部移到用户组右边
		    $('#group_add_all').click(function() {
		           //获取全部的选项,删除并追加给对方
		           $('#select3 option').appendTo('#select4');
		    });
		    //全部移到用户组左边
		    $('#group_del_all').click(function() {
		           $('#select4 option').appendTo('#select3');
		    });
		    
		    //移到用户右边
		    $('#user_add').click(function() {
		           //获取选中的选项，删除并追加给对方
		           $('#select1 option:selected').appendTo('#select2');
		    });
		    //移到用户左边
		    $('#user_del').click(function() {
		           $('#select2 option:selected').appendTo('#select1');
		    });
		    //全部移到用户右边
		    $('#user_add_all').click(function() {
		           //获取全部的选项,删除并追加给对方
		           $('#select1 option').appendTo('#select2');
		    });
		    //全部移到用户左边
		    $('#user_del_all').click(function() {
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
		  //双击选项
		    $('#select3').dblclick(function(){     //绑定双击事件
		           //获取全部的选项,删除并追加给对方
		           $("option:selected",this).appendTo('#select4');     //追加给对方
		    });
		    //双击选项
		    $('#select4').dblclick(function(){
		           $("option:selected",this).appendTo('#select3');
		    });
		});
		
		function checkForm(f){
			if(f.elements["pj"].value==""){
				alert("项目不可以为空");
				f.elements["pj"].focus();
				return false;
			}
			if(f.elements["res"].value==""){
				alert("资源不可以为空");
				f.elements["res"].focus();
				return false;
			}
			if(f.elements["grs"].value=="" && f.elements["usrs"].value==""){
				alert("请选择用户组或用户");
				f.elements["grs"].focus();
				return false;
			}
			return true;
		}
		</script>
	</head>
<body style="margin: 0px;">
<form name="pjauth" action="pjauth/save" method="post" onsubmit="return checkForm(this);">
	<input type="hidden" name="pj" value="${pj}">
	<table class="table table-striped table-bordered">
		<tr>
			<td class="lbl">资源</td>
			<td colspan="3">
				<input type="text" name="res" value="${entity.res!""}" style="width:400px;height:30px"><span style="color:red;">*</span>
			  <select onchange="this.form.res.value=this.value">
				<option value="">选择资源</option>
				<#list pjreslist as pjre>
				 <option value="${pjre}">${pjre}</option>
				 </#list>
			  </select> 
			</td>
		</tr>
		<tr>
			<td class="lbl">用户组</td>
			<td valign="top">
				<table>
					<tr>
						<td style="border:0px;">
							<select id="select3" multiple="multiple" style="height: 150px;width: 150px;">
								<#list pjgrlist as pjGr>
								<option value="${pjGr.gr}">${pjGr.gr}</option>
								</#list>
							</select>
						</td>
						<td style="border:0px;">
							<input id="group_add" type="button" style="width:30px;" value="&gt;"><br>
							<input id="group_add_all" type="button" style="width:30px;" value="&gt;&gt;"><br><br>
							<input id="group_del" type="button" style="width:30px;" value="&lt;"><br>
							<input id="group_del_all" type="button" style="width:30px;" value="&lt;&lt;"><br><br>
						</td>
						<td style="border:0px;">
							<select id="select4" name="grs" multiple="multiple" style="height: 150px;width: 150px;">
							</select>
						</td>
					</tr>
				</table>
			</td>
			<td class="lbl">用户</td>
			<td valign="top">
				<table>
					<tr>
						<td style="border:0px;">
							<select id="select1" multiple="multiple" style="height: 150px;width: 160px;">
							<#list usrList as usr>
								<option value="${usr.usr}">${usr.name!usr.usr}(${usr.usr})</option>
							</#list>
							</select>
						</td>
						<td style="border:0px;">
							<input id="user_add" type="button" style="width:30px;" value="&gt;"><br>
							<input id="user_add_all" type="button" style="width:30px;" value="&gt;&gt;"><br><br>
							<input id="user_del" type="button" style="width:30px;" value="&lt;"><br>
							<input id="user_del_all" type="button" style="width:30px;" value="&lt;&lt;"><br><br>
						</td>
						<td style="border:0px;">
							<select id="select2" name="usrs" multiple="multiple" style="height: 150px;width: 160px;">
							</select>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="lbl">权限</td>
			<td colspan="3">
			<select name="rw">
					<option value="rw" <#if entity.rw == "rw">selected='selected'</#if>>可读可写</option>
					<option value="" <#if entity.rw == "">selected='selected'</#if>>没有权限</option>
					<option value="r"<#if entity.rw == "r">selected='selected'</#if>>可读</option>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="4" align="center" style="text-align: center;"><input type="submit" class="btn btn-primary" value="保存"></td>
		</tr>
	</table>
</form>
<table class="table table-striped table-bordered">
	<thead>
		<td>NO.</td>
		<td>项目</td>
		<td>资源</td>
		<td>用户组/用户</td>
		<td>权限</td>
		<td>删除</td>
	</thead>
	<#list list as pjAuth>
		<tr>
		<td>${pjAuth_index +1}</td>
		<td>${pjAuth.pj}</td>
		<td>${pjAuth.res}</td>
		<td>${pjAuth.gr!""}${pjAuth.usr!pjAuth.usrName}:(${pjAuth.usr})</td>
		<td><#if "r"== pjAuth.rw>可读<#elseif "rw"== pjAuth.rw>可读可写<#else>没有权限</#if></td>
		<@shiro.hasPermission name="svn.project:auth.manager">	
		<td><a href="javascript:if(confirm('确认删除?')){del('pjauth_delete?pj=${pjAuth.pj}&res=${pjAuth.res}&gr=${pjAuth.gr}&usr=${pjAuth.usr}')}">删除</a></td>
		</@shiro.hasPermission>
	</tr>
	</#list>
</table>
</body>
</html>