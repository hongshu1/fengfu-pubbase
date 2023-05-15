package cn.rjtech.config;

import cn.jbolt.core.base.config.JBoltConfig;

/**
 * 应用配置
 *
 * @author Kephon
 */
public class AppConfig {

    public static String getU8ApiUrl() {
        return JBoltConfig.prop.get("u8api.url");
    }

    /**
     * 是否为调试环境
     */
    public static boolean isDebugEnv() {
        return JBoltConfig.prop.getBoolean("u8.debug_env");
    }

    /**
     * 是否启用U8接口调用
     */
    public static boolean isU8ApiEnabled() {
        return JBoltConfig.prop.getBoolean("u8.api.enabled");
    }

    public static String getServerApiKey() {
        return JBoltConfig.prop.get("server.api.key");
    }

    public static String getServerBindHost() {
        return JBoltConfig.prop.get("server.bind.host");
    }

    public static String getDomain() {
        return JBoltConfig.prop.get("domain");
    }

    public static String getPrintUrl() {
        return JBoltConfig.prop.get("print.url");
    }

    public static String getPrintFilePath() {
        return JBoltConfig.prop.get("printFilePath");
    }

    public static String getPrintAddressUrl() {
        return JBoltConfig.prop.get("printaddress.url");
    }

    public static String getJdbcUrl() {
        return JBoltConfig.prop.get("jdbc.url");
    }

    /**
     * U9推单地址
     */
    public static String getVouchSumbmitUrl() {
        return JBoltConfig.prop.get("u9.api.url") + "/web/erp/common/vouchProcessSubmit";
    }

    /**
     * U9推单地址(推送采购入库单到U8系统)
     */
    public static String getVouchProcessDynamicSubmitUrl() {
        return JBoltConfig.prop.get("u8.api.url") + "/web/erp/common/vouchProcessDynamicSubmit";
    }

}
