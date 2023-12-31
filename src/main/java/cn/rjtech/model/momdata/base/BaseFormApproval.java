package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 系统设置-表单审批流
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseFormApproval<M extends BaseFormApproval<M>> extends JBoltBaseModel<M>{
    public static final String DATASOURCE_CONFIG_NAME = "momdata";
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**审批流ID*/
    public static final String IAPPROVALID = "iApprovalId";
    /**表单ID*/
    public static final String IFORMID = "iFormId";
    /**单据ID*/
    public static final String IFORMOBJECTID = "iFormObjectId";
    /**是否自动去重：0. 否 1. 是*/
    public static final String ISSKIPPEDONDUPLICATE = "isSkippedOnDuplicate";
    /**是否自动通过：0. 否 1. 是*/
    public static final String ISAPPROVEDONSAME = "isApprovedOnSame";
    /**创建人ID*/
    public static final String ICREATEBY = "iCreateBy";
    /**创建人名称*/
    public static final String CCREATENAME = "cCreateName";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**删除状态：0. 未删除 1. 已删除*/
    public static final String ISDELETED = "isDeleted";
    /**审批流类型：1. 默认审批流 2. 自定审批流*/
    public static final String IAPPROVALTYPE = "iApprovalType";
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
	@JBoltField(name="corgcode" ,columnName="cOrgCode",type="String", remark="组织编码", required=true, maxLength=40, fixed=0, order=3)
	@JSONField(name = "corgcode")
	public java.lang.String getCOrgCode() {
		return getStr("cOrgCode");
	}

	/**
	 * 组织名称
	 */
	public M setCOrgName(java.lang.String cOrgName) {
		set("cOrgName", cOrgName);
		return (M)this;
	}

	/**
	 * 组织名称
	 */
	@JBoltField(name="corgname" ,columnName="cOrgName",type="String", remark="组织名称", required=true, maxLength=50, fixed=0, order=4)
	@JSONField(name = "corgname")
	public java.lang.String getCOrgName() {
		return getStr("cOrgName");
	}

	/**
	 * 审批流ID
	 */
	public M setIApprovalId(java.lang.Long iApprovalId) {
		set("iApprovalId", iApprovalId);
		return (M)this;
	}

	/**
	 * 审批流ID
	 */
	@JBoltField(name="iapprovalid" ,columnName="iApprovalId",type="Long", remark="审批流ID", required=false, maxLength=19, fixed=0, order=5)
	@JSONField(name = "iapprovalid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIApprovalId() {
		return getLong("iApprovalId");
	}

	/**
	 * 表单ID
	 */
	public M setIFormId(java.lang.Long iFormId) {
		set("iFormId", iFormId);
		return (M)this;
	}

	/**
	 * 表单ID
	 */
	@JBoltField(name="iformid" ,columnName="iFormId",type="Long", remark="表单ID", required=true, maxLength=19, fixed=0, order=6)
	@JSONField(name = "iformid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIFormId() {
		return getLong("iFormId");
	}

	/**
	 * 单据ID
	 */
	public M setIFormObjectId(java.lang.Long iFormObjectId) {
		set("iFormObjectId", iFormObjectId);
		return (M)this;
	}

	/**
	 * 单据ID
	 */
	@JBoltField(name="iformobjectid" ,columnName="iFormObjectId",type="Long", remark="单据ID", required=true, maxLength=19, fixed=0, order=7)
	@JSONField(name = "iformobjectid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIFormObjectId() {
		return getLong("iFormObjectId");
	}

	/**
	 * 是否自动去重：0. 否 1. 是
	 */
	public M setIsSkippedOnDuplicate(java.lang.Boolean isSkippedOnDuplicate) {
		set("isSkippedOnDuplicate", isSkippedOnDuplicate);
		return (M)this;
	}

	/**
	 * 是否自动去重：0. 否 1. 是
	 */
	@JBoltField(name="isskippedonduplicate" ,columnName="isSkippedOnDuplicate",type="Boolean", remark="是否自动去重：0. 否 1. 是", required=true, maxLength=1, fixed=0, order=8)
	@JSONField(name = "isskippedonduplicate")
	public java.lang.Boolean getIsSkippedOnDuplicate() {
		return getBoolean("isSkippedOnDuplicate");
	}

	/**
	 * 是否自动通过：0. 否 1. 是
	 */
	public M setIsApprovedOnSame(java.lang.Boolean isApprovedOnSame) {
		set("isApprovedOnSame", isApprovedOnSame);
		return (M)this;
	}

	/**
	 * 是否自动通过：0. 否 1. 是
	 */
	@JBoltField(name="isapprovedonsame" ,columnName="isApprovedOnSame",type="Boolean", remark="是否自动通过：0. 否 1. 是", required=true, maxLength=1, fixed=0, order=9)
	@JSONField(name = "isapprovedonsame")
	public java.lang.Boolean getIsApprovedOnSame() {
		return getBoolean("isApprovedOnSame");
	}

	/**
	 * 创建人ID
	 */
	public M setICreateBy(java.lang.Long iCreateBy) {
		set("iCreateBy", iCreateBy);
		return (M)this;
	}

	/**
	 * 创建人ID
	 */
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=10)
	@JSONField(name = "icreateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICreateBy() {
		return getLong("iCreateBy");
	}

	/**
	 * 创建人名称
	 */
	public M setCCreateName(java.lang.String cCreateName) {
		set("cCreateName", cCreateName);
		return (M)this;
	}

	/**
	 * 创建人名称
	 */
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=11)
	@JSONField(name = "ccreatename")
	public java.lang.String getCCreateName() {
		return getStr("cCreateName");
	}

	/**
	 * 创建时间
	 */
	public M setDCreateTime(java.util.Date dCreateTime) {
		set("dCreateTime", dCreateTime);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=12)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDCreateTime() {
		return getDate("dCreateTime");
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	public M setIsDeleted(java.lang.Boolean isDeleted) {
		set("isDeleted", isDeleted);
		return (M)this;
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=13)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 审批流类型：1. 默认审批流 2. 自定审批流
	 */
	public M setIApprovalType(java.lang.Integer iApprovalType) {
		set("iApprovalType", iApprovalType);
		return (M)this;
	}

	/**
	 * 审批流类型：1. 默认审批流 2. 自定审批流
	 */
	@JBoltField(name="iapprovaltype" ,columnName="iApprovalType",type="Integer", remark="审批流类型：1. 默认审批流 2. 自定审批流", required=true, maxLength=10, fixed=0, order=14)
	@JSONField(name = "iapprovaltype")
	public java.lang.Integer getIApprovalType() {
		return getInt("iApprovalType");
	}

}

