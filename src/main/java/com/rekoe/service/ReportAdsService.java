package com.rekoe.service;

import java.util.List;

import org.nutz.aop.interceptor.async.Async;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.domain.ReportAds;

@IocBean(fields = { "dao" })
public class ReportAdsService extends BaseService<ReportAds> {

	@Async
	public void add(ReportAds reportAds) {
		dao().insert(reportAds);
	}

	public List<Record> getList(int pid) {
		Sql sql = Sqls.create("select pfid,idfa,idfv,talking_data_id from $table $condition");
		sql.vars().set("table", dao().getEntity(getEntityClass()).getTableName());
		sql.setCallback(Sqls.callback.records());
		sql.setCondition(Cnd.where("pid", "=", pid));
		dao().execute(sql);
		return sql.getList(Record.class);
	}
}
