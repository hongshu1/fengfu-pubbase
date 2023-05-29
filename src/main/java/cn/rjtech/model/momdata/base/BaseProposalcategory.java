package cn.rjtech.model.momdata.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 禀议类型区分
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseProposalcategory<M extends BaseProposalcategory<M>> extends JBoltBaseModel<M>{
    /**主键ID*/
    public static final String IAUTOID = "iAutoId";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**类型编码*/
    public static final String CCATEGORYCODE = "cCategoryCode";
    /**类型名称*/
    public static final String CCATEGORYNAME = "cCategoryName";
    /**上级ID*/
    public static final String IPID = "iPid";
    /**层级*/
    public static final String ILEVEL = "iLevel";
    /**创建时间*/
    public static final String CREATETIME = "CreateTime";
    /**更新时间*/
    public static final String UPDATETIME = "UpdateTime";
    /**创建人*/
    public static final String ICREATEBBY = "iCreatebBy";
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
	 * 组织ID
	 */
	public M setIorgid(java.lang.Long iorgid) {
		set("iOrgId", iorgid);
		return (M)this;
	}

	/**
	 * 组织ID
	 */
	@JBoltField(name="iorgid" ,columnName="iOrgId",type="Long", remark="组织ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIorgid() {
		return getLong("iOrgId");
	}

	/**
	 * 组织编码
	 */
	public M setCorgcode(java.lang.String corgcode) {
		set("cOrgCode", corgcode);
		return (M)this;
	}

	/**
	 * 组织编码
	 */
	@JBoltField(name="corgcode" ,columnName="cOrgCode",type="String", remark="组织编码", required=true, maxLength=20, fixed=0, order=3)
	public java.lang.String getCorgcode() {
		return getStr("cOrgCode");
	}

	/**
	 * 类型编码
	 */
	public M setCcategorycode(java.lang.String ccategorycode) {
		set("cCategoryCode", ccategorycode);
		return (M)this;
	}

	/**
	 * 类型编码
	 */
	@JBoltField(name="ccategorycode" ,columnName="cCategoryCode",type="String", remark="类型编码", required=true, maxLength=30, fixed=0, order=4)
	public java.lang.String getCcategorycode() {
		return getStr("cCategoryCode");
	}

	/**
	 * 类型名称
	 */
	public M setCcategoryname(java.lang.String ccategoryname) {
		set("cCategoryName", ccategoryname);
		return (M)this;
	}

	/**
	 * 类型名称
	 */
	@JBoltField(name="ccategoryname" ,columnName="cCategoryName",type="String", remark="类型名称", required=true, maxLength=100, fixed=0, order=5)
	public java.lang.String getCcategoryname() {
		return getStr("cCategoryName");
	}

	/**
	 * 上级ID
	 */
	public M setIpid(java.lang.Long ipid) {
		set("iPid", ipid);
		return (M)this;
	}

	/**
	 * 上级ID
	 */
	@JBoltField(name="ipid" ,columnName="iPid",type="Long", remark="上级ID", required=true, maxLength=19, fixed=0, order=6)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIpid() {
		return getLong("iPid");
	}

	/**
	 * 层级
	 */
	public M setIlevel(java.lang.Integer ilevel) {
		set("iLevel", ilevel);
		return (M)this;
	}

	/**
	 * 层级
	 */
	@JBoltField(name="ilevel" ,columnName="iLevel",type="Integer", remark="层级", required=true, maxLength=10, fixed=0, order=7)
	public java.lang.Integer getIlevel() {
		return getInt("iLevel");
	}

	/**
	 * 创建时间
	 */
	public M setCreatetime(java.util.Date createtime) {
		set("CreateTime", createtime);
		return (M)this;
	}

	/**
	 * 创建时间
	 */
	@JBoltField(name="createtime" ,columnName="CreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=8)
	public java.util.Date getCreatetime() {
		return getDate("CreateTime");
	}

	/**
	 * 更新时间
	 */
	public M setUpdatetime(java.util.Date updatetime) {
		set("UpdateTime", updatetime);
		return (M)this;
	}

	/**
	 * 更新时间
	 */
	@JBoltField(name="updatetime" ,columnName="UpdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=9)
	public java.util.Date getUpdatetime() {
		return getDate("UpdateTime");
	}

	/**
	 * 创建人
	 */
	public M setIcreatebby(java.lang.Long icreatebby) {
		set("iCreatebBy", icreatebby);
		return (M)this;
	}

	/**
	 * 创建人
	 */
	@JBoltField(name="icreatebby" ,columnName="iCreatebBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=10)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIcreatebby() {
		return getLong("iCreatebBy");
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
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人", required=false, maxLength=19, fixed=0, order=11)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIupdateby() {
		return getLong("iUpdateBy");
	}

}

