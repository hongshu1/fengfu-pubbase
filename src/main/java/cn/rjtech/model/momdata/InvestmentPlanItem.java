package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseInvestmentPlanItem;

/**
 * 投资计划项目表
 * Generated by JBolt.
 */
@TableBind(dataSource = "momdata" , table = "PL_Investment_Plan_Item" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class InvestmentPlanItem extends BaseInvestmentPlanItem<InvestmentPlanItem> {
}

