Cms={};
Cms.lmenu = function(id) {
	$("li").each(function(i){
		$(this).removeClass();
		if(i==0){
			$(this).addClass("leftCurr");
		}else{
			$(this).addClass("leftNol");
		}
		$(this).click( function () {
			$("li").each(function(){
				$(this).removeClass();
				$(this).addClass("leftNol");
			});
			$("h3").each(function(){
				$(this).next().hide();
				$(this).removeClass();
				$(this).addClass("leftNol");
			});
			$(this).removeClass();
			$(this).addClass("leftCurr");
		});
	});
	$("h3").each(function(){
		$(this).next().hide();
		$(this).addClass("leftNol");
		$(this).click( function () {
			$("h3").each(function(){
				$(this).next().hide();
				$(this).removeClass();
				$(this).addClass("leftNol");
			});
			$("li").each(function(){
				$(this).removeClass();
				$(this).addClass("leftNol");
			});
			$(this).removeClass();
			$(this).addClass("leftCurr");
		    $(this).next().show(); 
		});
	});
}
Cms.add = function(id){
	$.dialog({
		type: "warn",
		content: '确定要添加此记录?',
		ok: 'Ok',
		cancel: 'Cancel',
		onOk: function() {
			$.ajax({
				url: "o_save.rk",
				type: "POST",
				data: $('#jvForm').serialize(),
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					if (message.type == "success")
					{
						window.location.href = "list.rk"
					}
				}
			});
		}
	}); 
	return false;
}
Cms.addBack = function(url){
	$.dialog({
		type: "warn",
		content: '确定要添加此记录?',
		ok: 'Ok',
		cancel: 'Cancel',
		onOk: function() {
			$.ajax({
				url: "o_save.rk",
				type: "POST",
				data: $('#jvForm').serialize(),
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					if (message.type == "success")
					{
						window.location.href = url;
					}
				}
			});
		}
	}); 
	return false;
}
Cms.updateBack = function(url){
	$.dialog({
		type: "warn",
		content: '确定要修改此记录?',
		ok: 'Ok',
		cancel: 'Cancel',
		onOk: function() {
			$.ajax({
				url: "o_update.rk",
				type: "POST",
				data: $('#jvForm').serialize(),
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					if (message.type == "success")
					{
						window.location.href = url;
					}
				}
			});
		}
	}); 
	return false;
}
Cms.update = function(id){
	$.dialog({
		type: "warn",
		content: '确定要修改此记录?',
		ok: 'Ok',
		cancel: 'Cancel',
		onOk: function() {
			$.ajax({
				url: "o_update.rk",
				type: "POST",
				data: $('#jvForm').serialize(),
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					if (message.type == "success")
					{
						window.location.href = "list.rk";
					}
				}
			});
		}
	}); 
	return false;
}

Cms.updateAll = function(url,back){
	$.dialog({
		type: "warn",
		content: '确定要修改此记录?',
		ok: 'Ok',
		cancel: 'Cancel',
		onOk: function() {
			$.ajax({
				url: url,
				type: "POST",
				data: $('#jvForm').serialize(),
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					if (message.type == "success")
					{
						window.location.href = back;
					}
				}
			});
		}
	}); 
	return false;
}
Cms.deleted = function(id){
	$.dialog({
		type: "warn",
		content: '确定要删除此记录?',
		ok: 'Ok',
		cancel: 'Cancel',
		onOk: function() {
			$.ajax({
				url: "delete.rk",
				type: "POST",
				data: {"id":id},
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					if (message.type == "success")
					{
						window.location.href = back;
					}
				}
			});
		}
	}); 
	return false;
}