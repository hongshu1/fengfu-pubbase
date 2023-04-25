package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseStockoutqcformdLine;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 质量管理-出库检明细列值
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "PL_StockoutQcFormD_Line" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class StockoutqcformdLine extends BaseStockoutqcformdLine<StockoutqcformdLine> {
}

