package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseCustomer;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 往来单位-客户档案
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_Customer" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Customer extends BaseCustomer<Customer> {
}
