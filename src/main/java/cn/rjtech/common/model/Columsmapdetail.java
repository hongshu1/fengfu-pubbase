package cn.rjtech.common.model;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.common.model.base.BaseColumsmapdetail;

/**
 * 字段映射表明细
 * Generated by JBolt.
 */
@TableBind(dataSource = "momdata" , table = "T_Sys_ColumsMapDetail" , primaryKey = "AutoID" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Columsmapdetail extends BaseColumsmapdetail<Columsmapdetail> {
}

