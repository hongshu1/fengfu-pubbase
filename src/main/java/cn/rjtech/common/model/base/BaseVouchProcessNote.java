package cn.rjtech.common.model.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 单据处理节点信息表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseVouchProcessNote<M extends BaseVouchProcessNote<M>> extends JBoltBaseModel<M>{
    /**AutoID*/
    public static final String AUTOID = "AutoID";
    /**单据业务ID*/
    public static final String VOUCHBUSINESSID = "VouchBusinessID";
    /**流程次序*/
    public static final String SEQ = "Seq";
    /***/
    public static final String PREALLOCATE = "PreAllocate";
    /**流程返回的交换表ID*/
    public static final String EXCHANGEID = "ExchangeID";
    /**流程处理返回值*/
    public static final String RETURNVALUE = "ReturnValue";
    /***/
    public static final String CREATEDATE = "CreateDate";
	/**
	 * AutoID
	 */
	public M setAutoid(String autoid) {
		set("AutoID", autoid);
		return (M)this;
	}

	/**
	 * AutoID
	 */
	@JBoltField(name="autoid" ,columnName="AutoID",type="String", remark="AutoID", required=true, maxLength=30, fixed=0, order=1)
	@JSONField(name = "autoid")
	public String getAutoid() {
		return getStr("AutoID");
	}

	/**
	 * 单据业务ID
	 */
	public M setVouchbusinessid(String vouchbusinessid) {
		set("VouchBusinessID", vouchbusinessid);
		return (M)this;
	}

	/**
	 * 单据业务ID
	 */
	@JBoltField(name="vouchbusinessid" ,columnName="VouchBusinessID",type="String", remark="单据业务ID", required=false, maxLength=30, fixed=0, order=2)
	@JSONField(name = "vouchbusinessid")
	public String getVouchbusinessid() {
		return getStr("VouchBusinessID");
	}

	/**
	 * 流程次序
	 */
	public M setSeq(Integer seq) {
		set("Seq", seq);
		return (M)this;
	}

	/**
	 * 流程次序
	 */
	@JBoltField(name="seq" ,columnName="Seq",type="Integer", remark="流程次序", required=false, maxLength=10, fixed=0, order=3)
	@JSONField(name = "seq")
	public Integer getSeq() {
		return getInt("Seq");
	}

	public M setPreallocate(String preallocate) {
		set("PreAllocate", preallocate);
		return (M)this;
	}

	@JBoltField(name="preallocate" ,columnName="PreAllocate",type="String", remark="PREALLOCATE", required=false, maxLength=2147483647, fixed=0, order=4)
	@JSONField(name = "preallocate")
	public String getPreallocate() {
		return getStr("PreAllocate");
	}

	/**
	 * 流程返回的交换表ID
	 */
	public M setExchangeid(String exchangeid) {
		set("ExchangeID", exchangeid);
		return (M)this;
	}

	/**
	 * 流程返回的交换表ID
	 */
	@JBoltField(name="exchangeid" ,columnName="ExchangeID",type="String", remark="流程返回的交换表ID", required=false, maxLength=255, fixed=0, order=5)
	@JSONField(name = "exchangeid")
	public String getExchangeid() {
		return getStr("ExchangeID");
	}

	/**
	 * 流程处理返回值
	 */
	public M setReturnvalue(String returnvalue) {
		set("ReturnValue", returnvalue);
		return (M)this;
	}

	/**
	 * 流程处理返回值
	 */
	@JBoltField(name="returnvalue" ,columnName="ReturnValue",type="String", remark="流程处理返回值", required=false, maxLength=2147483647, fixed=0, order=6)
	@JSONField(name = "returnvalue")
	public String getReturnvalue() {
		return getStr("ReturnValue");
	}

	public M setCreatedate(java.util.Date createdate) {
		set("CreateDate", createdate);
		return (M)this;
	}

	@JBoltField(name="createdate" ,columnName="CreateDate",type="Date", remark="CREATEDATE", required=false, maxLength=23, fixed=3, order=7)
	@JSONField(name = "createdate")
	public java.util.Date getCreatedate() {
		return getDate("CreateDate");
	}

}

