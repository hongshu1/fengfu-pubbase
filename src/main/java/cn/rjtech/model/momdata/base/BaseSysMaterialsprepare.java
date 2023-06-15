package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 材料备料表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseSysMaterialsprepare<M extends BaseSysMaterialsprepare<M>> extends JBoltBaseModel<M>{
    public static final String DATASOURCE_CONFIG_NAME = "momdata";
    /**AutoID*/
    public static final String AUTOID = "AutoID";
    /**组织代码*/
    public static final String ORGANIZECODE = "OrganizeCode";
    /**单号*/
    public static final String BILLNO = "BillNo";
    /**单据类型;MO 生产订单  SoDeliver销售发货单*/
    public static final String BILLTYPE = "BillType";
    /**单据日期*/
    public static final String BILLDATE = "BillDate";
    /***/
    public static final String SOURCEBILLNO = "SourceBillNo";
    /***/
    public static final String SOURCEBILLID = "SourceBillID";
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
    /**创建人id*/
    public static final String ICREATEBY = "icreateby";
    /**创建人名称*/
    public static final String CCREATENAME = "ccreatename";
    /**创建时间*/
    public static final String DCREATETIME = "dcreatetime";
    /**修改人id*/
    public static final String IUPDATEBY = "iupdateby";
    /**修改人名称*/
    public static final String CUPDATENAME = "cupdatename";
    /**修改时间*/
    public static final String DUPDATETIME = "dupdatetime";
    /**审核人id*/
    public static final String IAUDITBY = "iAuditby";
    /**审核名称*/
    public static final String CAUDITNAME = "cAuditname";
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
	 * 组织代码
	 */
	public M setOrganizeCode(java.lang.String OrganizeCode) {
		set("OrganizeCode", OrganizeCode);
		return (M)this;
	}

	/**
	 * 组织代码
	 */
	@JBoltField(name="organizecode" ,columnName="OrganizeCode",type="String", remark="组织代码", required=false, maxLength=30, fixed=0, order=2)
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
	@JBoltField(name="billno" ,columnName="BillNo",type="String", remark="单号", required=false, maxLength=30, fixed=0, order=3)
	@JSONField(name = "billno")
	public java.lang.String getBillNo() {
		return getStr("BillNo");
	}

	/**
	 * 单据类型;MO 生产订单  SoDeliver销售发货单
	 */
	public M setBillType(java.lang.String BillType) {
		set("BillType", BillType);
		return (M)this;
	}

	/**
	 * 单据类型;MO 生产订单  SoDeliver销售发货单
	 */
	@JBoltField(name="billtype" ,columnName="BillType",type="String", remark="单据类型;MO 生产订单  SoDeliver销售发货单", required=false, maxLength=30, fixed=0, order=4)
	@JSONField(name = "billtype")
	public java.lang.String getBillType() {
		return getStr("BillType");
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

	public M setSourceBillNo(java.lang.String SourceBillNo) {
		set("SourceBillNo", SourceBillNo);
		return (M)this;
	}

	@JBoltField(name="sourcebillno" ,columnName="SourceBillNo",type="String", remark="SOURCEBILLNO", required=false, maxLength=50, fixed=0, order=6)
	@JSONField(name = "sourcebillno")
	public java.lang.String getSourceBillNo() {
		return getStr("SourceBillNo");
	}

	public M setSourceBillID(java.lang.Long SourceBillID) {
		set("SourceBillID", SourceBillID);
		return (M)this;
	}

	@JBoltField(name="sourcebillid" ,columnName="SourceBillID",type="Long", remark="SOURCEBILLID", required=false, maxLength=19, fixed=0, order=7)
	@JSONField(name = "sourcebillid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getSourceBillID() {
		return getLong("SourceBillID");
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
	@JBoltField(name="iauditway" ,columnName="iAuditWay",type="Integer", remark="审批方式：1. 审核 2. 审批流", required=false, maxLength=10, fixed=0, order=8)
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
	@JBoltField(name="dsubmittime" ,columnName="dSubmitTime",type="Date", remark="提审时间", required=false, maxLength=23, fixed=3, order=9)
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
	@JBoltField(name="iauditstatus" ,columnName="iAuditStatus",type="Integer", remark="审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过", required=true, maxLength=10, fixed=0, order=10)
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
	@JBoltField(name="daudittime" ,columnName="dAuditTime",type="Date", remark="审核时间", required=false, maxLength=23, fixed=3, order=11)
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
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="是否删除：0. 否 1. 是", required=false, maxLength=1, fixed=0, order=12)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 创建人id
	 */
	public M setIcreateby(java.lang.Long icreateby) {
		set("icreateby", icreateby);
		return (M)this;
	}

	/**
	 * 创建人id
	 */
	@JBoltField(name="icreateby" ,columnName="icreateby",type="Long", remark="创建人id", required=false, maxLength=19, fixed=0, order=13)
	@JSONField(name = "icreateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIcreateby() {
		return getLong("icreateby");
	}

	/**
	 * 创建人名称
	 */
	public M setCcreatename(java.lang.String ccreatename) {
		set("ccreatename", ccreatename);
		return (M)this;
	}

	/**
	 * 创建人名称
	 */
	@JBoltField(name="ccreatename" ,columnName="ccreatename",type="String", remark="创建人名称", required=false, maxLength=30, fixed=0, order=14)
	@JSONField(name = "ccreatename")
	public java.lang.String getCcreatename() {
		return getStr("ccreatename");
	}

	/**
	 * 创建时间
	 */
	public M setDcreatetime(java.util.Date dcreatetime) {
		set("dcreatetime", dcreatetime);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="dcreatetime" ,columnName="dcreatetime",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=15)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDcreatetime() {
		return getDate("dcreatetime");
	}

	/**
	 * 修改人id
	 */
	public M setIupdateby(java.lang.Long iupdateby) {
		set("iupdateby", iupdateby);
		return (M)this;
	}

	/**
	 * 修改人id
	 */
	@JBoltField(name="iupdateby" ,columnName="iupdateby",type="Long", remark="修改人id", required=false, maxLength=19, fixed=0, order=16)
	@JSONField(name = "iupdateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIupdateby() {
		return getLong("iupdateby");
	}

	/**
	 * 修改人名称
	 */
	public M setCupdatename(java.lang.String cupdatename) {
		set("cupdatename", cupdatename);
		return (M)this;
	}

	/**
	 * 修改人名称
	 */
	@JBoltField(name="cupdatename" ,columnName="cupdatename",type="String", remark="修改人名称", required=false, maxLength=30, fixed=0, order=17)
	@JSONField(name = "cupdatename")
	public java.lang.String getCupdatename() {
		return getStr("cupdatename");
	}

	/**
	 * 修改时间
	 */
	public M setDupdatetime(java.util.Date dupdatetime) {
		set("dupdatetime", dupdatetime);
		return (M)this;
	}

	/**
	 * 修改时间
	 */
	@JBoltField(name="dupdatetime" ,columnName="dupdatetime",type="Date", remark="修改时间", required=false, maxLength=23, fixed=3, order=18)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDupdatetime() {
		return getDate("dupdatetime");
	}

	/**
	 * 审核人id
	 */
	public M setIAuditby(java.lang.Long iAuditby) {
		set("iAuditby", iAuditby);
		return (M)this;
	}

	/**
	 * 审核人id
	 */
	@JBoltField(name="iauditby" ,columnName="iAuditby",type="Long", remark="审核人id", required=false, maxLength=19, fixed=0, order=19)
	@JSONField(name = "iauditby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIAuditby() {
		return getLong("iAuditby");
	}

	/**
	 * 审核名称
	 */
	public M setCAuditname(java.lang.String cAuditname) {
		set("cAuditname", cAuditname);
		return (M)this;
	}

	/**
	 * 审核名称
	 */
	@JBoltField(name="cauditname" ,columnName="cAuditname",type="String", remark="审核名称", required=false, maxLength=30, fixed=0, order=20)
	@JSONField(name = "cauditname")
	public java.lang.String getCAuditname() {
		return getStr("cAuditname");
	}

}

