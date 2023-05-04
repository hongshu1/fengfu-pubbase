package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseSubcontractOrderM;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 采购/委外订单-采购订单主表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "PS_SubcontractOrderM" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class SubcontractOrderM extends BaseSubcontractOrderM<SubcontractOrderM> {
    // 根据存货id记录中间变数据
    public static final String PURCHASEORDERREFLIST = "purchaseOrderRefList";
    
    public static final String ARR = "arr";
    
    public static final String ORDERSTATUS = "orderStatus";
    
    public static final String ADUITSTATUSTEXT = "aduitStatusText";
    
    public static final String PAYABLETYPETEXT = "payableTypeText";
    
    public static final String BUSTYPETEXT = "busTypeText";
}

