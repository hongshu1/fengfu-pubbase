package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseFormapprovaldRoleusers;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 系统配置-审批节点角色用户
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_FormApprovalD_RoleUsers" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class FormapprovaldRoleusers extends BaseFormapprovaldRoleusers<FormapprovaldRoleusers> {
}
