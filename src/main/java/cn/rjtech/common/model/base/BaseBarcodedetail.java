package cn.rjtech.common.model.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 条码明细表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseBarcodedetail<M extends BaseBarcodedetail<M>> extends JBoltBaseModel<M>{
    /**AutoID*/
    public static final String AUTOID = "AutoID";
    /**主表ID*/
    public static final String MASID = "MasID";
    /***/
    public static final String SOURCEBILLTYPE = "SourceBillType";
    /**T_Sys_BarcodeEncodingDetail.AutoID*/
    public static final String ENCODINGIDXXXX = "EncodingIDXXXX";
    /**供应商条码*/
    public static final String VENBARCODE = "VenBarcode";
    /**供应商编码*/
    public static final String VENCODE = "VenCode";
    /**供应商存货编码*/
    public static final String VENINVCODE = "VenInvCode";
    /**条码*/
    public static final String BARCODE = "Barcode";
    /**存货编码*/
    public static final String INVCODE = "Invcode";
    /**客户条码*/
    public static final String CUSBARCODE = "CusBarcode";
    /***/
    public static final String CUSCODE = "CusCode";
    /**客户存货编码*/
    public static final String CUSINVCODE = "CusInvcode";
    /**包装比例*/
    public static final String PACKRATE = "PackRate";
    /**条码日期*/
    public static final String BARCODEDATE = "BarcodeDate";
    /**批次*/
    public static final String BATCH = "Batch";
    /***/
    public static final String QTY = "Qty";
    /**毛重*/
    public static final String WEIGHT = "Weight";
    /**净重*/
    public static final String NETWEIGHT = "NetWeight";
    /**面积*/
    public static final String AREA = "Area";
    /**体积*/
    public static final String VOLUME = "Volume";
    /**长度*/
    public static final String LENGTH = "Length";
    /***/
    public static final String ROLLNO = "RollNo";
    /***/
    public static final String COLOR = "Color";
    /**字唛*/
    public static final String MASK = "Mask";
    /**打印次数*/
    public static final String PRINTNUM = "PrintNum";
    /**销售订单号*/
    public static final String SONO = "SONo";
    /**销售订单号+行号*/
    public static final String SONOROW = "SONoRow";
    /**销售订单明细ID*/
    public static final String SODID = "SODid";
    /**生产单号*/
    public static final String MONO = "MONo";
    /**生产单号+行号*/
    public static final String MONOROW = "MONoRow";
    /**生产订单明细ID*/
    public static final String MODID = "MoDid";
    /**采购单号*/
    public static final String PONO = "PONo";
    /**采购单号+行号*/
    public static final String PONOROW = "PONoRow";
    /**采购订单明细ID*/
    public static final String PODID = "PODid";
    /**委外订单号*/
    public static final String OMNO = "OMNo";
    /**委外订单号+行号*/
    public static final String OMNOROW = "OMNoRow";
    /**委外订单明细ID*/
    public static final String OMDID = "OMDid";
    /**序列号*/
    public static final String SERIAL = "Serial";
    /***/
    public static final String PAT = "PAT";
    /***/
    public static final String PACKNUM = "PackNum";
    /**关闭日期*/
    public static final String CLOSEDATE = "CloseDate";
    /**关闭人*/
    public static final String CLOSEPERSON = "ClosePerson";
    /**创建人*/
    public static final String CREATEPERSON = "CreatePerson";
    /**创建时间*/
    public static final String CREATEDATE = "CreateDate";
    /**更新人*/
    public static final String MODIFYPERSON = "ModifyPerson";
    /**更新时间*/
    public static final String MODIFYDATE = "ModifyDate";
    /*标签打印模板*/
    public static final String REPORTFILENAME = "reportFileName";
	/**
	 * AutoID
	 */
	public M setAutoid(Long autoid) {
		set("AutoID", autoid);
		return (M)this;
	}

	/**
	 * AutoID
	 */
	@JBoltField(name="autoid" ,columnName="AutoID",type="Long", remark="AutoID", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(name = "autoid", serializeUsing = ToStringSerializer.class)
	public Long getAutoid() {
		return getLong("AutoID");
	}

	/**
	 * 主表ID
	 */
	public M setMasid(String masid) {
		set("MasID", masid);
		return (M)this;
	}

	/**
	 * 主表ID
	 */
	@JBoltField(name="masid" ,columnName="MasID",type="String", remark="主表ID", required=false, maxLength=30, fixed=0, order=2)
	@JSONField(name = "masid")
	public String getMasid() {
		return getStr("MasID");
	}

	public M setSourcebilltype(String sourcebilltype) {
		set("SourceBillType", sourcebilltype);
		return (M)this;
	}

	@JBoltField(name="sourcebilltype" ,columnName="SourceBillType",type="String", remark="SOURCEBILLTYPE", required=false, maxLength=30, fixed=0, order=3)
	@JSONField(name = "sourcebilltype")
	public String getSourcebilltype() {
		return getStr("SourceBillType");
	}

	/**
	 * T_Sys_BarcodeEncodingDetail.AutoID
	 */
	public M setEncodingidxxxx(String encodingidxxxx) {
		set("EncodingIDXXXX", encodingidxxxx);
		return (M)this;
	}

	/**
	 * T_Sys_BarcodeEncodingDetail.AutoID
	 */
	@JBoltField(name="encodingidxxxx" ,columnName="EncodingIDXXXX",type="String", remark="T_Sys_BarcodeEncodingDetail.AutoID", required=false, maxLength=30, fixed=0, order=4)
	@JSONField(name = "encodingidxxxx")
	public String getEncodingidxxxx() {
		return getStr("EncodingIDXXXX");
	}

	/**
	 * 供应商条码
	 */
	public M setVenbarcode(String venbarcode) {
		set("VenBarcode", venbarcode);
		return (M)this;
	}

	/**
	 * 供应商条码
	 */
	@JBoltField(name="venbarcode" ,columnName="VenBarcode",type="String", remark="供应商条码", required=false, maxLength=300, fixed=0, order=5)
	@JSONField(name = "venbarcode")
	public String getVenbarcode() {
		return getStr("VenBarcode");
	}

	/**
	 * 供应商编码
	 */
	public M setVencode(String vencode) {
		set("VenCode", vencode);
		return (M)this;
	}

	/**
	 * 供应商编码
	 */
	@JBoltField(name="vencode" ,columnName="VenCode",type="String", remark="供应商编码", required=false, maxLength=30, fixed=0, order=6)
	@JSONField(name = "vencode")
	public String getVencode() {
		return getStr("VenCode");
	}

	/**
	 * 供应商存货编码
	 */
	public M setVeninvcode(String veninvcode) {
		set("VenInvCode", veninvcode);
		return (M)this;
	}

	/**
	 * 供应商存货编码
	 */
	@JBoltField(name="veninvcode" ,columnName="VenInvCode",type="String", remark="供应商存货编码", required=false, maxLength=30, fixed=0, order=7)
	@JSONField(name = "veninvcode")
	public String getVeninvcode() {
		return getStr("VenInvCode");
	}

	/**
	 * 条码
	 */
	public M setBarcode(String barcode) {
		set("Barcode", barcode);
		return (M)this;
	}

	/**
	 * 条码
	 */
	@JBoltField(name="barcode" ,columnName="Barcode",type="String", remark="条码", required=true, maxLength=300, fixed=0, order=8)
	@JSONField(name = "barcode")
	public String getBarcode() {
		return getStr("Barcode");
	}

	/**
	 * 存货编码
	 */
	public M setInvcode(String invcode) {
		set("Invcode", invcode);
		return (M)this;
	}

	/**
	 * 存货编码
	 */
	@JBoltField(name="invcode" ,columnName="Invcode",type="String", remark="存货编码", required=false, maxLength=30, fixed=0, order=9)
	@JSONField(name = "invcode")
	public String getInvcode() {
		return getStr("Invcode");
	}

	/**
	 * 客户条码
	 */
	public M setCusbarcode(String cusbarcode) {
		set("CusBarcode", cusbarcode);
		return (M)this;
	}

	/**
	 * 客户条码
	 */
	@JBoltField(name="cusbarcode" ,columnName="CusBarcode",type="String", remark="客户条码", required=false, maxLength=300, fixed=0, order=10)
	@JSONField(name = "cusbarcode")
	public String getCusbarcode() {
		return getStr("CusBarcode");
	}

	public M setCuscode(String cuscode) {
		set("CusCode", cuscode);
		return (M)this;
	}

	@JBoltField(name="cuscode" ,columnName="CusCode",type="String", remark="CUSCODE", required=false, maxLength=30, fixed=0, order=11)
	@JSONField(name = "cuscode")
	public String getCuscode() {
		return getStr("CusCode");
	}

	/**
	 * 客户存货编码
	 */
	public M setCusinvcode(String cusinvcode) {
		set("CusInvcode", cusinvcode);
		return (M)this;
	}

	/**
	 * 客户存货编码
	 */
	@JBoltField(name="cusinvcode" ,columnName="CusInvcode",type="String", remark="客户存货编码", required=false, maxLength=30, fixed=0, order=12)
	@JSONField(name = "cusinvcode")
	public String getCusinvcode() {
		return getStr("CusInvcode");
	}

	/**
	 * 包装比例
	 */
	public M setPackrate(java.math.BigDecimal packrate) {
		set("PackRate", packrate);
		return (M)this;
	}

	/**
	 * 包装比例
	 */
	@JBoltField(name="packrate" ,columnName="PackRate",type="BigDecimal", remark="包装比例", required=false, maxLength=18, fixed=6, order=13)
	@JSONField(name = "packrate")
	public java.math.BigDecimal getPackrate() {
		return getBigDecimal("PackRate");
	}

	/**
	 * 条码日期
	 */
	public M setBarcodedate(java.util.Date barcodedate) {
		set("BarcodeDate", barcodedate);
		return (M)this;
	}

	/**
	 * 条码日期
	 */
	@JBoltField(name="barcodedate" ,columnName="BarcodeDate",type="Date", remark="条码日期", required=false, maxLength=10, fixed=0, order=14)
	@JSONField(name = "barcodedate")
	public java.util.Date getBarcodedate() {
		return getDate("BarcodeDate");
	}

	/**
	 * 批次
	 */
	public M setBatch(String batch) {
		set("Batch", batch);
		return (M)this;
	}

	/**
	 * 批次
	 */
	@JBoltField(name="batch" ,columnName="Batch",type="String", remark="批次", required=false, maxLength=30, fixed=0, order=15)
	@JSONField(name = "batch")
	public String getBatch() {
		return getStr("Batch");
	}

	public M setQty(java.math.BigDecimal qty) {
		set("Qty", qty);
		return (M)this;
	}

	@JBoltField(name="qty" ,columnName="Qty",type="BigDecimal", remark="QTY", required=false, maxLength=18, fixed=6, order=16)
	@JSONField(name = "qty")
	public java.math.BigDecimal getQty() {
		return getBigDecimal("Qty");
	}

	/**
	 * 毛重
	 */
	public M setWeight(java.math.BigDecimal weight) {
		set("Weight", weight);
		return (M)this;
	}

	/**
	 * 毛重
	 */
	@JBoltField(name="weight" ,columnName="Weight",type="BigDecimal", remark="毛重", required=false, maxLength=18, fixed=6, order=17)
	@JSONField(name = "weight")
	public java.math.BigDecimal getWeight() {
		return getBigDecimal("Weight");
	}

	/**
	 * 净重
	 */
	public M setNetweight(java.math.BigDecimal netweight) {
		set("NetWeight", netweight);
		return (M)this;
	}

	/**
	 * 净重
	 */
	@JBoltField(name="netweight" ,columnName="NetWeight",type="BigDecimal", remark="净重", required=false, maxLength=18, fixed=6, order=18)
	@JSONField(name = "netweight")
	public java.math.BigDecimal getNetweight() {
		return getBigDecimal("NetWeight");
	}

	/**
	 * 面积
	 */
	public M setArea(java.math.BigDecimal area) {
		set("Area", area);
		return (M)this;
	}

	/**
	 * 面积
	 */
	@JBoltField(name="area" ,columnName="Area",type="BigDecimal", remark="面积", required=false, maxLength=18, fixed=6, order=19)
	@JSONField(name = "area")
	public java.math.BigDecimal getArea() {
		return getBigDecimal("Area");
	}

	/**
	 * 体积
	 */
	public M setVolume(String volume) {
		set("Volume", volume);
		return (M)this;
	}

	/**
	 * 体积
	 */
	@JBoltField(name="volume" ,columnName="Volume",type="String", remark="体积", required=false, maxLength=30, fixed=0, order=20)
	@JSONField(name = "volume")
	public String getVolume() {
		return getStr("Volume");
	}

	/**
	 * 长度
	 */
	public M setLength(String length) {
		set("Length", length);
		return (M)this;
	}

	/**
	 * 长度
	 */
	@JBoltField(name="length" ,columnName="Length",type="String", remark="长度", required=false, maxLength=30, fixed=0, order=21)
	@JSONField(name = "length")
	public String getLength() {
		return getStr("Length");
	}

	public M setRollno(String rollno) {
		set("RollNo", rollno);
		return (M)this;
	}

	@JBoltField(name="rollno" ,columnName="RollNo",type="String", remark="ROLLNO", required=false, maxLength=30, fixed=0, order=22)
	@JSONField(name = "rollno")
	public String getRollno() {
		return getStr("RollNo");
	}

	public M setColor(String color) {
		set("Color", color);
		return (M)this;
	}

	@JBoltField(name="color" ,columnName="Color",type="String", remark="COLOR", required=false, maxLength=30, fixed=0, order=23)
	@JSONField(name = "color")
	public String getColor() {
		return getStr("Color");
	}

	/**
	 * 字唛
	 */
	public M setMask(String mask) {
		set("Mask", mask);
		return (M)this;
	}

	/**
	 * 字唛
	 */
	@JBoltField(name="mask" ,columnName="Mask",type="String", remark="字唛", required=false, maxLength=30, fixed=0, order=24)
	@JSONField(name = "mask")
	public String getMask() {
		return getStr("Mask");
	}

	/**
	 * 打印次数
	 */
	public M setPrintnum(Integer printnum) {
		set("PrintNum", printnum);
		return (M)this;
	}

	/**
	 * 打印次数
	 */
	@JBoltField(name="printnum" ,columnName="PrintNum",type="Integer", remark="打印次数", required=false, maxLength=10, fixed=0, order=25)
	@JSONField(name = "printnum")
	public Integer getPrintnum() {
		return getInt("PrintNum");
	}

	/**
	 * 销售订单号
	 */
	public M setSono(String sono) {
		set("SONo", sono);
		return (M)this;
	}

	/**
	 * 销售订单号
	 */
	@JBoltField(name="sono" ,columnName="SONo",type="String", remark="销售订单号", required=false, maxLength=30, fixed=0, order=26)
	@JSONField(name = "sono")
	public String getSono() {
		return getStr("SONo");
	}

	/**
	 * 销售订单号+行号
	 */
	public M setSonorow(String sonorow) {
		set("SONoRow", sonorow);
		return (M)this;
	}

	/**
	 * 销售订单号+行号
	 */
	@JBoltField(name="sonorow" ,columnName="SONoRow",type="String", remark="销售订单号+行号", required=false, maxLength=30, fixed=0, order=27)
	@JSONField(name = "sonorow")
	public String getSonorow() {
		return getStr("SONoRow");
	}

	/**
	 * 销售订单明细ID
	 */
	public M setSodid(String sodid) {
		set("SODid", sodid);
		return (M)this;
	}

	/**
	 * 销售订单明细ID
	 */
	@JBoltField(name="sodid" ,columnName="SODid",type="String", remark="销售订单明细ID", required=false, maxLength=30, fixed=0, order=28)
	@JSONField(name = "sodid")
	public String getSodid() {
		return getStr("SODid");
	}

	/**
	 * 生产单号
	 */
	public M setMono(String mono) {
		set("MONo", mono);
		return (M)this;
	}

	/**
	 * 生产单号
	 */
	@JBoltField(name="mono" ,columnName="MONo",type="String", remark="生产单号", required=false, maxLength=30, fixed=0, order=29)
	@JSONField(name = "mono")
	public String getMono() {
		return getStr("MONo");
	}

	/**
	 * 生产单号+行号
	 */
	public M setMonorow(String monorow) {
		set("MONoRow", monorow);
		return (M)this;
	}

	/**
	 * 生产单号+行号
	 */
	@JBoltField(name="monorow" ,columnName="MONoRow",type="String", remark="生产单号+行号", required=false, maxLength=30, fixed=0, order=30)
	@JSONField(name = "monorow")
	public String getMonorow() {
		return getStr("MONoRow");
	}

	/**
	 * 生产订单明细ID
	 */
	public M setModid(String modid) {
		set("MoDid", modid);
		return (M)this;
	}

	/**
	 * 生产订单明细ID
	 */
	@JBoltField(name="modid" ,columnName="MoDid",type="String", remark="生产订单明细ID", required=false, maxLength=30, fixed=0, order=31)
	@JSONField(name = "modid")
	public String getModid() {
		return getStr("MoDid");
	}

	/**
	 * 采购单号
	 */
	public M setPono(String pono) {
		set("PONo", pono);
		return (M)this;
	}

	/**
	 * 采购单号
	 */
	@JBoltField(name="pono" ,columnName="PONo",type="String", remark="采购单号", required=false, maxLength=30, fixed=0, order=32)
	@JSONField(name = "pono")
	public String getPono() {
		return getStr("PONo");
	}

	/**
	 * 采购单号+行号
	 */
	public M setPonorow(String ponorow) {
		set("PONoRow", ponorow);
		return (M)this;
	}

	/**
	 * 采购单号+行号
	 */
	@JBoltField(name="ponorow" ,columnName="PONoRow",type="String", remark="采购单号+行号", required=false, maxLength=30, fixed=0, order=33)
	@JSONField(name = "ponorow")
	public String getPonorow() {
		return getStr("PONoRow");
	}

	/**
	 * 采购订单明细ID
	 */
	public M setPodid(String podid) {
		set("PODid", podid);
		return (M)this;
	}

	/**
	 * 采购订单明细ID
	 */
	@JBoltField(name="podid" ,columnName="PODid",type="String", remark="采购订单明细ID", required=false, maxLength=30, fixed=0, order=34)
	@JSONField(name = "podid")
	public String getPodid() {
		return getStr("PODid");
	}

	/**
	 * 委外订单号
	 */
	public M setOmno(String omno) {
		set("OMNo", omno);
		return (M)this;
	}

	/**
	 * 委外订单号
	 */
	@JBoltField(name="omno" ,columnName="OMNo",type="String", remark="委外订单号", required=false, maxLength=30, fixed=0, order=35)
	@JSONField(name = "omno")
	public String getOmno() {
		return getStr("OMNo");
	}

	/**
	 * 委外订单号+行号
	 */
	public M setOmnorow(String omnorow) {
		set("OMNoRow", omnorow);
		return (M)this;
	}

	/**
	 * 委外订单号+行号
	 */
	@JBoltField(name="omnorow" ,columnName="OMNoRow",type="String", remark="委外订单号+行号", required=false, maxLength=30, fixed=0, order=36)
	@JSONField(name = "omnorow")
	public String getOmnorow() {
		return getStr("OMNoRow");
	}

	/**
	 * 委外订单明细ID
	 */
	public M setOmdid(String omdid) {
		set("OMDid", omdid);
		return (M)this;
	}

	/**
	 * 委外订单明细ID
	 */
	@JBoltField(name="omdid" ,columnName="OMDid",type="String", remark="委外订单明细ID", required=false, maxLength=30, fixed=0, order=37)
	@JSONField(name = "omdid")
	public String getOmdid() {
		return getStr("OMDid");
	}

	/**
	 * 序列号
	 */
	public M setSerial(String serial) {
		set("Serial", serial);
		return (M)this;
	}

	/**
	 * 序列号
	 */
	@JBoltField(name="serial" ,columnName="Serial",type="String", remark="序列号", required=false, maxLength=30, fixed=0, order=38)
	@JSONField(name = "serial")
	public String getSerial() {
		return getStr("Serial");
	}

	public M setPat(String pat) {
		set("PAT", pat);
		return (M)this;
	}

	@JBoltField(name="pat" ,columnName="PAT",type="String", remark="PAT", required=false, maxLength=250, fixed=0, order=39)
	@JSONField(name = "pat")
	public String getPat() {
		return getStr("PAT");
	}

	public M setPacknum(Integer packnum) {
		set("PackNum", packnum);
		return (M)this;
	}

	@JBoltField(name="packnum" ,columnName="PackNum",type="Integer", remark="PACKNUM", required=false, maxLength=10, fixed=0, order=40)
	@JSONField(name = "packnum")
	public Integer getPacknum() {
		return getInt("PackNum");
	}

	/**
	 * 关闭日期
	 */
	public M setClosedate(java.util.Date closedate) {
		set("CloseDate", closedate);
		return (M)this;
	}

	/**
	 * 关闭日期
	 */
	@JBoltField(name="closedate" ,columnName="CloseDate",type="Date", remark="关闭日期", required=false, maxLength=23, fixed=3, order=41)
	@JSONField(name = "closedate")
	public java.util.Date getClosedate() {
		return getDate("CloseDate");
	}

	/**
	 * 关闭人
	 */
	public M setCloseperson(String closeperson) {
		set("ClosePerson", closeperson);
		return (M)this;
	}

	/**
	 * 关闭人
	 */
	@JBoltField(name="closeperson" ,columnName="ClosePerson",type="String", remark="关闭人", required=false, maxLength=30, fixed=0, order=42)
	@JSONField(name = "closeperson")
	public String getCloseperson() {
		return getStr("ClosePerson");
	}

	/**
	 * 创建人
	 */
	public M setCreateperson(String createperson) {
		set("CreatePerson", createperson);
		return (M)this;
	}

	/**
	 * 创建人
	 */
	@JBoltField(name="createperson" ,columnName="CreatePerson",type="String", remark="创建人", required=false, maxLength=30, fixed=0, order=43)
	@JSONField(name = "createperson")
	public String getCreateperson() {
		return getStr("CreatePerson");
	}

	/**
	 * 创建时间
	 */
	public M setCreatedate(java.util.Date createdate) {
		set("CreateDate", createdate);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="createdate" ,columnName="CreateDate",type="Date", remark="创建时间", required=false, maxLength=23, fixed=3, order=44)
	@JSONField(name = "createdate")
	public java.util.Date getCreatedate() {
		return getDate("CreateDate");
	}

	/**
	 * 更新人
	 */
	public M setModifyperson(String modifyperson) {
		set("ModifyPerson", modifyperson);
		return (M)this;
	}

	/**
	 * 更新人
	 */
	@JBoltField(name="modifyperson" ,columnName="ModifyPerson",type="String", remark="更新人", required=false, maxLength=30, fixed=0, order=45)
	@JSONField(name = "modifyperson")
	public String getModifyperson() {
		return getStr("ModifyPerson");
	}

	/**
	 * 更新时间
	 */
	public M setModifydate(java.util.Date modifydate) {
		set("ModifyDate", modifydate);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="modifydate" ,columnName="ModifyDate",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=46)
	@JSONField(name = "modifydate")
	public java.util.Date getModifydate() {
		return getDate("ModifyDate");
	}

	/**
	 * 标签打印模板
	 */
	public M setReportFileName(String reportFileName) {
		set("ReportFileName", reportFileName);
		return (M)this;
	}

	/**
	 * 标签打印模板
	 */
	@JBoltField(name="reportfilename" ,columnName="ReportFileName",type="String", remark="标签打印模板", required=false, maxLength=60, fixed=0, order=47)
	@JSONField(name = "reportfilename")
	public String getReportFileName() {
		return getStr("ReportFileName");
	}
}

