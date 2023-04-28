package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseApsWeekscheduledQty;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 生产计划-月周生产计划数量
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Aps_WeekScheduleD_Qty" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class ApsWeekscheduledQty extends BaseApsWeekscheduledQty<ApsWeekscheduledQty> {
}
