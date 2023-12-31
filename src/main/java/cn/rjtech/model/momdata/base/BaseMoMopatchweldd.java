package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 制造工单-补焊记录明细表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseMoMopatchweldd<M extends BaseMoMopatchweldd<M>> extends JBoltBaseModel<M>{
    
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**补焊记录主表ID*/
    public static final String IMOPATCHWELDMID = "iMoPatchWeldMid";
    /**生产工艺配置ID*/
    public static final String IMOROUTINGCONFIGID = "iMoRoutingConfigId";
    /**补焊数量*/
    public static final String IQTY = "iQty";
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
	 * 补焊记录主表ID
	 */
	public M setIMoPatchWeldMid(java.lang.Long iMoPatchWeldMid) {
		set("iMoPatchWeldMid", iMoPatchWeldMid);
		return (M)this;
	}

	/**
	 * 补焊记录主表ID
	 */
	@JBoltField(name="imopatchweldmid" ,columnName="iMoPatchWeldMid",type="Long", remark="补焊记录主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "imopatchweldmid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIMoPatchWeldMid() {
		return getLong("iMoPatchWeldMid");
	}

	/**
	 * 生产工艺配置ID
	 */
	public M setIMoRoutingConfigId(java.lang.Long iMoRoutingConfigId) {
		set("iMoRoutingConfigId", iMoRoutingConfigId);
		return (M)this;
	}

	/**
	 * 生产工艺配置ID
	 */
	@JBoltField(name="imoroutingconfigid" ,columnName="iMoRoutingConfigId",type="Long", remark="生产工艺配置ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "imoroutingconfigid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIMoRoutingConfigId() {
		return getLong("iMoRoutingConfigId");
	}

	/**
	 * 补焊数量
	 */
	public M setIQty(java.lang.Integer iQty) {
		set("iQty", iQty);
		return (M)this;
	}

	/**
	 * 补焊数量
	 */
	@JBoltField(name="iqty" ,columnName="iQty",type="Integer", remark="补焊数量", required=true, maxLength=10, fixed=0, order=4)
	@JSONField(name = "iqty")
	public java.lang.Integer getIQty() {
		return getInt("iQty");
	}

}

