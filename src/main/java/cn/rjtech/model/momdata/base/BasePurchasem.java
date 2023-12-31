package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 申购单主表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BasePurchasem<M extends BasePurchasem<M>> extends JBoltBaseModel<M>{
    
    /**主键*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**单据号*/
    public static final String CPURCHASENO = "cPurchaseNo";
    /**业务类型*/
    public static final String CSERVICETYPENAME = "cServiceTypeName";
    /**日期*/
    public static final String CPURCHASEDATE = "cPurchaseDate";
    /**请购部门*/
    public static final String CDEPCODE = "cDepCode";
    /**请购人编码*/
    public static final String CPERSONCODE = "cPersonCode";
    /**采购类型*/
    public static final String IPURCHASETYPE = "iPurchaseType";
    /**是否经采购*/
    public static final String ISPURCHASED = "IsPurchased";
    /**追加的初始禀议书ID*/
    public static final String IFIRSTSOURCEPROPOSALID = "iFirstSourceProposalId";
    /**审核状态*/
    public static final String IAUDITSTATUS = "iAuditStatus";
    /**单据状态(1.未推送 2.已推送)*/
    public static final String ISTATUS = "iStatus";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**更新时间*/
    public static final String DUPDATETIME = "dUpdateTime";
    /**创建人*/
    public static final String ICREATEBY = "iCreateBy";
    /**更新人*/
    public static final String IUPDATEBY = "iUpdateBy";
    /**备注*/
    public static final String CMEMO = "cMemo";
    /**附件路径*/
    public static final String CACCESSORYPATH = "cAccessoryPath";
    /**申购金额(本币不含税)*/
    public static final String INATMONEY = "iNatMoney";
    /**参照类型:1.禀议书 2.预算*/
    public static final String IREFTYPE = "iRefType";
    /**审批方式：1. 审批状态 2. 审批流*/
    public static final String IAUDITWAY = "iAuditWay";
    /**提审时间*/
    public static final String DSUBMITTIME = "dSubmitTime";
    /**审核人ID，不走审批流时有该值*/
    public static final String IAUDITBY = "iAuditBy";
    /**审核人名称，不走审批流时有该值*/
    public static final String CAUDITNAME = "cAuditName";
    /**删除状态：0. 未删除 1. 已删除*/
    public static final String ISDELETED = "IsDeleted";
    /**审核时间*/
    public static final String DAUDITTIME = "dAuditTime";
	/**
	 * 主键
	 */
	public M setIAutoId(java.lang.Long iAutoId) {
		set("iAutoId", iAutoId);
		return (M)this;
	}

	/**
	 * 主键
	 */
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Long", remark="主键", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(name = "iautoid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIAutoId() {
		return getLong("iAutoId");
	}

	/**
	 * 组织ID
	 */
	public M setIOrgId(java.lang.Long iOrgId) {
		set("iOrgId", iOrgId);
		return (M)this;
	}

	/**
	 * 组织ID
	 */
	@JBoltField(name="iorgid" ,columnName="iOrgId",type="Long", remark="组织ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iorgid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIOrgId() {
		return getLong("iOrgId");
	}

	/**
	 * 组织编码
	 */
	public M setCOrgCode(java.lang.String cOrgCode) {
		set("cOrgCode", cOrgCode);
		return (M)this;
	}

	/**
	 * 组织编码
	 */
	@JBoltField(name="corgcode" ,columnName="cOrgCode",type="String", remark="组织编码", required=true, maxLength=20, fixed=0, order=3)
	@JSONField(name = "corgcode")
	public java.lang.String getCOrgCode() {
		return getStr("cOrgCode");
	}

	/**
	 * 单据号
	 */
	public M setCPurchaseNo(java.lang.String cPurchaseNo) {
		set("cPurchaseNo", cPurchaseNo);
		return (M)this;
	}

	/**
	 * 单据号
	 */
	@JBoltField(name="cpurchaseno" ,columnName="cPurchaseNo",type="String", remark="单据号", required=false, maxLength=50, fixed=0, order=4)
	@JSONField(name = "cpurchaseno")
	public java.lang.String getCPurchaseNo() {
		return getStr("cPurchaseNo");
	}

	/**
	 * 业务类型
	 */
	public M setCServiceTypeName(java.lang.String cServiceTypeName) {
		set("cServiceTypeName", cServiceTypeName);
		return (M)this;
	}

	/**
	 * 业务类型
	 */
	@JBoltField(name="cservicetypename" ,columnName="cServiceTypeName",type="String", remark="业务类型", required=true, maxLength=20, fixed=0, order=5)
	@JSONField(name = "cservicetypename")
	public java.lang.String getCServiceTypeName() {
		return getStr("cServiceTypeName");
	}

	/**
	 * 日期
	 */
	public M setCPurchaseDate(java.util.Date cPurchaseDate) {
		set("cPurchaseDate", cPurchaseDate);
		return (M)this;
	}

	/**
	 * 日期
	 */
	@JBoltField(name="cpurchasedate" ,columnName="cPurchaseDate",type="Date", remark="日期", required=true, maxLength=23, fixed=3, order=6)
	@JSONField(name = "cpurchasedate")
	public java.util.Date getCPurchaseDate() {
		return getDate("cPurchaseDate");
	}

	/**
	 * 请购部门
	 */
	public M setCDepCode(java.lang.String cDepCode) {
		set("cDepCode", cDepCode);
		return (M)this;
	}

	/**
	 * 请购部门
	 */
	@JBoltField(name="cdepcode" ,columnName="cDepCode",type="String", remark="请购部门", required=false, maxLength=20, fixed=0, order=7)
	@JSONField(name = "cdepcode")
	public java.lang.String getCDepCode() {
		return getStr("cDepCode");
	}

	/**
	 * 请购人编码
	 */
	public M setCPersonCode(java.lang.String cPersonCode) {
		set("cPersonCode", cPersonCode);
		return (M)this;
	}

	/**
	 * 请购人编码
	 */
	@JBoltField(name="cpersoncode" ,columnName="cPersonCode",type="String", remark="请购人编码", required=false, maxLength=20, fixed=0, order=8)
	@JSONField(name = "cpersoncode")
	public java.lang.String getCPersonCode() {
		return getStr("cPersonCode");
	}

	/**
	 * 采购类型
	 */
	public M setIPurchaseType(java.lang.String iPurchaseType) {
		set("iPurchaseType", iPurchaseType);
		return (M)this;
	}

	/**
	 * 采购类型
	 */
	@JBoltField(name="ipurchasetype" ,columnName="iPurchaseType",type="String", remark="采购类型", required=true, maxLength=2, fixed=0, order=9)
	@JSONField(name = "ipurchasetype")
	public java.lang.String getIPurchaseType() {
		return getStr("iPurchaseType");
	}

	/**
	 * 是否经采购
	 */
	public M setIsPurchased(java.lang.Boolean IsPurchased) {
		set("IsPurchased", IsPurchased);
		return (M)this;
	}

	/**
	 * 是否经采购
	 */
	@JBoltField(name="ispurchased" ,columnName="IsPurchased",type="Boolean", remark="是否经采购", required=true, maxLength=1, fixed=0, order=10)
	@JSONField(name = "ispurchased")
	public java.lang.Boolean getIsPurchased() {
		return getBoolean("IsPurchased");
	}

	/**
	 * 追加的初始禀议书ID
	 */
	public M setIFirstSourceProposalId(java.lang.Long iFirstSourceProposalId) {
		set("iFirstSourceProposalId", iFirstSourceProposalId);
		return (M)this;
	}

	/**
	 * 追加的初始禀议书ID
	 */
	@JBoltField(name="ifirstsourceproposalid" ,columnName="iFirstSourceProposalId",type="Long", remark="追加的初始禀议书ID", required=false, maxLength=19, fixed=0, order=11)
	@JSONField(name = "ifirstsourceproposalid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIFirstSourceProposalId() {
		return getLong("iFirstSourceProposalId");
	}

	/**
	 * 审核状态
	 */
	public M setIAuditStatus(java.lang.Integer iAuditStatus) {
		set("iAuditStatus", iAuditStatus);
		return (M)this;
	}

	/**
	 * 审核状态
	 */
	@JBoltField(name="iauditstatus" ,columnName="iAuditStatus",type="Integer", remark="审核状态", required=true, maxLength=10, fixed=0, order=12)
	@JSONField(name = "iauditstatus")
	public java.lang.Integer getIAuditStatus() {
		return getInt("iAuditStatus");
	}

	/**
	 * 单据状态(1.未推送 2.已推送)
	 */
	public M setIStatus(java.lang.Integer iStatus) {
		set("iStatus", iStatus);
		return (M)this;
	}

	/**
	 * 单据状态(1.未推送 2.已推送)
	 */
	@JBoltField(name="istatus" ,columnName="iStatus",type="Integer", remark="单据状态(1.未推送 2.已推送)", required=true, maxLength=10, fixed=0, order=13)
	@JSONField(name = "istatus")
	public java.lang.Integer getIStatus() {
		return getInt("iStatus");
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=14)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDCreateTime() {
		return getDate("dCreateTime");
	}

	/**
	 * 更新时间
	 */
	public M setDUpdateTime(java.util.Date dUpdateTime) {
		set("dUpdateTime", dUpdateTime);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=15)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
	}

	/**
	 * 创建人
	 */
	public M setICreateBy(java.lang.Long iCreateBy) {
		set("iCreateBy", iCreateBy);
		return (M)this;
	}

	/**
	 * 创建人
	 */
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=16)
	@JSONField(name = "icreateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICreateBy() {
		return getLong("iCreateBy");
	}

	/**
	 * 更新人
	 */
	public M setIUpdateBy(java.lang.Long iUpdateBy) {
		set("iUpdateBy", iUpdateBy);
		return (M)this;
	}

	/**
	 * 更新人
	 */
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人", required=false, maxLength=19, fixed=0, order=17)
	@JSONField(name = "iupdateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUpdateBy() {
		return getLong("iUpdateBy");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=255, fixed=0, order=18)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
	}

	/**
	 * 附件路径
	 */
	public M setCAccessoryPath(java.lang.String cAccessoryPath) {
		set("cAccessoryPath", cAccessoryPath);
		return (M)this;
	}

	/**
	 * 附件路径
	 */
	@JBoltField(name="caccessorypath" ,columnName="cAccessoryPath",type="String", remark="附件路径", required=false, maxLength=255, fixed=0, order=19)
	@JSONField(name = "caccessorypath")
	public java.lang.String getCAccessoryPath() {
		return getStr("cAccessoryPath");
	}

	/**
	 * 申购金额(本币不含税)
	 */
	public M setINatMoney(java.math.BigDecimal iNatMoney) {
		set("iNatMoney", iNatMoney);
		return (M)this;
	}

	/**
	 * 申购金额(本币不含税)
	 */
	@JBoltField(name="inatmoney" ,columnName="iNatMoney",type="BigDecimal", remark="申购金额(本币不含税)", required=true, maxLength=20, fixed=2, order=20)
	@JSONField(name = "inatmoney")
	public java.math.BigDecimal getINatMoney() {
		return getBigDecimal("iNatMoney");
	}

	/**
	 * 参照类型:1.禀议书 2.预算
	 */
	public M setIRefType(java.lang.Integer iRefType) {
		set("iRefType", iRefType);
		return (M)this;
	}

	/**
	 * 参照类型:1.禀议书 2.预算
	 */
	@JBoltField(name="ireftype" ,columnName="iRefType",type="Integer", remark="参照类型:1.禀议书 2.预算", required=true, maxLength=10, fixed=0, order=21)
	@JSONField(name = "ireftype")
	public java.lang.Integer getIRefType() {
		return getInt("iRefType");
	}

	/**
	 * 审批方式：1. 审批状态 2. 审批流
	 */
	public M setIAuditWay(java.lang.Integer iAuditWay) {
		set("iAuditWay", iAuditWay);
		return (M)this;
	}

	/**
	 * 审批方式：1. 审批状态 2. 审批流
	 */
	@JBoltField(name="iauditway" ,columnName="iAuditWay",type="Integer", remark="审批方式：1. 审批状态 2. 审批流", required=false, maxLength=10, fixed=0, order=22)
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
	@JBoltField(name="dsubmittime" ,columnName="dSubmitTime",type="Date", remark="提审时间", required=false, maxLength=23, fixed=3, order=23)
	@JSONField(name = "dsubmittime")
	public java.util.Date getDSubmitTime() {
		return getDate("dSubmitTime");
	}

	/**
	 * 审核人ID，不走审批流时有该值
	 */
	public M setIAuditBy(java.lang.Long iAuditBy) {
		set("iAuditBy", iAuditBy);
		return (M)this;
	}

	/**
	 * 审核人ID，不走审批流时有该值
	 */
	@JBoltField(name="iauditby" ,columnName="iAuditBy",type="Long", remark="审核人ID，不走审批流时有该值", required=false, maxLength=19, fixed=0, order=24)
	@JSONField(name = "iauditby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIAuditBy() {
		return getLong("iAuditBy");
	}

	/**
	 * 审核人名称，不走审批流时有该值
	 */
	public M setCAuditName(java.util.Date cAuditName) {
		set("cAuditName", cAuditName);
		return (M)this;
	}

	/**
	 * 审核人名称，不走审批流时有该值
	 */
	@JBoltField(name="cauditname" ,columnName="cAuditName",type="Date", remark="审核人名称，不走审批流时有该值", required=false, maxLength=23, fixed=3, order=25)
	@JSONField(name = "cauditname")
	public java.util.Date getCAuditName() {
		return getDate("cAuditName");
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
	@JBoltField(name="isdeleted" ,columnName="IsDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=26)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("IsDeleted");
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
	@JBoltField(name="daudittime" ,columnName="dAuditTime",type="Date", remark="审核时间", required=false, maxLength=23, fixed=3, order=27)
	@JSONField(name = "daudittime")
	public java.util.Date getDAuditTime() {
		return getDate("dAuditTime");
	}

}

