package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 系统配置-审批流表单
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseApprovalForm<M extends BaseApprovalForm<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**审批流主表ID*/
    public static final String IAPPROVALMID = "iApprovalMid";
    /**表单ID*/
    public static final String IFORMID = "iFormId";
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
	 * 审批流主表ID
	 */
	public M setIApprovalMid(java.lang.Long iApprovalMid) {
		set("iApprovalMid", iApprovalMid);
		return (M)this;
	}

	/**
	 * 审批流主表ID
	 */
	@JBoltField(name="iapprovalmid" ,columnName="iApprovalMid",type="Long", remark="审批流主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iapprovalmid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIApprovalMid() {
		return getLong("iApprovalMid");
	}

	/**
	 * 表单ID
	 */
	public M setIFormId(java.lang.Long iFormId) {
		set("iFormId", iFormId);
		return (M)this;
	}

	/**
	 * 表单ID
	 */
	@JBoltField(name="iformid" ,columnName="iFormId",type="Long", remark="表单ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iformid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIFormId() {
		return getLong("iFormId");
	}

}
