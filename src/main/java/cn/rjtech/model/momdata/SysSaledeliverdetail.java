package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseSysSaledeliverdetail;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 销售出库明细
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "T_Sys_SaleDeliverDetail" , primaryKey = "AutoID" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class SysSaledeliverdetail extends BaseSysSaledeliverdetail<SysSaledeliverdetail> {
}

