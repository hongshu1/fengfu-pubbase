package cn.rjtech.common.model;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.common.model.base.BaseBarcodeencoding;

/**
 *
 * Generated by JBolt.
 */
@TableBind(dataSource = "momdata" , table = "T_Sys_BarCodeEncoding" , primaryKey = "AutoID" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Barcodeencoding extends BaseBarcodeencoding<Barcodeencoding> {
}

