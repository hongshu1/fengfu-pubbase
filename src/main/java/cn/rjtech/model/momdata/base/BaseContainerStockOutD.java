package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 容器档案-容器出库明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseContainerStockOutD<M extends BaseContainerStockOutD<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**容器入库主表ID*/
    public static final String ICONTAINERSTOCKOUTMID = "iContainerStockOutMid";
    /**容器ID*/
    public static final String ICONTAINERID = "iContainerId";
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
	 * 容器入库主表ID
	 */
	public M setIContainerStockOutMid(java.lang.Long iContainerStockOutMid) {
		set("iContainerStockOutMid", iContainerStockOutMid);
		return (M)this;
	}

	/**
	 * 容器入库主表ID
	 */
	@JBoltField(name="icontainerstockoutmid" ,columnName="iContainerStockOutMid",type="Long", remark="容器入库主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "icontainerstockoutmid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIContainerStockOutMid() {
		return getLong("iContainerStockOutMid");
	}

	/**
	 * 容器ID
	 */
	public M setIContainerId(java.lang.Long iContainerId) {
		set("iContainerId", iContainerId);
		return (M)this;
	}

	/**
	 * 容器ID
	 */
	@JBoltField(name="icontainerid" ,columnName="iContainerId",type="Long", remark="容器ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "icontainerid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIContainerId() {
		return getLong("iContainerId");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=4)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
	}

}

