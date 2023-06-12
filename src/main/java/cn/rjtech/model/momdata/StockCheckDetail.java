package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseStockCheckDetail;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 库存盘点单-盘点明细表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "T_Sys_StockCheckDetail" , primaryKey = "AutoID" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class StockCheckDetail extends BaseStockCheckDetail<StockCheckDetail> {
}
