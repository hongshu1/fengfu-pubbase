package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseApprovalD;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 系统配置-审批流审批节点
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_ApprovalD" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class ApprovalD extends BaseApprovalD<ApprovalD> {
}

