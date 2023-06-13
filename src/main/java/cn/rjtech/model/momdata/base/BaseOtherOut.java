package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 其它出库单
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseOtherOut<M extends BaseOtherOut<M>> extends JBoltBaseModel<M>{
    public static final String DATASOURCE_CONFIG_NAME = "momdata";
    /**AutoID*/
    public static final String AUTOID = "AutoID";
    /**来源类型;MO生产工单*/
    public static final String SOURCEBILLTYPE = "SourceBillType";
    /**来源单据Did*/
    public static final String SOURCEBILLDID = "SourceBillDid";
    /**收发类别*/
    public static final String RDCODE = "RdCode";
    /**组织代码*/
    public static final String ORGANIZECODE = "OrganizeCode";
    /**单号*/
    public static final String BILLNO = "BillNo";
    /**业务类型*/
    public static final String BILLTYPE = "BillType";
    /**单据日期*/
    public static final String BILLDATE = "BillDate";
    /**部门编码*/
    public static final String DEPTCODE = "DeptCode";
    /**仓库编码*/
    public static final String WHCODE = "Whcode";
    /**供应商*/
    public static final String VENCODE = "VenCode";
    /**审核人*/
    public static final String AUDITPERSON = "AuditPerson";
    /**审核日期*/
    public static final String AUDITDATE = "AuditDate";
    /**创建人*/
    public static final String CCREATENAME = "cCreateName";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**更新人*/
    public static final String CUPDATENAME = "cUpdateName";
    /**更新时间*/
    public static final String DUPDATETIME = "dupdateTime";
    /**订单状态：1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已出库 6. 已核对 7. 已关闭*/
    public static final String STATUS = "Status";
    /**审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过*/
    public static final String AUDITSTATUS = "AuditStatus";
    /**其他出库=OtherOut，特殊领料=OtherOutMES*/
    public static final String TYPE = "Type";
    /***/
    public static final String MEMO = "Memo";
    /**原因分析*/
    public static final String REASON = "Reason";
    /**审批方式：1. 审核 2. 审批流*/
    public static final String IAUDITWAY = "iAuditWay";
    /**提审时间*/
    public static final String DSUBMITTIME = "dSubmitTime";
    /**审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过*/
    public static final String IAUDITSTATUS = "iAuditStatus";
    /**审核时间*/
    public static final String DAUDITTIME = "dAuditTime";
    /**是否删除：0. 否 1. 是*/
    public static final String ISDELETED = "isDeleted";
    /**创建人ID*/
    public static final String ICREATEBY = "iCreateBy";
    /**更新人ID*/
    public static final String IUPDATEBY = "iUpdateBy";
	/**
	 * AutoID
	 */
	public M setAutoID(java.lang.String AutoID) {
		set("AutoID", AutoID);
		return (M)this;
	}

	/**
	 * AutoID
	 */
	@JBoltField(name="autoid" ,columnName="AutoID",type="String", remark="AutoID", required=true, maxLength=30, fixed=0, order=1)
	@JSONField(name = "autoid")
	public java.lang.String getAutoID() {
		return getStr("AutoID");
	}

	/**
	 * 来源类型;MO生产工单
	 */
	public M setSourceBillType(java.lang.String SourceBillType) {
		set("SourceBillType", SourceBillType);
		return (M)this;
	}

	/**
	 * 来源类型;MO生产工单
	 */
	@JBoltField(name="sourcebilltype" ,columnName="SourceBillType",type="String", remark="来源类型;MO生产工单", required=false, maxLength=30, fixed=0, order=2)
	@JSONField(name = "sourcebilltype")
	public java.lang.String getSourceBillType() {
		return getStr("SourceBillType");
	}

	/**
	 * 来源单据Did
	 */
	public M setSourceBillDid(java.lang.String SourceBillDid) {
		set("SourceBillDid", SourceBillDid);
		return (M)this;
	}

	/**
	 * 来源单据Did
	 */
	@JBoltField(name="sourcebilldid" ,columnName="SourceBillDid",type="String", remark="来源单据Did", required=false, maxLength=30, fixed=0, order=3)
	@JSONField(name = "sourcebilldid")
	public java.lang.String getSourceBillDid() {
		return getStr("SourceBillDid");
	}

	/**
	 * 收发类别
	 */
	public M setRdCode(java.lang.String RdCode) {
		set("RdCode", RdCode);
		return (M)this;
	}

	/**
	 * 收发类别
	 */
	@JBoltField(name="rdcode" ,columnName="RdCode",type="String", remark="收发类别", required=false, maxLength=30, fixed=0, order=4)
	@JSONField(name = "rdcode")
	public java.lang.String getRdCode() {
		return getStr("RdCode");
	}

	/**
	 * 组织代码
	 */
	public M setOrganizeCode(java.lang.String OrganizeCode) {
		set("OrganizeCode", OrganizeCode);
		return (M)this;
	}

	/**
	 * 组织代码
	 */
	@JBoltField(name="organizecode" ,columnName="OrganizeCode",type="String", remark="组织代码", required=false, maxLength=30, fixed=0, order=5)
	@JSONField(name = "organizecode")
	public java.lang.String getOrganizeCode() {
		return getStr("OrganizeCode");
	}

	/**
	 * 单号
	 */
	public M setBillNo(java.lang.String BillNo) {
		set("BillNo", BillNo);
		return (M)this;
	}

	/**
	 * 单号
	 */
	@JBoltField(name="billno" ,columnName="BillNo",type="String", remark="单号", required=false, maxLength=30, fixed=0, order=6)
	@JSONField(name = "billno")
	public java.lang.String getBillNo() {
		return getStr("BillNo");
	}

	/**
	 * 业务类型
	 */
	public M setBillType(java.lang.String BillType) {
		set("BillType", BillType);
		return (M)this;
	}

	/**
	 * 业务类型
	 */
	@JBoltField(name="billtype" ,columnName="BillType",type="String", remark="业务类型", required=false, maxLength=30, fixed=0, order=7)
	@JSONField(name = "billtype")
	public java.lang.String getBillType() {
		return getStr("BillType");
	}

	/**
	 * 单据日期
	 */
	public M setBillDate(java.util.Date BillDate) {
		set("BillDate", BillDate);
		return (M)this;
	}

	/**
	 * 单据日期
	 */
	@JBoltField(name="billdate" ,columnName="BillDate",type="Date", remark="单据日期", required=false, maxLength=30, fixed=0, order=8)
	@JSONField(name = "billdate")
	public java.util.Date getBillDate() {
		return getDate("BillDate");
	}

	/**
	 * 部门编码
	 */
	public M setDeptCode(java.lang.String DeptCode) {
		set("DeptCode", DeptCode);
		return (M)this;
	}

	/**
	 * 部门编码
	 */
	@JBoltField(name="deptcode" ,columnName="DeptCode",type="String", remark="部门编码", required=false, maxLength=30, fixed=0, order=9)
	@JSONField(name = "deptcode")
	public java.lang.String getDeptCode() {
		return getStr("DeptCode");
	}

	/**
	 * 仓库编码
	 */
	public M setWhcode(java.lang.String Whcode) {
		set("Whcode", Whcode);
		return (M)this;
	}

	/**
	 * 仓库编码
	 */
	@JBoltField(name="whcode" ,columnName="Whcode",type="String", remark="仓库编码", required=false, maxLength=30, fixed=0, order=10)
	@JSONField(name = "whcode")
	public java.lang.String getWhcode() {
		return getStr("Whcode");
	}

	/**
	 * 供应商
	 */
	public M setVenCode(java.lang.String VenCode) {
		set("VenCode", VenCode);
		return (M)this;
	}

	/**
	 * 供应商
	 */
	@JBoltField(name="vencode" ,columnName="VenCode",type="String", remark="供应商", required=false, maxLength=30, fixed=0, order=11)
	@JSONField(name = "vencode")
	public java.lang.String getVenCode() {
		return getStr("VenCode");
	}

	/**
	 * 审核人
	 */
	public M setAuditPerson(java.lang.String AuditPerson) {
		set("AuditPerson", AuditPerson);
		return (M)this;
	}

	/**
	 * 审核人
	 */
	@JBoltField(name="auditperson" ,columnName="AuditPerson",type="String", remark="审核人", required=false, maxLength=30, fixed=0, order=12)
	@JSONField(name = "auditperson")
	public java.lang.String getAuditPerson() {
		return getStr("AuditPerson");
	}

	/**
	 * 审核日期
	 */
	public M setAuditDate(java.util.Date AuditDate) {
		set("AuditDate", AuditDate);
		return (M)this;
	}

	/**
	 * 审核日期
	 */
	@JBoltField(name="auditdate" ,columnName="AuditDate",type="Date", remark="审核日期", required=false, maxLength=10, fixed=0, order=13)
	@JSONField(name = "auditdate")
	public java.util.Date getAuditDate() {
		return getDate("AuditDate");
	}

	/**
	 * 创建人
	 */
	public M setCCreateName(java.lang.String cCreateName) {
		set("cCreateName", cCreateName);
		return (M)this;
	}

	/**
	 * 创建人
	 */
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人", required=false, maxLength=60, fixed=0, order=14)
	@JSONField(name = "ccreatename")
	public java.lang.String getCCreateName() {
		return getStr("cCreateName");
	}

	/**
	 * 创建时间
	 */
	public M setDCreateTime(java.util.Date dCreateTime) {
		set("dCreateTime", dCreateTime);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=15)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDCreateTime() {
		return getDate("dCreateTime");
	}

	/**
	 * 更新人
	 */
	public M setCUpdateName(java.lang.String cUpdateName) {
		set("cUpdateName", cUpdateName);
		return (M)this;
	}

	/**
	 * 更新人
	 */
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人", required=false, maxLength=30, fixed=0, order=16)
	@JSONField(name = "cupdatename")
	public java.lang.String getCUpdateName() {
		return getStr("cUpdateName");
	}

	/**
	 * 更新时间
	 */
	public M setDupdateTime(java.util.Date dupdateTime) {
		set("dupdateTime", dupdateTime);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="dupdatetime" ,columnName="dupdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=17)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDupdateTime() {
		return getDate("dupdateTime");
	}

	/**
	 * 订单状态：1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已出库 6. 已核对 7. 已关闭
	 */
	public M setStatus(java.lang.Integer Status) {
		set("Status", Status);
		return (M)this;
	}

	/**
	 * 订单状态：1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已出库 6. 已核对 7. 已关闭
	 */
	@JBoltField(name="status" ,columnName="Status",type="Integer", remark="订单状态：1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已出库 6. 已核对 7. 已关闭", required=false, maxLength=10, fixed=0, order=18)
	@JSONField(name = "status")
	public java.lang.Integer getStatus() {
		return getInt("Status");
	}

	/**
	 * 审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
	 */
	public M setAuditStatus(java.lang.Integer AuditStatus) {
		set("AuditStatus", AuditStatus);
		return (M)this;
	}

	/**
	 * 审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
	 */
	@JBoltField(name="auditstatus" ,columnName="AuditStatus",type="Integer", remark="审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过", required=false, maxLength=10, fixed=0, order=19)
	@JSONField(name = "auditstatus")
	public java.lang.Integer getAuditStatus() {
		return getInt("AuditStatus");
	}

	/**
	 * 其他出库=OtherOut，特殊领料=OtherOutMES
	 */
	public M setType(java.lang.String Type) {
		set("Type", Type);
		return (M)this;
	}

	/**
	 * 其他出库=OtherOut，特殊领料=OtherOutMES
	 */
	@JBoltField(name="type" ,columnName="Type",type="String", remark="其他出库=OtherOut，特殊领料=OtherOutMES", required=false, maxLength=30, fixed=0, order=20)
	@JSONField(name = "type")
	public java.lang.String getType() {
		return getStr("Type");
	}

	public M setMemo(java.lang.String Memo) {
		set("Memo", Memo);
		return (M)this;
	}

	@JBoltField(name="memo" ,columnName="Memo",type="String", remark="MEMO", required=false, maxLength=30, fixed=0, order=21)
	@JSONField(name = "memo")
	public java.lang.String getMemo() {
		return getStr("Memo");
	}

	/**
	 * 原因分析
	 */
	public M setReason(java.lang.String Reason) {
		set("Reason", Reason);
		return (M)this;
	}

	/**
	 * 原因分析
	 */
	@JBoltField(name="reason" ,columnName="Reason",type="String", remark="原因分析", required=false, maxLength=30, fixed=0, order=22)
	@JSONField(name = "reason")
	public java.lang.String getReason() {
		return getStr("Reason");
	}

	/**
	 * 审批方式：1. 审核 2. 审批流
	 */
	public M setIAuditWay(java.lang.Integer iAuditWay) {
		set("iAuditWay", iAuditWay);
		return (M)this;
	}

	/**
	 * 审批方式：1. 审核 2. 审批流
	 */
	@JBoltField(name="iauditway" ,columnName="iAuditWay",type="Integer", remark="审批方式：1. 审核 2. 审批流", required=false, maxLength=10, fixed=0, order=23)
	@JSONField(name = "iauditway")
	public java.lang.Integer getIAuditWay() {
		return getInt("iAuditWay");
	}

	/**
	 * 提审时间
	 */
	public M setDSubmitTime(java.util.Date dSubmitTime) {
		set("dSubmitTime", dSubmitTime);
		return (M)this;
	}

	/**
	 * 提审时间
	 */
	@JBoltField(name="dsubmittime" ,columnName="dSubmitTime",type="Date", remark="提审时间", required=false, maxLength=23, fixed=3, order=24)
	@JSONField(name = "dsubmittime")
	public java.util.Date getDSubmitTime() {
		return getDate("dSubmitTime");
	}

	/**
	 * 审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
	 */
	public M setIAuditStatus(java.lang.Integer iAuditStatus) {
		set("iAuditStatus", iAuditStatus);
		return (M)this;
	}

	/**
	 * 审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
	 */
	@JBoltField(name="iauditstatus" ,columnName="iAuditStatus",type="Integer", remark="审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过", required=true, maxLength=10, fixed=0, order=25)
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
	@JBoltField(name="daudittime" ,columnName="dAuditTime",type="Date", remark="审核时间", required=false, maxLength=23, fixed=3, order=26)
	@JSONField(name = "daudittime")
	public java.util.Date getDAuditTime() {
		return getDate("dAuditTime");
	}

	/**
	 * 是否删除：0. 否 1. 是
	 */
	public M setIsDeleted(java.lang.Boolean isDeleted) {
		set("isDeleted", isDeleted);
		return (M)this;
	}

	/**
	 * 是否删除：0. 否 1. 是
	 */
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="是否删除：0. 否 1. 是", required=false, maxLength=1, fixed=0, order=27)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 创建人ID
	 */
	public M setICreateBy(java.lang.Long iCreateBy) {
		set("iCreateBy", iCreateBy);
		return (M)this;
	}

	/**
	 * 创建人ID
	 */
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=false, maxLength=19, fixed=0, order=28)
	@JSONField(name = "icreateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICreateBy() {
		return getLong("iCreateBy");
	}

	/**
	 * 更新人ID
	 */
	public M setIUpdateBy(java.lang.Long iUpdateBy) {
		set("iUpdateBy", iUpdateBy);
		return (M)this;
	}

	/**
	 * 更新人ID
	 */
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=false, maxLength=19, fixed=0, order=29)
	@JSONField(name = "iupdateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUpdateBy() {
		return getLong("iUpdateBy");
	}

}

