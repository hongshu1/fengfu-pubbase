package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseSpecMaterialsRcvD;

/**
 * 制造工单-特殊领料单明细
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Mo_SpecMaterialsRcvD" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class SpecMaterialsRcvD extends BaseSpecMaterialsRcvD<SpecMaterialsRcvD> {
}

