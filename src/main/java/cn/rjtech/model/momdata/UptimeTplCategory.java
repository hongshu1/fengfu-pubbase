package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseUptimeTplCategory;

/**
 * 稼动时间建模-稼动时间模板类别
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_UptimeTplCategory" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class UptimeTplCategory extends BaseUptimeTplCategory<UptimeTplCategory> {
}

