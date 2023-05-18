package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseTransVouch;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 调拨单
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "T_Sys_TransVouch" , primaryKey = "AutoID" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class TransVouch extends BaseTransVouch<TransVouch> {
}

