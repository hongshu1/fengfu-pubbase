package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.JBoltAutoCache;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.main.base.BaseWorkclass;

/**
 * 工种档案
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@JBoltAutoCache(keyCache = true, column = "cWorkClassCode")
@TableBind(dataSource = "momdata" , table = "Bd_WorkClass" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Workclass extends BaseWorkclass<Workclass> {
}

