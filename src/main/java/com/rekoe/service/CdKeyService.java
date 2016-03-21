package com.rekoe.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.ConnCallback;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.rekoe.domain.CdkeyCategory;
import com.rekoe.domain.CdkeyEntity;
import com.rekoe.utils.StringGenerator;

@IocBean(fields = { "dao" }, create = "init")
public class CdKeyService extends BaseService<CdkeyEntity> {

	private final static String CDKEY_SUFFIX = "cdkey_";

	private final static Log log = Logs.get();
	private final Map<String, Boolean> CDKEY_PROCESS_STATUS_ON = new HashMap<String, Boolean>();
	private Object lock = new Object();

	private ExecutorService executorService;

	public CdKeyService() {
	}

	public CdKeyService(Dao dao) {
	}

	public void init() {
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		/**
		 * 修复下表结构
		 */
		Sql sql = Sqls.create("select cdkey_type from $table");
		sql.setEntity(dao().getEntity(CdkeyCategory.class));
		sql.vars().set("table", sql.getEntity().getTableName());
		final List<Integer> typeList = new ArrayList<>();
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				while (rs.next()) {
					typeList.add(rs.getInt(1));
				}
				return null;
			}
		});
		dao().execute(sql);
		for (int id : typeList) {
			Daos.migration(dao(), CdkeyEntity.class, true, true, id);
		}
	}

	public boolean use(String cdkey, int taskid) {
		Dao dao = Daos.ext(dao(), taskid);
		return dao.update(CdkeyEntity.class, Chain.make("is_used", true), Cnd.where("cdkey", "=", cdkey).and("is_used", "=", false)) > 0;
	}

	public boolean addCdkey(final String taskid, final int num, final int len) {
		synchronized (lock) {
			Boolean isRight = CDKEY_PROCESS_STATUS_ON.get(taskid);
			if (Lang.isEmpty(isRight) || !isRight) {
				CDKEY_PROCESS_STATUS_ON.put(taskid, true);
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						try {
							Date now = Times.now();
							List<String> linkd = new ArrayList<String>();
							Dao dao = Daos.ext(dao(), taskid);
							boolean exists = dao.exists(CdkeyEntity.class);
							Set<String> temp = new HashSet<>();
							Set<String> set = new HashSet<String>();
							if (!exists) {
								dao.create(CdkeyEntity.class, false);
							} else {
								Sql sql = Sqls.create("select cdkey from $table");
								sql.setEntity(dao.getEntity(CdkeyEntity.class));
								sql.vars().set("table", sql.getEntity().getTableName() + taskid);
								sql.setCallback(Sqls.callback.strList());
								dao.execute(sql);
								List<String> list = sql.getList(String.class);
								linkd.addAll(list);
								for (String str : list) {
									temp.add(str.toLowerCase());
								}
							}
							int count = 0;
							int safeCount = 0;
							BlockingQueue<CdkeyEntity> queue = new LinkedBlockingQueue<CdkeyEntity>();
							StringGenerator sg = new StringGenerator(len, len);
							while (true) {
								String key = sg.next();
								String newStrKey = getRandomStr(key, taskid);
								if (temp.contains(newStrKey.toLowerCase())) {
									safeCount++;
									continue;
								}
								count++;
								if (count > num) {
									break;
								}
								if (safeCount > 10000) {
									break;
								}
								set.add(newStrKey);
							}
							for (String $key : set) {
								queue.add(new CdkeyEntity($key, now));
								linkd.add($key);
							}
							List<CdkeyEntity> list = new ArrayList<>();
							while (true) {
								if (queue.size() >= 100) {
									queue.drainTo(list, 100);
									dao.fastInsert(list);
									list.clear();
								} else if (queue.size() != 0) {
									queue.drainTo(list, 50);
									dao.fastInsert(list);
									list.clear();
								} else {
									list.clear();
									break;
								}
							}
						} catch (Exception e) {
							log.error(e);
						}
						CDKEY_PROCESS_STATUS_ON.put(taskid, false);
					}
				});
				return true;
			}
			return false;
		}
	}

	public List<String> cdkTables() {
		final List<String> tables = new ArrayList<String>();
		dao().run(new ConnCallback() {
			@Override
			public void invoke(java.sql.Connection conn) throws Exception {
				String showTables = "show tables";
				java.sql.PreparedStatement ppstat = conn.prepareStatement(showTables);
				ResultSet rest = ppstat.executeQuery();
				while (rest.next()) {
					String table = rest.getString(1);
					if (StringUtils.contains(table, CDKEY_SUFFIX)) {
						if (!StringUtils.containsOnly(table, CDKEY_SUFFIX)) {
							tables.add(table);
						}
					}
				}
			}
		});
		return tables;
	}

	public List<Record> searchDown(int taskid, boolean used) {
		Dao dao = Daos.ext(dao(), taskid);
		Sql sql = Sqls.create("select * from $table $condition");
		sql.setEntity(dao.getEntity(CdkeyEntity.class));
		sql.vars().set("table", sql.getEntity().getTableName() + taskid);
		sql.setCondition(used ? Cnd.where("used", "=", true) : null);
		sql.setCallback(Sqls.callback.records());
		dao.execute(sql);
		return sql.getList(Record.class);
	}

}
