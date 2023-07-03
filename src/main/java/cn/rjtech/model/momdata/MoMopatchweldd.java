package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseMoMopatchweldd;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 制造工单-补焊记录明细表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Mo_MoPatchWeldD" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class MoMopatchweldd extends BaseMoMopatchweldd<MoMopatchweldd> {
}
