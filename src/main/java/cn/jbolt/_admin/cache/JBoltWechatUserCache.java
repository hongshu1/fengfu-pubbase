package cn.jbolt._admin.cache;
import cn.jbolt._admin.user.UserExtendService;
import cn.jbolt.admin.wechat.user.WechatUserService;
import cn.jbolt.common.model.UserExtend;
import cn.jbolt.common.model.WechatUser;
import cn.jbolt.core.base.config.JBoltConfig;
import cn.jbolt.core.cache.JBoltCache;
import cn.jbolt.core.cache.JBoltCacheKit;
import com.jfinal.aop.Aop;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.IDataLoader;

/**
 * 微信用户
 * 缓存工具类
 */
public class JBoltWechatUserCache extends JBoltCache {
    public static final JBoltWechatUserCache me = new JBoltWechatUserCache();
    WechatUserService service = Aop.get(WechatUserService.class);
    private static final String TYPE_NAME = "wechat_user";
    private static final String PRE_API = "api_";
    public String getCacheTypeName() {
        return TYPE_NAME;
    }

    /**
     * 从缓存里获取wechatUser
     * @param mpId
     * @param id
     * @return
     */
    public WechatUser getApiWechatUserByApiUserId(Long mpId, Object id) {
        return JBoltCacheKit.get(JBoltConfig.JBOLT_CACHE_NAME, buildCacheKey(WechatUser.class,PRE_API,mpId.toString(),id.toString()), new IDataLoader() {
            @Override
            public Object load() {
                WechatUser wechatUser = service.findByIdToWechatUser(mpId, id);
                if (wechatUser != null) {
                    wechatUser.removeNullValueAttrs().remove("last_auth_time","first_auth_time","first_login_time","last_login_time","checked_time","remark","group_id","tag_ids","subscribe_scene","qr_scene","qr_scene_str","check_code","session_key","update_time");
                }
                return wechatUser;
            }
        });
    }

    /**
     * 删除wechatUser
     * @param mpId
     * @param id
     */
    public void removeApiWechatUser(Long mpId, Long id) {
        JBoltCacheKit.remove(JBoltConfig.JBOLT_CACHE_NAME, buildCacheKey(WechatUser.class,PRE_API,mpId.toString(),id.toString()));
    }

    /**
     * 从缓存里获取wechatUser 昵称
     * @param mpId
     * @param id
     * @return
     */
    public String getNickName(Long mpId, Long id) {
        WechatUser apiUser = getApiWechatUserByApiUserId(mpId, id);
        return apiUser==null?null:apiUser.getNickname();
    }

    /**
     * 从缓存里获取wechatUser 头像
     * @param mpId
     * @param id
     * @return
     */
    public String getAvatar(Long mpId, Long id) {
        WechatUser apiUser = getApiWechatUserByApiUserId(mpId, id);
        return apiUser==null?null:apiUser.getHeadImgUrl();
    }





    /**
     * 根据mpid和openId拿到wechat用户信息 api专用
     * @param mpId
     * @param openId
     * @return
     */
    public WechatUser getApiWechatUserByMpOpenId(Long mpId, String openId) {
        if (mpId == null || StrKit.isBlank(openId)) {
            return null;
        }
        return JBoltCacheKit.get(JBoltConfig.JBOLT_CACHE_NAME,buildCacheKey(WechatUser.class,"api_byopenid_", mpId.toString() , openId), new IDataLoader() {
                    @Override
                    public Object load() {
                        return service.getApiWechatUserByOpenId(mpId, openId);
                    }
                });
    }

    /**
     * 根据mpid和openId删除wechat用户信息 api专用
     *
     * @param mpId
     * @param openId
     */
    public void removeApiWechatUserByMpOpenId(Long mpId, String openId) {
        if (mpId != null && StrKit.notBlank(openId)) {
            JBoltCacheKit.remove(JBoltConfig.JBOLT_CACHE_NAME, buildCacheKey(WechatUser.class,"api_byopenid_", mpId.toString() , openId));
        }
    }


    /**
     * 根据mpid和unionId拿到wechat用户信息 api专用
     *
     * @param mpId
     * @param unionId
     * @return
     */
    public WechatUser getApiWechatUserByMpUnionId(Long mpId, String unionId) {
        if (mpId == null || StrKit.isBlank(unionId)) {
            return null;
        }
        return JBoltCacheKit.get(JBoltConfig.JBOLT_CACHE_NAME,
                buildCacheKey(WechatUser.class,"api_byunionid_", mpId.toString(), unionId), new IDataLoader() {
                    @Override
                    public Object load() {
                        return service.getApiWechatUserByUnionId(mpId, unionId);
                    }
                });
    }

    /**
     * 根据mpid和unionId删除wechat用户信息 api专用
     *
     * @param mpId
     * @param unionId
     */
    public void removeApiWechatUserByMpUnionId(Long mpId, String unionId) {
        if (mpId != null && StrKit.notBlank(unionId)) {
            JBoltCacheKit.remove(JBoltConfig.JBOLT_CACHE_NAME,
                    buildCacheKey(WechatUser.class,"api_byunionid_", mpId.toString(), unionId));
        }
    }

    /**
     * 是否enable
     * @param mpId
     * @param id
     * @return
     */
    public Boolean isEnable(Long mpId, Long id) {
        WechatUser user = getApiWechatUserByApiUserId(mpId, id);
        return user == null ? false : user.getEnable();
    }

}
