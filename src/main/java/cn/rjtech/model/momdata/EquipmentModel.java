package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseEquipmentModel;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 物料建模-机型档案
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_EquipmentModel" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class EquipmentModel extends BaseEquipmentModel<EquipmentModel> {
}

