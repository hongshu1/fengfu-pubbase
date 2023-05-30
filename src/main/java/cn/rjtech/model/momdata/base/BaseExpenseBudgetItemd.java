package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 费用预算项目明细表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseExpenseBudgetItemd<M extends BaseExpenseBudgetItemd<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**费用预算项目ID*/
    public static final String IEXPENSEITEMID = "iExpenseItemId";
    /**年份*/
    public static final String IYEAR = "iYear";
    /**月份*/
    public static final String IMONTH = "iMonth";
    /**数量*/
    public static final String IQUANTITY = "iQuantity";
    /**金额*/
    public static final String IAMOUNT = "iAmount";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**更新时间*/
    public static final String DUPDATETIME = "dUpdateTime";
    /**创建人*/
    public static final String ICREATEBY = "iCreateBy";
    /**更新人*/
    public static final String IUPDATEBY = "iUpdateBy";
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
	 * 费用预算项目ID
	 */
	public M setIexpenseitemid(java.lang.Long iexpenseitemid) {
		set("iExpenseItemId", iexpenseitemid);
		return (M)this;
	}

	/**
	 * 费用预算项目ID
	 */
	@JBoltField(name="iexpenseitemid" ,columnName="iExpenseItemId",type="Long", remark="费用预算项目ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIexpenseitemid() {
		return getLong("iExpenseItemId");
	}

	/**
	 * 年份
	 */
	public M setIyear(java.lang.Integer iyear) {
		set("iYear", iyear);
		return (M)this;
	}

	/**
	 * 年份
	 */
	@JBoltField(name="iyear" ,columnName="iYear",type="Integer", remark="年份", required=true, maxLength=10, fixed=0, order=3)
	public java.lang.Integer getIyear() {
		return getInt("iYear");
	}

	/**
	 * 月份
	 */
	public M setImonth(java.lang.Integer imonth) {
		set("iMonth", imonth);
		return (M)this;
	}

	/**
	 * 月份
	 */
	@JBoltField(name="imonth" ,columnName="iMonth",type="Integer", remark="月份", required=true, maxLength=10, fixed=0, order=4)
	public java.lang.Integer getImonth() {
		return getInt("iMonth");
	}

	/**
	 * 数量
	 */
	public M setIquantity(java.math.BigDecimal iquantity) {
		set("iQuantity", iquantity);
		return (M)this;
	}

	/**
	 * 数量
	 */
	@JBoltField(name="iquantity" ,columnName="iQuantity",type="BigDecimal", remark="数量", required=false, maxLength=30, fixed=0, order=5)
	public java.math.BigDecimal getIquantity() {
		return getBigDecimal("iQuantity");
	}

	/**
	 * 金额
	 */
	public M setIamount(java.math.BigDecimal iamount) {
		set("iAmount", iamount);
		return (M)this;
	}

	/**
	 * 金额
	 */
	@JBoltField(name="iamount" ,columnName="iAmount",type="BigDecimal", remark="金额", required=false, maxLength=30, fixed=2, order=6)
	public java.math.BigDecimal getIamount() {
		return getBigDecimal("iAmount");
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
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=7)
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
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=8)
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
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=false, maxLength=19, fixed=0, order=9)
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人", required=false, maxLength=19, fixed=0, order=10)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIupdateby() {
		return getLong("iUpdateBy");
	}

}
