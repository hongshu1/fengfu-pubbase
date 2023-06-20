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
public abstract class BaseSysMaterialspreparescan<M extends BaseSysMaterialspreparescan<M>> extends JBoltBaseModel<M>{
    public static final String DATASOURCE_CONFIG_NAME = "momdata";
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**备料单ID*/
    public static final String IMATERIALSPREPAREID = "iMaterialsPrepareId";
    /**备料单明细ID*/
    public static final String IMATERIALSPREPAREDETAILID = "iMaterialsPrepareDetailId";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**库区ID*/
    public static final String IWAREHOUSEAREAID = "iWarehouseAreaId";
    /**现品票*/
    public static final String CBARCODE = "cBarcode";
    /**已备料数量*/
    public static final String IQTY = "iQty";
    /**批次号*/
    public static final String CBATCH = "cBatch";
    /**生产日期*/
    public static final String DPRODDATE = "dProdDate";
    /**需求数量*/
    public static final String ITOTALQTY = "iTotalQty";
    /**扫描数量*/
    public static final String ISCANQTY = "iScanQty";
    /**状态：1=已扫描，0=未扫描*/
    public static final String STATE = "State";
    /**扫描时间*/
    public static final String ISCANDATE = "iScanDate";
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
	 * 备料单ID
	 */
	public M setIMaterialsPrepareId(java.lang.Long iMaterialsPrepareId) {
		set("iMaterialsPrepareId", iMaterialsPrepareId);
		return (M)this;
	}

	/**
	 * 备料单ID
	 */
	@JBoltField(name="imaterialsprepareid" ,columnName="iMaterialsPrepareId",type="Long", remark="备料单ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "imaterialsprepareid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIMaterialsPrepareId() {
		return getLong("iMaterialsPrepareId");
	}

	/**
	 * 备料单明细ID
	 */
	public M setIMaterialsPrepareDetailId(java.lang.Long iMaterialsPrepareDetailId) {
		set("iMaterialsPrepareDetailId", iMaterialsPrepareDetailId);
		return (M)this;
	}

	/**
	 * 备料单明细ID
	 */
	@JBoltField(name="imaterialspreparedetailid" ,columnName="iMaterialsPrepareDetailId",type="Long", remark="备料单明细ID", required=true, maxLength=19, fixed=0, order=3)
	@JSONField(name = "imaterialspreparedetailid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIMaterialsPrepareDetailId() {
		return getLong("iMaterialsPrepareDetailId");
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
	@JBoltField(name="iinventoryid" ,columnName="iInventoryId",type="Long", remark="存货ID", required=true, maxLength=19, fixed=0, order=4)
	@JSONField(name = "iinventoryid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIInventoryId() {
		return getLong("iInventoryId");
	}

	/**
	 * 库区ID
	 */
	public M setIWarehouseAreaId(java.lang.Long iWarehouseAreaId) {
		set("iWarehouseAreaId", iWarehouseAreaId);
		return (M)this;
	}

	/**
	 * 库区ID
	 */
	@JBoltField(name="iwarehouseareaid" ,columnName="iWarehouseAreaId",type="Long", remark="库区ID", required=true, maxLength=19, fixed=0, order=5)
	@JSONField(name = "iwarehouseareaid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIWarehouseAreaId() {
		return getLong("iWarehouseAreaId");
	}

	/**
	 * 现品票
	 */
	public M setCBarcode(java.lang.String cBarcode) {
		set("cBarcode", cBarcode);
		return (M)this;
	}

	/**
	 * 现品票
	 */
	@JBoltField(name="cbarcode" ,columnName="cBarcode",type="String", remark="现品票", required=true, maxLength=300, fixed=0, order=6)
	@JSONField(name = "cbarcode")
	public java.lang.String getCBarcode() {
		return getStr("cBarcode");
	}

	/**
	 * 已备料数量
	 */
	public M setIQty(java.math.BigDecimal iQty) {
		set("iQty", iQty);
		return (M)this;
	}

	/**
	 * 已备料数量
	 */
	@JBoltField(name="iqty" ,columnName="iQty",type="BigDecimal", remark="已备料数量", required=true, maxLength=18, fixed=0, order=7)
	@JSONField(name = "iqty")
	public java.math.BigDecimal getIQty() {
		return getBigDecimal("iQty");
	}

	/**
	 * 批次号
	 */
	public M setCBatch(java.lang.String cBatch) {
		set("cBatch", cBatch);
		return (M)this;
	}

	/**
	 * 批次号
	 */
	@JBoltField(name="cbatch" ,columnName="cBatch",type="String", remark="批次号", required=true, maxLength=50, fixed=0, order=8)
	@JSONField(name = "cbatch")
	public java.lang.String getCBatch() {
		return getStr("cBatch");
	}

	/**
	 * 生产日期
	 */
	public M setDProdDate(java.util.Date dProdDate) {
		set("dProdDate", dProdDate);
		return (M)this;
	}

	/**
	 * 生产日期
	 */
	@JBoltField(name="dproddate" ,columnName="dProdDate",type="Date", remark="生产日期", required=true, maxLength=23, fixed=3, order=9)
	@JSONField(name = "dproddate")
	public java.util.Date getDProdDate() {
		return getDate("dProdDate");
	}

	/**
	 * 需求数量
	 */
	public M setITotalQty(java.math.BigDecimal iTotalQty) {
		set("iTotalQty", iTotalQty);
		return (M)this;
	}

	/**
	 * 需求数量
	 */
	@JBoltField(name="itotalqty" ,columnName="iTotalQty",type="BigDecimal", remark="需求数量", required=true, maxLength=18, fixed=0, order=10)
	@JSONField(name = "itotalqty")
	public java.math.BigDecimal getITotalQty() {
		return getBigDecimal("iTotalQty");
	}

	/**
	 * 扫描数量
	 */
	public M setIScanQty(java.math.BigDecimal iScanQty) {
		set("iScanQty", iScanQty);
		return (M)this;
	}

	/**
	 * 扫描数量
	 */
	@JBoltField(name="iscanqty" ,columnName="iScanQty",type="BigDecimal", remark="扫描数量", required=true, maxLength=18, fixed=0, order=11)
	@JSONField(name = "iscanqty")
	public java.math.BigDecimal getIScanQty() {
		return getBigDecimal("iScanQty");
	}

	/**
	 * 状态：1=已扫描，0=未扫描
	 */
	public M setState(java.lang.String State) {
		set("State", State);
		return (M)this;
	}

	/**
	 * 状态：1=已扫描，0=未扫描
	 */
	@JBoltField(name="state" ,columnName="State",type="String", remark="状态：1=已扫描，0=未扫描", required=true, maxLength=50, fixed=0, order=12)
	@JSONField(name = "state")
	public java.lang.String getState() {
		return getStr("State");
	}

	/**
	 * 扫描时间
	 */
	public M setIScanDate(java.util.Date iScanDate) {
		set("iScanDate", iScanDate);
		return (M)this;
	}

	/**
	 * 扫描时间
	 */
	@JBoltField(name="iscandate" ,columnName="iScanDate",type="Date", remark="扫描时间", required=true, maxLength=23, fixed=3, order=13)
	@JSONField(name = "iscandate")
	public java.util.Date getIScanDate() {
		return getDate("iScanDate");
	}

}

