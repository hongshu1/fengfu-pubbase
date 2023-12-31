package cn.rjtech.model.main.base;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings("unchecked")
public abstract class BaseMessageUser<M extends BaseMessageUser<M>> extends JBoltBaseModel<M>{
    /**主键id*/
    public static final String ID = "id";
    /**消息id*/
    public static final String MESSAGE_ID = "message_id";
    /**用户id*/
    public static final String USER_ID = "user_id";
    /**删除标记*/
    public static final String DEL_FAG = "del_fag";
	/**
	 * 主键id
	 */
	public M setId(java.lang.Long id) {
		set("id", id);
		return (M)this;
	}

	/**
	 * 主键id
	 */
	@JBoltField(name="id" ,columnName="id",type="Long", remark="主键id", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(name = "id", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getId() {
		return getLong("id");
	}

	/**
	 * 消息id
	 */
	public M setMessageId(java.lang.Long messageId) {
		set("message_id", messageId);
		return (M)this;
	}

	/**
	 * 消息id
	 */
	@JBoltField(name="messageid" ,columnName="message_id",type="Long", remark="消息id", required=false, maxLength=19, fixed=0, order=2)
	@JSONField(name = "messageid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getMessageId() {
		return getLong("message_id");
	}

	/**
	 * 用户id
	 */
	public M setUserId(java.lang.Long userId) {
		set("user_id", userId);
		return (M)this;
	}

	/**
	 * 用户id
	 */
	@JBoltField(name="userid" ,columnName="user_id",type="Long", remark="用户id", required=false, maxLength=19, fixed=0, order=3)
	@JSONField(name = "userid", serializeUsing = ToStringSerializer.class)
	public java.lang.Long getUserId() {
		return getLong("user_id");
	}

	/**
	 * 删除标记
	 */
	public M setDelFag(java.lang.Boolean delFag) {
		set("del_fag", delFag);
		return (M)this;
	}

	/**
	 * 删除标记
	 */
	@JBoltField(name="delfag" ,columnName="del_fag",type="Boolean", remark="删除标记", required=false, maxLength=1, fixed=0, order=4)
	@JSONField(name = "delfag")
	public java.lang.Boolean getDelFag() {
		return getBoolean("del_fag");
	}

}

