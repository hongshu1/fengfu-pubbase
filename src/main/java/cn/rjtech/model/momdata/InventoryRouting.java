package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseInventoryRouting;

/**
 * 物料建模-存货工艺档案
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_InventoryRouting" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class InventoryRouting extends BaseInventoryRouting<InventoryRouting> {
    public static final String CAUDITSTATUSTEXT = "cAuditStatusText";
}

