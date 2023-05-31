package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 科目对照主表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseSubjectm<M extends BaseSubjectm<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**科目编码*/
    public static final String CSUBJECTCODE = "cSubjectCode";
    /**科目名称*/
    public static final String CSUBJECTNAME = "cSubjectName";
    /**上级科目*/
    public static final String IPARENTID = "iParentId";
    /**科目等级(1-4级)*/
    public static final String CLEVEL = "cLevel";
    /**是否末级*/
    public static final String ISEND = "isEnd";
    /**创建时间*/
    public static final String CREATETIME = "CreateTime";
    /**更新时间*/
    public static final String UPDATETIME = "UpdateTime";
    /**创建人*/
    public static final String ICREATEBY = "iCreateBy";
    /**更新人*/
    public static final String IUPDATEBY = "iUpdateBy";
    /**版本*/
    public static final String CVERSION = "cVersion";
    /**是否启用*/
    public static final String ISENABLED = "IsEnabled";
    /**1（删除）*/
    public static final String ISDELETE = "isDelete";
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
	 * 组织ID
	 */
	public M setIOrgId(java.lang.Long iOrgId) {
		set("iOrgId", iOrgId);
		return (M)this;
	}

	/**
	 * 组织ID
	 */
	@JBoltField(name="iorgid" ,columnName="iOrgId",type="Long", remark="组织ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iorgid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIOrgId() {
		return getLong("iOrgId");
	}

	/**
	 * 组织编码
	 */
	public M setCOrgCode(java.lang.String cOrgCode) {
		set("cOrgCode", cOrgCode);
		return (M)this;
	}

	/**
	 * 组织编码
	 */
	@JBoltField(name="corgcode" ,columnName="cOrgCode",type="String", remark="组织编码", required=true, maxLength=20, fixed=0, order=3)
	@JSONField(name = "corgcode")
	public java.lang.String getCOrgCode() {
		return getStr("cOrgCode");
	}

	/**
	 * 科目编码
	 */
	public M setCSubjectCode(java.lang.String cSubjectCode) {
		set("cSubjectCode", cSubjectCode);
		return (M)this;
	}

	/**
	 * 科目编码
	 */
	@JBoltField(name="csubjectcode" ,columnName="cSubjectCode",type="String", remark="科目编码", required=true, maxLength=30, fixed=0, order=4)
	@JSONField(name = "csubjectcode")
	public java.lang.String getCSubjectCode() {
		return getStr("cSubjectCode");
	}

	/**
	 * 科目名称
	 */
	public M setCSubjectName(java.lang.String cSubjectName) {
		set("cSubjectName", cSubjectName);
		return (M)this;
	}

	/**
	 * 科目名称
	 */
	@JBoltField(name="csubjectname" ,columnName="cSubjectName",type="String", remark="科目名称", required=true, maxLength=30, fixed=0, order=5)
	@JSONField(name = "csubjectname")
	public java.lang.String getCSubjectName() {
		return getStr("cSubjectName");
	}

	/**
	 * 上级科目
	 */
	public M setIParentId(java.lang.Long iParentId) {
		set("iParentId", iParentId);
		return (M)this;
	}

	/**
	 * 上级科目
	 */
	@JBoltField(name="iparentid" ,columnName="iParentId",type="Long", remark="上级科目", required=false, maxLength=19, fixed=0, order=6)
	@JSONField(name = "iparentid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIParentId() {
		return getLong("iParentId");
	}

	/**
	 * 科目等级(1-4级)
	 */
	public M setCLevel(java.lang.Integer cLevel) {
		set("cLevel", cLevel);
		return (M)this;
	}

	/**
	 * 科目等级(1-4级)
	 */
	@JBoltField(name="clevel" ,columnName="cLevel",type="Integer", remark="科目等级(1-4级)", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "clevel")
	public java.lang.Integer getCLevel() {
		return getInt("cLevel");
	}

	/**
	 * 是否末级
	 */
	public M setIsEnd(java.lang.Boolean isEnd) {
		set("isEnd", isEnd);
		return (M)this;
	}

	/**
	 * 是否末级
	 */
	@JBoltField(name="isend" ,columnName="isEnd",type="Boolean", remark="是否末级", required=true, maxLength=1, fixed=0, order=8)
	@JSONField(name = "isend")
	public java.lang.Boolean getIsEnd() {
		return getBoolean("isEnd");
	}

	/**
	 * 创建时间
	 */
	public M setCreateTime(java.util.Date CreateTime) {
		set("CreateTime", CreateTime);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="createtime" ,columnName="CreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=9)
	@JSONField(name = "createtime")
	public java.util.Date getCreateTime() {
		return getDate("CreateTime");
	}

	/**
	 * 更新时间
	 */
	public M setUpdateTime(java.util.Date UpdateTime) {
		set("UpdateTime", UpdateTime);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="updatetime" ,columnName="UpdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=10)
	@JSONField(name = "updatetime")
	public java.util.Date getUpdateTime() {
		return getDate("UpdateTime");
	}

	/**
	 * 创建人
	 */
	public M setICreateBy(java.lang.Long iCreateBy) {
		set("iCreateBy", iCreateBy);
		return (M)this;
	}

	/**
	 * 创建人
	 */
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=11)
	@JSONField(name = "icreateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICreateBy() {
		return getLong("iCreateBy");
	}

	/**
	 * 更新人
	 */
	public M setIUpdateBy(java.lang.Long iUpdateBy) {
		set("iUpdateBy", iUpdateBy);
		return (M)this;
	}

	/**
	 * 更新人
	 */
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人", required=false, maxLength=19, fixed=0, order=12)
	@JSONField(name = "iupdateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUpdateBy() {
		return getLong("iUpdateBy");
	}

	/**
	 * 版本
	 */
	public M setCVersion(java.lang.String cVersion) {
		set("cVersion", cVersion);
		return (M)this;
	}

	/**
	 * 版本
	 */
	@JBoltField(name="cversion" ,columnName="cVersion",type="String", remark="版本", required=false, maxLength=30, fixed=0, order=13)
	@JSONField(name = "cversion")
	public java.lang.String getCVersion() {
		return getStr("cVersion");
	}

	/**
	 * 是否启用
	 */
	public M setIsEnabled(java.lang.Boolean IsEnabled) {
		set("IsEnabled", IsEnabled);
		return (M)this;
	}

	/**
	 * 是否启用
	 */
	@JBoltField(name="isenabled" ,columnName="IsEnabled",type="Boolean", remark="是否启用", required=false, maxLength=1, fixed=0, order=14)
	@JSONField(name = "isenabled")
	public java.lang.Boolean getIsEnabled() {
		return getBoolean("IsEnabled");
	}

	/**
	 * 1（删除）
	 */
	public M setIsDelete(java.lang.Integer isDelete) {
		set("isDelete", isDelete);
		return (M)this;
	}

	/**
	 * 1（删除）
	 */
	@JBoltField(name="isdelete" ,columnName="isDelete",type="Integer", remark="1（删除）", required=false, maxLength=10, fixed=0, order=15)
	@JSONField(name = "isdelete")
	public java.lang.Integer getIsDelete() {
		return getInt("isDelete");
	}

}

