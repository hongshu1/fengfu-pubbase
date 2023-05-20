package cn.rjtech.model.main.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 数据权限
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseDataPermission<M extends BaseDataPermission<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String ID = "id";
    /**业务对象ID*/
    public static final String BUSOBJECT_ID = "busobject_id";
    /**授权类型：1. 角色 2. 用户*/
    public static final String OBJECT_TYPE = "object_type";
    /**授权对象ID*/
    public static final String OBJECT_ID = "object_id";
    /**业务对象值ID*/
    public static final String BUSOBJECT_VALUE_ID = "busobject_value_id";
    /**业务对象值名称*/
    public static final String BUSOBJECT_VALUE_NAME = "busobject_value_name";
    /**查看权限：0. 禁用 1. 启用*/
    public static final String IS_VIEW_ENABLED = "is_view_enabled";
    /**编辑权限：0. 禁用 1. 启用*/
    public static final String IS_EDIT_ENABLED = "is_edit_enabled";
    /**删除权限：0. 禁用 1. 启用*/
    public static final String IS_DELETE_ENABLED = "is_delete_enabled";
    /**版本号*/
    public static final String VERSION_NUM = "version_num";
    /**创建用户ID*/
    public static final String CREATE_USER_ID = "create_user_id";
    /**创建用户名称*/
    public static final String CREATE_USER_NAME = "create_user_name";
    /**创建时间*/
    public static final String CREATE_TIME = "create_time";
    /**更新用户ID*/
    public static final String LAST_UPDATE_ID = "last_update_id";
    /**更新用户名称*/
    public static final String LAST_UPDATE_NAME = "last_update_name";
    /**更新时间*/
    public static final String LAST_UPDATE_TIME = "last_update_time";
    /**删除状态：0. 未删除 1. 已删除*/
    public static final String IS_DELETED = "is_deleted";
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
	 * 业务对象ID
	 */
	public M setBusobjectId(java.lang.Long busobjectId) {
		set("busobject_id", busobjectId);
		return (M)this;
	}
	
	/**
	 * 业务对象ID
	 */
	@JBoltField(name="busobjectId" ,columnName="busobject_id",type="Long", remark="业务对象ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getBusobjectId() {
		return getLong("busobject_id");
	}

	/**
	 * 授权类型：1. 角色 2. 用户
	 */
	public M setObjectType(java.lang.Integer objectType) {
		set("object_type", objectType);
		return (M)this;
	}
	
	/**
	 * 授权类型：1. 角色 2. 用户
	 */
	@JBoltField(name="objectType" ,columnName="object_type",type="Integer", remark="授权类型：1. 角色 2. 用户", required=true, maxLength=10, fixed=0, order=3)
	public java.lang.Integer getObjectType() {
		return getInt("object_type");
	}

	/**
	 * 授权对象ID
	 */
	public M setObjectId(java.lang.Long objectId) {
		set("object_id", objectId);
		return (M)this;
	}
	
	/**
	 * 授权对象ID
	 */
	@JBoltField(name="objectId" ,columnName="object_id",type="Long", remark="授权对象ID", required=true, maxLength=19, fixed=0, order=4)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getObjectId() {
		return getLong("object_id");
	}

	/**
	 * 业务对象值ID
	 */
	public M setBusobjectValueId(java.lang.Long busobjectValueId) {
		set("busobject_value_id", busobjectValueId);
		return (M)this;
	}
	
	/**
	 * 业务对象值ID
	 */
	@JBoltField(name="busobjectValueId" ,columnName="busobject_value_id",type="Long", remark="业务对象值ID", required=false, maxLength=19, fixed=0, order=5)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getBusobjectValueId() {
		return getLong("busobject_value_id");
	}

	/**
	 * 业务对象值名称
	 */
	public M setBusobjectValueName(java.lang.String busobjectValueName) {
		set("busobject_value_name", busobjectValueName);
		return (M)this;
	}
	
	/**
	 * 业务对象值名称
	 */
	@JBoltField(name="busobjectValueName" ,columnName="busobject_value_name",type="String", remark="业务对象值名称", required=false, maxLength=200, fixed=0, order=6)
	public java.lang.String getBusobjectValueName() {
		return getStr("busobject_value_name");
	}

	/**
	 * 查看权限：0. 禁用 1. 启用
	 */
	public M setIsViewEnabled(java.lang.Boolean isViewEnabled) {
		set("is_view_enabled", isViewEnabled);
		return (M)this;
	}
	
	/**
	 * 查看权限：0. 禁用 1. 启用
	 */
	@JBoltField(name="isViewEnabled" ,columnName="is_view_enabled",type="Boolean", remark="查看权限：0. 禁用 1. 启用", required=true, maxLength=1, fixed=0, order=7)
	public java.lang.Boolean getIsViewEnabled() {
		return getBoolean("is_view_enabled");
	}

	/**
	 * 编辑权限：0. 禁用 1. 启用
	 */
	public M setIsEditEnabled(java.lang.Boolean isEditEnabled) {
		set("is_edit_enabled", isEditEnabled);
		return (M)this;
	}
	
	/**
	 * 编辑权限：0. 禁用 1. 启用
	 */
	@JBoltField(name="isEditEnabled" ,columnName="is_edit_enabled",type="Boolean", remark="编辑权限：0. 禁用 1. 启用", required=true, maxLength=1, fixed=0, order=8)
	public java.lang.Boolean getIsEditEnabled() {
		return getBoolean("is_edit_enabled");
	}

	/**
	 * 删除权限：0. 禁用 1. 启用
	 */
	public M setIsDeleteEnabled(java.lang.Boolean isDeleteEnabled) {
		set("is_delete_enabled", isDeleteEnabled);
		return (M)this;
	}
	
	/**
	 * 删除权限：0. 禁用 1. 启用
	 */
	@JBoltField(name="isDeleteEnabled" ,columnName="is_delete_enabled",type="Boolean", remark="删除权限：0. 禁用 1. 启用", required=true, maxLength=1, fixed=0, order=9)
	public java.lang.Boolean getIsDeleteEnabled() {
		return getBoolean("is_delete_enabled");
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
	@JBoltField(name="versionNum" ,columnName="version_num",type="Integer", remark="版本号", required=true, maxLength=10, fixed=0, order=10)
	public java.lang.Integer getVersionNum() {
		return getInt("version_num");
	}

	/**
	 * 创建用户ID
	 */
	public M setCreateUserId(java.lang.Long createUserId) {
		set("create_user_id", createUserId);
		return (M)this;
	}
	
	/**
	 * 创建用户ID
	 */
	@JBoltField(name="createUserId" ,columnName="create_user_id",type="Long", remark="创建用户ID", required=true, maxLength=19, fixed=0, order=11)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getCreateUserId() {
		return getLong("create_user_id");
	}

	/**
	 * 创建用户名称
	 */
	public M setCreateUserName(java.lang.String createUserName) {
		set("create_user_name", createUserName);
		return (M)this;
	}
	
	/**
	 * 创建用户名称
	 */
	@JBoltField(name="createUserName" ,columnName="create_user_name",type="String", remark="创建用户名称", required=true, maxLength=50, fixed=0, order=12)
	public java.lang.String getCreateUserName() {
		return getStr("create_user_name");
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
	@JBoltField(name="createTime" ,columnName="create_time",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=13)
	public java.util.Date getCreateTime() {
		return getDate("create_time");
	}

	/**
	 * 更新用户ID
	 */
	public M setLastUpdateId(java.lang.Long lastUpdateId) {
		set("last_update_id", lastUpdateId);
		return (M)this;
	}
	
	/**
	 * 更新用户ID
	 */
	@JBoltField(name="lastUpdateId" ,columnName="last_update_id",type="Long", remark="更新用户ID", required=true, maxLength=19, fixed=0, order=14)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getLastUpdateId() {
		return getLong("last_update_id");
	}

	/**
	 * 更新用户名称
	 */
	public M setLastUpdateName(java.lang.String lastUpdateName) {
		set("last_update_name", lastUpdateName);
		return (M)this;
	}
	
	/**
	 * 更新用户名称
	 */
	@JBoltField(name="lastUpdateName" ,columnName="last_update_name",type="String", remark="更新用户名称", required=true, maxLength=50, fixed=0, order=15)
	public java.lang.String getLastUpdateName() {
		return getStr("last_update_name");
	}

	/**
	 * 更新时间
	 */
	public M setLastUpdateTime(java.util.Date lastUpdateTime) {
		set("last_update_time", lastUpdateTime);
		return (M)this;
	}
	
	/**
	 * 更新时间
	 */
	@JBoltField(name="lastUpdateTime" ,columnName="last_update_time",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=16)
	public java.util.Date getLastUpdateTime() {
		return getDate("last_update_time");
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	public M setIsDeleted(java.lang.Boolean isDeleted) {
		set("is_deleted", isDeleted);
		return (M)this;
	}
	
	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	@JBoltField(name="isDeleted" ,columnName="is_deleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=17)
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("is_deleted");
	}

}

