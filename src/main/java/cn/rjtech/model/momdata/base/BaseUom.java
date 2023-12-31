package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 物料建模-计量单位
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseUom<M extends BaseUom<M>> extends JBoltBaseModel<M>{
    /**主键*/
    public static final String IAUTOID = "iAutoId";
    /**计量单位组Id*/
    public static final String IUOMCLASSID = "iUomClassId";
    /**计量单位编码*/
    public static final String CUOMCODE = "cUomCode";
    /**计量单位名称*/
    public static final String CUOMNAME = "cUomName";
    /**是否主计量*/
    public static final String ISBASE = "isBase";
    /**换算率*/
    public static final String IRATIOTOBASE = "iRatioToBase";
    /**换算说明*/
    public static final String CMEMO = "cMemo";
    /**来源: 1.MES 2. U8*/
    public static final String ISOURCE = "iSource";
    /**来源标识ID，U8为编码*/
    public static final String ISOURCEID = "iSourceId";
    /**创建人*/
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
	 * 计量单位组Id
	 */
	public M setIUomClassId(java.lang.Long iUomClassId) {
		set("iUomClassId", iUomClassId);
		return (M)this;
	}

	/**
	 * 计量单位组Id
	 */
	@JBoltField(name="iuomclassid" ,columnName="iUomClassId",type="Long", remark="计量单位组Id", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iuomclassid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUomClassId() {
		return getLong("iUomClassId");
	}

	/**
	 * 计量单位编码
	 */
	public M setCUomCode(java.lang.String cUomCode) {
		set("cUomCode", cUomCode);
		return (M)this;
	}

	/**
	 * 计量单位编码
	 */
	@JBoltField(name="cuomcode" ,columnName="cUomCode",type="String", remark="计量单位编码", required=true, maxLength=40, fixed=0, order=3)
	@JSONField(name = "cuomcode")
	public java.lang.String getCUomCode() {
		return getStr("cUomCode");
	}

	/**
	 * 计量单位名称
	 */
	public M setCUomName(java.lang.String cUomName) {
		set("cUomName", cUomName);
		return (M)this;
	}

	/**
	 * 计量单位名称
	 */
	@JBoltField(name="cuomname" ,columnName="cUomName",type="String", remark="计量单位名称", required=true, maxLength=50, fixed=0, order=4)
	@JSONField(name = "cuomname")
	public java.lang.String getCUomName() {
		return getStr("cUomName");
	}

	/**
	 * 是否主计量
	 */
	public M setIsBase(java.lang.Boolean isBase) {
		set("isBase", isBase);
		return (M)this;
	}

	/**
	 * 是否主计量
	 */
	@JBoltField(name="isbase" ,columnName="isBase",type="Boolean", remark="是否主计量", required=true, maxLength=1, fixed=0, order=5)
	@JSONField(name = "isbase")
	public java.lang.Boolean getIsBase() {
		return getBoolean("isBase");
	}

	/**
	 * 换算率
	 */
	public M setIRatioToBase(java.math.BigDecimal iRatioToBase) {
		set("iRatioToBase", iRatioToBase);
		return (M)this;
	}

	/**
	 * 换算率
	 */
	@JBoltField(name="iratiotobase" ,columnName="iRatioToBase",type="BigDecimal", remark="换算率", required=true, maxLength=24, fixed=6, order=6)
	@JSONField(name = "iratiotobase")
	public java.math.BigDecimal getIRatioToBase() {
		return getBigDecimal("iRatioToBase");
	}

	/**
	 * 换算说明
	 */
	public M setCMemo(java.lang.String cMemo) {
		set("cMemo", cMemo);
		return (M)this;
	}

	/**
	 * 换算说明
	 */
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="换算说明", required=false, maxLength=200, fixed=0, order=7)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
	}

	/**
	 * 来源: 1.MES 2. U8
	 */
	public M setISource(java.lang.Integer iSource) {
		set("iSource", iSource);
		return (M)this;
	}

	/**
	 * 来源: 1.MES 2. U8
	 */
	@JBoltField(name="isource" ,columnName="iSource",type="Integer", remark="来源: 1.MES 2. U8", required=true, maxLength=10, fixed=0, order=8)
	@JSONField(name = "isource")
	public java.lang.Integer getISource() {
		return getInt("iSource");
	}

	/**
	 * 来源标识ID，U8为编码
	 */
	public M setISourceId(java.lang.String iSourceId) {
		set("iSourceId", iSourceId);
		return (M)this;
	}

	/**
	 * 来源标识ID，U8为编码
	 */
	@JBoltField(name="isourceid" ,columnName="iSourceId",type="String", remark="来源标识ID，U8为编码", required=false, maxLength=32, fixed=0, order=9)
	@JSONField(name = "isourceid")
	public java.lang.String getISourceId() {
		return getStr("iSourceId");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=10)
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

