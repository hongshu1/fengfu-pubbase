package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseInventoryMfgInfo;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 料品生产档案
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_InventoryMfgInfo" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class InventoryMfgInfo extends BaseInventoryMfgInfo<InventoryMfgInfo> {
}
