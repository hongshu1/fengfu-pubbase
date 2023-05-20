package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseRcvDocDefect;

/**
 * 质量管理-来料异常品记录
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "PL_RcvDocDefect" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class RcvDocDefect extends BaseRcvDocDefect<RcvDocDefect> {
}

