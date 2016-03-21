package com.rekoe.mobile.provider;

import java.io.Serializable;
import java.util.Map;

import org.nutz.http.Http;
import org.nutz.http.Request;
import org.nutz.http.Request.METHOD;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.rekoe.domain.PlatformConfig;
import com.rekoe.mobile.AbstractParam;
import com.rekoe.mobile.AuthProvider;
import com.rekoe.service.PlatformConfigService;

public abstract class AbstractProvider<T extends AbstractParam> extends AuthProvider<T> implements Serializable {

	private static final long serialVersionUID = 3293830764207197084L;

	private Mirror<T> mirror;

	private static final Log log = Logs.get();

	@Inject
	private PlatformConfigService platformConfigService;

	/**
	 * 本抽象类能提供一些帮助方法,减少重复写实体类型的麻烦
	 */
	public AbstractProvider() {
		try {
			Class<T> entryClass = Mirror.getTypeParam(getClass(), 0);
			mirror = Mirror.me(entryClass);
			if (log.isDebugEnabled())
				log.debugf("Get TypeParams for self : %s", entryClass.getName());
		} catch (Throwable e) {
			if (log.isWarnEnabled())
				log.warn("!!!Fail to get TypeParams for self!", e);
		}
	}

	public PlatformConfig getPlatformConfig(int pid) {
		PlatformConfig pc = platformConfigService.getPlatformConfig(pid, getProviderId());
		if (Lang.isEmpty(pc)) {
			throw Lang.makeThrow(NullPointerException.class, "Can`t find %s PlatformConfig", getProviderId());
		}
		return pc;
	}

	/**
	 * 获取实体类型
	 * 
	 * @return 实体类型
	 */
	public Class<T> getEntityClass() {
		return mirror.getType();
	}

	public String HttpGet(String url) {
		return HttpGet(url, def_num);
	}

	private String HttpGet(String url, int num) {
		try {
			return Http.get(url).getContent();
		} catch (Exception e) {
			log.errorf("net Connect Err,url=%s", url);
			if (num > 0 && num < max) {
				num--;
				HttpGet(url, num);
			}
		}
		return "{}";
	}

	public String httpPost(String url, Map<String, Object> params) {
		return httpPost(url, params, def_num);
	}

	private String httpPost(String url, Map<String, Object> params, int num) {
		try {
			Response res = Http.post2(url, params, 6000);
			if (res.isOK()) {
				return res.getContent();
			}
		} catch (Exception e) {
			log.errorf("net Connect Err,url=%s", url);
			if (num > 0 && num < max) {
				num--;
				httpPost(url, params, num);
			}
		}
		return "{}";
	}

	public String HttpsGet(String url) {
		return HttpsGet(url, def_num);
	}

	private String HttpsGet(String url, int num) {
		try {
			Http.disableJvmHttpsCheck();
			return Http.get(url).getContent();
		} catch (Exception e) {
			log.errorf("net Connect Err,url=%s", url);
			if (num > 0 && num < max) {
				num--;
				HttpsGet(url, num);
			}
		}
		return "{}";
	}

	public String httpsPost(String url, Map<String, Object> params) {
		return httpsPost(url, params, def_num);
	}

	private String httpsPost(String url, Map<String, Object> params, int num) {
		try {
			Http.disableJvmHttpsCheck();
			Response res = Http.post2(url, params, 6000);
			if (res.isOK()) {
				return res.getContent();
			}
		} catch (Exception e) {
			log.errorf("net Connect Err,url=%s", url);
			if (num > 0 && num < max) {
				num--;
				httpsPost(url, params, num);
			}
		}
		return "{}";
	}

	public String httpsPostBody(String url, String data) {
		return httpsPostBody(url, data, def_num);
	}

	public String httpPostBody(String url, String data) {
		return httpPostBody(url, data, def_num);
	}

	private String httpPostBody(String url, String data, int num) {
		Request req = Request.create(url, METHOD.POST);
		req.getHeader().set("Content-Type", "application/json:charset=utf-8 ");
		req.setData(data);
		try {
			Response resp = Sender.create(req).send();
			return resp.getContent();
		} catch (Exception e) {
			log.errorf("net Connect Err,url=%s", url);
			if (num > 0 && num < max) {
				num--;
				httpPostBody(url, data, num);
			}
		}
		return "{}";
	}

	private String httpsPostBody(String url, String data, int num) {
		Http.disableJvmHttpsCheck();
		Request req = Request.create(url, METHOD.POST);
		req.getHeader().set("Content-Type", "application/json:charset=utf-8 ");
		req.setData(data);
		try {
			Response resp = Sender.create(req).send();
			return resp.getContent();
		} catch (Exception e) {
			log.errorf("net Connect Err,url=%s", url);
			if (num > 0 && num < max) {
				num--;
				httpsPostBody(url, data, num);
			}
		}
		return "{}";
	}

	public <B> Map<String, B> result2Json(String jsonStr, Class<B> eleType) throws Exception {
		if (Strings.isBlank(jsonStr)) {
			throw Lang.makeThrow("%s callBack is Blank", getProviderId());
		}
		return Json.fromJsonAsMap(eleType, jsonStr);
	}

	public <B> Map<String, B> result2Json(String jsonStr, Class<B> eleType, String def) throws Exception {
		return Json.fromJsonAsMap(eleType, Strings.isBlank(jsonStr) ? def : jsonStr);
	}

	private final int max = 5;
	private final int def_num = 1;
}
