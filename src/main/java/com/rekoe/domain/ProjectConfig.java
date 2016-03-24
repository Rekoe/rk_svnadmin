package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.Table;

@Table("project_config")
public class ProjectConfig implements Serializable {

	private static final long serialVersionUID = 2288585699874782890L;

	@Name
	@Prev(els = { @EL("uuid()") })
	private String id;

	@Column(hump = true)
	@ColDefine(type = ColType.VARCHAR, width = 100)
	@Comment("仓库根路径")
	@Default("/data/repo/")
	private String repositoryPath;

	@Column(hump = true)
	@ColDefine(type = ColType.VARCHAR, width = 100)
	@Default("http://127.0.0.1/")
	private String domainPath;

	public ProjectConfig() {
		super();
	}

	public ProjectConfig(String repositoryPath, String domainPath) {
		super();
		this.repositoryPath = repositoryPath;
		this.domainPath = domainPath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRepositoryPath() {
		return repositoryPath;
	}

	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
	}

	public String getDomainPath() {
		return domainPath;
	}

	public void setDomainPath(String domainPath) {
		this.domainPath = domainPath;
	}

}
