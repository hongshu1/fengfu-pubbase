package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseDepartment;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 组织建模-部门档案
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_Department" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Department extends BaseDepartment<Department> {
}
