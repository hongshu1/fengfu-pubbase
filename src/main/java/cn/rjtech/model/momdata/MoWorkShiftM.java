package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseMoWorkShiftM;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 制造工单-指定日期班次人员信息主表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Mo_MoWorkShiftM" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class MoWorkShiftM extends BaseMoWorkShiftM<MoWorkShiftM> {
}

