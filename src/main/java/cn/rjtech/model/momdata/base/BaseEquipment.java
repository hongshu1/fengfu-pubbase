package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 设备管理-设备档案
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseEquipment<M extends BaseEquipment<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**设备编码*/
    public static final String CEQUIPMENTCODE = "cEquipmentCode";
    /**设备名称*/
    public static final String CEQUIPMENTNAME = "cEquipmentName";
    /**产线ID*/
    public static final String IWORKREGIONMID = "iWorkRegionmId";
    /**是否导电咀更换*/
    public static final String ISNOZZLESWITCHENABLED = "isNozzleSwitchEnabled";
    /**状态：1. 生产 2. 空闲 3. 故障 4. 调机 5. 关机 6. 未连接*/
    public static final String ISTATUS = "iStatus";
    /**是否启用：0. 否 1. 是*/
    public static final String ISENABLED = "isEnabled";
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
    public static final String ISDELETED = "isDeleted";
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
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Integer", remark="主键ID", required=true, maxLength=10, fixed=0, order=1)
	@JSONField(name = "iautoid")
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
	 * 设备编码
	 */
	public M setCEquipmentCode(java.lang.String cEquipmentCode) {
		set("cEquipmentCode", cEquipmentCode);
		return (M)this;
	}

	/**
	 * 设备编码
	 */
	@JBoltField(name="cequipmentcode" ,columnName="cEquipmentCode",type="String", remark="设备编码", required=true, maxLength=40, fixed=0, order=5)
	@JSONField(name = "cequipmentcode")
	public java.lang.String getCEquipmentCode() {
		return getStr("cEquipmentCode");
	}

	/**
	 * 设备名称
	 */
	public M setCEquipmentName(java.lang.String cEquipmentName) {
		set("cEquipmentName", cEquipmentName);
		return (M)this;
	}

	/**
	 * 设备名称
	 */
	@JBoltField(name="cequipmentname" ,columnName="cEquipmentName",type="String", remark="设备名称", required=true, maxLength=50, fixed=0, order=6)
	@JSONField(name = "cequipmentname")
	public java.lang.String getCEquipmentName() {
		return getStr("cEquipmentName");
	}

	/**
	 * 产线ID
	 */
	public M setIWorkRegionmId(java.lang.Long iWorkRegionmId) {
		set("iWorkRegionmId", iWorkRegionmId);
		return (M)this;
	}

	/**
	 * 产线ID
	 */
	@JBoltField(name="iworkregionmid" ,columnName="iWorkRegionmId",type="Long", remark="产线ID", required=true, maxLength=19, fixed=0, order=7)
	@JSONField(name = "iworkregionmid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIWorkRegionmId() {
		return getLong("iWorkRegionmId");
	}

	/**
	 * 是否导电咀更换
	 */
	public M setIsNozzleSwitchEnabled(java.lang.Boolean isNozzleSwitchEnabled) {
		set("isNozzleSwitchEnabled", isNozzleSwitchEnabled);
		return (M)this;
	}

	/**
	 * 是否导电咀更换
	 */
	@JBoltField(name="isnozzleswitchenabled" ,columnName="isNozzleSwitchEnabled",type="Boolean", remark="是否导电咀更换", required=true, maxLength=1, fixed=0, order=8)
	@JSONField(name = "isnozzleswitchenabled")
	public java.lang.Boolean getIsNozzleSwitchEnabled() {
		return getBoolean("isNozzleSwitchEnabled");
	}

	/**
	 * 状态：1. 生产 2. 空闲 3. 故障 4. 调机 5. 关机 6. 未连接
	 */
	public M setIStatus(java.lang.Integer iStatus) {
		set("iStatus", iStatus);
		return (M)this;
	}

	/**
	 * 状态：1. 生产 2. 空闲 3. 故障 4. 调机 5. 关机 6. 未连接
	 */
	@JBoltField(name="istatus" ,columnName="iStatus",type="Integer", remark="状态：1. 生产 2. 空闲 3. 故障 4. 调机 5. 关机 6. 未连接", required=true, maxLength=10, fixed=0, order=9)
	@JSONField(name = "istatus")
	public java.lang.Integer getIStatus() {
		return getInt("iStatus");
	}

	/**
	 * 是否启用：0. 否 1. 是
	 */
	public M setIsEnabled(java.lang.Boolean isEnabled) {
		set("isEnabled", isEnabled);
		return (M)this;
	}

	/**
	 * 是否启用：0. 否 1. 是
	 */
	@JBoltField(name="isenabled" ,columnName="isEnabled",type="Boolean", remark="是否启用：0. 否 1. 是", required=true, maxLength=1, fixed=0, order=10)
	@JSONField(name = "isenabled")
	public java.lang.Boolean getIsEnabled() {
		return getBoolean("isEnabled");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=11)
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=12)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=13)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=14)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=15)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=50, fixed=0, order=16)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=17)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
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
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=18)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

}

