package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.util.Date;

/**
 * 仓库建模-仓库档案
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseWarehouse<M extends BaseWarehouse<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**仓库编码*/
    public static final String CWHCODE = "cWhCode";
    /**仓库名称*/
    public static final String CWHNAME = "cWhName";
    /**所属部门*/
    public static final String CDEPCODE = "cDepCode";
    /**仓库地址*/
    public static final String CWHADDRESS = "cWhAddress";
    /**负责人*/
    public static final String CWHPERSON = "cWhPerson";
    /**备注*/
    public static final String CWHMEMO = "cWhMemo";
    /**删除状态: 0. 未删除 1. 已删除*/
    public static final String ISDELETED = "isDeleted";
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
	/**来源编码*/
    public static final String ISOURCE = "iSource";
	/**来源ID*/
	public static final String ISOURCEID = "iSourceId";
	/**启用空间掌控：0. 否 1. 是*/
	public static final String ISSPACECONTROLENABLED = "isSpaceControlEnabled";
	/**启用库存预警：0. 否 1. 是*/
	public static final String ISSTOCKWARNENABLED = "isStockWarnEnabled";
	/**启用库区：0. 否 1. 是*/
	public static final String ISRESERVOIRAREA = "isReservoirArea";
	/**是否启用：0. 否 1. 是*/
	public static final String ISENABLED = "isEnabled";
	/**最大存储数量*/
	public static final String IMAXSTOCK = "iMaxStock";
	/**最大存储空间*/
	public static final String IMAXSPACE = "iMaxSpace";
	/**
	 * 主键ID
	 */
	public M setIAutoId(Long iAutoId) {
		set("iAutoId", iAutoId);
		return (M)this;
	}

	/**
	 * 主键ID
	 */
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Long", remark="主键ID", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(name = "iautoid", serializeUsing = ToStringSerializer.class)
	public Long getIAutoId() {
		return getLong("iAutoId");
	}

	/**
	 * 组织ID
	 */
	public M setIOrgId(Long iOrgId) {
		set("iOrgId", iOrgId);
		return (M)this;
	}

	/**
	 * 组织ID
	 */
	@JBoltField(name="iorgid" ,columnName="iOrgId",type="Long", remark="组织ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iorgid", serializeUsing = ToStringSerializer.class)
	public Long getIOrgId() {
		return getLong("iOrgId");
	}

	/**
	 * 组织编码
	 */
	public M setCOrgCode(String cOrgCode) {
		set("cOrgCode", cOrgCode);
		return (M)this;
	}

	/**
	 * 组织编码
	 */
	@JBoltField(name="corgcode" ,columnName="cOrgCode",type="String", remark="组织编码", required=true, maxLength=40, fixed=0, order=3)
	@JSONField(name = "corgcode")
	public String getCOrgCode() {
		return getStr("cOrgCode");
	}

	/**
	 * 组织名称
	 */
	public M setCOrgName(String cOrgName) {
		set("cOrgName", cOrgName);
		return (M)this;
	}

	/**
	 * 组织名称
	 */
	@JBoltField(name="corgname" ,columnName="cOrgName",type="String", remark="组织名称", required=true, maxLength=50, fixed=0, order=4)
	@JSONField(name = "corgname")
	public String getCOrgName() {
		return getStr("cOrgName");
	}

	/**
	 * 仓库编码
	 */
	public M setCWhCode(String cWhCode) {
		set("cWhCode", cWhCode);
		return (M)this;
	}

	/**
	 * 仓库编码
	 */
	@JBoltField(name="cwhcode" ,columnName="cWhCode",type="String", remark="仓库编码", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "cwhcode")
	public String getCWhCode() {
		return getStr("cWhCode");
	}

	/**
	 * 仓库名称
	 */
	public M setCWhName(String cWhName) {
		set("cWhName", cWhName);
		return (M)this;
	}

	/**
	 * 仓库名称
	 */
	@JBoltField(name="cwhname" ,columnName="cWhName",type="String", remark="仓库名称", required=false, maxLength=20, fixed=0, order=6)
	@JSONField(name = "cwhname")
	public String getCWhName() {
		return getStr("cWhName");
	}

	/**
	 * 所属部门
	 */
	public M setCDepCode(String cDepCode) {
		set("cDepCode", cDepCode);
		return (M)this;
	}

	/**
	 * 所属部门
	 */
	@JBoltField(name="cdepcode" ,columnName="cDepCode",type="String", remark="所属部门", required=false, maxLength=12, fixed=0, order=7)
	@JSONField(name = "cdepcode")
	public String getCDepCode() {
		return getStr("cDepCode");
	}

	/**
	 * 仓库地址
	 */
	public M setCWhAddress(String cWhAddress) {
		set("cWhAddress", cWhAddress);
		return (M)this;
	}

	/**
	 * 仓库地址
	 */
	@JBoltField(name="cwhaddress" ,columnName="cWhAddress",type="String", remark="仓库地址", required=false, maxLength=255, fixed=0, order=8)
	@JSONField(name = "cwhaddress")
	public String getCWhAddress() {
		return getStr("cWhAddress");
	}

	/**
	 * 负责人
	 */
	public M setCWhPerson(String cWhPerson) {
		set("cWhPerson", cWhPerson);
		return (M)this;
	}

	/**
	 * 负责人
	 */
	@JBoltField(name="cwhperson" ,columnName="cWhPerson",type="String", remark="负责人", required=false, maxLength=20, fixed=0, order=10)
	@JSONField(name = "cwhperson")
	public String getCWhPerson() {
		return getStr("cWhPerson");
	}


	/**
	 * 备注
	 */
	public M setCWhMemo(String cWhMemo) {
		set("cWhMemo", cWhMemo);
		return (M)this;
	}

	/**
	 * 备注
	 */
	@JBoltField(name="cwhmemo" ,columnName="cWhMemo",type="String", remark="备注", required=false, maxLength=20, fixed=0, order=13)
	@JSONField(name = "cwhmemo")
	public String getCWhMemo() {
		return getStr("cWhMemo");
	}

	/**
	 * 删除状态: 0. 未删除 1. 已删除
	 */
	public M setIsDeleted(Boolean isDeleted) {
		set("isDeleted", isDeleted);
		return (M)this;
	}

	/**
	 * 删除状态: 0. 未删除 1. 已删除
	 */
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态: 0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=42)
	@JSONField(name = "isdeleted")
	public Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 创建人
	 */
	public M setIcreateby(Long iCreateBy) {
		set("iCreateBy", iCreateBy);
		return (M)this;
	}

	/**
	 * 创建人
	 */
	@JBoltField(name="iCreateBy" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=6)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIcreateby() {
		return getLong("iCreateBy");
	}

	/**
	 * 创建人名称
	 */
	public M setCcreatename(String cCreateName) {
		set("cCreateName", cCreateName);
		return (M)this;
	}
	/**
	 * 创建人名称
	 */
	@JBoltField(name="cCreateName" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=255, fixed=0, order=7)
	public String getCcreatename() {
		return getStr("cCreateName");
	}

	/**
	 * 创建时间
	 */
	public M setDCreateTime(Date dCreateTime) {
		set("dCreateTime", dCreateTime);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=43)
	@JSONField(name = "dcreatetime")
	public Date getDCreateTime() {
		return getDate("dCreateTime");
	}

	/**
	 * 更新人
	 */
	public M setIupdateby(Long iUpdateBy) {
		set("iUpdateBy", iUpdateBy);
		return (M)this;
	}

	/**
	 * 更新人
	 */
	@JBoltField(name="iUpdateBy" ,columnName="iUpdateBy",type="Long", remark="更新人", required=false, maxLength=19, fixed=0, order=9)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIupdateby() {
		return getLong("iUpdateBy");
	}

	/**
	 * 更新人名称
	 */
	public M setCupdatename(String cUpdateName) {
		set("cUpdateName", cUpdateName);
		return (M)this;
	}

	/**
	 * 更新人名称
	 */
	@JBoltField(name="cUpdateName" ,columnName="cUpdateName",type="String", remark="更新人名称", required=false, maxLength=255, fixed=0, order=10)
	public String getCupdatename() {
		return getStr("cUpdateName");
	}

	/**
	 * 更新时间
	 */
	public M setDupdatetime(Date dUpdateTime) {
		set("dUpdateTime", dUpdateTime);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="dUpdateTime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=11)
	public Date getDupdatetime() {
		return getDate("dUpdateTime");
	}


	/**
	 * 来源1.MES 2.U8;
	 */
	public M setISource(Integer isource) {
		set("iSource", isource);
		return (M)this;
	}

	/**
	 * 来源1.MES 2.U8;
	 */
	@JBoltField(name="isource" ,columnName="iSource",type="Integer", remark="来源1.MES 2.U8", required=false, maxLength=10, fixed=0, order=15)
	public Integer getISource() {
		return getInt("iSource");
	}

	/**
	 * 来源ID
	 */
	public M setISourceId(String iSourceId) {
		set("iSourceId", iSourceId);
		return (M)this;
	}

	/**
	 * 来源ID
	 */
	@JBoltField(name="iSourceId" ,columnName="iSourceId",type="String", remark="来源ID",  required=false, maxLength=100, fixed=0,  order=45)
	public String getISourceId() {
		return getStr("iSourceId");
	}

	/**
	 * 启用空间掌控：0. 否 1. 是
	 */
	public M setIsSpaceControlEnabled(Boolean isSpaceControlEnabled) {
		set("isSpaceControlEnabled", isSpaceControlEnabled);
		return (M)this;
	}

	/**
	 * 启用空间掌控：0. 否 1. 是
	 */
	@JBoltField(name="isSpaceControlEnabled" ,columnName="isSpaceControlEnabled",type="Boolean", remark="启用空间掌控", required=false, maxLength=1, fixed=0, order=8)
	public Boolean getIsSpaceControlEnabled() {
		return getBoolean("isSpaceControlEnabled");
	}

	/**
	 * 启用库存预警：0. 否 1. 是
	 */
	public M setIsStockWarnEnabled(java.lang.Boolean isStockWarnEnabled) {
		set("isStockWarnEnabled", isStockWarnEnabled);
		return (M)this;
	}

	/**
	 * 启用库存预警：0. 否 1. 是
	 */
	@JBoltField(name="isStockWarnEnabled" ,columnName="isStockWarnEnabled",type="Boolean", remark="启用库存预警", required=false, maxLength=1, fixed=0, order=8)
	public java.lang.Boolean getIsStockWarnEnabled() {
		return getBoolean("isStockWarnEnabled");
	}

	/**
	 * 最大存储数量
	 */
	public M setIMaxStock(java.math.BigDecimal iMaxStock) {
		set("iMaxStock", iMaxStock);
		return (M)this;
	}

	/**
	 * 最大存储数量
	 */
	@JBoltField(name="iMaxStock" ,columnName="iMaxStock",type="BigDecimal", remark="最大存储数", required=false, maxLength=24, fixed=6, order=10)
	public java.math.BigDecimal getIMaxStock() {
		return getBigDecimal("iMaxStock");
	}

	/**
	 * 最大存储空间
	 */
	public M setImaxspace(java.math.BigDecimal imaxspace) {
		set("iMaxSpace", imaxspace);
		return (M)this;
	}

	/**
	 * 最大存储空间
	 */
	@JBoltField(name="imaxspace" ,columnName="iMaxSpace",type="BigDecimal", remark="最大存储空间", required=false, maxLength=24, fixed=6, order=11)
	public java.math.BigDecimal getImaxspace() {
		return getBigDecimal("iMaxSpace");
	}


	/**
	 * 启用库区：0. 否 1. 是
	 */
	public M setIsReservoirAread(java.lang.Boolean isReservoirArea) {
		set("isReservoirArea", isReservoirArea);
		return (M)this;
	}

	/**
	 * 启用库区：0. 否 1. 是
	 */
	@JBoltField(name="isReservoirArea" ,columnName="isReservoirArea",type="Boolean", remark="启用库区", required=false, maxLength=1, fixed=0, order=8)
	public java.lang.Boolean getIsReservoirArea() {
		return getBoolean("isReservoirArea");
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
	@JBoltField(name="isEnabled" ,columnName="isEnabled",type="Boolean", remark="是否启用", required=false, maxLength=1, fixed=0, order=8)
	public java.lang.Boolean getIsEnabled() {
		return getBoolean("isEnabled");
	}
}

