package cn.rjtech.config;

import cn.jbolt.core.base.config.JBoltConfig;
import cn.jbolt.core.cache.JBoltGlobalConfigCache;

/**
 * 应用配置
 *
 * @author Kephon
 */
public class AppConfig {

    /**
     * 应用端口号
     */
    public static String getServerPort() {
        return JBoltConfig.prop.get("server.port");
    }

    public static String getU8ApiUrl() {
        return JBoltConfig.prop.get("u8api.url");
    }

    /**
     * 是否为调试环境
     */
    public static boolean isDebugEnv() {
        return JBoltConfig.prop.getBoolean("u8.debug_env");
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
    public static String getVouchSubmitUrl() {
        return JBoltConfig.prop.get("vouch.api.url") + "/web/erp/common/vouchProcessSubmit";
    }

    /**
     * U8推单地址(推送采购入库单到U8系统)，web层用的
     */
    public static String getVouchProcessDynamicSubmitUrl() {
        return String.format("http://127.0.0.1:%s/web/erp/common/vouchProcessDynamicSubmit", getServerPort());
    }

    /**
     * 是否启用审批流
     */
    public static boolean isVerifyProgressEnabled() {
        return JBoltGlobalConfigCache.me.getBooleanConfigValue("verify_progress_enabled", true);
    }

    /**
     * U8推单地址(推送采购入库单到U8系统)，api接口用的
     */
    public static String getVouchProcessDynamicSubmitUrlToApi() {
        return String.format("http://127.0.0.1:%s/api/erp/common/vouchProcessDynamicSubmit", getServerPort());
    }

    /**
     * 通知U8弃审采购入库单
     */
    public static String inUnVouchProcessDynamicSubmitUrl() {
        return String.format("%s/PurchasesInUnConfirmV1", JBoltConfig.prop.get("u8.stock.api"));
    }

    /**
     * 通知U8删除采购入库单
     */
    public static String deleteVouchProcessDynamicSubmitUrl() {
        return String.format("%s/PurchasesInDeleteV1", JBoltConfig.prop.get("u8.stock.api"));
    }



    /**
     * U9推单地址
     */
    public static String getVouchSumbmitUrl() {
        return JBoltConfig.prop.get("u9.api.url") + "/web/erp/common/vouchProcessSubmit";
    }
}
