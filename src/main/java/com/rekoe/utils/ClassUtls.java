package com.rekoe.utils;

import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.util.Daos;
import org.nutz.lang.Lang;
import org.nutz.resource.Scans;

public final class ClassUtls {

	public static void migration(Dao dao, String packageName, Object nameTable, Class<?>... ignoreClzs) {
		boolean haveIgnoreClazz = !Lang.isEmptyArray(ignoreClzs);
		for (Class<?> klass : Scans.me().scanPackage(packageName)) {
			if (klass.getAnnotation(Table.class) != null) {
				boolean isContinue = false;
				if (haveIgnoreClazz) {
					for (Class<?> clazz : ignoreClzs) {
						if (clazz == klass) {
							isContinue = true;
							break;
						}
					}
				}
				if (isContinue)
					continue;
				Daos.migration(dao, klass, true, true, nameTable);
			}
		}
	}

	public static void createTablesInPackage(Dao dao, Class<?> $clazz, boolean delReCreate, Class<?>... ignoreClzs) {
		boolean haveIgnoreClazz = !Lang.isEmptyArray(ignoreClzs);
		for (Class<?> klass : Scans.me().scanPackage($clazz.getPackage().getName())) {
			if (klass.getAnnotation(Table.class) != null) {
				boolean isContinue = false;
				if (haveIgnoreClazz) {
					for (Class<?> clazz : ignoreClzs) {
						if (clazz == klass) {
							isContinue = true;
							break;
						}
					}
				}
				if (isContinue)
					continue;
				dao.create(klass, delReCreate);
			}
		}
	}
}
