package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseProposalcategory;

/**
 * 禀议类型区分
 * Generated by JBolt.
 */
@TableBind(dataSource = "momdata" , table = "Bas_ProposalCategory" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Proposalcategory extends BaseProposalcategory<Proposalcategory> {
}

