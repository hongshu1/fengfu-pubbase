package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 系统配置-审批节点角色用户
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseFormapprovaldRoleusers<M extends BaseFormapprovaldRoleusers<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**角色ID*/
    public static final String IFORMAPPROVALDROLEID = "iFormApprovaldRoleId";
    /**顺序值*/
    public static final String ISEQ = "iSeq";
    /**用户ID*/
    public static final String IUSERID = "iUserId";
	/**
	 * 主键ID
	 */
	public M setIAutoId(java.lang.Long iAutoId) {
		set("iAutoId", iAutoId);
		return (M)this;
	}

	/**
	 * 主键ID
	 */
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Long", remark="主键ID", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(name = "iautoid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIAutoId() {
		return getLong("iAutoId");
	}

	/**
	 * 角色ID
	 */
	public M setIFormApprovaldRoleId(java.lang.Long iFormApprovaldRoleId) {
		set("iFormApprovaldRoleId", iFormApprovaldRoleId);
		return (M)this;
	}

	/**
	 * 角色ID
	 */
	@JBoltField(name="iformapprovaldroleid" ,columnName="iFormApprovaldRoleId",type="Long", remark="角色ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iformapprovaldroleid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIFormApprovaldRoleId() {
		return getLong("iFormApprovaldRoleId");
	}

	/**
	 * 顺序值
	 */
	public M setISeq(java.lang.Integer iSeq) {
		set("iSeq", iSeq);
		return (M)this;
	}

	/**
	 * 顺序值
	 */
	@JBoltField(name="iseq" ,columnName="iSeq",type="Integer", remark="顺序值", required=true, maxLength=10, fixed=0, order=3)
	@JSONField(name = "iseq")
	public java.lang.Integer getISeq() {
		return getInt("iSeq");
	}

	/**
	 * 用户ID
	 */
	public M setIUserId(java.lang.Long iUserId) {
		set("iUserId", iUserId);
		return (M)this;
	}

	/**
	 * 用户ID
	 */
	@JBoltField(name="iuserid" ,columnName="iUserId",type="Long", remark="用户ID", required=true, maxLength=19, fixed=0, order=4)
	@JSONField(name = "iuserid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUserId() {
		return getLong("iUserId");
	}

}

