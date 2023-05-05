package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseStockoutDefect;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 质量管理-出库异常品记录
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "PL_StockoutDefect" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class StockoutDefect extends BaseStockoutDefect<StockoutDefect> {
}

