package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 扩展字段值集
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseDescflexsegvaluesetvalue<M extends BaseDescflexsegvaluesetvalue<M>> extends JBoltBaseModel<M> {
    /**主键*/
    public static final String IAUTOID = "iAutoId";
    /**实体扩展字段表ID*/
    public static final String IDESCFLEXFIELDDEFID = "iDescFlexFieldDefId";
    /**MES数据ID*/
    public static final String IMESDATAID = "iMesDataId";
    /**值*/
    public static final String CVALUE = "cValue";
	/**
	 * 主键
	 */
	public M setIautoid(Long iautoid) {
		set("iAutoId", iautoid);
		return (M)this;
	}

	/**
	 * 主键
	 */
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Long", remark="主键", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIautoid() {
		return getLong("iAutoId");
	}

	/**
	 * 实体扩展字段表ID
	 */
	public M setIdescflexfielddefid(Long idescflexfielddefid) {
		set("iDescFlexFieldDefId", idescflexfielddefid);
		return (M)this;
	}

	/**
	 * 实体扩展字段表ID
	 */
	@JBoltField(name="idescflexfielddefid" ,columnName="iDescFlexFieldDefId",type="Long", remark="实体扩展字段表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIdescflexfielddefid() {
		return getLong("iDescFlexFieldDefId");
	}

	/**
	 * MES数据ID
	 */
	public M setImesdataid(Long imesdataid) {
		set("iMesDataId", imesdataid);
		return (M)this;
	}

	/**
	 * MES数据ID
	 */
	@JBoltField(name="imesdataid" ,columnName="iMesDataId",type="Long", remark="MES数据ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getImesdataid() {
		return getLong("iMesDataId");
	}

	/**
	 * 值
	 */
	public M setCvalue(String cvalue) {
		set("cValue", cvalue);
		return (M)this;
	}

	/**
	 * 值
	 */
	@JBoltField(name="cvalue" ,columnName="cValue",type="String", remark="值", required=false, maxLength=255, fixed=0, order=4)
	public String getCvalue() {
		return getStr("cValue");
	}

}
