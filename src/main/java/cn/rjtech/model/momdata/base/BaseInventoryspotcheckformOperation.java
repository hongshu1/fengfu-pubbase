package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 质量建模-存货点检工序
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseInventoryspotcheckformOperation<M extends BaseInventoryspotcheckformOperation<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**存货点检表ID*/
    public static final String IINVENTORYSPOTCHECKFORMID = "iInventorySpotCheckFormId";
    /**工序ID*/
    public static final String IOPERATIONID = "iOperationId";
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
	 * 存货点检表ID
	 */
	public M setIInventorySpotCheckFormId(java.lang.Long iInventorySpotCheckFormId) {
		set("iInventorySpotCheckFormId", iInventorySpotCheckFormId);
		return (M)this;
	}

	/**
	 * 存货点检表ID
	 */
	@JBoltField(name="iinventoryspotcheckformid" ,columnName="iInventorySpotCheckFormId",type="Long", remark="存货点检表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iinventoryspotcheckformid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventorySpotCheckFormId() {
		return getLong("iInventorySpotCheckFormId");
	}

	/**
	 * 工序ID
	 */
	public M setIOperationId(java.lang.Long iOperationId) {
		set("iOperationId", iOperationId);
		return (M)this;
	}

	/**
	 * 工序ID
	 */
	@JBoltField(name="ioperationid" ,columnName="iOperationId",type="Long", remark="工序ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "ioperationid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIOperationId() {
		return getLong("iOperationId");
	}

}

