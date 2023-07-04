package cn.rjtech.u8.stockapi.otherinunconfirm;

import cn.rjtech.config.AppConfig;

/**
 * StockApi 常量
 *
 * @author Kephon
 */
public class StockApiConstants {

    /**
     * U8 API 访问地址
     */
    static String BASE = AppConfig.getStockApiUrl();

    public static final String OTHER_IN_UNCONFIRM_V1 = BASE + "?op=OtherInUnConfirmV1";
    
    private StockApiConstants() {
    }

}
