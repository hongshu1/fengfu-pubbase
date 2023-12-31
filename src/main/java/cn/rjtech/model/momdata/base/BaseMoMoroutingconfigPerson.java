package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 制造工单-工单工艺人员配置
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseMoMoroutingconfigPerson<M extends BaseMoMoroutingconfigPerson<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**工单工艺配置ID*/
    public static final String IMOROUTINGCONFIGID = "iMoRoutingConfigId";
    /**人员ID*/
    public static final String IPERSONID = "iPersonId";
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
	 * 人员ID
	 */
	public M setIPersonId(java.lang.Long iPersonId) {
		set("iPersonId", iPersonId);
		return (M)this;
	}

	/**
	 * 人员ID
	 */
	@JBoltField(name="ipersonid" ,columnName="iPersonId",type="Long", remark="人员ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "ipersonid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIPersonId() {
		return getLong("iPersonId");
	}

}

