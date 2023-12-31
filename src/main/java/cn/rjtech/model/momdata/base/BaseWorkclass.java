package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 工种档案
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseWorkclass<M extends BaseWorkclass<M>> extends JBoltBaseModel<M>{
    /**主键*/
    public static final String IAUTOID = "iAutoId";
    /**编码*/
    public static final String CWORKCLASSCODE = "cWorkClassCode";
    /**名称*/
    public static final String CWORKCLASSNAME = "cWorkClassName";
    /**等级*/
    public static final String ILEVEL = "iLevel";
    /**薪酬*/
    public static final String ISALARY = "iSalary";
    /**要求备注*/
    public static final String CMEMO = "cMemo";
    /**创建人*/
    public static final String ICREATEBY = "iCreateBy";
    /**创建人名称*/
    public static final String CCREATENAME = "cCreateName";
    /**创建时间*/
    public static final String DCREATETIME = "dCreateTime";
    /**更新人*/
    public static final String IUPDATEBY = "iUpdateBy";
    /**更新人名称*/
    public static final String CUPDATENAME = "cUpdateName";
    /**更新时间*/
    public static final String DUPDATETIME = "dUpdateTime";
    /**组织ID*/
    public static final String IORGID = "iOrgId";
    /**组织编码*/
    public static final String CORGCODE = "cOrgCode";
    /**组织名称*/
    public static final String CORGNAME = "cOrgName";
    /**来源1.MES 2.U9;1.MES*/
    public static final String ISOURCE = "iSource";
    /**来源标识ID*/
    public static final String ISOURCEID = "iSourceId";
    /**是否删除 1未删除*/
    public static final String ISDELETED = "isDeleted";
    /**是否启用*/
    public static final String ISENABLED = "isEnabled";
	/**
	 * 主键
	 */
	public M setIautoid(Long iautoid) {
		set("iAutoId", iautoid);
		return (M)this;
	}
	
	/**
	 * 主键
	 */
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Long", remark="主键", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIautoid() {
		return getLong("iAutoId");
	}

	/**
	 * 编码
	 */
	public M setCworkclasscode(String cworkclasscode) {
		set("cWorkClassCode", cworkclasscode);
		return (M)this;
	}
	
	/**
	 * 编码
	 */
	@JBoltField(name="cworkclasscode" ,columnName="cWorkClassCode",type="String", remark="编码", required=true, maxLength=255, fixed=0, order=2)
	public String getCworkclasscode() {
		return getStr("cWorkClassCode");
	}

	/**
	 * 名称
	 */
	public M setCworkclassname(String cworkclassname) {
		set("cWorkClassName", cworkclassname);
		return (M)this;
	}
	
	/**
	 * 名称
	 */
	@JBoltField(name="cworkclassname" ,columnName="cWorkClassName",type="String", remark="名称", required=true, maxLength=255, fixed=0, order=3)
	public String getCworkclassname() {
		return getStr("cWorkClassName");
	}

	/**
	 * 等级
	 */
	public M setIlevel(Integer ilevel) {
		set("iLevel", ilevel);
		return (M)this;
	}
	
	/**
	 * 等级
	 */
	@JBoltField(name="ilevel" ,columnName="iLevel",type="Integer", remark="等级", required=false, maxLength=10, fixed=0, order=4)
	public Integer getIlevel() {
		return getInt("iLevel");
	}

	/**
	 * 薪酬
	 */
	public M setIsalary(java.math.BigDecimal isalary) {
		set("iSalary", isalary);
		return (M)this;
	}
	
	/**
	 * 薪酬
	 */
	@JBoltField(name="isalary" ,columnName="iSalary",type="BigDecimal", remark="薪酬", required=false, maxLength=24, fixed=6, order=5)
	public java.math.BigDecimal getIsalary() {
		return getBigDecimal("iSalary");
	}

	/**
	 * 要求备注
	 */
	public M setCmemo(String cmemo) {
		set("cMemo", cmemo);
		return (M)this;
	}
	
	/**
	 * 要求备注
	 */
	@JBoltField(name="cmemo" ,columnName="cMemo",type="String", remark="要求备注", required=false, maxLength=255, fixed=0, order=6)
	public String getCmemo() {
		return getStr("cMemo");
	}

	/**
	 * 创建人
	 */
	public M setIcreateby(Long icreateby) {
		set("iCreateBy", icreateby);
		return (M)this;
	}
	
	/**
	 * 创建人
	 */
	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="创建人", required=true, maxLength=19, fixed=0, order=7)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIcreateby() {
		return getLong("iCreateBy");
	}

	/**
	 * 创建人名称
	 */
	public M setCcreatename(String ccreatename) {
		set("cCreateName", ccreatename);
		return (M)this;
	}
	
	/**
	 * 创建人名称
	 */
	@JBoltField(name="ccreatename" ,columnName="cCreateName",type="String", remark="创建人名称", required=true, maxLength=255, fixed=0, order=8)
	public String getCcreatename() {
		return getStr("cCreateName");
	}

	/**
	 * 创建时间
	 */
	public M setDcreatetime(java.util.Date dcreatetime) {
		set("dCreateTime", dcreatetime);
		return (M)this;
	}
	
	/**
	 * 创建时间
	 */
	@JBoltField(name="dcreatetime" ,columnName="dCreateTime",type="Date", remark="创建时间", required=true, maxLength=23, fixed=3, order=9)
	public java.util.Date getDcreatetime() {
		return getDate("dCreateTime");
	}

	/**
	 * 更新人
	 */
	public M setIupdateby(Long iupdateby) {
		set("iUpdateBy", iupdateby);
		return (M)this;
	}
	
	/**
	 * 更新人
	 */
	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="更新人", required=false, maxLength=19, fixed=0, order=10)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIupdateby() {
		return getLong("iUpdateBy");
	}

	/**
	 * 更新人名称
	 */
	public M setCupdatename(String cupdatename) {
		set("cUpdateName", cupdatename);
		return (M)this;
	}
	
	/**
	 * 更新人名称
	 */
	@JBoltField(name="cupdatename" ,columnName="cUpdateName",type="String", remark="更新人名称", required=false, maxLength=255, fixed=0, order=11)
	public String getCupdatename() {
		return getStr("cUpdateName");
	}

	/**
	 * 更新时间
	 */
	public M setDupdatetime(java.util.Date dupdatetime) {
		set("dUpdateTime", dupdatetime);
		return (M)this;
	}
	
	/**
	 * 更新时间
	 */
	@JBoltField(name="dupdatetime" ,columnName="dUpdateTime",type="Date", remark="更新时间", required=false, maxLength=23, fixed=3, order=12)
	public java.util.Date getDupdatetime() {
		return getDate("dUpdateTime");
	}

	/**
	 * 组织ID
	 */
	public M setIorgid(Long iorgid) {
		set("iOrgId", iorgid);
		return (M)this;
	}
	
	/**
	 * 组织ID
	 */
	@JBoltField(name="iorgid" ,columnName="iOrgId",type="Long", remark="组织ID", required=true, maxLength=19, fixed=0, order=13)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public Long getIorgid() {
		return getLong("iOrgId");
	}

	/**
	 * 组织编码
	 */
	public M setCorgcode(String corgcode) {
		set("cOrgCode", corgcode);
		return (M)this;
	}
	
	/**
	 * 组织编码
	 */
	@JBoltField(name="corgcode" ,columnName="cOrgCode",type="String", remark="组织编码", required=true, maxLength=255, fixed=0, order=14)
	public String getCorgcode() {
		return getStr("cOrgCode");
	}

	/**
	 * 组织名称
	 */
	public M setCorgname(String corgname) {
		set("cOrgName", corgname);
		return (M)this;
	}
	
	/**
	 * 组织名称
	 */
	@JBoltField(name="corgname" ,columnName="cOrgName",type="String", remark="组织名称", required=true, maxLength=255, fixed=0, order=15)
	public String getCorgname() {
		return getStr("cOrgName");
	}

	/**
	 * 来源1.MES 2.U9;1.MES
	 */
	public M setIsource(Integer isource) {
		set("iSource", isource);
		return (M)this;
	}
	
	/**
	 * 来源1.MES 2.U9;1.MES
	 */
	@JBoltField(name="isource" ,columnName="iSource",type="Integer", remark="来源1.MES 2.U9;1.MES", required=false, maxLength=10, fixed=0, order=16)
	public Integer getIsource() {
		return getInt("iSource");
	}

	/**
	 * 来源标识ID
	 */
	public M setIsourceid(String isourceid) {
		set("iSourceId", isourceid);
		return (M)this;
	}
	
	/**
	 * 来源标识ID
	 */
	@JBoltField(name="isourceid" ,columnName="iSourceId",type="String", remark="来源标识ID", required=false, maxLength=255, fixed=0, order=17)
	public String getIsourceid() {
		return getStr("iSourceId");
	}

	/**
	 * 是否删除 1未删除
	 */
	public M setIsdeleted(Boolean isdeleted) {
		set("isDeleted", isdeleted);
		return (M)this;
	}
	
	/**
	 * 是否删除 1未删除
	 */
	@JBoltField(name="isdeleted" ,columnName="isDeleted",type="Boolean", remark="是否删除 1未删除", required=true, maxLength=1, fixed=0, order=18)
	public Boolean getIsdeleted() {
		return getBoolean("isDeleted");
	}

	/**
	 * 是否启用
	 */
	public M setIsenabled(Boolean isenabled) {
		set("isEnabled", isenabled);
		return (M)this;
	}
	
	/**
	 * 是否启用
	 */
	@JBoltField(name="isenabled" ,columnName="isEnabled",type="Boolean", remark="是否启用", required=false, maxLength=1, fixed=0, order=19)
	public Boolean getIsenabled() {
		return getBoolean("isEnabled");
	}

}

