package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 客户订单-年度订单
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseAnnualOrderM<M extends BaseAnnualOrderM<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**订单号*/
    public static final String CORDERNO = "cOrderNo";
    /**客户ID*/
    public static final String ICUSTOMERID = "iCustomerId";
    /**年份*/
    public static final String IYEAR = "iYear";
    /**订单状态：1. 已保存 2. 待审核 3. 已审批*/
    public static final String IORDERSTATUS = "iOrderStatus";
    /**审核状态：0. 未审核 1.待审核 2. 审核通过 3. 审核不通过*/
    public static final String IAUDITSTATUS = "iAuditStatus";
    /**审核时间，不走审批流时有该值*/
    public static final String DAUDITTIME = "dAuditTime";
    /**审核人ID，不走审批流时有该值*/
    public static final String IAUDITBY = "iAuditBy";
    /**审核人名称，不走审批流时有该值*/
    public static final String CAUDITNAME = "cAuditName";
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
    public static final String ISDELETED = "isDeleted";
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
	 * 订单号
	 */
	public M setCOrderNo(java.lang.Boolean cOrderNo) {
		set("cOrderNo", cOrderNo);
		return (M)this;
	}

	/**
	 * 订单号
	 */
	@JBoltField(name="corderno" ,columnName="cOrderNo",type="Boolean", remark="订单号", required=true, maxLength=1, fixed=0, order=5)
	@JSONField(name = "corderno")
	public java.lang.Boolean getCOrderNo() {
		return getBoolean("cOrderNo");
	}

	/**
	 * 客户ID
	 */
	public M setICustomerId(java.lang.Long iCustomerId) {
		set("iCustomerId", iCustomerId);
		return (M)this;
	}

	/**
	 * 客户ID
	 */
	@JBoltField(name="icustomerid" ,columnName="iCustomerId",type="Long", remark="客户ID", required=true, maxLength=19, fixed=0, order=6)
	@JSONField(name = "icustomerid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICustomerId() {
		return getLong("iCustomerId");
	}

	/**
	 * 年份
	 */
	public M setIYear(java.lang.Integer iYear) {
		set("iYear", iYear);
		return (M)this;
	}

	/**
	 * 年份
	 */
	@JBoltField(name="iyear" ,columnName="iYear",type="Integer", remark="年份", required=true, maxLength=10, fixed=0, order=7)
	@JSONField(name = "iyear")
	public java.lang.Integer getIYear() {
		return getInt("iYear");
	}

	/**
	 * 订单状态：1. 已保存 2. 待审核 3. 已审批
	 */
	public M setIOrderStatus(java.lang.Integer iOrderStatus) {
		set("iOrderStatus", iOrderStatus);
		return (M)this;
	}

	/**
	 * 订单状态：1. 已保存 2. 待审核 3. 已审批
	 */
	@JBoltField(name="iorderstatus" ,columnName="iOrderStatus",type="Integer", remark="订单状态：1. 已保存 2. 待审核 3. 已审批", required=true, maxLength=10, fixed=0, order=8)
	@JSONField(name = "iorderstatus")
	public java.lang.Integer getIOrderStatus() {
		return getInt("iOrderStatus");
	}

	/**
	 * 审核状态：0. 未审核 1.待审核 2. 审核通过 3. 审核不通过
	 */
	public M setIAuditStatus(java.lang.Integer iAuditStatus) {
		set("iAuditStatus", iAuditStatus);
		return (M)this;
	}

	/**
	 * 审核状态：0. 未审核 1.待审核 2. 审核通过 3. 审核不通过
	 */
	@JBoltField(name="iauditstatus" ,columnName="iAuditStatus",type="Integer", remark="审核状态：0. 未审核 1.待审核 2. 审核通过 3. 审核不通过", required=true, maxLength=10, fixed=0, order=9)
	@JSONField(name = "iauditstatus")
	public java.lang.Integer getIAuditStatus() {
		return getInt("iAuditStatus");
	}

	/**
	 * 审核时间，不走审批流时有该值
	 */
	public M setDAuditTime(java.util.Date dAuditTime) {
		set("dAuditTime", dAuditTime);
		return (M)this;
	}

	/**
	 * 审核时间，不走审批流时有该值
	 */
	@JBoltField(name="daudittime" ,columnName="dAuditTime",type="Date", remark="审核时间，不走审批流时有该值", required=false, maxLength=23, fixed=3, order=10)
	@JSONField(name = "daudittime")
	public java.util.Date getDAuditTime() {
		return getDate("dAuditTime");
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
	@JBoltField(name="iauditby" ,columnName="iAuditBy",type="Long", remark="审核人ID，不走审批流时有该值", required=false, maxLength=19, fixed=0, order=11)
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
	@JBoltField(name="cauditname" ,columnName="cAuditName",type="Date", remark="审核人名称，不走审批流时有该值", required=false, maxLength=23, fixed=3, order=12)
	@JSONField(name = "cauditname")
	public java.util.Date getCAuditName() {
		return getDate("cAuditName");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=13)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=14)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=15)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=16)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=50, fixed=0, order=17)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=18)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	public M setIsDeleted(java.lang.Boolean isDeleted) {
		set("isDeleted", isDeleted);
		return (M)this;
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=19)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

}

