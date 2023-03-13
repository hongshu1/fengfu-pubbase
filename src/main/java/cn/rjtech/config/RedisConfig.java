package cn.rjtech.config;

import cn.jbolt.core.base.config.JBoltConfig;

/**
 * Redis配置
 *
 * @author Kephon
 */
public class RedisConfig {

    public static String getRedisCacheName1() {
        return JBoltConfig.prop.get("redis.cache1");
    }

    public static String getRedisCacheName2() {
        return JBoltConfig.prop.get("redis.cache2");
    }

    public static String getRedisHost() {
        return JBoltConfig.prop.get("redis.host");
    }

    public static String getRedisPassword() {
        return JBoltConfig.prop.get("redis.password");
    }

}
