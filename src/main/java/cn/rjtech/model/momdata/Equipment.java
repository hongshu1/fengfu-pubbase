package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.rjtech.model.momdata.base.BaseEquipment;

/**
 * 设备管理-设备档案
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_Equipment" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Equipment extends BaseEquipment<Equipment> {
    public String getStateName() {
        return JBoltDictionaryCache.me.getNameBySn("healthy_type", getIStatus()+"");
    }

}

