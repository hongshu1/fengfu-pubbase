package cn.rjtech.constants;

import cn.jbolt.core.util.JBoltRealUrlUtil;
import cn.rjtech.config.AppConfig;

/**
 * @Description 通用的静态参数类
 * @Author Create by Alvin
 * @Date 2020-09-21 13:42
 */
public class Constants {

    private Constants() {
        // ignored
    }

    /**
     * 获取环境名称
     */
    public static String getProdEnv(boolean isProdEnv) {
        return isProdEnv ? "生产环境" : "测试环境";
    }

    /**
     * 获取资源访问链接
     */
    public static String getResUrl(String uri) {
        return JBoltRealUrlUtil.get(uri);
    }

    /**
     * 获取http://访问的地址
     */
    public static String getFullUrl(String path) {
        return String.format("http://%s/%s", AppConfig.getDomain(), getResUrl(path));
    }

}
