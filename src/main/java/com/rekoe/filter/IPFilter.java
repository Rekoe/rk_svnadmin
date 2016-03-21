package com.rekoe.filter;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.util.Callback;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.view.RawView;
import org.nutz.mvc.view.ViewWrapper;

import com.rekoe.utils.CommonUtils;

@IocBean(create = "init")
public class IPFilter implements ActionFilter {

	private final static Log log = Logs.get();
	protected static final String IP_WHITE_LIST = "ipwhite";
	protected String configPath = "";
	protected Set<String> ipWhiteSet = new HashSet<String>();
	private final View IP_FORBIT_ERROR = new ViewWrapper(new RawView(""), "非法IP");

	/* 初始化 */
	public void init() throws ServletException {
		configPath = Mvcs.getNutConfig().getAppRoot() + "/../conf/";
		refreshIPList();
		initFileMonitor();
	}

	private void refreshIPList() {
		try {
			File file = Files.findFile(IP_WHITE_LIST);
			if (Lang.isEmpty(file)) {
				file = Files.findFile(configPath + IP_WHITE_LIST);
			}
			if (Lang.isEmpty(file)) {
				this.ipWhiteSet = null;
				return;
			}
			final Set<String> ips = new HashSet<String>();
			Files.readLine(file, new Callback<String>() {
				@Override
				public void invoke(String str) {
					ips.add(str);
				}
			});
			this.ipWhiteSet = ips;
		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	public View match(ActionContext actionContext) {
		HttpServletRequest req = actionContext.getRequest();
		String remoteIP = Lang.getIP(req);
		if (log.isDebugEnabled()) {
			log.debug(" ip file pass " + remoteIP);
		}
		if (this.ipWhiteSet == null || this.ipWhiteSet.isEmpty())
			return null;
		if (this.ipWhiteSet.contains(remoteIP) || CommonUtils.isInnerIP(remoteIP)) {
			return null;
		}
		return IP_FORBIT_ERROR;
	}

	private void initFileMonitor() {
		FileSystemManager fsManager = null;
		org.apache.commons.vfs2.FileObject listendir = null;
		try {
			fsManager = VFS.getManager();
			listendir = fsManager.resolveFile(new File(configPath).getAbsolutePath());
		} catch (org.apache.commons.vfs2.FileSystemException e) {
			log.error(e);
		}

		DefaultFileMonitor fm = new DefaultFileMonitor(new FileListener() {
			public void fileCreated(FileChangeEvent event) throws Exception {
				monitor(event);
			}

			public void fileDeleted(FileChangeEvent event) throws Exception {
				monitor(event);
			}

			public void fileChanged(FileChangeEvent event) throws Exception {
				monitor(event);
			}

			private void monitor(FileChangeEvent event) {
				org.apache.commons.vfs2.FileObject fileObject = event.getFile();
				FileName fileName = fileObject.getName();
				if (fileName.getBaseName().contains(IP_WHITE_LIST)) {
					refreshIPList();
				}
			}
		});
		fm.setRecursive(true);
		fm.addFile(listendir);
		fm.start();
	}
}