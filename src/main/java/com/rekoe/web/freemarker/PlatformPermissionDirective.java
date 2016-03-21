package com.rekoe.web.freemarker;

import java.io.IOException;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.lang.Lang;

import com.rekoe.domain.GameServer;
import com.rekoe.domain.User;
import com.rekoe.utils.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class PlatformPermissionDirective implements TemplateDirectiveModel {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		int sid = DirectiveUtils.getInt("sid", params);
		Subject subject = SecurityUtils.getSubject();
		if (subject.getPrincipal() instanceof User) {
			User user = (User) subject.getPrincipal();
			if (user.isSystem()) {
				body.render(env.getOut());
			} else {
				Map<Integer, GameServer> servers = user.getServers();
				if (Lang.isEmpty(servers.get(sid))) {
					env.getOut().append("无权限操作");
				}else{
					body.render(env.getOut());
				}
			}
		}
	}
}
