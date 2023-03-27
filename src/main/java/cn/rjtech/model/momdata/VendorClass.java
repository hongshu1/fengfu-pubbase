package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseVendorClass;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 往来单位-供应商分类
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_VendorClass" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class VendorClass extends BaseVendorClass<VendorClass> {
}
