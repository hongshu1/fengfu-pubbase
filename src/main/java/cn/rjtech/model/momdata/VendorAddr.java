package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseVendorAddr;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 供应商档案-联系地址
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_VendorAddr" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class VendorAddr extends BaseVendorAddr<VendorAddr> {
}

