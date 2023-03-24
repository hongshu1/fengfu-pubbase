package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseInventoryAddition;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 存货档案-附加
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_InventoryAddition" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class InventoryAddition extends BaseInventoryAddition<InventoryAddition> {
}

