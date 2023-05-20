package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 基础档案-销售类型
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseSaleType<M extends BaseSaleType<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**收发类型ID*/
    public static final String IRDSTYLEID = "iRdStyleId";
    /**销售类型编码*/
    public static final String CSTCODE = "cSTCode";
    /**销售类型名称*/
    public static final String CSTNAME = "cSTName";
    /**出库类别编码*/
    public static final String CRDCODE = "cRdCode";
    /**是否默认值: 1. 是 0. 否*/
    public static final String BDEFAULT = "bDefault";
    /**是否列入MPS/MRP计划*/
    public static final String BSTMPS_MRP = "bSTMPS_MRP";
    /**来源: 1. MES 2. U8*/
    public static final String ISOURCE = "iSource";
    /**来源ID，U8为编码*/
    public static final String ISOURCEID = "iSourceId";
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
	 * 收发类型ID
	 */
	public M setIRdStyleId(java.lang.Long iRdStyleId) {
		set("iRdStyleId", iRdStyleId);
		return (M)this;
	}

	/**
	 * 收发类型ID
	 */
	@JBoltField(name="irdstyleid" ,columnName="iRdStyleId",type="Long", remark="收发类型ID", required=true, maxLength=19, fixed=0, order=5)
	@JSONField(name = "irdstyleid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIRdStyleId() {
		return getLong("iRdStyleId");
	}

	/**
	 * 销售类型编码
	 */
	public M setCSTCode(java.lang.String cSTCode) {
		set("cSTCode", cSTCode);
		return (M)this;
	}

	/**
	 * 销售类型编码
	 */
	@JBoltField(name="cstcode" ,columnName="cSTCode",type="String", remark="销售类型编码", required=true, maxLength=2, fixed=0, order=6)
	@JSONField(name = "cstcode")
	public java.lang.String getCSTCode() {
		return getStr("cSTCode");
	}

	/**
	 * 销售类型名称
	 */
	public M setCSTName(java.lang.String cSTName) {
		set("cSTName", cSTName);
		return (M)this;
	}

	/**
	 * 销售类型名称
	 */
	@JBoltField(name="cstname" ,columnName="cSTName",type="String", remark="销售类型名称", required=true, maxLength=20, fixed=0, order=7)
	@JSONField(name = "cstname")
	public java.lang.String getCSTName() {
		return getStr("cSTName");
	}

	/**
	 * 出库类别编码
	 */
	public M setCRdCode(java.lang.String cRdCode) {
		set("cRdCode", cRdCode);
		return (M)this;
	}

	/**
	 * 出库类别编码
	 */
	@JBoltField(name="crdcode" ,columnName="cRdCode",type="String", remark="出库类别编码", required=false, maxLength=5, fixed=0, order=8)
	@JSONField(name = "crdcode")
	public java.lang.String getCRdCode() {
		return getStr("cRdCode");
	}

	/**
	 * 是否默认值: 1. 是 0. 否
	 */
	public M setBDefault(java.lang.Boolean bDefault) {
		set("bDefault", bDefault);
		return (M)this;
	}

	/**
	 * 是否默认值: 1. 是 0. 否
	 */
	@JBoltField(name="bdefault" ,columnName="bDefault",type="Boolean", remark="是否默认值: 1. 是 0. 否", required=true, maxLength=1, fixed=0, order=9)
	@JSONField(name = "bdefault")
	public java.lang.Boolean getBDefault() {
		return getBoolean("bDefault");
	}

	/**
	 * 是否列入MPS/MRP计划
	 */
	public M setBstmpsMrp(java.lang.Boolean bstmpsMrp) {
		set("bSTMPS_MRP", bstmpsMrp);
		return (M)this;
	}

	/**
	 * 是否列入MPS/MRP计划
	 */
	@JBoltField(name="bstmpsmrp" ,columnName="bSTMPS_MRP",type="Boolean", remark="是否列入MPS/MRP计划", required=true, maxLength=1, fixed=0, order=10)
	@JSONField(name = "bstmpsmrp")
	public java.lang.Boolean getBstmpsMrp() {
		return getBoolean("bSTMPS_MRP");
	}

	/**
	 * 来源: 1. MES 2. U8
	 */
	public M setISource(java.lang.Integer iSource) {
		set("iSource", iSource);
		return (M)this;
	}

	/**
	 * 来源: 1. MES 2. U8
	 */
	@JBoltField(name="isource" ,columnName="iSource",type="Integer", remark="来源: 1. MES 2. U8", required=true, maxLength=10, fixed=0, order=11)
	@JSONField(name = "isource")
	public java.lang.Integer getISource() {
		return getInt("iSource");
	}

	/**
	 * 来源ID，U8为编码
	 */
	public M setISourceId(java.lang.String iSourceId) {
		set("iSourceId", iSourceId);
		return (M)this;
	}

	/**
	 * 来源ID，U8为编码
	 */
	@JBoltField(name="isourceid" ,columnName="iSourceId",type="String", remark="来源ID，U8为编码", required=false, maxLength=32, fixed=0, order=12)
	@JSONField(name = "isourceid")
	public java.lang.String getISourceId() {
		return getStr("iSourceId");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=13)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=14)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=15)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=16)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=50, fixed=0, order=17)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=18)
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
	@JBoltField(name="isdeleted" ,columnName="IsDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=19)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("IsDeleted");
	}

}

