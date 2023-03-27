package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseInventoryClass;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 物料建模-存货分类
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_InventoryClass" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class InventoryClass extends BaseInventoryClass<InventoryClass> {
}
