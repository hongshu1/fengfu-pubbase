package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseSubcontractOrderD;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 采购/委外订单-采购订单明细
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "PS_SubcontractOrderD" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class SubcontractOrderD extends BaseSubcontractOrderD<SubcontractOrderD> {
}

