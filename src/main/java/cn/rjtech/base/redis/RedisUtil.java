package cn.rjtech.base.redis;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.model.User;
import cn.rjtech.config.RedisConfig;
import com.jfinal.log.Log;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

import java.util.Set;

/**
 * @Description
 * @Author Create by Alvin
 * @Date 2020-10-27 13:58
 */
public class RedisUtil {

    /**
     * 2小时有效
     */
    public static final int EXPIRES_IN_2_HOURS = 2 * 60 * 60;
    /**
     * 12小时有效
     */
    public static final int EXPIRES_IN_12_HOURS = 12 * 60 * 60;
    /**
     * 微信小程序Session key过期时间
     */
    public static final int EXPIRES_IN_15_DAYS = 60 * 60 * 24 * 15;
    protected static final Cache CACHE = Redis.use(RedisConfig.getRedisCacheName2());
    private static final Log LOG = Log.getLog(RedisUtil.class);
    /**
     * 1分钟
     */
    private static final int EXPIRES_IN_1_MIN = 60;
    /**
     * 短信验证码有效期
     */
    private static final int EXPIRES_IN_5_MINS = 5 * 60;
    /**
     * 15秒
     */
    private static final int EXPIRES_IN_15_SEC = 15;

    private RedisUtil() {
        // ignored
    }

    /**
     * 自增缓存值，不存在则初始化为0再自增, 即默认返回1
     */
    public static Long incr(String key) {
        return CACHE.incr(key);
    }

    /**
     * 获取缓存内容
     */
    public static <T> T get(String key) {
        return CACHE.get(key);
    }

    /**
     * 设置key的过期时间
     */
    public static Long expire(String key, int time) {
        return CACHE.expire(key, time);
    }

    public static boolean exists(String key) {
        return CACHE.exists(key);
    }

    public static void del(String key) {
        LOG.info("del key: {}", key);
        CACHE.del(key);
    }

    public static void setEx(String key, Object v, int expiresIn) {
        CACHE.setex(key, expiresIn, v);
    }

    public static <T> T getByPattern(String pattern) {
        Set<String> keys = getKeys(pattern);
        if (CollUtil.isEmpty(keys)) {
            return null;
        }
        return get(keys.iterator().next());
    }

    public static Set<String> getKeys(String pattern) {
        return CACHE.keys(pattern);
    }

    public static void removeByPattern(String pattern) {
        Set<String> keys = getKeys(pattern);
        if (CollUtil.isNotEmpty(keys)) {
            for (String key : keys) {
                del(key);
            }
        }
    }

    public static void setUser(String authCode, User user) {
        setEx(RedisKeys.getUserKey(authCode), user, EXPIRES_IN_5_MINS);
    }

    public static User getUser(String authCode) {
        return get(RedisKeys.getUserKey(authCode));
    }

    public static void removeUser(String authCode) {
        CACHE.del(RedisKeys.getUserKey(authCode));
    }

}
