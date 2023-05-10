package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 制造工单-工艺设备集
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseMoMoroutingequipment<M extends BaseMoMoroutingequipment<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**工单工艺配置ID*/
    public static final String IMOROUTINGCONFIGID = "iMoRoutingConfigId";
    /**设备ID*/
    public static final String IEQUIPMENTID = "iEquipmentId";
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
	 * 工单工艺配置ID
	 */
	public M setIMoRoutingConfigId(java.lang.Long iMoRoutingConfigId) {
		set("iMoRoutingConfigId", iMoRoutingConfigId);
		return (M)this;
	}

	/**
	 * 工单工艺配置ID
	 */
	@JBoltField(name="imoroutingconfigid" ,columnName="iMoRoutingConfigId",type="Long", remark="工单工艺配置ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "imoroutingconfigid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIMoRoutingConfigId() {
		return getLong("iMoRoutingConfigId");
	}

	/**
	 * 设备ID
	 */
	public M setIEquipmentId(java.lang.Long iEquipmentId) {
		set("iEquipmentId", iEquipmentId);
		return (M)this;
	}

	/**
	 * 设备ID
	 */
	@JBoltField(name="iequipmentid" ,columnName="iEquipmentId",type="Long", remark="设备ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iequipmentid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIEquipmentId() {
		return getLong("iEquipmentId");
	}

}

