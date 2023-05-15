package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseMoPickItemD;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 生产订单-备料单明细
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Mo_MoPickItemD" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class MoPickItemD extends BaseMoPickItemD<MoPickItemD> {
}

