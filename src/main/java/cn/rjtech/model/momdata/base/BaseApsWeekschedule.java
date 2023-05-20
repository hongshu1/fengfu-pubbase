package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 生产计划-月周生产计划排产
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseApsWeekschedule<M extends BaseApsWeekschedule<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**排产层级*/
    public static final String ILEVEL = "iLevel";
    /**排产开始时间*/
    public static final String DSCHEDULEBEGINTIME = "dScheduleBeginTime";
    /**排产截止时间*/
    public static final String DSCHEDULEENDTIME = "dScheduleEndTime";
    /**锁定截止时间*/
    public static final String DLOCKENDTIME = "dLockEndTime";
    /**是否锁定：0. 否 1. 是*/
    public static final String ISLOCKED = "isLocked";
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
	 * 排产层级
	 */
	public M setILevel(java.lang.Integer iLevel) {
		set("iLevel", iLevel);
		return (M)this;
	}

	/**
	 * 排产层级
	 */
	@JBoltField(name="ilevel" ,columnName="iLevel",type="Integer", remark="排产层级", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "ilevel")
	public java.lang.Integer getILevel() {
		return getInt("iLevel");
	}

	/**
	 * 排产开始时间
	 */
	public M setDScheduleBeginTime(java.util.Date dScheduleBeginTime) {
		set("dScheduleBeginTime", dScheduleBeginTime);
		return (M)this;
	}

	/**
	 * 排产开始时间
	 */
	@JBoltField(name="dschedulebegintime" ,columnName="dScheduleBeginTime",type="Date", remark="排产开始时间", required=true, maxLength=23, fixed=3, order=6)
	@JSONField(name = "dschedulebegintime")
	public java.util.Date getDScheduleBeginTime() {
		return getDate("dScheduleBeginTime");
	}

	/**
	 * 排产截止时间
	 */
	public M setDScheduleEndTime(java.util.Date dScheduleEndTime) {
		set("dScheduleEndTime", dScheduleEndTime);
		return (M)this;
	}

	/**
	 * 排产截止时间
	 */
	@JBoltField(name="dscheduleendtime" ,columnName="dScheduleEndTime",type="Date", remark="排产截止时间", required=true, maxLength=23, fixed=3, order=7)
	@JSONField(name = "dscheduleendtime")
	public java.util.Date getDScheduleEndTime() {
		return getDate("dScheduleEndTime");
	}

	/**
	 * 锁定截止时间
	 */
	public M setDLockEndTime(java.util.Date dLockEndTime) {
		set("dLockEndTime", dLockEndTime);
		return (M)this;
	}

	/**
	 * 锁定截止时间
	 */
	@JBoltField(name="dlockendtime" ,columnName="dLockEndTime",type="Date", remark="锁定截止时间", required=true, maxLength=23, fixed=3, order=8)
	@JSONField(name = "dlockendtime")
	public java.util.Date getDLockEndTime() {
		return getDate("dLockEndTime");
	}

	/**
	 * 是否锁定：0. 否 1. 是
	 */
	public M setIsLocked(java.lang.Boolean isLocked) {
		set("isLocked", isLocked);
		return (M)this;
	}

	/**
	 * 是否锁定：0. 否 1. 是
	 */
	@JBoltField(name="islocked" ,columnName="isLocked",type="Boolean", remark="是否锁定：0. 否 1. 是", required=true, maxLength=1, fixed=0, order=9)
	@JSONField(name = "islocked")
	public java.lang.Boolean getIsLocked() {
		return getBoolean("isLocked");
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
	 * 更新人ID
	 */
	public M setIUpdateBy(java.lang.Long iUpdateBy) {
		set("iUpdateBy", iUpdateBy);
		return (M)this;
	}

	/**
	 * 更新人ID
	 */
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=13)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=50, fixed=0, order=14)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=15)
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
	@JBoltField(name="isdeleted" ,columnName="IsDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=16)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("IsDeleted");
	}

}

