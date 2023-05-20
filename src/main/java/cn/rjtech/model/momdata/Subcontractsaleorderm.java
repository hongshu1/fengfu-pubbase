package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseSubcontractsaleorderm;

/**
 * 客户订单-委外销售订单主表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Co_SubcontractSaleOrderM" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Subcontractsaleorderm extends BaseSubcontractsaleorderm<Subcontractsaleorderm> {
}

