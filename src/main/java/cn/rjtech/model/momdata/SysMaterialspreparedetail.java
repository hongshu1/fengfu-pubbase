package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseSysMaterialspreparedetail;

/**
 * 备料单明细
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "T_Sys_MaterialsPrepareDetail" , primaryKey = "AutoID" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class SysMaterialspreparedetail extends BaseSysMaterialspreparedetail<SysMaterialspreparedetail> {
}

