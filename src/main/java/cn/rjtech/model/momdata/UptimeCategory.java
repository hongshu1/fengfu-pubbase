package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseUptimeCategory;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 稼动时间建模-稼动时间参数类别
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_UptimeCategory" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class UptimeCategory extends BaseUptimeCategory<UptimeCategory> {
}

