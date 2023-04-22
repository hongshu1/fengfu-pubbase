package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BasePurchaseOrderDBatchVersion;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 采购/委外管理-采购现品票版本记录
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "PS_PurchaseOrderDBatchVersion" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class PurchaseOrderDBatchVersion extends BasePurchaseOrderDBatchVersion<PurchaseOrderDBatchVersion> {
}

