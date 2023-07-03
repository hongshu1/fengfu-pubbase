package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BasePurchaseAttachment;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 申购单附件表
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "PL_Purchase_Attachment" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class PurchaseAttachment extends BasePurchaseAttachment<PurchaseAttachment> {
}
