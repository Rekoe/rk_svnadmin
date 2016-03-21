package com.rekoe.web.freemarker;

import java.io.IOException;
import java.util.Map;

import org.nutz.lang.Lang;

import com.rekoe.domain.OfficialServer;
import com.rekoe.service.OfficialServerService;
import com.rekoe.utils.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class Sid2NameDirective implements TemplateDirectiveModel {

	private OfficialServerService officialServerService;

	public Sid2NameDirective(OfficialServerService officialServerService) {
		this.officialServerService = officialServerService;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		int sid = DirectiveUtils.getInt("sid", params);
		int pid = DirectiveUtils.getInt("pid", params);
		OfficialServer officialServer = officialServerService.getOfficialServer(pid, sid);
		if (Lang.isEmpty(officialServer)) {
			env.getOut().append(sid + "");
		} else {
			env.getOut().append(officialServer.getName());
		}
	}
}
