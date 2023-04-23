package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BasePurchaseOrderM;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 采购/委外订单-采购订单主表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "PS_PurchaseOrderM" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class PurchaseOrderM extends BasePurchaseOrderM<PurchaseOrderM> {
    
    public static final String IAFTERINVENTORYID = "iAfterInventoryId";
    
    public static final String AFTERCINVCODE = "afterCInvCode";
    
    public static final String AFTERCINVCODE1 = "afterCInvCode1";
    
    public static final String AFTERCINVNAME = "afterCInvName";
    
    public static final String AFTERCINVNAME1 = "afterCInvName1";
   
    // 转换后： 包装数量
    public static final String IPKGQTY = "iPkgQty";
    
    public static final String CINVSTD = "cInvStd";
    
    public static final String CUOMNAME = "cUomName";
    // 根据存货id记录中间变数据
    public static final String PURCHASEORDERREFLIST = "purchaseOrderRefList";
    
    public static final String ARR = "arr";
   
    public static final String ORDERSTATUS = "orderStatus";
    
    public static final String ADUITSTATUSTEXT = "aduitStatusText";
    
    public static final String PAYABLETYPETEXT = "payableTypeText";
    
    public static final String BUSTYPETEXT = "busTypeText";
    
}

