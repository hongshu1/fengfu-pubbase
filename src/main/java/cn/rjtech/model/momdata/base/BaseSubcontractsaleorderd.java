package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 客户订单-委外销售订单明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseSubcontractsaleorderd<M extends BaseSubcontractsaleorderd<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**委外销售订单主表ID*/
    public static final String ISUBCONTRACTSALEORDERMID = "iSubcontractSaleOrderMid";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**1日数量*/
    public static final String IQTY1 = "iQty1";
    /**2日数量*/
    public static final String IQTY2 = "iQty2";
    /**3日数量*/
    public static final String IQTY3 = "iQty3";
    /**4日数量*/
    public static final String IQTY4 = "iQty4";
    /**5日数量*/
    public static final String IQTY5 = "iQty5";
    /**6日数量*/
    public static final String IQTY6 = "iQty6";
    /**7日数量*/
    public static final String IQTY7 = "iQty7";
    /**8日数量*/
    public static final String IQTY8 = "iQty8";
    /**9日数量*/
    public static final String IQTY9 = "iQty9";
    /**10日数量*/
    public static final String IQTY10 = "iQty10";
    /**11日数量*/
    public static final String IQTY11 = "iQty11";
    /**12日数量*/
    public static final String IQTY12 = "iQty12";
    /**13日数量*/
    public static final String IQTY13 = "iQty13";
    /**14日数量*/
    public static final String IQTY14 = "iQty14";
    /**15日数量*/
    public static final String IQTY15 = "iQty15";
    /**16日数量*/
    public static final String IQTY16 = "iQty16";
    /**17日数量*/
    public static final String IQTY17 = "iQty17";
    /**18日数量*/
    public static final String IQTY18 = "iQty18";
    /**19日数量*/
    public static final String IQTY19 = "iQty19";
    /**20日数量*/
    public static final String IQTY20 = "iQty20";
    /**21日数量*/
    public static final String IQTY21 = "iQty21";
    /**22日数量*/
    public static final String IQTY22 = "iQty22";
    /**23日数量*/
    public static final String IQTY23 = "iQty23";
    /**24日数量*/
    public static final String IQTY24 = "iQty24";
    /**25日数量*/
    public static final String IQTY25 = "iQty25";
    /**26日数量*/
    public static final String IQTY26 = "iQty26";
    /**27日数量*/
    public static final String IQTY27 = "iQty27";
    /**28日数量*/
    public static final String IQTY28 = "iQty28";
    /**29日数量*/
    public static final String IQTY29 = "iQty29";
    /**30日数量*/
    public static final String IQTY30 = "iQty30";
    /**31日数量*/
    public static final String IQTY31 = "iQty31";
    /**合计数量*/
    public static final String ISUM = "iSum";
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
	 * 委外销售订单主表ID
	 */
	public M setISubcontractSaleOrderMid(java.lang.Long iSubcontractSaleOrderMid) {
		set("iSubcontractSaleOrderMid", iSubcontractSaleOrderMid);
		return (M)this;
	}

	/**
	 * 委外销售订单主表ID
	 */
	@JBoltField(name="isubcontractsaleordermid" ,columnName="iSubcontractSaleOrderMid",type="Long", remark="委外销售订单主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "isubcontractsaleordermid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getISubcontractSaleOrderMid() {
		return getLong("iSubcontractSaleOrderMid");
	}

	/**
	 * 存货ID
	 */
	public M setIInventoryId(java.lang.Long iInventoryId) {
		set("iInventoryId", iInventoryId);
		return (M)this;
	}

	/**
	 * 存货ID
	 */
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="存货ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 1日数量
	 */
	public M setIQty1(java.math.BigDecimal iQty1) {
		set("iQty1", iQty1);
		return (M)this;
	}

	/**
	 * 1日数量
	 */
	@JBoltField(name="iqty1" ,columnName="iQty1",type="BigDecimal", remark="1日数量", required=false, maxLength=18, fixed=2, order=4)
	@JSONField(name = "iqty1")
	public java.math.BigDecimal getIQty1() {
		return getBigDecimal("iQty1");
	}

	/**
	 * 2日数量
	 */
	public M setIQty2(java.math.BigDecimal iQty2) {
		set("iQty2", iQty2);
		return (M)this;
	}

	/**
	 * 2日数量
	 */
	@JBoltField(name="iqty2" ,columnName="iQty2",type="BigDecimal", remark="2日数量", required=false, maxLength=18, fixed=2, order=5)
	@JSONField(name = "iqty2")
	public java.math.BigDecimal getIQty2() {
		return getBigDecimal("iQty2");
	}

	/**
	 * 3日数量
	 */
	public M setIQty3(java.math.BigDecimal iQty3) {
		set("iQty3", iQty3);
		return (M)this;
	}

	/**
	 * 3日数量
	 */
	@JBoltField(name="iqty3" ,columnName="iQty3",type="BigDecimal", remark="3日数量", required=false, maxLength=18, fixed=2, order=6)
	@JSONField(name = "iqty3")
	public java.math.BigDecimal getIQty3() {
		return getBigDecimal("iQty3");
	}

	/**
	 * 4日数量
	 */
	public M setIQty4(java.math.BigDecimal iQty4) {
		set("iQty4", iQty4);
		return (M)this;
	}

	/**
	 * 4日数量
	 */
	@JBoltField(name="iqty4" ,columnName="iQty4",type="BigDecimal", remark="4日数量", required=false, maxLength=18, fixed=2, order=7)
	@JSONField(name = "iqty4")
	public java.math.BigDecimal getIQty4() {
		return getBigDecimal("iQty4");
	}

	/**
	 * 5日数量
	 */
	public M setIQty5(java.math.BigDecimal iQty5) {
		set("iQty5", iQty5);
		return (M)this;
	}

	/**
	 * 5日数量
	 */
	@JBoltField(name="iqty5" ,columnName="iQty5",type="BigDecimal", remark="5日数量", required=false, maxLength=18, fixed=2, order=8)
	@JSONField(name = "iqty5")
	public java.math.BigDecimal getIQty5() {
		return getBigDecimal("iQty5");
	}

	/**
	 * 6日数量
	 */
	public M setIQty6(java.math.BigDecimal iQty6) {
		set("iQty6", iQty6);
		return (M)this;
	}

	/**
	 * 6日数量
	 */
	@JBoltField(name="iqty6" ,columnName="iQty6",type="BigDecimal", remark="6日数量", required=false, maxLength=18, fixed=2, order=9)
	@JSONField(name = "iqty6")
	public java.math.BigDecimal getIQty6() {
		return getBigDecimal("iQty6");
	}

	/**
	 * 7日数量
	 */
	public M setIQty7(java.math.BigDecimal iQty7) {
		set("iQty7", iQty7);
		return (M)this;
	}

	/**
	 * 7日数量
	 */
	@JBoltField(name="iqty7" ,columnName="iQty7",type="BigDecimal", remark="7日数量", required=false, maxLength=18, fixed=2, order=10)
	@JSONField(name = "iqty7")
	public java.math.BigDecimal getIQty7() {
		return getBigDecimal("iQty7");
	}

	/**
	 * 8日数量
	 */
	public M setIQty8(java.math.BigDecimal iQty8) {
		set("iQty8", iQty8);
		return (M)this;
	}

	/**
	 * 8日数量
	 */
	@JBoltField(name="iqty8" ,columnName="iQty8",type="BigDecimal", remark="8日数量", required=false, maxLength=18, fixed=2, order=11)
	@JSONField(name = "iqty8")
	public java.math.BigDecimal getIQty8() {
		return getBigDecimal("iQty8");
	}

	/**
	 * 9日数量
	 */
	public M setIQty9(java.math.BigDecimal iQty9) {
		set("iQty9", iQty9);
		return (M)this;
	}

	/**
	 * 9日数量
	 */
	@JBoltField(name="iqty9" ,columnName="iQty9",type="BigDecimal", remark="9日数量", required=false, maxLength=18, fixed=2, order=12)
	@JSONField(name = "iqty9")
	public java.math.BigDecimal getIQty9() {
		return getBigDecimal("iQty9");
	}

	/**
	 * 10日数量
	 */
	public M setIQty10(java.math.BigDecimal iQty10) {
		set("iQty10", iQty10);
		return (M)this;
	}

	/**
	 * 10日数量
	 */
	@JBoltField(name="iqty10" ,columnName="iQty10",type="BigDecimal", remark="10日数量", required=false, maxLength=18, fixed=2, order=13)
	@JSONField(name = "iqty10")
	public java.math.BigDecimal getIQty10() {
		return getBigDecimal("iQty10");
	}

	/**
	 * 11日数量
	 */
	public M setIQty11(java.math.BigDecimal iQty11) {
		set("iQty11", iQty11);
		return (M)this;
	}

	/**
	 * 11日数量
	 */
	@JBoltField(name="iqty11" ,columnName="iQty11",type="BigDecimal", remark="11日数量", required=false, maxLength=18, fixed=2, order=14)
	@JSONField(name = "iqty11")
	public java.math.BigDecimal getIQty11() {
		return getBigDecimal("iQty11");
	}

	/**
	 * 12日数量
	 */
	public M setIQty12(java.math.BigDecimal iQty12) {
		set("iQty12", iQty12);
		return (M)this;
	}

	/**
	 * 12日数量
	 */
	@JBoltField(name="iqty12" ,columnName="iQty12",type="BigDecimal", remark="12日数量", required=false, maxLength=18, fixed=2, order=15)
	@JSONField(name = "iqty12")
	public java.math.BigDecimal getIQty12() {
		return getBigDecimal("iQty12");
	}

	/**
	 * 13日数量
	 */
	public M setIQty13(java.math.BigDecimal iQty13) {
		set("iQty13", iQty13);
		return (M)this;
	}

	/**
	 * 13日数量
	 */
	@JBoltField(name="iqty13" ,columnName="iQty13",type="BigDecimal", remark="13日数量", required=false, maxLength=18, fixed=2, order=16)
	@JSONField(name = "iqty13")
	public java.math.BigDecimal getIQty13() {
		return getBigDecimal("iQty13");
	}

	/**
	 * 14日数量
	 */
	public M setIQty14(java.math.BigDecimal iQty14) {
		set("iQty14", iQty14);
		return (M)this;
	}

	/**
	 * 14日数量
	 */
	@JBoltField(name="iqty14" ,columnName="iQty14",type="BigDecimal", remark="14日数量", required=false, maxLength=18, fixed=2, order=17)
	@JSONField(name = "iqty14")
	public java.math.BigDecimal getIQty14() {
		return getBigDecimal("iQty14");
	}

	/**
	 * 15日数量
	 */
	public M setIQty15(java.math.BigDecimal iQty15) {
		set("iQty15", iQty15);
		return (M)this;
	}

	/**
	 * 15日数量
	 */
	@JBoltField(name="iqty15" ,columnName="iQty15",type="BigDecimal", remark="15日数量", required=false, maxLength=18, fixed=2, order=18)
	@JSONField(name = "iqty15")
	public java.math.BigDecimal getIQty15() {
		return getBigDecimal("iQty15");
	}

	/**
	 * 16日数量
	 */
	public M setIQty16(java.math.BigDecimal iQty16) {
		set("iQty16", iQty16);
		return (M)this;
	}

	/**
	 * 16日数量
	 */
	@JBoltField(name="iqty16" ,columnName="iQty16",type="BigDecimal", remark="16日数量", required=false, maxLength=18, fixed=2, order=19)
	@JSONField(name = "iqty16")
	public java.math.BigDecimal getIQty16() {
		return getBigDecimal("iQty16");
	}

	/**
	 * 17日数量
	 */
	public M setIQty17(java.math.BigDecimal iQty17) {
		set("iQty17", iQty17);
		return (M)this;
	}

	/**
	 * 17日数量
	 */
	@JBoltField(name="iqty17" ,columnName="iQty17",type="BigDecimal", remark="17日数量", required=false, maxLength=18, fixed=2, order=20)
	@JSONField(name = "iqty17")
	public java.math.BigDecimal getIQty17() {
		return getBigDecimal("iQty17");
	}

	/**
	 * 18日数量
	 */
	public M setIQty18(java.math.BigDecimal iQty18) {
		set("iQty18", iQty18);
		return (M)this;
	}

	/**
	 * 18日数量
	 */
	@JBoltField(name="iqty18" ,columnName="iQty18",type="BigDecimal", remark="18日数量", required=false, maxLength=18, fixed=2, order=21)
	@JSONField(name = "iqty18")
	public java.math.BigDecimal getIQty18() {
		return getBigDecimal("iQty18");
	}

	/**
	 * 19日数量
	 */
	public M setIQty19(java.math.BigDecimal iQty19) {
		set("iQty19", iQty19);
		return (M)this;
	}

	/**
	 * 19日数量
	 */
	@JBoltField(name="iqty19" ,columnName="iQty19",type="BigDecimal", remark="19日数量", required=false, maxLength=18, fixed=2, order=22)
	@JSONField(name = "iqty19")
	public java.math.BigDecimal getIQty19() {
		return getBigDecimal("iQty19");
	}

	/**
	 * 20日数量
	 */
	public M setIQty20(java.math.BigDecimal iQty20) {
		set("iQty20", iQty20);
		return (M)this;
	}

	/**
	 * 20日数量
	 */
	@JBoltField(name="iqty20" ,columnName="iQty20",type="BigDecimal", remark="20日数量", required=false, maxLength=18, fixed=2, order=23)
	@JSONField(name = "iqty20")
	public java.math.BigDecimal getIQty20() {
		return getBigDecimal("iQty20");
	}

	/**
	 * 21日数量
	 */
	public M setIQty21(java.math.BigDecimal iQty21) {
		set("iQty21", iQty21);
		return (M)this;
	}

	/**
	 * 21日数量
	 */
	@JBoltField(name="iqty21" ,columnName="iQty21",type="BigDecimal", remark="21日数量", required=false, maxLength=18, fixed=2, order=24)
	@JSONField(name = "iqty21")
	public java.math.BigDecimal getIQty21() {
		return getBigDecimal("iQty21");
	}

	/**
	 * 22日数量
	 */
	public M setIQty22(java.math.BigDecimal iQty22) {
		set("iQty22", iQty22);
		return (M)this;
	}

	/**
	 * 22日数量
	 */
	@JBoltField(name="iqty22" ,columnName="iQty22",type="BigDecimal", remark="22日数量", required=false, maxLength=18, fixed=2, order=25)
	@JSONField(name = "iqty22")
	public java.math.BigDecimal getIQty22() {
		return getBigDecimal("iQty22");
	}

	/**
	 * 23日数量
	 */
	public M setIQty23(java.math.BigDecimal iQty23) {
		set("iQty23", iQty23);
		return (M)this;
	}

	/**
	 * 23日数量
	 */
	@JBoltField(name="iqty23" ,columnName="iQty23",type="BigDecimal", remark="23日数量", required=false, maxLength=18, fixed=2, order=26)
	@JSONField(name = "iqty23")
	public java.math.BigDecimal getIQty23() {
		return getBigDecimal("iQty23");
	}

	/**
	 * 24日数量
	 */
	public M setIQty24(java.math.BigDecimal iQty24) {
		set("iQty24", iQty24);
		return (M)this;
	}

	/**
	 * 24日数量
	 */
	@JBoltField(name="iqty24" ,columnName="iQty24",type="BigDecimal", remark="24日数量", required=false, maxLength=18, fixed=2, order=27)
	@JSONField(name = "iqty24")
	public java.math.BigDecimal getIQty24() {
		return getBigDecimal("iQty24");
	}

	/**
	 * 25日数量
	 */
	public M setIQty25(java.math.BigDecimal iQty25) {
		set("iQty25", iQty25);
		return (M)this;
	}

	/**
	 * 25日数量
	 */
	@JBoltField(name="iqty25" ,columnName="iQty25",type="BigDecimal", remark="25日数量", required=false, maxLength=18, fixed=2, order=28)
	@JSONField(name = "iqty25")
	public java.math.BigDecimal getIQty25() {
		return getBigDecimal("iQty25");
	}

	/**
	 * 26日数量
	 */
	public M setIQty26(java.math.BigDecimal iQty26) {
		set("iQty26", iQty26);
		return (M)this;
	}

	/**
	 * 26日数量
	 */
	@JBoltField(name="iqty26" ,columnName="iQty26",type="BigDecimal", remark="26日数量", required=false, maxLength=18, fixed=2, order=29)
	@JSONField(name = "iqty26")
	public java.math.BigDecimal getIQty26() {
		return getBigDecimal("iQty26");
	}

	/**
	 * 27日数量
	 */
	public M setIQty27(java.math.BigDecimal iQty27) {
		set("iQty27", iQty27);
		return (M)this;
	}

	/**
	 * 27日数量
	 */
	@JBoltField(name="iqty27" ,columnName="iQty27",type="BigDecimal", remark="27日数量", required=false, maxLength=18, fixed=2, order=30)
	@JSONField(name = "iqty27")
	public java.math.BigDecimal getIQty27() {
		return getBigDecimal("iQty27");
	}

	/**
	 * 28日数量
	 */
	public M setIQty28(java.math.BigDecimal iQty28) {
		set("iQty28", iQty28);
		return (M)this;
	}

	/**
	 * 28日数量
	 */
	@JBoltField(name="iqty28" ,columnName="iQty28",type="BigDecimal", remark="28日数量", required=false, maxLength=18, fixed=2, order=31)
	@JSONField(name = "iqty28")
	public java.math.BigDecimal getIQty28() {
		return getBigDecimal("iQty28");
	}

	/**
	 * 29日数量
	 */
	public M setIQty29(java.math.BigDecimal iQty29) {
		set("iQty29", iQty29);
		return (M)this;
	}

	/**
	 * 29日数量
	 */
	@JBoltField(name="iqty29" ,columnName="iQty29",type="BigDecimal", remark="29日数量", required=false, maxLength=18, fixed=2, order=32)
	@JSONField(name = "iqty29")
	public java.math.BigDecimal getIQty29() {
		return getBigDecimal("iQty29");
	}

	/**
	 * 30日数量
	 */
	public M setIQty30(java.math.BigDecimal iQty30) {
		set("iQty30", iQty30);
		return (M)this;
	}

	/**
	 * 30日数量
	 */
	@JBoltField(name="iqty30" ,columnName="iQty30",type="BigDecimal", remark="30日数量", required=false, maxLength=18, fixed=2, order=33)
	@JSONField(name = "iqty30")
	public java.math.BigDecimal getIQty30() {
		return getBigDecimal("iQty30");
	}

	/**
	 * 31日数量
	 */
	public M setIQty31(java.math.BigDecimal iQty31) {
		set("iQty31", iQty31);
		return (M)this;
	}

	/**
	 * 31日数量
	 */
	@JBoltField(name="iqty31" ,columnName="iQty31",type="BigDecimal", remark="31日数量", required=false, maxLength=18, fixed=2, order=34)
	@JSONField(name = "iqty31")
	public java.math.BigDecimal getIQty31() {
		return getBigDecimal("iQty31");
	}

	/**
	 * 合计数量
	 */
	public M setISum(java.math.BigDecimal iSum) {
		set("iSum", iSum);
		return (M)this;
	}

	/**
	 * 合计数量
	 */
	@JBoltField(name="isum" ,columnName="iSum",type="BigDecimal", remark="合计数量", required=false, maxLength=18, fixed=2, order=35)
	@JSONField(name = "isum")
	public java.math.BigDecimal getISum() {
		return getBigDecimal("iSum");
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
	@JBoltField(name="isdeleted" ,columnName="IsDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=36)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("IsDeleted");
	}

}

