package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseInvestmentplanMonthAdjustmentItemd<M extends BaseInvestmentplanMonthAdjustmentItemd<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**调整项目表ID*/
    public static final String IADJUSTMENTITEMID = "iAdjustmentItemId";
    /**期数*/
    public static final String IPERIODNUM = "iPeriodNum";
    /**日程*/
    public static final String CPERIODPROGRESS = "cPeriodProgress";
    /**日期*/
    public static final String DPERIODDATE = "dPeriodDate";
    /**金额*/
    public static final String IAMOUNT = "iAmount";
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
	 * 调整项目表ID
	 */
	public M setIadjustmentitemid(java.lang.Long iadjustmentitemid) {
		set("iAdjustmentItemId", iadjustmentitemid);
		return (M)this;
	}

	/**
	 * 调整项目表ID
	 */
	@JBoltField(name="iadjustmentitemid" ,columnName="iAdjustmentItemId",type="Long", remark="调整项目表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIadjustmentitemid() {
		return getLong("iAdjustmentItemId");
	}

	/**
	 * 期数
	 */
	public M setIperiodnum(java.lang.Integer iperiodnum) {
		set("iPeriodNum", iperiodnum);
		return (M)this;
	}

	/**
	 * 期数
	 */
	@JBoltField(name="iperiodnum" ,columnName="iPeriodNum",type="Integer", remark="期数", required=false, maxLength=10, fixed=0, order=3)
	public java.lang.Integer getIperiodnum() {
		return getInt("iPeriodNum");
	}

	/**
	 * 日程
	 */
	public M setCperiodprogress(java.lang.String cperiodprogress) {
		set("cPeriodProgress", cperiodprogress);
		return (M)this;
	}

	/**
	 * 日程
	 */
	@JBoltField(name="cperiodprogress" ,columnName="cPeriodProgress",type="String", remark="日程", required=false, maxLength=20, fixed=0, order=4)
	public java.lang.String getCperiodprogress() {
		return getStr("cPeriodProgress");
	}

	/**
	 * 日期
	 */
	public M setDperioddate(java.util.Date dperioddate) {
		set("dPeriodDate", dperioddate);
		return (M)this;
	}

	/**
	 * 日期
	 */
	@JBoltField(name="dperioddate" ,columnName="dPeriodDate",type="Date", remark="日期", required=false, maxLength=10, fixed=0, order=5)
	public java.util.Date getDperioddate() {
		return getDate("dPeriodDate");
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

}

