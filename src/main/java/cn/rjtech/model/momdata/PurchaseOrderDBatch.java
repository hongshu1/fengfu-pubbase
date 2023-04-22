package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BasePurchaseOrderDBatch;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 采购/委外管理-采购现品票
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "PS_PurchaseOrderDBatch" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class PurchaseOrderDBatch extends BasePurchaseOrderDBatch<PurchaseOrderDBatch> {
}

