package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseApsWeekscheduledetails;

/**
 * 生产计划-月周生产计划排产明细
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Aps_WeekScheduleDetails" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class ApsWeekscheduledetails extends BaseApsWeekscheduledetails<ApsWeekscheduledetails> {
}

