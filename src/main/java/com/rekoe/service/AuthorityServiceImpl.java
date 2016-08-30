package com.rekoe.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.integration.shiro.annotation.NutzRequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;
import org.nutz.resource.Scans;

import com.rekoe.domain.Permission;
import com.rekoe.domain.PermissionCategory;

@IocBean(name = "authorityService")
public class AuthorityServiceImpl implements AuthorityService {

	@Inject
	private Dao dao;

	class PermissionTagClzz {

		private String premission;
		private String tag;
		private String name;

		public PermissionTagClzz(String premission, String tag, String name) {
			super();
			this.premission = premission;
			this.tag = tag;
			this.name = name;
		}

		public String getPremission() {
			return premission;
		}

		public void setPremission(String premission) {
			this.premission = premission;
		}

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public void initFormPackage(String... pkgs) {
		List<Class<?>> allClazz = new ArrayList<>();
		for (String pkg : pkgs) {
			List<Class<?>> scanPackage = Scans.me().scanPackage(pkg);
			allClazz.addAll(scanPackage);
		}
		Set<String> rpTagNames = new HashSet<>();
		final Set<String> permissions = new HashSet<String>();
		final Map<String, PermissionTagClzz> perTagMap = new HashMap<>();
		for (Class<?> klass : allClazz) {
			for (Method method : klass.getMethods()) {
				NutzRequiresPermissions rp = method.getAnnotation(NutzRequiresPermissions.class);
				if (rp != null && rp.value() != null) {
					if (rp.enable() == false)
						continue;
					rpTagNames.add(rp.tag());
					for (String permission : rp.value()) {
						if (permission != null && !permission.endsWith("*")) {
							permissions.add(permission);
							perTagMap.put(permission, new PermissionTagClzz(permission, rp.tag(), rp.name()));
						}
					}
				}
			}
		}

		// 整理出 需要添加权限的所有的权限分类
		final Iterator<Entry<String, PermissionTagClzz>> iter = perTagMap.entrySet().iterator();
		final Set<String> perTags = new HashSet<String>();
		while (iter.hasNext()) {
			Entry<String, PermissionTagClzz> entry = iter.next();
			PermissionTagClzz ptc = entry.getValue();
			String name = ptc.getTag();
			perTags.add(name);
		}

		dao.each(PermissionCategory.class, null, new Each<PermissionCategory>() {
			public void invoke(int index, PermissionCategory ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				perTags.remove(ele.getName());
			}
		});

		// 把分类数据插入到数据库
		final Map<String, String> tagIds = new HashMap<String, String>();
		for (String name : perTags) {
			PermissionCategory pc = new PermissionCategory();
			pc.setLocked(true);
			pc.setName(name);
			dao.insert(pc);
		}

		// 把全部权限查出来一一检查
		dao.each(Permission.class, null, new Each<Permission>() {
			public void invoke(int index, Permission ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				permissions.remove(ele.getName());
				perTagMap.remove(ele.getName());
			}
		});

		Iterator<Entry<String, PermissionTagClzz>> iterator = perTagMap.entrySet().iterator();
		Set<String> tagNames = new HashSet<String>();
		while (iterator.hasNext()) {
			Entry<String, PermissionTagClzz> entry = iterator.next();
			tagNames.add(entry.getValue().getTag());
		}
		dao.each(PermissionCategory.class, Cnd.where("name", "in", tagNames), new Each<PermissionCategory>() {
			public void invoke(int index, PermissionCategory ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				tagIds.put(ele.getName(), ele.getId());
			}
		});

		List<Permission> newSet = new ArrayList<>();
		final Iterator<Entry<String, PermissionTagClzz>> $iter = perTagMap.entrySet().iterator();
		while ($iter.hasNext()) {
			Entry<String, PermissionTagClzz> entry = $iter.next();
			PermissionTagClzz ptc = entry.getValue();
			String name = ptc.getTag();
			Permission p = new Permission();
			p.setDescription(ptc.getName());
			p.setPermissionCategoryId(tagIds.get(name));
			p.setName(entry.getKey());
			newSet.add(p);
		}

		if (!Lang.isEmpty(newSet)) {
			dao.fastInsert(newSet);
		}
	}
}
