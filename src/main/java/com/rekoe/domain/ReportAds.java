package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("sg_report_ads")
@TableIndexes({ @Index(name = "report_ads_index", fields = { "pid", "pfid" }, unique = false) })
public class ReportAds implements Serializable {

	private static final long serialVersionUID = -8361228159606140014L;

	@Name
	@Prev(els = { @EL("uuid()") })
	private String id;

	@Column
	private int pid;

	@Column
	private String pfid;

	@Column
	@ColDefine(type = ColType.VARCHAR, width = 100)
	private String idfa;

	@Column
	@ColDefine(type = ColType.VARCHAR, width = 100)
	private String idfv;

	@Column(hump = true)
	@ColDefine(type = ColType.VARCHAR, width = 100)
	private String talkingDataId;

	public ReportAds() {
		super();
	}

	public ReportAds(int pid, String pfid, String idfa, String idfv, String talkingDataId) {
		super();
		this.pid = pid;
		this.pfid = pfid;
		this.idfa = idfa;
		this.idfv = idfv;
		this.talkingDataId = talkingDataId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getPfid() {
		return pfid;
	}

	public void setPfid(String pfid) {
		this.pfid = pfid;
	}

	public String getIdfa() {
		return idfa;
	}

	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}

	public String getIdfv() {
		return idfv;
	}

	public void setIdfv(String idfv) {
		this.idfv = idfv;
	}

	public String getTalkingDataId() {
		return talkingDataId;
	}

	public void setTalkingDataId(String talkingDataId) {
		this.talkingDataId = talkingDataId;
	}

}
