package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseInventoryRoutingConfig;

/**
 * 物料建模-存货工艺配置
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_InventoryRoutingConfig" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class InventoryRoutingConfig extends BaseInventoryRoutingConfig<InventoryRoutingConfig> {
    // 工序物料集
    public static final String ITEMJSONSTR = "itemJsonStr";
    // 工序物料集json
    public static final String ITEMJSON = "itemJson";
    // 设备集
    public static final String EQUIPMENTJSONSTR = "equipmentJsonStr";
    
    public static final String EQUIPMENTJSON = "equipmentJson";
    // 作业指导书
    public static final String ROUTINGSOPJSONSTR = "routingSopJsonStr";
    
    public static final String ROUTINGSOPJSON = "routingSopJson";

    // 是否新增
    public static final String ISADD = "isAdd";
    
    private boolean isAdd;
    
    public boolean getIsAdd() {
        return this.isAdd;
    }
    
    public void setIsAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }

    // 人员集
    public static final String PERSONEQUIPMENTJSONSTR = "personequipmentjsonstr";

    public static final String PERSONEQUIPMENTJSON = "personequipmentjson";
    
    // 设备集
//    public static final String
}

