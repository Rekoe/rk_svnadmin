package com.rekoe.module;

import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;

import com.rekoe.domain.CdkeyLog;
import com.rekoe.service.CdKeyCategoryService;
import com.rekoe.service.CdKeyLogService;
import com.rekoe.service.CdKeyService;

@IocBean
@At("/excharge")
public class ExchargeAction {

	private final static Log log = Logs.get();
	@Inject
	private CdKeyService cdKeyService;

	@Inject
	private CdKeyCategoryService cdKeyCategoryService;

	@Inject
	private CdKeyLogService cdKeyLogService;

	@Inject
	private PropertiesProxy conf;

	@At
	@Ok("json")
	@POST
	@AdaptBy(type = JsonAdaptor.class)
	public NutMap cdk(CdkeyBean bean) {
		StringBuffer sb = new StringBuffer("cdk.");
		sb.append(bean.getPid()).append(".secret");
		String secret = conf.get(sb.toString());
		String sign = Lang.md5(bean.getCdkey() + "|" + bean.getMac() + "|" + bean.getPid() + "|" + secret);
		if (log.isDebugEnabled()) {
			log.debugf("secretKey : %s", sb);
			log.debugf("secret : %s", secret);
			log.debugf("Cdkey : %s", bean.getCdkey());
			log.debugf("Mac : %s", bean.getMac());
			log.debugf("Pid : %s", bean.getPid());
			log.debugf("Sign : %s", bean.getSign());
			log.debugf("P_Sign : %s", sign);
		}

		if (Lang.equals(sign, bean.getSign())) {
			int type = cdKeyCategoryService.getType(bean.getCdkey());
			if (cdKeyCategoryService.isExists(type)) {
				if (cdKeyService.use(bean.getCdkey(), type)) {
					cdKeyLogService.insert(new CdkeyLog(bean.getPid(), bean.getProviderId(), bean.getMac()));
					return NutMap.NEW().addv("feedback", 0).addv("mesage", "ok");
				}
				return NutMap.NEW().addv("feedback", 92).addv("mesage", "已失效");
			}
			return NutMap.NEW().addv("feedback", 93).addv("mesage", "不存在");
		}
		return NutMap.NEW().addv("feedback", 61).addv("mesage", "签名失败");
	}
}
