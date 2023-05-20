package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseRcvDocQcFormM;

/**
 * 质量管理-来料检
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "PL_RcvDocQcFormM" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class RcvDocQcFormM extends BaseRcvDocQcFormM<RcvDocQcFormM> {
}

