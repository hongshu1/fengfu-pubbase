package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 制造工单-生产工艺路线
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseMoMorouting<M extends BaseMoMorouting<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**生产工单ID*/
    public static final String IMODOCID = "iMoDocId";
    /**存货工艺路线ID*/
    public static final String IINVENTORYROUTINGID = "iInventoryRoutingId";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**工艺路线名称*/
    public static final String CROUTINGNAME = "cRoutingName";
    /**工艺路线版本*/
    public static final String CVERSION = "cVersion";
    /**成品率*/
    public static final String IFINISHEDRATE = "iFinishedRate";
    /**工艺要求*/
    public static final String CREQUIREMENT = "cRequirement";
    /**备注*/
    public static final String CMEMO = "cMemo";
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
	 * 生产工单ID
	 */
	public M setIMoDocId(java.lang.Long iMoDocId) {
		set("iMoDocId", iMoDocId);
		return (M)this;
	}

	/**
	 * 生产工单ID
	 */
	@JBoltField(name="imodocid" ,columnName="iMoDocId",type="Long", remark="生产工单ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "imodocid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIMoDocId() {
		return getLong("iMoDocId");
	}

	/**
	 * 存货工艺路线ID
	 */
	public M setIInventoryRoutingId(java.lang.Long iInventoryRoutingId) {
		set("iInventoryRoutingId", iInventoryRoutingId);
		return (M)this;
	}

	/**
	 * 存货工艺路线ID
	 */
	@JBoltField(name="iinventoryroutingid" ,columnName="iInventoryRoutingId",type="Long", remark="存货工艺路线ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iinventoryroutingid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryRoutingId() {
		return getLong("iInventoryRoutingId");
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
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="存货ID", required=true, maxLength=19, fixed=0, order=4)
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
	@JBoltField(name="croutingname" ,columnName="cRoutingName",type="String", remark="工艺路线名称", required=true, maxLength=50, fixed=0, order=5)
	@JSONField(name = "croutingname")
	public java.lang.String getCRoutingName() {
		return getStr("cRoutingName");
	}

	/**
	 * 工艺路线版本
	 */
	public M setCVersion(java.lang.String cVersion) {
		set("cVersion", cVersion);
		return (M)this;
	}

	/**
	 * 工艺路线版本
	 */
	@JBoltField(name="cversion" ,columnName="cVersion",type="String", remark="工艺路线版本", required=true, maxLength=10, fixed=0, order=6)
	@JSONField(name = "cversion")
	public java.lang.String getCVersion() {
		return getStr("cVersion");
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
	 * 备注
	 */
	public M setCMemo(java.lang.String cMemo) {
		set("cMemo", cMemo);
		return (M)this;
	}

	/**
	 * 备注
	 */
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=9)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
	}

}

