var uploadIoc = {
	tmpFilePool : {
		type : 'org.nutz.filepool.NutFilePool', // 临时文件最大个数为 1000 个
		args : [ {
			java : "$conf.get('upload.temp','~/tmp')"
		}, 1000 ]
	},
	uploadFileContext : {
		type : 'org.nutz.mvc.upload.UploadingContext',
		singleton : true,
		args : [ {
			refer : 'tmpFilePool'
		} ],
		fields : {
			// 是否忽略空文件, 默认为 false
			ignoreNull : true,
			// 单个文件最大尺寸(大约的值，单位为字节，即 1048576 为 1M)
			maxFileSize : {
				java : "$conf.getInt('upload.maxFileSize',5048576)"
			},
			nameFilter : {
				java : "$conf.get('upload.nameFilter','^(.+[.])(doc|docx|ppt|pptx|pdf|jpg|gif)$')"
			}
		}
	},
	upload : {
		type : 'org.nutz.mvc.upload.UploadAdaptor',
		singleton : true,
		args : [ {
			refer : 'uploadFileContext'
		} ]
	}
};