package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 物料建模-存货工艺路线
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseInventoryRouting<M extends BaseInventoryRouting<M>> extends JBoltBaseModel<M>{
    /**主键*/
    public static final String IAUTOID = "iAutoId";
    /**料品档案ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**工艺路线名称*/
    public static final String CROUTINGNAME = "cRoutingName";
    /**工艺版本*/
    public static final String CVERSION = "cVersion";
    /**启用日期*/
    public static final String DFROMDATE = "dFromDate";
    /**停用日期*/
    public static final String DTODATE = "dToDate";
    /**成品率*/
    public static final String IFINISHEDRATE = "iFinishedRate";
    /**工艺要求*/
    public static final String CREQUIREMENT = "cRequirement";
    /**说明*/
    public static final String CMEMO = "cMemo";
    /**是否启用: 0. 否 1. 是*/
    public static final String ISENABLED = "isEnabled";
    /**创建人*/
    public static final String ICREATEBY = "iCreateBy";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**创建人名称*/
    public static final String CCREATENAME = "cCreateName";
    /**审批方式：0. 未知 1. 审核 2. 审批流*/
    public static final String IAUDITWAY = "iAuditWay";
    /**提审时间*/
    public static final String DSUBMITTIME = "dSubmitTime";
    /**审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过*/
    public static final String IAUDITSTATUS = "iAuditStatus";
    /**删除状态：0. 未删除 1. 已删除*/
    public static final String ISDELETED = "isDeleted";
	/**
	 * 主键
	 */
	public M setIAutoId(java.lang.Long iAutoId) {
		set("iAutoId", iAutoId);
		return (M)this;
	}

	/**
	 * 主键
	 */
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Long", remark="主键", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(name = "iautoid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIAutoId() {
		return getLong("iAutoId");
	}

	/**
	 * 料品档案ID
	 */
	public M setIInventoryId(java.lang.Long iInventoryId) {
		set("iInventoryId", iInventoryId);
		return (M)this;
	}

	/**
	 * 料品档案ID
	 */
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="料品档案ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 工艺路线名称
	 */
	public M setCRoutingName(java.lang.String cRoutingName) {
		set("cRoutingName", cRoutingName);
		return (M)this;
	}

	/**
	 * 工艺路线名称
	 */
	@JBoltField(name="croutingname" ,columnName="cRoutingName",type="String", remark="工艺路线名称", required=true, maxLength=50, fixed=0, order=3)
	@JSONField(name = "croutingname")
	public java.lang.String getCRoutingName() {
		return getStr("cRoutingName");
	}

	/**
	 * 工艺版本
	 */
	public M setCVersion(java.lang.String cVersion) {
		set("cVersion", cVersion);
		return (M)this;
	}

	/**
	 * 工艺版本
	 */
	@JBoltField(name="cversion" ,columnName="cVersion",type="String", remark="工艺版本", required=true, maxLength=10, fixed=0, order=4)
	@JSONField(name = "cversion")
	public java.lang.String getCVersion() {
		return getStr("cVersion");
	}

	/**
	 * 启用日期
	 */
	public M setDFromDate(java.util.Date dFromDate) {
		set("dFromDate", dFromDate);
		return (M)this;
	}

	/**
	 * 启用日期
	 */
	@JBoltField(name="dfromdate" ,columnName="dFromDate",type="Date", remark="启用日期", required=false, maxLength=10, fixed=0, order=5)
	@JSONField(name = "dfromdate")
	public java.util.Date getDFromDate() {
		return getDate("dFromDate");
	}

	/**
	 * 停用日期
	 */
	public M setDToDate(java.util.Date dToDate) {
		set("dToDate", dToDate);
		return (M)this;
	}

	/**
	 * 停用日期
	 */
	@JBoltField(name="dtodate" ,columnName="dToDate",type="Date", remark="停用日期", required=false, maxLength=10, fixed=0, order=6)
	@JSONField(name = "dtodate")
	public java.util.Date getDToDate() {
		return getDate("dToDate");
	}

	/**
	 * 成品率
	 */
	public M setIFinishedRate(java.math.BigDecimal iFinishedRate) {
		set("iFinishedRate", iFinishedRate);
		return (M)this;
	}

	/**
	 * 成品率
	 */
	@JBoltField(name="ifinishedrate" ,columnName="iFinishedRate",type="BigDecimal", remark="成品率", required=false, maxLength=24, fixed=6, order=7)
	@JSONField(name = "ifinishedrate")
	public java.math.BigDecimal getIFinishedRate() {
		return getBigDecimal("iFinishedRate");
	}

	/**
	 * 工艺要求
	 */
	public M setCRequirement(java.lang.String cRequirement) {
		set("cRequirement", cRequirement);
		return (M)this;
	}

	/**
	 * 工艺要求
	 */
	@JBoltField(name="crequirement" ,columnName="cRequirement",type="String", remark="工艺要求", required=false, maxLength=200, fixed=0, order=8)
	@JSONField(name = "crequirement")
	public java.lang.String getCRequirement() {
		return getStr("cRequirement");
	}

	/**
	 * 说明
	 */
	public M setCMemo(java.lang.String cMemo) {
		set("cMemo", cMemo);
		return (M)this;
	}

	/**
	 * 说明
	 */
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="说明", required=false, maxLength=200, fixed=0, order=9)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
	}

	/**
	 * 是否启用: 0. 否 1. 是
	 */
	public M setIsEnabled(java.lang.Boolean isEnabled) {
		set("isEnabled", isEnabled);
		return (M)this;
	}

	/**
	 * 是否启用: 0. 否 1. 是
	 */
	@JBoltField(name="isenabled" ,columnName="isEnabled",type="Boolean", remark="是否启用: 0. 否 1. 是", required=true, maxLength=1, fixed=0, order=10)
	@JSONField(name = "isenabled")
	public java.lang.Boolean getIsEnabled() {
		return getBoolean("isEnabled");
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
	 * 创建人名称
	 */
	public M setCCreateName(java.lang.String cCreateName) {
		set("cCreateName", cCreateName);
		return (M)this;
	}

	/**
	 * 创建人名称
	 */
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=13)
	@JSONField(name = "ccreatename")
	public java.lang.String getCCreateName() {
		return getStr("cCreateName");
	}

	/**
	 * 审批方式：0. 未知 1. 审核 2. 审批流
	 */
	public M setIAuditWay(java.lang.Integer iAuditWay) {
		set("iAuditWay", iAuditWay);
		return (M)this;
	}

	/**
	 * 审批方式：0. 未知 1. 审核 2. 审批流
	 */
	@JBoltField(name="iauditway" ,columnName="iAuditWay",type="Integer", remark="审批方式：0. 未知 1. 审核 2. 审批流", required=false, maxLength=10, fixed=0, order=14)
	@JSONField(name = "iauditway")
	public java.lang.Integer getIAuditWay() {
		return getInt("iAuditWay");
	}

	/**
	 * 提审时间
	 */
	public M setDSubmitTime(java.util.Date dSubmitTime) {
		set("dSubmitTime", dSubmitTime);
		return (M)this;
	}

	/**
	 * 提审时间
	 */
	@JBoltField(name="dsubmittime" ,columnName="dSubmitTime",type="Date", remark="提审时间", required=false, maxLength=23, fixed=3, order=15)
	@JSONField(name = "dsubmittime")
	public java.util.Date getDSubmitTime() {
		return getDate("dSubmitTime");
	}

	/**
	 * 审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
	 */
	public M setIAuditStatus(java.lang.Integer iAuditStatus) {
		set("iAuditStatus", iAuditStatus);
		return (M)this;
	}

	/**
	 * 审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
	 */
	@JBoltField(name="iauditstatus" ,columnName="iAuditStatus",type="Integer", remark="审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过", required=true, maxLength=10, fixed=0, order=16)
	@JSONField(name = "iauditstatus")
	public java.lang.Integer getIAuditStatus() {
		return getInt("iAuditStatus");
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
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=17)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

}

