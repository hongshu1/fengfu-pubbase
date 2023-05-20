package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseRcvdocqcformdLine;

/**
 * 质量管理-来料检明细列值
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "PL_RcvDocQcFormD_Line" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class RcvdocqcformdLine extends BaseRcvdocqcformdLine<RcvdocqcformdLine> {
}

