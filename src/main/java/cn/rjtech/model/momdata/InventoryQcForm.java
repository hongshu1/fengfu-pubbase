package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseInventoryQcForm;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 质量建模-检验适用标准
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_InventoryQcForm" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class InventoryQcForm extends BaseInventoryQcForm<InventoryQcForm> {
}
