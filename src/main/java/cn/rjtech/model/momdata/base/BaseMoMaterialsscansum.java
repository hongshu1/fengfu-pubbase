package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 制造管理-制造工单齐料汇总
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseMoMaterialsscansum<M extends BaseMoMaterialsscansum<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**生产工单ID*/
    public static final String IMODOCID = "iMoDocId";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**计划数量*/
    public static final String IPLANQTY = "iPlanQty";
    /**已扫数量*/
    public static final String ISCANNEDQTY = "iScannedQty";
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
	 * 存货ID
	 */
	public M setIInventoryId(java.lang.Long iInventoryId) {
		set("iInventoryId", iInventoryId);
		return (M)this;
	}

	/**
	 * 存货ID
	 */
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="存货ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 计划数量
	 */
	public M setIPlanQty(java.math.BigDecimal iPlanQty) {
		set("iPlanQty", iPlanQty);
		return (M)this;
	}

	/**
	 * 计划数量
	 */
	@JBoltField(name="iplanqty" ,columnName="iPlanQty",type="BigDecimal", remark="计划数量", required=true, maxLength=18, fixed=2, order=4)
	@JSONField(name = "iplanqty")
	public java.math.BigDecimal getIPlanQty() {
		return getBigDecimal("iPlanQty");
	}

	/**
	 * 已扫数量
	 */
	public M setIScannedQty(java.math.BigDecimal iScannedQty) {
		set("iScannedQty", iScannedQty);
		return (M)this;
	}

	/**
	 * 已扫数量
	 */
	@JBoltField(name="iscannedqty" ,columnName="iScannedQty",type="BigDecimal", remark="已扫数量", required=true, maxLength=18, fixed=2, order=5)
	@JSONField(name = "iscannedqty")
	public java.math.BigDecimal getIScannedQty() {
		return getBigDecimal("iScannedQty");
	}

}
