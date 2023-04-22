package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseMonthorderd;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 客户订单-月度计划订单
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Co_MonthOrderD" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Monthorderd extends BaseMonthorderd<Monthorderd> {
    /**月份*/
    private String iMonth;

    /**
     * 月份
     */
    public void setIMonth(java.lang.String iMonth) {
        this.iMonth = iMonth;
    }

    /**
     * 月份
     */
    public java.lang.String getIMonth() {
        return iMonth;
    }
}

