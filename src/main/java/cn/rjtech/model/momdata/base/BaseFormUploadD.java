package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 制造工单-上传记录明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseFormUploadD<M extends BaseFormUploadD<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**上传记录主表ID*/
    public static final String IFORMUPLOADMID = "iFormUploadMid";
    /**附件;多个“,”分隔*/
    public static final String CATTACHMENTS = "cAttachments";
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
	 * 上传记录主表ID
	 */
	public M setIFormUploadMid(java.lang.Long iFormUploadMid) {
		set("iFormUploadMid", iFormUploadMid);
		return (M)this;
	}

	/**
	 * 上传记录主表ID
	 */
	@JBoltField(name="iformuploadmid" ,columnName="iFormUploadMid",type="Long", remark="上传记录主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iformuploadmid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIFormUploadMid() {
		return getLong("iFormUploadMid");
	}

	/**
	 * 附件;多个“,”分隔
	 */
	public M setCAttachments(java.lang.String cAttachments) {
		set("cAttachments", cAttachments);
		return (M)this;
	}

	/**
	 * 附件;多个“,”分隔
	 */
	@JBoltField(name="cattachments" ,columnName="cAttachments",type="String", remark="附件;多个“,”分隔", required=false, maxLength=2147483647, fixed=0, order=3)
	@JSONField(name = "cattachments")
	public java.lang.String getCAttachments() {
		return getStr("cAttachments");
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
