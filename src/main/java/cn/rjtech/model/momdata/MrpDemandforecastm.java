package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseMrpDemandforecastm;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 需求计划管理-物料需求预示主表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Mrp_DemandForecastM" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class MrpDemandforecastm extends BaseMrpDemandforecastm<MrpDemandforecastm> {
}

