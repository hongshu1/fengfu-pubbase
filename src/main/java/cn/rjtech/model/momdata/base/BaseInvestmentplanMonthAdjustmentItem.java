package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseInvestmentplanMonthAdjustmentItem<M extends BaseInvestmentplanMonthAdjustmentItem<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**调整主表ID*/
    public static final String IADJUSTMENTMID = "iAdjustmentMId";
    /**投资计划项目ID*/
    public static final String IPLANID = "iPlanId";
	/**
	 * 主键ID
	 */
	public M setIautoid(java.lang.Long iautoid) {
		set("iAutoId", iautoid);
		return (M)this;
	}

	/**
	 * 主键ID
	 */
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Long", remark="主键ID", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIautoid() {
		return getLong("iAutoId");
	}

	/**
	 * 调整主表ID
	 */
	public M setIadjustmentmid(java.lang.Long iadjustmentmid) {
		set("iAdjustmentMId", iadjustmentmid);
		return (M)this;
	}

	/**
	 * 调整主表ID
	 */
	@JBoltField(name="iadjustmentmid" ,columnName="iAdjustmentMId",type="Long", remark="调整主表ID", required=false, maxLength=19, fixed=0, order=2)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIadjustmentmid() {
		return getLong("iAdjustmentMId");
	}

	/**
	 * 投资计划项目ID
	 */
	public M setIplanid(java.lang.Long iplanid) {
		set("iPlanId", iplanid);
		return (M)this;
	}

	/**
	 * 投资计划项目ID
	 */
	@JBoltField(name="iplanid" ,columnName="iPlanId",type="Long", remark="投资计划项目ID", required=false, maxLength=19, fixed=0, order=3)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIplanid() {
		return getLong("iPlanId");
	}

}

