package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseProposalcategory;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 禀议类型区分
 * Generated by JBolt.
 */
@TableBind(dataSource = "mes" , table = "Bas_ProposalCategory" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Proposalcategory extends BaseProposalcategory<Proposalcategory> {
}

