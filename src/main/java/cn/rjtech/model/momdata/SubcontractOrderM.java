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
}

