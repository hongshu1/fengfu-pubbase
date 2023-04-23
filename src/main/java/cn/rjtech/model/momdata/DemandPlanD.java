package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseDemandPlanD;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 需求计划管理-到货计划细表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Mrp_DemandPlanD" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class DemandPlanD extends BaseDemandPlanD<DemandPlanD> {
}
