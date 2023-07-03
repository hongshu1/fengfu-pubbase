package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseStockCheckVouchBarcode;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 库存盘点-条码明细
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "T_Sys_StockCheckVouchBarcode" , primaryKey = "AutoID" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class StockCheckVouchBarcode extends BaseStockCheckVouchBarcode<StockCheckVouchBarcode> {
}
