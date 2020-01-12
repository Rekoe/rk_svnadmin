package com.rekoe.service;

import java.io.File;
import java.util.Map;

import org.apache.commons.mail.ImageHtmlEmail;
import org.nutz.boot.starter.freemarker.FreeMarkerConfigurer;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import freemarker.template.Template;

@IocBean(name = "emailService")
public class EmailServiceImpl implements EmailService {

	private static final Log log = Logs.get();

	@Inject
	protected ImageHtmlEmail imageHtmlEmail;

	@Inject
	private FreeMarkerConfigurer freeMarkerConfigurer;

	public boolean send(String to, String subject, String templateFile, Map<String, Object> root) {
		try {
			imageHtmlEmail.setCharset("UTF-8");
			imageHtmlEmail.setSubject(subject);
			imageHtmlEmail.setHtmlMsg(processTemplateIntoString(templateFile, root));
			imageHtmlEmail.addTo(to);
			String res = imageHtmlEmail.send();
			if (log.isDebugEnabled()) {
				log.debug(res);
			}
			return true;
		} catch (Exception e) {
			log.error("send email fail", e);
			return false;
		}
	}

	private String processTemplateIntoString(String templateFile, Map<String, Object> root) {
		try {
			String path = "template" + File.separator + "admin" + File.separator + "common" + File.separator + templateFile + freeMarkerConfigurer.getSuffix();
			Template template = freeMarkerConfigurer.getConfiguration().getTemplate(path);
			template.setEncoding("UTF-8");
			java.io.StringWriter writer = new java.io.StringWriter();
			template.process(root, writer);
			return writer.toString();
		} catch (Exception e) {
			log.error(e);
		}
		return "";
	}

	@Override
	public boolean restpwd(String to, Map<String, Object> root) {
		return send(to, "svn-密码重置邮件<系统邮件,请勿回复>", "rest_pwd", root);
	}

	@Override
	public boolean projectOpen(String to, Map<String, Object> root) {
		return send(to, "svn-项目开启邮件<系统邮件,请勿回复>", "project_open", root);
	}
}
