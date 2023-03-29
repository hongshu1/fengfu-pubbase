package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseInventoryRoutingInvc;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 物料建模-存货工艺工序物料集
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_InventoryRoutingInvc" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class InventoryRoutingInvc extends BaseInventoryRoutingInvc<InventoryRoutingInvc> {
}

