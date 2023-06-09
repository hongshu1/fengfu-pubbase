package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseStockCheck;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 库存盘点单主表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "T_Sys_StockCheck" , primaryKey = "AutoID" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class StockCheck extends BaseStockCheck<StockCheck> {
}

