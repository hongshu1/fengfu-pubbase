package cn.rjtech.config;

import com.jfinal.aop.Aop;

import cn.jbolt._admin.globalconfig.GlobalConfigService;
import cn.jbolt.core.base.config.JBoltConfig;
import cn.jbolt.core.db.datasource.JBoltDataSourceUtil;
import cn.jbolt.core.model.GlobalConfig;
import cn.rjtech.constants.DataSourceConstants;

/**
 * 应用配置
 *
 * @author Kephon
 */
public class AppConfig {
	private static final GlobalConfigService GLOBAL_CONFIG_SERVICE = Aop.get(GlobalConfigService.class);
	
	
    /**
     * 基础库名
     */
    public static String getJBoltDbName() {
        return JBoltDataSourceUtil.me.getJBoltDatasource(DataSourceConstants.MAIN).getDbName();
    }	
	
    /**
     * MES库名
     */
    public static String getMesDbName() {
        return JBoltDataSourceUtil.me.getJBoltDatasource(DataSourceConstants.MOMDATA).getDbName();
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
     * U8推单地址(推送采购入库单到U8系统)
     */
    public static String getVouchProcessDynamicSubmitUrl() {
        String url = "http://localhost:8081/web/erp/common/vouchProcessDynamicSubmit";
        //JBoltConfig.prop.get("u8.api.url") + "/api/erp/common/vouchProcessDynamicSubmit"
        return url;
    }
    /**
     * 是否启用审批流
     */
    public static Boolean isVerifyProgressEnabled() {
    	GlobalConfig globalConfig = GLOBAL_CONFIG_SERVICE.getByConfigKey("verify_progress_enabled");
    	if(globalConfig == null) return true;
    	return globalConfig.getBoolean("config_value");
    }

}
