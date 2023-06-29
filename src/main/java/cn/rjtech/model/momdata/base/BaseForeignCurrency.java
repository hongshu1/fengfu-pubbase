package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 基础档案-币种档案
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseForeignCurrency<M extends BaseForeignCurrency<M>> extends JBoltBaseModel<M>{
    
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**币种名称*/
    public static final String CEXCH_NAME = "cexch_name";
    /**币种编码*/
    public static final String CEXCH_CODE = "cexch_code";
    /**折算方式*/
    public static final String BCAL = "bcal";
    /**小数位数*/
    public static final String IDEC = "idec";
    /**最大误差*/
    public static final String NERROR = "nerror";
    /**是否本位币*/
    public static final String IOTHERUSED = "iotherused";
    /**创建人ID*/
    public static final String ICREATEBY = "iCreateBy";
    /**创建人名称*/
    public static final String CCREATENAME = "cCreateName";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**更新人ID*/
    public static final String IUPDATEBY = "iUpdateBy";
    /**更新人名称*/
    public static final String CUPDATENAME = "cUpdateName";
    /**更新时间*/
    public static final String DUPDATETIME = "dUpdateTime";
    /**删除状态：0. 未删除 1. 已删除*/
    public static final String ISDELETED = "isDeleted";
    /**来源：1. MES 2. U8, MES端只能维护MES来源的记录*/
    public static final String ISOURCE = "iSource";
    /**来源ID*/
    public static final String ISOURCEID = "iSourceId";
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
	 * 组织ID
	 */
	public M setIOrgId(java.lang.Long iOrgId) {
		set("iOrgId", iOrgId);
		return (M)this;
	}

	/**
	 * 组织ID
	 */
	@JBoltField(name="iorgid" ,columnName="iOrgId",type="Long", remark="组织ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(name = "iorgid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIOrgId() {
		return getLong("iOrgId");
	}

	/**
	 * 组织编码
	 */
	public M setCOrgCode(java.lang.String cOrgCode) {
		set("cOrgCode", cOrgCode);
		return (M)this;
	}

	/**
	 * 组织编码
	 */
	@JBoltField(name="corgcode" ,columnName="cOrgCode",type="String", remark="组织编码", required=true, maxLength=40, fixed=0, order=3)
	@JSONField(name = "corgcode")
	public java.lang.String getCOrgCode() {
		return getStr("cOrgCode");
	}

	/**
	 * 组织名称
	 */
	public M setCOrgName(java.lang.String cOrgName) {
		set("cOrgName", cOrgName);
		return (M)this;
	}

	/**
	 * 组织名称
	 */
	@JBoltField(name="corgname" ,columnName="cOrgName",type="String", remark="组织名称", required=true, maxLength=50, fixed=0, order=4)
	@JSONField(name = "corgname")
	public java.lang.String getCOrgName() {
		return getStr("cOrgName");
	}

	/**
	 * 币种名称
	 */
	public M setCexchName(java.lang.String cexchName) {
		set("cexch_name", cexchName);
		return (M)this;
	}

	/**
	 * 币种名称
	 */
	@JBoltField(name="cexchname" ,columnName="cexch_name",type="String", remark="币种名称", required=true, maxLength=8, fixed=0, order=5)
	@JSONField(name = "cexchname")
	public java.lang.String getCexchName() {
		return getStr("cexch_name");
	}

	/**
	 * 币种编码
	 */
	public M setCexchCode(java.lang.String cexchCode) {
		set("cexch_code", cexchCode);
		return (M)this;
	}

	/**
	 * 币种编码
	 */
	@JBoltField(name="cexchcode" ,columnName="cexch_code",type="String", remark="币种编码", required=true, maxLength=4, fixed=0, order=6)
	@JSONField(name = "cexchcode")
	public java.lang.String getCexchCode() {
		return getStr("cexch_code");
	}

	/**
	 * 折算方式
	 */
	public M setBcal(java.lang.Boolean bcal) {
		set("bcal", bcal);
		return (M)this;
	}

	/**
	 * 折算方式
	 */
	@JBoltField(name="bcal" ,columnName="bcal",type="Boolean", remark="折算方式", required=true, maxLength=1, fixed=0, order=7)
	@JSONField(name = "bcal")
	public java.lang.Boolean getBcal() {
		return getBoolean("bcal");
	}

	/**
	 * 小数位数
	 */
	public M setIdec(java.lang.Short idec) {
		set("idec", idec);
		return (M)this;
	}

	/**
	 * 小数位数
	 */
	@JBoltField(name="idec" ,columnName="idec",type="", remark="小数位数", required=true, maxLength=3, fixed=0, order=8)
	@JSONField(name = "idec")
	public java.lang.Short getIdec() {
		return getShort("idec");
	}

	/**
	 * 最大误差
	 */
	public M setNerror(java.lang.Float nerror) {
		set("nerror", nerror);
		return (M)this;
	}

	/**
	 * 最大误差
	 */
	@JBoltField(name="nerror" ,columnName="nerror",type="", remark="最大误差", required=true, maxLength=24, fixed=0, order=9)
	@JSONField(name = "nerror")
	public java.lang.Float getNerror() {
		return getFloat("nerror");
	}

	/**
	 * 是否本位币
	 */
	public M setIotherused(java.lang.Integer iotherused) {
		set("iotherused", iotherused);
		return (M)this;
	}

	/**
	 * 是否本位币
	 */
	@JBoltField(name="iotherused" ,columnName="iotherused",type="Integer", remark="是否本位币", required=false, maxLength=10, fixed=0, order=10)
	@JSONField(name = "iotherused")
	public java.lang.Integer getIotherused() {
		return getInt("iotherused");
	}

	/**
	 * 创建人ID
	 */
	public M setICreateBy(java.lang.Long iCreateBy) {
		set("iCreateBy", iCreateBy);
		return (M)this;
	}

	/**
	 * 创建人ID
	 */
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人ID", required=true, maxLength=19, fixed=0, order=11)
	@JSONField(name = "icreateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getICreateBy() {
		return getLong("iCreateBy");
	}

	/**
	 * 创建人名称
	 */
	public M setCCreateName(java.lang.String cCreateName) {
		set("cCreateName", cCreateName);
		return (M)this;
	}

	/**
	 * 创建人名称
	 */
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=50, fixed=0, order=12)
	@JSONField(name = "ccreatename")
	public java.lang.String getCCreateName() {
		return getStr("cCreateName");
	}

	/**
	 * 创建时间
	 */
	public M setDCreateTime(java.util.Date dCreateTime) {
		set("dCreateTime", dCreateTime);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=13)
	@JSONField(name = "dcreatetime")
	public java.util.Date getDCreateTime() {
		return getDate("dCreateTime");
	}

	/**
	 * 更新人ID
	 */
	public M setIUpdateBy(java.lang.Long iUpdateBy) {
		set("iUpdateBy", iUpdateBy);
		return (M)this;
	}

	/**
	 * 更新人ID
	 */
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人ID", required=true, maxLength=19, fixed=0, order=14)
	@JSONField(name = "iupdateby", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIUpdateBy() {
		return getLong("iUpdateBy");
	}

	/**
	 * 更新人名称
	 */
	public M setCUpdateName(java.lang.String cUpdateName) {
		set("cUpdateName", cUpdateName);
		return (M)this;
	}

	/**
	 * 更新人名称
	 */
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=true, maxLength=50, fixed=0, order=15)
	@JSONField(name = "cupdatename")
	public java.lang.String getCUpdateName() {
		return getStr("cUpdateName");
	}

	/**
	 * 更新时间
	 */
	public M setDUpdateTime(java.util.Date dUpdateTime) {
		set("dUpdateTime", dUpdateTime);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=true, maxLength=23, fixed=3, order=16)
	@JSONField(name = "dupdatetime")
	public java.util.Date getDUpdateTime() {
		return getDate("dUpdateTime");
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	public M setIsDeleted(java.lang.Boolean isDeleted) {
		set("isDeleted", isDeleted);
		return (M)this;
	}

	/**
	 * 删除状态：0. 未删除 1. 已删除
	 */
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="删除状态：0. 未删除 1. 已删除", required=true, maxLength=1, fixed=0, order=17)
	@JSONField(name = "isdeleted")
	public java.lang.Boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 来源：1. MES 2. U8, MES端只能维护MES来源的记录
	 */
	public M setISource(java.lang.Integer iSource) {
		set("iSource", iSource);
		return (M)this;
	}

	/**
	 * 来源：1. MES 2. U8, MES端只能维护MES来源的记录
	 */
	@JBoltField(name="isource" ,columnName="iSource",type="Integer", remark="来源：1. MES 2. U8, MES端只能维护MES来源的记录", required=true, maxLength=10, fixed=0, order=18)
	@JSONField(name = "isource")
	public java.lang.Integer getISource() {
		return getInt("iSource");
	}

	/**
	 * 来源ID
	 */
	public M setISourceId(java.lang.String iSourceId) {
		set("iSourceId", iSourceId);
		return (M)this;
	}

	/**
	 * 来源ID
	 */
	@JBoltField(name="isourceid" ,columnName="iSourceId",type="String", remark="来源ID", required=false, maxLength=32, fixed=0, order=19)
	@JSONField(name = "isourceid")
	public java.lang.String getISourceId() {
		return getStr("iSourceId");
	}

}

