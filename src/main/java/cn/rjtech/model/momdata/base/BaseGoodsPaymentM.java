package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 货款核对表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseGoodsPaymentM<M extends BaseGoodsPaymentM<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**货款核对单号*/
    public static final String CGOODSPAYMENTNO = "cGoodsPaymentNo";
    /**客户ID*/
    public static final String ICUSTOMERID = "iCustomerId";
    /**客户编码*/
    public static final String CCUSTOMERCODE = "cCustomerCode";
    /**客户名称*/
    public static final String CCUSTOMERNAME = "cCustomerName";
    /**状态;1.已保存 2.待审核 3.已审批 4. 审批不通过*/
    public static final String ISTATUS = "iStatus";
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
    /**删除状态*/
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
	@JBoltField(name="iorgid" ,columnName="iOrgId",type="Long", remark="组织ID", required=false, maxLength=19, fixed=0, order=2)
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
	@JBoltField(name="corgcode" ,columnName="cOrgCode",type="String", remark="组织编码", required=false, maxLength=40, fixed=0, order=3)
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
	@JBoltField(name="corgname" ,columnName="cOrgName",type="String", remark="组织名称", required=false, maxLength=50, fixed=0, order=4)
	@JSONField(name = "corgname")
	public java.lang.String getCOrgName() {
		return getStr("cOrgName");
	}

	/**
	 * 货款核对单号
	 */
	public M setCGoodsPaymentNo(java.lang.String cGoodsPaymentNo) {
		set("cGoodsPaymentNo", cGoodsPaymentNo);
		return (M)this;
	}

	/**
	 * 货款核对单号
	 */
	@JBoltField(name="cgoodspaymentno" ,columnName="cGoodsPaymentNo",type="String", remark="货款核对单号", required=false, maxLength=255, fixed=0, order=5)
	@JSONField(name = "cgoodspaymentno")
	public java.lang.String getCGoodsPaymentNo() {
		return getStr("cGoodsPaymentNo");
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
	@JBoltField(name="icustomerid" ,columnName="iCustomerId",type="Long", remark="客户ID", required=false, maxLength=19, fixed=0, order=6)
	@JSONField(name = "icustomerid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICustomerId() {
		return getLong("iCustomerId");
	}

	/**
	 * 客户编码
	 */
	public M setCCustomerCode(java.lang.String cCustomerCode) {
		set("cCustomerCode", cCustomerCode);
		return (M)this;
	}

	/**
	 * 客户编码
	 */
	@JBoltField(name="ccustomercode" ,columnName="cCustomerCode",type="String", remark="客户编码", required=false, maxLength=40, fixed=0, order=7)
	@JSONField(name = "ccustomercode")
	public java.lang.String getCCustomerCode() {
		return getStr("cCustomerCode");
	}

	/**
	 * 客户名称
	 */
	public M setCCustomerName(java.lang.String cCustomerName) {
		set("cCustomerName", cCustomerName);
		return (M)this;
	}

	/**
	 * 客户名称
	 */
	@JBoltField(name="ccustomername" ,columnName="cCustomerName",type="String", remark="客户名称", required=false, maxLength=60, fixed=0, order=8)
	@JSONField(name = "ccustomername")
	public java.lang.String getCCustomerName() {
		return getStr("cCustomerName");
	}

	/**
	 * 状态;1.已保存 2.待审核 3.已审批 4. 审批不通过
	 */
	public M setIStatus(java.lang.Integer iStatus) {
		set("iStatus", iStatus);
		return (M)this;
	}

	/**
	 * 状态;1.已保存 2.待审核 3.已审批 4. 审批不通过
	 */
	@JBoltField(name="istatus" ,columnName="iStatus",type="Integer", remark="状态;1.已保存 2.待审核 3.已审批 4. 审批不通过", required=false, maxLength=10, fixed=0, order=9)
	@JSONField(name = "istatus")
	public java.lang.Integer getIStatus() {
		return getInt("iStatus");
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=false, maxLength=19, fixed=0, order=10)
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
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=false, maxLength=60, fixed=0, order=11)
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=12)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=false, maxLength=19, fixed=0, order=13)
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
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=false, maxLength=60, fixed=0, order=14)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=15)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
	}

	/**
	 * 删除状态
	 */
	public M setIsDeleted(java.lang.Boolean isDeleted) {
		set("isDeleted", isDeleted);
		return (M)this;
	}

	/**
	 * 删除状态
	 */
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态", required=false, maxLength=1, fixed=0, order=16)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

}
