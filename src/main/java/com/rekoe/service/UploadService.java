package com.rekoe.service;

import java.io.File;

import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.upload.TempFile;

/**
 * @author 科技㊣²º¹³<br />
 *         2014年2月3日 下午4:48:45 <br />
 *         http://www.rekoe.com <br />
 *         QQ:5382211
 */
@IocBean
public class UploadService extends BaseService<Object> {

	private final static Log log = Logs.get();

	@Inject
	private PropertiesProxy conf;

	@SuppressWarnings("deprecation")
	public File upload(TempFile tempFile) throws Exception {
		String savePath = conf.get("xlsPath") + "/" + tempFile.getSubmittedFileName();
		Files.makeDir(new File(conf.get("xlsPath")));
		File saveFile = new File(savePath);
		Files.move(tempFile.getFile(), saveFile);
		if (log.isDebugEnabled()) {
			log.debugf("save path[%s]", savePath);
		}
		return saveFile;
	}

	@SuppressWarnings("deprecation")
	public File upload(TempFile tempFile, String savePath) throws Exception {
		Files.makeDir(new File(savePath));
		File saveFile = new File(savePath);
		Files.move(tempFile.getFile(), saveFile);
		if (log.isDebugEnabled()) {
			log.debugf("save path[%s]", savePath);
		}
		return saveFile;
	}
}
