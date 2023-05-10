package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseSysPuinstore;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 采购入库单
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "T_Sys_PUInStore" , primaryKey = "AutoID" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class SysPuinstore extends BaseSysPuinstore<SysPuinstore> {
}
