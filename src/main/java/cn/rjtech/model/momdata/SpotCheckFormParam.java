package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseSpotCheckFormParam;

/**
 * 质量建模-点检表格参数
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_SpotCheckFormParam" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class SpotCheckFormParam extends BaseSpotCheckFormParam<SpotCheckFormParam> {
}

