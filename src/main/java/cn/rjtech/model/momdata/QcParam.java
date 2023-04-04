package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseQcParam;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 质量建模-检验参数
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_QcParam" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class QcParam extends BaseQcParam<QcParam> {
}

