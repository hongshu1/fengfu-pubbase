package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseStockcheckvouchdetail;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 盘点单明细表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "T_Sys_StockCheckVouchDetail" , primaryKey = "AutoID" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Stockcheckvouchdetail extends BaseStockcheckvouchdetail<Stockcheckvouchdetail> {
}

