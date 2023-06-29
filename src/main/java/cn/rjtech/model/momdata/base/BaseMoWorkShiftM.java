package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 制造工单-指定日期班次人员信息主表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseMoWorkShiftM<M extends BaseMoWorkShiftM<M>> extends JBoltBaseModel<M>{
    public static final String DATASOURCE_CONFIG_NAME = "momdata";
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**工单任务ID*/
    public static final String IMOTASKID = "iMoTaskId";
    /**年*/
    public static final String IYEAR = "iYear";
    /**月*/
    public static final String IMONTH = "iMonth";
    /**日*/
    public static final String IDATE = "iDate";
    /**班次ID*/
    public static final String IWORKSHIFTMID = "iWorkShiftMid";
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
	 * 工单任务ID
	 */
	public M setIMoTaskId(java.lang.Long iMoTaskId) {
		set("iMoTaskId", iMoTaskId);
		return (M)this;
	}

	/**
	 * 工单任务ID
	 */
	@JBoltField(name="imotaskid" ,columnName="iMoTaskId",type="Long", remark="工单任务ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "imotaskid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIMoTaskId() {
		return getLong("iMoTaskId");
	}

	/**
	 * 年
	 */
	public M setIYear(java.lang.Integer iYear) {
		set("iYear", iYear);
		return (M)this;
	}

	/**
	 * 年
	 */
	@JBoltField(name="iyear" ,columnName="iYear",type="Integer", remark="年", required=true, maxLength=10, fixed=0, order=3)
	@JSONField(name = "iyear")
	public java.lang.Integer getIYear() {
		return getInt("iYear");
	}

	/**
	 * 月
	 */
	public M setIMonth(java.lang.Integer iMonth) {
		set("iMonth", iMonth);
		return (M)this;
	}

	/**
	 * 月
	 */
	@JBoltField(name="imonth" ,columnName="iMonth",type="Integer", remark="月", required=true, maxLength=10, fixed=0, order=4)
	@JSONField(name = "imonth")
	public java.lang.Integer getIMonth() {
		return getInt("iMonth");
	}

	/**
	 * 日
	 */
	public M setIDate(java.lang.Integer iDate) {
		set("iDate", iDate);
		return (M)this;
	}

	/**
	 * 日
	 */
	@JBoltField(name="idate" ,columnName="iDate",type="Integer", remark="日", required=true, maxLength=10, fixed=0, order=5)
	@JSONField(name = "idate")
	public java.lang.Integer getIDate() {
		return getInt("iDate");
	}

	/**
	 * 班次ID
	 */
	public M setIWorkShiftMid(java.lang.Long iWorkShiftMid) {
		set("iWorkShiftMid", iWorkShiftMid);
		return (M)this;
	}

	/**
	 * 班次ID
	 */
	@JBoltField(name="iworkshiftmid" ,columnName="iWorkShiftMid",type="Long", remark="班次ID", required=true, maxLength=19, fixed=0, order=6)
	@JSONField(name = "iworkshiftmid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIWorkShiftMid() {
		return getLong("iWorkShiftMid");
	}

}

