package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 系统设置-表单审批流程主表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseFormApprovalFlowM<M extends BaseFormApprovalFlowM<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**单据审批流ID*/
    public static final String IAPPROVALID = "iApprovalId";
    /**单据审批节点ID*/
    public static final String IAPPROVALDID = "iApprovalDid";
    /**审批节点*/
    public static final String ISEQ = "iSeq";
    /**多人审批时采用的审批方式;1. 依次审批 2. 会签（需所有审批人同意） 3. 或签（一名审批人同意或拒绝即可）*/
    public static final String IAPPROVALWAY = "iApprovalWay";
    /**审核状态;1. 待审核 2. 审核通过 3. 审核不通过*/
    public static final String IAUDITSTATUS = "iAuditStatus";
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
	 * 单据审批流ID
	 */
	public M setIApprovalId(java.lang.Long iApprovalId) {
		set("iApprovalId", iApprovalId);
		return (M)this;
	}

	/**
	 * 单据审批流ID
	 */
	@JBoltField(name="iapprovalid" ,columnName="iApprovalId",type="Long", remark="单据审批流ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iapprovalid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIApprovalId() {
		return getLong("iApprovalId");
	}

	/**
	 * 单据审批节点ID
	 */
	public M setIApprovalDid(java.lang.Long iApprovalDid) {
		set("iApprovalDid", iApprovalDid);
		return (M)this;
	}

	/**
	 * 单据审批节点ID
	 */
	@JBoltField(name="iapprovaldid" ,columnName="iApprovalDid",type="Long", remark="单据审批节点ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iapprovaldid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIApprovalDid() {
		return getLong("iApprovalDid");
	}

	/**
	 * 审批节点
	 */
	public M setISeq(java.lang.Integer iSeq) {
		set("iSeq", iSeq);
		return (M)this;
	}

	/**
	 * 审批节点
	 */
	@JBoltField(name="iseq" ,columnName="iSeq",type="Integer", remark="审批节点", required=true, maxLength=10, fixed=0, order=4)
	@JSONField(name = "iseq")
	public java.lang.Integer getISeq() {
		return getInt("iSeq");
	}

	/**
	 * 多人审批时采用的审批方式;1. 依次审批 2. 会签（需所有审批人同意） 3. 或签（一名审批人同意或拒绝即可）
	 */
	public M setIApprovalWay(java.lang.Integer iApprovalWay) {
		set("iApprovalWay", iApprovalWay);
		return (M)this;
	}

	/**
	 * 多人审批时采用的审批方式;1. 依次审批 2. 会签（需所有审批人同意） 3. 或签（一名审批人同意或拒绝即可）
	 */
	@JBoltField(name="iapprovalway" ,columnName="iApprovalWay",type="Integer", remark="多人审批时采用的审批方式;1. 依次审批 2. 会签（需所有审批人同意） 3. 或签（一名审批人同意或拒绝即可）", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "iapprovalway")
	public java.lang.Integer getIApprovalWay() {
		return getInt("iApprovalWay");
	}

	/**
	 * 审核状态;1. 待审核 2. 审核通过 3. 审核不通过
	 */
	public M setIAuditStatus(java.lang.Integer iAuditStatus) {
		set("iAuditStatus", iAuditStatus);
		return (M)this;
	}

	/**
	 * 审核状态;1. 待审核 2. 审核通过 3. 审核不通过
	 */
	@JBoltField(name="iauditstatus" ,columnName="iAuditStatus",type="Integer", remark="审核状态;1. 待审核 2. 审核通过 3. 审核不通过", required=true, maxLength=10, fixed=0, order=6)
	@JSONField(name = "iauditstatus")
	public java.lang.Integer getIAuditStatus() {
		return getInt("iAuditStatus");
	}

}

