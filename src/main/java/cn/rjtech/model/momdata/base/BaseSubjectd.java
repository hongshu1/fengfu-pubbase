package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 科目对照细表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseSubjectd<M extends BaseSubjectd<M>> extends JBoltBaseModel<M>{
    /**id*/
    public static final String IAUTOID = "iAutoId";
    /**科目主表ID*/
    public static final String ISUBJECTMID = "iSubjectMId";
    /**U8末级科目*/
    public static final String CSUBJECTCODE = "cSubjectCode";
    /**U8末级科目*/
    public static final String CSUBJECTNAME = "cSubjectName";
    /***/
    public static final String CREATETIME = "CreateTime";
    /***/
    public static final String UPDATETIME = "UpdateTime";
    /***/
    public static final String IUPDATEBY = "iUpdateBy";
    /***/
    public static final String ICREATEBY = "iCreateBy";
	/**
	 * id
	 */
	public M setIautoid(java.lang.Long iautoid) {
		set("iAutoId", iautoid);
		return (M)this;
	}

	/**
	 * id
	 */
	@JBoltField(name="iautoid" ,columnName="iAutoId",type="Long", remark="id", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIautoid() {
		return getLong("iAutoId");
	}

	/**
	 * 科目主表ID
	 */
	public M setIsubjectmid(java.lang.Long isubjectmid) {
		set("iSubjectMId", isubjectmid);
		return (M)this;
	}

	/**
	 * 科目主表ID
	 */
	@JBoltField(name="isubjectmid" ,columnName="iSubjectMId",type="Long", remark="科目主表ID", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIsubjectmid() {
		return getLong("iSubjectMId");
	}

	/**
	 * U8末级科目
	 */
	public M setCsubjectcode(java.lang.String csubjectcode) {
		set("cSubjectCode", csubjectcode);
		return (M)this;
	}

	/**
	 * U8末级科目
	 */
	@JBoltField(name="csubjectcode" ,columnName="cSubjectCode",type="String", remark="U8末级科目", required=false, maxLength=30, fixed=0, order=3)
	public java.lang.String getCsubjectcode() {
		return getStr("cSubjectCode");
	}

	/**
	 * U8末级科目
	 */
	public M setCsubjectname(java.lang.String csubjectname) {
		set("cSubjectName", csubjectname);
		return (M)this;
	}

	/**
	 * U8末级科目
	 */
	@JBoltField(name="csubjectname" ,columnName="cSubjectName",type="String", remark="U8末级科目", required=false, maxLength=30, fixed=0, order=4)
	public java.lang.String getCsubjectname() {
		return getStr("cSubjectName");
	}

	public M setCreatetime(java.util.Date createtime) {
		set("CreateTime", createtime);
		return (M)this;
	}

	@JBoltField(name="createtime" ,columnName="CreateTime",type="Date", remark="CREATETIME", required=true, maxLength=23, fixed=3, order=5)
	public java.util.Date getCreatetime() {
		return getDate("CreateTime");
	}

	public M setUpdatetime(java.util.Date updatetime) {
		set("UpdateTime", updatetime);
		return (M)this;
	}

	@JBoltField(name="updatetime" ,columnName="UpdateTime",type="Date", remark="UPDATETIME", required=false, maxLength=23, fixed=3, order=6)
	public java.util.Date getUpdatetime() {
		return getDate("UpdateTime");
	}

	public M setIupdateby(java.lang.Long iupdateby) {
		set("iUpdateBy", iupdateby);
		return (M)this;
	}

	@JBoltField(name="iupdateby" ,columnName="iUpdateBy",type="Long", remark="IUPDATEBY", required=false, maxLength=19, fixed=0, order=7)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIupdateby() {
		return getLong("iUpdateBy");
	}

	public M setIcreateby(java.lang.Long icreateby) {
		set("iCreateBy", icreateby);
		return (M)this;
	}

	@JBoltField(name="icreateby" ,columnName="iCreateBy",type="Long", remark="ICREATEBY", required=true, maxLength=19, fixed=0, order=8)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getIcreateby() {
		return getLong("iCreateBy");
	}

}

