package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseManualOrderD;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 客户订单-手配订单明细
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Co_ManualOrderD" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class ManualOrderD extends BaseManualOrderD<ManualOrderD> {
}

