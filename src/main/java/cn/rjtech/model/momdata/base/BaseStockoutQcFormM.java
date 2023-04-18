package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 质量管理-出库检
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseStockoutQcFormM<M extends BaseStockoutQcFormM<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**检验单号*/
    public static final String CSTOCKOUTQCFORMNO = "cStockoutQcFormNo";
    /**供应商ID*/
    public static final String IVENDORID = "iVendorId";
    /**质检表格ID*/
    public static final String IQCFORMID = "iQcFormId";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**数量*/
    public static final String IQTY = "iQty";
    /**批次号*/
    public static final String CBATCHNO = "cBatchNo";
    /**检验结果：0. 未生成 1. 待检 2. 不合格 3. 合格*/
    public static final String ISTATUS = "iStatus";
    /**是否已完成：0. 未完成 1. 已完成*/
    public static final String ISCOMPLETED = "isCompleted";
    /**检验员Id, 系统用户ID*/
    public static final String IQCUSERID = "iQcUserId";
    /**测定目的*/
    public static final String CMEASUREPURPOSE = "cMeasurePurpose";
    /**测定理由*/
    public static final String CMEASUREREASON = "cMeasureReason";
    /**设变号*/
    public static final String CDCNO = "cDcNo";
    /**测定单位*/
    public static final String CMEASUREUNIT = "cMeasureUnit";
    /**检验日期*/
    public static final String DQCDATE = "dQcDate";
    /**是否CPK数值的输入内容按正负数显示：0. 否 1. 是*/
    public static final String ISCPKSIGNED = "isCpkSigned";
    /**是否判定合格：0. 否 1. 是*/
    public static final String ISOK = "isOk";
    /**备注*/
    public static final String CMEMO = "cMemo";
    /**创建人ID*/
    public static final String ICREATEBY = "iCreateBy";
    /**创建人名称*/
    public static final String CCREATENAME = "cCreateName";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**更新人ID*/
    public static final String IUPDATEBY = "iUpdateBy";
    /**更新人名称*/
    public static final String CUPDATENAME = "cUpdateName";
    /**更新时间*/
    public static final String DUPDATETIME = "dUpdateTime";
    /**删除状态：0. 未删除 1. 已删除*/
    public static final String ISDELETED = "IsDeleted";
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
	 * 检验单号
	 */
	public M setCStockoutQcFormNo(java.lang.String cStockoutQcFormNo) {
		set("cStockoutQcFormNo", cStockoutQcFormNo);
		return (M)this;
	}

	/**
	 * 检验单号
	 */
	@JBoltField(name="cstockoutqcformno" ,columnName="cStockoutQcFormNo",type="String", remark="检验单号", required=true, maxLength=50, fixed=0, order=5)
	@JSONField(name = "cstockoutqcformno")
	public java.lang.String getCStockoutQcFormNo() {
		return getStr("cStockoutQcFormNo");
	}

	/**
	 * 客户ID
	 */
	public M setICustomerId(java.lang.Long iCustomerId) {
		set("iCustomerId", iCustomerId);
		return (M)this;
	}

	/**
	 * 供应商ID
	 */
	@JBoltField(name="icustomerid" ,columnName="iCustomerId",type="Long", remark="客户ID", required=true, maxLength=19, fixed=0, order=6)
	@JSONField(name = "icustomerid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICustomerId() {
		return getLong("iCustomerId");
	}

	/**
	 * 质检表格ID
	 */
	public M setIQcFormId(java.lang.Long iQcFormId) {
		set("iQcFormId", iQcFormId);
		return (M)this;
	}

	/**
	 * 质检表格ID
	 */
	@JBoltField(name="iqcformid" ,columnName="iQcFormId",type="Long", remark="质检表格ID", required=true, maxLength=19, fixed=0, order=7)
	@JSONField(name = "iqcformid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIQcFormId() {
		return getLong("iQcFormId");
	}

	/**
	 * 存货ID
	 */
	public M setIInventoryId(java.lang.Long iInventoryId) {
		set("iInventoryId", iInventoryId);
		return (M)this;
	}

	/**
	 * 存货ID
	 */
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="存货ID", required=true, maxLength=19, fixed=0, order=8)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 数量
	 */
	public M setIQty(java.math.BigDecimal iQty) {
		set("iQty", iQty);
		return (M)this;
	}

	/**
	 * 数量
	 */
	@JBoltField(name="iqty" ,columnName="iQty",type="Integer", remark="数量", required=true, maxLength=18, fixed=2, order=9)
	@JSONField(name = "iqty")
	public java.math.BigDecimal getIQty() {
		return getBigDecimal("iQty");
	}

	/**
	 * 批次号
	 */
	public M setCBatchNo(java.lang.String cBatchNo) {
		set("cBatchNo", cBatchNo);
		return (M)this;
	}

	/**
	 * 批次号
	 */
	@JBoltField(name="cbatchno" ,columnName="cBatchNo",type="String", remark="批次号", required=true, maxLength=40, fixed=0, order=10)
	@JSONField(name = "cbatchno")
	public java.lang.String getCBatchNo() {
		return getStr("cBatchNo");
	}

	/**
	 * 检验结果：0. 未生成 1. 待检 2. 不合格 3. 合格
	 */
	public M setIStatus(java.lang.Integer iStatus) {
		set("iStatus", iStatus);
		return (M)this;
	}

	/**
	 * 检验结果：0. 未生成 1. 待检 2. 不合格 3. 合格
	 */
	@JBoltField(name="istatus" ,columnName="iStatus",type="Integer", remark="检验结果：0. 未生成 1. 待检 2. 不合格 3. 合格", required=true, maxLength=10, fixed=0, order=11)
	@JSONField(name = "istatus")
	public java.lang.Integer getIStatus() {
		return getInt("iStatus");
	}

	/**
	 * 是否已完成：0. 未完成 1. 已完成
	 */
	public M setIsCompleted(java.lang.Boolean isCompleted) {
		set("isCompleted", isCompleted);
		return (M)this;
	}

	/**
	 * 是否已完成：0. 未完成 1. 已完成
	 */
	@JBoltField(name="iscompleted" ,columnName="isCompleted",type="Boolean", remark="是否已完成：0. 未完成 1. 已完成", required=true, maxLength=1, fixed=0, order=12)
	@JSONField(name = "iscompleted")
	public java.lang.Boolean getIsCompleted() {
		return getBoolean("isCompleted");
	}

	/**
	 * 检验员Id, 系统用户ID
	 */
	public M setIQcUserId(java.lang.Long iQcUserId) {
		set("iQcUserId", iQcUserId);
		return (M)this;
	}

	/**
	 * 检验员Id, 系统用户ID
	 */
	@JBoltField(name="iqcuserid" ,columnName="iQcUserId",type="Long", remark="检验员Id, 系统用户ID", required=false, maxLength=19, fixed=0, order=13)
	@JSONField(name = "iqcuserid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIQcUserId() {
		return getLong("iQcUserId");
	}

	/**
	 * 测定目的
	 */
	public M setCMeasurePurpose(java.lang.String cMeasurePurpose) {
		set("cMeasurePurpose", cMeasurePurpose);
		return (M)this;
	}

	/**
	 * 测定目的
	 */
	@JBoltField(name="cmeasurepurpose" ,columnName="cMeasurePurpose",type="String", remark="测定目的", required=false, maxLength=200, fixed=0, order=14)
	@JSONField(name = "cmeasurepurpose")
	public java.lang.String getCMeasurePurpose() {
		return getStr("cMeasurePurpose");
	}

	/**
	 * 测定理由
	 */
	public M setCMeasureReason(java.lang.String cMeasureReason) {
		set("cMeasureReason", cMeasureReason);
		return (M)this;
	}

	/**
	 * 测定理由
	 */
	@JBoltField(name="cmeasurereason" ,columnName="cMeasureReason",type="String", remark="测定理由", required=false, maxLength=200, fixed=0, order=15)
	@JSONField(name = "cmeasurereason")
	public java.lang.String getCMeasureReason() {
		return getStr("cMeasureReason");
	}

	/**
	 * 设变号
	 */
	public M setCDcNo(java.lang.String cDcNo) {
		set("cDcNo", cDcNo);
		return (M)this;
	}

	/**
	 * 设变号
	 */
	@JBoltField(name="cdcno" ,columnName="cDcNo",type="String", remark="设变号", required=false, maxLength=200, fixed=0, order=16)
	@JSONField(name = "cdcno")
	public java.lang.String getCDcNo() {
		return getStr("cDcNo");
	}

	/**
	 * 测定单位
	 */
	public M setCMeasureUnit(java.lang.String cMeasureUnit) {
		set("cMeasureUnit", cMeasureUnit);
		return (M)this;
	}

	/**
	 * 测定单位
	 */
	@JBoltField(name="cmeasureunit" ,columnName="cMeasureUnit",type="String", remark="测定单位", required=false, maxLength=5, fixed=0, order=17)
	@JSONField(name = "cmeasureunit")
	public java.lang.String getCMeasureUnit() {
		return getStr("cMeasureUnit");
	}

	/**
	 * 检验日期
	 */
	public M setDQcDate(java.util.Date dQcDate) {
		set("dQcDate", dQcDate);
		return (M)this;
	}

	/**
	 * 检验日期
	 */
	@JBoltField(name="dqcdate" ,columnName="dQcDate",type="Date", remark="检验日期", required=false, maxLength=10, fixed=0, order=18)
	@JSONField(name = "dqcdate")
	public java.util.Date getDQcDate() {
		return getDate("dQcDate");
	}

	/**
	 * 是否CPK数值的输入内容按正负数显示：0. 否 1. 是
	 */
	public M setIsCpkSigned(java.lang.Boolean isCpkSigned) {
		set("isCpkSigned", isCpkSigned);
		return (M)this;
	}

	/**
	 * 是否CPK数值的输入内容按正负数显示：0. 否 1. 是
	 */
	@JBoltField(name="iscpksigned" ,columnName="isCpkSigned",type="Boolean", remark="是否CPK数值的输入内容按正负数显示：0. 否 1. 是", required=true, maxLength=1, fixed=0, order=19)
	@JSONField(name = "iscpksigned")
	public java.lang.Boolean getIsCpkSigned() {
		return getBoolean("isCpkSigned");
	}

	/**
	 * 是否判定合格：0. 否 1. 是
	 */
	public M setIsOk(java.lang.Boolean isOk) {
		set("isOk", isOk);
		return (M)this;
	}

	/**
	 * 是否判定合格：0. 否 1. 是
	 */
	@JBoltField(name="isok" ,columnName="isOk",type="Boolean", remark="是否判定合格：0. 否 1. 是", required=false, maxLength=1, fixed=0, order=20)
	@JSONField(name = "isok")
	public java.lang.Boolean getIsOk() {
		return getBoolean("isOk");
	}

	/**
	 * 备注
	 */
	public M setCMemo(java.lang.String cMemo) {
		set("cMemo", cMemo);
		return (M)this;
	}

	/**
	 * 备注
	 */
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=21)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=22)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=23)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=24)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDCreateTime() {
		return getDate("dCreateTime");
	}

	/**
	 * 更新人ID
	 */
	public M setIUpdateBy(java.lang.Long iUpdateBy) {
		set("iUpdateBy", iUpdateBy);
		return (M)this;
	}

	/**
	 * 更新人ID
	 */
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=25)
	@JSONField(name = "iupdateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUpdateBy() {
		return getLong("iUpdateBy");
	}

	/**
	 * 更新人名称
	 */
	public M setCUpdateName(java.lang.String cUpdateName) {
		set("cUpdateName", cUpdateName);
		return (M)this;
	}

	/**
	 * 更新人名称
	 */
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=50, fixed=0, order=26)
	@JSONField(name = "cupdatename")
	public java.lang.String getCUpdateName() {
		return getStr("cUpdateName");
	}

	/**
	 * 更新时间
	 */
	public M setDUpdateTime(java.util.Date dUpdateTime) {
		set("dUpdateTime", dUpdateTime);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=27)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	public M setIsDeleted(java.lang.Boolean IsDeleted) {
		set("IsDeleted", IsDeleted);
		return (M)this;
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	@JBoltField(name="isdeleted" ,columnName="IsDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=28)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("IsDeleted");
	}

}
