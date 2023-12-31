package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.JBoltAutoCache;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseUom;

/**
 * 计量单位
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@JBoltAutoCache(keyCache = true,column = "cUomCode")
@TableBind(dataSource = "momdata" , table = "Bd_Uom" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Uom extends BaseUom<Uom> {
}

