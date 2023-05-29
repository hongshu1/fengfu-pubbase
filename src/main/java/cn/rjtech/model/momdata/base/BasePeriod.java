package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 期间管理
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BasePeriod<M extends BasePeriod<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**预算类型(1.全年预算 2.下期修改3.实绩预测4.实绩)*/
    public static final String IBUDGETTYPE = "iBudgetType";
    /**预算年度*/
    public static final String IBUDGETYEAR = "iBudgetYear";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**更新时间*/
    public static final String DUPDATETIME = "dUpdateTime";
    /**创建人*/
    public static final String ICREATEBY = "iCreateBy";
    /**更新人*/
    public static final String IUPDATEBY = "iUpdateBy";
    /**是否启用*/
    public static final String ISENABLED = "IsEnabled";
    /**开始年月*/
    public static final String DSTARTTIME = "dStartTime";
    /**截止年月*/
    public static final String DENDTIME = "dEndTime";
    /**业务类型(1.费用预算 2投资计划)*/
    public static final String ISERVICETYPE = "iServiceType";
	/**
	 * 主键ID
	 */
	public M setIautoid(java.lang.Long iautoid) {
		set("iAutoId", iautoid);
		return (M)this;
	}

	/**
	 * 主键ID
	 */
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Long", remark="主键ID", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIautoid() {
		return getLong("iAutoId");
	}

	/**
	 * 组织ID
	 */
	public M setIorgid(java.lang.Long iorgid) {
		set("iOrgId", iorgid);
		return (M)this;
	}

	/**
	 * 组织ID
	 */
	@JBoltField(name="iorgid" ,columnName="iOrgId",type="Long", remark="组织ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIorgid() {
		return getLong("iOrgId");
	}

	/**
	 * 组织编码
	 */
	public M setCorgcode(java.lang.String corgcode) {
		set("cOrgCode", corgcode);
		return (M)this;
	}

	/**
	 * 组织编码
	 */
	@JBoltField(name="corgcode" ,columnName="cOrgCode",type="String", remark="组织编码", required=true, maxLength=20, fixed=0, order=3)
	public java.lang.String getCorgcode() {
		return getStr("cOrgCode");
	}

	/**
	 * 预算类型(1.全年预算 2.下期修改3.实绩预测4.实绩)
	 */
	public M setIbudgettype(java.lang.Integer ibudgettype) {
		set("iBudgetType", ibudgettype);
		return (M)this;
	}

	/**
	 * 预算类型(1.全年预算 2.下期修改3.实绩预测4.实绩)
	 */
	@JBoltField(name="ibudgettype" ,columnName="iBudgetType",type="Integer", remark="预算类型(1.全年预算 2.下期修改3.实绩预测4.实绩)", required=false, maxLength=10, fixed=0, order=4)
	public java.lang.Integer getIbudgettype() {
		return getInt("iBudgetType");
	}

	/**
	 * 预算年度
	 */
	public M setIbudgetyear(java.lang.Integer ibudgetyear) {
		set("iBudgetYear", ibudgetyear);
		return (M)this;
	}

	/**
	 * 预算年度
	 */
	@JBoltField(name="ibudgetyear" ,columnName="iBudgetYear",type="Integer", remark="预算年度", required=false, maxLength=10, fixed=0, order=5)
	public java.lang.Integer getIbudgetyear() {
		return getInt("iBudgetYear");
	}

	/**
	 * 创建时间
	 */
	public M setDcreatetime(java.util.Date dcreatetime) {
		set("dCreateTime", dcreatetime);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=6)
	public java.util.Date getDcreatetime() {
		return getDate("dCreateTime");
	}

	/**
	 * 更新时间
	 */
	public M setDupdatetime(java.util.Date dupdatetime) {
		set("dUpdateTime", dupdatetime);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=7)
	public java.util.Date getDupdatetime() {
		return getDate("dUpdateTime");
	}

	/**
	 * 创建人
	 */
	public M setIcreateby(java.lang.Long icreateby) {
		set("iCreateBy", icreateby);
		return (M)this;
	}

	/**
	 * 创建人
	 */
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=8)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIcreateby() {
		return getLong("iCreateBy");
	}

	/**
	 * 更新人
	 */
	public M setIupdateby(java.lang.Long iupdateby) {
		set("iUpdateBy", iupdateby);
		return (M)this;
	}

	/**
	 * 更新人
	 */
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人", required=false, maxLength=19, fixed=0, order=9)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIupdateby() {
		return getLong("iUpdateBy");
	}

	/**
	 * 是否启用
	 */
	public M setIsenabled(java.lang.Boolean isenabled) {
		set("IsEnabled", isenabled);
		return (M)this;
	}

	/**
	 * 是否启用
	 */
	@JBoltField(name="isenabled" ,columnName="IsEnabled",type="Boolean", remark="是否启用", required=true, maxLength=1, fixed=0, order=10)
	public java.lang.Boolean getIsenabled() {
		return getBoolean("IsEnabled");
	}

	/**
	 * 开始年月
	 */
	public M setDstarttime(java.util.Date dstarttime) {
		set("dStartTime", dstarttime);
		return (M)this;
	}

	/**
	 * 开始年月
	 */
	@JBoltField(name="dstarttime" ,columnName="dStartTime",type="Date", remark="开始年月", required=false, maxLength=23, fixed=3, order=11)
	public java.util.Date getDstarttime() {
		return getDate("dStartTime");
	}

	/**
	 * 截止年月
	 */
	public M setDendtime(java.util.Date dendtime) {
		set("dEndTime", dendtime);
		return (M)this;
	}

	/**
	 * 截止年月
	 */
	@JBoltField(name="dendtime" ,columnName="dEndTime",type="Date", remark="截止年月", required=false, maxLength=23, fixed=3, order=12)
	public java.util.Date getDendtime() {
		return getDate("dEndTime");
	}

	/**
	 * 业务类型(1.费用预算 2投资计划)
	 */
	public M setIservicetype(java.lang.Integer iservicetype) {
		set("iServiceType", iservicetype);
		return (M)this;
	}

	/**
	 * 业务类型(1.费用预算 2投资计划)
	 */
	@JBoltField(name="iservicetype" ,columnName="iServiceType",type="Integer", remark="业务类型(1.费用预算 2投资计划)", required=true, maxLength=10, fixed=0, order=13)
	public java.lang.Integer getIservicetype() {
		return getInt("iServiceType");
	}

}

