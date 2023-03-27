package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseAnnualOrderM;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 客户订单-年度订单
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Co_AnnualOrderM" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class AnnualOrderM extends BaseAnnualOrderM<AnnualOrderM> {
}

