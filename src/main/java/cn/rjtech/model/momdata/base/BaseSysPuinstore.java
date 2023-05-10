package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 采购入库单
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysPuinstore<M extends BaseSysPuinstore<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String AUTOID = "AutoID";
    /**到货单类型;采购PO  委外OM*/
    public static final String BILLTYPE = "BillType";
    /**组织编码*/
    public static final String ORGANIZECODE = "OrganizeCode";
    /**入库单号*/
    public static final String BILLNO = "BillNo";
    /**单据日期*/
    public static final String BILLDATE = "BillDate";
    /**供应商编码*/
    public static final String VENCODE = "VenCode";
    /**备注*/
    public static final String MEMO = "Memo";
    /**创建人*/
    public static final String CREATEPERSON = "CreatePerson";
    /**创建日期*/
    public static final String CREATEDATE = "CreateDate";
    /**审核人*/
    public static final String AUDITPERSON = "AuditPerson";
    /**审核日期*/
    public static final String AUDITDATE = "AuditDate";
    /**修改人*/
    public static final String MODIFYPERSON = "ModifyPerson";
    /**修改时间*/
    public static final String MODIFYDATE = "ModifyDate";
    /**删除状态：0. 未删除 1. 已删除*/
    public static final String ISDELETED = "IsDeleted";
    /**仓库*/
    public static final String REPOSITORY = "repository";
    /**仓库名称*/
    public static final String REPOSITORYNAME = "repositoryName";
    /**订单号*/
    public static final String INDENT = "indent";
    /**生产部门*/
    public static final String DEPT = "dept";
    /**生产部门名字*/
    public static final String DEPTNAME = "deptName";
    /**采购类型*/
    public static final String PROCURETYPE = "procureType";
    /**入库类别*/
    public static final String WAREHOUSINGTYPE = "warehousingType";
    /**备注*/
    public static final String REMARK = "remark";
	/**状态 1已保存 2待审批 3已审批 4审批不通过 */
	public static final String STATE = "state";
	/**
	 * 主键ID
	 */
	public M setAutoID(java.lang.String AutoID) {
		set("AutoID", AutoID);
		return (M)this;
	}

	/**
	 * 主键ID
	 */
	@JBoltField(name="autoid" ,columnName="AutoID",type="String", remark="主键ID", required=true, maxLength=30, fixed=0, order=1)
	@JSONField(name = "autoid")
	public java.lang.String getAutoID() {
		return getStr("AutoID");
	}

	/**
	 * 到货单类型;采购PO  委外OM
	 */
	public M setBillType(java.lang.String BillType) {
		set("BillType", BillType);
		return (M)this;
	}

	/**
	 * 到货单类型;采购PO  委外OM
	 */
	@JBoltField(name="billtype" ,columnName="BillType",type="String", remark="到货单类型;采购PO  委外OM", required=false, maxLength=30, fixed=0, order=2)
	@JSONField(name = "billtype")
	public java.lang.String getBillType() {
		return getStr("BillType");
	}

	/**
	 * 组织编码
	 */
	public M setOrganizeCode(java.lang.String OrganizeCode) {
		set("OrganizeCode", OrganizeCode);
		return (M)this;
	}

	/**
	 * 组织编码
	 */
	@JBoltField(name="organizecode" ,columnName="OrganizeCode",type="String", remark="组织编码", required=false, maxLength=30, fixed=0, order=3)
	@JSONField(name = "organizecode")
	public java.lang.String getOrganizeCode() {
		return getStr("OrganizeCode");
	}

	/**
	 * 入库单号
	 */
	public M setBillNo(java.lang.String BillNo) {
		set("BillNo", BillNo);
		return (M)this;
	}

	/**
	 * 入库单号
	 */
	@JBoltField(name="billno" ,columnName="BillNo",type="String", remark="入库单号", required=false, maxLength=30, fixed=0, order=4)
	@JSONField(name = "billno")
	public java.lang.String getBillNo() {
		return getStr("BillNo");
	}

	/**
	 * 单据日期
	 */
	public M setBillDate(java.lang.String BillDate) {
		set("BillDate", BillDate);
		return (M)this;
	}

	/**
	 * 单据日期
	 */
	@JBoltField(name="billdate" ,columnName="BillDate",type="String", remark="单据日期", required=false, maxLength=30, fixed=0, order=5)
	@JSONField(name = "billdate")
	public java.lang.String getBillDate() {
		return getStr("BillDate");
	}

	/**
	 * 供应商编码
	 */
	public M setVenCode(java.lang.String VenCode) {
		set("VenCode", VenCode);
		return (M)this;
	}

	/**
	 * 供应商编码
	 */
	@JBoltField(name="vencode" ,columnName="VenCode",type="String", remark="供应商编码", required=false, maxLength=30, fixed=0, order=6)
	@JSONField(name = "vencode")
	public java.lang.String getVenCode() {
		return getStr("VenCode");
	}

	/**
	 * 备注
	 */
	public M setMemo(java.lang.String Memo) {
		set("Memo", Memo);
		return (M)this;
	}

	/**
	 * 备注
	 */
	@JBoltField(name="memo" ,columnName="Memo",type="String", remark="备注", required=false, maxLength=32, fixed=0, order=7)
	@JSONField(name = "memo")
	public java.lang.String getMemo() {
		return getStr("Memo");
	}

	/**
	 * 创建人
	 */
	public M setCreatePerson(java.lang.String CreatePerson) {
		set("CreatePerson", CreatePerson);
		return (M)this;
	}

	/**
	 * 创建人
	 */
	@JBoltField(name="createperson" ,columnName="CreatePerson",type="String", remark="创建人", required=false, maxLength=30, fixed=0, order=8)
	@JSONField(name = "createperson")
	public java.lang.String getCreatePerson() {
		return getStr("CreatePerson");
	}

	/**
	 * 创建日期
	 */
	public M setCreateDate(java.util.Date CreateDate) {
		set("CreateDate", CreateDate);
		return (M)this;
	}

	/**
	 * 创建日期
	 */
	@JBoltField(name="createdate" ,columnName="CreateDate",type="Date", remark="创建日期", required=false, maxLength=23, fixed=3, order=9)
	@JSONField(name = "createdate")
	public java.util.Date getCreateDate() {
		return getDate("CreateDate");
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
	@JBoltField(name="auditperson" ,columnName="AuditPerson",type="String", remark="审核人", required=false, maxLength=30, fixed=0, order=10)
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
	@JBoltField(name="auditdate" ,columnName="AuditDate",type="Date", remark="审核日期", required=false, maxLength=23, fixed=3, order=11)
	@JSONField(name = "auditdate")
	public java.util.Date getAuditDate() {
		return getDate("AuditDate");
	}

	/**
	 * 修改人
	 */
	public M setModifyPerson(java.lang.String ModifyPerson) {
		set("ModifyPerson", ModifyPerson);
		return (M)this;
	}

	/**
	 * 修改人
	 */
	@JBoltField(name="modifyperson" ,columnName="ModifyPerson",type="String", remark="修改人", required=false, maxLength=30, fixed=0, order=12)
	@JSONField(name = "modifyperson")
	public java.lang.String getModifyPerson() {
		return getStr("ModifyPerson");
	}

	/**
	 * 修改时间
	 */
	public M setModifyDate(java.util.Date ModifyDate) {
		set("ModifyDate", ModifyDate);
		return (M)this;
	}

	/**
	 * 修改时间
	 */
	@JBoltField(name="modifydate" ,columnName="ModifyDate",type="Date", remark="修改时间", required=false, maxLength=23, fixed=3, order=13)
	@JSONField(name = "modifydate")
	public java.util.Date getModifyDate() {
		return getDate("ModifyDate");
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	public M setIsDeleted(java.lang.Boolean IsDeleted) {
		set("IsDeleted", IsDeleted);
		return (M)this;
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	@JBoltField(name="isdeleted" ,columnName="IsDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=false, maxLength=1, fixed=0, order=14)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("IsDeleted");
	}

	/**
	 * 仓库
	 */
	public M setRepository(java.lang.String repository) {
		set("repository", repository);
		return (M)this;
	}

	/**
	 * 仓库
	 */
	@JBoltField(name="repository" ,columnName="repository",type="String", remark="仓库", required=false, maxLength=30, fixed=0, order=15)
	@JSONField(name = "repository")
	public java.lang.String getRepository() {
		return getStr("repository");
	}

	/**
	 * 仓库名称
	 */
	public M setRepositoryName(java.lang.String repositoryName) {
		set("repositoryName", repositoryName);
		return (M)this;
	}

	/**
	 * 仓库名称
	 */
	@JBoltField(name="repositoryname" ,columnName="repositoryName",type="String", remark="仓库名称", required=false, maxLength=30, fixed=0, order=16)
	@JSONField(name = "repositoryname")
	public java.lang.String getRepositoryName() {
		return getStr("repositoryName");
	}

	/**
	 * 订单号
	 */
	public M setIndent(java.lang.String indent) {
		set("indent", indent);
		return (M)this;
	}

	/**
	 * 订单号
	 */
	@JBoltField(name="indent" ,columnName="indent",type="String", remark="订单号", required=false, maxLength=30, fixed=0, order=17)
	@JSONField(name = "indent")
	public java.lang.String getIndent() {
		return getStr("indent");
	}

	/**
	 * 生产部门
	 */
	public M setDept(java.lang.String dept) {
		set("dept", dept);
		return (M)this;
	}

	/**
	 * 生产部门
	 */
	@JBoltField(name="dept" ,columnName="dept",type="String", remark="生产部门", required=false, maxLength=30, fixed=0, order=18)
	@JSONField(name = "dept")
	public java.lang.String getDept() {
		return getStr("dept");
	}

	/**
	 * 生产部门名字
	 */
	public M setDeptName(java.lang.String deptName) {
		set("deptName", deptName);
		return (M)this;
	}

	/**
	 * 生产部门名字
	 */
	@JBoltField(name="deptname" ,columnName="deptName",type="String", remark="生产部门名字", required=false, maxLength=30, fixed=0, order=19)
	@JSONField(name = "deptname")
	public java.lang.String getDeptName() {
		return getStr("deptName");
	}

	/**
	 * 采购类型
	 */
	public M setProcureType(java.lang.String procureType) {
		set("procureType", procureType);
		return (M)this;
	}

	/**
	 * 采购类型
	 */
	@JBoltField(name="procuretype" ,columnName="procureType",type="String", remark="采购类型", required=false, maxLength=30, fixed=0, order=20)
	@JSONField(name = "procuretype")
	public java.lang.String getProcureType() {
		return getStr("procureType");
	}

	/**
	 * 入库类别
	 */
	public M setWarehousingType(java.lang.String warehousingType) {
		set("warehousingType", warehousingType);
		return (M)this;
	}

	/**
	 * 入库类别
	 */
	@JBoltField(name="warehousingtype" ,columnName="warehousingType",type="String", remark="入库类别", required=false, maxLength=30, fixed=0, order=21)
	@JSONField(name = "warehousingtype")
	public java.lang.String getWarehousingType() {
		return getStr("warehousingType");
	}

	/**
	 * 备注
	 */
	public M setRemark(java.lang.String remark) {
		set("remark", remark);
		return (M)this;
	}

	/**
	 * 备注
	 */
	@JBoltField(name="remark" ,columnName="remark",type="String", remark="备注", required=false, maxLength=255, fixed=0, order=22)
	@JSONField(name = "remark")
	public java.lang.String getRemark() {
		return getStr("remark");
	}

	/**
	 * 状态 1已保存 2待审批 3已审批 4审批不通过
	 */
	public M setState(java.lang.String state) {
		set("state", state);
		return (M)this;
	}

	/**
	 * 状态 1已保存 2待审批 3已审批 4审批不通过
	 */
	@JBoltField(name="state" ,columnName="state",type="String", remark="状态 1已保存 2待审批 3已审批 4审批不通过 ", required=false, maxLength=30, fixed=0, order=14)
	@JSONField(name = "state")
	public java.lang.String getState() {
		return getStr("state");
	}

}
