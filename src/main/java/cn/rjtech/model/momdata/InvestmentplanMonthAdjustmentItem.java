package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseInvestmentplanMonthAdjustmentItem;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 
 * Generated by JBolt.
 */
@TableBind(dataSource = "momdata" , table = "PL_InvestmentPlan_Month_Adjustment_Item" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class InvestmentplanMonthAdjustmentItem extends BaseInvestmentplanMonthAdjustmentItem<InvestmentplanMonthAdjustmentItem> {
}
