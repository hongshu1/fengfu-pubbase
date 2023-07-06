package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseBommTrl;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 物料建模-BOM扩展
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_BomM_Trl" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class BommTrl extends BaseBommTrl<BommTrl> {
}
