package com.rekoe.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.domain.CdkeyLog;

@IocBean(fields = { "dao" })
public class CdKeyLogService extends BaseService<CdkeyLog> {

	public CdKeyLogService() {
	}

	public CdKeyLogService(Dao dao) {
		super(dao);
	}
	
	
}
