
package cn.jbolt._admin.globalconfig;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cn.jbolt.core.service.JBoltGlobalConfigService;
import cn.rjtech.config.MesConfigKey;
import cn.rjtech.util.ValidationUtils;

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
    /**
     * 获取税率配置
     */
    public BigDecimal getTaxRate() {
        String configValue = getConfigValue(MesConfigKey.TAX_RATE);
        ValidationUtils.notNull(configValue, "系统参数缺少税率配置项");
        BigDecimal itaxrate = new BigDecimal(configValue);
        //税率在全局参数中录入1-100的正整数，获取参与计算的税率需要除以100
        itaxrate = itaxrate.divide(BigDecimal.valueOf(100L),2,RoundingMode.HALF_UP);
        ValidationUtils.isTrue(itaxrate.compareTo(BigDecimal.valueOf(0.5)) < 0, "税率超过了50%");
        return itaxrate;
    }
}
