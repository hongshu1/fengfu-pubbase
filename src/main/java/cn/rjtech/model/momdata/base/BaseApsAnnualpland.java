package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 生产计划-年度计划排产明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseApsAnnualpland<M extends BaseApsAnnualpland<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**年度计划订单ID*/
    public static final String IANNUALPLANMID = "iAnnualPlanMid";
    /**机型ID*/
    public static final String IEQUIPMENTMODELID = "iEquipmentModelId";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**年度1-计划使用（数量）*/
    public static final String IYEAR11QTY = "iYear11Qty";
    /**年度1-计划数量*/
    public static final String IYEAR12QTY = "iYear12Qty";
    /**年度1-计划在库（数量）*/
    public static final String IYEAR13QTY = "iYear13Qty";
    /**年度2-计划使用（数量）*/
    public static final String IYEAR21QTY = "iYear21Qty";
    /**年度2-计划数量*/
    public static final String IYEAR22QTY = "iYear22Qty";
    /**年度2-计划在库（数量）*/
    public static final String IYEAR23QTY = "iYear23Qty";
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
	 * 年度计划订单ID
	 */
	public M setIAnnualPlanMid(java.lang.Long iAnnualPlanMid) {
		set("iAnnualPlanMid", iAnnualPlanMid);
		return (M)this;
	}

	/**
	 * 年度计划订单ID
	 */
	@JBoltField(name="iannualplanmid" ,columnName="iAnnualPlanMid",type="Long", remark="年度计划订单ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iannualplanmid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIAnnualPlanMid() {
		return getLong("iAnnualPlanMid");
	}

	/**
	 * 机型ID
	 */
	public M setIEquipmentModelId(java.lang.Long iEquipmentModelId) {
		set("iEquipmentModelId", iEquipmentModelId);
		return (M)this;
	}

	/**
	 * 机型ID
	 */
	@JBoltField(name="iequipmentmodelid" ,columnName="iEquipmentModelId",type="Long", remark="机型ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iequipmentmodelid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIEquipmentModelId() {
		return getLong("iEquipmentModelId");
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
	 * 年度1-计划使用（数量）
	 */
	public M setIYear11Qty(java.lang.Integer iYear11Qty) {
		set("iYear11Qty", iYear11Qty);
		return (M)this;
	}

	/**
	 * 年度1-计划使用（数量）
	 */
	@JBoltField(name="iyear11qty" ,columnName="iYear11Qty",type="Integer", remark="年度1-计划使用（数量）", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "iyear11qty")
	public java.lang.Integer getIYear11Qty() {
		return getInt("iYear11Qty");
	}

	/**
	 * 年度1-计划数量
	 */
	public M setIYear12Qty(java.lang.Integer iYear12Qty) {
		set("iYear12Qty", iYear12Qty);
		return (M)this;
	}

	/**
	 * 年度1-计划数量
	 */
	@JBoltField(name="iyear12qty" ,columnName="iYear12Qty",type="Integer", remark="年度1-计划数量", required=true, maxLength=10, fixed=0, order=6)
	@JSONField(name = "iyear12qty")
	public java.lang.Integer getIYear12Qty() {
		return getInt("iYear12Qty");
	}

	/**
	 * 年度1-计划在库（数量）
	 */
	public M setIYear13Qty(java.lang.Integer iYear13Qty) {
		set("iYear13Qty", iYear13Qty);
		return (M)this;
	}

	/**
	 * 年度1-计划在库（数量）
	 */
	@JBoltField(name="iyear13qty" ,columnName="iYear13Qty",type="Integer", remark="年度1-计划在库（数量）", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "iyear13qty")
	public java.lang.Integer getIYear13Qty() {
		return getInt("iYear13Qty");
	}

	/**
	 * 年度2-计划使用（数量）
	 */
	public M setIYear21Qty(java.lang.Integer iYear21Qty) {
		set("iYear21Qty", iYear21Qty);
		return (M)this;
	}

	/**
	 * 年度2-计划使用（数量）
	 */
	@JBoltField(name="iyear21qty" ,columnName="iYear21Qty",type="Integer", remark="年度2-计划使用（数量）", required=false, maxLength=10, fixed=0, order=8)
	@JSONField(name = "iyear21qty")
	public java.lang.Integer getIYear21Qty() {
		return getInt("iYear21Qty");
	}

	/**
	 * 年度2-计划数量
	 */
	public M setIYear22Qty(java.lang.Integer iYear22Qty) {
		set("iYear22Qty", iYear22Qty);
		return (M)this;
	}

	/**
	 * 年度2-计划数量
	 */
	@JBoltField(name="iyear22qty" ,columnName="iYear22Qty",type="Integer", remark="年度2-计划数量", required=false, maxLength=10, fixed=0, order=9)
	@JSONField(name = "iyear22qty")
	public java.lang.Integer getIYear22Qty() {
		return getInt("iYear22Qty");
	}

	/**
	 * 年度2-计划在库（数量）
	 */
	public M setIYear23Qty(java.lang.Integer iYear23Qty) {
		set("iYear23Qty", iYear23Qty);
		return (M)this;
	}

	/**
	 * 年度2-计划在库（数量）
	 */
	@JBoltField(name="iyear23qty" ,columnName="iYear23Qty",type="Integer", remark="年度2-计划在库（数量）", required=false, maxLength=10, fixed=0, order=10)
	@JSONField(name = "iyear23qty")
	public java.lang.Integer getIYear23Qty() {
		return getInt("iYear23Qty");
	}

}

