package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 禀议书附件表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseProposalAttachment<M extends BaseProposalAttachment<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**禀议书主表ID*/
    public static final String IPROPOSALMID = "iProposalMId";
    /**附件名称*/
    public static final String ATTACHMENTNAME = "AttachmentName";
    /**附件路径*/
    public static final String ATTACHMENTURI = "AttachmentUri";
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
	 * 禀议书主表ID
	 */
	public M setIproposalmid(java.lang.Long iproposalmid) {
		set("iProposalMId", iproposalmid);
		return (M)this;
	}

	/**
	 * 禀议书主表ID
	 */
	@JBoltField(name="iproposalmid" ,columnName="iProposalMId",type="Long", remark="禀议书主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIproposalmid() {
		return getLong("iProposalMId");
	}

	/**
	 * 附件名称
	 */
	public M setAttachmentname(java.lang.String attachmentname) {
		set("AttachmentName", attachmentname);
		return (M)this;
	}

	/**
	 * 附件名称
	 */
	@JBoltField(name="attachmentname" ,columnName="AttachmentName",type="String", remark="附件名称", required=false, maxLength=200, fixed=0, order=3)
	public java.lang.String getAttachmentname() {
		return getStr("AttachmentName");
	}

	/**
	 * 附件路径
	 */
	public M setAttachmenturi(java.lang.String attachmenturi) {
		set("AttachmentUri", attachmenturi);
		return (M)this;
	}

	/**
	 * 附件路径
	 */
	@JBoltField(name="attachmenturi" ,columnName="AttachmentUri",type="String", remark="附件路径", required=false, maxLength=200, fixed=0, order=4)
	public java.lang.String getAttachmenturi() {
		return getStr("AttachmentUri");
	}

}

