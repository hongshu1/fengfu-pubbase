package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseSubcontractSaleOrderD;

/**
 * 客户订单-委外销售订单明细
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Co_SubcontractSaleOrderD" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Subcontractsaleorderd extends BaseSubcontractSaleOrderD<Subcontractsaleorderd> {
}

