package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseMoMaterialsscansum;

/**
 * 制造管理-制造工单齐料汇总
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Mo_MaterialsScanSum" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class MoMaterialsscansum extends BaseMoMaterialsscansum<MoMaterialsscansum> {
}

