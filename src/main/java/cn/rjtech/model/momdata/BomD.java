package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseBomD;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 物料建模-BOM明细
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_BomD" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class BomD extends BaseBomD<BomD> {
    // 源id
    public static final String SOURCEID = "sourceId";
    
}
