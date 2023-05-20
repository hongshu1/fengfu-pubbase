package cn.rjtech.model.momdata.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 位置列表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseLocListCN<M extends BaseLocListCN<M>> extends JBoltBaseModel<M>{
    /**主键*/
    public static final String IAUTOID = "iAutoId";
    /**上级ID*/
    public static final String IPID = "iPid";
    /**地址*/
    public static final String NAME = "Name";
    /**等级 1国家 2省 3城市 4区县*/
    public static final String ILEVEL = "iLevel";
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
	 * 上级ID
	 */
	public M setIPid(java.lang.Long iPid) {
		set("iPid", iPid);
		return (M)this;
	}

	/**
	 * 上级ID
	 */
	@JBoltField(name="ipid" ,columnName="iPid",type="Long", remark="上级ID", required=false, maxLength=19, fixed=0, order=2)
	@JSONField(name = "ipid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getIPid() {
		return getLong("iPid");
	}

	/**
	 * 地址
	 */
	public M setName(java.lang.String Name) {
		set("Name", Name);
		return (M)this;
	}

	/**
	 * 地址
	 */
	@JBoltField(name="name" ,columnName="Name",type="String", remark="地址", required=true, maxLength=255, fixed=0, order=3)
	@JSONField(name = "name")
	public java.lang.String getName() {
		return getStr("Name");
	}

	/**
	 * 等级 1国家 2省 3城市 4区县
	 */
	public M setILevel(java.lang.Integer iLevel) {
		set("iLevel", iLevel);
		return (M)this;
	}

	/**
	 * 等级 1国家 2省 3城市 4区县
	 */
	@JBoltField(name="ilevel" ,columnName="iLevel",type="Integer", remark="等级 1国家 2省 3城市 4区县", required=false, maxLength=10, fixed=0, order=4)
	@JSONField(name = "ilevel")
	public java.lang.Integer getILevel() {
		return getInt("iLevel");
	}

}

