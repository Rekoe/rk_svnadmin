package com.rekoe.domain;

import java.io.Serializable;
import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("pay_refund")
@TableIndexes({ @Index(name = "pay_refund_index", fields = { "paymentid" }, unique = false), @Index(name = "pay_status_index", fields = { "status" }, unique = false) })
public class PayRefund implements Serializable {

	private static final long serialVersionUID = -6311504223475095588L;
	@Name
	@Comment("ID主键")
	@Prev(els = { @EL("uuid()") })
	private String id;
	@Column
	@ColDefine(type = ColType.TIMESTAMP)
	@Comment("创建时间")
	private Date createTime;
	@Column
	private String passportid;
	@Column("payment_id")
	private String paymentid;
	@Column
	private String name;
	@Column(hump = true)
	private String requestId;

	@Column(hump = true)
	@ColDefine(type = ColType.TEXT)
	private String changedFields;

	@Column("do_status")
	@Comment("订单处理状态")
	private boolean status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPassportid() {
		return passportid;
	}

	public void setPassportid(String passportid) {
		this.passportid = passportid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPaymentid() {
		return paymentid;
	}

	public void setPaymentid(String paymentid) {
		this.paymentid = paymentid;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getChangedFields() {
		return changedFields;
	}

	public void setChangedFields(String changedFields) {
		this.changedFields = changedFields;
	}

}
