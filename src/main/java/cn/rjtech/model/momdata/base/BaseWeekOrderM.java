package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 客户订单-周间客户订单
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseWeekOrderM<M extends BaseWeekOrderM<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**客户ID*/
    public static final String ICUSTOMERID = "iCustomerId";
    /**订单状态：1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已发货 6. 已核对 7. 已关闭*/
    public static final String IORDERSTATUS = "iOrderStatus";
    /**审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过*/
    public static final String IAUDITSTATUS = "iAuditStatus";
    /**审核时间*/
    public static final String DAUDITTIME = "dAuditTime";
    /**订单号*/
    public static final String CORDERNO = "cOrderNo";
    /**扩展字段1*/
    public static final String CDEFINE1 = "cDefine1";
    /**扩展字段2*/
    public static final String CDEFINE2 = "cDefine2";
    /**扩展字段3*/
    public static final String CDEFINE3 = "cDefine3";
    /**扩展字段4*/
    public static final String CDEFINE4 = "cDefine4";
    /**扩展字段5*/
    public static final String CDEFINE5 = "cDefine5";
    /**扩展字段6*/
    public static final String CDEFINE6 = "cDefine6";
    /**扩展字段7*/
    public static final String CDEFINE7 = "cDefine7";
    /**扩展字段8*/
    public static final String CDEFINE8 = "cDefine8";
    /**扩展字段9*/
    public static final String CDEFINE9 = "cDefine9";
    /**扩展字段10*/
    public static final String CDEFINE10 = "cDefine10";
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
	 * 客户ID
	 */
	public M setICustomerId(java.lang.Long iCustomerId) {
		set("iCustomerId", iCustomerId);
		return (M)this;
	}

	/**
	 * 客户ID
	 */
	@JBoltField(name="icustomerid" ,columnName="iCustomerId",type="Long", remark="客户ID", required=true, maxLength=19, fixed=0, order=5)
	@JSONField(name = "icustomerid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICustomerId() {
		return getLong("iCustomerId");
	}

	/**
	 * 订单状态：1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已发货 6. 已核对 7. 已关闭
	 */
	public M setIOrderStatus(java.lang.Integer iOrderStatus) {
		set("iOrderStatus", iOrderStatus);
		return (M)this;
	}

	/**
	 * 订单状态：1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已发货 6. 已核对 7. 已关闭
	 */
	@JBoltField(name="iorderstatus" ,columnName="iOrderStatus",type="Integer", remark="订单状态：1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已发货 6. 已核对 7. 已关闭", required=true, maxLength=10, fixed=0, order=6)
	@JSONField(name = "iorderstatus")
	public java.lang.Integer getIOrderStatus() {
		return getInt("iOrderStatus");
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
	@JBoltField(name="iauditstatus" ,columnName="iAuditStatus",type="Integer", remark="审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过", required=true, maxLength=10, fixed=0, order=7)
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
	@JBoltField(name="daudittime" ,columnName="dAuditTime",type="Date", remark="审核时间", required=false, maxLength=23, fixed=3, order=8)
	@JSONField(name = "daudittime")
	public java.util.Date getDAuditTime() {
		return getDate("dAuditTime");
	}

	/**
	 * 订单号
	 */
	public M setCOrderNo(java.lang.String cOrderNo) {
		set("cOrderNo", cOrderNo);
		return (M)this;
	}

	/**
	 * 订单号
	 */
	@JBoltField(name="corderno" ,columnName="cOrderNo",type="String", remark="订单号", required=true, maxLength=40, fixed=0, order=9)
	@JSONField(name = "corderno")
	public java.lang.String getCOrderNo() {
		return getStr("cOrderNo");
	}

	/**
	 * 扩展字段1
	 */
	public M setCDefine1(java.lang.String cDefine1) {
		set("cDefine1", cDefine1);
		return (M)this;
	}

	/**
	 * 扩展字段1
	 */
	@JBoltField(name="cdefine1" ,columnName="cDefine1",type="String", remark="扩展字段1", required=false, maxLength=200, fixed=0, order=10)
	@JSONField(name = "cdefine1")
	public java.lang.String getCDefine1() {
		return getStr("cDefine1");
	}

	/**
	 * 扩展字段2
	 */
	public M setCDefine2(java.lang.String cDefine2) {
		set("cDefine2", cDefine2);
		return (M)this;
	}

	/**
	 * 扩展字段2
	 */
	@JBoltField(name="cdefine2" ,columnName="cDefine2",type="String", remark="扩展字段2", required=false, maxLength=200, fixed=0, order=11)
	@JSONField(name = "cdefine2")
	public java.lang.String getCDefine2() {
		return getStr("cDefine2");
	}

	/**
	 * 扩展字段3
	 */
	public M setCDefine3(java.lang.String cDefine3) {
		set("cDefine3", cDefine3);
		return (M)this;
	}

	/**
	 * 扩展字段3
	 */
	@JBoltField(name="cdefine3" ,columnName="cDefine3",type="String", remark="扩展字段3", required=false, maxLength=200, fixed=0, order=12)
	@JSONField(name = "cdefine3")
	public java.lang.String getCDefine3() {
		return getStr("cDefine3");
	}

	/**
	 * 扩展字段4
	 */
	public M setCDefine4(java.lang.String cDefine4) {
		set("cDefine4", cDefine4);
		return (M)this;
	}

	/**
	 * 扩展字段4
	 */
	@JBoltField(name="cdefine4" ,columnName="cDefine4",type="String", remark="扩展字段4", required=false, maxLength=200, fixed=0, order=13)
	@JSONField(name = "cdefine4")
	public java.lang.String getCDefine4() {
		return getStr("cDefine4");
	}

	/**
	 * 扩展字段5
	 */
	public M setCDefine5(java.lang.String cDefine5) {
		set("cDefine5", cDefine5);
		return (M)this;
	}

	/**
	 * 扩展字段5
	 */
	@JBoltField(name="cdefine5" ,columnName="cDefine5",type="String", remark="扩展字段5", required=false, maxLength=200, fixed=0, order=14)
	@JSONField(name = "cdefine5")
	public java.lang.String getCDefine5() {
		return getStr("cDefine5");
	}

	/**
	 * 扩展字段6
	 */
	public M setCDefine6(java.lang.String cDefine6) {
		set("cDefine6", cDefine6);
		return (M)this;
	}

	/**
	 * 扩展字段6
	 */
	@JBoltField(name="cdefine6" ,columnName="cDefine6",type="String", remark="扩展字段6", required=false, maxLength=200, fixed=0, order=15)
	@JSONField(name = "cdefine6")
	public java.lang.String getCDefine6() {
		return getStr("cDefine6");
	}

	/**
	 * 扩展字段7
	 */
	public M setCDefine7(java.lang.String cDefine7) {
		set("cDefine7", cDefine7);
		return (M)this;
	}

	/**
	 * 扩展字段7
	 */
	@JBoltField(name="cdefine7" ,columnName="cDefine7",type="String", remark="扩展字段7", required=false, maxLength=200, fixed=0, order=16)
	@JSONField(name = "cdefine7")
	public java.lang.String getCDefine7() {
		return getStr("cDefine7");
	}

	/**
	 * 扩展字段8
	 */
	public M setCDefine8(java.lang.String cDefine8) {
		set("cDefine8", cDefine8);
		return (M)this;
	}

	/**
	 * 扩展字段8
	 */
	@JBoltField(name="cdefine8" ,columnName="cDefine8",type="String", remark="扩展字段8", required=false, maxLength=200, fixed=0, order=17)
	@JSONField(name = "cdefine8")
	public java.lang.String getCDefine8() {
		return getStr("cDefine8");
	}

	/**
	 * 扩展字段9
	 */
	public M setCDefine9(java.lang.String cDefine9) {
		set("cDefine9", cDefine9);
		return (M)this;
	}

	/**
	 * 扩展字段9
	 */
	@JBoltField(name="cdefine9" ,columnName="cDefine9",type="String", remark="扩展字段9", required=false, maxLength=200, fixed=0, order=18)
	@JSONField(name = "cdefine9")
	public java.lang.String getCDefine9() {
		return getStr("cDefine9");
	}

	/**
	 * 扩展字段10
	 */
	public M setCDefine10(java.lang.String cDefine10) {
		set("cDefine10", cDefine10);
		return (M)this;
	}

	/**
	 * 扩展字段10
	 */
	@JBoltField(name="cdefine10" ,columnName="cDefine10",type="String", remark="扩展字段10", required=false, maxLength=200, fixed=0, order=19)
	@JSONField(name = "cdefine10")
	public java.lang.String getCDefine10() {
		return getStr("cDefine10");
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

}

