package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 质量建模-检验适用标准类型
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseInventoryQcFormType<M extends BaseInventoryQcFormType<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**存货质检表格记录ID*/
    public static final String IINVENTORYQCFORMID = "iInventoryQcFormId";
    /**类型：1. 来料检 2. 在库检 3. 出库检*/
    public static final String ITYPE = "iType";
    /**类型名称*/
    public static final String CTYPENAME = "cTypeName";
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
	 * 存货质检表格记录ID
	 */
	public M setIInventoryQcFormId(java.lang.Long iInventoryQcFormId) {
		set("iInventoryQcFormId", iInventoryQcFormId);
		return (M)this;
	}

	/**
	 * 存货质检表格记录ID
	 */
	@JBoltField(name="iinventoryqcformid" ,columnName="iInventoryQcFormId",type="Long", remark="存货质检表格记录ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iinventoryqcformid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryQcFormId() {
		return getLong("iInventoryQcFormId");
	}

	/**
	 * 类型：1. 来料检 2. 在库检 3. 出库检
	 */
	public M setIType(java.lang.Integer iType) {
		set("iType", iType);
		return (M)this;
	}

	/**
	 * 类型：1. 来料检 2. 在库检 3. 出库检
	 */
	@JBoltField(name="itype" ,columnName="iType",type="Integer", remark="类型：1. 来料检 2. 在库检 3. 出库检", required=true, maxLength=10, fixed=0, order=3)
	@JSONField(name = "itype")
	public java.lang.Integer getIType() {
		return getInt("iType");
	}

	/**
	 * 类型名称
	 */
	public M setCTypeName(java.lang.String cTypeName) {
		set("cTypeName", cTypeName);
		return (M)this;
	}

	/**
	 * 类型名称
	 */
	@JBoltField(name="ctypename" ,columnName="cTypeName",type="String", remark="类型名称", required=false, maxLength=255, fixed=0, order=4)
	@JSONField(name = "ctypename")
	public java.lang.String getCTypeName() {
		return getStr("cTypeName");
	}

}

