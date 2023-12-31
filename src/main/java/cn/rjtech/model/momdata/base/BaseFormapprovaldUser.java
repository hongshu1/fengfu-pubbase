package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 系统设置-单据审批审批用户
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseFormapprovaldUser<M extends BaseFormapprovaldUser<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**单据审批节点ID*/
    public static final String IFORMAPPROVALDID = "iFormApprovalDid";
    /**顺序*/
    public static final String ISEQ = "iSeq";
    /**用户ID*/
    public static final String IUSERID = "iUserId";
    /**审核状态：1. 待审核 2. 审核通过 3. 审核不通过*/
    public static final String IAUDITSTATUS = "iAuditStatus";
    /**审核时间*/
    public static final String DAUDITTIME = "dAuditTime";
	/**人员档案ID*/
	public static final String IPERSONID = "iPersonId";
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
	 * 单据审批节点ID
	 */
	public M setIFormApprovalId(java.lang.Long iFormApprovalDid) {
		set("iFormApprovalDid", iFormApprovalDid);
		return (M)this;
	}

	/**
	 * 单据审批节点ID
	 */
	@JBoltField(name="iformapprovaldid" ,columnName="iFormApprovalDid",type="Long", remark="单据审批节点ID", required=true,
			maxLength=19, fixed=0, order=2)
	@JSONField(name = "iformapprovaldid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIFormApprovalDid() {
		return getLong("iFormApprovalDid");
	}

	/**
	 * 顺序
	 */
	public M setISeq(java.lang.Integer iSeq) {
		set("iSeq", iSeq);
		return (M)this;
	}

	/**
	 * 顺序
	 */
	@JBoltField(name="iseq" ,columnName="iSeq",type="Integer", remark="顺序", required=true, maxLength=10, fixed=0, order=3)
	@JSONField(name = "iseq")
	public java.lang.Integer getISeq() {
		return getInt("iSeq");
	}

	/**
	 * 用户ID
	 */
	public M setIUserId(java.lang.Long iUserId) {
		set("iUserId", iUserId);
		return (M)this;
	}

	/**
	 * 用户ID
	 */
	@JBoltField(name="iuserid" ,columnName="iUserId",type="Long", remark="用户ID", required=true, maxLength=19, fixed=0, order=4)
	@JSONField(name = "iuserid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUserId() {
		return getLong("iUserId");
	}

	/**
	 * 审核状态：1. 待审核 2. 审核通过 3. 审核不通过
	 */
	public M setIAuditStatus(java.lang.Integer iAuditStatus) {
		set("iAuditStatus", iAuditStatus);
		return (M)this;
	}

	/**
	 * 审核状态：1. 待审核 2. 审核通过 3. 审核不通过
	 */
	@JBoltField(name="iauditstatus" ,columnName="iAuditStatus",type="Integer", remark="审核状态：1. 待审核 2. 审核通过 3. 审核不通过", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "iauditstatus")
	public java.lang.Integer getIAuditStatus() {
		return getInt("iAuditStatus");
	}

	/**
	 * 审核时间
	 */
	public M setDAuditTime(java.util.Date dAuditTime) {
		set("dAuditTime", dAuditTime);
		return (M)this;
	}

	/**
	 * 审核时间
	 */
	@JBoltField(name="daudittime" ,columnName="dAuditTime",type="Date", remark="审核时间", required=false, maxLength=23, fixed=3, order=6)
	@JSONField(name = "daudittime")
	public java.util.Date getDAuditTime() {
		return getDate("dAuditTime");
	}

	/**
	 * 人员ID
	 */
	public M setIPersonId(java.lang.Long iPersonId) {
		set("iPersonId", iPersonId);
		return (M)this;
	}

	/**
	 * 人员ID
	 */
	@JBoltField(name="ipersonid" ,columnName="iPersonId",type="Long", remark="人员ID", required=true, maxLength=19,
			fixed=0,	order=4)
	@JSONField(name = "ipersonid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIPersonId() {
		return getLong("iPersonId");
	}

}

