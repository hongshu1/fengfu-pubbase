package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseWarehousePosition;

/**
 * 库位档案
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_Warehouse_Position" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class WarehousePosition extends BaseWarehousePosition<WarehousePosition> {
}

