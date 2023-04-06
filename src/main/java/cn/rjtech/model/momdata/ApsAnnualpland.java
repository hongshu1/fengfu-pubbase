package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseApsAnnualpland;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 生产计划-年度计划排产明细
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Aps_AnnualPlanD" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class ApsAnnualpland extends BaseApsAnnualpland<ApsAnnualpland> {
}

