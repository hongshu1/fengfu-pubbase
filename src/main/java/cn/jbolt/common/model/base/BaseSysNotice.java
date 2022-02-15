package cn.jbolt.common.model.base;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;

/**
 * 系统通知
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysNotice<M extends BaseSysNotice<M>> extends JBoltBaseModel<M>{

	/**
	 * 主键ID
	 */
	public M setId(java.lang.Long id) {
		set("id", id);
		return (M)this;
	}
	
	/**
	 * 主键ID
	 */
	@JBoltField(name="id" ,columnName="id",type="Long", remark="主键ID", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getId() {
		return getLong("id");
	}

	/**
	 * 标题
	 */
	public M setTitle(java.lang.String title) {
		set("title", title);
		return (M)this;
	}
	
	/**
	 * 标题
	 */
	@JBoltField(name="title" ,columnName="title",type="String", remark="标题", required=true, maxLength=255, fixed=0, order=2)
	public java.lang.String getTitle() {
		return getStr("title");
	}

	/**
	 * 消息内容
	 */
	public M setContent(java.lang.String content) {
		set("content", content);
		return (M)this;
	}
	
	/**
	 * 消息内容
	 */
	@JBoltField(name="content" ,columnName="content",type="String", remark="消息内容", required=true, maxLength=65535, fixed=0, order=3)
	public java.lang.String getContent() {
		return getStr("content");
	}

	/**
	 * 通知类型
	 */
	public M setType(java.lang.Integer type) {
		set("type", type);
		return (M)this;
	}
	
	/**
	 * 通知类型
	 */
	@JBoltField(name="type" ,columnName="type",type="Integer", remark="通知类型", required=true, maxLength=10, fixed=0, order=4)
	public java.lang.Integer getType() {
		return getInt("type");
	}

	/**
	 * 优先级
	 */
	public M setPriorityLevel(java.lang.Integer priorityLevel) {
		set("priority_level", priorityLevel);
		return (M)this;
	}
	
	/**
	 * 优先级
	 */
	@JBoltField(name="priorityLevel" ,columnName="priority_level",type="Integer", remark="优先级", required=true, maxLength=10, fixed=0, order=5)
	public java.lang.Integer getPriorityLevel() {
		return getInt("priority_level");
	}

	/**
	 * 已读人数
	 */
	public M setReadCount(java.lang.Integer readCount) {
		set("read_count", readCount);
		return (M)this;
	}
	
	/**
	 * 已读人数
	 */
	@JBoltField(name="readCount" ,columnName="read_count",type="Integer", remark="已读人数", required=false, maxLength=10, fixed=0, order=6)
	public java.lang.Integer getReadCount() {
		return getInt("read_count");
	}

	/**
	 * 创建时间
	 */
	public M setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
		return (M)this;
	}
	
	/**
	 * 创建时间
	 */
	@JBoltField(name="createTime" ,columnName="create_time",type="Date", remark="创建时间", required=true, maxLength=19, fixed=0, order=7)
	public java.util.Date getCreateTime() {
		return getDate("create_time");
	}

	/**
	 * 更新时间
	 */
	public M setUpdateTime(java.util.Date updateTime) {
		set("update_time", updateTime);
		return (M)this;
	}
	
	/**
	 * 更新时间
	 */
	@JBoltField(name="updateTime" ,columnName="update_time",type="Date", remark="更新时间", required=false, maxLength=19, fixed=0, order=8)
	public java.util.Date getUpdateTime() {
		return getDate("update_time");
	}

	/**
	 * 创建人
	 */
	public M setCreateUserId(java.lang.Long createUserId) {
		set("create_user_id", createUserId);
		return (M)this;
	}
	
	/**
	 * 创建人
	 */
	@JBoltField(name="createUserId" ,columnName="create_user_id",type="Long", remark="创建人", required=false, maxLength=19, fixed=0, order=9)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getCreateUserId() {
		return getLong("create_user_id");
	}

	/**
	 * 更新人
	 */
	public M setUpdateUserId(java.lang.Long updateUserId) {
		set("update_user_id", updateUserId);
		return (M)this;
	}
	
	/**
	 * 更新人
	 */
	@JBoltField(name="updateUserId" ,columnName="update_user_id",type="Long", remark="更新人", required=false, maxLength=19, fixed=0, order=10)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getUpdateUserId() {
		return getLong("update_user_id");
	}

	/**
	 * 接收人类型
	 */
	public M setReceiverType(java.lang.Integer receiverType) {
		set("receiver_type", receiverType);
		return (M)this;
	}
	
	/**
	 * 接收人类型
	 */
	@JBoltField(name="receiverType" ,columnName="receiver_type",type="Integer", remark="接收人类型", required=false, maxLength=10, fixed=0, order=11)
	public java.lang.Integer getReceiverType() {
		return getInt("receiver_type");
	}

	/**
	 * 接收人值
	 */
	public M setReceiverValue(java.lang.String receiverValue) {
		set("receiver_value", receiverValue);
		return (M)this;
	}
	
	/**
	 * 接收人值
	 */
	@JBoltField(name="receiverValue" ,columnName="receiver_value",type="String", remark="接收人值", required=false, maxLength=65535, fixed=0, order=12)
	public java.lang.String getReceiverValue() {
		return getStr("receiver_value");
	}

	/**
	 * 附件
	 */
	public M setFiles(java.lang.String files) {
		set("files", files);
		return (M)this;
	}
	
	/**
	 * 附件
	 */
	@JBoltField(name="files" ,columnName="files",type="String", remark="附件", required=false, maxLength=255, fixed=0, order=13)
	public java.lang.String getFiles() {
		return getStr("files");
	}

	/**
	 * 删除标志
	 */
	public M setDelFlag(java.lang.Boolean delFlag) {
		set("del_flag", delFlag);
		return (M)this;
	}
	
	/**
	 * 删除标志
	 */
	@JBoltField(name="delFlag" ,columnName="del_flag",type="Boolean", remark="删除标志", required=false, maxLength=1, fixed=0, order=14)
	public java.lang.Boolean getDelFlag() {
		return getBoolean("del_flag");
	}

}

