package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseSpotcheckformdLine;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 制造管理-点检表明细列值
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "PL_SpotCheckFormD_Line" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class SpotcheckformdLine extends BaseSpotcheckformdLine<SpotcheckformdLine> {
}

