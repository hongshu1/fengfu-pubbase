package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseApsAnnualplanm;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 生产计划-年度计划排产主表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Aps_AnnualPlanM" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class ApsAnnualplanm extends BaseApsAnnualplanm<ApsAnnualplanm> {
}

