package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseBdForgeigncurrency;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 币种档案
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_ForgeignCurrency" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class BdForgeigncurrency extends BaseBdForgeigncurrency<BdForgeigncurrency> {
}

