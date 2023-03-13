package cn.rjtech.model.main.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 用户组织关系
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseUserOrg<M extends BaseUserOrg<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String ID = "id";
    /**组织ID*/
    public static final String ORG_ID = "org_id";
    /**用户ID*/
    public static final String USER_ID = "user_id";
    /**职位*/
    public static final String POSITION = "position";
    /**负责人标识：0. 否 1. 是*/
    public static final String IS_PRINCIPAL = "is_principal";
    /**直接上级*/
    public static final String PARENT_PSN_ID = "parent_psn_id";
    /**创建时间*/
    public static final String CREATE_TIME = "create_time";
    /**创建人ID*/
    public static final String CREATE_USER_ID = "create_user_id";
    /**创建人*/
    public static final String CREATE_USER_NAME = "create_user_name";
    /**最后更新时间*/
    public static final String LAST_UPDATE_TIME = "last_update_time";
    /**最后更新人编号*/
    public static final String LAST_UPDATE_ID = "last_update_id";
    /**最后更新人*/
    public static final String LAST_UPDATE_NAME = "last_update_name";
    /**删除标识 0：未删除 1：删除*/
    public static final String IS_DELETED = "is_deleted";
    /**版本号*/
    public static final String VERSION_NUM = "version_num";
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
	 * 组织ID
	 */
	public M setOrgId(java.lang.Long orgId) {
		set("org_id", orgId);
		return (M)this;
	}
	
	/**
	 * 组织ID
	 */
	@JBoltField(name="orgId" ,columnName="org_id",type="Long", remark="组织ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getOrgId() {
		return getLong("org_id");
	}

	/**
	 * 用户ID
	 */
	public M setUserId(java.lang.Long userId) {
		set("user_id", userId);
		return (M)this;
	}
	
	/**
	 * 用户ID
	 */
	@JBoltField(name="userId" ,columnName="user_id",type="Long", remark="用户ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getUserId() {
		return getLong("user_id");
	}

	/**
	 * 职位
	 */
	public M setPosition(java.lang.String position) {
		set("position", position);
		return (M)this;
	}
	
	/**
	 * 职位
	 */
	@JBoltField(name="position" ,columnName="position",type="String", remark="职位", required=false, maxLength=150, fixed=0, order=4)
	public java.lang.String getPosition() {
		return getStr("position");
	}

	/**
	 * 负责人标识：0. 否 1. 是
	 */
	public M setIsPrincipal(java.lang.Boolean isPrincipal) {
		set("is_principal", isPrincipal);
		return (M)this;
	}
	
	/**
	 * 负责人标识：0. 否 1. 是
	 */
	@JBoltField(name="isPrincipal" ,columnName="is_principal",type="Boolean", remark="负责人标识：0. 否 1. 是", required=true, maxLength=1, fixed=0, order=5)
	public java.lang.Boolean getIsPrincipal() {
		return getBoolean("is_principal");
	}

	/**
	 * 直接上级
	 */
	public M setParentPsnId(java.lang.Long parentPsnId) {
		set("parent_psn_id", parentPsnId);
		return (M)this;
	}
	
	/**
	 * 直接上级
	 */
	@JBoltField(name="parentPsnId" ,columnName="parent_psn_id",type="Long", remark="直接上级", required=false, maxLength=19, fixed=0, order=6)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getParentPsnId() {
		return getLong("parent_psn_id");
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
	@JBoltField(name="createTime" ,columnName="create_time",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=7)
	public java.util.Date getCreateTime() {
		return getDate("create_time");
	}

	/**
	 * 创建人ID
	 */
	public M setCreateUserId(java.lang.Long createUserId) {
		set("create_user_id", createUserId);
		return (M)this;
	}
	
	/**
	 * 创建人ID
	 */
	@JBoltField(name="createUserId" ,columnName="create_user_id",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=8)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getCreateUserId() {
		return getLong("create_user_id");
	}

	/**
	 * 创建人
	 */
	public M setCreateUserName(java.lang.String createUserName) {
		set("create_user_name", createUserName);
		return (M)this;
	}
	
	/**
	 * 创建人
	 */
	@JBoltField(name="createUserName" ,columnName="create_user_name",type="String", remark="创建人", required=false, maxLength=100, fixed=0, order=9)
	public java.lang.String getCreateUserName() {
		return getStr("create_user_name");
	}

	/**
	 * 最后更新时间
	 */
	public M setLastUpdateTime(java.util.Date lastUpdateTime) {
		set("last_update_time", lastUpdateTime);
		return (M)this;
	}
	
	/**
	 * 最后更新时间
	 */
	@JBoltField(name="lastUpdateTime" ,columnName="last_update_time",type="Date", remark="最后更新时间", required=false, maxLength=23, fixed=3, order=10)
	public java.util.Date getLastUpdateTime() {
		return getDate("last_update_time");
	}

	/**
	 * 最后更新人编号
	 */
	public M setLastUpdateId(java.lang.Long lastUpdateId) {
		set("last_update_id", lastUpdateId);
		return (M)this;
	}
	
	/**
	 * 最后更新人编号
	 */
	@JBoltField(name="lastUpdateId" ,columnName="last_update_id",type="Long", remark="最后更新人编号", required=false, maxLength=19, fixed=0, order=11)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getLastUpdateId() {
		return getLong("last_update_id");
	}

	/**
	 * 最后更新人
	 */
	public M setLastUpdateName(java.lang.String lastUpdateName) {
		set("last_update_name", lastUpdateName);
		return (M)this;
	}
	
	/**
	 * 最后更新人
	 */
	@JBoltField(name="lastUpdateName" ,columnName="last_update_name",type="String", remark="最后更新人", required=false, maxLength=100, fixed=0, order=12)
	public java.lang.String getLastUpdateName() {
		return getStr("last_update_name");
	}

	/**
	 * 删除标识 0：未删除 1：删除
	 */
	public M setIsDeleted(java.lang.Boolean isDeleted) {
		set("is_deleted", isDeleted);
		return (M)this;
	}
	
	/**
	 * 删除标识 0：未删除 1：删除
	 */
	@JBoltField(name="isDeleted" ,columnName="is_deleted",type="Boolean", remark="删除标识 0：未删除 1：删除", required=true, maxLength=1, fixed=0, order=13)
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("is_deleted");
	}

	/**
	 * 版本号
	 */
	public M setVersionNum(java.lang.Integer versionNum) {
		set("version_num", versionNum);
		return (M)this;
	}
	
	/**
	 * 版本号
	 */
	@JBoltField(name="versionNum" ,columnName="version_num",type="Integer", remark="版本号", required=false, maxLength=10, fixed=0, order=14)
	public java.lang.Integer getVersionNum() {
		return getInt("version_num");
	}

}

