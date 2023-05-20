package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 存货档案-附加
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseInventoryAddition<M extends BaseInventoryAddition<M>> extends JBoltBaseModel<M>{
    /**主键*/
    public static final String IAUTOID = "iAutoId";
    /**料品档案ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**长*/
    public static final String ILENGTH = "iLength";
    /**宽*/
    public static final String IWIDTH = "iWidth";
    /**高*/
    public static final String IHEIGHT = "iHeight";
    /**空间值*/
    public static final String ISPACE = "iSpace";
    /**体积单位*/
    public static final String IVOLUME = "iVolume";
    /**启用时间*/
    public static final String DFROMDATE = "dFromDate";
    /**停用时间*/
    public static final String DTODATE = "dToDate";
    /**要求描述*/
    public static final String CDESCRIPTION = "cDescription";
    /**步距*/
    public static final String ISTEPDISTANCE = "iStepDistance";
    /**结算模式*/
    public static final String PRIVATEDESCSEG9 = "privatedescseg9";
    /**材质*/
    public static final String PRIVATEDESCSEG10 = "privatedescseg10";
    /**模摊次数*/
    public static final String PRIVATEDESCSEG11 = "privatedescseg11";
    /**模摊月数*/
    public static final String PRIVATEDESCSEG12 = "privatedescseg12";
    /**模摊总费用*/
    public static final String PRIVATEDESCSEG13 = "privatedescseg13";
    /**检验批次*/
    public static final String PRIVATEDESCSEG14 = "privatedescseg14";
    /**检验参数*/
    public static final String PRIVATEDESCSEG15 = "privatedescseg15";
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
	 * 料品档案ID
	 */
	public M setIInventoryId(java.lang.Long iInventoryId) {
		set("iInventoryId", iInventoryId);
		return (M)this;
	}

	/**
	 * 料品档案ID
	 */
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="料品档案ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 长
	 */
	public M setILength(java.math.BigDecimal iLength) {
		set("iLength", iLength);
		return (M)this;
	}

	/**
	 * 长
	 */
	@JBoltField(name="ilength" ,columnName="iLength",type="BigDecimal", remark="长", required=false, maxLength=24, fixed=6, order=3)
	@JSONField(name = "ilength")
	public java.math.BigDecimal getILength() {
		return getBigDecimal("iLength");
	}

	/**
	 * 宽
	 */
	public M setIWidth(java.math.BigDecimal iWidth) {
		set("iWidth", iWidth);
		return (M)this;
	}

	/**
	 * 宽
	 */
	@JBoltField(name="iwidth" ,columnName="iWidth",type="BigDecimal", remark="宽", required=false, maxLength=24, fixed=6, order=4)
	@JSONField(name = "iwidth")
	public java.math.BigDecimal getIWidth() {
		return getBigDecimal("iWidth");
	}

	/**
	 * 高
	 */
	public M setIHeight(java.math.BigDecimal iHeight) {
		set("iHeight", iHeight);
		return (M)this;
	}

	/**
	 * 高
	 */
	@JBoltField(name="iheight" ,columnName="iHeight",type="BigDecimal", remark="高", required=false, maxLength=24, fixed=6, order=5)
	@JSONField(name = "iheight")
	public java.math.BigDecimal getIHeight() {
		return getBigDecimal("iHeight");
	}

	/**
	 * 空间值
	 */
	public M setISpace(java.math.BigDecimal iSpace) {
		set("iSpace", iSpace);
		return (M)this;
	}

	/**
	 * 空间值
	 */
	@JBoltField(name="ispace" ,columnName="iSpace",type="BigDecimal", remark="空间值", required=false, maxLength=24, fixed=6, order=6)
	@JSONField(name = "ispace")
	public java.math.BigDecimal getISpace() {
		return getBigDecimal("iSpace");
	}

	/**
	 * 体积单位
	 */
	public M setIVolume(java.math.BigDecimal iVolume) {
		set("iVolume", iVolume);
		return (M)this;
	}

	/**
	 * 体积单位
	 */
	@JBoltField(name="ivolume" ,columnName="iVolume",type="BigDecimal", remark="体积单位", required=false, maxLength=24, fixed=6, order=7)
	@JSONField(name = "ivolume")
	public java.math.BigDecimal getIVolume() {
		return getBigDecimal("iVolume");
	}

	/**
	 * 启用时间
	 */
	public M setDFromDate(java.util.Date dFromDate) {
		set("dFromDate", dFromDate);
		return (M)this;
	}

	/**
	 * 启用时间
	 */
	@JBoltField(name="dfromdate" ,columnName="dFromDate",type="Date", remark="启用时间", required=false, maxLength=23, fixed=3, order=8)
	@JSONField(name = "dfromdate")
	public java.util.Date getDFromDate() {
		return getDate("dFromDate");
	}

	/**
	 * 停用时间
	 */
	public M setDToDate(java.util.Date dToDate) {
		set("dToDate", dToDate);
		return (M)this;
	}

	/**
	 * 停用时间
	 */
	@JBoltField(name="dtodate" ,columnName="dToDate",type="Date", remark="停用时间", required=false, maxLength=23, fixed=3, order=9)
	@JSONField(name = "dtodate")
	public java.util.Date getDToDate() {
		return getDate("dToDate");
	}

	/**
	 * 要求描述
	 */
	public M setCDescription(java.lang.String cDescription) {
		set("cDescription", cDescription);
		return (M)this;
	}

	/**
	 * 要求描述
	 */
	@JBoltField(name="cdescription" ,columnName="cDescription",type="String", remark="要求描述", required=false, maxLength=255, fixed=0, order=10)
	@JSONField(name = "cdescription")
	public java.lang.String getCDescription() {
		return getStr("cDescription");
	}

	/**
	 * 步距
	 */
	public M setIStepDistance(java.math.BigDecimal iStepDistance) {
		set("iStepDistance", iStepDistance);
		return (M)this;
	}

	/**
	 * 步距
	 */
	@JBoltField(name="istepdistance" ,columnName="iStepDistance",type="BigDecimal", remark="步距", required=false, maxLength=24, fixed=6, order=11)
	@JSONField(name = "istepdistance")
	public java.math.BigDecimal getIStepDistance() {
		return getBigDecimal("iStepDistance");
	}

	/**
	 * 结算模式
	 */
	public M setPrivatedescseg9(java.lang.Boolean privatedescseg9) {
		set("privatedescseg9", privatedescseg9);
		return (M)this;
	}

	/**
	 * 结算模式
	 */
	@JBoltField(name="privatedescseg9" ,columnName="privatedescseg9",type="Boolean", remark="结算模式", required=false, maxLength=1, fixed=0, order=12)
	@JSONField(name = "privatedescseg9")
	public java.lang.Boolean getPrivatedescseg9() {
		return getBoolean("privatedescseg9");
	}

	/**
	 * 材质
	 */
	public M setPrivatedescseg10(java.lang.String privatedescseg10) {
		set("privatedescseg10", privatedescseg10);
		return (M)this;
	}

	/**
	 * 材质
	 */
	@JBoltField(name="privatedescseg10" ,columnName="privatedescseg10",type="String", remark="材质", required=false, maxLength=255, fixed=0, order=13)
	@JSONField(name = "privatedescseg10")
	public java.lang.String getPrivatedescseg10() {
		return getStr("privatedescseg10");
	}

	/**
	 * 模摊次数
	 */
	public M setPrivatedescseg11(java.lang.Integer privatedescseg11) {
		set("privatedescseg11", privatedescseg11);
		return (M)this;
	}

	/**
	 * 模摊次数
	 */
	@JBoltField(name="privatedescseg11" ,columnName="privatedescseg11",type="Integer", remark="模摊次数", required=false, maxLength=10, fixed=0, order=14)
	@JSONField(name = "privatedescseg11")
	public java.lang.Integer getPrivatedescseg11() {
		return getInt("privatedescseg11");
	}

	/**
	 * 模摊月数
	 */
	public M setPrivatedescseg12(java.lang.Integer privatedescseg12) {
		set("privatedescseg12", privatedescseg12);
		return (M)this;
	}

	/**
	 * 模摊月数
	 */
	@JBoltField(name="privatedescseg12" ,columnName="privatedescseg12",type="Integer", remark="模摊月数", required=false, maxLength=10, fixed=0, order=15)
	@JSONField(name = "privatedescseg12")
	public java.lang.Integer getPrivatedescseg12() {
		return getInt("privatedescseg12");
	}

	/**
	 * 模摊总费用
	 */
	public M setPrivatedescseg13(java.math.BigDecimal privatedescseg13) {
		set("privatedescseg13", privatedescseg13);
		return (M)this;
	}

	/**
	 * 模摊总费用
	 */
	@JBoltField(name="privatedescseg13" ,columnName="privatedescseg13",type="BigDecimal", remark="模摊总费用", required=false, maxLength=24, fixed=6, order=16)
	@JSONField(name = "privatedescseg13")
	public java.math.BigDecimal getPrivatedescseg13() {
		return getBigDecimal("privatedescseg13");
	}

	/**
	 * 检验批次
	 */
	public M setPrivatedescseg14(java.lang.Integer privatedescseg14) {
		set("privatedescseg14", privatedescseg14);
		return (M)this;
	}

	/**
	 * 检验批次
	 */
	@JBoltField(name="privatedescseg14" ,columnName="privatedescseg14",type="Integer", remark="检验批次", required=false, maxLength=10, fixed=0, order=17)
	@JSONField(name = "privatedescseg14")
	public java.lang.Integer getPrivatedescseg14() {
		return getInt("privatedescseg14");
	}

	/**
	 * 检验参数
	 */
	public M setPrivatedescseg15(java.lang.String privatedescseg15) {
		set("privatedescseg15", privatedescseg15);
		return (M)this;
	}

	/**
	 * 检验参数
	 */
	@JBoltField(name="privatedescseg15" ,columnName="privatedescseg15",type="String", remark="检验参数", required=false, maxLength=255, fixed=0, order=18)
	@JSONField(name = "privatedescseg15")
	public java.lang.String getPrivatedescseg15() {
		return getStr("privatedescseg15");
	}

}

