var ioc = {
	shiroTags : {
		type : "com.rekoe.shiro.freemarker.ShiroTags"
	},
	permissionResolver : {
		type : "org.apache.shiro.authz.permission.WildcardPermissionResolver"
	},
	permissionShiro : {
		type : "com.rekoe.web.freemarker.PermissionShiroFreemarker",
		args : [ {
			refer : "permissionResolver"
		} ]
	},
	permission : {
		type : "com.rekoe.web.freemarker.PermissionDirective"
	},
	process : {
		type : "com.rekoe.web.freemarker.ProcessTimeDirective"
	},
	currentTime : {
		type : "com.rekoe.web.freemarker.CurrentTimeDirective"
	},
	htmlCut : {
		type : "com.rekoe.web.freemarker.HtmlCutDirective"
	},
	pagination : {
		type : "com.rekoe.web.freemarker.PaginationDirective"
	},
	timeFormat : {
		type : "com.rekoe.web.freemarker.TimeFormatDirective"
	},
	mapTags : {
		factory : "$freeMarkerConfigurer#addTags",
		args : [ {
			'shiro' : {
				refer : 'shiroTags'
			},
			'perm_chow' : {
				refer : 'permissionShiro'
			},
			'cms_perm' : {
				refer : 'permission'
			},
			'process_time' : {
				refer : 'process'
			},
			'pagination' : {
				refer : 'pagination'
			},
			'htmlCut' : {
				refer : 'htmlCut'
			},
			'timeFormat' : {
				refer : 'timeFormat'
			},
			'currentTime' : {
				refer : 'currentTime'
			}
		} ]
	}
};