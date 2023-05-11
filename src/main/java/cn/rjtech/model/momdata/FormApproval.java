package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseFormApproval;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 系统设置-表单审批流
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_FormApproval" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class FormApproval extends BaseFormApproval<FormApproval> {
}
