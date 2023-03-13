
package cn.jbolt._admin.globalconfig;

import cn.jbolt.core.service.JBoltGlobalConfigService;

/**
 * 全局配置 service
 *
 * @author 小木 qq:909854136
 * @version 创建时间：2018年12月25日 下午11:18:26
 */
public class GlobalConfigService extends JBoltGlobalConfigService {

    public String getConfigValue(String configKey) {
        return queryColumn("SELECT config_value FROM jb_global_config WHERE config_key = ? ", configKey);
    }

}
