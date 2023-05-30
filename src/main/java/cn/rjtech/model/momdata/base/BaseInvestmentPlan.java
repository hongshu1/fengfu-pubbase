package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 投资计划主表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseInvestmentPlan<M extends BaseInvestmentPlan<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**部门编码*/
    public static final String CDEPCODE = "cDepCode";
    /**预算类型(1.全年预算 2.下期修改)*/
    public static final String IBUDGETTYPE = "iBudgetType";
    /**预算年度*/
    public static final String IBUDGETYEAR = "iBudgetYear";
    /**备注*/
    public static final String CMEMO = "cMemo";
    /**审核状态*/
    public static final String IAUDITSTATUS = "iAuditStatus";
    /**审核时间*/
    public static final String DAUDITTIME = "dAuditTime";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**更新时间*/
    public static final String DUPDATETIME = "dUpdateTime";
    /**创建人*/
    public static final String ICREATEBY = "iCreateBy";
    /**更新人*/
    public static final String IUPDATEBY = "iUpdateBy";
    /**生效状态(1.未生效 2.已生效 3.失效)*/
    public static final String IEFFECTIVESTATUS = "iEffectiveStatus";
    /**期间ID*/
    public static final String IPERIODID = "iPeriodId";
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
	 * 部门编码
	 */
	public M setCdepcode(java.lang.String cdepcode) {
		set("cDepCode", cdepcode);
		return (M)this;
	}

	/**
	 * 部门编码
	 */
	@JBoltField(name="cdepcode" ,columnName="cDepCode",type="String", remark="部门编码", required=true, maxLength=20, fixed=0, order=4)
	public java.lang.String getCdepcode() {
		return getStr("cDepCode");
	}

	/**
	 * 预算类型(1.全年预算 2.下期修改)
	 */
	public M setIbudgettype(java.lang.Integer ibudgettype) {
		set("iBudgetType", ibudgettype);
		return (M)this;
	}

	/**
	 * 预算类型(1.全年预算 2.下期修改)
	 */
	@JBoltField(name="ibudgettype" ,columnName="iBudgetType",type="Integer", remark="预算类型(1.全年预算 2.下期修改)", required=true, maxLength=10, fixed=0, order=5)
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
	@JBoltField(name="ibudgetyear" ,columnName="iBudgetYear",type="Integer", remark="预算年度", required=false, maxLength=10, fixed=0, order=6)
	public java.lang.Integer getIbudgetyear() {
		return getInt("iBudgetYear");
	}

	/**
	 * 备注
	 */
	public M setCmemo(java.lang.String cmemo) {
		set("cMemo", cmemo);
		return (M)this;
	}

	/**
	 * 备注
	 */
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="备注", required=false, maxLength=200, fixed=0, order=7)
	public java.lang.String getCmemo() {
		return getStr("cMemo");
	}

	/**
	 * 审核状态
	 */
	public M setIauditstatus(java.lang.Integer iauditstatus) {
		set("iAuditStatus", iauditstatus);
		return (M)this;
	}

	/**
	 * 审核状态
	 */
	@JBoltField(name="iauditstatus" ,columnName="iAuditStatus",type="Integer", remark="审核状态", required=true, maxLength=10, fixed=0, order=8)
	public java.lang.Integer getIauditstatus() {
		return getInt("iAuditStatus");
	}

	/**
	 * 审核时间
	 */
	public M setDaudittime(java.util.Date daudittime) {
		set("dAuditTime", daudittime);
		return (M)this;
	}

	/**
	 * 审核时间
	 */
	@JBoltField(name="daudittime" ,columnName="dAuditTime",type="Date", remark="审核时间", required=false, maxLength=23, fixed=3, order=9)
	public java.util.Date getDaudittime() {
		return getDate("dAuditTime");
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=10)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=11)
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=12)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人", required=false, maxLength=19, fixed=0, order=13)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIupdateby() {
		return getLong("iUpdateBy");
	}

	/**
	 * 生效状态(1.未生效 2.已生效 3.失效)
	 */
	public M setIeffectivestatus(java.lang.Integer ieffectivestatus) {
		set("iEffectiveStatus", ieffectivestatus);
		return (M)this;
	}

	/**
	 * 生效状态(1.未生效 2.已生效 3.失效)
	 */
	@JBoltField(name="ieffectivestatus" ,columnName="iEffectiveStatus",type="Integer", remark="生效状态(1.未生效 2.已生效 3.失效)", required=true, maxLength=10, fixed=0, order=14)
	public java.lang.Integer getIeffectivestatus() {
		return getInt("iEffectiveStatus");
	}

	/**
	 * 期间ID
	 */
	public M setIperiodid(java.lang.Long iperiodid) {
		set("iPeriodId", iperiodid);
		return (M)this;
	}

	/**
	 * 期间ID
	 */
	@JBoltField(name="iperiodid" ,columnName="iPeriodId",type="Long", remark="期间ID", required=false, maxLength=19, fixed=0, order=15)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIperiodid() {
		return getLong("iPeriodId");
	}

}
