package com.rekoe.web.freemarker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nutz.lang.Lang;

import com.rekoe.common.PlatformVO;
import com.rekoe.utils.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class PlatformEditDirective implements TemplateDirectiveModel {

	private static final String PATTERN = "obj";

	private final List<PlatformVO> list = new ArrayList<>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Object obj = DirectiveUtils.getObject(PATTERN, params);
		Map<String, Object> localHashMap = new HashMap<String, Object>();
		if (!Lang.isEmpty(obj)) {
			HashMap map = (HashMap) obj;
			Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> entry = iterator.next();
				localHashMap.put(entry.getKey(), entry.getValue());
			}
		}
		localHashMap.put("plist", list);
		DirectiveUtils.setVariables(localHashMap, env, body);
	}

	public void init() {
		list.add(new PlatformVO("0", "请选择"));
		list.add(new PlatformVO("uc", "uc"));
		list.add(new PlatformVO("qihoo", "奇虎360"));
		list.add(new PlatformVO("sg", "上游"));
		list.add(new PlatformVO("google", "谷歌"));
		list.add(new PlatformVO("facebook", "脸谱"));
		list.add(new PlatformVO("apple", "苹果"));
		list.add(new PlatformVO("a91", "百度"));
		list.add(new PlatformVO("hw", "华为"));
		list.add(new PlatformVO("qq", "腾讯"));
		list.add(new PlatformVO("msdk", "应用宝"));
		list.add(new PlatformVO("rk", "自平台"));
		list.add(new PlatformVO("guest", "游客"));
		list.add(new PlatformVO("googleP", "GooglePlay"));
	}
}
