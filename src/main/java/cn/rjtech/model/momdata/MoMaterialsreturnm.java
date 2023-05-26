package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseMoMaterialsreturnm;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 制造工单-生产退料主表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Mo_MaterialsReturnM" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class MoMaterialsreturnm extends BaseMoMaterialsreturnm<MoMaterialsreturnm> {
}
