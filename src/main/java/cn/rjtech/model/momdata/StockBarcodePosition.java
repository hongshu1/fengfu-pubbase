package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseStockBarcodePosition;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 条码库存表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "T_Sys_StockBarcodePosition" , primaryKey = "AutoID" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class StockBarcodePosition extends BaseStockBarcodePosition<StockBarcodePosition> {
}
