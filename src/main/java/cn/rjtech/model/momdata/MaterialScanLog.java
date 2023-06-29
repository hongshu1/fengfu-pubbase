package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseMaterialScanLog;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 制造管理-制造工单齐料明细
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Mo_MaterialScanLog" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class MaterialScanLog extends BaseMaterialScanLog<MaterialScanLog> {
}

