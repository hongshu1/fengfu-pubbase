package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseUptimeD;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 制造管理-稼动时间记录明细表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "PL_UptimeD" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class UptimeD extends BaseUptimeD<UptimeD> {
}

