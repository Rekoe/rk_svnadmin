package com.rekoe.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.domain.ProjectConfig;

/**
 * @author 科技㊣²º¹³<br/>
 *         2016年3月24日 下午4:48:45<br/>
 *         http://www.rekoe.com<br/>
 *         QQ:5382211
 */
@IocBean(args = { "refer:dao" }, create = "init")
public class ProjectConfigService extends BaseService<ProjectConfig> {

	public ProjectConfigService(Dao dao) {
		super(dao);
	}

	public ProjectConfig get() {
		ProjectConfig conf = dao().fetch(getEntityClass());
		return conf;
	}

	public boolean update(String repoPath, String domainPath) {
		ProjectConfig conf = get();
		conf.setDomainPath(domainPath);
		conf.setRepositoryPath(repoPath);
		dao().update(conf);
		return true;
	}

	public void init() {
		get();
	}
}
