package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 制造工单-工艺工序物料集
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseMoMoroutinginvc<M extends BaseMoMoroutinginvc<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**生产工单-工艺配置ID*/
    public static final String IMOROUTINGCONFIGID = "iMoRoutingConfigId";
    /**料品工艺档案配置ID*/
    public static final String IINVENTORYROUTINGCONFIGID = "iInventoryRoutingConfigId";
    /**料品档案ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**基本用量*/
    public static final String IUSAGEUOM = "iUsageUOM";
    /**备注*/
    public static final String CMEMO = "cMemo";
    /**创建人*/
    public static final String ICREATEBY = "iCreateBy";
    /**创建人名称*/
    public static final String CCREATENAME = "cCreateName";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
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
	 * 生产工单-工艺配置ID
	 */
	public M setIMoRoutingConfigId(java.lang.Long iMoRoutingConfigId) {
		set("iMoRoutingConfigId", iMoRoutingConfigId);
		return (M)this;
	}

	/**
	 * 生产工单-工艺配置ID
	 */
	@JBoltField(name="imoroutingconfigid" ,columnName="iMoRoutingConfigId",type="Long", remark="生产工单-工艺配置ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "imoroutingconfigid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIMoRoutingConfigId() {
		return getLong("iMoRoutingConfigId");
	}

	/**
	 * 料品工艺档案配置ID
	 */
	public M setIInventoryRoutingConfigId(java.lang.Long iInventoryRoutingConfigId) {
		set("iInventoryRoutingConfigId", iInventoryRoutingConfigId);
		return (M)this;
	}

	/**
	 * 料品工艺档案配置ID
	 */
	@JBoltField(name="iinventoryroutingconfigid" ,columnName="iInventoryRoutingConfigId",type="Long", remark="料品工艺档案配置ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iinventoryroutingconfigid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryRoutingConfigId() {
		return getLong("iInventoryRoutingConfigId");
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
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="料品档案ID", required=true, maxLength=19, fixed=0, order=4)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 基本用量
	 */
	public M setIUsageUOM(java.math.BigDecimal iUsageUOM) {
		set("iUsageUOM", iUsageUOM);
		return (M)this;
	}

	/**
	 * 基本用量
	 */
	@JBoltField(name="iusageuom" ,columnName="iUsageUOM",type="BigDecimal", remark="基本用量", required=true, maxLength=24, fixed=6, order=5)
	@JSONField(name = "iusageuom")
	public java.math.BigDecimal getIUsageUOM() {
		return getBigDecimal("iUsageUOM");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=6)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=7)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=8)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=9)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDCreateTime() {
		return getDate("dCreateTime");
	}

}

