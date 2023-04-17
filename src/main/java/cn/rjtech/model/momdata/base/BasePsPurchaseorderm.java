package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 采购/委外订单-采购订单主表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BasePsPurchaseorderm<M extends BasePsPurchaseorderm<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**订单编号*/
    public static final String CORDERNO = "cOrderNo";
    /**订单日期*/
    public static final String DORDERDATE = "dOrderDate";
    /**业务类型： 1. 普通采购*/
    public static final String IBUSTYPE = "iBusType";
    /**采购类型*/
    public static final String IPURCHASETYPE = "iPurchaseType";
    /**供应商ID*/
    public static final String IVENDORID = "iVendorId";
    /**部门ID*/
    public static final String IDEPARTMENTID = "iDepartmentId";
    /**业务员ID，系统用户ID*/
    public static final String IDUTYUSERID = "iDutyUserId";
    /**税率*/
    public static final String ITAXRATE = "iTaxRate";
    /**应付类型：1. 材料费*/
    public static final String IPAYABLETYPE = "iPayableType";
    /**币种*/
    public static final String CCURRENCY = "cCurrency";
    /**汇率*/
    public static final String IEXCHANGERATE = "iExchangeRate";
    /**订单状态：1. 已保存 2. 待审批 3. 已审核 4. 审批不通过 5. 已生成现品票 6. 已关闭*/
    public static final String IORDERSTATUS = "iOrderStatus";
    /**审核状态：1. 审核中 2. 审核通过 3. 审核不通过*/
    public static final String IAUDITSTATUS = "iAuditStatus";
    /**审核时间*/
    public static final String DAUDITTIME = "dAuditTime";
    /**备注*/
    public static final String CMEMO = "cMemo";
    /**创建人ID*/
    public static final String ICREATEBY = "iCreateBy";
    /**创建人名称*/
    public static final String CCREATENAME = "cCreateName";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**更新人ID*/
    public static final String IUPDATEBY = "iUpdateBy";
    /**更新人名称*/
    public static final String CUPDATENAME = "cUpdateName";
    /**更新时间*/
    public static final String DUPDATETIME = "dUpdateTime";
    /**删除状态：0. 未删除 1. 已删除*/
    public static final String ISDELETED = "IsDeleted";
    /**已失效隐藏：0. 否 1. 是*/
    public static final String HIDEINVALID = "hideInvalid";
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
	@JBoltField(name="corgcode" ,columnName="cOrgCode",type="String", remark="组织编码", required=true, maxLength=40, fixed=0, order=3)
	@JSONField(name = "corgcode")
	public java.lang.String getCOrgCode() {
		return getStr("cOrgCode");
	}

	/**
	 * 组织名称
	 */
	public M setCOrgName(java.lang.String cOrgName) {
		set("cOrgName", cOrgName);
		return (M)this;
	}

	/**
	 * 组织名称
	 */
	@JBoltField(name="corgname" ,columnName="cOrgName",type="String", remark="组织名称", required=true, maxLength=50, fixed=0, order=4)
	@JSONField(name = "corgname")
	public java.lang.String getCOrgName() {
		return getStr("cOrgName");
	}

	/**
	 * 订单编号
	 */
	public M setCOrderNo(java.lang.String cOrderNo) {
		set("cOrderNo", cOrderNo);
		return (M)this;
	}

	/**
	 * 订单编号
	 */
	@JBoltField(name="corderno" ,columnName="cOrderNo",type="String", remark="订单编号", required=true, maxLength=50, fixed=0, order=5)
	@JSONField(name = "corderno")
	public java.lang.String getCOrderNo() {
		return getStr("cOrderNo");
	}

	/**
	 * 订单日期
	 */
	public M setDOrderDate(java.util.Date dOrderDate) {
		set("dOrderDate", dOrderDate);
		return (M)this;
	}

	/**
	 * 订单日期
	 */
	@JBoltField(name="dorderdate" ,columnName="dOrderDate",type="Date", remark="订单日期", required=true, maxLength=10, fixed=0, order=6)
	@JSONField(name = "dorderdate")
	public java.util.Date getDOrderDate() {
		return getDate("dOrderDate");
	}

	/**
	 * 业务类型： 1. 普通采购
	 */
	public M setIBusType(java.lang.Integer iBusType) {
		set("iBusType", iBusType);
		return (M)this;
	}

	/**
	 * 业务类型： 1. 普通采购
	 */
	@JBoltField(name="ibustype" ,columnName="iBusType",type="Integer", remark="业务类型： 1. 普通采购", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "ibustype")
	public java.lang.Integer getIBusType() {
		return getInt("iBusType");
	}

	/**
	 * 采购类型
	 */
	public M setIPurchaseType(java.lang.Integer iPurchaseType) {
		set("iPurchaseType", iPurchaseType);
		return (M)this;
	}

	/**
	 * 采购类型
	 */
	@JBoltField(name="ipurchasetype" ,columnName="iPurchaseType",type="Integer", remark="采购类型", required=true, maxLength=10, fixed=0, order=8)
	@JSONField(name = "ipurchasetype")
	public java.lang.Integer getIPurchaseType() {
		return getInt("iPurchaseType");
	}

	/**
	 * 供应商ID
	 */
	public M setIVendorId(java.lang.Long iVendorId) {
		set("iVendorId", iVendorId);
		return (M)this;
	}

	/**
	 * 供应商ID
	 */
	@JBoltField(name="ivendorid" ,columnName="iVendorId",type="Long", remark="供应商ID", required=true, maxLength=19, fixed=0, order=9)
	@JSONField(name = "ivendorid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIVendorId() {
		return getLong("iVendorId");
	}

	/**
	 * 部门ID
	 */
	public M setIDepartmentId(java.lang.Long iDepartmentId) {
		set("iDepartmentId", iDepartmentId);
		return (M)this;
	}

	/**
	 * 部门ID
	 */
	@JBoltField(name="idepartmentid" ,columnName="iDepartmentId",type="Long", remark="部门ID", required=true, maxLength=19, fixed=0, order=10)
	@JSONField(name = "idepartmentid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIDepartmentId() {
		return getLong("iDepartmentId");
	}

	/**
	 * 业务员ID，系统用户ID
	 */
	public M setIDutyUserId(java.lang.Long iDutyUserId) {
		set("iDutyUserId", iDutyUserId);
		return (M)this;
	}

	/**
	 * 业务员ID，系统用户ID
	 */
	@JBoltField(name="idutyuserid" ,columnName="iDutyUserId",type="Long", remark="业务员ID，系统用户ID", required=false, maxLength=19, fixed=0, order=11)
	@JSONField(name = "idutyuserid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIDutyUserId() {
		return getLong("iDutyUserId");
	}

	/**
	 * 税率
	 */
	public M setITaxRate(java.math.BigDecimal iTaxRate) {
		set("iTaxRate", iTaxRate);
		return (M)this;
	}

	/**
	 * 税率
	 */
	@JBoltField(name="itaxrate" ,columnName="iTaxRate",type="BigDecimal", remark="税率", required=false, maxLength=5, fixed=4, order=12)
	@JSONField(name = "itaxrate")
	public java.math.BigDecimal getITaxRate() {
		return getBigDecimal("iTaxRate");
	}

	/**
	 * 应付类型：1. 材料费
	 */
	public M setIPayableType(java.lang.Integer iPayableType) {
		set("iPayableType", iPayableType);
		return (M)this;
	}

	/**
	 * 应付类型：1. 材料费
	 */
	@JBoltField(name="ipayabletype" ,columnName="iPayableType",type="Integer", remark="应付类型：1. 材料费", required=true, maxLength=10, fixed=0, order=13)
	@JSONField(name = "ipayabletype")
	public java.lang.Integer getIPayableType() {
		return getInt("iPayableType");
	}

	/**
	 * 币种
	 */
	public M setCCurrency(java.lang.String cCurrency) {
		set("cCurrency", cCurrency);
		return (M)this;
	}

	/**
	 * 币种
	 */
	@JBoltField(name="ccurrency" ,columnName="cCurrency",type="String", remark="币种", required=false, maxLength=10, fixed=0, order=14)
	@JSONField(name = "ccurrency")
	public java.lang.String getCCurrency() {
		return getStr("cCurrency");
	}

	/**
	 * 汇率
	 */
	public M setIExchangeRate(java.math.BigDecimal iExchangeRate) {
		set("iExchangeRate", iExchangeRate);
		return (M)this;
	}

	/**
	 * 汇率
	 */
	@JBoltField(name="iexchangerate" ,columnName="iExchangeRate",type="BigDecimal", remark="汇率", required=false, maxLength=10, fixed=6, order=15)
	@JSONField(name = "iexchangerate")
	public java.math.BigDecimal getIExchangeRate() {
		return getBigDecimal("iExchangeRate");
	}

	/**
	 * 订单状态：1. 已保存 2. 待审批 3. 已审核 4. 审批不通过 5. 已生成现品票 6. 已关闭
	 */
	public M setIOrderStatus(java.lang.Integer iOrderStatus) {
		set("iOrderStatus", iOrderStatus);
		return (M)this;
	}

	/**
	 * 订单状态：1. 已保存 2. 待审批 3. 已审核 4. 审批不通过 5. 已生成现品票 6. 已关闭
	 */
	@JBoltField(name="iorderstatus" ,columnName="iOrderStatus",type="Integer", remark="订单状态：1. 已保存 2. 待审批 3. 已审核 4. 审批不通过 5. 已生成现品票 6. 已关闭", required=true, maxLength=10, fixed=0, order=16)
	@JSONField(name = "iorderstatus")
	public java.lang.Integer getIOrderStatus() {
		return getInt("iOrderStatus");
	}

	/**
	 * 审核状态：1. 审核中 2. 审核通过 3. 审核不通过
	 */
	public M setIAuditStatus(java.lang.Integer iAuditStatus) {
		set("iAuditStatus", iAuditStatus);
		return (M)this;
	}

	/**
	 * 审核状态：1. 审核中 2. 审核通过 3. 审核不通过
	 */
	@JBoltField(name="iauditstatus" ,columnName="iAuditStatus",type="Integer", remark="审核状态：1. 审核中 2. 审核通过 3. 审核不通过", required=true, maxLength=10, fixed=0, order=17)
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
	@JBoltField(name="daudittime" ,columnName="dAuditTime",type="Date", remark="审核时间", required=false, maxLength=23, fixed=3, order=18)
	@JSONField(name = "daudittime")
	public java.util.Date getDAuditTime() {
		return getDate("dAuditTime");
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
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=19)
	@JSONField(name = "cmemo")
	public java.lang.String getCMemo() {
		return getStr("cMemo");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=20)
	@JSONField(name = "icreateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICreateBy() {
		return getLong("iCreateBy");
	}

	/**
	 * 创建人名称
	 */
	public M setCCreateName(java.lang.String cCreateName) {
		set("cCreateName", cCreateName);
		return (M)this;
	}

	/**
	 * 创建人名称
	 */
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=21)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=22)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDCreateTime() {
		return getDate("dCreateTime");
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=23)
	@JSONField(name = "iupdateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUpdateBy() {
		return getLong("iUpdateBy");
	}

	/**
	 * 更新人名称
	 */
	public M setCUpdateName(java.lang.String cUpdateName) {
		set("cUpdateName", cUpdateName);
		return (M)this;
	}

	/**
	 * 更新人名称
	 */
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=50, fixed=0, order=24)
	@JSONField(name = "cupdatename")
	public java.lang.String getCUpdateName() {
		return getStr("cUpdateName");
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=25)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
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
	 * 已失效隐藏：0. 否 1. 是
	 */
	public M setHideInvalid(java.lang.Boolean hideInvalid) {
		set("hideInvalid", hideInvalid);
		return (M)this;
	}

	/**
	 * 已失效隐藏：0. 否 1. 是
	 */
	@JBoltField(name="hideinvalid" ,columnName="hideInvalid",type="Boolean", remark="已失效隐藏：0. 否 1. 是", required=true, maxLength=1, fixed=0, order=27)
	@JSONField(name = "hideinvalid")
	public java.lang.Boolean getHideInvalid() {
		return getBoolean("hideInvalid");
	}

}

