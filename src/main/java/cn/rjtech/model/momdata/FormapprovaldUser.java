package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseFormapprovaldUser;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 系统设置-单据审批审批用户
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_FormApprovalD_User" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class FormapprovaldUser extends BaseFormapprovaldUser<FormapprovaldUser> {
}

