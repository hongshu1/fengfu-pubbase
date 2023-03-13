package cn.rjtech.base.redis;

import cn.rjtech.util.ValidationUtils;

/**
 * @Description
 * @Author Create by Alvin
 * @Date 2020-10-27 13:58
 */
public class RedisKeys {
    /**
     * 分割字符
     */
    private static final String SEPERATOR = "-";
    /**
     * 重启脚本限制时间
     */
    public static final String RESTART_TIME = "LH:RT";
    /**
     * 请求限制key
     */
    private static final String REQUEST_LIMIT_PREFIX = "request_limit_";

    private RedisKeys() {
        // ignored
    }

    public static String getRequestLimitKey(String uri, String ip) {
        ValidationUtils.notBlank(uri, "参数uri为空");
        ValidationUtils.notBlank(ip, "缺少IP参数");

        return REQUEST_LIMIT_PREFIX + uri + ip;
    }

    public static String getUserKey(String authCode) {
        return "user_key:" + authCode;
    }

    /**
     * 异常的key
     */
    public static final class Error {
        /**
         * 数据库异常
         */
        public static final String DB = "DB";
        /**
         * 数据重复错误
         */
        public static final String DUPLICATE_KEY = "DUPLICATE_KEY";
        /**
         * 全局异常
         */
        public static final String GLOBAL = "GLOBAL";

        private Error() {
            // ignored
        }
    }

}

