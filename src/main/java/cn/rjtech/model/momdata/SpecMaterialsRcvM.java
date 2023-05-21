package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseSpecMaterialsRcvM;

/**
 * 制造工单-特殊领料单主表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Mo_SpecMaterialsRcvM" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class SpecMaterialsRcvM extends BaseSpecMaterialsRcvM<SpecMaterialsRcvM> {
}

