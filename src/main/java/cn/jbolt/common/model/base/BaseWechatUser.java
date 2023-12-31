package cn.jbolt.common.model.base;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import cn.jbolt.core.gen.JBoltField;
import cn.jbolt.core.model.base.JBoltBaseModel;

/**
 * 微信用户信息_模板表
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseWechatUser<M extends BaseWechatUser<M>> extends JBoltBaseModel<M>{

	/**
	 * 主键ID
	 */
	public M setId(java.lang.Long id) {
		set("id", id);
		return (M)this;
	}
	
	/**
	 * 主键ID
	 */
	@JBoltField(name="id" ,columnName="id",type="Long", remark="主键ID", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getId() {
		return getLong("id");
	}

	/**
	 * 用户昵称
	 */
	public M setNickname(java.lang.String nickname) {
		set("nickname", nickname);
		return (M)this;
	}
	
	/**
	 * 用户昵称
	 */
	@JBoltField(name="nickname" ,columnName="nickname",type="String", remark="用户昵称", required=false, maxLength=255, fixed=0, order=2)
	public java.lang.String getNickname() {
		return getStr("nickname");
	}

	/**
	 * openId
	 */
	public M setOpenId(java.lang.String openId) {
		set("open_id", openId);
		return (M)this;
	}
	
	/**
	 * openId
	 */
	@JBoltField(name="openId" ,columnName="open_id",type="String", remark="openId", required=false, maxLength=255, fixed=0, order=3)
	public java.lang.String getOpenId() {
		return getStr("open_id");
	}

	/**
	 * unionID
	 */
	public M setUnionId(java.lang.String unionId) {
		set("union_id", unionId);
		return (M)this;
	}
	
	/**
	 * unionID
	 */
	@JBoltField(name="unionId" ,columnName="union_id",type="String", remark="unionID", required=false, maxLength=255, fixed=0, order=4)
	public java.lang.String getUnionId() {
		return getStr("union_id");
	}

	/**
	 * 性别 1男 2女 0 未知
	 */
	public M setSex(java.lang.Integer sex) {
		set("sex", sex);
		return (M)this;
	}
	
	/**
	 * 性别 1男 2女 0 未知
	 */
	@JBoltField(name="sex" ,columnName="sex",type="Integer", remark="性别 1男 2女 0 未知", required=false, maxLength=10, fixed=0, order=5)
	public java.lang.Integer getSex() {
		return getInt("sex");
	}

	/**
	 * 语言
	 */
	public M setLanguage(java.lang.String language) {
		set("language", language);
		return (M)this;
	}
	
	/**
	 * 语言
	 */
	@JBoltField(name="language" ,columnName="language",type="String", remark="语言", required=false, maxLength=255, fixed=0, order=6)
	public java.lang.String getLanguage() {
		return getStr("language");
	}

	/**
	 * 是否已关注
	 */
	public M setSubscribe(java.lang.Boolean subscribe) {
		set("subscribe", subscribe);
		return (M)this;
	}
	
	/**
	 * 是否已关注
	 */
	@JBoltField(name="subscribe" ,columnName="subscribe",type="Boolean", remark="是否已关注", required=false, maxLength=1, fixed=0, order=7)
	public java.lang.Boolean getSubscribe() {
		return getBoolean("subscribe");
	}

	/**
	 * 国家
	 */
	public M setCountry(java.lang.String country) {
		set("country", country);
		return (M)this;
	}
	
	/**
	 * 国家
	 */
	@JBoltField(name="country" ,columnName="country",type="String", remark="国家", required=false, maxLength=255, fixed=0, order=8)
	public java.lang.String getCountry() {
		return getStr("country");
	}

	/**
	 * 省
	 */
	public M setProvince(java.lang.String province) {
		set("province", province);
		return (M)this;
	}
	
	/**
	 * 省
	 */
	@JBoltField(name="province" ,columnName="province",type="String", remark="省", required=false, maxLength=255, fixed=0, order=9)
	public java.lang.String getProvince() {
		return getStr("province");
	}

	/**
	 * 城市
	 */
	public M setCity(java.lang.String city) {
		set("city", city);
		return (M)this;
	}
	
	/**
	 * 城市
	 */
	@JBoltField(name="city" ,columnName="city",type="String", remark="城市", required=false, maxLength=255, fixed=0, order=10)
	public java.lang.String getCity() {
		return getStr("city");
	}

	/**
	 * 头像
	 */
	public M setHeadImgUrl(java.lang.String headImgUrl) {
		set("head_img_url", headImgUrl);
		return (M)this;
	}
	
	/**
	 * 头像
	 */
	@JBoltField(name="headImgUrl" ,columnName="head_img_url",type="String", remark="头像", required=false, maxLength=255, fixed=0, order=11)
	public java.lang.String getHeadImgUrl() {
		return getStr("head_img_url");
	}

	/**
	 * 关注时间
	 */
	public M setSubscribeTime(java.util.Date subscribeTime) {
		set("subscribe_time", subscribeTime);
		return (M)this;
	}
	
	/**
	 * 关注时间
	 */
	@JBoltField(name="subscribeTime" ,columnName="subscribe_time",type="Date", remark="关注时间", required=false, maxLength=19, fixed=0, order=12)
	public java.util.Date getSubscribeTime() {
		return getDate("subscribe_time");
	}

	/**
	 * 备注
	 */
	public M setRemark(java.lang.String remark) {
		set("remark", remark);
		return (M)this;
	}
	
	/**
	 * 备注
	 */
	@JBoltField(name="remark" ,columnName="remark",type="String", remark="备注", required=false, maxLength=255, fixed=0, order=13)
	public java.lang.String getRemark() {
		return getStr("remark");
	}

	/**
	 * 分组
	 */
	public M setGroupId(java.lang.Integer groupId) {
		set("group_id", groupId);
		return (M)this;
	}
	
	/**
	 * 分组
	 */
	@JBoltField(name="groupId" ,columnName="group_id",type="Integer", remark="分组", required=false, maxLength=10, fixed=0, order=14)
	public java.lang.Integer getGroupId() {
		return getInt("group_id");
	}

	/**
	 * 标签
	 */
	public M setTagIds(java.lang.String tagIds) {
		set("tag_ids", tagIds);
		return (M)this;
	}
	
	/**
	 * 标签
	 */
	@JBoltField(name="tagIds" ,columnName="tag_ids",type="String", remark="标签", required=false, maxLength=255, fixed=0, order=15)
	public java.lang.String getTagIds() {
		return getStr("tag_ids");
	}

	/**
	 * 关注渠道
	 */
	public M setSubscribeScene(java.lang.String subscribeScene) {
		set("subscribe_scene", subscribeScene);
		return (M)this;
	}
	
	/**
	 * 关注渠道
	 */
	@JBoltField(name="subscribeScene" ,columnName="subscribe_scene",type="String", remark="关注渠道", required=false, maxLength=255, fixed=0, order=16)
	public java.lang.String getSubscribeScene() {
		return getStr("subscribe_scene");
	}

	/**
	 * 二维码场景-开发者自定义
	 */
	public M setQrScene(java.lang.Integer qrScene) {
		set("qr_scene", qrScene);
		return (M)this;
	}
	
	/**
	 * 二维码场景-开发者自定义
	 */
	@JBoltField(name="qrScene" ,columnName="qr_scene",type="Integer", remark="二维码场景-开发者自定义", required=false, maxLength=10, fixed=0, order=17)
	public java.lang.Integer getQrScene() {
		return getInt("qr_scene");
	}

	/**
	 * 二维码扫码场景描述-开发者自定义
	 */
	public M setQrSceneStr(java.lang.String qrSceneStr) {
		set("qr_scene_str", qrSceneStr);
		return (M)this;
	}
	
	/**
	 * 二维码扫码场景描述-开发者自定义
	 */
	@JBoltField(name="qrSceneStr" ,columnName="qr_scene_str",type="String", remark="二维码扫码场景描述-开发者自定义", required=false, maxLength=255, fixed=0, order=18)
	public java.lang.String getQrSceneStr() {
		return getStr("qr_scene_str");
	}

	/**
	 * 真实姓名
	 */
	public M setRealname(java.lang.String realname) {
		set("realname", realname);
		return (M)this;
	}
	
	/**
	 * 真实姓名
	 */
	@JBoltField(name="realname" ,columnName="realname",type="String", remark="真实姓名", required=false, maxLength=255, fixed=0, order=19)
	public java.lang.String getRealname() {
		return getStr("realname");
	}

	/**
	 * 手机号
	 */
	public M setPhone(java.lang.String phone) {
		set("phone", phone);
		return (M)this;
	}
	
	/**
	 * 手机号
	 */
	@JBoltField(name="phone" ,columnName="phone",type="String", remark="手机号", required=false, maxLength=255, fixed=0, order=20)
	public java.lang.String getPhone() {
		return getStr("phone");
	}

	/**
	 * 手机号国家代码
	 */
	public M setPhoneCountryCode(java.lang.String phoneCountryCode) {
		set("phone_country_code", phoneCountryCode);
		return (M)this;
	}
	
	/**
	 * 手机号国家代码
	 */
	@JBoltField(name="phoneCountryCode" ,columnName="phone_country_code",type="String", remark="手机号国家代码", required=false, maxLength=40, fixed=0, order=21)
	public java.lang.String getPhoneCountryCode() {
		return getStr("phone_country_code");
	}

	/**
	 * 手机验证码
	 */
	public M setCheckCode(java.lang.String checkCode) {
		set("check_code", checkCode);
		return (M)this;
	}
	
	/**
	 * 手机验证码
	 */
	@JBoltField(name="checkCode" ,columnName="check_code",type="String", remark="手机验证码", required=false, maxLength=255, fixed=0, order=22)
	public java.lang.String getCheckCode() {
		return getStr("check_code");
	}

	/**
	 * 是否已验证
	 */
	public M setIsChecked(java.lang.Boolean isChecked) {
		set("is_checked", isChecked);
		return (M)this;
	}
	
	/**
	 * 是否已验证
	 */
	@JBoltField(name="isChecked" ,columnName="is_checked",type="Boolean", remark="是否已验证", required=false, maxLength=1, fixed=0, order=23)
	public java.lang.Boolean getIsChecked() {
		return getBoolean("is_checked");
	}

	/**
	 * 来源 小程序还是公众平台
	 */
	public M setSource(java.lang.Integer source) {
		set("source", source);
		return (M)this;
	}
	
	/**
	 * 来源 小程序还是公众平台
	 */
	@JBoltField(name="source" ,columnName="source",type="Integer", remark="来源 小程序还是公众平台", required=false, maxLength=10, fixed=0, order=24)
	public java.lang.Integer getSource() {
		return getInt("source");
	}

	/**
	 * 小程序登录SessionKey
	 */
	public M setSessionKey(java.lang.String sessionKey) {
		set("session_key", sessionKey);
		return (M)this;
	}
	
	/**
	 * 小程序登录SessionKey
	 */
	@JBoltField(name="sessionKey" ,columnName="session_key",type="String", remark="小程序登录SessionKey", required=false, maxLength=255, fixed=0, order=25)
	public java.lang.String getSessionKey() {
		return getStr("session_key");
	}

	/**
	 * 禁用访问
	 */
	public M setEnable(java.lang.Boolean enable) {
		set("enable", enable);
		return (M)this;
	}
	
	/**
	 * 禁用访问
	 */
	@JBoltField(name="enable" ,columnName="enable",type="Boolean", remark="禁用访问", required=false, maxLength=1, fixed=0, order=26)
	public java.lang.Boolean getEnable() {
		return getBoolean("enable");
	}

	/**
	 * 创建时间
	 */
	public M setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
		return (M)this;
	}
	
	/**
	 * 创建时间
	 */
	@JBoltField(name="createTime" ,columnName="create_time",type="Date", remark="创建时间", required=false, maxLength=19, fixed=0, order=27)
	public java.util.Date getCreateTime() {
		return getDate("create_time");
	}

	/**
	 * 验证绑定手机号时间
	 */
	public M setCheckedTime(java.util.Date checkedTime) {
		set("checked_time", checkedTime);
		return (M)this;
	}
	
	/**
	 * 验证绑定手机号时间
	 */
	@JBoltField(name="checkedTime" ,columnName="checked_time",type="Date", remark="验证绑定手机号时间", required=false, maxLength=19, fixed=0, order=28)
	public java.util.Date getCheckedTime() {
		return getDate("checked_time");
	}

	/**
	 * 所属公众平台ID
	 */
	public M setMpId(java.lang.Long mpId) {
		set("mp_id", mpId);
		return (M)this;
	}
	
	/**
	 * 所属公众平台ID
	 */
	@JBoltField(name="mpId" ,columnName="mp_id",type="Long", remark="所属公众平台ID", required=false, maxLength=19, fixed=0, order=29)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getMpId() {
		return getLong("mp_id");
	}

	/**
	 * 微信号
	 */
	public M setWeixin(java.lang.String weixin) {
		set("weixin", weixin);
		return (M)this;
	}
	
	/**
	 * 微信号
	 */
	@JBoltField(name="weixin" ,columnName="weixin",type="String", remark="微信号", required=false, maxLength=255, fixed=0, order=30)
	public java.lang.String getWeixin() {
		return getStr("weixin");
	}

	/**
	 * 更新时间
	 */
	public M setUpdateTime(java.util.Date updateTime) {
		set("update_time", updateTime);
		return (M)this;
	}
	
	/**
	 * 更新时间
	 */
	@JBoltField(name="updateTime" ,columnName="update_time",type="Date", remark="更新时间", required=false, maxLength=19, fixed=0, order=31)
	public java.util.Date getUpdateTime() {
		return getDate("update_time");
	}

	/**
	 * 最后登录时间
	 */
	public M setLastLoginTime(java.util.Date lastLoginTime) {
		set("last_login_time", lastLoginTime);
		return (M)this;
	}
	
	/**
	 * 最后登录时间
	 */
	@JBoltField(name="lastLoginTime" ,columnName="last_login_time",type="Date", remark="最后登录时间", required=false, maxLength=19, fixed=0, order=32)
	public java.util.Date getLastLoginTime() {
		return getDate("last_login_time");
	}

	/**
	 * 首次授权时间
	 */
	public M setFirstAuthTime(java.util.Date firstAuthTime) {
		set("first_auth_time", firstAuthTime);
		return (M)this;
	}
	
	/**
	 * 首次授权时间
	 */
	@JBoltField(name="firstAuthTime" ,columnName="first_auth_time",type="Date", remark="首次授权时间", required=false, maxLength=19, fixed=0, order=33)
	public java.util.Date getFirstAuthTime() {
		return getDate("first_auth_time");
	}

	/**
	 * 最后一次更新授权时间
	 */
	public M setLastAuthTime(java.util.Date lastAuthTime) {
		set("last_auth_time", lastAuthTime);
		return (M)this;
	}
	
	/**
	 * 最后一次更新授权时间
	 */
	@JBoltField(name="lastAuthTime" ,columnName="last_auth_time",type="Date", remark="最后一次更新授权时间", required=false, maxLength=19, fixed=0, order=34)
	public java.util.Date getLastAuthTime() {
		return getDate("last_auth_time");
	}

	/**
	 * 首次登录时间
	 */
	public M setFirstLoginTime(java.util.Date firstLoginTime) {
		set("first_login_time", firstLoginTime);
		return (M)this;
	}
	
	/**
	 * 首次登录时间
	 */
	@JBoltField(name="firstLoginTime" ,columnName="first_login_time",type="Date", remark="首次登录时间", required=false, maxLength=19, fixed=0, order=35)
	public java.util.Date getFirstLoginTime() {
		return getDate("first_login_time");
	}

	/**
	 * 绑定其他用户信息
	 */
	public M setBindUser(java.lang.String bindUser) {
		set("bind_user", bindUser);
		return (M)this;
	}
	
	/**
	 * 绑定其他用户信息
	 */
	@JBoltField(name="bindUser" ,columnName="bind_user",type="String", remark="绑定其他用户信息", required=false, maxLength=255, fixed=0, order=36)
	public java.lang.String getBindUser() {
		return getStr("bind_user");
	}

}

