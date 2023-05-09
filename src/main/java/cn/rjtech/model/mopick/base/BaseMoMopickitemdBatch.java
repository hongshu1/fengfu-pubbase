package cn.rjtech.model.mopick.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 生产订单-备料单存货现品票明细
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseMoMopickitemdBatch<M extends BaseMoMopickitemdBatch<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**备料单明细ID*/
    public static final String IMOPICKITEMDID = "iMoPickItemDid";
    /**存货ID*/
    public static final String IINVENTORYID = "iInventoryId";
    /**库区ID*/
    public static final String IWAREHOUSEAREAID = "iWarehouseAreaId";
    /**现品票*/
    public static final String CBARCODE = "cBarcode";
    /**数量*/
    public static final String IQTY = "iQty";
    /**批次号*/
    public static final String CBATCH = "cBatch";
    /**生产日期*/
    public static final String DPRODDATE = "dProdDate";
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
	 * 备料单明细ID
	 */
	public M setIMoPickItemDid(java.lang.Long iMoPickItemDid) {
		set("iMoPickItemDid", iMoPickItemDid);
		return (M)this;
	}

	/**
	 * 备料单明细ID
	 */
	@JBoltField(name="imopickitemdid" ,columnName="iMoPickItemDid",type="Long", remark="备料单明细ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "imopickitemdid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIMoPickItemDid() {
		return getLong("iMoPickItemDid");
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
	 * 库区ID
	 */
	public M setIWarehouseAreaId(java.lang.Long iWarehouseAreaId) {
		set("iWarehouseAreaId", iWarehouseAreaId);
		return (M)this;
	}

	/**
	 * 库区ID
	 */
	@JBoltField(name="iwarehouseareaid" ,columnName="iWarehouseAreaId",type="Long", remark="库区ID", required=true, maxLength=19, fixed=0, order=4)
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
	@JBoltField(name="cbarcode" ,columnName="cBarcode",type="String", remark="现品票", required=true, maxLength=40, fixed=0, order=5)
	@JSONField(name = "cbarcode")
	public java.lang.String getCBarcode() {
		return getStr("cBarcode");
	}

	/**
	 * 数量
	 */
	public M setIQty(java.math.BigDecimal iQty) {
		set("iQty", iQty);
		return (M)this;
	}

	/**
	 * 数量
	 */
	@JBoltField(name="iqty" ,columnName="iQty",type="BigDecimal", remark="数量", required=true, maxLength=24, fixed=6, order=6)
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
	@JBoltField(name="cbatch" ,columnName="cBatch",type="String", remark="批次号", required=true, maxLength=40, fixed=0, order=7)
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
	@JBoltField(name="dproddate" ,columnName="dProdDate",type="Date", remark="生产日期", required=true, maxLength=10, fixed=0, order=8)
	@JSONField(name = "dproddate")
	public java.util.Date getDProdDate() {
		return getDate("dProdDate");
	}

}

