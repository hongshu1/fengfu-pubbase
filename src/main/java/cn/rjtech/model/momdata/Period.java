package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BasePeriod;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 期间管理
 * Generated by JBolt.
 */
@TableBind(dataSource = "mes" , table = "Bas_Period" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Period extends BasePeriod<Period> {
}

