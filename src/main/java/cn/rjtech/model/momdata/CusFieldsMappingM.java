package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseCusFieldsMappingM;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 系统配置-导入字段配置
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_CusFieldsMappingM" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class CusFieldsMappingM extends BaseCusFieldsMappingM<CusFieldsMappingM> {
}

